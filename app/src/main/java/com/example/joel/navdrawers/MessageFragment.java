package com.example.joel.navdrawers;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MessageFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    public  static  String imageUrl;
    private ExampleAdapter mExampleAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mesage, container, false);

        mRecyclerView =   view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

        mExampleList = new ArrayList<>();


        mRequestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();
        return view;
    }

    private void parseJSON() {
        String url = "https://salty-garden-58363.herokuapp.com/v1/categories";
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