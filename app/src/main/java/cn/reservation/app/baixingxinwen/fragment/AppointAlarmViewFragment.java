package cn.reservation.app.baixingxinwen.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import cn.reservation.app.baixingxinwen.utils.ConfirmDialogView;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointAlarmViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointAlarmViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointAlarmViewFragment extends Fragment implements DialogInterface.OnCancelListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "UserID";

    // TODO: Rename and change types of parameters

    private Context mContext;
    private Resources res;

    private OnFragmentInteractionListener mListener;
    private TextView mTxtPatient;
    private TextView mTxtHostpital;
    private TextView mTxtJob;
    private TextView mTxtDateTime;
    private TextView mTxtFee;

    private AppointItem appointItem;
    TextView btnCancelAppoint;

    public AppointAlarmViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AppointAlarmViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointAlarmViewFragment newInstance(long param1) {
        AppointAlarmViewFragment fragment = new AppointAlarmViewFragment();
        Bundle args = new Bundle();
        args.putLong(USER_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getParent();
        res = mContext.getResources();
        if (getArguments() != null) {
            //mUserID = getArguments().getLong(USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appoint_alarm_view, container, false);
        mTxtPatient = (TextView) view.findViewById(R.id.txt_patient);
        mTxtHostpital = (TextView) view.findViewById(R.id.txt_hospital_title);
        mTxtJob = (TextView) view.findViewById(R.id.txt_job);
        mTxtDateTime = (TextView) view.findViewById(R.id.txt_date_time);
        mTxtFee = (TextView) view.findViewById(R.id.txt_fee);

        btnCancelAppoint = (TextView) view.findViewById(R.id.btn_cancel_appoint);
        btnCancelAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAppointment();
            }
        });

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
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, AppointAlarmViewFragment.this);
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
                                mTxtJob.setText("");
                                mTxtDateTime.setText("");
                                mTxtFee.setText("");
                                btnCancelAppoint.setVisibility(View.GONE);
                                CommonUtils.dismissProgress(progressDialog);
                            } else {
                                btnCancelAppoint.setVisibility(View.VISIBLE);
                                JSONObject book = response.getJSONObject("book");
                                appointItem = new AppointItem((long)book.getInt("ReserveNo"), book.getString("ReserveDay"), book.getString("ReserveTime"),
                                        book.getString("ReserveMin"),
                                        new DoctorItem((long)book.getInt("CourseNo"), (long)book.getInt("WHospNo"), (long)book.getInt("HosNo"), "Picture",
                                                book.getString("CourseName"), book.getString("HospName"), book.getString("HosName"), "",
                                                book.getString("Price"), book.getString("HospTel"), 5, true),
                                        book.getString("PatientNo"), book.getString("PatientName"), false);
                                mTxtPatient.setText(appointItem.getmPatient());
                                mTxtHostpital.setText(appointItem.getmDoctor().getmHospital());
                                mTxtJob.setText(appointItem.getmDoctor().getmRoom());
                                mTxtDateTime.setText(CommonUtils.getFormattedDateTime(mContext, appointItem.getmDate(),
                                        appointItem.getmHour(), appointItem.getmMinute()));
                                mTxtFee.setText(appointItem.getmDoctor().getmFee() + res.getString(R.string.fee_unit));
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

    private void cancelAppointment() {
        final Dialog dialog = new Dialog(mContext);
        View view = new ConfirmDialogView(mContext, 1, res.getString(R.string.confirm_cancel_appoint));

        TextView btnOK = (TextView) view.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                _cancelAppointment();
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout(CommonUtils.getPixelValue(mContext, 200), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void _cancelAppointment() {
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, AppointAlarmViewFragment.this);
        RequestParams params = new RequestParams();
        params.put("userid", CommonUtils.userInfo.getUserID());
        params.put("token", CommonUtils.userInfo.getToken());
        params.put("whospno", appointItem.getmDoctor().getmHospitalID());
        params.put("hosno", appointItem.getmDoctor().getmRoomID());
        params.put("courseno", appointItem.getmDoctor().getmDoctorID());
        params.put("reserveno", appointItem.getmID());
        params.put("reserveday", appointItem.getmDate());
        params.put("patientno", appointItem.getmPatientID());

        String url = "cancelbook";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    progressDialog.dismiss();
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                    } else {
                        appointItem = null;
                        loadAppointAlarm();
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

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
