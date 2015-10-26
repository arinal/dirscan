package dirscan.infras.data.scalikejdbc

import dirscan.infras.data.scalikejdbc.Mapper._
import dirscan.models.{DirectoryEntry, FileRepo, InodeEntry}
import scalikejdbc._

class FileScalikeJdbcRepo(implicit session: DBSession) extends FileRepo {

  val allFields = sqls"id, name, inode, fullname, level, symbolic, directory, parent"
  val tableName = sqls"files"
  val mainQuery = sqls"select $allFields from $tableName"

  def all: List[InodeEntry] = sql"$mainQuery order by level"
  def byId(id: Int) = sql"$mainQuery where id = $id"
  def childrenOf(path: String) = sql"$mainQuery where parent = (select id from $tableName where fullname = $path)"
  def childrenOf(id: Int) = sql"$mainQuery where parent = $id"
  def childrenOf(directory: DirectoryEntry) = childrenOf(directory.id)
  def byPath(path: String) = sql"$mainQuery where fullname = $path"

  def length: Int = sql"select count(*) from $tableName".map(_.int(1)).single().apply().getOrElse(0)

  def delete(id: Int) = sql"delete from files where id = $id".update().apply()
  def delete(path: String) = sql"delete from files where fullname = $path".update().apply()

  def save(file: InodeEntry) = sql"""insert into $tableName ($allFields) values (
        ${file.id}, ${file.name},
        ${file.inode}, ${file.fullName},
        ${file.level}, ${file.symbolic},
        ${file.isInstanceOf[DirectoryEntry]},
        ${if (file.parentId == 0) null else file.parentId})""".update().apply()
}

object FileScalikeJdbcRepo {
  def apply(implicit session: DBSession = ScalikeJdbcHelper.session) = new FileScalikeJdbcRepo()
}
