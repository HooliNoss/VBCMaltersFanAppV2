package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.News;
import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Result;

import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String KEY_ITEM = "entry"; // parent node

    private ArrayList<News> newsList;
    private ProgressDialog progressDialog;
    XMLParser parser;

    private View myFragmentView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {
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
        myFragmentView = inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.recycler_view_news);
        //mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_news);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        parser = new XMLParser();
        getNews();
        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onNewsFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onNewsFragmentInteraction(Uri uri);
    }

    private void getNews()
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://grodien.ddns.net:8080/GetNews.php";

        progressDialog = ProgressDialog.show(getActivity(), "", "Lade News...", false, true);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        progressDialog.dismiss();

                        Document doc = parser.getDomElement(response); // getting DOM element
                        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                        newsList = new ArrayList<News>();
                        // looping through all item nodes <item>
                        for (int i = 0; i < nl.getLength(); i++)
                        {
                            Element e = (Element) nl.item(i);

                            News news = new News();
                            news.setId(Integer.parseInt(parser.getValue(e, "NewsID")));
                            String date = parser.getValue(e, "NewsDate");
                            String formatedDate = date.substring(0, date.length() - 7);

                            news.setDate(formatedDate);
                            news.setTitle(parser.getValue(e, "NewsTitle"));
                            news.setBody(parser.getValue(e, "NewsBody"));
                            news.setNewsObject(e);

                            newsList.add(news);
                        }

                        News[] myDataset = newsList.toArray(new News[newsList.size()]);
                        mAdapter = new NewsAdapter(getActivity(), myDataset);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle(android.R.string.dialog_alert_title);
                b.setMessage("Es konnte keine Verbindung hergestellt werden");
                b.setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dlg, int arg1){
                                dlg.dismiss();
                            }
                        });
                b.create().show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
