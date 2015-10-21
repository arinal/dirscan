package com.dirscan.infras.data.files

import java.io.File
import java.nio.file.{Paths, Files}
import java.nio.file.attribute.BasicFileAttributes

import com.dirscan.models._

object FileSystemRepo extends FileRepo {

  implicit def file2Entry(f: File): InodeEntry = {
    val inode = inodeNumber(f.getCanonicalPath)
    if (f.isDirectory) DirectoryEntry(f.getName, inode) else FileEntry(f.getName, inode)
  }

  def inodeNumber(path: String): Long = {
    val attr = Files.readAttributes(Paths.get(path), classOf[BasicFileAttributes])
    val key = attr.fileKey.toString
    val inode = key.substring(key.indexOf("ino=") + 4, key.indexOf(")"))
    inode.toLong
  }

  def children(directory: DirectoryEntry): List[InodeEntry] = {
    new File(directory.fullName).listFiles().foreach(n => directory.add(n))
    directory.children
  }

  def children(path: String) : List[InodeEntry] = {
    children(DirectoryEntry.fromPath(path))
  }
}
