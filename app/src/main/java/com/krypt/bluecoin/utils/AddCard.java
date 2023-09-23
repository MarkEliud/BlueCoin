package com.krypt.bluecoin.utils;

import static com.krypt.bluecoin.User.UserModel.userID;
//import static com.krypt.bluecoin.utils.AddCard.BACKEND_URL;
//import static com.krypt.bluecoin.utils.AddCard.USER_ID;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
     EditText cardNumberEditText;
     EditText expMonthEditText;
     EditText expYearEditText ;
     EditText cvcEditText;
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
        protected void onPostExecute(String userid) {
            if (userid != null && !userid.equals("0")) {
                // Here, userId is the ID fetched from the backend. You can store it or use it.
                USER_ID=userid;
                ; user.setUserID(userid);
                Toast.makeText(AddCard.this, "userid: "+userid, Toast.LENGTH_SHORT).show();
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
        fetchUserBalance();

        //   balanceTextView
        balanceTextView  = findViewById(R.id.balanceTextView);

         cardNumberEditText = findViewById(R.id.cardNumberEditText);
       expMonthEditText = findViewById(R.id.expMonthEditText);
        expYearEditText = findViewById(R.id.expYearEditText);
         cvcEditText = findViewById(R.id.cvcEditText);
        Button addCardButton = findViewById(R.id.addCardButton);
        Button depositButton = findViewById(R.id.depositButton);
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
                    Toast.makeText(AddCard.this, "Please select a card", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchUserCards();
    }
    private void fetchUserBalance() {
        new FetchUserBalanceTask().execute();
    }

    private class FetchUserBalanceTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(BACKEND_URL + "?action=get_balance&userId=" + USER_ID);
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
        protected void onPostExecute(String balance) {
            if (balance != null) {
                balanceTextView.setText("$" + balance);  // Or whatever format you prefer
            } else {
                Toast.makeText(AddCard.this, "Failed to fetch balance.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addCard(String cardNumber, int month, int year, String cvc) {
        PaymentMethodCreateParams.Card cardDetails = new PaymentMethodCreateParams.Card.Builder()
                .setNumber(cardNumber)
                .setExpiryMonth(month)
                .setExpiryYear(year)
                .setCvc(cvc)
                .build();
//        Required type:CardParams
//        Provided:
//        Card
      //  PaymentMethodCreateParams params = PaymentMethodCreateParams.createCard(cardDetails);

        PaymentMethodCreateParams params = PaymentMethodCreateParams.create(cardDetails);

        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod paymentMethod) {
              //  new SaveCardTask(cardNumber).execute(paymentMethod.id);
                new SaveCardTask(cardNumber, paymentMethod.card.brand.name()).execute(paymentMethod.id);
                //error in paymentMethod.card.brand Required type: String
              //  Provided:                CardBrand
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

    private void depositUsingCard(String selectedCard, int amount) {
        new DepositTask().execute(selectedCard, String.valueOf(amount));
    }

    private class DepositTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String paymentMethodId = params[0];
            String amount = params[1];
            //https://blupayinc.com/api_files/index.php?action=get_cards&userId=1

            try {
                URL url = new URL(BACKEND_URL + "?action=charge");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                String requestBody = "paymentMethodId=" + paymentMethodId + "&userId=" + USER_ID + "&amount=" + amount;
                os.write(requestBody.getBytes());
                os.flush();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response = reader.readLine();
                    reader.close();

                    if ("Charge Successful".equals(response)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(AddCard.this, "Deposit Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddCard.this, "Deposit Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


//    private class SaveCardTask extends AsyncTask<String, Void, Boolean> {
//        private String cardNumber;
//
//        SaveCardTask(String cardNumber) {
//            this.cardNumber = cardNumber;
//        }
//
//        @Override
//        protected Boolean doInBackground(String... paymentMethodIds) {
//            String paymentMethodId = paymentMethodIds[0];
//            try {
//                //                //https://blupayinc.com/api_files/index.php?action=get_cards&userId=1
//                URL url = new URL(BACKEND_URL + "?action=save_card");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setDoOutput(true);
//                OutputStream os = conn.getOutputStream();
//                String requestBody = "paymentMethodId=" + paymentMethodId + "&userId=" + USER_ID + "&cardNumber=" + cardNumber;
//                os.write(requestBody.getBytes());
//                os.flush();
//                os.close();
//                int responseCode = conn.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    return true;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            if (success) {
//                fetchUserCards();
//            } else {
//                Toast.makeText(AddCard.this, "Failed to save card", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
private class SaveCardTask extends AsyncTask<String, Void, String> {
    private String cardNumber;
    private String cardBrand;

    SaveCardTask(String cardNumber, String cardBrand) {
        this.cardNumber = cardNumber;
        this.cardBrand = cardBrand;
    }

    @Override
    protected String doInBackground(String... paymentMethodIds) {
        String paymentMethodId = paymentMethodIds[0];
        try {
            URL url = new URL(BACKEND_URL + "?action=save_card");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            String requestBody = "paymentMethodId=" + paymentMethodId + "&userId=" + USER_ID + "&cardNumber=" + cardNumber + "&cardBrand=" + cardBrand;

            //String requestBody = "paymentMethodId=" + paymentMethodId + "&userId=" + USER_ID + "&cardNumber=" + cardNumber;
            os.write(requestBody.getBytes());
            os.flush();
            os.close();
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
    protected void onPostExecute(String response) {
        if ("Card Saved".equals(response)) {
            fetchUserCards();
        } else if ("Card Already Exists".equals(response)) {
            Toast.makeText(AddCard.this, "This card is already saved.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddCard.this, "Failed to save card", Toast.LENGTH_SHORT).show();
        }
    }
}

    private class FetchCardsTask extends AsyncTask<Void, Void, List<CardDisplayData>> {
        @Override
        protected List<CardDisplayData> doInBackground(Void... voids) {
            List<CardDisplayData> fetchedCards = new ArrayList<>();
            try {
                //https://blupayinc.com/api_files/index.php/get_cards
                //https://blupayinc.com/api_files/index.php?action=get_cards&userId=1
                URL url = new URL(BACKEND_URL + "?action=get_cards&userId=" + USER_ID);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    fetchedCards = new Gson().fromJson(reader, new TypeToken<List<CardDisplayData>>(){}.getType());
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fetchedCards;
        }

        @Override
        protected void onPostExecute(List<CardDisplayData> fetchedCards) {
            if (fetchedCards != null) {
                userCards.clear();
                List<String> displayCards = new ArrayList<>();
                for(CardDisplayData data : fetchedCards) {
                    userCards.add(data.id);
                    displayCards.add(data.display);
                }
                ArrayAdapter<String> cardAdapter = new ArrayAdapter<>(AddCard.this, android.R.layout.simple_spinner_item, displayCards);
                cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cardSpinner.setAdapter(cardAdapter);
            } else {
               // Toast.makeText(AddCard.this, "", Toast.LENGTH_SHORT).show();
                Toast.makeText(AddCard.this, "Failed to fetch cards. Please try again later.", Toast.LENGTH_LONG).show();
            }
        }

    }

    private class CardDisplayData {
        public String id;
        public String display;
    }
}