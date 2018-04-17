/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.aksiem.eizzy.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.android.aksiem.eizzy.R;
import com.android.aksiem.eizzy.app.NavigationFragment;
import com.android.aksiem.eizzy.binding.FragmentDataBindingComponent;
import com.android.aksiem.eizzy.databinding.ValidateOtpFragmentBinding;
import com.android.aksiem.eizzy.ui.common.NavigationController;
import com.android.aksiem.eizzy.ui.toolbar.NavigationBuilder;
import com.android.aksiem.eizzy.util.AutoClearedValue;

import javax.inject.Inject;

import static com.android.aksiem.eizzy.ui.toolbar.CollapsableToolbarBuilder.mainCollapsableToolbar;

/**
 * Created by napendersingh on 31/03/18.
 */

public class ValidateOTPFragment extends NavigationFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    AutoClearedValue<ValidateOtpFragmentBinding> binding;

    private ValidateOTPViewModel validateOTPViewModel;

    protected DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @Override
    public NavigationBuilder buildNavigation() {
        return mainCollapsableToolbar()
                .toolbarTitleRes(R.string.screen_title_validate_otp)
                .toolbarSubtitleRes(R.string.screen_subtitle_validate_otp)
                .toolbarNavIconRes(R.drawable.ic_back)
                .setToolbarNavClickListener(v -> onBackPressed());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ValidateOtpFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.validate_otp_fragment, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return wrapNavigationLayout(inflater, container, dataBinding.getRoot());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        validateOTPViewModel = ViewModelProviders.of(this, viewModelFactory).get(ValidateOTPViewModel.class);
        initInputListener();
        binding.get().actionValidateOTP.setOnClickListener(v -> onValidateOTP(v));
        binding.get().setCallback(() -> validateOTPViewModel.onValidateOTP());
    }

    private void initInputListener() {
        binding.get().userid.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onValidateOTP(v);
                return true;
            }
            return false;
        });
        binding.get().userid.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                onValidateOTP(v);
                return true;
            }
            return false;
        });
    }

    private void onValidateOTP(View v) {
        String otp = binding.get().userid.getText().toString();

        // Dismiss keyboard
        dismissKeyboard(v.getWindowToken());
        validateOTPViewModel.setOTP(otp);
        validateOTPViewModel.onValidateOTP();
    }
}