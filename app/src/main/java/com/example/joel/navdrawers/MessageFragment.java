package com.example.joel.navdrawers;



import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import es.dmoral.toasty.Toasty;


public class MessageFragment extends Fragment  {

    ExampleBroadcastReceiver exampleBroadcastReceiver = new ExampleBroadcastReceiver();

    private RecyclerView mRecyclerView;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    public  static  String imageUrl;
    private ExampleAdapter mExampleAdapter;


    SharedPreferences sp;

    private RecyclerView mRecyclerViewHorizontal;
    private ExampleAdapterHorizontal mExampleAdapterHorizontal;
    private TextView categoryMessage;
    private TextView recentMessage;
    private SwipeRefreshLayout swipeContainer;







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mesage, container, false);

        mRecyclerView =   view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        categoryMessage = view.findViewById(R.id.categoryMessage);
        recentMessage =view.findViewById(R.id.RecentMessage);
        //mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parseJSON();
                Toasty.success(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
            }

        });
        mRecyclerViewHorizontal = (RecyclerView)view.findViewById(R.id.recycle_view);
        mRecyclerViewHorizontal.setHasFixedSize(true);
        mRecyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        mExampleList = new ArrayList<>();

        sp = this.getActivity().getSharedPreferences("com.example.joel.navdrawers", Context.MODE_PRIVATE);




        mRequestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(exampleBroadcastReceiver,filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(exampleBroadcastReceiver);
    }

    private void parseJSON() {

        sp = this.getActivity().getSharedPreferences("com.loveeconomy.loveeconomy", Context.MODE_PRIVATE);
        final String token = sp.getString("auth_token", "");

        String url = "http://loveapp-le.herokuapp.com/v1/categories";
           // String url ="https://salty-garden-58363.herokuapp.com/v1/users/new";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeContainer.setRefreshing(false);


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
                headers.put("Authorization", token);
                return headers;
            }
        };


        mRequestQueue.add(request);


    }




}