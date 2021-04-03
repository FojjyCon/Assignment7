package temple.edu.assignment7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface {

    FragmentManager fragManage;

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

        Fragment frag;
        fragManage = getSupportFragmentManager();
        frag = fragManage.findFragmentById(R.id.container_1);
        if(frag instanceof BookDetailsFragment){
            fragManage.popBackStack();
        }
        else if(!(frag instanceof BookListFragment)){
            fragManage.beginTransaction()
                    .add(R.id.container_1, BookListFragment.newInstance(bookList))
                    .commit();
        }

        // search button clicked
        Button btnSearchMain = findViewById(R.id.btnSearchMain);
        btnSearchMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = new Intent(MainActivity.this, BookSearchActivity.class);
                startActivity(launchIntent);
            }
        });

        // checking on container 2
        bookDetailsFragment = (book == null) ? new BookDetailsFragment() : BookDetailsFragment.newInstance(book);
        Intent newIntent = getIntent();
        if (container2Present) {
            bookDetailsFragment = new BookDetailsFragment();

            if (newIntent.hasExtra("Books")) {
                Bundle extras = getIntent().getExtras();
                bookList = extras.getParcelable("Books");
            }
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
                    .replace(R.id.container_1, BookListFragment.newInstance(bookList))
                    .commit();
        }

    }

    public void onBackPressed() {
        book = null;
        super.onBackPressed();
    }

    @Override
    public void bookClicked(int position) {

        book = bookList.get(position);

        if (!container2Present) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_1, BookDetailsFragment.newInstance(book))
                    .addToBackStack(null)
                    .commit();
        } else {
            //bookDetailsFragment.changeBook(bookList.get(position));
            bookDetailsFragment.changeBook(book);
        }
        bookIndex = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_BOOKLIST, book);
    }

}