package com.example.joel.navdrawers;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
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


public class DetailActivity extends AppCompatActivity implements MessageAdapter.OnItemClickListener {
    //int pid;
    // private MessageInfo messageInfo;


    private RecyclerView mRecyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<MessageInfo> messageInfos;
    private RequestQueue mRequestQueue;
    SharedPreferences sp;





    //  private static final String IMAGE_ADDRESS ="https://cdn.pixabay.com/photo/2018/09/16/19/32/morgentau-3682209_960_720.jpg";



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
     * so that we can displaying the messages under each category
     */


    private void parseJSON() {

        sp = getSharedPreferences("com.loveeconomy.loveeconomy", Context.MODE_PRIVATE);
        final String token = sp.getString("auth_token", "");



        // String url = String.format("http://loveapp-le.herokuapp.com/v1/categories/", pid);

        String url = "http://loveapp-le.herokuapp.com/v1/contents";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONArray jsonArray = response.getJSONArray("contents");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject categories = jsonArray.getJSONObject(i);

                                String cardTitle = categories.getString("title");
                                String cardAuthor = categories.getString("author");
                                String cardGenre = categories.getString("genre");
                                String cardSource = categories.getString("source");
                                // String cardImage = categories.getString(IMAGE_ADDRESS);
                                // imageUrl = categories.getString("wallpaper");


                                messageInfos.add(new MessageInfo(cardTitle, cardGenre, cardSource, cardAuthor));
                            }


                            messageAdapter = new MessageAdapter(DetailActivity.this, messageInfos);
                            mRecyclerView.setAdapter(messageAdapter);
                            messageAdapter.setOnItemClickListener(DetailActivity.this);
                            // Toast.makeText(getActivity(), response.getJSONArray(), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailActivity.this, "JSON Exception" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // Toast.makeText(DetailActivity.this, "Error"+ error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                return headers;
            }
        };


        mRequestQueue.add(request);


    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this,MessagePlayerActivity.class);
        MessageInfo clickedMessage = messageInfos.get(position);

        detailIntent.putExtra("title",clickedMessage.getmTitle());
        detailIntent.putExtra("author",clickedMessage.getmAuthor());

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(DetailActivity.this,findViewById(R.id.message_view),"myImage");

        startActivity(detailIntent,optionsCompat.toBundle());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                messageAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}