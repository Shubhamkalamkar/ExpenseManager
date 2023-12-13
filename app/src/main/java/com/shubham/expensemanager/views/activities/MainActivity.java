package com.shubham.expensemanager.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.shubham.expensemanager.adapters.TransactionAdapter;
import com.shubham.expensemanager.models.Transaction;
import com.shubham.expensemanager.utils.Constants;
import com.shubham.expensemanager.utils.Helper;
import com.shubham.expensemanager.viewmodels.MainViewModel;
import com.shubham.expensemanager.views.fragments.AddTransactionFragment;
import com.shubham.expensemanager.R;
import com.shubham.expensemanager.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Calendar calendar;

    int selectedTab = 0; //daily

    public MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);


        setSupportActionBar(binding.materialToolbar);

        Constants.setCategories();
        getSupportActionBar().setTitle("Transactions");

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDate.setOnClickListener(c -> {
            if (selectedTab==Constants.DAILY){
                calendar.add(Calendar.DATE, 1);
            } else if (selectedTab==Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.previousDate.setOnClickListener(c -> {
            if (selectedTab==Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            }else if (selectedTab==Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c -> {
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

//        ArrayList<Transaction> transactions = new ArrayList<>();

//        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","Some Note Here",new Date(),500,6));
//        transactions.add(new Transaction(Constants.EXPENSE,"Business","Bank","Some Note Here",new Date(),500,4));
//        transactions.add(new Transaction(Constants.INCOME,"Investment","Cash","Some Note Here",new Date(),500,7));
//        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","Some Note Here",new Date(),500,8));
//        transactions.add(new Transaction(Constants.EXPENSE,"Rent","Cash","Some Note Here",new Date(),500,9));
//        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","Some Note Here",new Date(),500,11));


        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Snackbar.make(binding.getRoot(), tab.getText(), Snackbar.LENGTH_LONG).show();
                if (tab.getText().equals("Monthly")) {
                    selectedTab = 1;
                    updateDate();
                } else if (tab.getText().equals("Daily")) {
                    selectedTab = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mainViewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionAdapter transactionAdapter = new TransactionAdapter(MainActivity.this, transactions);
                binding.transactionList.setAdapter(transactionAdapter);
                if (transactions.size() > 0) {

                    binding.emptyImg.setVisibility(View.GONE);

                } else {
                    binding.emptyImg.setVisibility(View.VISIBLE);
                }
            }
        });

        mainViewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLabel.setText(String.valueOf(aDouble));
            }
        });

        mainViewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLabel.setText(String.valueOf(aDouble));
            }
        });

        mainViewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLabel.setText(String.valueOf(aDouble));

            }
        });

        mainViewModel.getTransaction(calendar);
    }

    public void getTransaction() {
        mainViewModel.getTransaction(calendar);
    }

    void updateDate() {
        if (selectedTab==0){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        }else {
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));

        }
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
//        binding.currentDate.setText(dateFormat.format(calendar.getTime()));
        mainViewModel.getTransaction(calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}