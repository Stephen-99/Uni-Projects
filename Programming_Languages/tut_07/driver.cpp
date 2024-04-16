#include "quicksort.h"

int main()
{
    int NUMBOOKS = 10;

    Book **books = new Book*[NUMBOOKS];
    books[0] = new Book(10, "Shaun the sheep", "979-4-21-189012-0");
    books[1] = new Book(7, "Not sure", "979-3-13-123456-1");
    books[2] = new Book(9, "The Next Big Thing", "979-7-23-123456-1");
    books[3] = new Book(4, "How to study for Exams", "979-3-13-223456-1");
    books[4] = new Book(2, "The joy in living", "978-3-13-123456-1");
    books[5] = new Book(6, "Welcome to Australia", "978-2-13-123456-1");
    books[6] = new Book(3, "The Perfect Dream", "978-7-13-123456-1");
    books[7] = new Book(5, "Lying well", "978-1-13-123456-1");
    books[8] = new Book(8, "When to speak the truth", "978-5-13-123456-1");
    books[9] = new Book(1, "Learn C++ in 24 hours", "979-8-13-123456-1");

    std::cout << "Before sorting:\n";
    printArray(books, NUMBOOKS);

    quicksort(books, NUMBOOKS, Book::idLessThan);
    std::cout << "After sorting: by ID:\n";
    printArray(books, NUMBOOKS);

    quicksort(books, NUMBOOKS, Book::ISBNLessThan);
    std::cout << "\n\nAfter sorting by ISBN:\n";
    printArray(books, NUMBOOKS);

    quicksort(books, NUMBOOKS, Book::bookNameLessThan);
    std::cout << "\n\nAfter sorting by name:\n";
    printArray(books, NUMBOOKS);

    for (int ii=0; ii < NUMBOOKS; ii++)
    {
        delete books[ii];
    }
    delete[] books;


    return 0;
}
