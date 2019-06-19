package com.globalwebsoft.a8to8.Adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.globalwebsoft.a8to8.Object.invoce_list_object;
import com.globalwebsoft.a8to8.R;

import java.util.ArrayList;

public class Invoice_list_adapter extends BaseAdapter {
    Context ctx;
    ArrayList<invoce_list_object> objects;
    LayoutInflater inflater;
    ViewHolder viewHolder;

    public Invoice_list_adapter(Context ctx, ArrayList<invoce_list_object> objects) {
        this.ctx = ctx;
        this.objects = objects;
        this.inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return objects.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.print_text, null);

            viewHolder = new ViewHolder();

            viewHolder.list_name   = (TextView) convertView.findViewById(R.id.list_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final invoce_list_object item = objects.get(position);

        viewHolder.list_name.setText(item.getSportname());

        return convertView;
    }
    private class ViewHolder {
        TextView list_name;
    }
}

