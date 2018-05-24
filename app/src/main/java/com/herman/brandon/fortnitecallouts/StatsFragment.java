package com.herman.brandon.fortnitecallouts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.herman.brandon.fortnitecallouts.Helpers.StatsContent;

import org.json.JSONObject;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.herman.brandon.fortnitecallouts.Helpers.FortniteApiHelper.fetchUserStats;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatsFragment.OnStatsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EPIC_ACCOUNT_NAME = "account_name";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;

    private OnStatsFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    private String epicAccountName;

    private JSONObject playerStats;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Interface used to handle callbacks from Volley fetches
     */
    public interface VolleyCallback{
        void onSuccess(JSONObject stats);
        void onFailure(VolleyError error);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param epicAccountName
     * @param columnCount
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String epicAccountName, int columnCount) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EPIC_ACCOUNT_NAME, epicAccountName);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            epicAccountName = getArguments().getString(ARG_EPIC_ACCOUNT_NAME);
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_stats_list, container, false);

        LinearLayout header = v.findViewById(R.id.stat_header);

        if (epicAccountName != null) {
            if (playerStats == null) {
                // Set the adapter
                fetchUserStats(epicAccountName, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject stats) {
                        playerStats = stats;

                        createRecycleViewer(v);
                    }

                    @Override
                    public void onFailure(VolleyError error) {

                    }
                });
            } else {
                createRecycleViewer(v);
            }

            createHeader(epicAccountName + "\'s Stats", header);
        } else {
            createHeader("Link Your Epic Account!", header);
            final EditText userName = new EditText(getContext());
            userName.setLayoutParams(new LinearLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT));
            userName.setGravity(Gravity.CENTER);
            header.addView(userName);

            Button setEpicAccountNameButton = new Button(getContext());
            setEpicAccountNameButton.setText("Set Epic Account Name");
            setEpicAccountNameButton.setLayoutParams(new LinearLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT));
            setEpicAccountNameButton.setGravity(Gravity.CENTER);
            setEpicAccountNameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String user = userName.getText().toString();
                    if (!"".equals(user)) {
                        onButtonPressed(user);
                    }
                }
            });
            header.addView(setEpicAccountNameButton);
        }

        // Inflate the layout for this fragment
        return v;
    }

    public void createHeader(String text, LinearLayout header) {
        final TextView userName = new TextView(getContext());
        userName.setLayoutParams(new LinearLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT));
        userName.setText(text);
        userName.setTextSize(20);
        userName.setGravity(Gravity.CENTER);
        header.addView(userName);
    }

    public void createRecycleViewer(View v) {
        StatsContent.generateAllGameModeStats(playerStats);

        RecyclerView recyclerView = v.findViewById(R.id.stats_list);
        if (recyclerView != null) {
            Context context = v.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyStatRecyclerViewAdapter(StatsContent.ITEMS, mListener));
        }
    }

    public void clearFragmentAndGetStats(){
        View parentView = getView();
        if (parentView != null) {
            LinearLayout container = parentView.findViewById(R.id.stat_header);
            container.removeAllViews();
            createHeader(epicAccountName + "\'s Stats", container);
            createRecycleViewer(parentView);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(final String epicName) {
        if (mListener != null) {
            fetchUserStats(epicName, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject stats) {
                    playerStats = stats;
                    epicAccountName = epicName;
                    clearFragmentAndGetStats();

                    mListener.onEpicAccountLinked(epicName);
                }

                @Override
                public void onFailure(VolleyError error) {

                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStatsFragmentInteractionListener) {
            mListener = (OnStatsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStatsFragmentInteractionListener {
        void onEpicAccountLinked(String account);
    }
}
