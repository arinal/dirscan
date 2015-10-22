package com.dirscan.models

trait FileRepo {
  def children(path: String) : List[InodeEntry]
  def children(directory: DirectoryEntry): List[InodeEntry]
}
