package com.buyer.buyerApp.RetrofitApi;

import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.All_serviecs;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.Bannerlist;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.ForgotCheckPassword;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.ForgotChengePassword;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.MailInvoice;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.Project_detail_list;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.add_to_cart;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.cancelServicerequest;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.cancel_list;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.cancel_order;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.cancelorderlist_item;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.cart_lists;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.categories;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.change_password;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.check_out;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.completedOrderList;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.context_us;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.delete_cart_list;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.delete_myservice_item;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.edit_group_farmer;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.edit_individual_farmer;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.empty_cart;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.getAquote;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.get_profile_details;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.increment_decrement;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.login_group_farmer;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.login_individual_farmer;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.notification_allclear;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.notification_delete;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.notification_lists;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.order_list;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.product;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.product_categories;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.remove_all_myservice_item;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.service_request;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.service_request_list;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.services;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.signup_group_farmer;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.signup_individual_farmers;
import static com.buyer.buyerApp.RetrofitApi.ServiceUrlList.transactionrequestList;

import com.buyer.buyerApp.Model.BannerModel;
import com.buyer.buyerApp.Model.CancelListModel;
import com.buyer.buyerApp.Model.CancelServicerequetModel;
import com.buyer.buyerApp.Model.CartListModel;
import com.buyer.buyerApp.Model.CategoriesModel;
import com.buyer.buyerApp.Model.ClearAllCartitems_Model;
import com.buyer.buyerApp.Model.ClearAllNotification_Model;
import com.buyer.buyerApp.Model.CommonModel;
import com.buyer.buyerApp.Model.ContactUs_Model;
import com.buyer.buyerApp.Model.DeleteMyServiceItemModel;
import com.buyer.buyerApp.Model.ExploreProjectModel;
import com.buyer.buyerApp.Model.GetAQuote_Model;
import com.buyer.buyerApp.Model.Increment_DecrementModel;
import com.buyer.buyerApp.Model.LoginGroupModel;
import com.buyer.buyerApp.Model.NotificationDeleteModel;
import com.buyer.buyerApp.Model.NotificationList_Model;
import com.buyer.buyerApp.Model.OrderCompleteModel;
import com.buyer.buyerApp.Model.OrderListModel;
import com.buyer.buyerApp.Model.OrderList_Model;
import com.buyer.buyerApp.Model.ProceedToCheckOut_Model;
import com.buyer.buyerApp.Model.ProductCategoryModel;
import com.buyer.buyerApp.Model.ProductModel;
import com.buyer.buyerApp.Model.ProfileDetailsModel;
import com.buyer.buyerApp.Model.ServiceRequestListModel;
import com.buyer.buyerApp.Model.ServicesModel;
import com.buyer.buyerApp.Model.SignUpGroupFarmerModel;
import com.buyer.buyerApp.Model.TransactionRequestModel;
import com.buyer.buyerApp.Model.UpdateProfileModel;
import com.buyer.buyerApp.Model.VehicleListModel;
import com.buyer.buyerApp.ModelResult.CancelListResult;
import com.buyer.buyerApp.ModelResult.CancelList_Response;
import com.buyer.buyerApp.ModelResult.CancelOrderResponse;
import com.buyer.buyerApp.ModelResult.CancelOrderResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiServices {
    @FormUrlEncoded
    @POST(signup_group_farmer)
    Call<SignUpGroupFarmerModel> getSignupGroup(@Field("name_of_group") String name_of_group,
                                                @Field("name_of_leader") String name_of_leader,
                                                @Field("phone_number") String phone_number,
                                                @Field("email") String email,
                                                @Field("district") String district,
                                                @Field("chiefdom") String chiefdom,
                                                @Field("section") String section,
                                                @Field("town") String town,
                                                @Field("device_token") String device_token,
                                                @Field("token") String token,
                                                @Field("password") String password);

    @FormUrlEncoded
    @POST(signup_individual_farmers)
    Call<SignUpGroupFarmerModel> getSignupIndividual(@Field("name") String name,
                                                     @Field("phone_number") String phone_number,
                                                     @Field("email") String email,
                                                     @Field("district") String district,
                                                     @Field("chiefdom") String chiefdom,
                                                     @Field("section") String section,
                                                     @Field("town") String town,
                                                     @Field("device_token") String device_token,
                                                     @Field("token") String token,
                                                     @Field("password") String password);


    @FormUrlEncoded
    @POST(login_group_farmer)
    Call<LoginGroupModel> getLoginGroup(@Field("phone_number") String phone_number,
                                        @Field("password") String password,
                                        @Field("device_token") String device_token,
                                        @Field("token") String token);


    @FormUrlEncoded
    @POST(login_individual_farmer)
    Call<LoginGroupModel> getLoginIndividual(@Field("phone_number") String phone_number,
                                             @Field("password") String password,
                                             @Field("device_token") String device_token,
                                             @Field("token") String token);


    @FormUrlEncoded
    @POST(categories)
    Call<CategoriesModel> getCategories(@Field("token") String token);


    @FormUrlEncoded
    @POST(services)
    Call<ServicesModel> getServices(@Field("token") String token,
                                    @Field("category_id") String category_id);

    @FormUrlEncoded
    @POST(product_categories)
    Call<ProductCategoryModel> getProductCategories(@Field("token") String token);

    @FormUrlEncoded
    @POST(product)
    Call<ProductModel> getProduct(@Field("token") String token,
                                  @Field("category_id") String category_id,
                                  @Field("user_id") String user_id,
                                  @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST(Project_detail_list)
    Call<ExploreProjectModel> getExploreProjectDetailList(@Field("token") String token);

    @FormUrlEncoded
    @POST(All_serviecs)
    Call<VehicleListModel> getVehicleList(@Field("token") String token);


    @FormUrlEncoded
    @POST(Bannerlist)
    Call<BannerModel> getBannerList(@Field("token") String token);


    @FormUrlEncoded
    @POST(service_request)
    Call<CommonModel> getServiceRequest(@Field("token") String token,
                                        @Field("category_id") String category_id,
                                        @Field("service_id") String service_id,
                                        @Field("user_type") String user_type,
                                        @Field("user_id") String user_id,
                                        @Field("land_size") String land_size,
                                        @Field("land_type") String land_type,
                                        @Field("location") String location,
                                        @Field("start_date") String start_date,
                                        @Field("number_of_days") String number_of_days,
                                        @Field("total_amount") String total_amount);

    @FormUrlEncoded
    @POST(add_to_cart)
    Call<CommonModel> getAddToCart(@Field("token") String token,
                                   @Field("user_type") String user_type,
                                   @Field("user_id") String user_id,
                                   @Field("product_id") String product_id,
                                   @Field("quantity") String quantity,
                                   @Field("product_price") String product_price,
                                   @Field("total_price") String total_price,
                                   @Field("product_name") String product_name);


    @FormUrlEncoded
    @POST(edit_group_farmer)
    Call<UpdateProfileModel> getEditProfileGroup(@Field("token") String token,
                                                 @Field("user_id") String user_id,
                                                 @Field("name_of_group") String name_of_group,
                                                 @Field("name_of_leader") String name_of_leader,
                                                 @Field("phone_number") String phone_number,
                                                 @Field("email") String email,
                                                 @Field("district") String district,
                                                 @Field("chiefdom") String chiefdom,
                                                 @Field("section") String section,
                                                 @Field("town") String town);

    @FormUrlEncoded
    @POST(edit_individual_farmer)
    Call<UpdateProfileModel> getEditProfileIndividual(@Field("token") String token,
                                                      @Field("user_id") String user_id,
                                                      @Field("name") String name,
                                                      @Field("phone_number") String phone_number,
                                                      @Field("email") String email,
                                                      @Field("district") String district,
                                                      @Field("chiefdom") String chiefdom,
                                                      @Field("section") String section,
                                                      @Field("town") String town);

    @FormUrlEncoded
    @POST(change_password)
    Call<CommonModel> getChangePassword(@Field("token") String token,
                                        @Field("user_type") String user_type,
                                        @Field("user_id") String user_id,
                                        @Field("old_password") String old_password,
                                        @Field("new_password") String new_password);


    @FormUrlEncoded
    @POST(cart_lists)
    Call<CartListModel> getCartList(@Field("token") String token,
                                    @Field("user_type") String user_type,
                                    @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(empty_cart)
    Call<ClearAllCartitems_Model> getClearallCartList(@Field("token") String token,
                                                      @Field("user_type") String user_type,
                                                      @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(delete_cart_list)
    Call<CommonModel> getDeleteCartItem(@Field("token") String token,
                                        @Field("user_type") String user_type,
                                        @Field("user_id") String user_id,
                                        @Field("id") String id);

    @FormUrlEncoded
    @POST(increment_decrement)
    Call<Increment_DecrementModel> getIncre_decre_cart(@Field("token") String token,
                                                       @Field("user_id") String user_id,
                                                       @Field("user_type") String user_type,
                                                       @Field("product_id") String product_id,
                                                       @Field("quantity") String quantity
    );

    @FormUrlEncoded
    @POST(check_out)
    Call<ProceedToCheckOut_Model> getProceedCheckOut(@Field("token") String token,
                                                     @Field("user_type") String user_type,
                                                     @Field("user_id") String user_id,
                                                     @Field("product_id") String product_id,
                                                     @Field("quantity") String quantity,
                                                     @Field("delivery_mode") String delivery_mode,
                                                     @Field("total_amount") String total_amount,
                                                     @Field("name") String name,
                                                     @Field("moblie") String moblie,
                                                     @Field("address") String address,
                                                     @Field("zipcode") String zipcode);

    @FormUrlEncoded
    @POST(ForgotCheckPassword)
    Call<CommonModel> getForgotCheckPassword(@Field("token") String token,
                                             @Field("user_type") String user_type,
                                             @Field("phone_number") String phone_number);

    @FormUrlEncoded
    @POST(ForgotChengePassword)
    Call<CommonModel> getForgotChangePassword(@Field("token") String token,
                                              @Field("user_type") String user_type,
                                              @Field("password") String password,
                                              @Field("phone_number") String phone_number);

    @FormUrlEncoded
    @POST(service_request_list)
    Call<ServiceRequestListModel> getServiceRequestList(@Field("token") String token,
                                                        @Field("user_type") String user_type,
                                                        @Field("user_id") String user_id,
                                                        @Field("status") String status);

    @FormUrlEncoded
    @POST(delete_myservice_item)
    Call<DeleteMyServiceItemModel> getMyserviceDeleteItem(@Field("token") String token,
                                                          @Field("user_type") String user_type,
                                                          @Field("user_id") String user_id,
                                                          @Field("request_number") String request_number);


    @FormUrlEncoded
    @POST(cancelServicerequest)
    Call<CancelServicerequetModel> getCancelserviceRequest(@Field("token") String token,
                                                           @Field("user_type") String user_type,
                                                           @Field("user_id") String user_id,
                                                           @Field("Reason") String Reason,
                                                           @Field("request_number") String request_number);

    @FormUrlEncoded
    @POST(remove_all_myservice_item)
    Call<CommonModel> getRemoveAllMyServiceItem(@Field("token") String token,
                                                @Field("user_type") String user_type,
                                                @Field("user_id") String user_id,
                                                @Field("status") String status);

    @FormUrlEncoded
    @POST(transactionrequestList)
    Call<TransactionRequestModel> gettransactionrequestList(@Field("token") String token,
                                                            @Field("user_type") String user_type,
                                                            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(MailInvoice)
    Call<CommonModel> getMailInvooice(@Field("token") String token,
                                      @Field("user_type") String user_type,
                                      @Field("user_id") String user_id,
                                      @Field("request_number") String request_number);

    @FormUrlEncoded
    @POST(order_list)
    Call<OrderList_Model> getOrderlist(@Field("token") String token,
                                       @Field("user_id") String user_id,
                                       @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST(cancel_order)
    Call<CancelOrderResponse> getCancel_Order(@Field("token") String token,
                                              @Field("user_id") String user_id,
                                              @Field("user_type") String user_type,
                                              @Field("order_number") String order_number,
                                              @Field("product_id") String product_id,
                                              @Field("resion") String resion
    );

    @FormUrlEncoded
    @POST(get_profile_details)
    Call<ProfileDetailsModel> getProfileDetails(@Field("token") String token,
                                                @Field("user_type") String user_type,
                                                @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(cancel_list)
    Call<CancelListModel> getCancelList(@Field("token") String token,
                                        @Field("user_id") String user_id,
                                        @Field("user_type") String user_type


    );


    @FormUrlEncoded
    @POST(cancelorderlist_item)
    Call<CancelList_Response> getCancellist_item(@Field("token") String token,
                                                 @Field("user_id") String user_id,
                                                 @Field("user_type") String user_type,
                                                 @Field("id") String id);


    @FormUrlEncoded
    @POST(completedOrderList)
    Call<OrderCompleteModel> getCompleteOrderlist(@Field("token") String token,
                                                  @Field("user_id") String user_id,
                                                  @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST(notification_lists)
    Call<NotificationList_Model> getNotificationList(@Field("token") String token,
                                                     @Field("user_type") String user_type,
                                                     @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(notification_delete)
    Call<NotificationDeleteModel> getDeleteNotificationItem(@Field("token") String token,
                                                            @Field("user_type") String user_type,
                                                            @Field("user_id") String user_id,
                                                            @Field("id") String id);

    @FormUrlEncoded
    @POST(notification_allclear)
    Call<ClearAllNotification_Model> getClearAllNotification(@Field("user_type") String user_type,
                                                             @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(getAquote)
    Call<GetAQuote_Model> getquote(@Field("token") String token,
                                   @Field("first_name") String first_name,
                                   @Field("last_name") String last_name,
                                   @Field("email") String email,
                                   @Field("service") String service,
                                   @Field("phone") String phone,
                                   @Field("message") String message);

    @FormUrlEncoded
    @POST(context_us)
    Call<ContactUs_Model> getcontactus(@Field("token") String token,
                                       @Field("name") String name,
                                       @Field("email") String email,
                                       @Field("phone") String phone,
                                       @Field("message") String message,
                                       @Field("subject") String subject);
}
