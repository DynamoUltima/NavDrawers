package com.example.joel.navdrawers;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;


public class MessageFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    public  static  String imageUrl;
    private ExampleAdapter mExampleAdapter;


    SharedPreferences sp;

    private RecyclerView mRecyclerViewHorizontal;
    private ExampleAdapterHorizontal mExampleAdapterHorizontal;

    public String API_TOKEN="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiZGM4NGI2YWItNGRjZC00NDY5LTkxNDAtYjcyMjkxYzQ1MzZmIiwiaWF0IjoxNTM3ODk2NTEyLCJleHAiOjE1Mzc5MDY1MTJ9.nzON94aGm0omvwGxQLwvay8EXkzcb5NQ_hF899pTWMA";




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mesage, container, false);

        mRecyclerView =   view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false));



        mRecyclerViewHorizontal = (RecyclerView)view.findViewById(R.id.recycle_view);
        mRecyclerViewHorizontal.setHasFixedSize(true);
        mRecyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        mExampleList = new ArrayList<>();

        sp = this.getActivity().getSharedPreferences("com.example.joel.navdrawers", Context.MODE_PRIVATE);


        mRequestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();
        return view;
    }

    private void parseJSON() {
        String url = "http://loveapp-le.herokuapp.com/v1/categories";
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
                                String id = categories.getString("id");
                                //actually int String likeCount = categories.getString("createdAt");

                                mExampleList.add(new ExampleItem(imageUrl, creatorName,id));

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("categoryID", id);
                                editor.commit();

                                Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
                            }


                            mExampleAdapter = new ExampleAdapter(getActivity(), mExampleList);//the was a third parameter
                            mRecyclerView.setAdapter(mExampleAdapter);
                           // mExampleAdapter.setOnItemClickListener(this);
                           // Toast.makeText(getActivity(), response.getJSONArray(), Toast.LENGTH_SHORT).show();


                            mExampleAdapterHorizontal = new ExampleAdapterHorizontal(getActivity(), mExampleList);
                            mRecyclerViewHorizontal.setAdapter(mExampleAdapterHorizontal);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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