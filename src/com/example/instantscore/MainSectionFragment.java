package com.example.instantscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
import com.example.instantscore.communication.DataSender;
import com.example.instantscore.database.DBManager;
import com.example.instantscore.model.Game;
import com.example.instantscore.model.League;
import com.example.instantscore.model.Match;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainSectionFragment extends Fragment {
    RelativeLayout backgroundWarning;
    Activity activity;

    Gson gson = new Gson();
    ExpandableListView expandableListView;
    ExpandableListAdapter listAdapter;
    String isLive = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
        backgroundWarning = (RelativeLayout) rootView.findViewById(R.id.layout_warning);
        isLive = args.getString("live");

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.list);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Match selectedMatch = (Match)parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                submitGame(selectedMatch, v);
                return false;
            }
        });

        return rootView;
    }

    public void onUpdate(String data) throws Exception {
        List<League> ls = gson.fromJson(data, new TypeToken<List<League>>(){}.getType());

        listAdapter = new ExpandableListAdapter(getActivity(), ls);
        expandableListView.setAdapter(listAdapter);
    }

    @SuppressWarnings("unchecked")
    void submitGame(Match match, View v) {
        DataSender sender = new DataSender(activity.getResources().getString(R.string.url_sms), v, getActivity());
        sender.execute(createSubscribtionDataToSend(match));
    }

    private List<NameValuePair> createSubscribtionDataToSend(Match match) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        pairs.add(new BasicNameValuePair("type", "submit_game"));
        pairs.add(new BasicNameValuePair("phone_num", getFromPrefs("phonenum")));
        pairs.add(new BasicNameValuePair("security_code", getFromPrefs("securitycode")));
        pairs.add(new BasicNameValuePair("match_id", match.getTeam1()+" vs "+match.getTeam2()));
        Toast.makeText(activity, pairs.toString(), Toast.LENGTH_SHORT).show();

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
