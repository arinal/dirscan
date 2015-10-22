package dirscan.infras.data.scalike

import dirscan.models.{DirectoryEntry, FileRepo, InodeEntry}

object FileScalikeRepo extends FileRepo {

  def childrenOf(directory: DirectoryEntry): List[InodeEntry] = ???

  def childrenOf(path: String) : List[InodeEntry] = ???

  override def byId(id: Int): InodeEntry = ???

  override def all: List[InodeEntry] = ???

  override def delete(id: Int): Unit = ???

  override def save(entity: InodeEntry): Unit = ???
}
