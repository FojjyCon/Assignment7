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
import android.service.controls.Control;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface, ControlFragment.ControlFragmentInterface {

    FragmentManager fragManage;

    BookList bookList;
    Book book;
    BookDetailsFragment bookDetailsFragment;
    ControlFragment controlFragment;
    boolean container2Present;
    int bookIndex;

    AudiobookService.MediaControlBinder mediaControlBinder;
    boolean connected = true;
    int duration;
    Uri bookUri;
    SeekBar seekbar;
    boolean playWasClicked;
    boolean PLAYING = true;

    Intent bindIntent;
    private int currentBookId = -1;
    private Uri currentBookUri;

    private final MediaPlayer mediaPlayer = new MediaPlayer();

    private static final String ARG_BOOKLIST = "param1";

    // service connection
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

    /*
    Handler progressHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {

            AudiobookService.BookProgress currBook = (AudiobookService.BookProgress) message.obj;

            if(mediaControlBinder.isPlaying()){
                //seekbar.setProgress(book.getProgress());
            }

            return false;
        }
    });
     */

    Handler mediaControlHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            final AudiobookService.BookProgress bookProgress = (AudiobookService.BookProgress) msg.obj;
            //seekbar.setMax(duration);
            if(mediaControlBinder.isPlaying()){
                //seekbar.setProgress(bookProgress.getProgress());
                bookUri = bookProgress.getBookUri();
            }
            /*
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

             */
            return false;
        }
    });

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
        controlFragment = (book == null) ? new ControlFragment() : ControlFragment.newInstance(book);

        Intent newIntent = getIntent();
        if (container2Present) {

            bookDetailsFragment = new BookDetailsFragment();
            controlFragment = new ControlFragment();

            if (newIntent.hasExtra("Books")) {
                Bundle extras = getIntent().getExtras();
                bookList = extras.getParcelable("Books");
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    //.replace(R.id.container_2, bookDetailsFragment)
                    .replace(R.id.container_1, BookListFragment.newInstance(bookList))
                    .replace(R.id.container_2, BookDetailsFragment.newInstance(book))
                    .replace(R.id.subContainerButton, ControlFragment.newInstance(book))
                    .commit();
        }
        else {
            if (newIntent.hasExtra("Books")) {
                Bundle extras = getIntent().getExtras();
                bookList = extras.getParcelable("Books");
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_1, BookListFragment.newInstance(bookList))
                    .replace(R.id.container_control, ControlFragment.newInstance(book))
                    //.replace(R.id.container_control, controlFragment)
                    .commit();
        }

        bindIntent = new Intent(this, AudiobookService.class);
        bindService(bindIntent, bookServiceConnection, BIND_AUTO_CREATE);

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
                    .replace(R.id.container_control, ControlFragment.newInstance(book))
                    .addToBackStack(null)
                    .commit();
        } else {
            //bookDetailsFragment.changeBook(bookList.get(position));
            bookDetailsFragment.changeBook(book);
            //controlFragment.bookSelected(book);
        }
        bookIndex = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_BOOKLIST, book);
    }

    @Override
    public void playBook(int id) {
        if(connected){
            startService(bindIntent);
            mediaControlBinder.setProgressHandler(mediaControlHandler);
            mediaControlBinder.play(id);
            playWasClicked = true;
        }
    }

    @Override
    public void pauseBook(int id) {
        mediaControlBinder.pause();
    }

    @Override
    public void stopBook(int id) {
        mediaControlBinder.stop();
    }
}