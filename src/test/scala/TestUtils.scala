import com.dirscan.models.{DirectoryEntry, FileEntry}

object TestUtils {

  val PLAYGROUND_PATH: String = "src/test/resources/playground"

  def constructTree = {
    val dir1 = DirectoryEntry("dir1")
    val dir2 = DirectoryEntry("dir2")
    val file1 = FileEntry("file1")
    val file2 = FileEntry("file1")

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

    (dir1, dir2, file1, file2)
  }
}
