package com.toddlertechiez.sudokuyou;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.toddlertechiez.sudokuyou.MainActivity.setSelectedItem;

/**
 * Created by chandan on 06-11-2016.
 */

public class SudokuAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<GridItems> arrayList;
    myViewHolder view_holder;

    static class  myViewHolder{
        TextView textView;
    }
    public SudokuAdapter(Context context, ArrayList<GridItems> arrayList)
    {
        this.mContext = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return 81;
    }

    @Override
    public Object getItem(int i) {
        return this.arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;

        GridView grid = (GridView)viewGroup;
        int size = grid.getColumnWidth();


        if (view == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.small_box, null);
            view.setLayoutParams(new GridView.LayoutParams(size, size));
        }
        else {
            view = convertView;
        }
        final GridItems item = arrayList.get(i);



        view_holder = new myViewHolder();
        view_holder.textView = (TextView) view.findViewById(R.id.Text_num);

        int num=item.getNumber();
        if(num==0)
        view_holder.textView.setText("");
        else
        view_holder.textView.setText(Integer.toString(num));

        view_holder.textView.setBackgroundColor(Color.parseColor(item.getItembgcolor()));


        if(item.modifable)
        {
            view_holder.textView.setTextColor(Color.BLUE);
            view_holder.textView.setTextSize(22);
        }
        else
        {
            view_holder.textView.setTextColor(Color.BLACK);
            view_holder.textView.setTextSize(18);
        }

        view_holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(item);
            }
        });
        view_holder.textView.setOnHoverListener(new View.OnHoverListener() {
                                            @Override
                                            public boolean onHover(View view, MotionEvent motionEvent) {

                                                return false;
                                            }
                                        });
        view.setTag(item);

        return view;
    }
}
