package com.maya.pqvalidator.fragments.home;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.maya.pqvalidator.R;
import com.maya.pqvalidator.apis.volley.VolleyHelperLayer;
import com.maya.pqvalidator.constants.Constants;
import com.maya.pqvalidator.interfaces.IFragment;
import com.maya.pqvalidator.utils.Logger;
import com.maya.pqvalidator.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements IFragment, QRCodeReaderView.OnQRCodeReadListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.qrCodeReaderView)
    QRCodeReaderView qrCodeReaderView;

    boolean isBusy = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        setUp();
        return view;
    }

    private void setUp()
    {
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(4000L);
        qrCodeReaderView.setFrontCamera();
        //qrCodeReaderView.setBackCamera();
    }

    @Override
    public void changeTitle(String name) {

    }

    @Override
    public void showSnackBar(String message, int type)
    {
        Utility.showSnackBar(activity(),coordinatorLayout,message,type);
    }


    @Override
    public Activity activity() {
        return getActivity();
    }


    @Override
    public void onQRCodeRead(String text, PointF[] points)
    {
        if(Utility.isNetworkAvailable(activity()) && !isBusy)
        {
            AudioManager audioManager = (AudioManager) activity().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(700);
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_qrcode_detected);
            mediaPlayer.start();
            addUserLog(text);
        }
        else
        {
            if(!Utility.isNetworkAvailable(activity()))
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();

    }

    public void addUserLog(String qrCode)
    {
        isBusy = true;
        String URL = Constants.URL_ADD_USER_LOG;
        JSONObject input = new JSONObject();
        try
        {
            input.put("qr_code",qrCode);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final ProgressDialog progressDialog = Utility.generateProgressDialog(activity());
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject)
            {
                Logger.d("[response]",jsonObject.toString());
                Utility.closeProgressDialog(progressDialog);
                boolean success = Utility.isSuccessful(jsonObject.toString());
                try
                {
                    if(success)
                    {
                        showSnackBar("Log added successfully",1);
                        isBusy = false;
                    }
                    else
                    {
                        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.alert_horn);
                        mediaPlayer.start();
                        showSnackBar("INAVLID USER",0);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isBusy = false;
                            }
                        },2000);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]","Unable to contact server");
                Utility.closeProgressDialog(progressDialog);
                showSnackBar(Constants.CONNECTION_ERROR,0);
                isBusy = false;
            }
        };
        if (Utility.isNetworkAvailable(activity()))
        {
            volleyHelperLayer.startHandlerVolley(URL, input, listener, errorListener, Request.Priority.NORMAL);
        }
        else
        {
            isBusy = false;
            showSnackBar(Constants.PLEASE_CHECK_INTERNET,0);
            Utility.closeProgressDialog(progressDialog);
        }


    }



}
