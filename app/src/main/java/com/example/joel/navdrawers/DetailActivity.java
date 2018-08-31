package com.example.joel.navdrawers;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import static com.example.joel.navdrawers.ExampleAdapter.EXTRA_CREATOR;
import static com.example.joel.navdrawers.ExampleAdapter.EXTRA_URL;




public class DetailActivity extends AppCompatActivity {

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

        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        textViewCreator.setText(creatorName);
       // textViewLikes.setText("Likes: " + likeCount);
    }
}