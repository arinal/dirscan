package dirscan.infras.data.scalike

import dirscan.models.{DirectoryEntry, FileEntry, FileRepo, InodeEntry}
import scalikejdbc._

class FileScalikeRepo(fileName: String) extends FileRepo {

  ConnectionPool.singleton(s"jdbc:h2:file:./$fileName", "", "")
  implicit val session = AutoSession

  def childrenOf(directory: DirectoryEntry): List[InodeEntry] = childrenOf(directory.fullName)

  def childrenOf(path: String): List[InodeEntry] = {
    sql"""select id, name, inode, fullname, symbolic, directory, parent from files
          where parent = (select id from files where fullname = $path)"""
      .map(_.toMap()).list().apply().map(map2File)
  }

  def childrenOf(id: Int): List[InodeEntry] = {
    sql"""select id, name, inode, fullname, symbolic, directory, parent from files
         |where parent = $id""".stripMargin
      .map(_.toMap()).list().apply().map(map2File)
  }

  def byPath(path: String): Option[InodeEntry] = {
    val file = sql"""select id, name, inode, fullname, symbolic, directory, parent from files where fullname = $path"""
      .map(_.toMap()).single().apply().map(map2File)
    file
  }

  def byId(id: Int): Option[InodeEntry] = {
    sql"""select id, name, inode, fullname, symbolic, directory, parent from files where id = $id"""
      .map(_.toMap()).single().apply().map(map2File)
  }

  def all: List[InodeEntry] = {
    sql"select id, name, inode, fullname, symbolic, directory, parent from files order by length(fullname)"
      .map(_.toMap()).list().apply().map(map2File)
  }

  def delete(id: Int) = sql"delete from files where id = $id".update().apply

  def save(file: InodeEntry) {
    sql"""insert into files (id, name, inode, fullname, symbolic, directory, parent) values (
        ${file.id}, ${file.name},
        ${file.inode}, ${file.fullName},
        ${file.symbolic}, ${file.isInstanceOf[DirectoryEntry]},
        ${if (file.parentId == 0) null else file.parentId}
      )""".update().apply
  }

  def createFileTable = {
    sql"""
          create table files (
            id int not null primary key,
            name nvarchar(128),
            fullname nvarchar(255),
            inode bigint,
            symbolic boolean,
            parent int,
            directory boolean,
            foreign key(parent) references files(id)
          )
      """.execute().apply
  }

  def map2File(map: Map[String, Any]): InodeEntry =
    if (map("DIRECTORY").asInstanceOf[Boolean])
      DirectoryEntry(map("NAME").asInstanceOf[String],
        map("FULLNAME").asInstanceOf[String],
        map("INODE").asInstanceOf[Long],
        if (map.contains("PARENT")) map("PARENT").asInstanceOf[Int] else 0,
        map("SYMBOLIC").asInstanceOf[Boolean],
        _id = map("ID").asInstanceOf[Int])
    else FileEntry(map("NAME").asInstanceOf[String],
      map("FULLNAME").asInstanceOf[String],
      map("INODE").asInstanceOf[Long],
      if (map.contains("PARENT")) map("PARENT").asInstanceOf[Int] else 0,
      map("SYMBOLIC").asInstanceOf[Boolean],
      _id = map("ID").asInstanceOf[Int])
}
