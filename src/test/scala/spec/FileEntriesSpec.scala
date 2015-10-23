package spec

import dirscan.infras.data.files.FileSystemRepo
import dirscan.models.services.FileTraverser
import dirscan.models.{DirectoryEntry, FileEntry}
import org.scalatest._
import utils.TestUtils

class FileEntriesSpec extends FlatSpec with Matchers {
  "traversing in-memory tree-structures mimicking 'src/test/resources'" should "have length 11" in {
    val (dir1, dir2, file1, file2) = TestUtils.constructTree
    val fileList = FileTraverser() traverse(dir1, dir2, file1, file2)

    fileList should have length 13
    fileList filter (_.isInstanceOf[FileEntry]) should have length 9
    fileList filter (_.isInstanceOf[DirectoryEntry]) should have length 4
  }

  it should "consists of two symbolic links" in {
    val (dir1, dir2, file1, file2) = TestUtils.constructTree
    val fileList = FileTraverser() traverse(dir1, dir2, file1, file2)

    fileList filter (_.symbolic) should have length 2
  }

  "generate entries targeted 'src/test/resources' path" should "have length 11" in {
    val fileList = FileTraverser() traverseRepo TestUtils.PLAYGROUND_PATH

    fileList should have length 13
    fileList filter (_.isInstanceOf[FileEntry]) should have length 9
    fileList filter (_.isInstanceOf[DirectoryEntry]) should have length 4
  }

  it should "have inode number on each element" in {
    FileTraverser() traverseRepo TestUtils.PLAYGROUND_PATH foreach (_.inode should be > 100000L)
  }

  it should "consists of two symbolic links" in {
    FileTraverser() traverseRepo TestUtils.PLAYGROUND_PATH filter (_.symbolic) should have length 2
  }

  it should "all be prefixed with 'src/test/playground" in {
    FileSystemRepo childrenOf TestUtils.PLAYGROUND_PATH foreach (_.fullName should startWith ("src/test/playground"))
  }
}
