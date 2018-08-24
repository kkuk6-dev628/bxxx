package cn.reservation.app.baixingxinwen.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.AppointAlarmViewActivity;
import cn.reservation.app.baixingxinwen.activity.AppointHelpActivity;
import cn.reservation.app.baixingxinwen.adapter.AppointItemListAdapter;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.AppointItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointAlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointAlarmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "UserID";

    // TODO: Rename and change types of parameters
    private long mUserID;

    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private Resources res;
    private View contentView;
    private ListView lstAlarm;
    public ArrayList<AppointItem> roomItems = new ArrayList<AppointItem>();

    public AppointAlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AppointAlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointAlarmFragment newInstance(long param1) {
        AppointAlarmFragment fragment = new AppointAlarmFragment();
        Bundle args = new Bundle();
        args.putLong(USER_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity().getParent();
        res = mContext.getResources();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserID = getArguments().getLong(USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_appoint_alarm, container, false);

        lstAlarm = (ListView) contentView.findViewById(R.id.lst_appoint_alarm);
        final AppointItemListAdapter appointItemListAdapter = new AppointItemListAdapter(mContext);
        appointItemListAdapter.setListItems(getAlarmItems());
        lstAlarm.setAdapter(appointItemListAdapter);
        lstAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gotoAppointAlarmView();
            }
        });

        return contentView;
    }

    private void gotoAppointAlarmView() {
        AppointHelpActivity appointHelpActivity = (AppointHelpActivity) getActivity();
        AnimatedActivity pActivity = (AnimatedActivity) appointHelpActivity.getParent();
        Intent intent;
        intent = new Intent(appointHelpActivity, AppointAlarmViewActivity.class);
        //intent.putExtra("Doctor", doctorItem);
        pActivity.startChildActivity("view_appoint_alarm", intent);
    }

    private ArrayList<AppointItem> getAlarmItems() {
        ArrayList<AppointItem> alarmItems = new ArrayList<AppointItem>();
        Time t = new Time(9, 0, 0);
        return alarmItems;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
