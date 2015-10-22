package dirscan.ui.konsole

import dirscan.infras.data.files.FileSystemRepo
import dirscan.models.{DirectoryEntry, InodeEntry}
import scalikejdbc._

object Main {

  def mainDb(args: Array[String]) {
    ConnectionPool.singleton("jdbc:h2:file:./db", "sa", "")

    val dir1 = FileSystemRepo.byPath("src/test/resources/playground/dir1").asInstanceOf[DirectoryEntry]
    val file1 = FileSystemRepo.byPath("src/test/resources/playground/dir1/file1", Some(dir1))
    dir1.add(file1)

    //  createFileTable
    //  save(dir1)
    //  save(file1)

    //  val fileMaps = sql"select id, name, inode, fullname, symbolic, directory, parent from files".map(_.toMap()).list().apply()
    //  fileMaps.map { m =>
    //    if (m("directory").asInstanceOf[Boolean]) {
    //      DirectoryEntry(m())
    //    }
    //
    //  })

    session.close()
  }

  implicit val session = AutoSession

  def createFileTable = {
    sql"""
          create table files (
            id int not null primary key,
            name nvarchar(50),
            inode bigint,
            fullname nvarchar(50),
            symbolic boolean,
            directory boolean,
            parent int,
            foreign key(parent) references files(id)
          )
      """.execute().apply
  }

  def save(file: InodeEntry): Int = {
    sql"""insert into files (id, name, inode, fullname, symbolic, directory, parent) values (
        ${file.id}, ${file.name},
        ${file.inode}, ${file.fullName},
        ${file.symbolic}, ${file.isInstanceOf[DirectoryEntry]},
        $file.parent.map(p => p.id).orNull
      )""".update().apply
  }
}
