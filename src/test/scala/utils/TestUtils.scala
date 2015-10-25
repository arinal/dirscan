package utils

import dirscan.infras.data.files.FileSystemRepo
import dirscan.models.services.FileTraverser
import dirscan.models.{DirectoryEntry, FileEntry}

import scala.annotation.tailrec

object TestUtils {

  val PLAYGROUND_PATH = "src/test/playground"
  val TEMP_PLAYGROUND_PATH = "src/test/temp-playground"

  val tempPlaygroundRoot = DirectoryEntry.fromPath(TEMP_PLAYGROUND_PATH)
  val fileSystemRepo = FileSystemRepo(TEMP_PLAYGROUND_PATH)
  
  def constructTree(parent: DirectoryEntry = DirectoryEntry()) = {
    val dir1 = parent.mkdir("dir1")
    val dir2 = parent.mkdir("dir2")
    val dir3 = parent.mkdir("dir3")
    val file1 = parent.mkfile("file1")
    val file2 = parent.mkfile("file2")

    val dir1file1 = dir1.mkfile("file1")
    val dir1file2 = dir1.mkfile("file2")
    val dir1dir1ln = dir1.mkdir("dir1ln", symbolic = true)
    dir1.add(dir1file1, dir1file2, dir1dir1ln)

    val dir2dir1 = dir2.mkdir("dir1")
    val dir2file1 = dir2.mkfile("file1")
    val dir2file2 = dir2.mkfile("file2")
    dir2.add(dir2dir1, dir2file1, dir2file2)

    val dir2Dir1File1 = dir2dir1.mkfile("file1")
    val dir2Dir1File2 = dir2dir1.mkfile("file2")
    val dir2Dir1File1Ln = dir2dir1.mkfile("file1ln", symbolic = true)
    dir2dir1.add(dir2Dir1File1, dir2Dir1File2, dir2Dir1File1Ln)

    (dir1, dir2, dir3, file1, file2)
  }

  def constructTree(parentPath: String): (DirectoryEntry, DirectoryEntry, DirectoryEntry, FileEntry, FileEntry) =
    constructTree(DirectoryEntry.fromPath(parentPath))
  
  def constructTempPlayground() {
    fileSystemRepo.save(tempPlaygroundRoot)

    @tailrec
    def recurse(n: Int, directory: DirectoryEntry) {
      if (n == 0) return
      val roots = TestUtils.constructTree(directory)
      directory.add(roots._1, roots._2, roots._3, roots._4, roots._5)
      recurse(n - 1, roots._3)
    }
    val (dir1, dir2, dir3, file1, file2) = TestUtils.constructTree(tempPlaygroundRoot)
    recurse(40, dir3)

    val fileList = FileTraverser traverse(dir1, dir2, dir3, file1, file2) sortBy (_.level)
    fileList foreach fileSystemRepo.save
  }
  
  def purgeTempPlayground() = FileTraverser.deleteRecursively(tempPlaygroundRoot.fullName, Some(fileSystemRepo))
}
