package com.dirscan.models

case class DirectoryEntry(_name: String,
                          _inode: Long = 0,
                          _parent: Option[InodeEntry] = None,
                          _symbolic: Boolean = false,
                          _rootPrefix: String = "")
    extends InodeEntry(_name, _inode, _parent, _symbolic, _rootPrefix) {

  private val childrenMap = scala.collection.mutable.Map[String, InodeEntry]()

  def children = childrenMap.values.toList.sortBy(_.name)

  def add(node: InodeEntry) {
    childrenMap(node.name) = node
    node.parent = Some(this)
  }
}

object DirectoryEntry {
  def fromPath(path: String, inode: Long = 0) = {
    new DirectoryEntry(_name = path.split('/').last,
                       _inode = inode,
                       _rootPrefix = path.substring(0, path.lastIndexOf('/')))
  }
}
