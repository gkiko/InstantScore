package com.example.instantscore;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.instantscore.adapter.ExpandableListAdapter;
import com.example.instantscore.communication.MyVolley;
import com.example.instantscore.communication.PostRequest;
import com.example.instantscore.database.DBManager;
import com.example.instantscore.model.League;
import com.example.instantscore.model.Match;
import com.example.instantscore.volley.NoConnectionError;
import com.example.instantscore.volley.Request;
import com.example.instantscore.volley.Response;
import com.example.instantscore.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainSectionFragment extends Fragment {
    RelativeLayout backgroundWarning;
    Activity activity;

    Gson gson = new Gson();
    ExpandableListView expandableListView;
    ExpandableListAdapter listAdapter;
    private List<Integer> expandedGroups = new ArrayList<Integer>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
        backgroundWarning = (RelativeLayout) rootView.findViewById(R.id.layout_warning);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.list);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Match selectedMatch = (Match) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                League selectedLeague = (League) parent.getExpandableListAdapter().getGroup(groupPosition);

                submitGame(selectedMatch, selectedLeague, v);
                return false;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                int index = expandedGroups.indexOf(i);
                if (index > -1) {
                    expandedGroups.remove(index);
                } else {
                    expandedGroups.add(i);
                }
                return false;
            }
        });

        return rootView;
    }

    public void onUpdate(List<League> ls) throws Exception {
        listAdapter = new ExpandableListAdapter(getActivity(), ls);
        highlightSelections(ls);
        expandableListView.setAdapter(listAdapter);
        expandGroups();
    }

    private void highlightSelections(List<League> ls) {
        List<String> selectedMatches = DBManager.getAllMatchIds();

        for (League l : ls) {
            for (Match m : l.getMatches()) {
                if (selectedMatches.contains(m.getId())) {
                    m.toggleMark();
                }
            }
        }
    }

    private void expandGroups() {
        for (int groupIndex : expandedGroups) {
            expandableListView.expandGroup(groupIndex);
        }
    }

    @SuppressWarnings("unchecked")
    void submitGame(final Match match, final League league, final View v) {
        PostRequest<String> request = new PostRequest<String>(Request.Method.POST, activity.getResources().getString(R.string.url_sms),
            createSubscriptionDataToSend(match),
            new com.example.instantscore.volley.Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    if (DBManager.isAlreadyInDatabase(match, league)) {
                        DBManager.removeMatchFromDatabase(match, league);
                    } else {
                        DBManager.insertMatchIntoDb(match, league);
                    }
                    match.toggleMark();
                    listAdapter.notifyDataSetChanged();

                    v.findViewById(R.id.progressBar3).setVisibility(View.GONE);
                    Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NoConnectionError){
                    Toast.makeText(activity, "no connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(activity, new String(volleyError.networkResponse.data), Toast.LENGTH_LONG).show();
                    v.findViewById(R.id.progressBar3).setVisibility(View.GONE);
                }
            }
        });

        v.findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);
        MyVolley.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    private Map<String, String> createSubscriptionDataToSend(Match match) {
        Map<String, String> pairs = new HashMap<String, String>();

        pairs.put("type", "submit_game");
        pairs.put("phone_num", getFromPrefs("phonenum"));
        pairs.put("security_code", getFromPrefs("securitycode"));
        pairs.put("match_id", match.getId());
        return pairs;
    }

    private String getFromPrefs(String key) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedpreferences.getString(key, "");
    }

    void setListBackground() {
        backgroundWarning.setVisibility(RelativeLayout.VISIBLE);
        expandableListView.setVisibility(ListView.GONE);
    }

    void removeListBackground() {
        backgroundWarning.setVisibility(RelativeLayout.GONE);
        expandableListView.setVisibility(ListView.VISIBLE);
    }

}
