package dirscan.models

case class DirectoryEntry(_name: String = "",
                          _fullName: String = "",
                          _inode: Long = 0,
                          _parentId: Int = 0,
                          _symbolic: Boolean = false,
                          _rootPrefix: String = "")
  extends InodeEntry(_name, FileService.chooseFullname(_name, _fullName, _rootPrefix, parentId = _parentId),
    _inode, _parentId, _symbolic) {

  private val childrenMap = scala.collection.mutable.Map[String, InodeEntry]()

  def children = childrenMap.values.toList.sortBy(_.name)

  def mkfile(name: String, inode: Long = 0, symbolic: Boolean = false) =
    FileEntry.fromParent(name, inode, Some(this), symbolic)

  def mkdir(name: String, inode: Long = 0, symbolic: Boolean = false) =
    DirectoryEntry.fromParent(Some(this), name, inode, symbolic)

  def add(node: InodeEntry) {
    if (node.parentId == id)
      childrenMap(node.name) = node
  }

  def add(nodes: InodeEntry*): Unit = nodes.foreach(n => add(n))
}

object DirectoryEntry {
  def fromPath(path: String, inode: Long = 0) = {
    new DirectoryEntry(_name = path.split('/').last, path, _inode = inode)
  }

  def fromParent(parent: Option[DirectoryEntry], name: String, inode: Long,
  symbolic: Boolean = false, rootPrefix: String = "") = {
    val fullName = FileService.chooseFullname(name = name, parentName = parent.map(_.fullName).getOrElse(""))
    val dir = new DirectoryEntry(name, fullName, inode, parent.map(_.id).getOrElse(0), symbolic)
    dir.parent = parent
    dir
  }
}
