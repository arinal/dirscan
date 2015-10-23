package dirscan.models.services

import commons.TreeVisitor
import dirscan.infras.data.files.FileSystemRepo
import dirscan.models.{DirectoryEntry, FileEntry, FileRepo, InodeEntry}

class FileTraverser(fileRepo: FileRepo) {

  implicit val canFanout = (n: InodeEntry) => n.isInstanceOf[DirectoryEntry] && !n.symbolic

  def traverse(entries: InodeEntry*) = {
    implicit val fanout = (n: InodeEntry) => n match {
      case dir: DirectoryEntry => dir.children
      case _: FileEntry => List()
    }
    TreeVisitor.traverse[InodeEntry](entries.toList)
  }

  def traverseRepo(path: String): List[InodeEntry] = {
    val roots = fileRepo.childrenOf(path).map(FileService.removeParent)
    implicit val fanoutFile = (n: InodeEntry) => n match {
      case dir: DirectoryEntry => fileRepo.childrenOf(dir)
      case _: FileEntry => List()
    }
    TreeVisitor.traverse[InodeEntry](roots)
  }
}

object FileTraverser {
  def apply(fileRepo: FileRepo = FileSystemRepo) = new FileTraverser(fileRepo)
}