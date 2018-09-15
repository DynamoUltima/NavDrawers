package com.example.joel.navdrawers;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.joel.navdrawers.ExampleAdapter.EXTRA_CREATOR;
import static com.example.joel.navdrawers.ExampleAdapter.EXTRA_URL;
import static com.example.joel.navdrawers.MessageFragment.imageUrl;


public class DetailActivity extends AppCompatActivity {
    int pid;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        //  int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);

        ImageView imageView = (ImageView) findViewById(R.id.image_view_detail);
        TextView textViewCreator = (TextView) findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = (TextView) findViewById(R.id.text_view_like_detail);

        Picasso.with(this).load(imageUrl).fit().centerCrop().into(imageView);
        textViewCreator.setText(creatorName);
        // textViewLikes.setText("Likes: " + likeCount);
    }

    /**
     * the plan is to get a particular id from each of the json objects(pid) in the first request in the message fragments
     * then pass that id into the string url (that is actually making a query)
     * so that we can displaying the messages under each category*/


   private void parseJSON() {

       SharedPreferences sharedPreferences = getSharedPreferences("com.example.joel.navdrawers", MODE_PRIVATE);
       String pid = sharedPreferences.getString("personelID", "");

        String url = String.format("http://loveapp-le.herokuapp.com/v1/categories/", pid);
        // String url ="https://salty-garden-58363.herokuapp.com/v1/users/new";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONArray jsonArray = response.getJSONArray("categories");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject categories = jsonArray.getJSONObject(i);

                                String creatorName = categories.getString("name");
                                imageUrl = categories.getString("wallpaper");
                                //actually int String likeCount = categories.getString("createdAt");

                                mExampleList.add(new ExampleItem(imageUrl, creatorName));
                            }


                            mExampleAdapter = new ExampleAdapter(getActivity(), mExampleList);//the was a third parameter
                            mRecyclerView.setAdapter(mExampleAdapter);
                            // mExampleAdapter.setOnItemClickListener(this);
                            // Toast.makeText(getActivity(), response.getJSONArray(), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        mRequestQueue.add(request);


    }
}