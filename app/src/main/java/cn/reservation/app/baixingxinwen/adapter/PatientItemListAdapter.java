package cn.reservation.app.baixingxinwen.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.AppointInfoViewActivity;
import cn.reservation.app.baixingxinwen.activity.ReAppointActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;
import cn.reservation.app.baixingxinwen.utils.PatientItemViewHolder;

/**
 * Created by LiYin on 3/18/2017.
 */
public class PatientItemListAdapter extends RecyclerView.Adapter<PatientItemViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<FamilyMemberItem> mItems = Collections.emptyList();
    private AppointInfoViewActivity mParent;
    private ReAppointActivity mParent2;

    public PatientItemListAdapter(List<FamilyMemberItem> aItems, Context context, AppointInfoViewActivity parent) {
        this.mContext = context;
        this.mItems = aItems;
        this.mParent = parent;
    }

    public PatientItemListAdapter(List<FamilyMemberItem> aItems, Context context, ReAppointActivity parent) {
        this.mContext = context;
        this.mItems = aItems;
        this.mParent2 = parent;
    }

    public void addItem(FamilyMemberItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<FamilyMemberItem> lItems) {
        mItems = lItems;
    }

    public void setClear() {
        mItems.clear();
    }

    public FamilyMemberItem getCheckedItem() {
        for(int i=0; i<mItems.size(); i++) {
            if (mItems.get(i).isSelected()) {
                return mItems.get(i);
            }
        }
        return null;
    }

    public void setChecked(int position) {
        for(int i=0; i<mItems.size(); i++) {
            if (i == position) {
                mItems.get(i).setSelected(true);
            } else {
                mItems.get(i).setSelected(false);
            }
        }
    }

    @Override
    public PatientItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.patient_item, parent, false);
        Activity activity = mParent;
        if (mParent == null) activity = mParent2;
        itemView.setLayoutParams(CommonUtils.getLayoutParams(activity, 5, CommonUtils.getPixelValue(mContext, 50)));
        return new PatientItemViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final PatientItemViewHolder holder, final int position) {
        final FamilyMemberItem item = mItems.get(position);
        holder.setMemberName(item.getmMemberName());
        holder.setMemberIcon(item.getmMemberType(), item.getmMemberGender());
        holder.setCheckedIcon(item.isSelected());

        holder.mLayoutMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mItems.get(position).getmMemberID().equals("0") ){
                    if (mParent != null) {
                        mParent.setCheckPatient(position);
                    } else if (mParent2 != null)
                        mParent2.setCheckPatient(position);
                } else {
                    if (mParent != null) {
                        mParent.addPatient();
                    } else if (mParent2 != null)
                        mParent2.addPatient();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
