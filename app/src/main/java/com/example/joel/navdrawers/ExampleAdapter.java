package com.example.joel.navdrawers;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import static com.example.joel.jsonparse.MainActivity.*;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<ExampleItem> mExampleList;
  //  private OnItemClickListener mListener;
    private RequestQueue mRequestQueue;
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";



   String imageUrl;

//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }

    public ExampleAdapter(Context context, ArrayList<ExampleItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String creatorName = currentItem.getCreator();
        //   int likeCount = currentItem.getLikeCount();

        holder.mTextViewCreator.setText(creatorName);
        // holder.mTextViewLikes.setText("Likes: " + likeCount);
        Picasso.with(mContext).load(imageUrl).fit().centerCrop().into(holder.mImageView);

                //.centerInside().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewCreator;
        // public TextView mTextViewLikes;

        public ExampleViewHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);






            //  mTextViewLikes = itemView.findViewById(R.id.text_view_likes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION ) {

                        ExampleItem clickedItem = mExampleList.get(position);

                        Toast.makeText(mContext, "testClick" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                        Intent detailIntent = new Intent(mContext, DetailActivity.class);
                        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
                        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
                        mContext.startActivity(detailIntent);
                    }
                    
                    
                    
                    
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            ExampleItem clickedItem = mListener.onItemClick(position);
//
//                            Intent detailIntent = new Intent(mContext, DetailActivity.class);
//                            detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
//                            detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
//
//                        }
//                    }



                }

            });
        }


    }

}
