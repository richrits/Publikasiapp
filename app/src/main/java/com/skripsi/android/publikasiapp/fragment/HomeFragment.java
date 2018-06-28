package com.skripsi.android.publikasiapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.adapters.ListPublikasiAdapter;
import com.skripsi.android.publikasiapp.helper.SQLiteHandler;
import com.skripsi.android.publikasiapp.model.Publikasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "HomeFragment";

    private static HomeFragment INSTANCE = null;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ListPublikasiAdapter adapter;
    private List<Publikasi> publikasiList;
    private SwipeRefreshLayout swipe;
    private SQLiteHandler db;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance() {
        Log.d(TAG, "getInstance: Home fragment terpanggil");
        if (INSTANCE == null) {
            INSTANCE = new HomeFragment();
        }

        return INSTANCE;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: started");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //Recycler view variables
        rootView.setTag(TAG);

        Publikasi publikasi;

        swipe = rootView.findViewById(R.id.swipe_refresh);
        recyclerView = rootView.findViewById(R.id.recycler_view_1);
        publikasiList = new ArrayList<>();


        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ListPublikasiAdapter(getContext(),publikasiList);
        recyclerView.setAdapter(adapter);
        callData(0);
      //  swipe.setOnRefreshListener(this);
//        swipe.post(new Runnable() {
//            @Override
//            public void run() {
//                swipe.setRefreshing(true);
//                callData(0);
//
//            }
//        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(gridLayoutManager.findLastCompletelyVisibleItemPosition()==publikasiList.size()-1){
                    callData(publikasiList.get(publikasiList.size()-1).getPub_id());
                }
            }
        });


        return rootView;
    }


    private void callData(int id) {
        Log.d(TAG, "load_data_from_server: started");
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        //Creating volley request obj
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConfig.URL_PUBLIKASI+id, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());
                //parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj =  response.getJSONObject(i);

                        Publikasi data = new Publikasi(
                                obj.getInt("pub_id"),
                                obj.getString("title"),
                                obj.getString("kat_no"),
                                obj.getString("pub_no"),
                                obj.getString("issn"),
                                obj.getString("abstract"),
                                obj.getString("sch_date"),
                                obj.getString("rl_date"),
                                obj.getString("cover"),
                                obj.getString("pdf"),
                                obj.getString("size"));

                        publikasiList.add(data);


//                        if(data!=null){
//
//                            db.addPublikasi(data.getTitle(),data.getKat_no(),data.getPub_no(),data.getIssn(),data.get_abstract(),data.getSch_date(),data.getRl_date(),data.getCover(),data.getPdf(),data.getSize());
//
//                        }
                    } catch (JSONException e) {
                        System.out.println("End of content");
                    }

                }
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error :" + error.getMessage());
                System.out.println("End of content");
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        });
        //adding request to request queuee
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: start");
        publikasiList.clear();
  //      db.deletePublikasi();
        callData(0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
    }



}
