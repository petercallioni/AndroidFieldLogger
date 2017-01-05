package com.callioni.peter.fieldlogger;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class Field_List_Fragment extends ListFragment {

    private List<String> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.field_list_fragment, container, false);
    }

    public void onStart() {
        final int field = getArguments().getInt("field");
        //Modify label
        Button returnButton = (Button) getActivity().findViewById(R.id.return_to_x_button);
        returnButton.setText("Return to " + MainActivity.pageNames.get(field));
        super.onStart();
        list.clear();
        int length = 0;
        for (FieldLogs log : MainActivity.fieldLogsList) {
            if (log.getFieldName().equals(MainActivity.pageNames.get(MainActivity.currentPage))) {
                list.add(
                        log.getTime());
                list.add(
                        Float.toString(log.getConductivity()) + " " +
                                Float.toString(log.getPh()) + " " +
                                Integer.toString(log.getMoisture()) + " " +
                                Integer.toString(log.getDissolvedOxygen()));
                length++;
                length++;
            }
        }
        String[] listContents = new String[length];
        int iterator = 0;
        for (String entry : list) {
            listContents[iterator] = list.get(iterator);
            iterator++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                listContents);
        setListAdapter(adapter);
    }
}
