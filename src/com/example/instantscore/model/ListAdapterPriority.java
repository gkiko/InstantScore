package com.example.instantscore.model;

import com.example.instantscore.adapter.ListAdapter;

public class ListAdapterPriority implements Comparable<ListAdapterPriority>{
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

	@Override
	public int compareTo(ListAdapterPriority another) {
		return this.priority != another.priority ? this.priority - another.priority : this.hashCode() - another.hashCode();
	}
	
}
