package dirscan.models.services

import java.io.File

import commons.TreeVisitor
import dirscan.infras.data.files.FileSystemRepo
import dirscan.models.{DirectoryEntry, FileEntry, FileRepo, InodeEntry}

object FileTraverser {

  implicit val canFanout = (n: InodeEntry) => n.isInstanceOf[DirectoryEntry] && !n.symbolic

  def traverse(entries: InodeEntry*) = {
    implicit val fanout = (n: InodeEntry) => n match {
      case dir: DirectoryEntry => dir.children
      case _: FileEntry => List()
    }
    TreeVisitor.traverse[InodeEntry](entries.toList)
  }

  def traverseRepo(path: String, someFileRepo: Option[FileRepo] = None, includeParent: Boolean = false) = {
    val fileRepo = someFileRepo getOrElse FileSystemRepo(path)
    val roots = fileRepo childrenOf path
    implicit val fanoutFile = (n: InodeEntry) => n match {
      case dir: DirectoryEntry => fileRepo childrenOf dir
      case _: FileEntry => List()
    }
    val results = TreeVisitor.traverse[InodeEntry](roots)
    if (includeParent) fileRepo.byPath(path).get :: results else results
  }

  def deleteRecursively(path: String, someFileRepo: Option[FileRepo] = None) =
    traverseRepo(path, someFileRepo, includeParent = true).sortBy(-_.level).foreach(f => new File(f.fullName).delete())
}
