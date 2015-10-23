# dirscan
A simple program to store the list of all files, directories, and symbolic links within its current working directory in a H2 database file. Please note that following symbolic links isn't implemented.

This program can also print the list files stored in the database. Last but not least, the program will be able to update the database with new changes in the file system, e.g., if there is a new file(s) added or an existing file(s) deleted.

Implementation wise, dirscan will support three arguments:
- `new=<dbname>`: scans current working directory recursively and store the list of items within a H2 database `<dbname>`.
- `list=<dbname>`: list the content of the database `<dbname>`.
- `diff=<dbname>`: view the differences between file entries in actual filesystem and inside database `<dbname>`
- `update=<dbname>`: updates `<dbname>` by adding new files within the current working directory (if any) and deleting entries if it does not exist anymore.

## Getting started
You can sync the project file structures or fire up some testing. 

```
Clone repository from github and make it as current directory
$ git clone https://github.com/arinal/dirscan.git && cd dirscan

Run the test
$ ./activator test

Run the project via buildscript
$ ./activator run
Some menu will be appeared in console, feel free playing around with that :)

Assemble an executable file
$ ./activator assemble
Not only does this will create a jar file, but also will prepend it with shebang and shellscript so it can be executed directly without any java -jar command. 
Unfortunately, the file-size is 10MB plus. This is the so called über jar files, because all the required dependencies are included within. In future commit we will reducing it.
The executable file is located in target/scala-2.11/dirscan

Copy the file to current directory
$ cp target/scala-2.11/dirscan .

Execute it to view menu
$ ./dirscan

Or you can run it with arguments to bypass menu
$ ./dirscan new=test

```

## Example run
Below is the expected output of dirscan:
```
$ ./dirscan new=list
Storing list of items within the current directory into “list”…
Done.

$ ./dirscan list=list
Listing stored items from “list”:
.
(dir) ./config
(dir) ./config/htop
(file) ./.config/htop/htoprc
(file) ./.DS_Store
(dir) ./Keka
(dir) ./Keka/EML
(file) ./Keka/EML/1.eml
(sym) ./symlink_example

$ echo “text” > ./new_file # add a new file
$ rm -f ./symlink_example # delete a file
$ ./dirscan update=list # update the db
Updating list of items within current directory into “list”...
Done.
$ ./dirscan list=list # list the content of updated db
Listing stored items from “list”:
.
(dir) ./config
(dir) ./config/htop
(file) ./.config/htop/htoprc
(file) ./.DS_Store
(dir) ./Keka
(dir) ./Keka/EML
(file) ./Keka/EML/1.eml
(file) ./new_file
```
