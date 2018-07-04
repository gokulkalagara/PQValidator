package com.maya.pqvalidator.fragments.start;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.pqvalidator.R;
import com.maya.pqvalidator.activities.StartActivity;
import com.maya.pqvalidator.interfaces.IFragment;
import com.maya.pqvalidator.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllowPermissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllowPermissionFragment extends Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    @BindView(R.id.tvAllow)
    TextView tvAllow;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AllowPermissionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllowPermissionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllowPermissionFragment newInstance(String param1, String param2) {
        AllowPermissionFragment fragment = new AllowPermissionFragment();
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
        View view = inflater.inflate(R.layout.fragment_allow_permission, container, false);
        ButterKnife.bind(this,view);

        setUp();
        return view;
    }

    private void setUp()
    {
        tvAllow.setOnClickListener(v ->
        {
            ((StartActivity) activity()).openQRCodeScan();
        });
    }

    @Override
    public void changeTitle(String name) {

    }

    @Override
    public void showSnackBar(String message, int type)
    {

    }


    @Override
    public Activity activity() {
        return getActivity();
    }
}
