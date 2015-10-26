package spec

import dirscan.models.services.FileTraverser
import dirscan.models.{DirectoryEntry, FileEntry}
import org.scalatest.{Matchers, WordSpec}
import utils.TestUtils

class TraversingFileSystem extends WordSpec with Matchers {

  "generated entries based on playground path" should {
    val rootPath = TestUtils.PLAYGROUND_PATH
    val entries = FileTraverser traverseRepo rootPath

    "have 15 entries"      in (entries should have length 15)
    "10 files"             in (entries filter (_.isInstanceOf[FileEntry]) should have length 10)
    "5 dirs"               in (entries filter (_.isInstanceOf[DirectoryEntry]) should have length 5)
    "2 symbolic links"     in (entries filter (_.symbolic) should have length 2)
    "max level of 5"       in (entries.map(_.level).sorted.last shouldBe 5)
    "consistent path each" in (entries foreach (_.fullName should startWith(rootPath)))
    "valid inode each"     in (entries foreach (_.inode should be > 100000L))
  }
}
