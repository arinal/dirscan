package dirscan.models

import commons.TreeVisitor
import dirscan.infras.data.files.FileSystemRepo

class FileService(fileRepo: FileRepo) {

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

object FileService {
  def apply(fileRepo: FileRepo = FileSystemRepo) = new FileService(fileRepo)

  def transfer(path: String, targetRepo: FileRepo, sourceRepo: FileRepo = FileSystemRepo) = {
    val sourceSvc = FileService(sourceRepo)
    val sources = sourceSvc.traverseRepo(path).sortBy(_.path.length)
    for (f <- sources) targetRepo.save(f)
  }

  def chooseFullname(name: String = "", fullName: String = "",
                     parentName: String = "", rootPrefix: String = "", parentId: Int = 0) =
    if (fullName != "") fullName
    else if (parentId == 0 && rootPrefix != "") s"$rootPrefix/$name"
    else if (parentName != "") s"$parentName/$name"
    else name

  def removeParent(f: InodeEntry) = {
    f match {
      case DirectoryEntry(n, full, in, _, sym, rp, id) => DirectoryEntry(n, full, in, 0, sym, rp, id)
      case FileEntry(n, full, in, _, sym, rp, id) => FileEntry(n, full, in, 0, sym, rp, id)
    }
  }
}
