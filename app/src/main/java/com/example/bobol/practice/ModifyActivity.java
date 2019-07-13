package com.example.bobol.practice;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
//@SuppressWarnings("all") 단순히 모든 워닝을 보이지 않게 해줌 ;
//안쓰는 import 정리 ctrl+ alt+ o
public class ModifyActivity extends AppCompatActivity implements OnMapReadyCallback { //onMapReadyCallback 인터페이스를 구현하여 OnMapReady 메소드를 구현함.

FriendsItems items;
int position;
EditText get_last_Name;
EditText get_first_Name;
EditText get_mo_Bile;
EditText get_email_Add;
String[] mailAdd= {"@google.com","@naver.com","@nate.com","@entermate.com","@hanmail.com"};
private String domain;
int spinnerPosition;
EditText get_Address;
public Double Lat;
public Double Lon;
GoogleMap googleMap;
final Geocoder geocoder =new Geocoder(this);
ScrollView scrollView;
ImageView transparent;
Spinner spinner;
double Old_Lat, Old_Lon;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editlayout);


       /**   해당 Layout(editLayout)에 googleMap Fragment를 추가한 후 getMapAsync로 호출하여 콜백     **/
       //GoogleMap mapFragment를 추가하는 두가지 방법 : 1.해당 액티비티에 fragment를 요소를 추가.
       //                                               2.코드에서 MapFragment를 를 추가(MapFragment 인스턴스를 생성한 후 FragmentTransaction.add()를 호출하여 현재 Activity에 추가.

        //FragmentManager를 호출하여 MapFragment를 Layout에 태그한 Fragment를 정의하고 getMapAsync를 통해 this(ModifyActivity)로 콜백한다.
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.edit_map);
        mapFragment.getMapAsync(this);



       /***  스크롤뷰 안에 있는 구글맵의 스크롤을 자유롭게 도와줌(맵 프래그먼트 안에 투명한 이미지뷰를 넣은 후 이미지 뷰를 터치시 스크롤의 이벤트를 제어함. **/
             scrollView = (ScrollView)findViewById(R.id.editScroll);
             transparent = (ImageView)findViewById(R.id.imageTrans);

            // 맵뷰에 투명한 이미지를 OnTouch(ACTION_DOWN)시 scrollView의 스크롤을 제한하기 위함.
             transparent.setOnTouchListener(new View.OnTouchListener() {
                 @Override
                 public boolean onTouch(View view, MotionEvent motionEvent) {
                        int action = motionEvent.getAction();
                        switch(action){
                             case MotionEvent.ACTION_DOWN: //영역을 눌렀을때 처리
                                scrollView.requestDisallowInterceptTouchEvent(true);
                                //return false; //return false로 빠져나와서 break가 없음.
                                break;
                             case MotionEvent.ACTION_UP:
                                 view.performClick();
                                 scrollView.requestDisallowInterceptTouchEvent(false);
                             //    return false;
                                 break;
                            default:
                                //return false;
                                break;
                        }
                     return false;
                }


             });


        items = (FriendsItems) getIntent().getSerializableExtra("items"); // HeaderActivity에서 전달받은 data들을 get함
        position = getIntent().getIntExtra("position", position);


        TextView textView = (TextView) findViewById(R.id.emailAdd);

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mailAdd); // mainAdd라는 String Array를 스피너 어댑터에 정의

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 스피너 어댑터 정의 및 setting
        spinner.setAdapter(spinnerAdapter);
        spinnerPosition = spinnerAdapter.getPosition(items.domain); //전달받은 domain값을 Adapter의 position에따라 int 값으로 받음
        spinner.setSelection(spinnerPosition);// 받은 int 값으로 초기 스피너를 setting 함



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //스피너의 배열에서 아이템을 선택할시
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                domain = (String) spinner.getSelectedItem(); //스피너에서 선택된 아이템을 String domain에 저장함.
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {  //선택안했을시..

            }
        });



        //HeaderActivity에서 클릭된 정보를 items와 position에 저장

        // items 의 각각의 정보를 get_name에 set함
        get_last_Name = (EditText) findViewById(R.id.get_lastname);
        get_last_Name.setText(items.getLastName());

        get_first_Name = (EditText) findViewById(R.id.get_firstname);
        get_first_Name.setText(items.getFirstName());

        get_mo_Bile = (EditText) findViewById(R.id.get_mobile);
        get_mo_Bile.setText(items.getMobile());

        get_email_Add=(EditText)findViewById(R.id.get_emailAdd);
        get_email_Add.setText(items.getEmailAdd());



        get_Address = (EditText)findViewById(R.id.Address);
        get_Address.setText(items.getAddress());




        Button completeEdit = (Button) findViewById(R.id.completeEdit);

        completeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                FriendsItems modeItems = new FriendsItems(items.get_id(),get_last_Name.getText().toString(), get_first_Name.getText().toString(), get_mo_Bile.getText().toString(),get_email_Add.getText().toString(),domain,get_Address.getText().toString());

                intent.putExtra("modeItems", modeItems); //id 값과 수정된 값을 다시 modeItems에 저장하여 intent함
                intent.putExtra("position", position);
                intent.putExtra("domain",domain);

                setResult(RESULT_OK, intent); // StartActivityForResult에서 보낸 REQUEST_CODE와 함께 RESULT_OK라는 RESULT값과 data를 함께 보냄
                // startActivity(intent);
                finish();

            }
        });

        /** 공유하기 **/
        Button share = (Button)findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String title = "전화번호"; //
                String content = (get_mo_Bile.getText().toString()+get_last_Name.getText().toString()+get_first_Name.getText().toString()+get_email_Add.getText().toString()+domain+get_Address.getText().toString());// 공유할 대상 data 모두 set

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // text타입의 data를 intent 함
                intent.putExtra(Intent.EXTRA_SUBJECT,title);
                intent.putExtra(Intent.EXTRA_TEXT,content);

              //  intent.setAction(Intent.ACTION_SEND);// !!!!!!!!!ACTION_SEND를 통해서 다른 Activity , 다른 process내 Activity까지 전달이 가능하다 ,공유 전달하는 app이 나타남. 액션에 대한 세팅은 intent를 정의할때 해도됨
                Intent chooser = Intent.createChooser(intent,"공유"); // chooser를 날리면 setType과 관련있는 모든 앱이 list 형태로 제공된다.

                startActivity(chooser);

            }
        });

        /** SMS보내기 **/
        Button send_SMS = (Button)findViewById(R.id.sendSMS);

        send_SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            //    Intent hong = new Intent (getApplicationContext(),HeaderActivity.class); // 인텐트의 두가지 Type
                Intent it2 =new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+get_mo_Bile.getText().toString())); //ACTION_DIAL을 통해 전화를 거는 어플리케이션으로 Uri와 함께 Intent한다
                Intent it = new Intent(Intent.ACTION_VIEW); //implict intent MIME 타입에 따라 안드로이드 시스템에서 적절한 다른앱의 액티비티를 찾은후 띄운다. 반대로 인텐트에서 클래스를 지정하여 호출하는 방식은 Explict intent(명시적 인텐트)라고 한다.
                it.putExtra("address",get_mo_Bile.getText().toString()); //address(전화번호에 연락처 setting)
                it.putExtra("sms_body","문자보냅니다.");// sms_body에 문자보낼 내용 set
                it.setType("vnd.android-dir/mms-sms");
                startActivity(it);



            }
        });

       /**   지역 검색 **/
        Button searchAdd = (Button)findViewById(R.id.searchAdd); // 주소 검색을 눌렀을시 일반 주소를 위도(Lat), 경도(Lon)으로 변환해줌

        searchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transAdd(get_Address.getText().toString());

                LatLng target = new LatLng(Lat,Lon);
                if(Old_Lat!=Lat) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(target); //받아온 주소의 위,경도에 마커를 setting
                    markerOptions.title(get_Address.getText().toString()); //Marker의 제목을 해당 주소를 가진 친구의 이름으로 setting
                    googleMap.addMarker(markerOptions).showInfoWindow(); // 마커 클릭시 나오게 되는 Title을 항상 show

                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(target)); // 초기화면을 서울로 set
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    Old_Lat= Lat;
                    Old_Lon= Lon;
                }
            }

        });

       }
    /**GeoCode 주소 변환*/
    public void transAdd(String address){ //일반 주소를 받아 Geocoder로 위도 경도 추출하는 메서드

        List<Address> list= null;

        String trans_Add = address;

        try {
            list = geocoder.getFromLocationName(trans_Add, 1); // Str은 주소로 받은 String을 지오코더의 getFromLocationName 메소드를 이용하여 위도 경도 국가등등 값을 최대 10개 List에 저장함
        }catch (IOException e){
            e.printStackTrace();
            Log.e("Hong","오류:입출력 오류입니다.");
        }

        if(list!=null) {
            if (list.size() == 0) { // 리스트에 값을 받지 못했을시 올바른 주소가 아닙니다라는 Toast를 띄움
                Toast.makeText(getApplicationContext(), "올바른 주소가 아닙니다.", Toast.LENGTH_LONG).show();

            }
            else {
                Address addr = list.get(0);//Address 인스턴스에 리스트의 첫번째 정보를 담은 addr에서 위도와 경도값을 Double 형태의 Lat과 Lon에 각각 저장.

                Lat = addr.getLatitude();
                Lon = addr.getLongitude();

                LatLng target = new LatLng(Lat, Lon); // 받아온 주소의 위도,경도를 new LatLng에 저장함.

            }
        }
        else{ Toast.makeText(getApplicationContext(), "주소를 입력해 주세요.", Toast.LENGTH_LONG).show();} //list가 null일시...


    }

    /** 초기 Map  Setting **/
    @Override
    public void onMapReady( GoogleMap Maps) {

                transAdd(get_Address.getText().toString()); //transAdd 를 통해 입력받은 주소의 위도와 경도값을 추출받음

                LatLng friendsHome = new LatLng(Lat,Lon); //friendsHome 에 위도 경도를 set 함

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(friendsHome); //  friendsHome 에 set 한 위도경도 위치에 마커를 추가하고 마커 위에 제목을 띄움
                markerOptions.title(items.getLastName()+items.getFirstName()+"의 집");
                Maps.addMarker(markerOptions).showInfoWindow();

                Maps.moveCamera(CameraUpdateFactory.newLatLng(friendsHome)); // 초기화면을 friendsHome 의 위치로 setting
                Maps.animateCamera(CameraUpdateFactory.zoomTo(15)); //15만큼 zoom
                googleMap=Maps;
                Old_Lat =Lat; // 전역변수 Lat와 Lon에 맵에 최초로 setting 되는 위도와 경도를 전달함
                Old_Lon =Lon;


            }




    public void onBackEditClick(View v){

        finish();
    }
}
