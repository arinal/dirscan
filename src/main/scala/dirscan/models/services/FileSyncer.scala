package dirscan.models.services

import dirscan.models.{FileRepo, InodeEntry}

class FileSyncer(path: String, indexRepo: FileRepo, actualRepo: FileRepo) {

  def allIndexedEntries = indexRepo.all

  def transfer() = actualRepo.all foreach indexRepo.save

  def diff = {
    val fromFiles = actualRepo.all.toSet
    val fromIndex = allIndexedEntries.toSet
    (fromFiles -- fromIndex, fromIndex -- fromFiles)
  }

  def patch(pluses: Set[InodeEntry], minuses: Set[InodeEntry]) {
    pluses foreach indexRepo.save
    minuses foreach (f => indexRepo.delete(f.id))
  }

  def patch(diffTuple: (Set[InodeEntry], Set[InodeEntry]) = this.diff) {
    val (pluses, minuses) = diffTuple
    patch(pluses, minuses)
  }
}