package com.globalwebsoft.a8to8.Object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ADMIJN on 25-Jan-18.
 */

public class InvoiceObject {
    String id, name,qty, price, total, product_id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public InvoiceObject(String id, String name, String qty, String price, String total, String product_id) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.product_id = product_id;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("p_id", product_id);
            obj.put("p_name", name);
            obj.put("p_qty", qty);
            obj.put("p_price", price);
            obj.put("p_total", total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}

