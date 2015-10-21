# dirscan
A simple program to store the list of all files, directories, and symbolic links within its current working directory in a SQLite database file. Please note that following the symbolic links isn't implemented.

This program can also print the list files stored in the database. Last but not least, the program will be able to update the database with new changes in the file system, e.g., if there is a new file(s) added or an existing file(s) deleted.

Implementation wise, dirscan will support three arguments:
- `--new-db=<dbname>`: scans current working directory recursively and store the list of items within a SQLite database `<dbname>`.
- `--list-from-db=<dbname>`: list the content of the database `<dbname>`.
- `--update-db=<dbname>`: updates `<dbname>` by adding new files within the current working directory (if any) and deleting entries if it does not exist anymore.

## Example Run
Below is given the expected output of dirscan:
```
$ ./dirscan --new-db=list.db
Storing list of items within the current directory into “list.db”…
Done.

$ ./dirscan --list-from-db=list.db
Listing stored items from “list.db”:
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
$ ./dirscan --update-db=list.db # update the db
Updating list of items within current directory into “list.db”...
Done.
$ ./dirscan --list-from-db=list.db # list the content of updated db
Listing stored items from “list.db”:
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
