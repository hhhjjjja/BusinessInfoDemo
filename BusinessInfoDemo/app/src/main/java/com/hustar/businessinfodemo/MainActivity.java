package com.hustar.businessinfodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText searchEditText;
    Button searchBtn;

    TextView textView;
    Spinner spinner;

    boolean STEP_NO = false, STEP_SEC = false, STEP_NAME = false,  STEP_AD = false, STEP_ADDR = false, STEP_MENU = false, STEP_PRICE = false;
    String no = null, name = null, sec = null, ad = null, addr = null, menu = null, price = null;

    AssetManager am;
    InputStream is;

    String searchWord;  //검색할 단어
    String searchTag;   //검색할 단어 포함된 태그

    ArrayList<String> spinArr;
    ArrayAdapter<String> arrayAdapter;

    InputMethodManager im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        searchEditText = findViewById(R.id.searchEditText);

        am = getResources().getAssets();        //AssetManager
        is = null;                      //inputStream
        im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);       //InputMethodManager

        //검색할 태그 선택하는 스피너
        spinArr = new ArrayList<String>();
        spinArr.add("업종");
        spinArr.add("주소");
        spinArr.add("대표메뉴");

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinArr);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchTag = spinArr.get(i);
                Toast.makeText(getApplicationContext(), searchTag, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        //초기화
                        textView.setText("");

                        //단어 검색
                        searchWord = searchEditText.getText().toString();

                        //키보드 내리기
                        im.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

                        Toast.makeText(getApplicationContext(),searchWord, Toast.LENGTH_SHORT).show();
                        parseData();
                        break;

                    case KeyEvent.KEYCODE_DEL:
                        searchEditText.setText("");
                        break;
                }
                return true;
            }
        });

        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //초기화
                textView.setText("");

                //단어 검색
                searchWord = searchEditText.getText().toString();
                Toast.makeText(getApplicationContext(),searchWord, Toast.LENGTH_SHORT).show();

                parseData();
            }
        });
    }

    public void parseData() {
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
                            //검색하는 단어가 포함되어있는지 확인
                            if(searchTag.equals("업종")) {
                                if(sec.contains(searchWord)) {
                                    textView.append(no + "\n" + sec + "\n" + name + "\n" + ad + "\n" + addr + "\n" + menu + "\n" + price + "\n\n");
                                }
                            } else if(searchTag.equals("주소")) {
                                if(addr.contains(searchWord)) {
                                    textView.append(no + "\n" + sec + "\n" + name + "\n" + ad + "\n" + addr + "\n" + menu + "\n" + price + "\n\n");
                                }
                            } else if(searchTag.equals("대표메뉴")) {
                                if(menu.contains(searchWord)) {
                                    textView.append(no + "\n" + sec + "\n" + name + "\n" + ad + "\n" + addr + "\n" + menu + "\n" + price + "\n\n");
                                }
                            }
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