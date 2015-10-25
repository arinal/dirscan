package spec

import dirscan.models.services.FileTraverser
import dirscan.models.{DirectoryEntry, FileEntry}
import org.scalatest._
import utils.TestUtils

class TraversingInMemoryEntriesSpec extends WordSpec with Matchers {

  "traversing in memory tree structured file representing 'src/test/resources" should {
    val (dir1, dir2, dir3, file1, file2) = TestUtils.constructTree(TestUtils.PLAYGROUND_PATH)
    val fileList = FileTraverser.traverse(dir1, dir2, dir3, file1, file2)

    "have 14 entries"        in (fileList should have length 14)
    "and 9 files"            in (fileList filter (_.isInstanceOf[FileEntry]) should have length 9)
    "and 5 directories"      in (fileList filter (_.isInstanceOf[DirectoryEntry]) should have length 5)
    "and two symbolic links" in (fileList filter (_.symbolic) should have length 2)
    "and max level of 5"     in (fileList.map(_.level).sorted.last shouldBe 5)
  }
}
