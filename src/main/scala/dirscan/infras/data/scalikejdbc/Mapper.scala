package dirscan.infras.data.scalikejdbc

import dirscan.models.InodeEntry
import dirscan.models.services.Utils._
import scalikejdbc.{DBSession, NoExtractor, SQL}

object Mapper {
  def map2File(map: Map[String, Any]): InodeEntry =
    (map("NAME").asInstanceOf[String],
      map("FULLNAME").asInstanceOf[String],
      map("INODE").asInstanceOf[Long],
      if (map.contains("PARENT")) map("PARENT").asInstanceOf[Int] else 0,
      map("SYMBOLIC").asInstanceOf[Boolean],
      map("ID").asInstanceOf[Int],
      map("LEVEL").asInstanceOf[Int]) dirOrFile map("DIRECTORY").asInstanceOf[Boolean]

  implicit def toNodeEntryList(sql: SQL[Nothing, NoExtractor])(implicit session:DBSession): List[InodeEntry] =
    sql.map(_.toMap()).list().apply() map map2File

  implicit def toNodeEntry(sql: SQL[Nothing, NoExtractor])(implicit session:DBSession): Option[InodeEntry] =
    sql.map(_.toMap()).single().apply() map map2File
}