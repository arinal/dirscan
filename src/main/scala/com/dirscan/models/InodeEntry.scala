package com.dirscan.models

class InodeEntry(var name: String,
                 var inode: Long,
                 var parent: Option[InodeEntry],
                 var symbolic: Boolean,
                 var rootPrefix: String) {
  def isRoot = parent == None
  def path = parent.map(p => p.fullName).getOrElse(rootPrefix)
  def fullName: String = s"$path/$name"
}
