package dirscan.models

import dirscan.models.services.Utils

case class FileEntry(_name: String = "",
                     _fullName: String = "",
                     _inode: Long = 0,
                     _parentId: Int = 0,
                     _symbolic: Boolean = false,
                     _rootPrefix: String = "",
                     _id: Int = 0, _level: Int = 0)
  extends InodeEntry(
    _name, Utils.chooseFullname(_name, _fullName, "", _rootPrefix, _parentId),
    _inode, _parentId, _symbolic, _id, _level)

object FileEntry {
  def fromParent(name: String, inode: Long, parent: Option[DirectoryEntry],
            symbolic: Boolean = false, rootPrefix: String = "") = {
    val fullName = Utils.chooseFullname(name = name, parentName = parent map (_.fullName) getOrElse "")
    val dir = new FileEntry(name, fullName, inode, parent map (_.id) getOrElse 0, symbolic)
    dir.parent = parent
    dir
  }
}
