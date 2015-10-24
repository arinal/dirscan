package dirscan.models

import commons.Repository

trait FileRepo extends Repository[InodeEntry] {
  def byPath(path: String): Option[InodeEntry]
  def childrenOf(path: String): List[InodeEntry]
  def childrenOf(directory: DirectoryEntry): List[InodeEntry]
}