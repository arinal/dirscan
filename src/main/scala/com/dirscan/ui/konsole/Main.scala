package com.dirscan.ui.konsole

import com.dirscan.infras.data.files.FileSystemRepo

object Main extends App {
  println(FileSystemRepo.inodeNumber("src/test/resources/playground/file1"))
}

