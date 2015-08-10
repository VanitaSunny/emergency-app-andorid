package com.example.sunnygurnani.multimenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Home extends Fragment {
    ImageButton btnSwitch;

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;

    ImageButton help_button;
    Button safe_button;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;


    Contact contact1, contact2;

    public static Home newInstance(int sectionNumber) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ContactStore contactStore = new ContactStore(getActivity());
        contact1 = contactStore.getContact_1();
        contact2 = contactStore.getContact_2();

        help_button = (ImageButton) view.findViewById(R.id.imageButton);
        help_button.setOnClickListener(onclick);

        safe_button = (Button) view.findViewById(R.id.button);
        safe_button.setOnClickListener(onclick);
        btnSwitch = (ImageButton) view.findViewById(R.id.flashButton);

        btnSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });
        // First check if device is supporting flashlight or not
        hasFlash = getActivity().getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alertDialog = null;

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getActivity().getTitle());
            alertDialogBuilder.setMessage("sorry, Your device dosen't support flash light");
            // set positive button: Yes message
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();
                }
            });
            alertDialog = alertDialogBuilder.create();
            // show alert
            alertDialog.show();

//            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
//                    .create();
//            alert.setTitle("Error");
//            alert.setMessage("Sorry, your device doesn't support flash light!");
//            alert.setButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // closing the application
////                    finish();
//                }
//            });
//            alert.show();

            //return view;
        }
        getCamera();
        return view;

        // displaying button image
        //toggleButtonImage();
        // Switch button click event to toggle flash on/off

    }
    // Get the camera
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Faile: ", e.getMessage());
            }
        }
    }
    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
//            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage();
        }
    }
    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
//            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            toggleButtonImage();
        }
    }
    // Playing sound
    // will play button toggle sound on flash on / off
//    private void playSound(){
//        if(isFlashOn){
//            mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_off);
//        }else{
//            mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_on);
//        }
//        mp.setOnCompletionListener(new OnCompletionListener() {
//
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                // TODO Auto-generated method stub
//                mp.release();
//            }
//        });
//        mp.start();
//    }

    /*
     * Toggle switch button images
     * changing image states to on / off
     * */
    private void toggleButtonImage(){
        if(isFlashOn){
            btnSwitch.setImageResource(R.mipmap.flash_off);
        }else{
            btnSwitch.setImageResource(R.mipmap.flash_on);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//    }

    @Override
    public void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            turnOnFlash();
    }

    @Override
    public void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    public void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.imageButton:
                    //if this button clicked it will start the call functionality.
                    if (isTelephonyEnabled()) {
                        if (contact1.getPhoneNumber() != null) {
                            Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact1.getPhoneNumber()));
                            startActivity(callintent);
                        }
                        if (contact2.getPhoneNumber() != null) {
                            Intent callintent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact2.getPhoneNumber()));
                            startActivity(callintent1);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Sim card not available", Toast.LENGTH_LONG).show();
                    }
                    //will send messages to the contacts saved.
                    sendsms("I need help.");
                    break;
                case R.id.button:
                    sendsms("I am safe now.");
                    if (contact1.getPhoneNumber() != null)
                        openAlert(v);
                    break;
            }

        }
    };

    private void openAlert(View view) {
        AlertDialog alertDialog = null;

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getActivity().getTitle());
        alertDialogBuilder.setMessage("Yor message has been sent.");

        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mListener.onFragmentAttach(getArguments().getInt(ARG_SECTION_NUMBER));

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // function created for sending messages.
    public void sendsms(String sms) {
        SmsManager smsManager = SmsManager.getDefault();
        if (contact1.getPhoneNumber() != null)
            smsManager.sendTextMessage(contact1.getPhoneNumber(), null, sms, null, null);
        if (contact2.getPhoneNumber() != null)
            smsManager.sendTextMessage(contact2.getPhoneNumber(), null, sms, null, null);

    }

    // function created to send call to the devices.
    private boolean isTelephonyEnabled() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

}




