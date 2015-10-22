package com.dirscan.models

class InodeEntry(val name: String,
                 val inode: Long,
                 val parent: Option[InodeEntry],
                 val symbolic: Boolean,
                 rootPrefix: String) {
  val isRoot = parent == None
  val path = parent.map(p => p.fullName).getOrElse(rootPrefix)
  val fullName: String = s"$path/$name"
}
