package com.android.aksiem.eizzy.ui.order;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aksiem.eizzy.R;
import com.android.aksiem.eizzy.app.AppResourceManager;
import com.android.aksiem.eizzy.app.EizzyActivity;
import com.android.aksiem.eizzy.app.NavigationFragment;
import com.android.aksiem.eizzy.binding.FragmentDataBindingComponent;
import com.android.aksiem.eizzy.databinding.OrderItemsFragmentBinding;
import com.android.aksiem.eizzy.di.ApplicationContext;
import com.android.aksiem.eizzy.ui.common.NavigationController;
import com.android.aksiem.eizzy.ui.common.ToastController;
import com.android.aksiem.eizzy.ui.toolbar.NavigationBuilder;
import com.android.aksiem.eizzy.ui.toolbar.ToolbarMenuUtil;
import com.android.aksiem.eizzy.ui.toolbar.menu.MenuActions;
import com.android.aksiem.eizzy.util.AutoClearedValue;
import com.android.aksiem.eizzy.util.Logger;
import com.android.aksiem.eizzy.vo.OrderDetailItem;
import com.android.aksiem.eizzy.vo.OrderListItem;
import com.android.aksiem.eizzy.vo.Resource;
import com.android.aksiem.eizzy.vo.TimestampedItemWrapper;

import java.util.List;

import javax.inject.Inject;

import static com.android.aksiem.eizzy.ui.toolbar.CollapsableToolbarBuilder.mainCollapsableToolbar;

/**
 * Created by pdubey on 09/04/18.
 */

public class OrderItemsFragment extends NavigationFragment {

    private final boolean runningInDummyMode = false;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private OrderItemsViewModel orderItemsViewModel;

    @Inject
    NavigationController navigationController;

    @Inject
    ToastController toastController;

    @Inject
    AppResourceManager appResourceManager;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<OrderItemsFragmentBinding> binding;
    AutoClearedValue<OrderItemsAdapter> adapter;

    public static OrderItemsFragment create() {
        return new OrderItemsFragment();
    }


    @Inject
    @ApplicationContext
    Context applicationContext;

    @Override
    public NavigationBuilder buildNavigation() {
        return mainCollapsableToolbar()
                .includeBottomNavBar(true)
                .includeDrawerNav(true)
                .toolbarTitleRes(R.string.screen_title_orders)
                .toolbarNavIconRes(R.drawable.ic_drawer_menu)
                .setToolbarNavClickListener(v -> {
                    ((EizzyActivity) getActivity()).openDrawer();
                })
                .menuRes(ToolbarMenuUtil.generateMenuFrom(R.menu.menu_settlement_fragment),
                        buildMenuActions());
    }

    private MenuActions buildMenuActions() {
        return new MenuActions.Builder()
                .action(R.id.nav_checkout, () -> {
                    navigateToCreateOrder();
                })
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        OrderItemsFragmentBinding dataBinding = DataBindingUtil.inflate(inflater,
                R.layout.order_items_fragment, container, false);
        binding = new AutoClearedValue<>(this, dataBinding);
        return wrapNavigationLayout(inflater, container, dataBinding.getRoot());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderItemsViewModel = ViewModelProviders.of(this, viewModelFactory).get(
                OrderItemsViewModel.class);
        initOrdersList();
        OrderItemsAdapter adapter = new OrderItemsAdapter(appResourceManager, dataBindingComponent,
                orderItem -> {
                    Logger.tag(OrderItemsFragment.class.getSimpleName())
                            .d("orderId = " + orderItem.orderId);
                    Logger.tag(OrderItemsFragment.class.getSimpleName())
                            .d("order_list_item::::" + orderItem.toString());
                    navigationController.navigateToOrderDetailsFragment(orderItem.orderId);
                });
        this.adapter = new AutoClearedValue<>(this, adapter);
        RecyclerView recyclerView = binding.get().orderList;
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        recyclerView.setAdapter(adapter);
    }

    private void initOrdersList() {
        if (runningInDummyMode) {
            orderItemsViewModel.getDummyOrderItems().observe(this, resource -> {
                //updateAdapter(resource);
            });
        } else {
            orderItemsViewModel.getAllOrderItems().observe(this, resource -> {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        updateAdapter(resource);
                        break;
                    case ERROR:
                        break;
                }
            });
        }
    }

    private void updateAdapter(Resource<List<TimestampedItemWrapper<OrderListItem>>> resource) {
        if (resource != null && resource.data != null) {
            adapter.get().replace(resource.data);
        } else {
            adapter.get().replace(null);
        }
    }

    private void navigateToCreateOrder() {
        orderItemsViewModel.getEizzyZones().observe(this, resource -> {
            binding.get().included.setResource(resource);
            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    navigationController.navigateToCreateOrderFragment(resource.data.data);
                    break;
                case ERROR:
                    break;
            }
        });
    }

}
