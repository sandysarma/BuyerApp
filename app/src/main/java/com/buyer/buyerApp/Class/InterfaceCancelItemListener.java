package com.buyer.buyerApp.Class;


import com.buyer.buyerApp.ModelResult.OrderList_Response;

public interface InterfaceCancelItemListener {



    void onItemClick(OrderList_Response orderList_response, String txt_rsn);
}
