package com.buyer.buyerApp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.buyerApp.Model.ContactUs_Model;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.ApiClient;
import com.buyer.buyerApp.RetrofitApi.ApiServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ContactUsFragment extends Fragment {
    String strName, strPhoneNumber, strEmail, strSubject, strMessage;
    EditText nameContactUsEdt, phoneContactUsEdt, emailContacTEdt, subjectContactEdt, messageContactEdt;
    TextView contactUsBtn;
    ImageView emailusimail, phoneusimage;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        contactUsBtn = view.findViewById(R.id.contactUsBtn);
        nameContactUsEdt = view.findViewById(R.id.nameContactUsEdt);
        phoneContactUsEdt = view.findViewById(R.id.phoneContactUsEdt);
        emailContacTEdt = view.findViewById(R.id.emailContacTEdt);
        subjectContactEdt = view.findViewById(R.id.subjectContactEdt);
        messageContactEdt = view.findViewById(R.id.messageContactEdt);
        emailusimail = view.findViewById(R.id.emailusimail);
        phoneusimage = view.findViewById(R.id.phoneusimage);

        emailusimail.setOnClickListener(view1 -> {
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT,"subject");
            intent.putExtra(Intent.EXTRA_EMAIL,"email");
            intent.putExtra(Intent.EXTRA_TEXT,"text");
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent,"choosee  an email"));
        });

        phoneusimage.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:1234567890"));
            startActivity(intent);
        });



        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_validatio())
                {
                    strName = nameContactUsEdt.getText().toString();
                    strPhoneNumber = phoneContactUsEdt.getText().toString();
                    strEmail = emailContacTEdt.getText().toString();
                    strMessage = messageContactEdt.getText().toString();
                    strSubject = subjectContactEdt.getText().toString();


                    ContactUs_Api();

                }
            }
        });


        return view;
    }

    private boolean check_validatio()
    {
        strEmail = emailContacTEdt.getText().toString();

        if(nameContactUsEdt.getText().toString().equals(""))
        {
            nameContactUsEdt.setError(" Please enter your name");
            nameContactUsEdt.requestFocus();
            return false;
        }else if (phoneContactUsEdt.getText().toString().equals("")) {
            phoneContactUsEdt.setError("Please enter your phone number");
            phoneContactUsEdt.requestFocus();
            return false;
        }else if(phoneContactUsEdt.length() < 10){
            phoneContactUsEdt.setError("Please enter valid phone number");
            phoneContactUsEdt.requestFocus();
            return  false;
        }else if (strEmail.equals("")) {
            Toast toast = Toast.makeText(getContext(), "Please enter your  email address", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;

        }else if (!strEmail.matches(emailPattern)) {
            Toast toast = Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            Log.i("EmailCheck","It is valid");
            return false;

        }
        else if(subjectContactEdt.getText().toString().equals("")){
            subjectContactEdt.setError("This field can not be blank");
            subjectContactEdt.requestFocus();
            return false;
        }else if(messageContactEdt.getText().toString().equals("")){
            messageContactEdt.setError("This field can not be blank");
            messageContactEdt.requestFocus();
            return false;
        }
        return true;
    }


    private void ContactUs_Api() {

        String withoutConryCodeNumberContactUs= "";
        withoutConryCodeNumberContactUs = "+91"+strPhoneNumber;

        Log.e("ContactUsNumber",withoutConryCodeNumberContactUs);

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
        retrofit2.Call<ContactUs_Model> call = service.getcontactus("12345",strName,strEmail,withoutConryCodeNumberContactUs,strMessage,strSubject);
        call.enqueue(new Callback<ContactUs_Model>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ContactUs_Model> call, @NonNull retrofit2.Response<ContactUs_Model> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ContactUs_Model result = response.body();
                        String success = result.getSuccess();
                        String message = result.getMessage();
                        nameContactUsEdt.setText("");
                        phoneContactUsEdt.setText("");
                        emailContacTEdt.setText("");
                        subjectContactEdt.setText("");
                        messageContactEdt.setText("");
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

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
            public void onFailure(@NonNull Call<ContactUs_Model> call, Throwable t) {
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
}