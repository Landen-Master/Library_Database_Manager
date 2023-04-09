# Library_Database_Manager
This school project manages a database for a proposed local library, which asked to manage their music and video collections to support their inventory and circulation operation given a large set of requirements and deliverables, including creating a Java application to interface with the database. For full details of the project requirements, database information, schemas, and more, please read the Project Information folder.

## User Guide

This program is written in Java and SQLite and currently uses Java's SQL library.

### Getting started

To start, you will need to create an SQLite database, Library.db. This repository already has a fully functional library, however, if there are issues with the one provided, here is a guide to creating a new Library database.

#### Populating a new library database
All files mentioned can be found in the Data Files folder SQLite studio is also recommended within this process.

1. Create a new SQLite database
2. Copy the contents of **Create.txt** into the editor and run all commands. This will create all the necessary tables, indexes, and views within the database.
3. Copy the contents of **Populate.txt** into the editor and run all commands. This will populate each table with at least 20 example items. If there are issues populating the database, see **Instructions_For_Data_Population.txt**.
4. Copy the contents of **Queries.txt** into the editor. This provides example queries for navigating the database.

For more information on adding, updating, and deleting items from any table, see **Insert_And_Delete_Examples.txt**.

### Running the program
To run the program, run the **Database_Interface.java** file. ***If your database name is not Library.db, change DB_Name to your database's name***. From there, users have the option to search for information on tracks and artists, add new tracks and artists, order new movies to the library, update artist information, or view useful reports.

## For Future Developers

***src Folder*** - contains Java program's code.

- **Database_Interface.java** - The main class that provides the interface for the user. This allows the user to traverse through the options mentioned above by going through distinct menu paths. 

- **Database_Methods.java** - Class that holds methods that directly contact the database, such as initialization, querying, updating, and deleting information, alongside checking if an item exists.
    
***Project Information Folder*** - contains more detailed information on the project.

- **Report on the Library's Database and Managerial System.pdf** - a full report detailing all relevant information on the project, including normal forms, queries, syntax, and more.

***Data Files Folder*** - contains the necessary information to recreate the database (see Populating a new library database above).
