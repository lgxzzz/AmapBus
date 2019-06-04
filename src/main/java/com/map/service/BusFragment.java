package com.map.service;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.map.service.adapter.FavBusAdapter;
import com.map.service.amap.api.SimulationDataApi;
import com.map.service.bean.BusInfo;
import com.map.service.bean.FavBus;
import com.map.service.bean.User;
import com.map.service.manager.DBManager;
import com.map.service.manager.LoginManager;
import com.map.service.view.FavBusDialog;
import com.map.service.view.HelpDialog;
import com.map.service.view.MyInfoDialog;
import com.map.service.view.SlideRecyclerView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SlideRecyclerView recycler_view_list;
    private List<FavBus> favBuses;
    private FavBusAdapter mAdapter;

    public BusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusFragment newInstance(String param1, String param2) {
        BusFragment fragment = new BusFragment();
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
        View view = inflater.inflate(R.layout.fragemnt_fav_bus, container, false);
        recycler_view_list = (SlideRecyclerView) view.findViewById(R.id.fav_bus_recyleview);
        recycler_view_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        favBuses = DBManager.getInstance(getContext()).getAllFavBus(null,null,null,null,null,null);
        mAdapter = new FavBusAdapter(getContext(),favBuses);
        mAdapter.setOnDeleteClickListener(new FavBusAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view,final int position) {
                DBManager.getInstance(getContext()).delFavBusStation(favBuses.get(position).getBus_line_id());
                favBuses.remove(position);
                mAdapter.notifyDataSetChanged();
                recycler_view_list.closeMenu();
            }
        });
        mAdapter.setOnItemClickListener(new FavBusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, View v, int position) {
                FavBusDialog dialog = new FavBusDialog(getContext(),R.layout.fav_bus_dialog,true,true);
                dialog.setFavBus(favBuses.get(position));
                dialog.show();
            }
        });
        recycler_view_list.setAdapter(mAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
