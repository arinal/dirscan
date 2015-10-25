package e2e

import dirscan.models.services.FileTraverser
import dirscan.models.{DirectoryEntry, FileEntry}
import dirscan.ui.konsole.UIWorkflow
import org.scalatest.{FlatSpec, Matchers}
import utils.TestUtils

class DirscanE2e extends FlatSpec with Matchers {
  val path = TestUtils.TEMP_PLAYGROUND_PATH
  TestUtils.constructTempPlayground()
  val fileSystemRepo = TestUtils.fileSystemRepo

  "dirscan application" should "behaved correctly" in {
    val workflow = new UIWorkflow(path, s"$path/test")
    val indexRepo = workflow.indexRepo

    workflow.executeNewDb()
    fileSystemRepo.byPath(s"$path/test.mv.db") should not be empty
    indexRepo.length should equal(575)

    workflow.executeUpdateDb()
    indexRepo.length should equal(575)

    println("Changing all of files named file1 into newfile1 and add sibling directory called 'newdir'")
    FileTraverser.traverseRepo(path, Some(fileSystemRepo))
      .filter(_.name == "file1")
      .foreach { f => {
        fileSystemRepo.delete(f.fullName)
        fileSystemRepo.save(FileEntry("newfile1", f.fullName.replace("file1", "newfile1")))
        fileSystemRepo.save(DirectoryEntry("newdir", f.fullName.replace("file1", "newdir")))
      }
    }

    workflow.executeUpdateDb()
    indexRepo.length should equal(739)

    workflow.executeUpdateDb()
    indexRepo.length should equal(739)

    FileTraverser.deleteRecursively(s"$path/dir3", Some(fileSystemRepo))
    workflow.executeUpdateDb()
    indexRepo.length should equal(18)

    TestUtils.purgeTempPlayground()
  }
}