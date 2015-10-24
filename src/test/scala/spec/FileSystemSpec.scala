package spec

import dirscan.infras.data.files.FileSystemRepo
import org.scalatest._
import utils.TestUtils

class FileSystemSpec extends FlatSpec with Matchers {

  val playgroundPath = TestUtils.PLAYGROUND_PATH

  "extracted file1 inode number" should "be greater than 100000" in {
    FileSystemRepo() inodeNumber playgroundPath + "/file1" should be > 100000L
  }

  "get files using file system targeted at 'playground'" should "have length 4" in {
    FileSystemRepo() childrenOf playgroundPath should have length 4
  }

  it should "all be prefixed with 'src/test/playground" in {
    FileSystemRepo() childrenOf playgroundPath foreach (_.fullName should startWith ("src/test/playground"))
  }

  "'dir1/dir1ln' and 'dir2/dir1/file1ln' symbolic status" should "be true" in {
    FileSystemRepo() symbolic playgroundPath + "/dir1/dir1ln" shouldBe true
    FileSystemRepo() symbolic playgroundPath + "/dir2/dir1/file1ln" shouldBe true
  }
}
