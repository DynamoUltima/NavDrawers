package com.example.joel.navdrawers;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.joel.navdrawers.ExampleAdapter.EXTRA_CREATOR;
import static com.example.joel.navdrawers.ExampleAdapter.EXTRA_URL;
import static com.example.joel.navdrawers.MessageFragment.imageUrl;


public class DetailActivity extends AppCompatActivity {
    //int pid;
   // private MessageInfo messageInfo;


    private RecyclerView mRecyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<MessageInfo> messageInfos;
    private RequestQueue mRequestQueue;

    public String API_TOKEN="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiZGM4NGI2YWItNGRjZC00NDY5LTkxNDAtYjcyMjkxYzQ1MzZmIiwiaWF0IjoxNTM3NTMyNTY5LCJleHAiOjE1Mzc1NDI1Njl9.gIgmS9AiqUGrKG2Fi0NQ21nlkyvmhNvAPHYknIAOwk4";


  //  private static final String IMAGE_ADDRESS ="https://cdn.pixabay.com/photo/2018/09/16/19/32/morgentau-3682209_960_720.jpg";

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageInfos = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);

        parseJSON();

//        Intent intent = getIntent();
//        String imageUrl = intent.getStringExtra(EXTRA_URL);
//        String creatorName = intent.getStringExtra(EXTRA_CREATOR);
//        //  int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);
//
//        ImageView imageView = (ImageView) findViewById(R.id.image_view_detail);
//        TextView textViewCreator = (TextView) findViewById(R.id.text_view_creator_detail);
//        TextView textViewLikes = (TextView) findViewById(R.id.text_view_like_detail);
//
//        Picasso.with(this).load(imageUrl).fit().centerCrop().into(imageView);
//        textViewCreator.setText(creatorName);
//        // textViewLikes.setText("Likes: " + likeCount);
    }

    /**
     * the plan is to get a particular id from each of the json objects(pid) in the first request in the message fragments
     * then pass that id into the string url (that is actually making a query)
     * so that we can displaying the messages under each category*/


   private void parseJSON() {

//       SharedPreferences sharedPreferences = getSharedPreferences("com.example.joel.navdrawers", MODE_PRIVATE);
//       String pid = sharedPreferences.getString("personelID", "");

       // String url = String.format("http://loveapp-le.herokuapp.com/v1/categories/", pid);

         String url ="http://loveapp-le.herokuapp.com/v1/contents";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONArray jsonArray = response.getJSONArray("contents");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject categories = jsonArray.getJSONObject(i);

                                String cardTitle = categories.getString("title");
                                String cardAuthor = categories.getString("source");
                                String cardGenre =  categories.getString("genre");
                                String cardSource = categories.getString("source");
                               // String cardImage = categories.getString(IMAGE_ADDRESS);
                               // imageUrl = categories.getString("wallpaper");
                                //actually int String likeCount = categories.getString("createdAt");

                                messageInfos.add(new MessageInfo(cardTitle,cardGenre,cardSource,cardAuthor));
                            }


                            messageAdapter = new MessageAdapter(DetailActivity.this, messageInfos);
                            mRecyclerView.setAdapter(messageAdapter);
                            // mExampleAdapter.setOnItemClickListener(this);
                            // Toast.makeText(getActivity(), response.getJSONArray(), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailActivity.this, "JSON Exception"+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
               // Toast.makeText(DetailActivity.this, "Error"+ error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", API_TOKEN);
                return headers;
            }
        };


        mRequestQueue.add(request);


    }
}