package com.example.avatarmind.robotmotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.avatarmind.robotmotion.Model.Customer;
import com.example.avatarmind.robotmotion.Model.MakeReservation;
import com.example.avatarmind.robotmotion.Model.Reservation;
import com.example.avatarmind.robotmotion.http.ReservationAPI;

import java.util.Date;

/** This class just handles the customer when they don't have a booking, which means making a reservation and getting the new table*/


public class NoBookingActivity extends Activity implements View.OnClickListener {

    private EditText mPhoneEditText;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private ImageView mTitleBack;

    ReservationAPI reservationAPI = new ReservationAPI();

    private static final String TAG = "NoBooking";

    private Customer loadedCustomer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) {
            getActionBar().hide();
        }

        initView();
        initListener();

        Handler mhandler = new Handler();

        mPhoneEditText = (EditText) findViewById(R.id.phone);
        mFirstNameEditText = (EditText) findViewById(R.id.firstName);
        mLastNameEditText = (EditText) findViewById(R.id.lastName);


        mPhoneEditText.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            String phone = mPhoneEditText.getText().toString();
            if (!hasFocus && !phone.equals("")) {
                mFirstNameEditText.setEnabled(true);
                mLastNameEditText.setEnabled(true);


                //check if this phone number belongs to a customer
                reservationAPI.getCustomerByNumber(mPhoneEditText.getText().toString(), (successful, customer) -> {
                    if (successful && customer.firstname != null) {
                        mhandler.post(() -> {
                            Toast.makeText(this, "We found your details! Please Click Confirm!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, customer.toString());
                            mFirstNameEditText.setText(customer.firstname);
                            mLastNameEditText.setText(customer.lastname);

                            mFirstNameEditText.setEnabled(false);
                            mLastNameEditText.setEnabled(false);

                            loadedCustomer = customer;
                        });
                    }
                    else {
                        mhandler.post(() -> {
                            Toast.makeText(this, "No Customer found", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "No Customer Found");
                        });
                    }
                });
//                Toast.makeText(this, "unfocus", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void initView() {
        setContentView(R.layout.activity_no_booking);
        mTitleBack = (ImageView) findViewById(R.id.common_title_back);

    }

    private void initListener() {
        mTitleBack.setOnClickListener(this);
    }


    public void createReservation(View v) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        if (loadedCustomer != null) {
            //this is a customer that already exists in the system, just just create reservation with id.
            Toast.makeText(this, "Customer loaded", Toast.LENGTH_SHORT).show();

//            reservationAPI.setReservation(mPhoneEditText.getText().toString(), makeReservation(), (successful, reservation) -> {
//                if (successful) {
//                    Intent intent = new Intent(this, HasValidBooking.class);
//                    intent.putExtra("reservation_id", reservation.id);
//                    startActivity(intent);
//                }
//                else {
//                    //Toast.makeText(this, "Failed to create Reservation", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Failed to create Reservation");
//                }
//            });

            reservationAPI.getReservationsByNumber(loadedCustomer.phone, (successful, reservations) -> {
                if (successful) {
                    if (reservations.length > 0) {
                        //Toast.makeText(this, "Reservation Found", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Reservation Found");
                        Log.d(TAG, TextUtils.join("\n", reservations));
                        mHandler.post(() -> {
                            Intent intent = new Intent(this, HasValidBooking.class);
                            intent.putExtra("reservation_id", reservations[0].id);
                            startActivity(intent);
                        });
                    }
                    else {
                        mHandler.post(() -> {
                            Toast.makeText(this, "You have no reservations", Toast.LENGTH_LONG).show();
                        });
                    }
                }
                else {
                    //Toast.makeText(this, "Failed to get Reservation", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Failed to contact server");
                }
            });
        }
        else {
            //create customer and create reservation
            Toast.makeText(this, "Customer not loaded", Toast.LENGTH_SHORT).show();
            loadedCustomer = new Customer();
            loadedCustomer.firstname = mFirstNameEditText.getText().toString();
            loadedCustomer.lastname = mLastNameEditText.getText().toString();
            loadedCustomer.phone = mPhoneEditText.getText().toString();
//            reservationAPI.createCustomer(loadedCustomer, (successful, customer) -> {
//                if (successful)
//                    reservationAPI.setReservation(mPhoneEditText.getText().toString(), makeReservation(), (reservationSuccessful, reservation) -> {
//                        if (reservationSuccessful) {
//                            Intent intent = new Intent(this, HasValidBooking.class);
//                            intent.putExtra("reservation_id", reservation.id);
//                            startActivity(intent);
//                        }
//                        else {
//                            Log.e(TAG, "Failed to create Reservation");
//                        }
//                    });
//                else {
//                    //Toast.makeText(this, "Failed to create your customer account", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Failed to create your customer account");
//                }
//            });


            reservationAPI.getReservationsByNumber("555", (successful, reservations) -> {
                if (successful) {
                    if (reservations.length > 0) {
                        //Toast.makeText(this, "Reservation Found", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Reservation Found");
                        Log.d(TAG, TextUtils.join("\n", reservations));
                        mHandler.post(() -> {
                            Intent intent = new Intent(this, HasValidBooking.class);
                            intent.putExtra("reservation_id", reservations[0].id);
                            startActivity(intent);
                        });
                    }
                    else {
                        mHandler.post(() -> {
                            Toast.makeText(this, "You have no reservations", Toast.LENGTH_LONG).show();
                        });
                    }
                }
                else {
                    //Toast.makeText(this, "Failed to get Reservation", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Failed to contact server");
                }
            });
        }
    }

    private MakeReservation makeReservation() {
        MakeReservation newReservation = new MakeReservation();
        newReservation.customer_id = loadedCustomer.id;
        newReservation.duration = 1;
        newReservation.start_time = new Date();
        newReservation.table_id = 1;
        newReservation.generateEndDate();
        return newReservation;
    }

    
        //TODO
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_back:
                finish();
                break;
            default:
                break;
        }

    }
}
