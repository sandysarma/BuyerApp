package com.buyer.buyerApp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.buyer.buyerApp.Activity.ExploreProjectActivity;
import com.buyer.buyerApp.Activity.ProductListActivity;
import com.buyer.buyerApp.Activity.ServiceListActivity;
import com.buyer.buyerApp.Activity.VehicleDetailsActivity;
import com.buyer.buyerApp.Adapter.Banner_PagerActivity;
import com.buyer.buyerApp.Adapter.ExploreProjectAdapter;
import com.buyer.buyerApp.Adapter.ProductsAdapter;
import com.buyer.buyerApp.Adapter.ServiceAdapter;
import com.buyer.buyerApp.Adapter.VehicleAdapter;
import com.buyer.buyerApp.Model.BannerModel;
import com.buyer.buyerApp.Model.CategoriesModel;
import com.buyer.buyerApp.Model.ExploreProjectModel;
import com.buyer.buyerApp.Model.ProductCategoryModel;
import com.buyer.buyerApp.Model.VehicleListModel;
import com.buyer.buyerApp.Model.VehicleModel;
import com.buyer.buyerApp.ModelResult.BannerResult;
import com.buyer.buyerApp.ModelResult.CatergoriesResult;
import com.buyer.buyerApp.ModelResult.ExploreProjectresult;
import com.buyer.buyerApp.ModelResult.ProductCategoryResult;
import com.buyer.buyerApp.ModelResult.VehicleListResult;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;


public class DashboardFragment extends Fragment {

    RecyclerView vehicle_recycle;
    ViewPager viewPager;
    CircleIndicator banner_indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private static final Integer[] IMAGES = {R.drawable.logo, R.drawable.splash, R.drawable.bg};
    RecyclerView recycler_exploreProject, vehicle_recyclerview, recycler_Ourservice, recycler_Ourproduct;

    ExploreProjectAdapter exploreProjectsAdapter;
    VehicleAdapter vehicleAdapter;
    ServiceAdapter servicesAdapter;
    ProductsAdapter productsAdapter;

    List<CatergoriesResult> categoriesResponseList = new ArrayList<>();
    List<ProductCategoryResult> productCategoriesResponseList = new ArrayList<>();
    List<ExploreProjectresult> exploreProjectsResponcelist = new ArrayList<>();
    List<VehicleListResult> vehicleResponcelist = new ArrayList<>();
    List<BannerResult> bannerResponcelist = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String userId = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        vehicle_recycle = view.findViewById(R.id.vehicle_recycle);

        viewPager = view.findViewById(R.id.viewPager);
        banner_indicator = view.findViewById(R.id.banner_indicator);
        recycler_exploreProject = view.findViewById(R.id.recycler_exploreProject);
        vehicle_recyclerview = view.findViewById(R.id.vehicle_recycle);
        recycler_Ourservice = view.findViewById(R.id.recycler_Ourservice);
        recycler_Ourproduct = view.findViewById(R.id.recycler_Ourproduct);


        RecyclerView.LayoutManager vehicleLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        vehicle_recyclerview.setLayoutManager(vehicleLayoutManager);
        vehicle_recyclerview.setItemAnimator(new DefaultItemAnimator());


        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");

        Log.e("Dash", userId);


        //RecyclerView.LayoutManager servicesLayoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView.LayoutManager servicesLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_Ourservice.setLayoutManager(servicesLayoutManager);
        //services_recyclerview.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recycler_Ourservice.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager productsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_Ourproduct.setLayoutManager(productsLayoutManager);
        recycler_Ourproduct.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager exploreProjectLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_exploreProject.setLayoutManager(exploreProjectLayoutManager);
        recycler_exploreProject.setItemAnimator(new DefaultItemAnimator());

        NUM_PAGES = IMAGES.length;


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


        ServiceCategoryList_Api();
        ProductCategoryList_Api();
        ExploreCatagoryList_Api();
        VehicleCatagoryList_Api();
        BannerCatagaryList_Api();


        return view;

    }

    private void BannerCatagaryList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        //progressDialog.show();


        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<BannerModel> call = service.getBannerList("12345");
        call.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<BannerModel> call, @NonNull retrofit2.Response<BannerModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        BannerModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        //  Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if (success.equalsIgnoreCase("true")) {
                            bannerResponcelist = result.getRecord();
                            //bannerResponcelist12.add()
                            viewPager.setAdapter(new Banner_PagerActivity(getActivity(), bannerResponcelist));
                            banner_indicator.setViewPager(viewPager);

                            banner_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageSelected(int position) {
                                    currentPage = position;
                                }

                                @Override
                                public void onPageScrolled(int pos, float arg1, int arg2) {
                                }

                                @Override
                                public void onPageScrollStateChanged(int pos) {
                                }
                            });

                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<BannerModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void VehicleCatagoryList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        // progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<VehicleListModel> call = service.getVehicleList("12345");
        call.enqueue(new Callback<VehicleListModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<VehicleListModel> call, @NonNull retrofit2.Response<VehicleListModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        VehicleListModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if (success.equalsIgnoreCase("true")) {
                            vehicleResponcelist = result.getRecord();
                            vehicleAdapter = new VehicleAdapter(getActivity(), vehicleResponcelist);
                            vehicle_recyclerview.setAdapter(vehicleAdapter);
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<VehicleListModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ExploreCatagoryList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        //progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ExploreProjectModel> call = service.getExploreProjectDetailList("123");
        call.enqueue(new Callback<ExploreProjectModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ExploreProjectModel> call, @NonNull retrofit2.Response<ExploreProjectModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ExploreProjectModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        //  Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if (success.equalsIgnoreCase("true")) {
                            exploreProjectsResponcelist = result.getRecord();
                            exploreProjectsAdapter = new ExploreProjectAdapter(getActivity(), exploreProjectsResponcelist);
                            recycler_exploreProject.setAdapter(exploreProjectsAdapter);
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ExploreProjectModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ServiceCategoryList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

//       if(progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
//        if (Dialog != null && Dialog.isShowing()) {
//            Dialog.dismiss();
//        }

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        //progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<CategoriesModel> call = service.getCategories("123");
        call.enqueue(new Callback<CategoriesModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CategoriesModel> call, @NonNull retrofit2.Response<CategoriesModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        CategoriesModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        //  Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();


                        if (success.equalsIgnoreCase("true")) {
                            categoriesResponseList = result.getCatergoriesResults();
                            servicesAdapter = new ServiceAdapter(getActivity(), categoriesResponseList);
                            recycler_Ourservice.setAdapter(servicesAdapter);
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<CategoriesModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void ProductCategoryList_Api() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        // progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<ProductCategoryModel> call = service.getProductCategories("123");
        call.enqueue(new Callback<ProductCategoryModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProductCategoryModel> call, @NonNull retrofit2.Response<ProductCategoryModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ProductCategoryModel result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();

                        if (success.equalsIgnoreCase("true")) {
                            productCategoriesResponseList = result.getProductCategoryResults();
                            productsAdapter = new ProductsAdapter(getActivity(), productCategoriesResponseList);
                            recycler_Ourproduct.setAdapter(productsAdapter);
                        }
                    } else {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ProductCategoryModel> call, Throwable t) {
                progressDialog.dismiss();

                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}