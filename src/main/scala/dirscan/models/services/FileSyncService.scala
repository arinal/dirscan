package dirscan.models.services

import dirscan.infras.data.files.FileSystemRepo
import dirscan.models.{InodeEntry, FileRepo}

class FileSyncService(val path: String, val targetRepo: FileRepo, val sourceRepo: FileRepo = FileSystemRepo) {
  def sync(diff: (Set[InodeEntry], Set[InodeEntry])) {
    val (shouldAdded, shouldRemoved) = diff
    shouldAdded foreach targetRepo.save
    shouldRemoved foreach (f => targetRepo.delete(f.id))
  }

  def transfer {
    val sourceSvc = FileTraverser(sourceRepo)
    val sources = sourceSvc.traverseRepo(path).sortBy(_.path.length)
    for (f <- sources) targetRepo.save(f)
  }

  def diff = {
    val fromFiles = FileTraverser().traverseRepo("src/test/temp-playground").toSet
    val fromDbs = targetRepo.all.toSet
    (fromFiles -- fromDbs, fromDbs -- fromFiles)
  }
}

object FileSyncService {
  def apply(path: String, targetRepo: FileRepo, sourceRepo: FileRepo = FileSystemRepo) =
    new FileSyncService(path, sourceRepo, targetRepo)
}
