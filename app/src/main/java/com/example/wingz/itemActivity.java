package com.example.wingz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class itemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public String fileName;
    public String fileNameTXT;
    public ArrayList<budgetCategory> categoryList = new ArrayList<budgetCategory>();
    public ArrayList<String> listOfItems = new ArrayList<String>();
    public ArrayList<budgetItem> itemList = new ArrayList<budgetItem>();
    public budgetCategory currentCategory;
    public float catTotalBudgeted;
    public float catLeftInBudget; // amount left in budget --> amountBudgeted - totalSpent
    public float catTotalSpent; // does this make sense? // updated from what is in piechart
    public PieChart pieChart;
    public Context masterContext = this;
    public View masterView;
    public LinearLayout addFormLL;
    public LinearLayout deleteFormLL;
    public int numberOfItems = 0;
    public boolean itemSelected = false;



    public Entry selectedItemEntry;
    public Highlight selectedItemHighlight;
    public float selectedItemX;




    // have csv
    // string for each item, where "Groceries|"
    /*
        // ITEM VIEW: //

        Food,BDUBS,20
        Food,Apple Bees,15



     */



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { ////////////////@NULLABLE???
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        pieChart = findViewById(R.id.pieChart2);
        Intent receivingIntent = getIntent();
        catTotalBudgeted = receivingIntent.getFloatExtra("amountBudgeted",0);
        fileName = receivingIntent.getStringExtra("fileName");
        fileNameTXT = fileName + ".txt";
        currentCategory = receivingIntent.getParcelableExtra("currentCat");
        masterView = findViewById(R.id.masterConstraint);
        deleteFormLL = findViewById(R.id.deleteFormLL);

        addFormLL = findViewById(R.id.addForm);
        addFormLL.setVisibility(View.GONE);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                addNewCategory(view);
//                updateFile();
                addFormLL.setVisibility(View.VISIBLE);
//                unHighlight();
//                itemSelected = false;
            }
        });


        readFile();
        setPieChart_Item();
        //Toast.makeText(this,"We finished onCreate", Toast.LENGTH_SHORT).show();

        ImageButton calButton = (ImageButton) findViewById(R.id.calButton);
        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        Log.v("itemActivity","Now setting the listener");
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // open viewing menu for category //
                // use logging
                Log.v("itemActivity","In onclick listener");
                itemSelected = true;
                selectedItemEntry = e;
                selectedItemHighlight = h;
                selectedItemX = h.getX();
//                Snackbar.make(findViewById(R.id.masterConstraint), "We clicked", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                //                updateFile();

                // open detail view of cat

            }


            @Override
            public void onNothingSelected() {
                Log.v("itemActivity","Nothing selected");
                // close all menus
                itemSelected = false;

            }
        });
        Log.v("itemActivity","Just set the listener");


    }

    public void toggleViewPercentages(View view){
        if (pieChart.isUsePercentValuesEnabled()){
            pieChart.setUsePercentValues(false);
        } else {
            pieChart.setUsePercentValues(true);
        }
        pieChart.animateXY(500, 500);
    }
    public void readFile(){
        String fileName = fileNameTXT;
        File file = getBaseContext().getFileStreamPath(fileName);
        if (file.exists()){
            try {
//                Toast.makeText(this, "THERE IS A FILE", Toast.LENGTH_SHORT).show();
                FileInputStream inStream = this.openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inStream);
                BufferedReader bf = new BufferedReader(inputStreamReader);


                // read lines and put into ArrayList
                String line = bf.readLine(); // get rid of title // dont need?d
//                favoriteGraph = line;
//                bf.readLine();
                String[] CVS;

                while(line != null){ /// / / / / / / / // / / /// //
                    // split readLine
                    CVS = line.split(",");
                    //itemList.add(new budgetItem(CVS[0], CVS[1], Float.parseFloat(CVS[1]), Date.valueOf(CVS[2])));
                    itemList.add(new budgetItem(CVS[0], CVS[1], Float.parseFloat(CVS[2]), Date.valueOf(CVS[3])));
                    listOfItems.add(CVS[1]);
                    line = bf.readLine();
                }
            } catch (Exception e) {
                Toast.makeText(this,"Problem with file reading", Toast.LENGTH_SHORT).show();
            }
        }
        //Snackbar.make(masterView, "file doesnt exist, bb", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    public void setPieChart_Item(){
        catTotalSpent = 0;

        // set itemPieChart visibility to gone ???

//        pieChart = findViewById(R.id.pieChart_itemView);

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (!listOfItems.isEmpty()) {
            pieChart.setVisibility(View.VISIBLE);
            for (int i = 0; i < listOfItems.size(); i++) {
                entries.add(new PieEntry(itemList.get(i).getAmountSpent(),itemList.get(i).getTitle())); // incorporate ,categoryList.get(i).getAmountSpent()
                catTotalSpent += itemList.get(i).getAmountSpent();
            }
            // adding a section for unspent money
                // set up to be optionally visible?
            catLeftInBudget = catTotalBudgeted - catTotalSpent;
            entries.add(new PieEntry(catLeftInBudget,"Remaining Budget"));
            // set specific color?
                // create array of colors in pastel pallete and insert *REMAINING* color into array at listOfItems.size()
                // be sure to remove before any updating

        } else {
            pieChart.setVisibility(View.GONE);
        }
//        entries.add(new PieEntry(300, "2019"));
//        entries.add(new PieEntry(500, "2009"));
//        entries.add(new PieEntry(400, "2012"));

        PieDataSet rDataSet = new PieDataSet(entries, "Entries");
        rDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        rDataSet.setValueTextColor(Color.WHITE);
        rDataSet.setValueTextSize(18f);
        rDataSet.setSliceSpace(3f);
        rDataSet.setSelectionShift(5f);

        PieData rData = new PieData(rDataSet);

        pieChart.setCenterTextSize(20f);
        pieChart.setData(rData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(fileName);
//        pieChart.setMaxAngle(180); // cool aesthetic
        if (catLeftInBudget > catTotalBudgeted/120) {
            try {
                pieChart.setMinAngleForSlices(30);
            } catch (Exception e1) {
                try {
                    pieChart.setMinAngleForSlices(15);
                } catch (Exception e2) {
                    Toast.makeText(masterContext, "You may have too many entries for this screen size", Toast.LENGTH_SHORT).show();
                    try {
                        pieChart.setMinAngleForSlices(10);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        pieChart.setDrawRoundedSlices(false); // weird Chrome looking icon if true
        pieChart.setDrawHoleEnabled(true); // filled in if false
        pieChart.setDrawEntryLabels(true); // false hides labels
        pieChart.getLegend().setEnabled(false); // false hides legend
        pieChart.animateXY(500,500);
    }

    public void updateFile(){
        StringBuilder sBuilder = new StringBuilder();
        try {
            FileOutputStream outStream = this.openFileOutput(fileNameTXT, Context.MODE_PRIVATE);

            for (int i=0; i<listOfItems.size();i++){
                sBuilder.append(itemList.get(i).getCategory());
                sBuilder.append(",");
                sBuilder.append(itemList.get(i).getTitle());
                sBuilder.append(",");
                sBuilder.append(itemList.get(i).getAmountSpent());
                sBuilder.append(",");
                sBuilder.append(itemList.get(i).getDate());
                sBuilder.append('\n');

            }
            outStream.write(sBuilder.toString().getBytes());
            outStream.close();
//            Toast.makeText(this,"Successful file update", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Problem with file update", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCategory(View view) throws ParseException {
        //DateFormat formatter = new SimpleDateFormat("dd/MM/yy");

        StringBuilder sBuilder = new StringBuilder();
        EditText titleET = findViewById(R.id.editText1);
        EditText amountSpentET = findViewById(R.id.editText2);
        TextView dateET = findViewById(R.id.calText);

        String title = titleET.getText().toString();
        Float amountSpent = Float.parseFloat(amountSpentET.getText().toString());
        String dateStr = dateET.getText().toString(); // Jul 6, 2020
        String monthS = dateStr.substring(0,3);
        int findComma = dateStr.indexOf(",");
        String dayS = dateStr.substring(4,findComma);
        String yearS = dateStr.substring(findComma+2);
        int monthInt = 0;
        if (monthS.equals("Jan")){
            monthInt = 0;
        }else if (monthS.equals("Feb")){
            monthInt = 1;
        } else if (monthS.equals("Mar")){
            monthInt = 2;
        } else if (monthS.equals("Apr")){
            monthInt = 3;
        } else if (monthS.equals("May")){
            monthInt = 4;
        } else if (monthS.equals("Jun")){
            monthInt = 5;
        } else if (monthS.equals("Jul")){
            monthInt = 6;
        } else if (monthS.equals("Aug")){
            monthInt = 7;
        } else if (monthS.equals("Sep")){
            monthInt = 8;
        } else if (monthS.equals("Oct")){
            monthInt = 9;
        } else if (monthS.equals("Nov")){
            monthInt = 10;
        } else {// monthS.equals("Dec"))
            monthInt = 11;
        }
        int dayInt = Integer.parseInt(dayS);
        int yearInt = Integer.parseInt(yearS);
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.MONTH, monthInt);
        selectedDate.set(Calendar.DAY_OF_MONTH, dayInt);
        selectedDate.set(Calendar.YEAR, yearInt);
        Date date = new Date(yearInt-1900,monthInt,dayInt);




        //java.util.Date date;

        //date = (java.util.Date) formatter.parse(dateStr); // 10/15/20 --> March 10, 2021


        try {

            FileOutputStream outStream = this.openFileOutput(fileNameTXT, Context.MODE_PRIVATE);
            sBuilder.append(fileName + "," + title + "," + amountSpent + "," + dateStr);
            sBuilder.append("\n");

            outStream.write(sBuilder.toString().getBytes());
            itemList.add(new budgetItem(fileName, title,amountSpent, date));
            numberOfItems++;
            listOfItems.add(title);

            updateFile();
            setPieChart_Item(); // throws error
            outStream.close();

            titleET.setText("");
            amountSpentET.setText("");
            dateET.setText("");
            addFormLL.setVisibility((View.GONE));

        } catch (Exception e) {
            Toast.makeText(this,"ERROR: Problem with file update", Toast.LENGTH_SHORT).show();
        }
        // hideKeyboard(findViewById(R.id.addForm)); // crashes if keyboard is not open on execution
    }
    public void cancelButton(View view){
        addFormLL.setVisibility(View.GONE);
        // hideKeyboard(findViewById(R.id.addForm)); // crashes if keyboard is not open on execution
    }
    public void cancelButton_Delete(View view){
        deleteFormLL.setVisibility(View.GONE);
    }

    public void showDialog_Delete(View view){
        deleteFormLL.setVisibility(View.VISIBLE);
    }
    public void unHighlight(){
        pieChart.highlightValues(null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime()); // Nov 15, 2020 // Jul 3, 2019

        TextView textView = (TextView) findViewById(R.id.calText);
        textView.setText(currentDateString);

    }

    public void deleteItem(View view){
//        int toRemoveX = Integer.parseInt(String.valueOf(selectedCategoryHighlight.getX()));


        if (itemSelected){
//            Toast.makeText(masterContext, selectedCategoryEntry.toString(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(masterContext, selectedCategoryHighlight.getX(), Toast.LENGTH_SHORT).show();
            int toRemoveX = (int) selectedItemHighlight.getX();

//            Snackbar.make(view, String.valueOf(selectedItemHighlight.getX()), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();

            listOfItems.remove(toRemoveX);
            itemList.remove(toRemoveX);
            updateFile();
            setPieChart_Item();
            // remove from listOfCategories + categoryList
            deleteFormLL.setVisibility(View.GONE);



        }
        else {
            Toast.makeText(masterContext, "No Item Selected for DEL", Toast.LENGTH_SHORT).show();
        }

    }
}
