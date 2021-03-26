package temple.edu.assignment7;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookDetailsFragment extends Fragment {

    private static final String ARG_BOOK = "param1";
    //private static final String ARG_PARAM2 = "param2";

    TextView txtTitle;
    TextView txtAuthor;

    private Book book;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(ARG_BOOK);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);

        txtTitle = v.findViewById(R.id.textViewTitle);
        txtAuthor = v.findViewById(R.id.textViewAuthor);
        if (book != null) {
            changeBook(book);
        }


        return v;
    }

    void changeBook(Book book) {
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
    }
}