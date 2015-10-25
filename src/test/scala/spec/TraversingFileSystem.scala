package spec

import dirscan.models.services.FileTraverser
import dirscan.models.{DirectoryEntry, FileEntry}
import org.scalatest.{Matchers, WordSpec}
import utils.TestUtils

class TraversingFileSystem extends WordSpec with Matchers {

  "generate entries based on playground path" should {
    val rootPath = TestUtils.PLAYGROUND_PATH
    val traversedFiles = FileTraverser traverseRepo rootPath

    "generate 14 entries" in { traversedFiles should have length 14 }
    "have 9 files" in { traversedFiles filter (_.isInstanceOf[FileEntry]) should have length 9 }
    "have 5 dirs" in { traversedFiles filter (_.isInstanceOf[DirectoryEntry]) should have length 5 }
    "have inode number on each element" in { traversedFiles.foreach (_.inode should be > 100000L) }
    "consists of two symbolic links" in { traversedFiles.filter (_.symbolic) should have length 2 }
    "all be prefixed with 'src/test/playground'" in { traversedFiles foreach (_.fullName should startWith(rootPath)) }
  }
}
