package dirscan.ui.konsole

import java.util.Scanner

import dirscan.infras.data.scalike.FileScalikeRepo
import dirscan.models._
import dirscan.models.services.FileSyncService

object DirscanApp {

  val workingPath = "src/test/temp-playground"

  def main(args: Array[String]) {
    val input = if (args.isEmpty) fromMenu else parse(args(0))
    input match {
      case (command, dbName) => command match {
        case "newdb" =>
          executeNewDb(dbName)
        case "listdb" =>
          executeListDb(dbName)
        case "updatedb" =>
          executeUpdateDb(dbName)
      }
    }
  }

  def fromMenu: (String, String) = {
    val map = Map(1 -> "newdb", 2 -> "listdb", 3 -> "updatedb")
    val scanner = new Scanner(System.in)

    print(
"""1. Traverse files and save to new db
2. List all files indexed by db
3. Scan and update db
Option: """)

    val command = map(scanner.nextInt())
    print("Specify db name: ")
    (command, scanner.next())
  }

  def executeNewDb(dbName: String) {
    val dbRepo = new FileScalikeRepo(dbName)
    dbRepo.deleteDbFile(dbName)
    dbRepo.createFileTable

    println(s"Storing list of items within the current directory into “$dbName”…")
    FileSyncService(workingPath, dbRepo).transfer
    println("Done.")
  }

  def executeListDb(dbName: String) {
    val dbRepo = new FileScalikeRepo(dbName)
    dbRepo.all foreach printEntry
  }

  def executeUpdateDb(dbName: String) {
    val syncer = FileSyncService(workingPath, new FileScalikeRepo(dbName))
    val diff = syncer.diff

    print("Adding..")
    diff._1 foreach printEntry
    print("Deleting..")
    diff._2 foreach printEntry

    syncer.sync(diff)
  }

  def printEntry(f: InodeEntry) {
    val fileType = if (f.symbolic) "sym" else if (f.isInstanceOf[DirectoryEntry]) "dir" else "file"
    println(s"($fileType) ${f.fullName}")
  }

  def parse(assignment: String) = {
    val split = assignment.split('=')
    (split(0), split(1))
  }
}
