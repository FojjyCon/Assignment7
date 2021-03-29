package temple.edu.assignment7;

import android.content.ClipData;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookAdapter extends ArrayAdapter {

    Context context;

    public BookAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView textViewTitle;
        TextView textViewAuthor;
        LinearLayout linearLayout;

        String title = ((Book)(getItem(position))).getTitle();
        String author = ((Book)(getItem(position))).getAuthor();

        if (convertView == null) {
            linearLayout = new LinearLayout(context);
            textViewTitle = new TextView(context);
            textViewTitle.setTextSize(22);
            textViewTitle.setPadding(15, 20, 0, 20);


            textViewAuthor = new TextView(context);
            textViewAuthor.setTextSize(14);
            textViewTitle.setPadding(15, 20, 0, 20);

            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(textViewTitle);
            linearLayout.addView(textViewAuthor);
        } else {
            linearLayout = (LinearLayout) convertView;
            textViewTitle = (TextView) linearLayout.getChildAt(0); //linearLayout.getChildAt(1);
            textViewAuthor = (TextView) linearLayout.getChildAt(1); //linearLayout.getChildAt(1);
        }
        textViewTitle.setText(title);
        textViewAuthor.setText(author);
        return linearLayout;
    }
}
