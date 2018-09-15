package com.example.joel.navdrawers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mContext;
    private ArrayList<MessageInfo> mInfo;
    private AdapterView.OnItemClickListener mListener;
    private RequestQueue mRequestQueue;

    public MessageAdapter(Context mContext, ArrayList<MessageInfo> mInfo) {
        this.mContext = mContext;
        this.mInfo =mInfo;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_row, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//
//        ExampleItem currentItem = mExampleList.get(position);
//
//        String imageUrl = currentItem.getImageUrl();
//        String creatorName = currentItem.getCreator();
//        //   int likeCount = currentItem.getLikeCount();
//
//        holder.mTextViewCreator.setText(creatorName);
//        // holder.mTextViewLikes.setText("Likes: " + likeCount);
//        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);

        //center crop instead of centre inside

        MessageInfo currentItem = mInfo.get(position);

        String messageTitle = currentItem.getmTitle();
        String messageAuthor = currentItem.getmAuthor();
        String messageImage = currentItem.getmMessageImage();

    }


    @Override
    public int getItemCount() {
        return 0;
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public ImageView mMessageImageView;
        public TextView mMessageTitle;
        public TextView mMessageAuthor;




        public MessageViewHolder(View itemView) {
            super(itemView);
            mMessageImageView = itemView.findViewById(R.id.message_view);
            mMessageTitle = itemView.findViewById(R.id.message_author);
            mMessageAuthor = itemView.findViewById(R.id.message_author);
        }
    }
}
