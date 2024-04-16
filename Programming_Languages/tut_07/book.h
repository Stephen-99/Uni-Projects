#pragma once

#include <string>
#include <cstdlib>
#include <iostream>

class Book
{   
private:
    int bookID;
    std::string bookName;
    std::string ISBN;
public:
    int getBookID();
    std::string getBookName();
    std::string getISBN();

    void setBookID(int);
    void setBookName(std::string);
    void setBookISBN(std::string);
    
    static bool idLessThan(Book, Book);
    static bool ISBNLessThan(Book, Book);
    static bool bookNameLessThan(Book, Book);
    
    friend std::ostream& operator<<(std::ostream& out, const Book& book);

    Book();
    Book(int id, std::string name, std::string isbn);
    ~Book();
};


/*Function pointer for comparing if first book is less than second.
    the 3 less than functions match this signature*/
typedef bool (*bookLessThan)(Book, Book);
