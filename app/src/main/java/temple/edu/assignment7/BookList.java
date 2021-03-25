package temple.edu.assignment7;

import java.util.ArrayList;

public class BookList {

    private ArrayList<Book> alBook;


    public void add(Book book) {
        alBook.add(book);
    }

    public void remove(Book book) {
        alBook.remove(book);
    }

    public Book get(int num) {
        return alBook.get(num);
    }

    public int size() {
        return alBook.size();
    }

}
