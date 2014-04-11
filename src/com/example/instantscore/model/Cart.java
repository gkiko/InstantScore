package com.example.instantscore.model;

import java.util.ArrayList;

public class Cart {
	private static ArrayList<Game> cart = new ArrayList<Game>();
	
	public static void addItem(Game game){
		cart.add(game);
	}
	
	public static Game removeOnIndex(int index){
		return cart.remove(index);
	}
	
	public static ArrayList<Game> getCart(){
		return cart;
	}
}
