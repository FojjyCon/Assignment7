package temple.edu.assignment7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Book> bookArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookArrayList = new ArrayList<>();

        bookArrayList.add(new Book("The Hobbit", "J.R.R. Tolkien"));
        bookArrayList.add(new Book("The Cat In the Hat", "Dr. Seuss"));
        bookArrayList.add(new Book("Pinocchio", "Carlo Collodi"));
        bookArrayList.add(new Book("Harry Potter and the Sorcerers Stone", "J.K Rowling"));
        bookArrayList.add(new Book("The Lion, The Witch, and The Wardrobe", "C.S Lewis"));
        bookArrayList.add(new Book("And Then There Were None", "Agatha Christie"));
        bookArrayList.add(new Book("Catcher in the Rye", "J.D. Salinger"));
        bookArrayList.add(new Book("Anne of Green Gables", "L.M. Montgomery"));
        bookArrayList.add(new Book("Twenty Thousand Leagues Under the Sea", "Jules Verne"));
        bookArrayList.add(new Book("The Eagle Has Landed", "Jack Higgins"));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_1, BookListFragment.newInstance(bookArrayList))
                .commit();

    }
}