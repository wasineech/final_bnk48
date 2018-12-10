package com.myweb.final_bnk48;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText txtName,txtDate;
    private Spinner price;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox cbOne,cbTwo,cbThree,cbFour;
    private Button btnSave;
    private ListView dataView;
    private MySQLConnect mySQLConnect;
    private List<String> items;
    private ArrayAdapter<String> adt;
    int amount,intPrice,total;
    String totalStr;
    String Pstr;
    String show = "";
    String strCb = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        update();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);

                if (cbOne.isChecked()){
                    strCb = strCb+cbOne.getText();
                }
                if (cbTwo.isChecked()){
                    strCb = strCb+cbTwo.getText();
                }
                if (cbThree.isChecked()){
                    strCb = strCb+cbThree.getText();
                }
                if (cbFour.isChecked()){
                    strCb = strCb+cbFour.getText();
                }

                int total = Integer.parseInt(strCb)*Integer.parseInt(Pstr);
                String totalStr = Integer.toString(total);


                mySQLConnect.sentData(txtName.getText().toString(),radioButton.getText().toString(),txtDate.getText().toString(),strCb,Pstr,totalStr);
                //แบบเดิมที่รวมทุกตัวในitemเดียว
                items.add(txtName.getText().toString()+"\n"+
                radioButton.getText().toString()+"\n"+txtDate.getText().toString()+
                "\n"+strCb+"\n"+price.getSelectedItem().toString()+"\n"+totalStr);
                //แบบเแยกitemเ
                /*
                items.add(txtName.getText().toString());
                items.add(radioButton.getText().toString());
                items.add(txtDate.getText().toString());
                items.add(strCb);
                items.add(price.getSelectedItem().toString());
                items.add(totalStr);
                adt.notifyDataSetChanged();*/
            }
        });
    }

    public void update(){
        items = mySQLConnect.getData();
        adt = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, items);
        dataView.setAdapter(adt);
    }

    public void init(){
        txtName = findViewById(R.id.txtName);
        txtDate = findViewById(R.id.txtDate);
        price = (Spinner) findViewById(R.id.price);
        btnSave = findViewById(R.id.btnSave);
        dataView = findViewById(R.id.dataView);
        radioGroup = findViewById(R.id.radio);
        cbOne = (CheckBox) findViewById(R.id.one);
        cbTwo = (CheckBox) findViewById(R.id.two);
        cbThree = (CheckBox) findViewById(R.id.three);
        cbFour = (CheckBox) findViewById(R.id.four);


        final String[] priceStr = getResources().getStringArray(R.array.price);
        ArrayAdapter<String> adapterDept = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, priceStr);

        price.setAdapter(adapterDept);
        price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id){

                Pstr = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*amount = Integer.parseInt(strCb);
        intPrice = Integer.parseInt(Pstr);

        total = amount*intPrice;

        totalStr = Integer.toString(total);

*/

        mySQLConnect = new MySQLConnect(MainActivity.this);
    }
}
