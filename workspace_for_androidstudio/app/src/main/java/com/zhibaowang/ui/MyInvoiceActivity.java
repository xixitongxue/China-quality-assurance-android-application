package com.zhibaowang.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.zhibaowang.adapter.InvoiceAdapter;
import com.zhibaowang.app.R;
import com.zhibaowang.model.Bill;

import java.util.ArrayList;

public class MyInvoiceActivity extends ZBaseActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invoice);
        initview();
    }

    private void initview() {
        listView=findViewById(R.id.listview_invoice);
        ArrayList<Bill>arrayList=new ArrayList<>();
        for(int i=0;i<20;i++){
            Bill bill=new Bill(null);
            bill.setJson(i+"");
            arrayList.add(bill);
        }
        InvoiceAdapter invoiceAdapter=new InvoiceAdapter(arrayList);
        listView.setAdapter(invoiceAdapter);

    }
    public void onBackClick(View view) {
        finish();
    }

}
