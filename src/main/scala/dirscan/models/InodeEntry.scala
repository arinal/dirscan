package dirscan.models

import commons.Entity

class InodeEntry(val name: String,
                 val fullName: String,
                 val inode: Long,
                 val parentId: Int,
                 val symbolic: Boolean,
                 _id: Int) extends Entity {
  val id = if (_id == 0) (inode + fullName).hashCode else _id
  val isRoot = parentId == 0

  private var _parent: Option[DirectoryEntry] = None
  def parent = _parent
  def parent_= (parent: Option[DirectoryEntry]) {
    val _id = parent.map(_.id).getOrElse(0)
    if (_id == parentId)
      _parent = parent
  }

  def path = parent.map(p => p.fullName).getOrElse("")
}
