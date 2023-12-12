package com.shubham.expensemanager.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shubham.expensemanager.models.Transaction;
import com.shubham.expensemanager.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import kotlinx.coroutines.sync.Mutex;

public class MainViewModel extends AndroidViewModel {

    Realm realm;

   public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();

   public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
   public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
   public MutableLiveData<Double> totalAmount = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
        addTransaction();
    }

    public void getTransaction(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        RealmResults<Transaction> newTransactions= realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                .greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .findAll();

        double income = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                .greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .equalTo("type", Constants.INCOME)
                        .sum("amount").doubleValue();

        double expense = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                .greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .equalTo("type", Constants.EXPENSE)
                        .sum("amount").doubleValue();

                        double total = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                .greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date", new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                        .sum("amount").doubleValue();

                        totalAmount.setValue(total);
                        totalIncome.setValue(income);
                        totalExpense.setValue(expense);

        transactions.setValue(newTransactions);
    }

    public void addTransaction(){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE,"Business","Bank","Some Note Here",new Date(),500,new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Business","Bank","Some Note Here",new Date(),500,new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE,"Business","Bank","Some Note Here",new Date(),500,new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Business","Bank","Some Note Here",new Date(),500,new Date().getTime()));
        realm.commitTransaction();
    }

    void setupDatabase(){
        realm = Realm.getDefaultInstance();
    }

}
