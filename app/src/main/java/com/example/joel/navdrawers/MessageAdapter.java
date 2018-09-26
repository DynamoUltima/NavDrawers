package com.example.joel.navdrawers;

import android.content.Context;
import android.content.Intent;
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
    private OnItemClickListener mListener;
    private RequestQueue mRequestQueue;

    public MessageAdapter(Context mContext, ArrayList<MessageInfo> mInfo) {
        this.mContext = mContext;
        this.mInfo = mInfo;
    }
    public interface OnItemClickListener{
    void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
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
      //  String messageImage = currentItem.getmMessageImage();


        holder.mMessageTitle.setText(messageTitle);
        holder.mMessageAuthor.setText(messageAuthor);
        Picasso.with(mContext).load("https://pastoroti.org/wp-content/uploads/2017/03/5min_newpsd-Copy-e1490524437255-540x310.jpg").fit().centerCrop().into(holder.mMessageImageView);
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
            mMessageTitle = itemView.findViewById(R.id.message_title);
            mMessageAuthor = itemView.findViewById(R.id.message_author);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);

                        }
                    }

                }
            });

        }
    }
}
