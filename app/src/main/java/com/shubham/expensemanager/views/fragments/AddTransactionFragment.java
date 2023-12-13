package com.shubham.expensemanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.shubham.expensemanager.R;
import com.shubham.expensemanager.adapters.AccountAdapter;
import com.shubham.expensemanager.adapters.CategoryAdapter;
import com.shubham.expensemanager.databinding.FragmentAddTransactionBinding;
import com.shubham.expensemanager.databinding.ListDialogBinding;
import com.shubham.expensemanager.models.Account;
import com.shubham.expensemanager.models.Category;
import com.shubham.expensemanager.models.Transaction;
import com.shubham.expensemanager.utils.Constants;
import com.shubham.expensemanager.utils.Helper;
import com.shubham.expensemanager.views.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {


    public AddTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);

        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));
            transaction.setType(Constants.INCOME);
        });
        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
            transaction.setType(Constants.EXPENSE);

        });

        binding.date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.YEAR, year);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                    String dateToShow = dateFormat.format(calendar.getTime());

                    // Show Snackbar with the formatted date
                    Snackbar.make(binding.getRoot(), "Selected Date: " + dateToShow, Snackbar.LENGTH_LONG).show();

                    binding.date.setText(dateToShow);

                    transaction.setDate(calendar.getTime());
                    transaction.setId(calendar.getTime().getTime());
                }
            });
            datePickerDialog.show();
        });

        binding.category.setOnClickListener(c -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());


            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();
        });

        binding.account.setOnClickListener(c -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(500, "Cash"));
            accounts.add(new Account(400, "Kotak Bank"));
            accounts.add(new Account(588, "Yes Bank"));
            accounts.add(new Account(90, "Hdfc Bank"));

            AccountAdapter accountAdapter = new AccountAdapter(getContext(), accounts, new AccountAdapter.AccountClickListener() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountsDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
            dialogBinding.recyclerView.setAdapter(accountAdapter);

            accountsDialog.show();
        });

        binding.saveTransactionBtn.setOnClickListener(c -> {
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String note = binding.Note.getText().toString();
            if (transaction.getType().equals(Constants.EXPENSE)) {
                transaction.setAmount(amount * -1);
            }
            transaction.setAmount(amount);
            transaction.setNote(note);

            ((MainActivity) getActivity()).mainViewModel.addTransaction(transaction);
            ((MainActivity) getActivity()).getTransaction();

            dismiss();
        });

        return binding.getRoot();
    }
}