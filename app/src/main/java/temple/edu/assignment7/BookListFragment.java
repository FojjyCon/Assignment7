package temple.edu.assignment7;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class BookListFragment extends Fragment {

    private static final String ARG_BOOKLIST = "param1";

    private BookList bookList;

    public BookListFragment() {}

    public static BookListFragment newInstance(BookList bookList) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_BOOKLIST, bookList);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookList = (BookList) getArguments().getParcelableArrayList(ARG_BOOKLIST);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        } else {
            bookList = new BookList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(R.layout.fragment_book_list, container, false);

        listView.setAdapter(new BookAdapter(getActivity(), android.R.layout.simple_list_item_1, bookList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BookListFragmentInterface) getActivity()).bookClicked(position);
            }
        });

        return listView;
        //return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    interface BookListFragmentInterface {
        public void bookClicked(int position);
    }

}