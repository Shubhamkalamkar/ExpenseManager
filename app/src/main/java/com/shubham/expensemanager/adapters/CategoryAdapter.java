package com.shubham.expensemanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubham.expensemanager.R;
import com.shubham.expensemanager.databinding.SampleCategoryItemBinding;
import com.shubham.expensemanager.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<Category> categories;

    public interface CategoryClickListener{
        void onCategoryClicked(Category category);
    }

    CategoryClickListener categoryClickListener;
    public CategoryAdapter(Context context, ArrayList<Category> categories, CategoryClickListener categoryClickListener){
        this.context = context;
        this.categories = categories;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        Log.d("TAG", "onBindViewHolder: "+category.getCategoryName());
        holder.binding.categoryText.setText(category.getCategoryName());
        holder.binding.selectCategoryIcon.setImageResource(category.getCategoryImage());

        holder.binding.selectCategoryIcon.setImageTintList(context.getColorStateList(category.getCategoryColor()));
        holder.itemView.setOnClickListener(v -> {
            categoryClickListener.onCategoryClicked(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

     SampleCategoryItemBinding binding;

     public CategoryViewHolder(@NonNull View itemView) {
         super(itemView);
         binding = SampleCategoryItemBinding.bind(itemView);

     }
 }
}
