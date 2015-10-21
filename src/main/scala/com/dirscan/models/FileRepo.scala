package com.dirscan.models

trait FileRepo {
  def children(directory: DirectoryEntry): List[InodeEntry]
}
