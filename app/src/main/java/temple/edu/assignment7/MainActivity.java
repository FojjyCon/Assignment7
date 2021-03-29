package temple.edu.assignment7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface {

    BookList bookList;
    BookDetailsFragment bookDetailsFragment;
    boolean container2Present;
    int bookIndex;

    private static final String ARG_BOOKLIST = "param1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container2Present = findViewById(R.id.container_2) != null;

        bookList = new BookList();

        bookList.add(new Book("The Hobbit", "J.R.R. Tolkien"));
        bookList.add(new Book("The Cat In the Hat", "Dr. Seuss"));
        bookList.add(new Book("Pinocchio", "Carlo Collodi"));
        bookList.add(new Book("Harry Potter and the Sorcerers Stone", "J.K Rowling"));
        bookList.add(new Book("The Lion, The Witch, and The Wardrobe", "C.S Lewis"));
        bookList.add(new Book("And Then There Were None", "Agatha Christie"));
        bookList.add(new Book("Catcher in the Rye", "J.D. Salinger"));
        bookList.add(new Book("Anne of Green Gables", "L.M. Montgomery"));
        bookList.add(new Book("Twenty Thousand Leagues Under the Sea", "Jules Verne"));
        bookList.add(new Book("The Eagle Has Landed", "Jack Higgins"));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_1, BookListFragment.newInstance(bookList))
                .commit();

        if (container2Present) {
            bookDetailsFragment = new BookDetailsFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_2, bookDetailsFragment)
                    .commit();
        }

        if (savedInstanceState != null) {
            //bookDetailsFragment = new BookDetailsFragment();
            bookList = (BookList) savedInstanceState.getParcelableArrayList(ARG_BOOKLIST);

        }
    }


    @Override
    public void bookClicked(int position) {

        if (!container2Present) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_1, BookDetailsFragment.newInstance(bookList.get(position)))
                    .addToBackStack(null)
                    .commit();
        } else {
            bookDetailsFragment.changeBook(bookList.get(position));
        }
        bookIndex = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(ARG_BOOKLIST, bookList);
    }

}