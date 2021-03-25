package temple.edu.assignment7;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class BookList extends ArrayList<Parcelable> {

    private ArrayList<Book> alBook;

    public BookList() {
        alBook = new ArrayList<Book>();
    }

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

    @NonNull
    @Override
    public Stream<Parcelable> stream() {
        return null;
    }

}
