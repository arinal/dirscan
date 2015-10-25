package unit

import dirscan.infras.data.files.FileSystemRepo
import dirscan.models.DirectoryEntry
import org.scalatest._
import utils.TestUtils

class FileSystemRepoSpec extends FlatSpec with Matchers {

  val playgroundPath = TestUtils.PLAYGROUND_PATH
  val repo = FileSystemRepo()

  "extracted file1 inode number" should "be greater than 100000" in {
     repo inodeNumber s"$playgroundPath/file1" should be > 100000L
  }

  "get file1" should "be fetched successfully" in {
    repo byPath s"$playgroundPath/file1" should not be empty
  }

  "get a nonexistent file" should "be empty" in {
    repo byPath s"$playgroundPath/phantom_of_the_file" shouldBe empty
  }

  "get files using file system targeted at 'playground'" should "get 5 entries" in {
    repo childrenOf playgroundPath should have length 5
  }

  it should "all be prefixed with 'src/test/playground" in {
    repo childrenOf playgroundPath foreach (_.fullName should startWith ("src/test/playground"))
  }

  "'dir1/dir1ln' and 'dir2/dir1/file1ln' symbolic status" should "be true" in {
    repo symbolic s"$playgroundPath/dir1/dir1ln" shouldBe true
    repo symbolic s"$playgroundPath/dir2/dir1/file1ln" shouldBe true
  }

  "creating file 'newfile'" should "be fetched successfully in" in {
    val parent = repo.byPath(playgroundPath).get.asInstanceOf[DirectoryEntry]
    val newFile = parent mkfile "newfile"
    repo save newFile
    repo byPath s"$playgroundPath/newfile" should not be empty
  }

  "fetching a deleted file 'newfile'" should "be empty" in {
    repo delete s"$playgroundPath/newfile"
    repo byPath s"$playgroundPath/newfile" shouldBe empty
  }
}
