package com.example.instantscore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.instantscore.adapter.ListAdapter;
import com.example.instantscore.adapter.SeparatedListAdapter;
import com.example.instantscore.communication.DataSender;
import com.example.instantscore.database.DBManager;
import com.example.instantscore.database.InsertStatus;
import com.example.instantscore.logic.DataParser;
import com.example.instantscore.model.Game;
import com.example.instantscore.model.ListAdapterPriority;

public class MainSectionFragment extends Fragment {
    private static HashSet<String> listOfAllLiveGames = new HashSet<String>();
    private static HashSet<String> listOfAllComingGames = new HashSet<String>();
    private ListView gamesListView;
    private RelativeLayout backgroundWarning;
    private Activity activity;
    private SeparatedListAdapter separatedListAdapter;
    private String isLive = "";

    /**
     * Returns whether the game with the given id is either live or coming. If it returns false, the it's 100% correct, but if it returns true, maybe it's just because
     * the live games or coming games are not loaded at the moment of this method invocation. It works fine for our purpose because we only need to remove match from isSelected
     * list if we know for sure that it's no longer active game.
     *
     * @param gameId
     * @return
     */
    public static boolean isGameLiveOrComing(String gameId) {
        return (listOfAllLiveGames.isEmpty() || listOfAllLiveGames.contains(gameId)) || (listOfAllComingGames.isEmpty() || listOfAllComingGames.contains(gameId));
    }

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
        gamesListView = (ListView) rootView.findViewById(R.id.list);
        isLive = args.getString("live");

        gamesListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                Game item = (Game) separatedListAdapter.getItem(position);

                InsertStatus insertStatus = null;
                if (item.isSelectable()) {
                    insertStatus = DBManager.insertMatchIntoDatabase(item);
                    item.setSelected(!item.isSelected());
                }
                showMessage(item, insertStatus);

                separatedListAdapter.notifyDataSetChanged();
            }

        });

        return rootView;
    }

    private void showMessage(Game item, InsertStatus insertStatus) {
        int messageId;
        if (!item.isSelectable()) {
            messageId = R.string.already_finished;
        } else {
            if (insertStatus != InsertStatus.INSERTED_OK) {
                messageId = (insertStatus == InsertStatus.ALREADY_EXISTS ? R.string.already_selected : R.string.limit_reached);
            } else {
                messageId = R.string.already_chosen;
            }
        }
        Toast.makeText(getActivity().getApplicationContext(), activity.getResources().getString(messageId), Toast.LENGTH_SHORT).show();
    }

    public void onUpdate(String data) throws Exception {
        @SuppressWarnings("rawtypes")
        HashMap[] maps = DataParser.parseData(data);
        @SuppressWarnings("unchecked")
        HashMap<String, ArrayList<Game>> map = maps[0];
        if (isLive.equals("true")) {
            listOfAllLiveGames.clear();
            fillArrayList(map, listOfAllLiveGames);
        } else {
            listOfAllComingGames.clear();
            fillArrayList(map, listOfAllComingGames);
        }
        DBManager.removeAllInactiveMatches();
        @SuppressWarnings("unchecked")
        ArrayList<ListAdapterPriority> sortedMatches = sortMatches(map, maps[1]);
        fillListAdapter(sortedMatches);
    }

    private void fillArrayList(HashMap<String, ArrayList<Game>> map, HashSet<String> gameIds) {
        for (ArrayList<Game> listGames : map.values()) {
            for (Game game : listGames) {
                gameIds.add(game.getGameId());
            }
        }
    }

    private void fillListAdapter(List<ListAdapterPriority> sortedMatches) {
        separatedListAdapter = new SeparatedListAdapter(activity);
        for (ListAdapterPriority lap : sortedMatches) {
            separatedListAdapter.addSection(lap.getTournamentName(), lap.getListAdapter());
        }

        gamesListView.setAdapter(separatedListAdapter);
        separatedListAdapter.notifyAllAdaptersDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    void submitGames() {
        DataSender sender = new DataSender(activity.getResources().getString(R.string.url_get_submit));
        sender.execute(getSubscribtionDataToSend());
    }

    private List<NameValuePair> getSubscribtionDataToSend() {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        StringBuilder sb = new StringBuilder();
        for (Game game : DBManager.getAllMatches()) {
            sb.append("[").append(game.getGameId()).append("]");
        }

        pairs.add(new BasicNameValuePair("phonenum", getFromPrefs("phonenum")));
        pairs.add(new BasicNameValuePair("securitycode", getFromPrefs("securitycode")));
        pairs.add(new BasicNameValuePair("data", sb.toString()));
        Toast.makeText(activity, pairs.toString(), Toast.LENGTH_SHORT).show();

        return pairs;
    }

    private String getFromPrefs(String key) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedpreferences.getString(key, "");
    }

    public ArrayList<ListAdapterPriority> sortMatches(HashMap<String, ArrayList<Game>> map, HashMap<String, Integer> priorities) {
        Iterator<String> it = map.keySet().iterator();
        ArrayList<ListAdapterPriority> listAdapters = new ArrayList<ListAdapterPriority>();
        while (it.hasNext()) {
            String tourn = it.next();
            ArrayList<Game> list = map.get(tourn);
            ListAdapter adapter1 = new ListAdapter(list, activity);
            listAdapters.add(new ListAdapterPriority(adapter1, priorities.get(tourn), tourn));
        }
        Collections.sort(listAdapters);
        return listAdapters;
    }

    void setListBackground() {
        backgroundWarning.setVisibility(RelativeLayout.VISIBLE);
        gamesListView.setVisibility(ListView.GONE);
    }

    void removeListBackground() {
        backgroundWarning.setVisibility(RelativeLayout.GONE);
        gamesListView.setVisibility(ListView.VISIBLE);
    }

}
