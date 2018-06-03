package com.skripsi.android.publikasiapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.adapters.ListPublikasiAdapter;
import com.skripsi.android.publikasiapp.model.Publikasi;


import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private static final String TAG = "SearchFragment";

    private static SearchFragment INSTANCE = null;

    private ProgressBar progressBar;
    private ListPublikasiAdapter adapter;
    private List<Publikasi> publikasiList;
    private TextView ikanfilter,pendudukfilter,ekofilter,sosfilter,tanifilter;
    private Fragment fragment;

    String tag_json_obj = "json_obj_req";


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment getInstance() {
        Log.d(TAG, "getInstance: fragment search terpanggil");
        if (INSTANCE == null) {
            INSTANCE = new SearchFragment();
        }

        return INSTANCE;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: startsearchview");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        //Recycler view variables
        rootView.setTag(TAG);

        ekofilter = (TextView) rootView.findViewById(R.id.ekofilter);
        sosfilter = (TextView) rootView.findViewById(R.id.sosfilter);
        tanifilter = (TextView) rootView.findViewById(R.id.tanifilter);
        ikanfilter = (TextView) rootView.findViewById(R.id.ikanfilter);
        pendudukfilter = (TextView) rootView.findViewById(R.id.pendudukfilter);

        ekofilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "ekonomi";
                fragment = new SearchResultsFragment();
                Bundle args = new Bundle();
                args.putString("query_string",query);
                fragment.setArguments(args);

                loadFragment(fragment);
            }
        });


        sosfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "sosial";
                fragment = new SearchResultsFragment();
                Bundle args = new Bundle();
                args.putString("query_string",query);
                fragment.setArguments(args);

                loadFragment(fragment);
            }
        });


        tanifilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "tani";
                fragment = new SearchResultsFragment();
                Bundle args = new Bundle();
                args.putString("query_string",query);
                fragment.setArguments(args);

                loadFragment(fragment);
            }
        });

        ikanfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "ikan";
                fragment = new SearchResultsFragment();
                Bundle args = new Bundle();
                args.putString("query_string",query);
                fragment.setArguments(args);

                loadFragment(fragment);
            }
        });

        pendudukfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "penduduk";
                fragment = new SearchResultsFragment();
                Bundle args = new Bundle();
                args.putString("query_string",query);
                fragment.setArguments(args);

                loadFragment(fragment);
            }
        });
        return rootView;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);

    }

//    private void CariData(final String search) {
//        progressBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleLarge);
//        progressBar.setVisibility(View.VISIBLE);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_CARI, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("onResponse: ", response.toString());
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    int value = jsonObject.getInt("value");
//
//                    if (value == 1) {
//                        publikasiList.clear();
//                        adapter.notifyDataSetChanged();
//
//                        String getObject = jsonObject.getString("result");
//                        JSONArray jsonArray = new JSONArray(getObject);
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject obj = jsonArray.getJSONObject(i);
//
//                            Publikasi data = new Publikasi(obj.getInt("pub_id"), obj.getString("title"), obj.getString("kat_no"), obj.getString("pub_no"), obj.getString("issn"), obj.getString("abstract"), obj.getString("sch_date"), obj.getString("rl_date"), obj.getString("updt_date"), obj.getString("cover"), obj.getString("pdf"), obj.getString("size"));
//
//                            publikasiList.add(data);
//                        }
//                    } else {
//                        Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                adapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e(TAG,"Error : "+error.getMessage());
//                Toast.makeText(getActivity().getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("search", search);
//
//                return params;
//            }
//        };
//
//        AppController.getInstance().addToRequestQueue(stringRequest,tag_json_obj);
//
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void loadFragment(Fragment fragment) {
        Log.d(TAG, "loadFragment: started");

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_container, fragment).commit();

    }
}
