package mc.usi.org.mobilecomputingproject.Views.Signup;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.REST.API;
import mc.usi.org.mobilecomputingproject.REST.models.UserSignup;
import mc.usi.org.mobilecomputingproject.Utils.DialogUtils;
import mc.usi.org.mobilecomputingproject.Utils.FailableCallback;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private EditText mEmail;
    private EditText mUsername;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mDateOfBirth;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private Button mRegisterButton;

    private Date mCurrentBirthDate;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        this.mEmail = v.findViewById(R.id.email_field_signup);
        this.mFirstName = v.findViewById(R.id.firstname_field_signup);
        this.mLastName = v.findViewById(R.id.lastname_field_signup);
        this.mUsername= v.findViewById(R.id.username_field_signup);
        this.mDateOfBirth= v.findViewById(R.id.date_of_birth_field_signup);
        this.mPassword = v.findViewById(R.id.password_field_signup);
        this.mConfirmPassword = v.findViewById(R.id.password_confirm_field_signup);

        this.mRegisterButton = v.findViewById(R.id.register_button);

        this.mRegisterButton.setOnClickListener(view -> {
            if (empty(this.mEmail) ||
                    empty(this.mUsername) ||
                    mCurrentBirthDate == null ||
                    empty(this.mPassword) ||
                    empty(this.mConfirmPassword) ||
                    empty(this.mFirstName) ||
                    empty(this.mLastName)) {
                Toast.makeText(getActivity(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!this.mPassword.getText().toString().equals(this.mConfirmPassword.getText().toString())) {
                Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog p = DialogUtils.getIndefiniteProgressDialog(getActivity(), "Signing you up", "This may take a while");
            p.show();

            UserSignup u = new UserSignup();
            u.setFirstname(this.mFirstName.getText().toString());
            u.setLastname(this.mLastName.getText().toString());
            u.setEmail(this.mEmail.getText().toString());
            u.setUsername(this.mUsername.getText().toString());
            u.setDateOfBirth(mCurrentBirthDate);
            u.setPassword(this.mPassword.getText().toString());


            API.userSignup(u, new FailableCallback<Void>(getActivity()) {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    p.dismiss();

                    if (response.code() == 201) {

                        DialogUtils.getDialogWithOkButton(getActivity(), "Signed up!", "You have successfully signed up. Login and start using the application!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        }).show();

                    } else if (response.code() == 400) {
                        DialogUtils.getDialogWithOkButton(getActivity(), "User already exists", "Looks like this user already exists").show();
                    } else {
                        DialogUtils.getDialogWithOkButton(getActivity(), "Failed Signup", "An error occured while signing up").show();
                    }
                }
            });
        });

        this.mDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) { // has focus
                    Calendar currentDate = Calendar.getInstance();

                    DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            // Selected date
                            Date newBirthDate = new GregorianCalendar(selectedyear, selectedmonth, selectedday).getTime();
                            mCurrentBirthDate = newBirthDate;

                            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                            String formatted = f.format(newBirthDate);
                            mDateOfBirth.setText(formatted);
                        }
                    }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                    mDatePicker.getDatePicker().setMaxDate(currentDate.getTimeInMillis() - 1000);
                    mDatePicker.getDatePicker().setSpinnersShown(true);
                    mDatePicker.setTitle("Select date");
                    mDatePicker.show();
                }
            }
        });






        return v;
    }



    private boolean empty(EditText f) {
        return f.getText().length() == 0;
    }

}
