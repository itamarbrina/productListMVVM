package com.example.myapplication.ui.dashboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OnRecycleViewItemClickListener;
import com.example.myapplication.adapter.ProductAdapter;
import com.example.myapplication.databinding.DialogEditProductBinding;
import com.example.myapplication.databinding.DialogErrorBinding;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashboardFragment extends Fragment {


    private FragmentDashboardBinding binding;
    private final ArrayList<Product> productsArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private DashboardViewModel dashboardViewModel;
    private ProductAdapter productAdapter;
    private NavController navController;
    FloatingActionButton buttonAddUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        navController = NavHostFragment.findNavController(this);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = binding.progressBar;
        recyclerView = binding.recyclerView;
        buttonAddUser = binding.buttonAddProduct;
        buttonAddUser.setOnClickListener(v -> showEditUserDialog(-1));

        setupRecyclerView();
        dashboardViewModel.fetchProducts();
        observeViewModel();

        return root;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(productsArrayList, new OnRecycleViewItemClickListener() {
            @Override
            public void onEditClick(int position) {
                showEditUserDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                dashboardViewModel.deleteUser(productsArrayList.get(position));
                productsArrayList.remove(position);
                productAdapter.notifyItemRemoved(position);
            }
        });
        recyclerView.setAdapter(productAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void observeViewModel() {
        dashboardViewModel.getProgressBarLiveData().observe(getViewLifecycleOwner(), progressBar::setVisibility);

        dashboardViewModel.getProductsLiveData().observe(getViewLifecycleOwner(), usersList -> {
            if (usersList != null) {
                productsArrayList.clear();
                productsArrayList.addAll(usersList);
                buttonAddUser.setVisibility(View.VISIBLE);
                productAdapter.notifyDataSetChanged();
            }
        });

        dashboardViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                showErrorDialog(errorMessage);
            }
        });
    }

    private void showEditUserDialog(int position) {
        DialogEditProductBinding dialogBinding = DialogEditProductBinding.inflate(getLayoutInflater());
        Product product;
        if (position == -1) {
            dialogBinding.editProductHeader.setText(getText(R.string.add_product));
            product = new Product();
            product.setId(productsArrayList.get(productsArrayList.size() - 1).getId() + 1);
        } else {
            product = productsArrayList.get(position);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogBinding.getRoot());


        dialogBinding.editTextTitle.setText(product.getTitle());
        dialogBinding.editTextDescription.setText(product.getDescription());
        dialogBinding.editTextCategory.setText(product.getCategory());
        dialogBinding.editTextPrice.setText(product.getPrice());
        dialogBinding.editTextImage.setText(product.getImageUrl());


        AlertDialog dialog = builder.create();

        Product finalUser = product;
        dialogBinding.buttonSaveUser.setOnClickListener(v -> {

            Map<String, String> errorMessages = new HashMap<>();

            finalUser.setTitle(Objects.requireNonNull(dialogBinding.editTextTitle.getText()).toString().trim());
            finalUser.setDescription(Objects.requireNonNull(dialogBinding.editTextDescription.getText()).toString().trim());
            finalUser.setCategory(Objects.requireNonNull(dialogBinding.editTextCategory.getText()).toString().trim());
            finalUser.setPrice(Objects.requireNonNull(dialogBinding.editTextPrice.getText()).toString().trim());
            finalUser.setImageUrl(Objects.requireNonNull(dialogBinding.editTextImage.getText()).toString().trim());

            boolean isValid = dashboardViewModel.isUserValid(finalUser, errorMessages);

            dialogBinding.editTextDescription.setError(errorMessages.get("descriptionError"));
            dialogBinding.editTextTitle.setError(errorMessages.get("titleError"));
            dialogBinding.editTextPrice.setError(errorMessages.get("priceError"));
            dialogBinding.editTextCategory.setError(errorMessages.get("categoryError"));


            if (isValid) {
                dashboardViewModel.insertUser(finalUser);
                if (position == -1) {
                    productsArrayList.add(finalUser);
                    productAdapter.notifyItemInserted(productsArrayList.size() - 1);
                } else {
                    productAdapter.notifyItemChanged(position);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showErrorDialog(String errorMessage) {
        DialogErrorBinding dialogBinding = DialogErrorBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogBinding.getRoot());

        dialogBinding.errorMessage.setText(errorMessage);

        AlertDialog dialog = builder.create();

        dialogBinding.retryButton.setOnClickListener(v -> {
            Log.d("DashboardFragment", "showEditUserDialog: retry button clicked");
            dashboardViewModel.fetchProducts();
            dialog.dismiss();
        });
        dialogBinding.backToHomeButton.setOnClickListener(v -> {
            dialog.dismiss();
            navController.popBackStack();
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}