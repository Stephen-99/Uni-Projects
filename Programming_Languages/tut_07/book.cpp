#include "book.h"

using namespace std;

//Constructors
Book::Book()
{    
    bookID = 1;
    bookName = "Default book name";
    ISBN = "123456789ABCD";
}


Book::Book(int id, string name, string isbn)
{
    setBookID(id);
    setBookName(name);
    setBookISBN(isbn);
}

//getters
int Book::getBookID() { return bookID; }

string Book::getBookName() { return bookName; }

string Book::getISBN() { return ISBN; }

//setters
void Book::setBookID(int id)
{
    if (id >= 0)
    {
        bookID = id;
    }
    //TODO else, throw some error
}

void Book::setBookName(string name) { bookName = name; }

//could convert to a number and do all the relavent checks, but since its a string
//we're going to assume its in the correct format
void Book::setBookISBN(string isbn) { ISBN = isbn; }

//destructor
Book::~Book()
{
    //um nothing dynamically allocated so ah nothing to do here...
}

//overload output stream operator
ostream& operator<< (ostream& out, const Book& book)
{
    out << "Name: " << book.bookName;
    out << ", ID: " << book.bookID;
    out << ", ISBN: " << book.ISBN;
    return out;
}

//compare if the ID of a is less than the id of the other book
bool Book::idLessThan(Book a, Book b)
{
    bool lt = false;
    if (a.getBookID() < b.getBookID())
    {
        lt = true;
    }
    return lt;
}

//compares if isbn of a is alphabetically b4 the other book
bool Book::ISBNLessThan(Book a, Book b)
{
    bool lt = false;
    if (a.getISBN().compare(b.getISBN()) < 0)
    {
        lt = true;
    }
    return lt;
}

/*compares if book name of a is alphabetically b4 the other book
    NOTE: Lower case comes after upper case*/
bool Book::bookNameLessThan(Book a, Book b)
{
    bool lt = false;
    if (a.getBookName().compare(b.getBookName()) < 0)
    {
        lt = true;
    }
    return lt;
}
