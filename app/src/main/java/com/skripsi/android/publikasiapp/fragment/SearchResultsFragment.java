package com.skripsi.android.publikasiapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.model.Publikasi;
import com.skripsi.android.publikasiapp.adapters.SearchPublikasiAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "SearchResultsFragment";

    private static SearchResultsFragment INSTANCE = null;


    private RecyclerView recyclerViewSearchResults;
    private GridLayoutManager gridLayoutManager;
    private SearchPublikasiAdapter adapterSearchResults;
    private List<Publikasi> SearchResultspublikasiList;
    private SwipeRefreshLayout swipe;

    public SearchResultsFragment() {
        // Required empty public constructor
    }
//    public static SearchResultsFragment getInstance() {
//        Log.d(TAG, "getInstance: Search result fragment terpanggil");
//        if (INSTANCE == null) {
//            INSTANCE = new SearchResultsFragment();
//        }
//
//        return INSTANCE;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: started");
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);

        rootView.setTag(TAG);

        final String query = getArguments().getString("query_string");
        //Code disini
        //Inisiasi

        swipe = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_search_results);
        Log.d(TAG, "onCreateView: swipe initiated");
        recyclerViewSearchResults = (RecyclerView)rootView.findViewById(R.id.recycler_view_search_results);
        Log.d(TAG, "onCreateView: rview init");
        SearchResultspublikasiList = new ArrayList<>();
        Log.d(TAG, "onCreateView: pub init");
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerViewSearchResults.setLayoutManager(gridLayoutManager);

        adapterSearchResults = new SearchPublikasiAdapter(getContext(),SearchResultspublikasiList);
        recyclerViewSearchResults.setAdapter(adapterSearchResults);

        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                SearchResultspublikasiList.clear();
                swipe.setRefreshing(true);
                cariData(query);
            }
        });



        return rootView;
    }

    @Override
    public void onRefresh() {
        final String query = getArguments().getString("query_string");
        Log.d(TAG, "onRefresh: start");
        SearchResultspublikasiList.clear();
        cariData(query);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_search, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setQueryHint(getString(R.string.action_search));
//        searchView.setIconifiedByDefault(false);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//         @Override
//         public boolean onQueryTextSubmit(String query) {
//                return true;
//         }
//         @Override
//         public boolean onQueryTextChange(String query) {
//                return false;
//         }
//        }
//        );
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    private void cariData(String query){
        Log.d(TAG, "CariData: start");
        adapterSearchResults.notifyDataSetChanged();
        swipe.setRefreshing(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConfig.URL_CARI + query, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());
                //parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj =  response.getJSONObject(i);

                        Publikasi data = new Publikasi(obj.getInt("pub_id"), obj.getString("title"), obj.getString("kat_no"), obj.getString("pub_no"), obj.getString("issn"), obj.getString("abstract"), obj.getString("sch_date"), obj.getString("rl_date"),  obj.getString("cover"), obj.getString("pdf"), obj.getString("size"));

                        SearchResultspublikasiList.add(data);

                    } catch (JSONException e) {
                        System.out.println("End of content");
                    }

                }
                adapterSearchResults.notifyDataSetChanged();
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
    }
}
