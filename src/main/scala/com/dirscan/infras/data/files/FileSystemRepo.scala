package com.dirscan.infras.data.files

import java.io.File
import java.nio.file.{Paths, Files}
import java.nio.file.attribute.BasicFileAttributes

import com.dirscan.models._

object FileSystemRepo extends FileRepo {

  def inodeNumber(path: String): Long = {
    val attr = Files.readAttributes(Paths.get(path), classOf[BasicFileAttributes])
    val key = attr.fileKey.toString
    val inode = key.substring(key.indexOf("ino=") + 4, key.indexOf(")"))
    inode.toLong
  }

  def children(directory: DirectoryEntry): List[InodeEntry] = {
    implicit def file2Entry(f: File): InodeEntry = {
      val inode = inodeNumber(f.getCanonicalPath)
      val parent = Some(directory)
      if (f.isDirectory) DirectoryEntry(f.getName, inode, parent) else FileEntry(f.getName, inode, parent)
    }
    new File(directory.fullName).listFiles().foreach(f => directory.add(f))
    directory.children
  }

  def children(path: String) : List[InodeEntry] = {
    children(DirectoryEntry.fromPath(path))
  }
}
