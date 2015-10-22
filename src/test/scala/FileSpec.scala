import com.dirscan.models.{DirectoryEntry, FileEntries, FileEntry}
import org.scalatest._

class FileSpec extends FlatSpec with Matchers {
  "traversing in-memory tree-structures mimicking 'src/test/resources'" should "have length 11" in {
    val (dir1, dir2, file1, file2) = TestUtils.constructTree
    val fileList = FileEntries().traverse(dir1, dir2, file1, file2)

    fileList should have length 11
    fileList.filter(n => n.isInstanceOf[FileEntry]) should have length 8
    fileList.filter(n => n.isInstanceOf[DirectoryEntry]) should have length 3
  }

  "generate entries targeted 'src/test/resources' path" should "have length 11" in {
    val fileList = FileEntries().generate("src/test/resources/playground")

    fileList should have length 11
    fileList.filter(n => n.isInstanceOf[FileEntry]) should have length 8
    fileList.filter(n => n.isInstanceOf[DirectoryEntry]) should have length 3
  }

  it should "have inode number on each element" in {
    val fileList = FileEntries().generate("src/test/resources/playground")
    fileList.map(_.inode should be > 100000L)
  }
}
