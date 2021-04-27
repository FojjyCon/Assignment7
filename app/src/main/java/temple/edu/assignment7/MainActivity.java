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
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.service.controls.Control;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface, ControlFragment.ControlFragmentInterface {

    FragmentManager fragManage;

    BookList bookList;
    Book book;
    BookDetailsFragment bookDetailsFragment;
    ControlFragment controlFragment;
    boolean container2Present;
    int bookIndex;
    int position;

    AudiobookService.MediaControlBinder mediaControlBinder;
    boolean connected = true;
    int duration;
    int progress;
    Uri bookUri;
    SeekBar seekbar;
    boolean playWasClicked;
    File file;
    boolean paused;
    int pausePosition;

    Intent bindIntent;
    private static final String ARG_BOOKLIST = "books";
    private static final String SEEKBAR_PROGRESS = "sbProgress";
    private static final String DURATION = "bookDuration";
    private static final String POSITION_VALUE = "pause_progress";

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

    Handler mediaControlHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            final AudiobookService.BookProgress bookProgress = (AudiobookService.BookProgress) msg.obj;
            seekbar =  findViewById(R.id.sbState);
            seekbar.setMax(duration);
            if(mediaControlBinder.isPlaying()){

                if (!paused) {
                    progress = duration;
                    seekbar.setProgress(bookProgress.getProgress());
                    bookUri = bookProgress.getBookUri();
                    pausePosition = bookProgress.getProgress();
                } else if (paused) {
                    progress = duration;
                    seekbar.setProgress(pausePosition);
                    bookUri = bookProgress.getBookUri();
                }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            //bookDetailsFragment = new BookDetailsFragment();
            //bookList = (BookList) savedInstanceState.getParcelableArrayList(ARG_BOOKLIST);

            book = savedInstanceState.getParcelable(ARG_BOOKLIST);
            duration = savedInstanceState.getInt(DURATION);
            progress = savedInstanceState.getInt(SEEKBAR_PROGRESS);
            pausePosition = savedInstanceState.getInt(POSITION_VALUE);
        }
        container2Present = findViewById(R.id.container_2) != null;
        seekbar = findViewById(R.id.sbState);
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
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_2, BookDetailsFragment.newInstance(book))
                    .replace(R.id.subContainerButton, ControlFragment.newInstance(book))
                    .addToBackStack(null)
                    .commit();
        }
        bookIndex = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_BOOKLIST, book);
        outState.putInt(SEEKBAR_PROGRESS, progress);
        outState.putInt(DURATION, duration);
        outState.putInt(POSITION_VALUE, position);
    }

    @Override
    public void playBook(int id) {
        if(connected){
            startService(bindIntent);
            duration = book.getDuration();
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
        seekbar.setProgress(0);
    }

    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/2011.kml");

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
        }

        return null;
    }

}