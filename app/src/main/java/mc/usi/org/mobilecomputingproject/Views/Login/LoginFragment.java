package mc.usi.org.mobilecomputingproject.Views.Login;


import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import io.realm.Realm;
import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.REST.API;
import mc.usi.org.mobilecomputingproject.REST.responses.TokenResponse;
import mc.usi.org.mobilecomputingproject.Utils.DialogUtils;
import mc.usi.org.mobilecomputingproject.Utils.FailableCallback;
import mc.usi.org.mobilecomputingproject.Utils.Singleton;
import mc.usi.org.mobilecomputingproject.Views.PasswordReset.PasswordResetActivity;
import mc.usi.org.mobilecomputingproject.Views.Signup.SignupActivity;
import mc.usi.org.mobilecomputingproject.Views.TabBar.TabBarActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private Button mLoginButton;
    private Button mResetPasswordButton;
    private Button mSignupButton;

    private EditText mEmailField;
    private EditText mPasswordField;




    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        // Fields
        mEmailField = v.findViewById(R.id.email_field_login);
        mPasswordField = v.findViewById(R.id.password_field_login);

        // Logins
        mLoginButton = v.findViewById(R.id.login_button);


        // Bottom buttons
        mSignupButton = v.findViewById(R.id.signup_button);
        mResetPasswordButton = v.findViewById(R.id.reset_password_button);

        // Common spinner
        final ProgressDialog progress = DialogUtils.getIndefiniteProgressDialog(getActivity(), "Logging in...", "This may take a while");


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();

                if (email.length() == 0) {
                    Toast.makeText(getActivity(), "Please fill out the Email field before proceeding", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() == 0) {
                    Toast.makeText(getActivity(), "Please fill out the Password field before proceeding", Toast.LENGTH_SHORT).show();
                    return;
                }

                progress.show();

                // Make the request to the server!
                API.loginWithPlainCredentials(email, password, new FailableCallback<TokenResponse>(getActivity()) {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        progress.dismiss();
                        if (response.code() == 200) {
                            String token = response.body().getToken();
                            Prefs.putString("token", token);
                            // If the request is successful, push the main view
                            pushMainView();
                        } else {
                            Toast.makeText(getActivity(), "Failed to login, invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        progress.dismiss();
                        super.onFailure(call, t);
                    }
                });

            }
        });

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PasswordResetActivity.class);
                startActivity(intent);
            }
        });



        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SignupActivity.class);
                startActivity(intent);
            }
        });






        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void pushMainView() {
        Intent mainView = new Intent();
        mainView.setClass(getActivity(), TabBarActivity.class);
        startActivity(mainView);
    }

}
