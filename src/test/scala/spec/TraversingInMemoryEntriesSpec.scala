package spec

import dirscan.models.services.FileTraverser
import dirscan.models.{DirectoryEntry, FileEntry}
import org.scalatest._
import utils.TestUtils

class TraversingInMemoryEntriesSpec extends WordSpec with Matchers {

  "traversing in memory tree structured file representing 'src/test/resources" should {
    val (dir1, dir2, dir3, file1, file2) = TestUtils.constructTree(TestUtils.PLAYGROUND_PATH)
    val entries = FileTraverser.traverse(dir1, dir2, dir3, file1, file2)

    "have 15 entries"      in (entries should have length 15)
    "10 files"             in (entries filter (_.isInstanceOf[FileEntry]) should have length 10)
    "5 dirs"               in (entries filter (_.isInstanceOf[DirectoryEntry]) should have length 5)
    "2 symbolic links"     in (entries filter (_.symbolic) should have length 2)
    "max level of 5"       in (entries.map(_.level).sorted.last shouldBe 5)
    "consistent path each" in (entries foreach (_.fullName should startWith(TestUtils.PLAYGROUND_PATH)))
  }
}
