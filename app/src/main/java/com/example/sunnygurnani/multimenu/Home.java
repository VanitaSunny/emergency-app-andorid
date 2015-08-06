package com.example.sunnygurnani.multimenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class Home extends Fragment {

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
        return view;
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
