package com.dirscan.models

case class FileEntry(_name: String,
                     _inode: Long = 0,
                     _parent: Option[InodeEntry] = None,
                     _symbolic: Boolean = false,
                     _rootPrefix: String = "")
    extends InodeEntry(_name, _inode, _parent, _symbolic, _rootPrefix)
