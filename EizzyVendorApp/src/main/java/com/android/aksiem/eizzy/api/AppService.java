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

package com.android.aksiem.eizzy.api;

import android.arch.lifecycle.LiveData;

import com.android.aksiem.eizzy.vo.Store;
import com.android.aksiem.eizzy.vo.User;

import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * REST API access points
 */
public interface AppService {

    @POST("user/login")
    LiveData<ApiResponse<User>> doUserLogin(@Field("userId") String userId,
                                            @Field("password") String password);

    @POST("user/create")
    LiveData<ApiResponse<Store>> createUserAccount(@Header("language") String language,
                                                   @Field("businessName") String businessName,
                                                   @Field("contactPerson") String contactPerson,
                                                   @Field("countryCode") String countryCode,
                                                   @Field("contactMobile") String contactMobile,
                                                   @Field("contactEmail") String contactEmail,
                                                   @Field("password") String password,
                                                   @Field("deviceId") String deviceId,
                                                   @Field("deviceType") String deviceType);

    @POST("user/forgotpassword")
    LiveData<ApiResponse<User>> onForgotPassword(@Field("userId") String userId);

    @POST("user/validateotp")
    LiveData<ApiResponse<User>> validateOTP(@Field("otp") String otp);


    @POST("user/resetPassword")
    LiveData<ApiResponse<User>> resetPassword(@Field("password") String password);
}
