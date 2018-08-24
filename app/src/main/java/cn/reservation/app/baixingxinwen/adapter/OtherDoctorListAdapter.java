package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.ChooseAppointActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.OtherDoctorItemViewHolder;

/**
 * Created by LiYin on 3/16/2017.
 */
public class OtherDoctorListAdapter extends RecyclerView.Adapter<OtherDoctorItemViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DoctorItem> mItems = Collections.emptyList();
    private ChooseAppointActivity mParent;
    private int mPhotoHeight = 0;
    private int mPhotoWidth = 0;

    public OtherDoctorListAdapter(List<DoctorItem> aItems, Context context, ChooseAppointActivity parent) {
        this.mContext = context;
        this.mItems = aItems;
        this.mParent = parent;
    }

    public void addItem(DoctorItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<DoctorItem> lItems) {
        mItems = lItems;
    }

    public void setClear() {
        mItems.clear();
    }

    public DoctorItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public OtherDoctorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.other_doctor_item, parent, false);
        itemView.setLayoutParams(CommonUtils.getLayoutParams(mParent, 4, CommonUtils.getPixelValue(mContext, 50)));
        return new OtherDoctorItemViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final OtherDoctorItemViewHolder holder, final int position) {
        DoctorItem item = mItems.get(position);
        holder.setName(item.getmName());
        holder.setPhoto(item.getmPhoto());

        holder.mLayoutOtherDoctorPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.selectOtherDoctor(position);
            }
        });
        holder.mLayoutOtherDoctorPhoto.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.mLayoutOtherDoctorPhoto.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    holder.mLayoutOtherDoctorPhoto.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (mPhotoHeight == 0) {
                    mPhotoHeight = holder.mLayoutOtherDoctorPhoto.getHeight();
                    mPhotoWidth = holder.mLayoutOtherDoctorPhoto.getWidth();
                    if (CommonUtils.getDpValue(mContext, mPhotoHeight) > 86) {
                        mPhotoHeight = CommonUtils.getPixelValue(mContext, 86);
                    }
                }
                if (mPhotoHeight != holder.mLayoutOtherDoctorPhoto.getHeight() && position == 0 ) {
                    holder.setLayoutParams(mPhotoWidth, mPhotoHeight);
                }
            }
        });
        if (mPhotoHeight > 0) {
            holder.setLayoutParams(mPhotoWidth, mPhotoHeight);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
