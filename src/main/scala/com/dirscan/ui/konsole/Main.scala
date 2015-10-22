package com.dirscan.ui.konsole

import com.dirscan.infras.data.files._

object Main extends App {
  val path = "src/test/resources/playground/dir2/dir1"
  FileSystemRepo.children(path).foreach(n => println(n.symbolic))
}
