package com.hustar.businessinfodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance()


        boolean STEP_NO = false, STEP_SEC = false, STEP_NAME = false,  STEP_AD = false, STEP_ADDR = false, STEP_MENU = false, STEP_PRICE = false;
        String no = null, name = null, sec = null, ad = null, addr = null, menu = null, price = null;

        AssetManager am = getResources().getAssets();
        InputStream is = null;

        TextView textView = findViewById(R.id.textView);

        try {
            //파싱하는 데이터파일 src/main/assets/data.xml
            is = am.open("data.xml");

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(is, "UTF-8");


            //파싱 시작
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG :
                        if(parser.getName().equals("연번")) {
                            STEP_NO = true;
                        }
                        if(parser.getName().equals("업종")) {
                            STEP_SEC = true;
                        }
                        if(parser.getName().equals("업소명")) {
                            STEP_NAME = true;
                        }
                        if(parser.getName().equals("구군")) {
                            STEP_AD = true;
                        }
                        if(parser.getName().equals("주소")) {
                            STEP_ADDR = true;
                        }
                        if(parser.getName().equals("대표메뉴")) {
                            STEP_MENU = true;
                        }
                        if(parser.getName().equals("대표메뉴가격_원_")) {
                            STEP_PRICE = true;
                        }
                        break;

                    case XmlPullParser.TEXT :
                        if(STEP_NO) {
                            no = parser.getText();
                            STEP_NO = false;
                        }
                        if(STEP_SEC) {
                            sec = parser.getText();
                            STEP_SEC = false;
                        }
                        if(STEP_NAME) {
                            name = parser.getText();
                            STEP_NAME = false;
                        }
                        if(STEP_AD) {
                            ad = parser.getText();
                            STEP_AD = false;
                        }
                        if(STEP_ADDR) {
                            addr = parser.getText();
                            STEP_ADDR = false;
                        }
                        if(STEP_MENU) {
                            menu = parser.getText();
                            STEP_MENU = false;
                        }
                        if(STEP_PRICE) {
                            price = parser.getText();
                            STEP_PRICE = false;
                        }
                        break;

                    case XmlPullParser.END_TAG :
                        if(parser.getName().equals("Row")) {
                            textView.append(no + "\n" + sec + "\n" + name + "\n" + ad + "\n" + addr + "\n" + menu + "\n" + price + "\n\n");
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}