package temple.edu.assignment7;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {

    private static final String ARG_BOOKARRAYLIST = "param1";
    //private static final String ARG_PARAM2 = "param2";

    private ArrayList<Book> bookArrayList;
    //private String mParam1;
    //private String mParam2;

    public BookListFragment() {}

    public static BookListFragment newInstance(ArrayList<Book> bookArrayList) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_BOOKARRAYLIST, bookArrayList);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookArrayList = getArguments().getParcelableArrayList(ARG_BOOKARRAYLIST);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        } else {
            bookArrayList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(R.layout.fragment_book_list, container, false);

        listView.setAdapter(new BookAdapter(getActivity(), android.R.layout.simple_list_item_1, bookArrayList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return listView;
        //return inflater.inflate(R.layout.fragment_book_list, container, false);
    }
}