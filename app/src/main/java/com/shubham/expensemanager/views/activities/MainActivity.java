package com.shubham.expensemanager.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;

import com.shubham.expensemanager.adapters.TransactionAdapter;
import com.shubham.expensemanager.models.Transaction;
import com.shubham.expensemanager.utils.Constants;
import com.shubham.expensemanager.views.fragments.AddTransactionFragment;
import com.shubham.expensemanager.R;
import com.shubham.expensemanager.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.materialToolbar);

        Constants.setCategories();
        getSupportActionBar().setTitle("Transactions");

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDate.setOnClickListener(c->{
            calendar.add(Calendar.DATE,1);
            updateDate();
        });

        binding.previousDate.setOnClickListener(c->{
            calendar.add(Calendar.DATE, -1);
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c->{
            new AddTransactionFragment().show(getSupportFragmentManager(),null);
        });

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","Some Note Here",new Date(),500,6));
        transactions.add(new Transaction(Constants.EXPENSE,"Business","Bank","Some Note Here",new Date(),500,4));
        transactions.add(new Transaction(Constants.INCOME,"Investment","Cash","Some Note Here",new Date(),500,7));
        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","Some Note Here",new Date(),500,8));
        transactions.add(new Transaction(Constants.EXPENSE,"Rent","Cash","Some Note Here",new Date(),500,9));
        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","Some Note Here",new Date(),500,11));

        TransactionAdapter transactionAdapter = new TransactionAdapter(this,transactions);
        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionList.setAdapter(transactionAdapter);
    }

    void updateDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        binding.currentDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}