package com.krypt.bluecoin.utils;

import static com.krypt.bluecoin.utils.Links.adddep;
import static com.krypt.bluecoin.utils.Links.userid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddCard extends AppCompatActivity {

    private Stripe stripe;
    private TextView balanceTextView;
    private Spinner cardSpinner;
    private List<String> userCards = new ArrayList<>();
    private static String USER_ID = "yourUserId";
    private static String BACKEND_URL = adddep;
    private void fetchUserId(String username) {
        new AddCard.FetchUserIdTask().execute(username);
    }
    private SessionHandler session;
    private UserModel user;
    private class FetchUserIdTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... usernames) {
            String username = usernames[0];
            try {
                URL url = new URL(userid+email);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    return reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String userId) {
            if (userId != null && !userId.equals("0")) {
                // Here, userId is the ID fetched from the backend. You can store it or use it.
                USER_ID=userId;
                Toast.makeText(AddCard.this, "userid: "+userId, Toast.LENGTH_SHORT).show();
            } else {
                // Handle user not found or other errors
                Toast.makeText(AddCard.this, "not found", Toast.LENGTH_SHORT).show();

            }
        }
    }
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.depay);

        PaymentConfiguration.init(getApplicationContext(), "pk_test_51NgnBJH3mwloq32MtCykGIuW2VndTpubuHqUpdbEdpOy6iKQE1Qqr3WvpEF1LMpalN17aCGzjsHwltwXc9hlVjnU00UvKT4Zwo");
        stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());
        try {
            session = new SessionHandler(this);
            user = session.getUserDetails();

            email=user.getEmail();
            Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
            //usnm.setText(user.getFname() + " " + user.getSname());
            // txt_status.setText(user.getStatus());



        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
        fetchUserId(email);
        final EditText cardNumberEditText = findViewById(R.id.cardNumberEditText);
        final EditText expMonthEditText = findViewById(R.id.expMonthEditText);
        final EditText expYearEditText = findViewById(R.id.expYearEditText);
        final EditText cvcEditText = findViewById(R.id.cvcEditText);
        Button addCardButton = findViewById(R.id.addCardButton);
        Button depositButton = findViewById(R.id.depositButton);
        balanceTextView = findViewById(R.id.balanceTextView);
        cardSpinner = findViewById(R.id.cardSpinner);

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = cardNumberEditText.getText().toString();
                int expMonth = Integer.parseInt(expMonthEditText.getText().toString());
                int expYear = Integer.parseInt(expYearEditText.getText().toString());
                String cvc = cvcEditText.getText().toString();
                addCard(cardNumber, expMonth, expYear, cvc);
            }
        });

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = cardSpinner.getSelectedItemPosition();
                if (selectedPosition != AdapterView.INVALID_POSITION) {
                    String selectedCard = userCards.get(selectedPosition);
                    int amount = 1000; // Example amount: $10
                    depositUsingCard(selectedCard, amount);
                } else {
                    // Handle no card selected
                }
            }
        });

        fetchUserBalance();
        fetchUserCards();
    }

    private void addCard(String cardNumber, int month, int year, String cvc) {
        PaymentMethodCreateParams.Card cardDetails = new PaymentMethodCreateParams.Card.Builder()
                .setNumber(cardNumber)
                .setExpiryMonth(month)
                .setExpiryYear(year)
                .setCvc(cvc)
                .build();

        PaymentMethodCreateParams params = PaymentMethodCreateParams.create(cardDetails, null);

        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod paymentMethod) {
                new SaveCardTask().execute(paymentMethod.id);
            }

            @Override
            public void onError(@NonNull Exception e) {
                Toast.makeText(AddCard.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void fetchUserCards() {
        new FetchCardsTask().execute();
    }

    private void fetchUserBalance() {
        new GetUserBalanceTask().execute();
    }

    private void depositUsingCard(String selectedCard, int amount) {
        new DepositTask(selectedCard, amount).execute();
    }

    private class SaveCardTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... paymentMethodIds) {
            String paymentMethodId = paymentMethodIds[0];
            try {
                URL url = new URL(BACKEND_URL + "/save_card");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(("paymentMethodId=" + paymentMethodId + "&userId=" + USER_ID).getBytes());
                os.flush();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                fetchUserCards();
            } else {
                Toast.makeText(AddCard.this, "Failed to save card", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class FetchCardsTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> fetchedCards = new ArrayList<>();
            try {
                URL url = new URL(BACKEND_URL + "/get_cards?userId=" + USER_ID);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fetchedCards.add(line);
                    }
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fetchedCards;
        }

        @Override
        protected void onPostExecute(List<String> fetchedCards) {
            userCards.clear();
            userCards.addAll(fetchedCards);
            ArrayAdapter<String> cardAdapter = new ArrayAdapter<>(AddCard.this, android.R.layout.simple_spinner_item, userCards);
            cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cardSpinner.setAdapter(cardAdapter);
        }
    }

    private class DepositTask extends AsyncTask<Void, Void, Boolean> {
        private String paymentMethodId;
        private int amount;

        DepositTask(String paymentMethodId, int amount) {
            this.paymentMethodId = paymentMethodId;
            this.amount = amount;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL(BACKEND_URL + "/charge");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(("paymentMethodId=" + paymentMethodId + "&amount=" + amount + "&userId=" + USER_ID).getBytes());
                os.flush();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                fetchUserBalance();
                Toast.makeText(AddCard.this, "Charge successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddCard.this, "Charge failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class GetUserBalanceTask extends AsyncTask<Void, Void, Double> {
        @Override
        protected Double doInBackground(Void... voids) {
            double balance = 0.0;
            try {
                URL url = new URL(BACKEND_URL + "/get_user_balance?userId=" + USER_ID);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = reader.readLine();
                    if (line != null) {
                        balance = Double.parseDouble(line);
                    }
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return balance;
        }

        @Override
        protected void onPostExecute(Double balance) {
            balanceTextView.setText(String.format("Balance: $%.2f", balance));
        }
    }
}