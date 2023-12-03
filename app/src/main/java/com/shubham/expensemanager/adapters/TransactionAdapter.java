package com.shubham.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubham.expensemanager.R;
import com.shubham.expensemanager.databinding.RowTransactionBinding;
import com.shubham.expensemanager.models.Category;
import com.shubham.expensemanager.models.Transaction;
import com.shubham.expensemanager.utils.Constants;
import com.shubham.expensemanager.utils.Helper;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    Context context;
    ArrayList<Transaction> transactions;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions){
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
     Transaction transaction = transactions.get(position);

     holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
     holder.binding.accountLabel.setText(transaction.getAccount());
     holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
     holder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());

        holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        holder.binding.categoryIcon.setImageTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

        holder.binding.accountLabel.setBackgroundTintList(context.getColorStateList(Constants.getAccountColor(transaction.getAccount())));

     if (transaction.getType().equals(Constants.INCOME)){
         holder.binding.transactionAmount.setTextColor(context.getColor(R.color.greenColor));
     } else if(transaction.getType().equals(Constants.EXPENSE)) {
         holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor));
     }


    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
