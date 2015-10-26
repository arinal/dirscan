package dirscan.ui.konsole

import dirscan.infras.data.files.FileSystemRepo
import dirscan.infras.data.scalikejdbc.{FileScalikeJdbcRepo, ScalikeJdbcHelper}
import dirscan.models.services.FileSyncer
import dirscan.models.{DirectoryEntry, InodeEntry}

class UIWorkflow(workingPath: String, databaseName: String) {

  ScalikeJdbcHelper.init(databaseName)
  val indexRepo = FileScalikeJdbcRepo()
  val syncer = new FileSyncer(workingPath, indexRepo, new FileSystemRepo(workingPath))

  def executeNewDb() {
    ScalikeJdbcHelper.reconstruct(databaseName)
    println(s"Storing list of items within the current directory into “$databaseName”……")
    syncer.transfer()
    println("Done.")
  }

  def executeListDb() {
    println(s"Listing stored items from “$databaseName”……")
    syncer.allIndexedEntries foreach printEntry
  }

  def executeDiff() {
    val (pluses, minuses, empty) = diffAndCheckEmptiness
    if (empty) return
    if (pluses.nonEmpty) println("Files to add:")
    pluses foreach printEntry
    if (minuses.nonEmpty) println("Files to remove:")
    minuses foreach printEntry
  }

  def executeUpdateDb() {
    val (pluses, minuses, empty) = diffAndCheckEmptiness
    if (empty) return
     println(s"Updating list of items within current directory into “$databaseName”……")
    syncer.patch(pluses, minuses)
    println("Done.")
  }

  def diffAndCheckEmptiness = {
    val (pluses, minuses) = syncer.diff
    val empty = pluses.isEmpty && minuses.isEmpty
    if (empty) println("There are no differences. Done.")
    (pluses, minuses, empty)
  }

  def printEntry(f: InodeEntry) {
    val fileType = if (f.symbolic) "sym" else if (f.isInstanceOf[DirectoryEntry]) "dir" else "file"
    println(s"($fileType) ${f.fullName}")
  }
}
