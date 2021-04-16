package temple.edu.assignment7;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.temple.audiobookplayer.AudiobookService;

public class ControlFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private Book book;

    TextView nowPlaying;
    Button btnPlay, btnPause, btnStop;
    SeekBar seekbar;

    ControlFragmentInterface parentActivity;

    interface ControlFragmentInterface{
        void playBook(int i);
        void pauseBook(int i);
        void stopBook(int i);
    };

    public ControlFragment() {
    }

    public static ControlFragment newInstance(Book book) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control, container, false);

        btnPlay = v.findViewById(R.id.btnPlay);
        btnPause = v.findViewById(R.id.btnPause);
        btnStop = v.findViewById(R.id.btnStop);
        nowPlaying = v.findViewById(R.id.txtNowPlaying);
        seekbar = v.findViewById(R.id.sbState);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.playBook(book.getId());
                if (book != null) {
                    changeBook(book);
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.pauseBook(book.getId());
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.stopBook(book.getId());
            }
        });

        return v;
    }

    void changeBook(Book book) {
        nowPlaying.setText("Now Playing: " + book.getTitle());
    }

    public void updatePlayStatus(BookDetailsFragment detailsFragment, boolean playing) {
        nowPlaying.setText("Now Playing: " + book.getTitle());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BookListFragment.BookListFragmentInterface) {
            parentActivity = (ControlFragment.ControlFragmentInterface) context;
        } else {
            throw new RuntimeException("Please implement the required interface(s)");
        }
    }

}