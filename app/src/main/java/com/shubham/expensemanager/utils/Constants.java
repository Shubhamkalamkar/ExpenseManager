package com.shubham.expensemanager.utils;

import com.shubham.expensemanager.R;
import com.shubham.expensemanager.models.Category;

import java.util.ArrayList;

public class Constants {
    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";

   public static ArrayList<Category> categories;


    public static void setCategories(){
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.ic_salary, R.color.category1));
        categories.add(new Category("Business",R.drawable.ic_business, R.color.category2));
        categories.add(new Category("Investment",R.drawable.ic_investment, R.color.category3));
        categories.add(new Category("Loan",R.drawable.ic_loan, R.color.category4));
        categories.add(new Category("Rent",R.drawable.ic_rent, R.color.category5));
        categories.add(new Category("Other",R.drawable.ic_other, R.color.category6));
    }

    public static Category getCategoryDetails(String categoryName){
        for (Category cat :
                categories
        ) {
            if (cat.getCategoryName().equals(categoryName)){
                return cat;
            }
        }
        return null;
    }

    public static int getAccountColor(String accountName){
        switch (accountName){
            case "Bank":
                return R.color.bank_color;
            case "Cash":
                return R.color.cash_color;
            case "Card":
                return R.color.card_color;
            default:
                return R.color.default_color;
        }
    }
}