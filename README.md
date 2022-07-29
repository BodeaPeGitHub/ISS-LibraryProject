# ISS - LibraryProject 

## Story:  
Project made for the Software Systems Engineering (ro. Ingineria Sistemelor Soft) Course.
The purpose of the course was to understand how to use diagrams when writing and designing code.   
The diagrams I used were 
* The diagram for use cases.
* Classes diagram.
* Sequence diagram.

## The problem: 
A library offers its subscribers a list of books that can be borrowed. 

For a subscriber we need information related to CNP, name, address, telephone number and a unique identification code him in the library.  
Each book can exist in one or more copies, identified by unique codes.  
To borrow a book, the terminals in the library are used, where a subscriber can authenticate with the help of the account.  
After logging in, the subscriber sees the list of available books and chooses the books he wants to borrow. 
In order to find books more easily, there must be a book filtering system by name, genre and reviews.
To return the books, there is only one work point, served by a librarian, to which each subscriber must bring the books.

## The solution:

As a solution, I developed a client-server desktop application in Java with the following technologies:
* gRPC for client-server communication of the application.
* Hibernate for database management.
* Gradle as an Automation Tool.
The data is kept in a SQLite database "library.db".

Here are a few screenshots of the final project: 

The log in window    
![Log in window](/Screenshots/log-in-window.png "Log in window")

***

The main window for the subscriber    
![Subscriber main window](/Screenshots/subscriber-main-window.png "Subscriber Main Window")

***

The main window for the libraian    
![Librarian main window](/Screenshots/librarian-main-window.png "Librarian main window")


## The diagrams

All the diagrams can be found in the "Models.mdj" that can be opened with "Star UML".   
The `Use Castes.pdf` file contains all the use cases that are implemented in the application.
