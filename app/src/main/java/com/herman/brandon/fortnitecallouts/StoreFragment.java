package com.herman.brandon.fortnitecallouts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.herman.brandon.fortnitecallouts.Helpers.StoreContent;

import org.json.JSONObject;

import static com.herman.brandon.fortnitecallouts.Helpers.FortniteApiHelper.fetchStoreInfo;

/**
 * A fragment representing a list of store items
 */
public class StoreFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private JSONObject storeInfo;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        if(storeInfo == null) {
            fetchStoreInfo(new StatsFragment.VolleyCallback() {
                @Override
                public void onSuccess(JSONObject store) {
                    storeInfo = store;

                    setupRecyclerView(view);
                }

                @Override
                public void onFailure(VolleyError error) {

                }
            });
        } else {
            setupRecyclerView(view);
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyStoreRecyclerViewAdapter(StoreContent.ITEMS, this));
        }
        return view;
    }

    private void setupRecyclerView(View view){
        StoreContent.generateStoreItems(storeInfo);

        RecyclerView recyclerView = view.findViewById(R.id.store_list);
        if (recyclerView != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyStoreRecyclerViewAdapter(StoreContent.ITEMS, this));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
