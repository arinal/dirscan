package dirscan.models.services

import dirscan.models.{DirectoryEntry, FileEntry, InodeEntry}

object Utils {
  def chooseFullname(name: String = "", fullName: String = "",
                     parentName: String = "", rootPrefix: String = "", parentId: Int = 0) =
    if (fullName != "") fullName
    else if (parentId == 0 && rootPrefix != "") s"$rootPrefix/$name"
    else if (parentName != "") s"$parentName/$name"
    else name

  def removeParent(f: InodeEntry) = f match {
    case DirectoryEntry(n, full, in, _, sym, rp, id, lvl) => DirectoryEntry(n, full, in, 0, sym, rp, id, lvl)
    case FileEntry(n, full, in, _, sym, rp, id, lvl) => FileEntry(n, full, in, 0, sym, rp, id, lvl)
  }

  class TupleAsEntry(tuple: (String, String, Long, Int, Boolean, Int, Int)) {
    val (n, fn, ind, parId, sym, id, lvl) = tuple
    def toDirectory = DirectoryEntry(n, fn, ind, parId, sym, _id = id, _level = lvl)
    def toFile = FileEntry(n, fn, ind, parId, sym, _id = id, _level = lvl)
    def dirOrFile(directory: Boolean) = if (directory) toDirectory else toFile
  }

  implicit def TupleAsEntry(tuple: (String, String, Long, Int, Boolean, Int, Int)): TupleAsEntry =
    new TupleAsEntry(tuple)
}