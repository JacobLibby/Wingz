package com.example.wingz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Easing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.w3c.dom.Attr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import android.view.MotionEvent;


public class MainActivity extends AppCompatActivity {

    public ArrayList<String> listOfCategories = new ArrayList<String>(); // arrayList of categories
    public ArrayList<budgetItem> itemList = new ArrayList<budgetItem>(); //arrayList of items
    public ArrayList<budgetCategory> categoryList = new ArrayList<budgetCategory>(); // arrayList of categories of spending
    public String favoriteGraph = "noFav";
    public int numberOfCategories;
    public String file_categories = "wingzOverviewList.txt";
    public String file_preferences = "wingzPreferences.txt";
    public String file_items_; //////////////////////////
    public String centerText = "Jacob's Budget";
    public boolean categorySelected = false;
    public Context masterContext = this;
    public float defaultRotation = 0;
    public LinearLayout addFormLL;
    public LinearLayout deleteFormLL;
    public String[] userPreferences = {"", "", "", "", "", ""}; // colors, rotation, placements,
    public Entry selectedCategoryEntry;
    public Highlight selectedCategoryHighlight;
    public float selectedCategoryX;
    public PieChart pieChart;
    public LinearLayout addCenterGoalForm;
    public String centerGoal_Title;
    public float centerGoal_amountGoal;
    public float centerGoal_amountSaved;
    public int centerGoal_Progress = 0;
    public Button deleteButton;

    private static final String TAG = "Floating Action Button";
    private static final String TRANSLATION_Y = "translationY";
    private ImageButton fab;
    private boolean expanded = false;
    private View fabAction1;
    private View fabAction2;
    private View fabAction3;
    private float offset1;
    private float offset2;
    private float offset3;



//    PieChart pieChart = findViewById(R.id.pieChart1);
//                float poopoo = pieChart.getRotationAngle();
//                String peepee = String.valueOf(poopoo);
//                Toast.makeText(masterContext, peepee, Toast.LENGTH_LONG).show();



    /*
     * check file to see
     * make file for storing data
     *
     *
     * Hide Keyboard
     *
     *  #### Emoji integration ####
     * Save rotation of graph
     *
     *  setting colorblind options for palletes
     *
     *  voice over accessibility for visually impaired
     *
     * activity transitions (explode)
     *
     *  Set of used budget in pie
     *
     *      Set these values for when editText is clicked
            unHighlight();
            categorySelected = false;
     *
     *
     *      Add more colors for more categories/entries
     *
     *      set center to be a liquid that shows the remaining balance of the budget
     *      and/or make center a button?
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favoriteGraph = "none";
        // generateList();
        generateLists();

        deleteButton = findViewById(R.id.deleteButton);

//        deleteFile(file_categories);
        addFormLL = findViewById(R.id.addForm);
        addFormLL.setVisibility(View.GONE);

        deleteFormLL = findViewById(R.id.deleteSubmission);
        deleteFormLL.setVisibility(View.GONE);
        addCenterGoalForm = findViewById(R.id.addForm_Goal);


        /*
        if (centerGoal_Progress == 0){
        read from progress file}
        else {
            // ?
        }

         */
        // set progress from file
//        EditText etSaved = (EditText) findViewById(R.id.editTextGoal_amountSaved);
//        EditText etGoal = (EditText) findViewById(R.id.editTextGoal_amountGoal);
//        ImageView img = (ImageView) findViewById(R.id.darkerCircle);
//        mImageDrawable = (ClipDrawable) img.getDrawable();
//        mImageDrawable.setLevel(0); // weird on onCreate
//        mImageDrawable.setLevel(centerGoal_Progress);


        readFile();


        // setting graph
        String favoriteGraph = checkForFavoriteGraph();
        setFavoriteGraph(favoriteGraph);

        /*
        itemAdapter = new asdfyListAdapter(this, theList);
        ListView numberList = findViewById(R.id.listView);
        numberList.setAdapter(itemAdapter);

        // Item in list is clicked
        numberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // get content of index
                index = i;
                content = theList.get(i);
                setSelectedItem(view);
                selectedLinLayout = view.findViewById(R.id.itemMenu);
            }
        });*/



        // TESTING
        PieChart pi = findViewById(R.id.pieChart1);

        setPieChart_Cat();
        pi.setVisibility(View.VISIBLE);


        // TESTING

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                addNewCategory(view);
//                updateFile();
                addFormLL.setVisibility(View.VISIBLE);
                unHighlight();
                categorySelected = false;
                deleteButton.setVisibility(View.GONE);
//                PieChart pieChart = findViewById(R.id.pieChart1);
//                float poopoo = pieChart.getRotationAngle();
//                String peepee = String.valueOf(poopoo);
//                Toast.makeText(masterContext, peepee, Toast.LENGTH_LONG).show();
            }
        });

        pieChart = findViewById(R.id.pieChart1);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // open viewing menu for category //
                categorySelected = true;
                selectedCategoryEntry = e;
                selectedCategoryHighlight = h;
                selectedCategoryX = h.getX();
                deleteButton.setVisibility(View.VISIBLE);

                //Snackbar.make(findViewById(R.id.masterConstraint), "We clicked", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                //                updateFile();

                // open detail view of cat

            }

            @Override
            public void onNothingSelected() {
                // close all menus
                categorySelected = false;
                deleteButton.setVisibility(View.GONE);

            }
        });

        FloatingActionButton fab1 = findViewById(R.id.fab);
//        pieChart.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // open up edit menu for selected
//                Toast.makeText(masterContext, "LONG CLICK", Toast.LENGTH_LONG).show();
//
//                return false;
//            }
//        });

        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//                Snackbar.make(findViewById(R.id.masterConstraint), "We clicked", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                //
                try {
                    Highlight hLight = pieChart.getHighlightByTouchPoint(me.getX(), me.getY());
//                    Toast.makeText(masterContext, hLight.toString(), Toast.LENGTH_LONG).show();
                    int index = Math.round(hLight.getX());
                    String selected = listOfCategories.get(index);
                    Float budgeted = categoryList.get(index).getAmountBudgeted();
                    Float spent = categoryList.get(index).getAmountSpent();
//                    Snackbar.make(findViewById(R.id.masterConstraint), selected, Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                    // open up itemView
                    startItemIntent(selected, index, budgeted, spent);



                }catch (Exception e){ // longclicking blankspace

                }
                //hLight.getX();
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

        float poopoo = pieChart.getRotation();
        String peepee = String.valueOf(poopoo);
//        Toast.makeText(this, peepee, Toast.LENGTH_LONG).show();


    }

    public void startItemIntent(String catName, int index, float amountBudgeted, float amountSpent){
        // creates Intent to send to receiver Activity
        Intent sendingIntent= new Intent(this,itemActivity.class);
        //String fileName = theList.get(index);
        sendingIntent.putExtra("fileName",catName);
        sendingIntent.putExtra("amountBudgeted",amountBudgeted);
        sendingIntent.putExtra("amountSpent",amountSpent);
        budgetCategory cat = categoryList.get(index);


        //sendingIntent.putExtra("currentCat", (Parcelable) cat);

        // hides buttons and starts Intent
        //hideButtons(view);
        startActivity(sendingIntent); /// NOT WORKING


    }

    public void readFile(){
        String fileName = file_categories;
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
                    listOfCategories.add(CVS[0]); // includes "TOTAL"
                    categoryList.add(new budgetCategory(CVS[0],Float.parseFloat(CVS[1]),Float.parseFloat(CVS[2])));
                    line = bf.readLine();
                }

            } catch (Exception e) {
                Toast.makeText(this,"Problem with file reading", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readPrefFile(){ // rotation, colors,
        String fileName = file_preferences;
        File file = getBaseContext().getFileStreamPath(fileName);
        if (file.exists()){
            try {
//                Toast.makeText(this, "THERE IS A PREFERENCES FILE", Toast.LENGTH_SHORT).show();
                FileInputStream inStream = this.openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inStream);
                BufferedReader bf = new BufferedReader(inputStreamReader);


                // read lines and put into ArrayList
                String line = bf.readLine(); // get rid of title // dont need?d
//                favoriteGraph = line;
//                bf.readLine();
                String[] CVS;

                //////// FIND OUT BEST WAY TO EXTRACT USER PREFs

            } catch (Exception e) {
                Toast.makeText(this,"Problem with file reading", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void updatePrefFile(){ //
        String fileName = file_preferences;


    }

    public void updateFile(){
        String fileName = file_categories;
        StringBuilder sBuilder = new StringBuilder();
        try {

            FileOutputStream outStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
                                /////////outStream.write("TITLE OF CATEGORIES".getBytes());
//            if (favoriteGraph == null){
//                outStream.write("noFav".getBytes());
//            }
//            else {
//                outStream.write(favoriteGraph.getBytes());
//            }
            for (int i=0; i < listOfCategories.size(); i++){
                // convert category List to string to bytes

                    sBuilder.append(categoryList.get(i).getTitle().toString());
                    sBuilder.append(",");
                    sBuilder.append(categoryList.get(i).getAmountBudgeted());
                    sBuilder.append(",");
                    sBuilder.append(categoryList.get(i).getAmountSpent());
                    sBuilder.append("\n");
                }
                outStream.write(sBuilder.toString().getBytes());
//
//            outStream.write("\0".getBytes());

            outStream.close();
        } catch (Exception e) {
            Toast.makeText(this,"Problem with file update", Toast.LENGTH_SHORT).show();
        }
    }

    public String checkForFavoriteGraph() {
        // read file

        // get fav graph


        //


        return "NOT DONE YET";
    }

    public void setFavoriteGraph(String graph) {
        if (graph.equals("pieChart")){
            setPieChart_Cat();
        }
    }
    public void setPieChart_Cat(){

        // set itemPieChart visibility to gone ???

        PieChart rChart = findViewById(R.id.pieChart1);

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (!listOfCategories.isEmpty()) {
            for (int i = 0; i < listOfCategories.size(); i++) {
                entries.add(new PieEntry(categoryList.get(i).getAmountBudgeted(),categoryList.get(i).getTitle())); // incorporate ,categoryList.get(i).getAmountSpent()
            }
        }
//        entries.add(new PieEntry(300, "2019"));
//        entries.add(new PieEntry(500, "2009"));
//        entries.add(new PieEntry(400, "2012"));

        PieDataSet rDataSet = new PieDataSet(entries, "Entries");
        rDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        rDataSet.setValueTextColor(Color.BLACK);
        rDataSet.setValueTextSize(16f);
        rDataSet.setSliceSpace(3f);
        rDataSet.setSelectionShift(5f);

        PieData rData = new PieData(rDataSet);

        rChart.setData(rData);
        rChart.getDescription().setEnabled(false);
        rChart.setCenterText(centerText);
        rChart.setCenterTextSize(20f);
//        rChart.setMaxAngle(180); // cool aesthetic
        try{ rChart.setMinAngleForSlices(30);}
        catch (Exception e1){
            try { rChart.setMinAngleForSlices(15); }
            catch (Exception e2) {
                Toast.makeText(masterContext, "You may have too many categories for this screen size" , Toast.LENGTH_LONG).show();
                try {rChart.setMinAngleForSlices(10);}
                catch (Exception ignored){}
            }
        }
        rChart.setDrawRoundedSlices(false); // weird Chrome looking icon if true
        rChart.setDrawHoleEnabled(true); // filled in if false
        rChart.setDrawEntryLabels(true); // false hides labels
        rChart.getLegend().setEnabled(false); // false hides legend
        rChart.animateXY(500,500);
    }

    public void toggleViewPercentages(View view){
        if (pieChart.isUsePercentValuesEnabled()){
            pieChart.setUsePercentValues(false);
        } else {
            pieChart.setUsePercentValues(true);
        }
        pieChart.animateXY(500, 500);
    }

    public void generateLists(){
        listOfCategories = new ArrayList<String>();
    }

    public void addTest(){
        String fileName = file_categories;
        StringBuilder sBuilder = new StringBuilder();
        try {

            FileOutputStream outStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            sBuilder.append("Category1, 100.00, 200.00");
            sBuilder.append("\n");
            sBuilder.append("Category2, 500.00, 50.00");
            sBuilder.append("\n");
            outStream.write(sBuilder.toString().getBytes());


            outStream.close();
        } catch (Exception e) {
            Toast.makeText(this,"Problem with file update", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCategory(View view){
        String fileName = file_categories;
        StringBuilder sBuilder = new StringBuilder();
        EditText categoryNameET = findViewById(R.id.editText);
        EditText amountBudgetedET = findViewById(R.id.editText1);
        EditText amountSpentET = findViewById(R.id.editText2);

        String categoryName = categoryNameET.getText().toString();
        Float amountBudgeted = Float.parseFloat(amountBudgetedET.getText().toString());
        Float amountSpent = Float.parseFloat(amountSpentET.getText().toString());

        try {

            FileOutputStream outStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            sBuilder.append(categoryName + "," + amountBudgeted + "," + amountSpent);
            sBuilder.append("\n");

            outStream.write(sBuilder.toString().getBytes());
            categoryList.add(new budgetCategory(categoryName,amountBudgeted,amountSpent));
            numberOfCategories++;
            listOfCategories.add(categoryName);

            updateFile();
            setPieChart_Cat();
            outStream.close();

            categoryNameET.setText("");
            amountBudgetedET.setText("");
            amountSpentET.setText("");
            addFormLL.setVisibility((View.GONE));

        } catch (Exception e) {
            Toast.makeText(this,"Problem with file update", Toast.LENGTH_SHORT).show();
        }
        // hideKeyboard(findViewById(R.id.addForm)); // crashes if keyboard is not open on execution
    }
    public void cancelButton(View view){
        addFormLL.setVisibility(View.GONE);
        // hideKeyboard(findViewById(R.id.addForm)); // crashes if keyboard is not open on execution
    }

    public void deleteCategory(View view){
//        int toRemoveX = Integer.parseInt(String.valueOf(selectedCategoryHighlight.getX()));


        if (categorySelected){
//            Toast.makeText(masterContext, selectedCategoryEntry.toString(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(masterContext, selectedCategoryHighlight.getX(), Toast.LENGTH_SHORT).show();
            int toRemoveX = (int) selectedCategoryHighlight.getX();

//            Snackbar.make(view, String.valueOf(selectedCategoryHighlight.getX()), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();

            listOfCategories.remove(toRemoveX);
            categoryList.remove(toRemoveX);
            updateFile();
            setPieChart_Cat();
            // remove from listOfCategories + categoryList
            deleteFormLL.setVisibility(View.GONE);



        }
        else {
            Toast.makeText(masterContext, "No Cat Selected for DEL", Toast.LENGTH_SHORT).show();
        }

    }

    public void unHighlight(){
        pieChart.highlightValues(null);
    }

    public void cancelButton_Delete(View view){
        deleteFormLL.setVisibility(View.GONE);
    }

    public void showDialog_Delete(View view){
        deleteFormLL.setVisibility(View.VISIBLE);
    }

    /*public void setPieChart_Items(String catFileName){ ////////////////////// needs work

        // set itemPieChart visibility to gone ???

        PieChart rChart = findViewById(R.id.pieChart_itemView);



        // create a new arraylist for each category and its items?
         // or maybe just order the file well and have a while loop with a (line != "") condition
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (!listOfCategories.isEmpty()) {
            for (int i = 0; i < listOfCategories.size(); i++) {
                entries.add(new PieEntry(categoryList.get(i).getAmountBudgeted(),categoryList.get(i).getTitle())); // incorporate ,categoryList.get(i).getAmountSpent()
            }
        }
//        entries.add(new PieEntry(300, "2019"));
//        entries.add(new PieEntry(500, "2009"));
//        entries.add(new PieEntry(400, "2012"));

        PieDataSet rDataSet = new PieDataSet(entries, "Entries");
        rDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        rDataSet.setValueTextColor(Color.BLACK);
        rDataSet.setValueTextSize(16f);
        rDataSet.setSliceSpace(3f);
        rDataSet.setSelectionShift(5f);


        PieData rData = new PieData(rDataSet);

        rChart.setData(rData);
        rChart.getDescription().setEnabled(false);
        rChart.setCenterText(centerText);
        rChart.setCenterTextSize(20f);
        rChart.setDrawRoundedSlices(false); // weird Chrome looking icon if true
        rChart.setDrawHoleEnabled(true); // filled in if false
        rChart.setDrawEntryLabels(true); // false hides labels
        rChart.animateXY(500,500);
    }*/
    public void readFile_itemView(String catName){
        //String fileName = file_categories;

        File file = getBaseContext().getFileStreamPath(catName);
        if (file.exists()){
            try {
//                Toast.makeText(this, "THERE IS A FILE (itemView)", Toast.LENGTH_SHORT).show();
                FileInputStream inStream = this.openFileInput(catName);
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
                    // listOfCategories.add(CVS[0]); // includes "TOTAL"
                    itemList.add(new budgetItem(catName, CVS[0],Float.parseFloat(CVS[1]), Date.valueOf(CVS[2])));
                    line = bf.readLine();
                }

            } catch (Exception e) {
                Toast.makeText(this,"Problem with file reading (itemView)", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void hideKeyboard(View view){ // crashes if keyboard is not open on execution
        InputMethodManager imm = (InputMethodManager) masterContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

//        // set reference variable for other functions
//        keyboardVisible = false;
    }

    public void addCenterGoal(View view){
        EditText goalTitleET = findViewById(R.id.editTextGoal_goalTitle);
        EditText goalAmountSavedET = findViewById(R.id.editTextGoal_amountSaved);
        EditText goalAmountGoalET = findViewById(R.id.editTextGoal_amountGoal);

        String goalTitle = goalTitleET.getText().toString();
        Float goalAmountSaved = Float.parseFloat(goalAmountSavedET.getText().toString());
        Float goalAmountGoal = Float.parseFloat(goalAmountGoalET.getText().toString());

        centerGoal_Title = goalTitle;
        centerGoal_amountGoal = goalAmountGoal;
        centerGoal_amountSaved = goalAmountSaved;

        // set center text
        // set progress
        // reanimate pie


    }

    public void cancelButton_Goal(View view){
        addCenterGoalForm.setVisibility(View.GONE);
    }

    public void showDialog_Goal(View view){
        addCenterGoalForm.setVisibility(View.VISIBLE);
    }



    private EditText etPercent;
    private ClipDrawable mImageDrawable;

    // a field in your class
    private int mLevel = 0;
    private int fromLevel = 0;
    private int toLevel = 0;

    public static final int MAX_LEVEL = 10000;
    public static final int LEVEL_DIFF = 100;
    public static final int DELAY = 30;

    private Handler mUpHandler = new Handler();
    private Runnable animateUpImage = new Runnable() {

        @Override
        public void run() {
            doTheUpAnimation(fromLevel, toLevel);
        }
    };

    private Handler mDownHandler = new Handler();
    private Runnable animateDownImage = new Runnable() {

        @Override
        public void run() {
            doTheDownAnimation(fromLevel, toLevel);
        }
    };

    private void doTheUpAnimation(int fromLevel, int toLevel) {
        mLevel += LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);
        if (mLevel <= toLevel) {
            mUpHandler.postDelayed(animateUpImage, DELAY);
        } else {
            mUpHandler.removeCallbacks(animateUpImage);
            MainActivity.this.fromLevel = toLevel;
        }
    }

    private void doTheDownAnimation(int fromLevel, int toLevel) {
        mLevel -= LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);
        if (mLevel >= toLevel) {
            mDownHandler.postDelayed(animateDownImage, DELAY);
        } else {
            mDownHandler.removeCallbacks(animateDownImage);
            MainActivity.this.fromLevel = toLevel;
        }
    }

    public void onClickOk(View v) {
        int temp_level = ((Integer.parseInt(etPercent.getText().toString())) * MAX_LEVEL) / 100;

        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
            return;
        }
        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
        if (toLevel > fromLevel) {
            // cancel previous process first
            mDownHandler.removeCallbacks(animateDownImage);
            MainActivity.this.fromLevel = toLevel;

            mUpHandler.post(animateUpImage);
        } else {
            // cancel previous process first
            mUpHandler.removeCallbacks(animateUpImage);
            MainActivity.this.fromLevel = toLevel;

            mDownHandler.post(animateDownImage);
        }
    }

}