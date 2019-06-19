package com.globalwebsoft.a8to8.Object;

public class invoce_list_object {

    String id, sportname ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSportname() {
        return sportname;
    }

    public void setSportname(String sportname) {
        this.sportname = sportname;
    }


    public invoce_list_object(String id, String sport_name) {
        this.id = id;
        this.sportname = sport_name;
    }
}
