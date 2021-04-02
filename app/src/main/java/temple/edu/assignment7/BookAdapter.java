package temple.edu.assignment7;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookAdapter extends BaseAdapter {

    Context context;
    BookList bookList;

    public BookAdapter (Context context, BookList bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public int getCount() { return bookList.size(); }

    @Override
    public Object getItem(int position) { return bookList.get(position); }

    @Override
    public long getItemId(int position) { return 0; }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView textViewTitle, textViewAuthor;

        if (!(convertView instanceof LinearLayout)) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_book_details, parent, false);
        }

        textViewTitle = convertView.findViewById(R.id.textViewTitle);
        textViewAuthor = convertView.findViewById(R.id.textViewAuthor);

        textViewTitle.setText(((Book) getItem(position)).getTitle());
        textViewAuthor.setText(((Book) getItem(position)).getAuthor());

        return convertView;
    }
}
