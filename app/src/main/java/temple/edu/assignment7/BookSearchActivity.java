package temple.edu.assignment7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class BookSearchActivity extends AppCompatActivity {

    Handler downloadHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try {
                            URL url = new URL(editTextUrl.getText().toString());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                            Message msg = Message.obtain();
                            StringBuilder sb = new StringBuilder();
                            String str;

                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }

                            msg.obj = reader.readLine();
                            downloadHandler.sendMessage(msg);

                        } catch (Exception e ) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });*/

        final EditText prompt = new EditText(this);
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("My title");
            adb.setView(prompt);
            adb.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String myText = prompt.getText().toString();






                    Intent launchIntent = new Intent(BookSearchActivity.this, BookListFragment.class);
                    startActivity(launchIntent);
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog promptDialog = adb.create();
            promptDialog.show();


    }

    /*
    private String sanitizeURL(String url) {
        if (url.startsWith("http")) {
            return url;
        }else {
            editTextUrl.setText("https://" + url);
            return "https://" + url;
        }
    }
     */
}