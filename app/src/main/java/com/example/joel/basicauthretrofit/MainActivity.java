package com.example.joel.basicauthretrofit;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout nameField, emailField, passwordField, phoneField, addressField;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(" https://dev.forhey.com/sales/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameField = findViewById(R.id.text_input_username);
        emailField = findViewById(R.id.text_input_email);
        passwordField = findViewById(R.id.text_input_password);
        phoneField = findViewById(R.id.text_input_phone);
        addressField = findViewById(R.id.text_input_address);


        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail() | !validateUsername() | !validatePassword()) {
                    return;
                } else {
                    login();
                }
            }
        });

//        findViewById(R.id.getBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getSecret();
//            }
//        });

    }

    private boolean validateEmail() {
        String emailInput = emailField.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailField.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailField.setError("Please enter a valid email address");
            return false;
        } else if (!emailInput.contains("@forhey.com")) {
            emailField.setError("email must contain @forhey.com");
            return false;
        } else {
            emailField.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = nameField.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            nameField.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            nameField.setError("Username too long");
            return false;
        } else {
            nameField.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordField.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passwordField.setError("Field can't be empty");
            return false;
        } else {
            passwordField.setError(null);
            return true;
        }
    }

    private static String token;

    private void login() {
        final String name = nameField.getEditText().getText().toString();
        final String email = emailField.getEditText().getText().toString();
        final String password = passwordField.getEditText().getText().toString();
        final String phone = phoneField.getEditText().getText().toString();
        final String address = addressField.getEditText().getText().toString();

        Login login = new Login(name, email, password, phone, address);
        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();
                    token = response.body().getToken();
                } else {
                    Toast.makeText(MainActivity.this, "login not correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error:(", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getSecret() {

        Call<ResponseBody> call = userClient.getSecret(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "token is not correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}
