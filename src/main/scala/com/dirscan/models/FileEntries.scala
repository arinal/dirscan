package com.dirscan.models

import com.dirscan.commons.DfsVisitor
import com.dirscan.infras.data.files.FileSystemRepo

class FileEntries(fileRepo: FileRepo) {

  implicit val canFanout = (n: InodeEntry) => n.isInstanceOf[DirectoryEntry] && !n.symbolic

  def traverse(entries: InodeEntry*) = {
    implicit val fanout = (n:InodeEntry) => n match {
      case dir: DirectoryEntry => dir.children
      case _: FileEntry => List()
    }
    DfsVisitor.traverse[InodeEntry](entries.toList)
  }

  def generate(path: String): List[InodeEntry] = {
    val rootNode = DirectoryEntry.fromPath(path)
    val nodes = fileRepo.children(rootNode)
    implicit val fanoutFile = (n: InodeEntry) => n match {
      case dir: DirectoryEntry => fileRepo.children(dir)
      case _: FileEntry => List()
    }
    DfsVisitor.traverse[InodeEntry](nodes)
  }
}

object FileEntries {
  def apply(fileRepo: FileRepo = FileSystemRepo) = new FileEntries(fileRepo)
}
