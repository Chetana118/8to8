package com.globalwebsoft.a8to8;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.globalwebsoft.a8to8.Adapater.InvoiceAdapter;
import com.globalwebsoft.a8to8.Adapater.Invoice_list_adapter;
import com.globalwebsoft.a8to8.Adapater.product_list_Adapater;
import com.globalwebsoft.a8to8.Object.InvoiceObject;
import com.globalwebsoft.a8to8.Object.invoce_list_object;
import com.globalwebsoft.a8to8.Object.product_list_Object;
import com.globalwebsoft.a8to8.Other.MyProgressDialog;
import com.globalwebsoft.a8to8.Other.Utills;
import com.globalwebsoft.a8to8.SharedPreference.Datastorage;
import com.globalwebsoft.a8to8.volley.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ADMIJN on 21-Apr-18.
 */

public class Invoice_Activity extends AppCompatActivity {

    TextView product_add,stock_avilabality;
    Dialog dialog_customer, dailog_add_product, dailog_pro_list;
    ListView listview_invoice;
    InvoiceObject item;
    ArrayList<InvoiceObject> items;
    ImageView imgInvoiceRefresh;
    InvoiceAdapter adapter;
    TextView invoice_date, invoice_customer_name, invoice_customer_mobile, msglistview_invoice_submit, listview_invoice_subtotal,
            listview_invoice_igst, listview_invoice_cgst, listview_invoice_sgst, listview_invoice_grandtotal;
    TextView msglistview_invoice_igst, msglistview_invoice_cgst, msglistview_invoice_sgst;
    double sub_total = 0, sub_total2 = 0, grand_total = 0;
    double cgst = 0, sgst = 0, igst = 0;
    double t_igst = 0;
    double t_cgst = 0;
    double t_sgst = 0;
    String date, c_name, mobile;
    TextView select_product;
    EditText product_qty_add;
    ArrayList<product_list_Object> feeditems;
    product_list_Object feeditem;
    product_list_Adapater adapater;
    ListView product_name_list;
    Mytextview product_name;
    Mytextview add_product;
    String qty;
    int stock, manage_stock;
    String product_id, selectedproduct_name, slected_product_price;
    MyProgressDialog myProgressDialog;
    int position;
    String allready_product = null;
    double price = 0;
    int iqty = 0;
    TextView listview_invoice_no, listview_invoice_name, listview_invoice_qty, listview_invoice_price, listview_invoice_total;
    TextView msglistview_invoice_print;
    String orderid,pdfurl;
    public static int PICK_FILE = 1; String result;

    //print
    String[] separated;
    String[] splitconert;
    TextView textptint;
    String setdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgInvoiceRefresh = (ImageView) this.findViewById(R.id.imgInvoiceRefresh);
        listview_invoice  = (ListView) this.findViewById(R.id.listview_invoice);
        items = new ArrayList<InvoiceObject>();

        View headerView = ((LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.invoice_listviwe_hader, null, false);
        listview_invoice.addHeaderView(headerView, null, false);
        setdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        invoice_date = (TextView) headerView.findViewById(R.id.invoice_date);
        invoice_date.setText(setdate);
        invoice_customer_name = (TextView) headerView.findViewById(R.id.invoice_customer_name);
        invoice_customer_name.setText("8 to 8 Once More");
        invoice_customer_mobile = (TextView) headerView.findViewById(R.id.invoice_customer_mobile);
        View footerview = ((LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.invoice_listview_footer, null, false);
        listview_invoice.addFooterView(footerview, null, false);
        msglistview_invoice_submit = (TextView) footerview.findViewById(R.id.msglistview_invoice_submit);
        listview_invoice_subtotal = (TextView) footerview.findViewById(R.id.listview_invoice_subtotal);
/*
        msglistview_invoice_print = (TextView)footerview.findViewById(R.id.msglistview_invoice_print);
*/
        listview_invoice_igst = (TextView) footerview.findViewById(R.id.listview_invoice_igst);
        listview_invoice_sgst = (TextView) footerview.findViewById(R.id.listview_invoice_sgst);
        listview_invoice_cgst = (TextView) footerview.findViewById(R.id.listview_invoice_cgst);
        msglistview_invoice_igst = (TextView) footerview.findViewById(R.id.msglistview_invoice_igst);
        msglistview_invoice_sgst = (TextView) footerview.findViewById(R.id.msglistview_invoice_sgst);
        msglistview_invoice_cgst = (TextView) footerview.findViewById(R.id.msglistview_invoice_cgst);
        listview_invoice_grandtotal = (TextView) footerview.findViewById(R.id.listview_invoice_grandtotal);
        product_name = (Mytextview) this.findViewById(R.id.product_name);
        //textptint = (TextView)this.findViewById(R.id.textptint);

        //stock_avilabality = (TextView) this.findViewById(R.id.stock_avilabality);

        //  print module //
/*
        msglistview_invoice_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject JSONestimate = new JSONObject();
                JSONArray printarray = new JSONArray();
                for (int i = 0; i < items.size(); i++) {
                    try {
                        JSONestimate.put(String.valueOf(i), items.get(i).getJSONObject());
                        printarray.put(items.get(i).getJSONObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    result = String.valueOf(JSONestimate).replaceAll("[\\-\\+\\.\\^:{p_id,name,qty,price,total,\\\"\"]", "");

                    splitconert = result.split("\\}");
                    for (int p = 0; p < splitconert.length; p++) {
                        Toast.makeText(Invoice_Activity.this, "" + splitconert[p], Toast.LENGTH_SHORT).show();
                        Log.d("ARRAYSTRING", splitconert[p]);
                        // textptint.setText(result);
                        textptint.setText(splitconert[p]);
                        Toast.makeText(Invoice_Activity.this, ""+splitconert[p], Toast.LENGTH_SHORT).show();

                        String shareBody = "Here is the share content body";
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,"      * 8 To 8 Once More *"+"\n"+"   Invoice Date : "+date +"\n"+"   Name : "+c_name +"\n"+"   Mobile No : "+mobile +"\n"+ " --------------------------- " +"\n" + "NO  "+" Product "+" Qty "+" price "+" total "+"\n"+ position+1 +"   " + item.getName()  +"   " + item.getQty()+ "  "+item.getPrice()+"  "+item.getTotal()+"\n"+"             Sub Total  : "+sub_total+"\n"+"            Grand Total : "+grand_total +"\n\n\n");
                        //sharingIntent.putExtra(Intent.EXTRA_TEXT,"      * 8 To 8 Once More *"+"\n"+"   Invoice Date : "+date +"\n"+"   Name : "+c_name +"\n"+"   Mobile No : "+mobile +"\n"+ " --------------------------- " +"\n" + "NO  "+" Product "+" Qty "+" price "+" total "+"\n"+result+"\n"+ position+1 +"   " + item.getName()+ item.getQty()+ "  "+item.getPrice()+"  "+item.getTotal()+"\n"+"             Sub Total  : "+sub_total+"\n"+"            Grand Total : "+grand_total +"\n\n\n");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "       *   8 To 8 Once More   *" + "\n"
                                + "   Invoice Date : " + date + "\n" + "   Name : "
                                + c_name + "\n" + "   Mobile No : " + mobile + "\n" + " --------------------------- "
                                + "\n" + "NO  " + " Product " + " Qty " + " price " + " total " + "\n"
                                + splitconert + "\n" + "\n" + "             Sub Total  : " + sub_total + "\n"
                                + "            Grand Total : " + grand_total + "\n\n\n");
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
                    }
                }
            }
            // getpdf();
        });
*/
        listview_invoice_no    = (TextView) this.findViewById(R.id.listview_invoice_no);
        listview_invoice_name  = (TextView) this.findViewById(R.id.listview_invoice_name);
        listview_invoice_qty   = (TextView) this.findViewById(R.id.listview_invoice_qty);
        listview_invoice_price = (TextView) this.findViewById(R.id.listview_invoice_price);
        listview_invoice_total = (TextView) this.findViewById(R.id.listview_invoice_total);

       /* final InvoiceAdapter adapter = new InvoiceAdapter(Invoice_Activity.this, items);
        listview_invoice.setAdapter(adapter);*/

        adapter = new InvoiceAdapter(Invoice_Activity.this, items);
        listview_invoice.setAdapter(adapter);

        //userDetailsDialog();
        {
            imgInvoiceRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Invoice_Activity.this, "Refresh ", Toast.LENGTH_SHORT).show();

                    items.clear();
                    //feeditems.clear();
                    adapter.notifyDataSetChanged();
                    // listview_invoice.setAdapter(null);
                    sub_total = 0;
                    sub_total2 = 0;
                    listview_invoice_subtotal.setText("" + new DecimalFormat("##.##").format(sub_total) + "₹");
                    t_igst = (sub_total * igst) / 100;
                    t_cgst = (sub_total * cgst) / 100;
                    t_sgst = (sub_total * sgst) / 100;
                    listview_invoice_igst.setText("" + new DecimalFormat("##.##").format(t_igst) + "₹");
                    listview_invoice_cgst.setText("" + new DecimalFormat("##.##").format(t_cgst) + "₹");
                    listview_invoice_sgst.setText("" + new DecimalFormat("##.##").format(t_sgst) + "₹");
                    grand_total = sub_total + t_igst + t_cgst + t_sgst;
                    listview_invoice_grandtotal.setText("" + new DecimalFormat("##.##").format(grand_total) + "₹");
                    //date   = "";
                    //c_name = "";
                    //mobile = "";
                    if (feeditems.size() > 0) {
                        //userDetailsDialog();
                        insertProductDialog();

                        // Toast.makeText(Invoice_Activity.this, "Product not loaded, please re-open this screen", Toast.LENGTH_SHORT).show();
                    }
                }
            });

       /* listview_invoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                items.remove(i - 1);
                adapter.notifyDataSetChanged();
            }
        });*/

            listview_invoice.setAdapter(adapter);

            msglistview_invoice_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (items.size() > 0) {
                        uploadInvoice();
                        // sendarray();
                    } else {
                        Toast.makeText(Invoice_Activity.this, "Please add some product", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getpdf() {
        myProgressDialog = MyProgressDialog.show(Invoice_Activity.this, "", "", true, false, null);
        final String url = Utills.BaseUrl + "WS_Invoice_PDF.php";

        StringRequest list = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(ctx, ""+response, Toast.LENGTH_SHORT).show();

                myProgressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(response);
                    {

                        if (obj.getString("code").equals("1")) {
                            pdfurl = obj.getString("url");
                            //Toast.makeText(Invoice_Activity.this, ""+pdfurl, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(pdfurl), "application/pdf");
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(Invoice_Activity.this, "Oops something wrong"+e, Toast.LENGTH_SHORT).show();
                    myProgressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Invoice_Activity.this, "Server Down or Network problem"+error, Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("ordr_no", "" + orderid);
                return map;
            }
        };
        list.setShouldCache(false);
        list.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(list);
    }

    private void sendarray() {
        JSONObject JSONestimate = new JSONObject();
        JSONArray myarray = new JSONArray();

        for (int i = 0; i < items.size(); i++) {

            try {
                JSONestimate.put("data:" + String.valueOf(i + 1), items.get(i).getJSONObject());
                myarray.put(items.get(i).getJSONObject());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //myarray.put(JSONestimate);
        }
        Log.d("pratikarray", String.valueOf(myarray));
    }

    private void userDetailsDialog() {

        dialog_customer = new Dialog(new android.view.ContextThemeWrapper(Invoice_Activity.this, R.style.DialogSlideAnim));
        dialog_customer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_customer.setContentView(R.layout.customer_details_dailog);
        dialog_customer.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_customer.getWindow().setGravity(Gravity.CENTER);
        dialog_customer.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        final EditText txtCustomer_Date, txt_CustomName, txt_CustomerMobile, txt_CustomerAddress;
        final Mytextview Submit_cuatomerdeatils;
        ImageView imgCustomerClose;

        txtCustomer_Date = (EditText) dialog_customer.findViewById(R.id.txtCustomer_Date);
        String setdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        txt_CustomName = (EditText) dialog_customer.findViewById(R.id.txt_CustomName);
        txt_CustomerMobile = (EditText) dialog_customer.findViewById(R.id.txt_CustomerMobile);
        txt_CustomerAddress = (EditText) dialog_customer.findViewById(R.id.txt_CustomerAddress);
        Submit_cuatomerdeatils = (Mytextview) dialog_customer.findViewById(R.id.Submit_cuatomerdeatils);
        imgCustomerClose = (ImageView) dialog_customer.findViewById(R.id.imgCustomerClose);

        txtCustomer_Date.setInputType(InputType.TYPE_NULL);

        txtCustomer_Date.setText("");
        txt_CustomerAddress.setText("");
        txtCustomer_Date.setText(setdate);

        imgCustomerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_customer.dismiss();
                onBackPressed();
            }
        });

        Submit_cuatomerdeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                date   = txtCustomer_Date.getText().toString().trim();
                c_name = txt_CustomName.getText().toString().trim();
                mobile = txt_CustomerMobile.getText().toString().trim();

                if (date.equals("")) {
                    Toast.makeText(Invoice_Activity.this, "Please select date", Toast.LENGTH_SHORT).show();
                }else {
                    if (c_name.equals("")) {
                        Toast.makeText(Invoice_Activity.this, "Please enter customer name", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mobile.equals("")) {
                            if (mobile.length() != 10) {
                                Toast.makeText(Invoice_Activity.this, "PLease enter 10 digit valid mobile number", Toast.LENGTH_SHORT).show();
                            } else {
                                invoice_date.setText(date);
                                invoice_customer_name.setText(c_name);
                                invoice_customer_mobile.setText(mobile);
                                dialog_customer.dismiss();
                            }
                        } else {
                            invoice_date.setText(date);
                            invoice_customer_name.setText(c_name);
                            invoice_customer_mobile.setText(mobile);
                            dialog_customer.dismiss();
                        }
                    }
                }
            }
        });

        dialog_customer.show();
        dialog_customer.setCanceledOnTouchOutside(false);
        dialog_customer.setCancelable(false);
    }


    private void uploadInvoice() {
        {
            myProgressDialog = MyProgressDialog.show(Invoice_Activity.this, "", "", true, false, null);
            String url = Utills.BaseUrl + "WS_billing.php";

            StringRequest login = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    myProgressDialog.dismiss();

                    //Toast.makeText(Invoice_Activity.this, "***** "+response, Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject obj = new JSONObject(response);
                        String rsp = obj.getString("code");
                        Log.d("pvnRes ", rsp);

                        if (rsp.equals("1") == true) {
                            orderid = obj.getString("orderNo");
                            //Toast.makeText(Invoice_Activity.this, ""+orderid, Toast.LENGTH_SHORT).show();
                            Toast.makeText(Invoice_Activity.this, " Bill Successfully uploaded", Toast.LENGTH_SHORT).show();
                            //msglistview_invoice_print.setVisibility(View.VISIBLE);
                            //onBackPressed();
                        }
                        if (rsp.equals("0")) {
                            Toast.makeText(Invoice_Activity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                        }
                        if (rsp.equals("2")) {
                            Toast.makeText(Invoice_Activity.this, "Stack not Enough", Toast.LENGTH_SHORT).show();
                        }
                        if (rsp.equals("3")) {
                            Toast.makeText(Invoice_Activity.this, "Stack not Added", Toast.LENGTH_SHORT).show();
                        }
                        if (rsp.equals("4")) {
                            Toast.makeText(Invoice_Activity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                        if (rsp.equals("5")) {
                            Toast.makeText(Invoice_Activity.this, "Stack not Updated", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.d("pvnRes", "" + e);
                        Toast.makeText(Invoice_Activity.this, "Oops something wrong"+e, Toast.LENGTH_SHORT).show();
                        myProgressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Invoice_Activity.this, "Server Down or Network problem", Toast.LENGTH_SHORT).show();
                    myProgressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<String, String>();

                    map.put("truck_id", "" + Datastorage.ReadFromPreference("t_id", Datastorage.STRING_KEY, Invoice_Activity.this));
                   /* map.put("Customer_Name", "" + c_name);
                    map.put("Customer_Mobileno", "" + mobile);
                      map.put("date", "" + date);*/
                    map.put("Customer_Name", "8 To 8 Once More");
                    map.put("Customer_Mobileno", "000000000");
                    map.put("subtotal", "" + new DecimalFormat("##.##").format(sub_total));
                    map.put("igst", "0");
                    map.put("sgst", "0");
                    map.put("cgst", "0");
                    map.put("grand_total", "" + new DecimalFormat("##.##").format(grand_total));
                    map.put("date", "" + setdate);

                    JSONObject JSONestimate = new JSONObject();
                    JSONArray myarray = new JSONArray();

                    for (int i = 0; i < items.size(); i++) {

                        try {
                            JSONestimate.put("data:" + String.valueOf(i + 1), items.get(i).getJSONObject());
                            myarray.put(items.get(i).getJSONObject());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //myarray.put(JSONestimate);
                    }
                   // Log.d("CHRATAYAAHA", String.valueOf(myarray));
                    map.put("product_bill_array", String.valueOf(myarray));

                    return map;
                }
            };
            login.setShouldCache(false);
            login.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(login);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            insertProductDialog();
        } else {
            onBackPressed();
            //Toast.makeText(Invoice_Activity.this, "Product not loaded, please re-open this screen", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    private void insertProductDialog() {
        selectedproduct_name = "";
        qty = "";
        position = 0;
        dailog_add_product = new Dialog(new ContextThemeWrapper(Invoice_Activity.this, R.style.DialogSlideAnim));
        dailog_add_product.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailog_add_product.setContentView(R.layout.add_invoice_product_dailog);
        dailog_add_product.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailog_add_product.getWindow().setGravity(Gravity.CENTER);
        dailog_add_product.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        stock_avilabality = (TextView) dailog_add_product.findViewById(R.id.stock_avilabality);
        select_product = (TextView) dailog_add_product.findViewById(R.id.select_product);
        select_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailog_pro_list = new Dialog(new ContextThemeWrapper(Invoice_Activity.this, R.style.DialogSlideAnim));
                dailog_pro_list.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dailog_pro_list.setContentView(R.layout.listview_product);
                dailog_pro_list.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dailog_pro_list.getWindow().setGravity(Gravity.CENTER);
                dailog_pro_list.getWindow()
                        .setLayout(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                product_name_list = (ListView) dailog_pro_list.findViewById(R.id.product_name_list);
                feeditems = new ArrayList<product_list_Object>();
                fetch_product_list();
                dailog_pro_list.show();
            }
        });
        product_qty_add = (EditText) dailog_add_product.findViewById(R.id.product_qty_add);
        product_qty_add.setText(null);
        add_product = (Mytextview) dailog_add_product.findViewById(R.id.add_product);

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedproduct_name = (select_product).getText().toString().trim();
                qty = (product_qty_add).getText().toString().trim();
                iqty = (qty == null || qty.trim().equals("") ? 0 : Integer.parseInt(qty));

               // iqty = Integer.parseInt(qty);

                if (selectedproduct_name.equals("Select Product")) {
                    Toast.makeText(Invoice_Activity.this, "Please select product", Toast.LENGTH_SHORT).show();
                } else if ((stock == 0)) {
                    Toast.makeText(Invoice_Activity.this, selectedproduct_name + "is Not Available,So, you can't add product", Toast.LENGTH_SHORT).show();
                    dailog_add_product.dismiss();
                } else if (qty.equals("")) {
                    Toast.makeText(Invoice_Activity.this, "Please enter your quantity", Toast.LENGTH_SHORT).show();
                } else if ((iqty > stock)) {
                    Toast.makeText(Invoice_Activity.this, selectedproduct_name + "product Stock Insufficient.", Toast.LENGTH_SHORT).show();
                    dailog_add_product.dismiss();
                } else {
                    allready_product = null;
                    int allready_product_position = 0;
                    int k = items.size();
                    if (k != 0) {
                        for (int i = 0; i < k; i++) {
                            if (feeditems.get(position).getId().equals(items.get(i).getId())) {
                                allready_product = feeditems.get(position).getId();
                                allready_product_position = i;
                            }
                        }
                    }
                    if (allready_product == null) {

                        double product_price = Double.parseDouble(slected_product_price);
                        double total = (Double.parseDouble(qty) * product_price);
                        sub_total = sub_total + total;
                        sub_total2 = sub_total;

                        item = new InvoiceObject(feeditems.get(position).getId(), selectedproduct_name, qty, "" + new DecimalFormat("##.##").format(product_price), "" + new DecimalFormat("##.##").format(total), product_id);
                        items.add(item);
                        adapter.notifyDataSetChanged();
                        listview_invoice_subtotal.setText("" + new DecimalFormat("##.##").format(sub_total) + "₹");
                        grand_total = sub_total + t_igst + t_cgst + t_sgst;
                        listview_invoice_grandtotal.setText("" + new DecimalFormat("##.##").format(grand_total) + "₹");
                        imgInvoiceRefresh.setVisibility(View.VISIBLE);

                    } else {
                        double product_price = Double.parseDouble(slected_product_price);
                        double total = (Double.parseDouble(qty) * product_price);
                        double total2 = total;
                        sub_total = sub_total2 + total2;
                        sub_total2 = sub_total;
                        //  sub_total = sub_total2 + total2;

                        double p_qty = Double.parseDouble(items.get(allready_product_position).getQty()) + Double.parseDouble(qty);
                        total = total + Double.parseDouble(items.get(allready_product_position).getTotal());
                        total2 = product_price + total;

                        items.get(allready_product_position).setQty("" + new DecimalFormat("##.##").format(p_qty));
                        items.get(allready_product_position).setTotal("" + new DecimalFormat("##.##").format(total));
                        adapter.notifyDataSetChanged();

                        listview_invoice_subtotal.setText("" + new DecimalFormat("##.##").format(sub_total) + "₹");
                        grand_total = sub_total + t_igst + t_cgst + t_sgst;
                        listview_invoice_grandtotal.setText("" + new DecimalFormat("##.##").format(grand_total) + "₹");
                    }
                    dailog_add_product.dismiss();
                }
            }
        });

        dailog_add_product.show();
    }

    private void fetch_product_list() {
        feeditems.clear();
        position = 0;
        myProgressDialog = MyProgressDialog.show(Invoice_Activity.this, "", "", true, false, null);
        String url = Utills.BaseUrl + "WS_Products_List_name_price.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    myProgressDialog.dismiss();
                    String statusresponse = response.getString("status");

                    if (statusresponse.equalsIgnoreCase("success")) {
                        JSONArray jArray = response.getJSONArray("data");
                        JSONObject data = new JSONObject();

                        if (jArray != null) {
                            for (int i = 0; i < jArray.length(); i++) {
                                data = (JSONObject) jArray.get(i);
                                feeditem = new product_list_Object(data.getString("id"), data.getString("Product_Name"), data.getString("Price"));
                                feeditems.add(feeditem);
                                price = Double.parseDouble(data.getString("Price"));
                            }
                            final product_list_Adapater adapter = new product_list_Adapater(Invoice_Activity.this, feeditems);
                            product_name_list.setAdapter(adapter);
                            product_name_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    product_id = feeditems.get(i).getId().toString().trim();
                                    selectedproduct_name = feeditems.get(i).getProductname().toString().trim();
                                    slected_product_price = feeditems.get(i).getProductprice().toString().trim();
                                    select_product.setText(selectedproduct_name);
                                    position = i;
                                    dailog_pro_list.dismiss();
                                    fetckstaocklist();
                                    stock_avilabality.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    myProgressDialog.dismiss();
                    Toast.makeText(Invoice_Activity.this, "Oops something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myProgressDialog.dismiss();
                Toast.makeText(Invoice_Activity.this, "Server Down or Network problem" + error, Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request);
    }

    private void fetckstaocklist() {
        myProgressDialog = MyProgressDialog.show(Invoice_Activity.this, "", "", true, false, null);
        String url = Utills.BaseUrl + "WS_Get_Stock.php";

        StringRequest list = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(Invoice_Activity.this, "" + response, Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String rsp = obj.getString("code");

                    if (rsp.equals("1") == true) {
                        stock = (obj.getInt("available_stock"));
                        stock_avilabality.setText("Available Stock = " + stock);
                    }

                } catch (JSONException e) {
                    Toast.makeText(Invoice_Activity.this, "Oops something wrong", Toast.LENGTH_SHORT).show();
                    myProgressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Invoice_Activity.this, "Server Down or Network problem", Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("truck_id", "" + Datastorage.ReadFromPreference("t_id", Datastorage.STRING_KEY, Invoice_Activity.this));
                map.put("p_id", product_id);
                return map;
            }
        };

        list.setShouldCache(false);
        list.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(list);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(Invoice_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();*/
    }
}