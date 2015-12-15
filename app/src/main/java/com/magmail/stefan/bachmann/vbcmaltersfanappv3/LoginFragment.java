package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.AccountPicker;

public class LoginFragment extends Fragment {
    private View myFragmentView;

    private TextView txtTitle;
    private TextView txtInfo;
    private Button btnSubmit;

    private OnFragmentInteractionListener mListener;

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        txtTitle = (TextView) myFragmentView.findViewById(R.id.txtLoginTitle);
        txtInfo = (TextView) myFragmentView.findViewById(R.id.txtLoginInfo);
        btnSubmit = (Button) myFragmentView.findViewById(R.id.btnLoginSubmit);

        txtTitle.setText("Login");
        txtInfo.setText("Damit du selber News schreiben kannst, musst du dich hier einloggen." +
                "Das ist nötig, damit die App erkennt wer du bist." +
                "Das ganze funktioniert aber nur, wenn du die eingetragene E-Mail Adresse einem Admin bekannt gegeben hast.");
        btnSubmit.setText("Login");
        btnSubmit.setOnClickListener(btnSubmitOnClickListener);

        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLoginFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onLoginFragmentInteraction(Uri uri);
    }

    private View.OnClickListener btnSubmitOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                    false, null, null, null, null);
            startActivityForResult(intent, 0);
        }
    };

    public void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {

        if (requestCode == -1) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            final SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putString("loginAdress", accountName);
            editor.commit();
        }
    }

}
