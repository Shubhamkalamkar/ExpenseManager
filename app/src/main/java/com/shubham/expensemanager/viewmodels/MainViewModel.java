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
    Calendar calendar;
    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
//        addTransaction();
    }

    public void getTransaction(Calendar calendar) {
        this.calendar = calendar;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        double income = 0;
        double expense = 0;
        double total = 0;

        RealmResults<Transaction> newTransactions = null;

        if (Constants.SELECTED_TAB == Constants.DAILY) {

             newTransactions = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .findAll();

             income = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

             expense = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

             total = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .sum("amount").doubleValue();



        } else if (Constants.SELECTED_TAB==Constants.MONTHLY){

            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

             newTransactions = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date",endTime)
                    .findAll();

            income = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date",endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

            expense = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date",endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

            total = realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date",endTime)
                    .sum("amount").doubleValue();
        }
        totalAmount.setValue(total);
        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        transactions.setValue(newTransactions);

    }

    public void addTransaction(Transaction transaction) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transaction);
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Bank", "Some Note Here", new Date(), 500, new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE, "Business", "Bank", "Some Note Here", new Date(), 500, new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Bank", "Some Note Here", new Date(), 500, new Date().getTime()));
        realm.commitTransaction();
    }

    public void deleteTransaction(Transaction transaction) {
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransaction(calendar);
    }

    void setupDatabase() {
        realm = Realm.getDefaultInstance();
    }

}
