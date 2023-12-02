package com.shubham.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubham.expensemanager.R;
import com.shubham.expensemanager.databinding.RowAccountBinding;
import com.shubham.expensemanager.models.Account;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountsViewHolder> {
    Context context;
    ArrayList<Account> accountArrayList;

    public interface AccountClickListener{
        void onAccountSelected(Account account);
    }

    AccountClickListener accountClickListener;
    public AccountAdapter(Context context, ArrayList<Account> accountArrayList, AccountClickListener accountClickListener){
       this.context = context;
       this.accountArrayList = accountArrayList;
       this.accountClickListener = accountClickListener;
    }

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_account,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {
        Account account = accountArrayList.get(position);
        holder.binding.accountName.setText(account.getAccountName());
        holder.itemView.setOnClickListener(c->{
            accountClickListener.onAccountSelected(account);
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AccountsViewHolder extends RecyclerView.ViewHolder {

        RowAccountBinding binding;

        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowAccountBinding.bind(itemView);
        }
    }
}
