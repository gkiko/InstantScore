package com.example.instantscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
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
import android.widget.Toast;

import com.example.instantscore.adapter.ListAdapter;
import com.example.instantscore.adapter.SeparatedListAdapter;
import com.example.instantscore.communication.DataFetcher;
import com.example.instantscore.communication.DataSender;
import com.example.instantscore.database.DBManager;
import com.example.instantscore.database.InsertStatus;
import com.example.instantscore.listener.CallbackListener;
import com.example.instantscore.listener.MyChangeEvent;
import com.example.instantscore.logic.DataParser;
import com.example.instantscore.model.Cart;
import com.example.instantscore.model.Game;
import com.example.instantscore.R;

public class MainSectionFragment extends Fragment implements CallbackListener {
	private ListView gamesListView;
	private DataFetcher fetcher;
	private transient Context c;
	private SeparatedListAdapter separatedListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
		gamesListView = (ListView) rootView.findViewById(R.id.list1);
		c = getActivity().getApplicationContext();

		gamesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
				Game item = (Game) separatedListAdapter.getItem(position);
				
				InsertStatus insertStatus = null;
				if(item.isSelectable()){
					insertStatus = DBManager.insertMatchIntoDatabase(item);
					item.setSelected();
				}
				showMessage(item, insertStatus);
				
				separatedListAdapter.notifyDataSetChanged();
			}
				
		});

		fetchList();
		return rootView;
	}
	
	private void showMessage(Game item, InsertStatus insertStatus){
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
		Toast.makeText(getActivity().getApplicationContext(), c.getResources().getString(messageId), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpdate(MyChangeEvent evt) {
		fetcher.removeMyChangeListener(this);
		String data = (String) evt.source;
		HashMap<String, ArrayList<Game>> map = DataParser.parseData(data);
		DBManager.removeOldMatchesFrom(map);
		fillListAdapter(map);
	}

	private void fillListAdapter(HashMap<String, ArrayList<Game>> map) {
		Iterator<String> it = map.keySet().iterator();
		separatedListAdapter = new SeparatedListAdapter(c);
		while (it.hasNext()) {
			String t = it.next();
			ArrayList<Game> list = map.get(t);
			ListAdapter adapter1 = new ListAdapter(list, c);
			separatedListAdapter.addSection(t, adapter1);
		}

		gamesListView.setAdapter(separatedListAdapter);
		separatedListAdapter.notifyAllAdaptersDataSetChanged();
	}

	void fetchList() {
		android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
		fetcher = new DataFetcher(manager);
		fetcher.addMyChangeListener(this);
		fetcher.execute((Void) null);
	}
	
	@SuppressWarnings("unchecked")
	void submitGames() {
		DataSender sender = new DataSender();
		sender.execute(getSubscribtionDataToSend());
	}
	
	private List<NameValuePair> getSubscribtionDataToSend(){
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("phonenum", getFromPrefs("phonenum")));
		pairs.add(new BasicNameValuePair("security", getFromPrefs("securitycode")));
		
		StringBuilder sb = new StringBuilder();
		for(Game game : Cart.getCart()){
			sb.append("[").append(game.getGameId()).append("]");
		}
		Toast.makeText(c, pairs.toString(), Toast.LENGTH_SHORT).show();
		pairs.add(new BasicNameValuePair("data", sb.toString()));
		
		return pairs;
	}
	
	private String getFromPrefs(String key){
		SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		return sharedpreferences.getString(key, "");
	}
	
}
