import com.dirscan.models.{FileEntry, DirectoryEntry}

object TestUtils {
  def constructTree = {
    val dir1 = DirectoryEntry("dir1")
    val dir2 = DirectoryEntry("dir2")
    val file1 = FileEntry("file1")
    val file2 = FileEntry("file1")

    val dir1file1 = FileEntry("file1")
    val dir1file2 = FileEntry("file2")
    dir1.add(dir1file1)
    dir1.add(dir1file2)

    val dir2dir1 = DirectoryEntry("dir1")
    val dir2file1 = FileEntry("file1")
    val dir2file2 = FileEntry("file2")
    dir2.add(dir2dir1)
    dir2.add(dir2file1)
    dir2.add(dir2file2)

    val dir2Dir1File1 = FileEntry("file1")
    val dir2Dir1File2 = FileEntry("file2")
    dir2dir1.add(dir2Dir1File1)
    dir2dir1.add(dir2Dir1File2)

    (dir1, dir2, file1, file2)
  }
}
