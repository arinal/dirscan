package dirscan.ui.konsole

import java.util.Scanner

object DirscanApp {

  def main(args: Array[String]) {
    val workingPath = "."

    val (command, dbName) = if (args.isEmpty) fromMenu else parse(args(0))
    val workflow = new UIWorkflow(workingPath, dbName)

    if (command == "newdb") workflow.executeNewDb()
    else if (command == "listdb") workflow.executeListDb()
    else if (command == "updatedb") workflow.executeUpdateDb()
  }

  def fromMenu: (String, String) = {
    val map = Map(1 -> "newdb", 2 -> "listdb", 3 -> "updatedb")
    val scanner = new Scanner(System.in)
    print(
"""      1. Traverse files and save to new db
      2. List all files indexed by db
      3. Scan and update db
    Option: """)
    val command = map(scanner.nextInt())
    print("Specify db name: ")
    (command, scanner.next())
  }

  def parse(assignment: String) = {
    val split = assignment.split('=')
    (split(0), split(1))
  }
}

