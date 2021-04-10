package temple.edu.assignment7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface, ControlFragment.ControlFragmentInterface {

    FragmentManager fragManage;

    BookList bookList;
    Book book;
    BookDetailsFragment bookDetailsFragment;
    boolean container2Present;
    int bookIndex;

    Handler mediaControlHandler;
    AudiobookService.MediaControlBinder mediaControlBinder;
    boolean connected;
    int duration;
    Uri bookUri;
    SeekBar seekbar;

    private int currentBookId = -1;
    private Uri currentBookUri;
    private final MediaPlayer mediaPlayer = new MediaPlayer();

    private static final String ARG_BOOKLIST = "param1";

    ServiceConnection bookServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaControlBinder = (AudiobookService.MediaControlBinder) service;
            mediaControlBinder.setProgressHandler(mediaControlHandler);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaControlHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                final AudiobookService.BookProgress bookProgress = (AudiobookService.BookProgress) msg.obj;
                seekbar.setMax(duration);
                if(mediaControlBinder.isPlaying()){
                    seekbar.setProgress(bookProgress.getProgress());
                    bookUri = bookProgress.getBookUri();
                }
                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mediaControlBinder.seekTo(progress);

                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                return false;
            }
        });

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
                    .replace(R.id.container_control, ControlFragment.newInstance(book))
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

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }
}