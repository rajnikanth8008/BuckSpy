package com.example.administrator.buckspy.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.buckspy.R;
import com.example.administrator.buckspy.dao.SpinnerDataDO;

import java.util.ArrayList;

public class CommonSpinnerAdapter extends BaseAdapter {
    Activity context = null;
    private ArrayList<SpinnerDataDO> listArrayData = null;

    public CommonSpinnerAdapter(Activity context, ArrayList<SpinnerDataDO> listArrayData) {
        this.context = context;
        this.listArrayData = listArrayData;
    }

    @Override
    public int getCount() {
        return listArrayData.size();
    }

    @Override
    public Object getItem(int i) {
        return listArrayData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View view1, ViewGroup viewGroup) {
        View view = view1;
        SpinnerDataDO listData = listArrayData.get(pos);
        try {
            if (view == null) {
                view = context.getLayoutInflater().inflate(R.layout.spinnerview, null, true);
                TextView tv_image = view.findViewById(R.id.tv_catagory_img);
                TextView tv_category = view.findViewById(R.id.tv_catagory_selection);
                tv_image.setBackground(context.getResources().getDrawable(listData.getImageDrawable()));
                tv_category.setText(listData.getCategory());
            }
        } catch (Exception e) {
            Log.e("CommonSpinnerAdapter", e.getMessage());
        }
        return view;
    }
}
