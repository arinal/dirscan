package dirscan.models

import commons.DfsVisitor
import dirscan.infras.data.files.FileSystemRepo

class FileService(fileRepo: FileRepo) {

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
    val nodes = fileRepo.childrenOf(rootNode)
    implicit val fanoutFile = (n: InodeEntry) => n match {
      case dir: DirectoryEntry => fileRepo.childrenOf(dir)
      case _: FileEntry => List()
    }
    DfsVisitor.traverse[InodeEntry](nodes)
  }
}

object FileService {
  def apply(fileRepo: FileRepo = FileSystemRepo) = new FileService(fileRepo)

  def chooseFullname(name: String = "", fullName: String = "",
                     parentName: String = "", rootPrefix: String = "", parentId: Int = 0) =
    if (fullName != "") fullName
    else if (parentId == 0 && rootPrefix != "") s"$rootPrefix/$name"
    else if (parentName != "") s"$parentName/$name"
    else name
}
