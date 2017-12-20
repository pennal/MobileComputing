package mc.usi.org.mobilecomputingproject.Views.Profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mc.usi.org.mobilecomputingproject.Models.Ride;
import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.REST.API;
import mc.usi.org.mobilecomputingproject.REST.RESTClient;
import mc.usi.org.mobilecomputingproject.REST.responses.UserResponse;
import mc.usi.org.mobilecomputingproject.Utils.DateUtils;
import mc.usi.org.mobilecomputingproject.Utils.DialogUtils;
import mc.usi.org.mobilecomputingproject.Utils.FailableCallback;
import mc.usi.org.mobilecomputingproject.Utils.Singleton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileFragment extends Fragment {

    public final static String TAG = "ProfileFragment";
    private final static int CAMERA_REQUEST = 102;
    private final static int PICTURE_REQUEST = 101;

    private TextView mTotalRidesTextView;
    private TextView mPublicRidesTextView;

    private TextView mUsernametextView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mDOBTextView;

    private Button mLogoutButton;
    private Button mEditButton;
    private Button mProfilePicButton;

    private CircleImageView mProfileImageView;
    private ImageView mBackgroundImageView;



    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        this.mTotalRidesTextView = view.findViewById(R.id.total_rides_label);
        this.mPublicRidesTextView = view.findViewById(R.id.public_rides_label);
        this.mEmailTextView = view.findViewById(R.id.email_label);
        this.mUsernametextView = view.findViewById(R.id.username_label);
        this.mNameTextView = view.findViewById(R.id.name_label);
        this.mDOBTextView = view.findViewById(R.id.dob_label);

        this.mProfileImageView = view.findViewById(R.id.main_profile_picture);
        this.mBackgroundImageView = view.findViewById(R.id.bg_profile_picture);
        this.mProfilePicButton = view.findViewById(R.id.profile_image_button);

        Fragment currentFragment = this;

        this.mProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence inputChoice[] = new CharSequence[] {"Gallery", "Camera"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(inputChoice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        // Depending on the action, choose the needed permissions
                        List<String> permissions = new ArrayList<String>();
                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (which != 0) {
                            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            permissions.add(Manifest.permission.CAMERA);
                        }

                        String[] finalPermissions = permissions.toArray(new String[0]);

                        Dexter.withActivity(getActivity())
                                .withPermissions(finalPermissions)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            if (which == 0) {
                                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                                photoPickerIntent.setType("image/*");
                                                startActivityForResult(photoPickerIntent, PICTURE_REQUEST);
                                            } else {
                                                //                                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                                //                                            startActivityForResult(cameraIntent, CAMERA_PICTURE_REQUEST);
                                                if (report.areAllPermissionsGranted()) {
                                                    new MaterialCamera(currentFragment).allowRetry(true).stillShot().start(CAMERA_REQUEST);
                                                } else {
                                                    Log.e(TAG, "NOT ALL PERMISSIONS WERE GRANTED");
                                                }
                                            }
                                        } else {
                                            DialogUtils.getDialogWithOkButton(getActivity(), "Missing permission", "Please grant all permissions!").show();
                                        }
                                    }
                                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).onSameThread().check();
                    }
                });
                builder.setTitle("Choose Source");
                builder.show();
            }
        });


        this.mLogoutButton = view.findViewById(R.id.logout_button);
        this.mLogoutButton.setOnClickListener(view1 -> {
            DialogUtils.showDialogWithConfirmation(getActivity(), "Confirm logout", "Are you sure?", "Ok", (dialogInterface, i) -> {
                Prefs.remove("token");
                getActivity().finish();
            }, "Cancel", (dialogInterface, i) -> {

            }).show();

        });
        
        this.mEditButton = view.findViewById(R.id.edit_button);
        this.mEditButton.setOnClickListener(view12 -> Toast.makeText(getActivity(), "Edit profile clicked", Toast.LENGTH_SHORT).show());

        // Start by using whatever is in the storage
        UserResponse ret = Singleton.getInstance().getProfile();
        if (ret != null) {
            setProfileData(ret);
        }

        API.getUserInfo(new FailableCallback<UserResponse>(getActivity()) {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    UserResponse res = response.body();

                    Singleton.getInstance().setProfile(res);

                    setProfileData(res);

                } else {
                    DialogUtils.getDialogWithOkButton(getActivity(), "Something went wrong", "Could not retrieve the profile").show();
                }
            }
        });

        return view;
    }


    public void setProfileData(UserResponse res) {
        List<Ride> rides = res.getRides();

        if (rides == null) {
            rides = new ArrayList<>();
        }

        int totalRides = 0;
        int publicRides = 0;



        for (Ride r: rides) {
            totalRides += 1;

            if (r.isPublic()) {
                publicRides += 1;
            }
        }


        mPublicRidesTextView.setText(publicRides + "");
        mTotalRidesTextView.setText(totalRides + "");

        mEmailTextView.setText(res.getEmailAddress());
        mUsernametextView.setText(res.getUsername());
        mNameTextView.setText(res.getFirstName() + " " + res.getLastName());

        mDOBTextView.setText(DateUtils.getFormattedDate(res.getDateOfBirth()));

        String url = RESTClient.BASE_URL + "/media/" + res.getProfilePicture();

        Picasso.with(getActivity()).load(url).into(this.mProfileImageView);
        Picasso.with(getActivity()).load(url).into(this.mBackgroundImageView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            final ProgressDialog dialog = DialogUtils.getIndefiniteProgressDialog(getActivity(), "Uploading", "This may take a while...");

            dialog.show();
            MultipartBody.Part body = null;

            if (requestCode == PICTURE_REQUEST) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                if (cursor == null) {
                    return;
                }

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);

                File f = new File(filePath);


                RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), f);

                body = MultipartBody.Part.createFormData("file", f.getName(), requestFile);

            } else if (requestCode == CAMERA_REQUEST) {

                File f = new File(data.getData().getPath());

                Log.e(TAG, String.format("Saved to: %s, size: %s", f.getAbsolutePath(), fileSize(f)));

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), f);

                body = MultipartBody.Part.createFormData("file", f.getName(), requestFile);
            }


            if (body == null) {

                DialogUtils.getDialogWithOkButton(getActivity(), "Internal server error", "Error while processing your image. Try again later").show();
            }

            API.uploadProfilePicture(body, new FailableCallback<Void>(getActivity()) {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    dialog.dismiss();
                    if (response.code() == 200) {
                        // Reset the current profile image

                        API.getUserInfo(new FailableCallback<UserResponse>(getActivity()) {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                if (response.code() == 200) {
                                    UserResponse res = response.body();

                                    Singleton.getInstance().setProfile(res);

                                    setProfileData(res);

                                } else {
                                    DialogUtils.getDialogWithOkButton(getActivity(), "Something went wrong", "Could not retrieve the profile").show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Could not upload file: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String fileSize(File file) {
        return readableFileSize(file.length());
    }

}
