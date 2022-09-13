package com.buyer.buyerApp.Class;

import com.buyer.buyerApp.ModelResult.CartListResponse;


public interface InterfacrItemClickListener {

    void onItemClick(String id, CartListResponse cartListResponse, int position);

    void onItemClickChange(int count, CartListResponse cartListResponse, int position);
    void onDecrementClick(int count, CartListResponse cartListResponse, int position);
}
