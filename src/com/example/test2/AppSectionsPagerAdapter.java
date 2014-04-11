package com.example.test2;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragmentList;
	private MainSectionFragment fragment1;
	private SelectedSectionFragment fragment2;
	
	public AppSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
		
		fragment2 = new SelectedSectionFragment();
		fragment1 = new MainSectionFragment();
		
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragment1);
		fragmentList.add(fragment2);
		
	}
	
	@Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Section " + (position + 1);
    }
    
    public void onRefresh(){
    	MainSectionFragment fragment = (MainSectionFragment)fragmentList.get(0);
    	fragment.fetchList();
    }
    
    public void onSubmit(){
    	MainSectionFragment fragment = (MainSectionFragment)fragmentList.get(0);
    	fragment.submitGames();
    }
    
}
