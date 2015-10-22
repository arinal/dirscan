import com.dirscan.infras.data.files.FileSystemRepo
import org.scalatest._

class FileSystemSpec extends FlatSpec with Matchers {
  "extracted file1 inode number" should "be greater than 100000" in {
    FileSystemRepo.inodeNumber(TestUtils.PLAYGROUND_PATH + "/file1") should be > 100000L
  }

  "get files using file system targeted at 'playground'" should "have length 4" in {
    FileSystemRepo.children(TestUtils.PLAYGROUND_PATH) should have length 4
  }

  "'dir1/dir1ln' and 'dir2/dir1/file1ln' symbolic status" should "be true" in {
    FileSystemRepo.symbolic(TestUtils.PLAYGROUND_PATH)
  }
}
