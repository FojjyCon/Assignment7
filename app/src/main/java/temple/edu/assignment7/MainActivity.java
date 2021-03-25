package temple.edu.assignment7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface {

    BookList bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (getSupportFragmentManager().findFragmentById(R.id.container_1) instanceof BookListFragment) {

        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_1, BookListFragment.newInstance(bookList))
                .addToBackStack(null)
                .commit();

    }


    @Override
    public void bookClicked(int position) {

    }
}