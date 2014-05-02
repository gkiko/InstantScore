package com.example.instantscore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;

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
					item.setSelected(!item.selected());
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
		HashMap[] maps = DataParser.parseData(data);
		HashMap<String, ArrayList<Game>> map = maps[0];
		DBManager.removeOldMatchesFrom(map);
		fillListAdapter(map, maps[1]);
	}

	private void fillListAdapter(HashMap<String, ArrayList<Game>> map, HashMap<String, Integer> priorities) {
		Iterator<String> it = map.keySet().iterator();
		separatedListAdapter = new SeparatedListAdapter(c);
		ArrayList<ListAdapterPriority> listAdapters = new ArrayList<MainSectionFragment.ListAdapterPriority>();
		while (it.hasNext()) {
			String tourn = it.next();
			ArrayList<Game> list = map.get(tourn);
			ListAdapter adapter1 = new ListAdapter(list, c);
	//		separatedListAdapter.addSection(tourn, adapter1);
			listAdapters.add(new ListAdapterPriority(adapter1, priorities.get(tourn), tourn));
		}
		Collections.sort(listAdapters, compareListAdapters);
		for(ListAdapterPriority lap : listAdapters){
			separatedListAdapter.addSection(lap.getTournamentName(), lap.getListAdapter());
		}

		gamesListView.setAdapter(separatedListAdapter);
		separatedListAdapter.notifyAllAdaptersDataSetChanged();
	}
	
	private Comparator<ListAdapterPriority> compareListAdapters = new Comparator<MainSectionFragment.ListAdapterPriority>() {

		@Override
		public int compare(ListAdapterPriority lhs, ListAdapterPriority rhs) {
			return lhs.getPriority() != rhs.getPriority() ? lhs.getPriority() - rhs.getPriority() : lhs.hashCode()-rhs.hashCode();
		}
	};
	
	class ListAdapterPriority{
		private ListAdapter listAdapter;
		private int priority;
		private String tourn;
		
		public ListAdapterPriority(ListAdapter listAdapter, int priority, String tourn){
			this.listAdapter = listAdapter;
			this.priority = priority;
			this.tourn = tourn;
		}
		
		public String getTournamentName(){
			return tourn;
		}
		
		public int getPriority(){
			return priority;
		}
		
		public ListAdapter getListAdapter(){
			return listAdapter;
		}
		
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
		
		StringBuilder sb = new StringBuilder();
		for(Game game : DBManager.getAllMatches()/*Cart.getCart()*/){
			sb.append("[").append(game.getGameId()).append("]");
		}
		
	//	pairs.add(new BasicNameValuePair("data", "phonenum="+getFromPrefs("phonenum")+"&"+"securitycode="+getFromPrefs("securitycode")+"&"
	//			+sb.toString()));
		pairs.add(new BasicNameValuePair("phonenum", getFromPrefs("phonenum")));
		pairs.add(new BasicNameValuePair("securitycode", getFromPrefs("securitycode")));
		pairs.add(new BasicNameValuePair("data", sb.toString()));
		Toast.makeText(c, pairs.toString(), Toast.LENGTH_SHORT).show();
		
		return pairs;
	}
	
	private String getFromPrefs(String key){
		SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		return sharedpreferences.getString(key, "");
	}
	
}
