package dirscan.models

import commons.Entity

class InodeEntry(val name: String,
                 val fullName: String,
                 val inode: Long,
                 val parentId: Int,
                 val symbolic: Boolean) extends Entity {
  val id = (inode + fullName).hashCode
  val isRoot = parentId == 0

  private var _parent: Option[DirectoryEntry] = None
  def parent = _parent
  def parent_=(parent: Option[DirectoryEntry]): Unit = {
    val _id = parent.map(_.id).getOrElse(0)
    if (_id == parentId)
      _parent = parent
  }

  def path = parent.map(p => p.fullName).getOrElse("")
}
