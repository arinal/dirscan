# dirscan

[![Circle CI](https://circleci.com/gh/arinal/dirscan.svg?style=svg)](https://circleci.com/gh/arinal/dirscan)

A simple program to store the list of all files, directories, and symbolic links within its current working directory in
a H2 database file. Please note that following directories referenced by symbolic links aren't implemented.

This program can also print the list files stored in the database. Last but not least, the program will be able to
update the database with new changes in the file system, e.g., if there is a new file(s) added or an existing file(s)
deleted.

Implementation wise, dirscan will support four arguments:
- `new=<dbname>`: scans current working directory recursively and store the list of items within a H2 database
`<dbname>`.
- `list=<dbname>`: list the content of the database `<dbname>`.
- `diff=<dbname>`: view the differences between file entries in actual filesystem and inside database `<dbname>`
- `update=<dbname>`: updates `<dbname>` by adding new files within the current working directory (if any) and deleting
entries if it does not exist anymore.

## Getting started
Clone repository from github and make it as current directory.
```
$ git clone https://github.com/arinal/dirscan.git && cd dirscan
```
Run the test.
```
$ ./activator test
```

Run the project via build script.
```
$ ./activator run
```
Some menu will be appeared in console, feel free playing around with it.

Assemble an executable file.
```
$ ./activator assemble
```
Not only does this will create a jar file, but also will prepend it with shebang and shell script to make it runnable
without extra `java -jar`. Unfortunately, the file-size is 10MB plus because all the required dependencies are included
within. This is the so called über jar files. In future commits, maybe we will handle this size problem. The executable
file is located in `target/scala-2.11/dirscan`.

Copy the file into current directory.
```
$ cp target/scala-2.11/dirscan .
```

Run via the appearing menu.
```
$ ./dirscan
```

Or you can run it with an argument to bypass menu.
```
$ ./dirscan new=test
```

## Example run
Is dirscan mature enough? Let's clarify it by testing on directory with more than 10 thousands files inside which happened
to be our very own project root folder. Let's assume that we've already built the project and copied dirscan into project's
root folder.

Examine how many files inside the root project's folder
```
$ find | wc -l
13059
```
Okay, that's amounty! Let's have all of those file entries into our db!
```
$ ./dirscan new=db
Storing list of items within the current directory into “db”……
Done.
```
Try to list top 100 files from index
```
$ ./dirscan list=db | head -100
Listing stored items from “list”……
(file) ./activator.bat
(dir) ./project
...
...
(dir) ./.git/objects/f4
(dir) ./.git/objects/25
```

> Note that we'are not printing all of the files to the screen, instead we've just redirected the output to wc (word count).
> Console screen printings are much slower processes even compared to device writings due to the nature of tty drivers.
> Plus, you don't want your precious screen bloated with garbage do you?

At this moment, we're expecting a zero differences between filesystem and index.
```
$ ./dirscan diff=db
There are no differences. Done.
```
Now let's clean and compile our project to make some file differences.
```
$ ./activator clean assembly
```
Observe how many diffs, remember not to print all the differences to the screen!
```
$ ./dirscan diff=db | wc -l
23760
```
Phew! 20 thousands files to go, intermixed with files to add and delete! Cross our fingers and run sync..
```
$ ./dirscan update=db
Updating list of items within current directory into “db”……
Done.
```
Hmm.. that was fast, but are our index really synchronized? Let's run diff again.

```
$ ./dirscan diff=db
There are no differences. Done.
```
We've just synchronized 20 thousands files successfully. Don't worry, this kind of test is always executed automatically
on every github push, notice a green 'passed' CircleCI badge on top of this document.
