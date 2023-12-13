package com.shubham.expensemanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.shubham.expensemanager.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    Context context;
    RealmResults<Transaction> transactions;

    public TransactionAdapter(Context context, RealmResults<Transaction> transactions){
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

     holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
             AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
             deleteDialog.setTitle("Delete Transaction");
             deleteDialog.setMessage("Are you sure to delete");
             deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     ((MainActivity)context).mainViewModel.deleteTransaction(transaction);
                 }
             });
             deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     deleteDialog.dismiss();
                 }
             });
             deleteDialog.show();
             return false;
         }
     });


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
