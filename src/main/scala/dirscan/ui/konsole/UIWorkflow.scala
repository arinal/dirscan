package dirscan.ui.konsole

import dirscan.infras.data.scalikejdbc.FileScalikeJdbcRepo
import dirscan.models.{DirectoryEntry, InodeEntry}
import dirscan.models.services.FileSyncer

class UIWorkflow(workingPath: String, databaseName: String) {

  val syncer = FileSyncer(workingPath, new FileScalikeJdbcRepo(databaseName))

  def executeNewDb() {
    syncer.prepareTargetRepo()
    println(s"Storing list of items within the current directory into “$databaseName”…")
    syncer.transfer()
    println("Done.")
  }

  def executeListDb() = syncer.allIndexedEntries foreach printEntry

  def executeUpdateDb() {
    val (pluses, minuses) = syncer.diff

    if (pluses.nonEmpty) println("Add list..")
    pluses foreach printEntry
    if (minuses.nonEmpty) println("Remove list..")
    minuses foreach printEntry

    syncer.patch((pluses, minuses))
  }

  def printEntry(f: InodeEntry) {
    val fileType = if (f.symbolic) "sym" else if (f.isInstanceOf[DirectoryEntry]) "dir" else "file"
    println(s"($fileType) ${f.fullName}")
  }
}