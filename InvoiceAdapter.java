package com.globalwebsoft.a8to8.Adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.globalwebsoft.a8to8.Invoice_Activity;
import com.globalwebsoft.a8to8.Object.InvoiceObject;
import com.globalwebsoft.a8to8.Object.TrackStrack_Object;
import com.globalwebsoft.a8to8.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ADMIJN on 25-Jan-18.
 */

public class InvoiceAdapter extends BaseAdapter {

    Context context;
    ArrayList<InvoiceObject> objects;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    String bill;
    Context ctx;


    public InvoiceAdapter(Context ctx, ArrayList<InvoiceObject> objects) {
        this.ctx = ctx;
        this.objects = objects;
        this.inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {

        if (convertview == null) {

            convertview = inflater.inflate(R.layout.listview_invoice, null);
            viewHolder = new ViewHolder();

            viewHolder.listview_invoice_no = (TextView) convertview.findViewById(R.id.listview_invoice_no);
            viewHolder.listview_invoice_name = (TextView) convertview.findViewById(R.id.listview_invoice_name);
            viewHolder.listview_invoice_qty = (TextView) convertview.findViewById(R.id.listview_invoice_qty);
            viewHolder.listview_invoice_price = (TextView) convertview.findViewById(R.id.listview_invoice_price);
            viewHolder.listview_invoice_total = (TextView) convertview.findViewById(R.id.listview_invoice_total);

            convertview.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertview.getTag();
        }

        InvoiceObject item = objects.get(position);

        viewHolder.listview_invoice_no.setText("" + (position + 1));
        viewHolder.listview_invoice_name.setText(item.getName());
        viewHolder.listview_invoice_qty.setText(item.getQty());
        viewHolder.listview_invoice_price.setText("" + new DecimalFormat("##.##").format(Double.parseDouble(item.getPrice())) + "₹");
        viewHolder.listview_invoice_total.setText("" + new DecimalFormat("##.##").format(Double.parseDouble(item.getTotal())) + "₹");
        return convertview;
    }

    private class ViewHolder {
        TextView listview_invoice_no, listview_invoice_name, listview_invoice_qty, listview_invoice_price, listview_invoice_total;
    }
}
