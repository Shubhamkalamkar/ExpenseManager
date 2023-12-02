package com.shubham.expensemanager.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import com.shubham.expensemanager.views.fragments.AddTransactionFragment;
import com.shubham.expensemanager.R;
import com.shubham.expensemanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.materialToolbar);
        getSupportActionBar().setTitle("Transactions");

        binding.floatingActionButton.setOnClickListener(c->{
            new AddTransactionFragment().show(getSupportFragmentManager(),null);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}