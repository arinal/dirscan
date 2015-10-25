package unit

import dirscan.models.FileEntry
import org.scalatest.{FlatSpec, Matchers}

class Equality extends FlatSpec with Matchers {
  "File entries equality'" should "be consistent" in {
    val node = FileEntry(_fullName = "a", _inode = 1)
    val sameNode = FileEntry(_fullName = "a", _inode = 1)
    val differentNode1 = FileEntry(_fullName = "b", _inode = 11)
    val differentNode2 = FileEntry(_fullName = "a", _inode = 12)

    node should === (sameNode)
    node should !== (differentNode1)
    node should !== (differentNode2)
  }
}
