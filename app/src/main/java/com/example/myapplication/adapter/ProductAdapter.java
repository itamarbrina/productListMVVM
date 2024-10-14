package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ItemProductBinding;
import com.example.myapplication.models.Product;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final ArrayList<Product> productArrayList;
    private final OnRecycleViewItemClickListener onRecycleViewItemClickListener;

    public ProductAdapter(ArrayList<Product> productArrayList, OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.productArrayList = productArrayList;
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, onRecycleViewItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ViewHolder(ItemProductBinding binding, OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.editProductButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onRecycleViewItemClickListener.onEditClick(position);
                }
            });

            binding.deleteProductButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onRecycleViewItemClickListener.onDeleteClick(position);
                }
            });
        }

        public void bind(Product product) {
            binding.productId.setText(product.getTitle());
            binding.TVcategory.setText(product.getCategory());
            binding.TVprice.setText(String.format("%s%s", product.getPrice(), Currency.getInstance(Locale.getDefault()).getSymbol()));
            binding.TVdescription.setText(product.getDescription());
            Glide.with(binding.getRoot().getContext())
                    .load(product.getImageUrl())
                    .into(binding.profileImageView);
            binding.TVdescription.setOnClickListener((view)->{
               if(binding.TVdescription.getMaxLines() == 2){
                   binding.TVdescription.setMaxLines(Integer.MAX_VALUE);
               }
               else{
                   binding.TVdescription.setMaxLines(2);
               }
            });
        }
    }
}
