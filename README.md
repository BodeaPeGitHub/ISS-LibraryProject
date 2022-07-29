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

<div style="width:250px ; height:400px ; border=2px solid black" >
  ![Log in window](/ISS-LibraryProject/blob/master/Screenshots/log-in-window.png?raw=True "Log in window")
</div>

<div style="width:250px ; height:400px ; border=2px solid black" >
  ![Log in window](/ISS-LibraryProject/blob/master/Screenshots/log-in-window.png?raw=True "Log in window")
</div>

<div style="width:250px ; height:400px ; border=2px solid black" >
  ![Log in window](/ISS-LibraryProject/blob/master/Screenshots/log-in-window.png?raw=True "Log in window")
</div>
