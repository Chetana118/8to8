package com.globalwebsoft.a8to8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.Spanned;
import android.util.Printer;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class Print_from extends Activity {

    TextView print;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

       /* print = (TextView) this.findViewById(R.id.print);
        print.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
               // Printer mPrinter = new Printer(Printer.TM_T88, Printer.HOIN, this);
            }
        });*/

        String htmlAsString = getString(R.string.app_name);      // used by WebView
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView

        // set the html content on a TextView
        print = (TextView) this.findViewById(R.id.print);
        print.setText(htmlAsSpanned);
        WebView webView = (WebView) findViewById(R.id.webView);
         webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);

    }
}