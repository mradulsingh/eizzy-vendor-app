package com.android.aksiem.eizzy.vo.support.order;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pdubey on 09/04/18.
 */

public class OrderDetails implements Serializable {

    @SerializedName("items")
    @NonNull
    public final List<OrderedItem> items;

    @SerializedName("priceComponents")
    @NonNull
    public final List<PriceComponent> priceComponents;

    @SerializedName("total")
    @NonNull
    public final PriceComponent total;

    @SerializedName("paymentMethod")
    @NonNull
    public final String paymentMethod;

    public OrderDetails(@NonNull List<OrderedItem> items,
                        @NonNull List<PriceComponent> priceComponents,
                        @NonNull PriceComponent total, @NonNull String paymentMethod) {

        this.items = items;
        this.priceComponents = priceComponents;
        this.total = total;
        this.paymentMethod = paymentMethod;
    }
}
