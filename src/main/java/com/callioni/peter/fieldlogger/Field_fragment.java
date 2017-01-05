package com.callioni.peter.fieldlogger;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Field_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.field_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final int field = getArguments().getInt("field");
        //Modify label
        TextView fieldLabel = (TextView) getActivity().findViewById(R.id.fieldName);
        fieldLabel.setText(MainActivity.pageNames.get(field));

        Button saveLogButton = (Button) getActivity().findViewById(R.id.saveLogButton);
        final EditText conductivityInput = (EditText) getActivity().findViewById(R.id.enterConductivity);
        final EditText phInput = (EditText) getActivity().findViewById(R.id.enterPH);
        final EditText moistureInput = (EditText) getActivity().findViewById(R.id.enterMoisture);
        final EditText dissolvedOxygenInput = (EditText) getActivity().findViewById(R.id.enterDisOxy);

        saveLogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean success = true;
                float conductivity = 0;
                float ph = 0;
                int moisture = 0;
                int dissolvedOxygen = 0;
                try {
                    EditText conductivityInput = (EditText) getActivity().findViewById(R.id.enterConductivity);
                    conductivity = Float.parseFloat(conductivityInput.getText().toString());
                    if (conductivity > (float) 1000 || conductivity < (float) 0) {
                        Toast.makeText(getActivity().getBaseContext(), "Entry not saved, conductivity out of bounds",
                                Toast.LENGTH_SHORT).show();
                        success = false;
                    }

                    EditText phInput = (EditText) getActivity().findViewById(R.id.enterPH);
                    ph = Float.parseFloat(phInput.getText().toString());
                    if (ph > (float) 14 || ph < (float) 0) {
                        Toast.makeText(getActivity().getBaseContext(), "Entry not saved, pH out of bounds",
                                Toast.LENGTH_SHORT).show();
                        success = false;
                    }

                    EditText moistureInput = (EditText) getActivity().findViewById(R.id.enterMoisture);
                    moisture = Integer.parseInt(moistureInput.getText().toString());
                    if (moisture > 100 || moisture < 0) {
                        Toast.makeText(getActivity().getBaseContext(), "Entry not saved, moisture out of bounds",
                                Toast.LENGTH_SHORT).show();
                        success = false;
                    }

                    EditText dissolvedOxygenInput = (EditText) getActivity().findViewById(R.id.enterDisOxy);
                    dissolvedOxygen = Integer.parseInt(dissolvedOxygenInput.getText().toString());
                    if (dissolvedOxygen > 100 || dissolvedOxygen < 0) {
                        Toast.makeText(getActivity().getBaseContext(), "Entry not saved, dissolved oxygen out of bounds",
                                Toast.LENGTH_SHORT).show();
                        success = false;
                    }
                } catch (NumberFormatException exception) {
                    Toast.makeText(getActivity().getBaseContext(), "Entry not saved to to conversion error",
                            Toast.LENGTH_SHORT).show();
                    success = false;
                }
                if (success) {
                    Calendar calendar = Calendar.getInstance();
                    String format = "%02d/%02d/%4d/ %02d:%02d:%02d";
                    String time = String.format(format,
                            calendar.get(Calendar.DATE),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            calendar.get(Calendar.SECOND));
                    FieldLogs entry = new FieldLogs(MainActivity.pageNames.get(field),time, conductivity, ph, moisture, dissolvedOxygen);
                    MainActivity.fieldLogsList.add(entry);
                    Toast.makeText(getActivity().getBaseContext(), "Entry saved successfully",
                            Toast.LENGTH_SHORT).show();
                    conductivityInput.setText("");
                    phInput.setText("");
                    moistureInput.setText("");
                    dissolvedOxygenInput.setText("");
                }
            }
        });

    }
}
