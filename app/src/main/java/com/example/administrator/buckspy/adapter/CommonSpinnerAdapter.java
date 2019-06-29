package com.example.administrator.buckspy.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.buckspy.R;
import com.example.administrator.buckspy.entity.AccountDetails;
import com.example.administrator.buckspy.entity.CategoryDetails;

import java.util.ArrayList;
import java.util.List;

public class CommonSpinnerAdapter<Object> extends BaseAdapter {
    Activity context = null;
    private List<Object> listArrayData = null;

    public CommonSpinnerAdapter(Activity context, List<Object> listArrayData) {
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
        Object listData = listArrayData.get(pos);
        try {
//            if (view == null) {
                view = context.getLayoutInflater().inflate(R.layout.spinnerview, null, true);
                TextView tv_image = view.findViewById(R.id.tv_catagory_img);
                TextView tv_category = view.findViewById(R.id.tv_catagory_selection);
                if(listData instanceof CategoryDetails) {
                    tv_image.setBackground(context.getResources().getDrawable(Integer.parseInt(((CategoryDetails) listData).getImage_path())));
                    tv_category.setText(((CategoryDetails) listData).getCategory_name());
                } else if(listData instanceof AccountDetails){
                    tv_image.setBackground(context.getResources().getDrawable(Integer.parseInt(((AccountDetails) listData).getImage_path())));
                    tv_category.setText(((AccountDetails) listData).getAccount_name());
                }
//            }
        } catch (Exception e) {
            Log.e("CommonSpinnerAdapter", e.getMessage());
        }
        return view;
    }
}
