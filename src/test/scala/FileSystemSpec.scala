import com.dirscan.models.{DirectoryEntry, FileEntries, FileEntry}
import com.dirscan.infras.data.files.FileSystemRepo
import org.scalatest._

class FileSystemSpec extends FlatSpec with Matchers {
  "extracted file1 inode number" should "be greater than 100000" in {
    FileSystemRepo.inodeNumber("src/test/resources/playground/file1") should be > 100000L
  }

  "get files using file system targeted at 'playground'" should "sized 4" in {
    val fileList = FileSystemRepo.children("src/test/resources/playground")

    fileList should have length 4
  }
}
