package com.dirscan.infras.data.files

import java.io.File
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Paths}

import com.dirscan.models._

object FileSystemRepo extends FileRepo {

  def symbolic(path: String): Boolean = Files.isSymbolicLink(Paths.get(path))

  def inodeNumber(path: String): Long = {
    val attr = Files.readAttributes(Paths.get(path), classOf[BasicFileAttributes])
    val key = attr.fileKey.toString
    val inode = key.substring(key.indexOf("ino=") + 4, key.indexOf(")"))
    inode.toLong
  }

  def children(directory: DirectoryEntry): List[InodeEntry] = {
    implicit def file2Entry(f: File): InodeEntry = {
      val inode = inodeNumber(f.getAbsolutePath)
      val parent = Some(directory)
      val sym = symbolic(f.getAbsolutePath)
      println(s"${f.getCanonicalPath} $sym")
      if (f.isDirectory) DirectoryEntry(f.getName, inode, parent, sym) else FileEntry(f.getName, inode, parent, sym)
    }
    new File(directory.fullName).listFiles.foreach(directory.add(_))
    directory.children
  }

  def children(path: String) : List[InodeEntry] = {
    children(DirectoryEntry.fromPath(path))
  }
}
