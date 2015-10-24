package dirscan.infras.data.scalikejdbc

import java.io.File

import dirscan.models.{DirectoryEntry, FileEntry, FileRepo, InodeEntry}
import scalikejdbc._

class FileScalikeJdbcRepo(fileName: String) extends FileRepo {

  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(enabled = false, logLevel = 'info)
  ConnectionPool.singleton(s"jdbc:h2:file:./$fileName", "", "")

  // Class.forName("org.sqlite.JDBC")
  // ConnectionPool.singleton(s"jdbc:sqlite:$fileName", null, null)

  implicit val session = AutoSession

  def childrenOf(directory: DirectoryEntry): List[InodeEntry] = childrenOf(directory.fullName)

  def childrenOf(path: String): List[InodeEntry] =
    sql"""select id, name, inode, fullname, level, symbolic, directory, parent from files
          where parent = (select id from files where fullname = $path)"""
      .map(_.toMap()).list().apply().map(map2File)

  def childrenOf(id: Int): List[InodeEntry] =
    sql"""select id, name, inode, fullname, level, symbolic, directory, parent from files
         |where parent = $id""".stripMargin
      .map(_.toMap()).list().apply().map(map2File)

  def byPath(path: String): Option[InodeEntry] =
    sql"""select id, name, inode, fullname, level, symbolic, directory, parent from files where fullname = $path"""
      .map(_.toMap()).single().apply().map(map2File)

  def byId(id: Int): Option[InodeEntry] =
    sql"""select id, name, inode, fullname, level, symbolic, directory, parent from files where id = $id"""
      .map(_.toMap()).single().apply().map(map2File)

  def all: List[InodeEntry] =
    sql"select id, name, inode, fullname, level, symbolic, directory, parent from files order by level"
      .map(_.toMap()).list().apply().map(map2File)

  def delete(id: Int) = sql"delete from files where id = $id".update().apply

  def save(file: InodeEntry) =
    sql"""insert into files (id, name, inode, fullname, level, symbolic, directory, parent) values (
        ${file.id}, ${file.name},
        ${file.inode}, ${file.fullName},
        ${file.level}, ${file.symbolic},
        ${file.isInstanceOf[DirectoryEntry]},
        ${if (file.parentId == 0) null else file.parentId}
      )""".update().apply

  def createFileTable() =
    sql"""
          create table files (
            id int not null primary key,
            name nvarchar(255),
            fullname nvarchar(511),
            level int,
            inode bigint,
            symbolic boolean,
            parent int,
            directory boolean,
          )
      """.execute().apply

  def reconstruct() {
    List(s"$fileName.mv.db", s"$fileName.trace.db") map (new File(_)) filter (_.exists()) foreach (_.delete())
    createFileTable()
  }

  def map2File(map: Map[String, Any]): InodeEntry =
    if (map("DIRECTORY").asInstanceOf[Boolean])
      DirectoryEntry(map("NAME").asInstanceOf[String],
                     map("FULLNAME").asInstanceOf[String],
                     map("INODE").asInstanceOf[Long],
                     if (map.contains("PARENT")) map("PARENT").asInstanceOf[Int] else 0,
                     map("SYMBOLIC").asInstanceOf[Boolean],
                     _id = map("ID").asInstanceOf[Int],
                     _level = map("LEVEL").asInstanceOf[Int]
      )
    else FileEntry(map("NAME").asInstanceOf[String],
                   map("FULLNAME").asInstanceOf[String],
                   map("INODE").asInstanceOf[Long],
                   if (map.contains("PARENT")) map("PARENT").asInstanceOf[Int] else 0,
                   map("SYMBOLIC").asInstanceOf[Boolean],
                   _id = map("ID").asInstanceOf[Int],
                   _level = map("LEVEL").asInstanceOf[Int]
    )
}
