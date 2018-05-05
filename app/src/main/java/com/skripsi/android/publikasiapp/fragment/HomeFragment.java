package com.skripsi.android.publikasiapp.fragment;


import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.model.ListPublikasiAdapter;
import com.skripsi.android.publikasiapp.model.Publikasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private static HomeFragment INSTANCE = null;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ListPublikasiAdapter adapter;
    private List<Publikasi> publikasiList;




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

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_1);
        publikasiList = new ArrayList<>();
        load_data_from_server(0);

        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ListPublikasiAdapter(getContext(),publikasiList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(gridLayoutManager.findLastCompletelyVisibleItemPosition()==publikasiList.size()-1){
                    load_data_from_server(publikasiList.get(publikasiList.size()-1).getPub_id());
                }
            }
        });

        return rootView;
    }

    private void load_data_from_server(final int id) {
        Log.d(TAG, "load_data_from_server: started");

        AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(AppConfig.URL_PUBLIKASI+id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());
                    for(int i=0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);

                        Publikasi data = new Publikasi(object.getInt("pub_id"),object.getString("title"),object.getString("kat_no"),object.getString("pub_no"),object.getString("issn"),object.getString("abstract"),object.getString("sch_date"),object.getString("rl_date"),object.getString("updt_date"),object.getString("cover"),object.getString("pdf"),object.getString("size"));

                         publikasiList.add(data);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d(TAG, "doInBackground: e");
                    System.out.println("End of content");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(id);
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
