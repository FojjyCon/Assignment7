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
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class BookSearchActivity extends AppCompatActivity {

    Button btnUserSearch;
    Button btnUserCancel;
    EditText editTextUrl;

    RequestQueue rq;

    BookList bookList = new BookList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        btnUserSearch = findViewById(R.id.btnBookSearch);
        btnUserCancel = findViewById(R.id.btnBookCancel);
        editTextUrl = findViewById(R.id.txtBookSearch);

        rq = Volley.newRequestQueue(this);

        btnUserCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUserSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = "https://kamorris.com/lab/cis3515/search.php?term=" + editTextUrl.getText().toString();
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject bookJSON;
                                    bookJSON = response.getJSONObject(i);
                                    bookList.add(new Book(bookJSON.getString("title"),
                                            bookJSON.getString("author"),
                                            bookJSON.getString("cover_url"),
                                            bookJSON.getInt("id")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {

                        }
                        Intent launchIntent = new Intent(BookSearchActivity.this, MainActivity.class);
                        launchIntent.putExtra("Books", bookList);
                        startActivity(launchIntent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                rq.add(jsonArrayRequest);
            }
        });
    }

}