package com.example.jcowan_countbook;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Counter> counterList = new ArrayList<>();
    public TextView totalCounters;
    public Button newCounterButton;
    private static final String FILENAME = "CounterFile.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the components from activity_main
        totalCounters = (TextView) findViewById(R.id.totalCounters);;
        ListView counterListView = (ListView) findViewById(R.id.CounterListView);

        counterList = loadFromFile();

        CustomAdapter customAdapter= new CustomAdapter();
        counterListView.setAdapter(customAdapter);

        //Define rules for new Counter button calling newCounter method
        newCounterButton = (Button) findViewById(R.id.newCounterButton);
        newCounterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCounter(view);
            }
        });
        customAdapter.notifyDataSetChanged();
    }

    //called when data sets are changed to allow for data to be persistent
    private void saveInFile(ArrayList<Counter> listToSave) {
        SharedPreferences sp = getSharedPreferences(FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor spE = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listToSave);
        spE.putString("newJson",json);
        spE.apply();
    }

    //loads persistent data
    private ArrayList<Counter> loadFromFile(){
        ArrayList<Counter> counterListLoad = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences(FILENAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("newJson","");
        //if no file has been saved an empty counterList is returned
        //else a list containing old information is loaded into the counterList
        if(json.isEmpty()){
            counterListLoad = new ArrayList<>();
        }else{
            Type type = new TypeToken<ArrayList<Counter>>(){}.getType();
            counterListLoad = gson.fromJson(json,type);
        }
        return counterListLoad;
    }

    //Called when the new counter button is pressed
    //This prompts the user to enter the initial values for their counter
    public void newCounter(View view){
        //get input_prompt view
        LayoutInflater inflater = LayoutInflater.from(this);
        View inputView = inflater.inflate(R.layout.input_prompt, null);

        //create  an alert dialogue and fill it with inputview
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inputView);

        //get all editTexts in inputView
        final EditText name = (EditText) inputView.findViewById(R.id.EditName);
        final EditText value = (EditText) inputView.findViewById(R.id.EditValue);
        final EditText comment = (EditText) inputView.findViewById(R.id.EditComment);

        //Set up user interaction with the alert dialog
        builder
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Override the positive button "Confirm"
        //so that it will not confirm until all necessary
        //values are filled
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //only dismiss if there is a name and a value input
                if(!name.getText().toString().equals("") && !value.getText().toString().equals("")){
                    //change count input to Type int
                    int initialValue = Integer.parseInt(value.getText().toString());

                    //Check if the user has input a comment or not
                    if(comment.getText().toString().equals("")){
                        Counter tempCounter = new Counter(name.getText().toString(),initialValue);
                        counterList.add(tempCounter);
                    }else{
                        Counter tempCounter = new Counter(name.getText().toString(),comment.getText().toString(),initialValue);
                        counterList.add(tempCounter);
                    }
                    totalCounters.setText(String.valueOf(counterList.size()));
                    alertDialog.dismiss();
                    saveInFile(counterList);
                }

            }
        });
    }


    //CustomAdapter controls all parts of the list
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return counterList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            //get all parts of a list_element
            view = getLayoutInflater().inflate(R.layout.list_element,null);

            TextView counterListID = (TextView)view.findViewById(R.id.counterListID);
            TextView counterValue = (TextView)view.findViewById(R.id.counterValue);
            TextView dateValue = (TextView)view.findViewById(R.id.lastEdit);

            Button incrementCount = (Button)view.findViewById(R.id.incrementCounterButton);
            Button decrementCount = (Button)view.findViewById(R.id.decrementCounterButton);
            Button moreInformation = (Button)view.findViewById(R.id.moreInformationButton);
            ImageButton resetValue = (ImageButton)view.findViewById(R.id.resetValueButton);

            counterValue.setText(counterList.get(i).getCurrentValueAsString());
            counterListID.setText(counterList.get(i).getName());
            dateValue.setText(counterList.get(i).getLastEditAsString());

            //when the reset button is clicked
            //set current value to initial value
            resetValue.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    counterList.get(i).setCurrentValue(counterList.get(i).getInitialValue());
                    notifyDataSetChanged();
                    saveInFile(counterList);
                }
            });

            //when incrementCount is pressed add one to the current value
            incrementCount.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int count = counterList.get(i).getCurrentValue();
                    count++;
                    counterList.get(i).setCurrentValue(count);
                    notifyDataSetChanged();
                    saveInFile(counterList);

                }
            });

            //when decrementCount is pressed subtract one to the current value
            decrementCount.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int count = counterList.get(i).getCurrentValue();
                    count--;
                    counterList.get(i).setCurrentValue(count);
                    notifyDataSetChanged();
                    saveInFile(counterList);
                }
            });

            //opens a new alert dialogue to show more information
            moreInformation.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    //inflater counter_information into the alertDialog
                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                    View informationView = inflater.inflate(R.layout.counter_information, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder((Activity) view.getContext());
                    builder.setView(informationView);

                    //get all elements of counter information
                    final TextView initialValue = (TextView) informationView.findViewById(R.id.InitialValue);
                    final TextView lastEdit = (TextView) informationView.findViewById(R.id.LastEditView);

                    final EditText currentValue = (EditText) informationView.findViewById(R.id.currentValue);
                    final EditText editName = (EditText) informationView.findViewById(R.id.editName);
                    final EditText comment = (EditText) informationView.findViewById(R.id.Comment);

                    Button deleteList = (Button) informationView.findViewById(R.id.deleteCounter);

                    //set all information
                    lastEdit.setText(counterList.get(i).getLastEditAsString());
                    editName.setText(counterList.get(i).getName());
                    comment.setText(counterList.get(i).getComment());

                    initialValue.setText(counterList.get(i).getInitialValueAsString());
                    currentValue.setText(counterList.get(i).getCurrentValueAsString());


                    //set up positive and negative buttons
                    builder
                            .setCancelable(false)
                            .setPositiveButton("Confirm Changes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("Cancel Changes", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i){
                                    dialogInterface.cancel();
                                }
                            });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    deleteList.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            counterList.remove(i);
                            totalCounters.setText(String.valueOf(counterList.size()));
                            notifyDataSetChanged();
                            saveInFile(counterList);
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            //only dismiss if there is a name and a value input
                            if(!editName.getText().toString().equals("") && !initialValue.getText().toString().equals("") && !currentValue.getText().toString().equals("")){
                                int value1 = Integer.parseInt(initialValue.getText().toString());
                                int value2 = Integer.parseInt(currentValue.getText().toString());
                                counterList.get(i).setName(editName.getText().toString());
                                counterList.get(i).setInitialValue(value1);
                                counterList.get(i).setCurrentValue(value2);

                                //Check if the user has input a comment or not
                                if(comment.getText().toString().equals("")){
                                    counterList.get(i).setComment("No Comment");
                                }else{
                                    counterList.get(i).setComment(comment.getText().toString());
                                }
                                Date date = new Date();
                                counterList.get(i).setLastEdit(date);
                                alertDialog.dismiss();
                                notifyDataSetChanged();
                                saveInFile(counterList);
                            }

                        }
                    });
                }
            });
            return view;
        }
    }

}

