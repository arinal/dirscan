package dirscan.infras.data.files

import java.io.File
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Paths}

import dirscan.models.{DirectoryEntry, FileEntry, FileRepo, InodeEntry}

object FileSystemRepo extends FileRepo {

  def symbolic(path: String): Boolean = Files.isSymbolicLink(Paths.get(path))

  def inodeNumber(path: String): Long = {
    val attr = Files.readAttributes(Paths.get(path), classOf[BasicFileAttributes])
    val key = attr.fileKey.toString
    val inode = key.substring(key.indexOf("ino=") + 4, key.indexOf(")"))
    inode.toLong
  }
  
  def byPath(path: String, parent: Option[DirectoryEntry] = None): InodeEntry = {
    implicit val f2e = mkFile2Entry(parent)
    new File(path)
  }

  def childrenOf(directory: DirectoryEntry): List[InodeEntry] = {
    implicit val f2e = mkFile2Entry(Some(directory))
    new File(directory.fullName).listFiles.foreach(directory.add(_))
    directory.children
  }

  def childrenOf(path: String) : List[InodeEntry] = {
    childrenOf(DirectoryEntry.fromPath(path))
  }

  def byId(id: Int): InodeEntry = ???
  def all: List[InodeEntry] = ???
  def delete(id: Int): Unit = ???
  def save(entity: InodeEntry): Unit = ???

  def mkFile2Entry(parent: Option[DirectoryEntry]) = {
    f: File => {
      val inode = inodeNumber(f.getAbsolutePath)
      val sym = symbolic(f.getAbsolutePath)
      if (f.isDirectory) DirectoryEntry.fromParent(parent, f.getName, inode, sym)
      else FileEntry.fromParent(f.getName, inode, parent, sym)
    }
  }
}
