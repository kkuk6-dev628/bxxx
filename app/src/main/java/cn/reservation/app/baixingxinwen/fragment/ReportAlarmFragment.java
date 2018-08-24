package cn.reservation.app.baixingxinwen.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AppointItem;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportAlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportAlarmFragment extends Fragment implements DialogInterface.OnCancelListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "UserID";

    // TODO: Rename and change types of parameters
    private Context mContext;
    private Resources res;

    private OnFragmentInteractionListener mListener;

    private TextView mTxtPatient;
    private TextView mTxtHostpital;
    private TextView mTxtJobDoctor;
    private TextView mTxtDateTime;

    private AppointItem appointItem;

    public ReportAlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ReportAlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportAlarmFragment newInstance(long param1) {
        ReportAlarmFragment fragment = new ReportAlarmFragment();
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
            //mUserID = getArguments().getLong(USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_alarm, container, false);
        mTxtPatient = (TextView) view.findViewById(R.id.txt_patient);
        mTxtHostpital = (TextView) view.findViewById(R.id.txt_hospital_title);
        mTxtJobDoctor = (TextView) view.findViewById(R.id.txt_job_doctor);
        mTxtDateTime = (TextView) view.findViewById(R.id.txt_date_time);

        loadAppointAlarm();
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadAppointAlarm() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ReportAlarmFragment.this);
                RequestParams params = new RequestParams();
                params.put("userid", CommonUtils.userInfo.getUserID());
                params.put("token", CommonUtils.userInfo.getToken());
                params.put("lang", CommonUtils.mIntLang);


                String url = "getfacingbook";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 0) {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                            } else if (response.getInt("code") == 2) {
                                //Toast.makeText(mContext, res.getString(R.string.no_found_appoint), Toast.LENGTH_SHORT).show();
                                mTxtPatient.setText("");
                                mTxtHostpital.setText("");
                                mTxtJobDoctor.setText("");
                                mTxtDateTime.setText("");
                                CommonUtils.dismissProgress(progressDialog);
                            } else {
                                JSONObject book = response.getJSONObject("book");
                                appointItem = new AppointItem((long)book.getInt("ReserveNo"), book.getString("ReserveDay"), book.getString("ReserveTime"),
                                        book.getString("ReserveMin"),
                                        new DoctorItem((long)book.getInt("CourseNo"), (long)book.getInt("WHospNo"), (long)book.getInt("HosNo"), "Picture",
                                                book.getString("CourseName"), book.getString("HospName"), book.getString("HosName"), "",
                                                book.getString("Price"), book.getString("HospTel"), 5, true),
                                        book.getString("PatientNo"), book.getString("PatientName"), false);
                                mTxtPatient.setText(appointItem.getmPatient());
                                mTxtHostpital.setText(appointItem.getmDoctor().getmHospital());
                                mTxtJobDoctor.setText(appointItem.getmDoctor().getmRoom() + "/" + appointItem.getmDoctor().getmName());
                                mTxtDateTime.setText(CommonUtils.getFormattedDateTime(mContext, appointItem.getmDate(),
                                        appointItem.getmHour(), appointItem.getmMinute()));
                                CommonUtils.dismissProgress(progressDialog);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
