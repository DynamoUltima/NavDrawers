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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mContext;
    private ArrayList<MessageInfo> mInfo;
    private AdapterView.OnItemClickListener mListener;
    private RequestQueue mRequestQueue;

    public MessageAdapter(Context mContext, ArrayList<MessageInfo> mInfo) {
        this.mContext = mContext;
        this.mInfo = mInfo;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_row, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageInfo currentItem = mInfo.get(position);

        String messageTitle = currentItem.getmTitle();
        String messageAuthor = currentItem.getmAuthor();
        String messageImage = currentItem.getmMessageImage();


        holder.mMessageTitle.setText(messageTitle);
        holder.mMessageAuthor.setText(messageAuthor);
        Picasso.with(mContext).load(messageImage).fit().centerCrop().into(holder.mMessageImageView);
        /**
         * there is no image in the API ,take note of the picasso loader
         * And the Message Info Java class*/

    }


    @Override
    public int getItemCount() {
        return mInfo.size();
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
