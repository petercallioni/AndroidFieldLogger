package com.callioni.peter.fieldlogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    static int currentPage = 0;
    static List<String> pageNames = new ArrayList<>();
    static List<FieldLogs> fieldLogsList = new ArrayList<>();
    static String username = "";
    private DBAdapter db;

    //On back button pressed
    @Override
    public void onBackPressed() {
        createAndShowExitDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        pageNames.addAll(Arrays.asList("Home", "HP 1", "HP 2", "FO", "MZ", "Control"));
        super.onCreate(savedInstanceState);

        // makes sure the list is clear when the application starts
        fieldLogsList.clear();

        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Home_Fragment home_fragment = new Home_Fragment();
        fragmentTransaction.replace(R.id.fragment_container, home_fragment);
        fragmentTransaction.commit();

        //populate the empty list from data in the database
        db = new DBAdapter(this);

        /*This didn't stop the doubling up for some reason???*/
        if (fieldLogsList.isEmpty())
        {
            try {
                for (FieldLogs element : getObjectsFromDB()) {
                    fieldLogsList.add(new FieldLogs(element.getFieldName(),
                            element.getTime(),
                            element.getConductivity(),
                            element.getPh(),
                            element.getMoisture(),
                            element.getDissolvedOxygen()));
                }
            } catch (NullPointerException e) {
                //nothing in the database, do nothing
            }
        }


    }

    //the user clicks on a field
    public void clickField(View view) {
        currentPage = Integer.valueOf((String) view.getTag());
        showCurrentPage();
    }

    //click previous
    public void clickPrevious(View view) {
        currentPage--;
        if (currentPage == -1 || currentPage == 0)
            currentPage = 5;
        showCurrentPage();

    }

    //click next
    public void clickNext(View view) {
        currentPage++;
        if (currentPage == 6 || currentPage == 0)
            currentPage = 1;
        showCurrentPage();
    }

    //click home
    public void clickHome(View view) {
        currentPage = 0;
        Fragment frag = new Home_Fragment();
        //Communicate the field number to the fragment
        Bundle args = new Bundle();
        args.putInt("field", currentPage);
        frag.setArguments(args);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag).commit();
    }

    //shows a page
    private void showCurrentPage() {
        Fragment frag = new Field_fragment();
        //Communicate the field number to the fragment
        Bundle args = new Bundle();
        args.putInt("field", currentPage);
        frag.setArguments(args);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag).commit();
    }

    //shows the logs
    public void showFieldLogs(View view) {
        Fragment frag = new Field_List_Fragment();
        //Communicate the field number to the fragment
        Bundle args = new Bundle();
        args.putInt("field", currentPage);
        frag.setArguments(args);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag).commit();
    }

    //go back to the page you came from
    public void clickReturn(View view) {
        showCurrentPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuChoice(item);
    }

    private boolean MenuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Profile_Fragment profile_fragment = new Profile_Fragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, profile_fragment).commit();
                return true;
            case R.id.save:
                db.open();
                db.clearFieldData();
                for (FieldLogs element : fieldLogsList) {
                    long id = db.insertFieldData(element.getFieldName(),
                            element.getTime(),
                            element.getConductivity(),
                            element.getPh(),
                            element.getMoisture(),
                            element.getDissolvedOxygen());
                }
                db.close();
                return true;
            case R.id.send:
                createAndShowSendEntriesDialog();
                return true;
        }
        return false;
    }

    private ArrayList<FieldLogs> getObjectsFromDB() throws NullPointerException {
        db.open();
        Cursor cursor = db.getAllFields();
        ArrayList<FieldLogs> objectList = new ArrayList<>();
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    FieldLogs log = new FieldLogs(cursor.getString(1),
                            cursor.getString(2),
                            cursor.getFloat(3),
                            cursor.getFloat(4),
                            cursor.getInt(5),
                            cursor.getInt(6));
                    objectList.add(log);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.d("SQL Error", e.getMessage());
            return null;
        } finally {
            //release all your resources
            cursor.close();
            db.close();
        }
        return objectList;
    }

    private void createAndShowExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save entries to DB first?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.open();
                db.clearFieldData();
                for (FieldLogs element : fieldLogsList) {
                    long dbID = db.insertFieldData(element.getFieldName(),
                            element.getTime(),
                            element.getConductivity(),
                            element.getPh(),
                            element.getMoisture(),
                            element.getDissolvedOxygen());
                }
                db.close();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createAndShowSendEntriesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure? This will delete all entries.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SendEmail sendMailTask= new SendEmail (MainActivity.this);
                sendMailTask.execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public class SendEmail extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;

        public SendEmail(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending data...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Logger Data");
            StringBuilder message = new StringBuilder();
            if (username.equals(""))
            {
                message.append("Not logged in\n");
            }
            else
            {
                message.append(MainActivity.username+"\n");
            }
            for (FieldLogs logs : fieldLogsList)
            {
                message.append(logs.getFieldName()).append(" ")
                        .append(logs.getTime()).append(" ")
                        .append(logs.getConductivity()).append(" ")
                        .append(logs.getPh()).append(" ")
                        .append(logs.getMoisture()).append(" ")
                        .append(logs.getDissolvedOxygen()).append("\n");
            }
            emailIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Email"));
            fieldLogsList.clear();
            db.open();
            db.clearFieldData();
            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
