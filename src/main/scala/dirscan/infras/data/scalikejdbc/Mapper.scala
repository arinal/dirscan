package dirscan.infras.data.scalikejdbc

import dirscan.models.{FileEntry, DirectoryEntry, InodeEntry}
import scalikejdbc.{DBSession, NoExtractor, SQL}

object Mapper {
  def map2File(map: Map[String, Any]): InodeEntry =
    if (map("DIRECTORY").asInstanceOf[Boolean])
      DirectoryEntry(map("NAME").asInstanceOf[String],
                     map("FULLNAME").asInstanceOf[String],
                     map("INODE").asInstanceOf[Long],
                     if (map.contains("PARENT")) map("PARENT").asInstanceOf[Int] else 0,
                     map("SYMBOLIC").asInstanceOf[Boolean],
                     _id = map("ID").asInstanceOf[Int],
                     _level = map("LEVEL").asInstanceOf[Int])
    else FileEntry(map("NAME").asInstanceOf[String],
                   map("FULLNAME").asInstanceOf[String],
                   map("INODE").asInstanceOf[Long],
                   if (map.contains("PARENT")) map("PARENT").asInstanceOf[Int] else 0,
                   map("SYMBOLIC").asInstanceOf[Boolean],
                   _id = map("ID").asInstanceOf[Int],
                   _level = map("LEVEL").asInstanceOf[Int])

  implicit def toNodeEntryList(sql: SQL[Nothing, NoExtractor])(implicit session:DBSession): List[InodeEntry] =
    sql.map(_.toMap()).list().apply() map map2File

  implicit def toNodeEntry(sql: SQL[Nothing, NoExtractor])(implicit session:DBSession): Option[InodeEntry] =
    sql.map(_.toMap()).single().apply() map map2File
}
