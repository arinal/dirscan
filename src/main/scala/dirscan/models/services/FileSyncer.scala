package dirscan.models.services

import dirscan.infras.data.files.FileSystemRepo
import dirscan.models.{FileRepo, InodeEntry}

class FileSyncer(path: String, indexRepo: FileRepo, actualRepo: FileRepo) {

  val traverser = FileTraverser(path, actualRepo)

  def prepareTargetRepo() = indexRepo.reconstruct()

  def transfer() {
    val sources = traverser.traverseRepo sortBy (_.level)
    for (f <- sources) indexRepo.save(f)
  }

  def allIndexedEntries = indexRepo.all

  def diff = {
    val fromFiles = traverser.traverseRepo.toSet
    val fromIndex = allIndexedEntries.toSet
    (fromFiles -- fromIndex, fromIndex -- fromFiles)
  }
  
  def patch(diffTuple: (Set[InodeEntry], Set[InodeEntry])) {
    val (pluses, minuses) = diffTuple
    pluses.toList sortBy (_.level) foreach indexRepo.save
    minuses.toList sortBy (-_.level) foreach (f => indexRepo.delete(f.id))
    // you can use this insted:
    // minuses.toList sorted Ordering.by((_: InodeEntry).level).reverse foreach (f => indexRepo.delete(f.id))
  }

  def patch(): Unit = patch(diff)
}

object FileSyncer {
  def apply(path: String, indexRepo: FileRepo, actualRepo: FileRepo = FileSystemRepo) =
    new FileSyncer(path, indexRepo, actualRepo)
}
