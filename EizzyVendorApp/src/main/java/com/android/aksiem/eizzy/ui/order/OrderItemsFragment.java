package com.android.aksiem.eizzy.ui.order;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aksiem.eizzy.R;
import com.android.aksiem.eizzy.app.BottomNavigationFragment;
import com.android.aksiem.eizzy.binding.FragmentDataBindingComponent;
import com.android.aksiem.eizzy.databinding.OrderItemsFragmentBinding;
import com.android.aksiem.eizzy.ui.common.NavigationController;
import com.android.aksiem.eizzy.util.AutoClearedValue;

import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by pdubey on 09/04/18.
 */

public class OrderItemsFragment extends BottomNavigationFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private OrderItemsViewModel orderItemsViewModel;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<OrderItemsFragmentBinding> binding;
    AutoClearedValue<OrderItemsAdapter> adapter;

    public static OrderItemsFragment create() {
        return new OrderItemsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        OrderItemsFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.order_items_fragment, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderItemsViewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderItemsViewModel.class);

        OrderItemsAdapter adapter = new OrderItemsAdapter(dataBindingComponent,
                orderItem -> {
                    //TODO
                });
        this.adapter = new AutoClearedValue<>(this, adapter);
        binding.get().orderList.setAdapter(adapter);
        initOrdersList(orderItemsViewModel);
    }

    private void initOrdersList(OrderItemsViewModel viewModel) {
        viewModel.getOrderItems().observe(this, listResource -> {
            if (listResource != null && listResource.data != null) {
                adapter.get().replace(listResource.data);
            } else {
                adapter.get().replace(Collections.emptyList());
            }
        });
    }


}