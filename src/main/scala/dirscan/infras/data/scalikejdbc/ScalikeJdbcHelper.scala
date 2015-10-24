package dirscan.infras.data.scalikejdbc

import java.io.File

import scalikejdbc._

object ScalikeJdbcHelper {

  implicit var session: DBSession = null

  def init(fileName: String) = {
    GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(enabled = false, logLevel = 'info)
    ConnectionPool.singleton(s"jdbc:h2:file:./$fileName", "", "")
    // Class.forName("org.sqlite.JDBC")
    // ConnectionPool.singleton(s"jdbc:sqlite:$fileName", null, null)
    session = AutoSession
  }

  def construct(fileName: String) {
    init(fileName)
    sql"""create table files (
      id int not null primary key,
      name nvarchar(255),
      fullname nvarchar(511),
      level int,
      inode bigint,
      symbolic boolean,
      parent int,
      directory boolean)""".execute().apply
  }

  def purgeDatabase(fileName: String) = List(s"$fileName.mv.db", s"$fileName.trace.db") map (new File(_)) filter (_.exists()) foreach (_.delete())

  def reconstruct(fileName: String) {
    purgeDatabase(fileName)
    construct(fileName)
  }
}
