package dirscan.ui.konsole

import java.util.Scanner

object DirscanApp {

  val commandMap = Map(1 -> "new", 2 -> "list", 3 -> "diff", 4 -> "update")

  def main(args: Array[String]) {
    val workingPath = "."

    val (command, dbName) = if (args.isEmpty) fromMenu else parse(args(0))
    val workflow = new UIWorkflow(workingPath, dbName)

    if (command == commandMap(1)) workflow.executeNewDb()
    else if (command == commandMap(2)) workflow.executeListDb()
    else if (command == commandMap(3)) workflow.executeDiff()
    else if (command == commandMap(4)) workflow.executeUpdateDb()
  }

  def fromMenu: (String, String) = {
    val scanner = new Scanner(System.in)
    print("""1. Traverse files and save to new db
            |2. List all files indexed by db
            |3. List the differences between entries in filesystem and db
            |4. Scan and update db
            |Option: """.stripMargin)
    val command = commandMap(scanner.nextInt())
    print("Specify db name: ")
    (command, scanner.next())
  }

  def parse(assignment: String) = {
    val split = assignment.split('=')
    (split(0), split(1))
  }
}

