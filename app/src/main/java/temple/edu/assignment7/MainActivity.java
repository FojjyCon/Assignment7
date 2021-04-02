package temple.edu.assignment7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface {

    BookList bookList;
    Book book;
    BookDetailsFragment bookDetailsFragment;
    boolean container2Present;
    int bookIndex;

    private static final String ARG_BOOKLIST = "param1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            //bookDetailsFragment = new BookDetailsFragment();
            //bookList = (BookList) savedInstanceState.getParcelableArrayList(ARG_BOOKLIST);
            book = savedInstanceState.getParcelable(ARG_BOOKLIST);
        }

        container2Present = findViewById(R.id.container_2) != null;

        bookList = new BookList();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_1, BookListFragment.newInstance(bookList))
                .commit();

        // checking on container 2
        Intent newIntent = getIntent();
        if (container2Present) {
            bookDetailsFragment = new BookDetailsFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_2, bookDetailsFragment)
                    .commit();
        } else {
            if (newIntent.hasExtra("Books")) {
                Bundle extras = getIntent().getExtras();
                bookList = extras.getParcelable("Books");
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_1, BookListFragment.newInstance(bookList))
                    .commit();
        }


        Button btnSearchMain = findViewById(R.id.btnSearchMain);
        btnSearchMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = new Intent(MainActivity.this, BookSearchActivity.class);
                startActivity(launchIntent);


            }
        });


    }

    @Override
    public void bookClicked(int position) {

        book = bookList.get(position);

        if (!container2Present) {

            getSupportFragmentManager()
                    .beginTransaction()
                    //.replace(R.id.btnSearchMain, BookDetailsFragment.newInstance(bookList.get(position)))
                    .replace(R.id.container_1, BookDetailsFragment.newInstance(book))
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

        outState.putParcelable(ARG_BOOKLIST, bookList);
    }



}