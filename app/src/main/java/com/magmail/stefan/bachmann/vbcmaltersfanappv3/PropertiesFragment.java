package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class PropertiesFragment extends Fragment {
    private View myFragmentView;

    private CheckBox chkBoxGetNotification;

    private OnFragmentInteractionListener mListener;

    public static PropertiesFragment newInstance(String param1, String param2) {
        PropertiesFragment fragment = new PropertiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PropertiesFragment() {
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
        myFragmentView = inflater.inflate(R.layout.fragment_properties, container, false);
        chkBoxGetNotification = (CheckBox) myFragmentView.findViewById(R.id.chkBox_GetNotification);


        checkIfNotificationsAreAllowed();
        return myFragmentView;
    }

    private void checkIfNotificationsAreAllowed()
    {
        final SharedPreferences settings = getActivity().getSharedPreferences(AppPreferences.PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();

        // Commit the edits!
        editor.commit();

        chkBoxGetNotification.setChecked(settings.getBoolean(AppPreferences.GENARALL_NOTIFICATION, true));

        chkBoxGetNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean(AppPreferences.GENARALL_NOTIFICATION, isChecked);
                // Commit the edits!
                editor.commit();

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPropertiesFragmentInteraction(uri);
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
        public void onPropertiesFragmentInteraction(Uri uri);
    }
}
