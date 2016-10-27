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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.AccountPicker;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers.ServerConnection;

import java.util.HashMap;
import java.util.Map;

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

        txtTitle.setText("Für Mitglieder vom VBC Malters:");
        txtInfo.setText("Du willst selber News für dein Team erstellen? Das finden wir super. Die App wird dadurch noch viel lebendiger. \n\n" +
                "Damit dies geht musst du erstmals deine E-Mail Adresse bekannt geben." +
                "Das ist nötig, damit die App erkennt wer du bist." +
                "Anschliessend melde dich bei einem Admin (oder deinem Trainer) damit dir die Berechtigungen erteilt werden");
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

        if (resultCode == -1) {
            final String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            final SharedPreferences settings = getActivity().getSharedPreferences(AppPreferences.PREFS_NAME, 0);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putString(AppPreferences.LOGIN_ADRESS, accountName);
            editor.commit();

            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = ServerConnection.SERVERURL + "SetRequestedAuthor.php";


            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", accountName);
                    return params;
                }
            };
            stringRequest.setTag(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringRequest);
        }
    }

}
