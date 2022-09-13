package com.buyer.buyerApp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Model.GetAQuote_Model;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class GetQuoteFragment extends Fragment {

    ImageView background;

    TextView GetaQuoteTxtBtn;
    Spinner selectservicesEdt;
    String getquote;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText firstnameGetQuoteEdt, LastnamegetquoteEdt, phoneContactUsEdt, emailgqtquoteEdt, MessagegetquoteEdt, locationgqtquoteEdt, NumberOfDaysgqtquoteEdt;
    String firstNamequotetxt, lastNamequotetxt,numberOfDaysquotetxt, phoneNumberquotetxt, emailquotetxt, messagequotetxt, selectservicequotetxt, locationquotetxt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_quote, container, false);

        background = view.findViewById(R.id.background);
        selectservicesEdt = view.findViewById(R.id.selectservicesEdt);
        GetaQuoteTxtBtn = view.findViewById(R.id.GetaQuoteTxtBtn);
        firstnameGetQuoteEdt = view.findViewById(R.id.firstnameGetQuoteEdt);
        LastnamegetquoteEdt = view.findViewById(R.id.LastnamegetquoteEdt);
        phoneContactUsEdt = view.findViewById(R.id.phoneContactUsEdt);
        emailgqtquoteEdt = view.findViewById(R.id.emailgqtquoteEdt);
        MessagegetquoteEdt = view.findViewById(R.id.MessagegetquoteEdt);
        locationgqtquoteEdt = view.findViewById(R.id.locationgqtquoteEdt);
        NumberOfDaysgqtquoteEdt = view.findViewById(R.id.NumberOfDaysgqtquoteEdt);


        Glide.with(getActivity()).load(R.drawable.bg).into(background);

        final List<String> groupstatus = new ArrayList<>();
        groupstatus.add("Farm Equipment Rental");
        groupstatus.add("Contract Farming");
        groupstatus.add("Argo Dealership");

        GetQuoteFragment.spinnerAdapter group_status_adapter = new spinnerAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item);
        group_status_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        group_status_adapter.addAll(groupstatus);
        group_status_adapter.add("Select Your Service");


        selectservicesEdt.setAdapter(group_status_adapter);

        selectservicesEdt.setSelection(group_status_adapter.getCount());

        selectservicesEdt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectservicequotetxt = selectservicesEdt.getSelectedItem().toString();
//                Toast.makeText(getActivity(), status+"ABC", Toast.LENGTH_SHORT).show();

                if (selectservicequotetxt.equals("Contract Farming")) {
                    getquote = "1";
                } else {
                    getquote = "0";
                }
                if (selectservicesEdt.getSelectedView() != null) {
                    ((TextView) selectservicesEdt.getSelectedView()).setTextColor(0xFFFFFFFF);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        GetaQuoteTxtBtn.setOnClickListener(v -> {
            if (CheckedValidation()){
                firstNamequotetxt = firstnameGetQuoteEdt.getText().toString();
                lastNamequotetxt = LastnamegetquoteEdt.getText().toString();
                phoneNumberquotetxt = phoneContactUsEdt.getText().toString();
                emailquotetxt = emailgqtquoteEdt.getText().toString();
                messagequotetxt = MessagegetquoteEdt.getText().toString();
                locationquotetxt = locationgqtquoteEdt.getText().toString();
                selectservicequotetxt = selectservicesEdt.getSelectedItem().toString();
                numberOfDaysquotetxt = NumberOfDaysgqtquoteEdt.getText().toString();

//                String countryCode = phoneNumberquotetxt.substring(0,3);
//                String countryCode1 = phoneNumberquotetxt.substring(0,4);
//                String zero = phoneNumberquotetxt.substring(0,1);
//                if(phoneNumberquotetxt.length() == 8)
//                {
//                    newPhoneNumber = "+276" + phoneNumberquotetxt;
//                }
//
//                if(countryCode.equals("+91"))
//                {
//                    newPhoneNumber = phoneNumberquotetxt;
//                }
//                if(countryCode1.equals("+276"))
//                {
//                    newPhoneNumber = phoneNumberquotetxt;
//                }
//                if(zero.equals("0"))
//                {
//                    String tempString = phoneNumberquotetxt.substring(1, phoneNumberquotetxt.length());
//                    newPhoneNumber = "+91"+tempString;
//
//                }
//
//                if (phoneNumberquotetxt.length() == 10 )
//                {
//                    newPhoneNumber = "+91"+phoneNumberquotetxt;
//
//                }

                GetAQuote_Api();

            }
        });

        return view;
    }

    private boolean CheckedValidation() {
        emailquotetxt = emailgqtquoteEdt.getText().toString();
        selectservicequotetxt = selectservicesEdt.getSelectedItem().toString();

        if (firstnameGetQuoteEdt.getText().toString().equals("")){
            firstnameGetQuoteEdt.setError("Please enter your first name ");
            firstnameGetQuoteEdt.requestFocus();
            return  false;
        }else if (LastnamegetquoteEdt.getText().toString().equals("")){
            LastnamegetquoteEdt.setError("Please enter your last name ");
            LastnamegetquoteEdt.requestFocus();
            return false;
        }else if (phoneContactUsEdt.getText().toString().equals("")){
            phoneContactUsEdt.setError("Please enter your phone number");
            phoneContactUsEdt.requestFocus();
            return  false;
        }else if(phoneContactUsEdt.length() < 10){
            phoneContactUsEdt.setError("Please enter valid phone number");
            phoneContactUsEdt.requestFocus();
            return  false;
        }
        else if (emailquotetxt.equals("")) {
            Toast toast = Toast.makeText(getContext(), "Please enter your email address", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;

        }else if (!emailquotetxt.matches(emailPattern)) {
            Toast toast = Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return  false;
        }
        else  if (locationgqtquoteEdt.getText().toString().equals("")){
            locationgqtquoteEdt.setError("Location can not be blank");
            locationgqtquoteEdt.requestFocus();
            return false;
        }else  if (NumberOfDaysgqtquoteEdt.getText().toString().equals("")){
            NumberOfDaysgqtquoteEdt.setError("Please enter Number of days");
            NumberOfDaysgqtquoteEdt.requestFocus();
            return  false;
        }else  if (selectservicequotetxt.equals("Select Your Service")){

            Toast toast = Toast.makeText(getActivity(), getString(R.string.pls_select_your_servvice), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return false;

        }else  if (MessagegetquoteEdt.getText().toString().equals("")) {
            MessagegetquoteEdt.setError("Message can not be blank");
            MessagegetquoteEdt.requestFocus();
            return false;
        }

        return true;
    }

    private void GetAQuote_Api() {

        String withoutConryCodeNumberGetAQuote= "";
        withoutConryCodeNumberGetAQuote = "+91"+phoneNumberquotetxt;

        Log.e("GetAQuoteNumber",withoutConryCodeNumberGetAQuote);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams WMLP = progressDialog.getWindow().getAttributes();
        WMLP.x = 20;
        WMLP.y = 30;
        progressDialog.getWindow().setAttributes(WMLP);
        progressDialog.show();

        ApiServices service = ApiClient.getClient().create(ApiServices.class);
        retrofit2.Call<GetAQuote_Model> call = service.getquote("12345",firstNamequotetxt,lastNamequotetxt,emailquotetxt,
                selectservicequotetxt,withoutConryCodeNumberGetAQuote,messagequotetxt);
        call.enqueue(new Callback<GetAQuote_Model>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<GetAQuote_Model> call, @NonNull retrofit2.Response<GetAQuote_Model> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        GetAQuote_Model result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        firstnameGetQuoteEdt.setText("");
                        LastnamegetquoteEdt.setText("");
                        phoneContactUsEdt.setText("");
                        emailgqtquoteEdt.setText("");
                        MessagegetquoteEdt.setText("");
                        locationgqtquoteEdt.setText("");
                        NumberOfDaysgqtquoteEdt.setText("");

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
            public void onFailure(@NonNull Call<GetAQuote_Model> call, Throwable t) {
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

    public class spinnerAdapter extends ArrayAdapter<String> {
        private spinnerAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }


}