package spec

import dirscan.models.{DirectoryEntry, FileEntry, FileService}
import org.scalatest._
import utils.TestUtils

class FileEntriesSpec extends FlatSpec with Matchers {
  "traversing in-memory tree-structures mimicking 'src/test/resources'" should "have length 11" in {
    val (dir1, dir2, file1, file2) = TestUtils.constructTree
    val fileList = FileService() traverse(dir1, dir2, file1, file2)

    fileList should have length 13
    fileList filter (_.isInstanceOf[FileEntry]) should have length 9
    fileList filter (_.isInstanceOf[DirectoryEntry]) should have length 4
  }

  it should "consists of two symbolic links" in {
    val (dir1, dir2, file1, file2) = TestUtils.constructTree
    val fileList = FileService() traverse(dir1, dir2, file1, file2)

    fileList filter (_.symbolic) should have length 2
  }

  "generate entries targeted 'src/test/resources' path" should "have length 11" in {
    val fileList = FileService() generate TestUtils.PLAYGROUND_PATH

    fileList should have length 13
    fileList filter (_.isInstanceOf[FileEntry]) should have length 9
    fileList filter (_.isInstanceOf[DirectoryEntry]) should have length 4
  }

  it should "have inode number on each element" in {
    val fileList = FileService() generate TestUtils.PLAYGROUND_PATH
    fileList foreach (_.inode should be > 100000L)
  }

  it should "consists of two symbolic links" in {
    val fileList = FileService() generate TestUtils.PLAYGROUND_PATH
    fileList filter (_.symbolic) should have length 2
  }
}
