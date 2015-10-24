package dirscan.infras.data.files

import java.io.File
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Paths}

import dirscan.models.{DirectoryEntry, FileEntry, FileRepo, InodeEntry}

object FileSystemRepo extends FileRepo {

  def byPath(path: String): Option[InodeEntry] = {
    implicit val f2e = (f: File) => {
      val (inode, sym) = inodeSym(f)
      if (f.isDirectory) DirectoryEntry.fromPath(path, inode, sym)
      else FileEntry(f.getName, path, inode, _symbolic = sym)
    }
    val file = new File(path)
    if (file.exists()) Some(file) else None
  }

  def childrenOf(directory: DirectoryEntry): List[InodeEntry] = {
    implicit val f2e = (f: File) => {
      val ((inode, sym), parent) = inodeSym(f.getAbsolutePath) -> Some(directory)
      if (f.isDirectory) DirectoryEntry.fromParent(parent, f.getName, inode, sym)
      else FileEntry.fromParent(f.getName, inode, parent, sym)
    }
    new File(directory.fullName).listFiles foreach (directory.add(_))
    directory.children
  }

  def childrenOf(path: String) : List[InodeEntry] = {
    val (inode, sym) = inodeSym(path)
    childrenOf(DirectoryEntry.fromPath(path, inode, sym))
  }

  def inodeNumber(path: String): Long = {
    val attr = Files.readAttributes(Paths.get(path), classOf[BasicFileAttributes])
    val key = attr.fileKey.toString
    val inode = key.substring(key.indexOf("ino=") + 4, key.indexOf(")"))
    inode.toLong
  }

  def symbolic(path: String): Boolean = Files.isSymbolicLink(Paths.get(path))
  def inodeSym(path: String) = (inodeNumber(path), symbolic(path))
  def inodeSym(file: File): (Long, Boolean) = inodeSym(file.getAbsolutePath)

  def byId(id: Int): Some[InodeEntry] = ???
  def all: List[InodeEntry] = ???
  def delete(id: Int): Unit = ???
  def save(entity: InodeEntry): Unit = ???
}
