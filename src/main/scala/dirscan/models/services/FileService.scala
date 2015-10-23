package dirscan.models.services

import dirscan.models.{DirectoryEntry, FileEntry, InodeEntry}

object FileService {
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

