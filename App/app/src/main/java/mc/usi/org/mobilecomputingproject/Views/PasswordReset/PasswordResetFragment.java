package mc.usi.org.mobilecomputingproject.Views.PasswordReset;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.Utils.DialogUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordResetFragment extends Fragment {

    private Button mResetPassword;

    public PasswordResetFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance() {
        return new PasswordResetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_password_reset, container, false);

        this.mResetPassword = v.findViewById(R.id.send_reset_email_button);

        this.mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dummy
                DialogUtils.getDialogWithOkButton(getActivity(), "Email sent", "An email has been sent with instructions", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                }).show();
            }
        });


        return v;
    }

}
