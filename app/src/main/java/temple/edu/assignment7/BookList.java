package temple.edu.assignment7;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class BookList implements Parcelable {

    private ArrayList<Book> alBook;

    public BookList() {
        alBook = new ArrayList<>();
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

    protected BookList(Parcel in) {
        alBook = in.createTypedArrayList(Book.CREATOR);
    }

    public static final Creator<BookList> CREATOR = new Creator<BookList>() {
        @Override
        public BookList createFromParcel(Parcel in) {
            return new BookList(in);
        }

        @Override
        public BookList[] newArray(int size) {
            return new BookList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(alBook);
    }
}
