package dirscan.ui.konsole

import java.io.File

import dirscan.infras.data.scalike.FileScalikeRepo
import dirscan.models.{DirectoryEntry, FileService}

object DirscanApp {

  def main(args: Array[String]): Unit = {

    val input = args(0)

    parse(input) match {
      case (command, dbname) => command match {
        case "newdb" =>
          executeNewDb(dbname)
        case "listdb" =>
          executeListDb(dbname)
        case "updatedb" =>
          executeUpdateDb(dbname)
      }
    }
  }

  def executeNewDb(dbname: String): Unit = {
    List(s"$dbname.mv.db", s"$dbname.trace.db") map (new File(_)) filter (_.exists()) foreach (_.delete())

    println(s"Storing list of items within the current directory into “$dbname”…")
    val dbRepo = new FileScalikeRepo(dbname)
    dbRepo.createFileTable
    FileService.transfer(".", dbRepo)
    println("Done.")
  }

  def executeListDb(dbname: String): Unit = {
    val dbrepo = new FileScalikeRepo(dbname)
    dbrepo.all.foreach { f =>
      val fileType = if (f.symbolic) "sym" else if (f.isInstanceOf[DirectoryEntry]) "dir" else "file"
      println(s"($fileType) ${f.fullName}")
    }
  }

  def executeUpdateDb(dbname: String): Unit = ???

  def parse(assignment: String) = {
    val split = assignment.split('=')
    (split(0), split(1))
  }
}
