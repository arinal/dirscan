# dirscan
A simple program to store the list of all files, directories, and symbolic links within its current working directory in a H2 database file. Please note that following symbolic links isn't implemented.

This program can also print the list files stored in the database. Last but not least, the program will be able to update the database with new changes in the file system, e.g., if there is a new file(s) added or an existing file(s) deleted.

Implementation wise, dirscan will support three arguments:
- `newdb=<dbname>`: scans current working directory recursively and store the list of items within a SQLite database `<dbname>`.
- `listdb=<dbname>`: list the content of the database `<dbname>`.
- `updatedb=<dbname>`: updates `<dbname>` by adding new files within the current working directory (if any) and deleting entries if it does not exist anymore.

## Getting started
You can sync the project file structures or fire up some testing. 

```
Clone repository from github and make it as current directory
$ git clone https://github.com/arinal/dirscan.git && cd dirscan

Run the test
$ ./activator test

Run the project
$ ./activator run
Some menu will be appeared in console, just playing around with that :)
```

## Example run (not done yet)
Below is the expected output of dirscan:
```
$ ./dirscan newdb=list
Storing list of items within the current directory into “list.db”…
Done.

$ ./dirscan listdb=list
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
$ ./dirscan updatedb=list # update the db
Updating list of items within current directory into “list.db”...
Done.
$ ./dirscan listdb=list # list the content of updated db
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