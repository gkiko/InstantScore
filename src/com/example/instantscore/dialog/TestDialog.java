package com.example.instantscore.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.instantscore.R;
import com.example.instantscore.adapter.ItemAdapter;
import com.example.instantscore.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkiko on 8/25/14.
 */
public class TestDialog extends DialogPreference {
    private static List<Item> itemList;
    private Item selectedItem;
    private int itemIndex;
    private String editTextValue;

    private EditText numberTv;

    public TestDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.test_layout);
    }

    @Override
    protected void onBindDialogView(View view) {
        if(itemList == null){
            readItems();
        }

        final Spinner p = (Spinner)view.findViewById(R.id.spinner);
        ItemAdapter adapter = new ItemAdapter(getContext(), itemList);
        p.setAdapter(adapter);
        p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (Item)p.getSelectedItem();
                itemIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        itemIndex = prefs.getInt("item_index", 0);
        editTextValue = prefs.getString(getKey(),"").substring(4);

        p.setSelection(itemIndex);
        numberTv = (EditText)view.findViewById(R.id.number_editText);
        numberTv.setText(editTextValue);

        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editor.putString(getKey(), selectedItem.getCountryPrefix()+editTextValue);
            editor.putInt("item_index", itemIndex);
            editor.commit();
        }

        super.onDialogClosed(positiveResult);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
            editTextValue = numberTv.getText().toString();
            default:
                break;
        }
        super.onClick(dialog, which);
    }

    private void readItems(){
        TypedArray countryCodes = getContext().getResources().obtainTypedArray(R.array.countries);
        itemList = new ArrayList<Item>();

        String countryPrefix, countryName;
        int cpt = countryCodes.length(), flagId;
        for (int i = 0; i < cpt; ++i) {
            int id = countryCodes.getResourceId(i, -1);
            TypedArray arr2 = getContext().getResources().obtainTypedArray(id);

            countryPrefix = arr2.getString(0);
            countryName = arr2.getString(1);
            flagId = arr2.getResourceId(2, -1);
            itemList.add(new Item(countryPrefix, countryName, flagId));
            arr2.recycle();
        }

        countryCodes.recycle();
    }
}
