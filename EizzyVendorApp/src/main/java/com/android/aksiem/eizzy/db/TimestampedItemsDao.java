package com.android.aksiem.eizzy.db;

import android.arch.lifecycle.LiveData;

import com.android.aksiem.eizzy.vo.OrderItem;
import com.android.aksiem.eizzy.vo.Timestamped;

import java.util.List;

/**
 * Created by pdubey on 14/04/18.
 */

public interface TimestampedItemsDao<T extends Timestamped> {

    void insert(T... items);
    LiveData<List<T>> getAllItems();
    LiveData<List<T>> getItemsFrom(Float timestamp);
    LiveData<List<OrderItem>> getItemsFromTo(Float start, Float end);

}
