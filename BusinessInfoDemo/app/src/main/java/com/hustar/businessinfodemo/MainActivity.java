package com.hustar.businessinfodemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    EditText searchEditText;
    Button sidebarBtn;
    Spinner spinner;
    ListView listView;

    boolean STEP_NO = false, STEP_SEC = false, STEP_NAME = false,  STEP_AD = false, STEP_ADDR = false, STEP_MENU = false, STEP_PRICE = false;
    String no = null, name = null, sec = null, ad = null, addr = null, menu = null, price = null;

    AssetManager am;
    InputStream is;

    String searchWord = "";  //검색할 단어
    String searchTag = "업종";   //검색할 단어 포함된 태그

    ArrayList<String> spinArr;
    ArrayAdapter<String> arrayAdapter;

    InputMethodManager im;
    ArrayList<ListItem> oData;

    public static Context context_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context_main = this;

        searchEditText = findViewById(R.id.searchEditText);
        listView = findViewById(R.id.listView);
        sidebarBtn = findViewById(R.id.sidebarBtn);

        am = getResources().getAssets();        //AssetManager
        is = null;                      //inputStream
        im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);       //InputMethodManager

////////////////////////검색할 태그 선택하는 스피너///////////////////
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
                switch (searchTag) {
                    case "업종":
                        searchEditText.setHint("ex) 한식, 일식, 중식, 이미용업...");
                        break;
                    case "주소":
                        searchEditText.setHint("도로명 주소 또는 동 ex) 수성로...");
                        break;
                    case "대표메뉴":
                        searchEditText.setHint("짜장면, 찜닭...");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
///////////////////// 스피너 END //////////////////////////////

        //액티비티 뜨자마자 데이터 출력
        parseData();

////////////////////사이드 메뉴/////////////////////////////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id= item.getItemId();

                if(id == R.id.home) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if(id == R.id.search) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if(id == R.id.board) {
                    Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;
            }
        });

        sidebarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //키보드이벤트
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        //단어 검색
                        searchWord = searchEditText.getText().toString();
                        //키보드 내리기
                        im.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                        parseData();
                        break;

                    case KeyEvent.KEYCODE_DEL:
                        searchEditText.setText("");
                        break;
                }
                return true;
            }
        });



        //listView 클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailInfoActivity.class);
                intent.putExtra("name", oData.get(i).getName());
                intent.putExtra("addr", oData.get(i).getAddr());
                intent.putExtra("menu", oData.get(i).getMenu());
                intent.putExtra("price", oData.get(i).getPrice());
                intent.putExtra("sector", oData.get(i).getSecor());
                intent.putExtra("ad", oData.get(i).getAd());

                startActivity(intent);
            }
        });
    }

    //데이터 파싱/검색
    public void parseData() {
        //itemList
        oData = new ArrayList<>();

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
                                    ListItem oItem = new ListItem();
                                    oItem.setName(name);
                                    oItem.setMenu(menu);
                                    oItem.setPrice(price);

                                    oItem.setAd(ad);
                                    oItem.setAddr(addr);
                                    oItem.setSecor(sec);

                                    oData.add(oItem);
                                }
                            } else if(searchTag.equals("주소")) {
                                if(addr.contains(searchWord)) {
                                    ListItem oItem = new ListItem();
                                    oItem.setName(name);
                                    oItem.setMenu(menu);
                                    oItem.setPrice(price);

                                    oItem.setAd(ad);
                                    oItem.setAddr(addr);
                                    oItem.setSecor(sec);

                                    oData.add(oItem);
                                }
                            } else if(searchTag.equals("대표메뉴")) {
                                if(menu.contains(searchWord)) {
                                    ListItem oItem = new ListItem();
                                    oItem.setName(name);
                                    oItem.setMenu(menu);
                                    oItem.setPrice(price);

                                    oItem.setAd(ad);
                                    oItem.setAddr(addr);
                                    oItem.setSecor(sec);

                                    oData.add(oItem);
                                }
                            }
                        }
                        break;
                }
                eventType = parser.next();

                ListAdapter oadapber = new ListAdapter(oData);
                listView.setAdapter(oadapber);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}