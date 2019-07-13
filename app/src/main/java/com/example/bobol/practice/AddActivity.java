package com.example.bobol.practice;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;


public class AddActivity extends AppCompatActivity implements OnMapReadyCallback {
    public EditText firstName;
    public EditText lastName;
    public EditText emailAdd;
    public EditText mobile;
    public EditText address;
    public int pos;
    String[] mailAdd= {"@google.com","@naver.com","@nate.com","@entermate.com","@hanmail.com"};
    String domain;
    GoogleMap googleMap;
    final Geocoder geocoder =new Geocoder(this);
    Double Lat,Lon;
    String Str;
    Boolean Address_ok = false;
    ArrayList<FriendsItems> FriendsList;

    // Button complete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlayout);


        FriendsList = new ArrayList<>();
        FriendsList = (ArrayList<FriendsItems>) getIntent().getSerializableExtra("FriendsList");




        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.add_map);
        mapFragment.getMapAsync(this);



        TextView textView = (TextView) findViewById(R.id.emailAdd);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mailAdd); // 도메인 array를 스피너 어댑터에 장착(?)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                domain = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // domain = spinner.getSelectedItem().toString();// spinner에서 받은 텍스트를 domain에 저장

        lastName = (EditText) findViewById(R.id.editText1);
        firstName = (EditText) findViewById(R.id.editText2);
        mobile = (EditText) findViewById(R.id.editText4);
        emailAdd = (EditText) findViewById(R.id.editText3);
        address = (EditText) findViewById(R.id.Address);




        Button complete = (Button) findViewById(R.id.completeAdd);

        /** 검색버튼 */
        Button Add_Search = (Button) findViewById(R.id.Add_Search);

        Add_Search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                transAdd(address.getText().toString());

            }
        });
        /** 추가 완료 버튼 */
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean found = false;

                for (int i = 0; i < FriendsList.size(); ++i) //TABLE의 총 행만큼 for문을 돌려서  입력받은 mobile값과 테이블 내의 모바일들을 비교
                {
                    if (FriendsList.get(i).getMobile().equals(mobile.getText().toString())) //i:3은 mobile열을 의미함
                    {
                        found = true; // 동일한 전화번호를 찾았을시 found에 true값을 넘겨줌
                        break;
                    }

                }
                if (found == true) {
                    Toast.makeText(getApplicationContext(), "중복된 전화번호 입니다.", Toast.LENGTH_LONG).show();
                }
                //중복된 전화번호가 없다면 커서를 닫고 새로 추가
                else {
                    if(Address_ok==false){

                        Toast.makeText(getApplicationContext(), "올바른 주소를 입력해주세요.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        FriendsItems addItems = new FriendsItems(0, lastName.getText().toString(), firstName.getText().toString(), mobile.getText().toString(), emailAdd.getText().toString(), domain, address.getText().toString());

                        Intent intent = new Intent();
                        intent.putExtra("addItems", addItems);
                        // intent.putExtra("pos", pos);
                        setResult(RESULT_OK, intent);// StartActivityForResult에서 보낸 REQUEST_CODE와 함께 RESULT_OK라는 RESULT값과 data를 함께 보냄
                        finish();
                    }
                }
            }
        });

    }


        @Override
        public void onMapReady (GoogleMap Maps){ //추가 화면에서 초기 지역은 서울시 서초구 양재동으로 Setting
            LatLng latLng = new LatLng(37.4731977, 127.03817019999997);

            MarkerOptions markerOptions;
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("서울시 서초구 양재동");
            Maps.addMarker(markerOptions).showInfoWindow();

            Maps.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            Maps.animateCamera(CameraUpdateFactory.zoomTo(15));
            googleMap = Maps;

        }
/*

    public void gogoMap(Double latitude, Double longitude){ // 위도 경도 값을 받아서 해당 위치로 카메라를 움직이고 마커를 찍는 메서드

        if(latitude!=null) {

            CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            googleMap.moveCamera(update);

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.animateCamera(zoom);

            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latitude, longitude));
            markerOptions.title(address.getText().toString() + "");
            googleMap.addMarker(markerOptions).showInfoWindow(); //마커를 추가하고 정보를 띄움
            Address_ok = true; //검색후 정상적 지도를 확인 Address_ok를 true로 반환하고 연락처 추가 가능 상태로 만듬
        }
        if(latitude==null)
        {Toast.makeText(getApplicationContext(),"올바른 주소가 아닙니다.",Toast.LENGTH_LONG).show();}

    }
*/




    public void transAdd(String address){ //일반 주소를 받아 Geocoder 로 위도 경도 추출하는 메서드

        List<Address> list= null;

        Str = address;


        try {
            list = geocoder.getFromLocationName(Str, 10); // Str 은 주소로 받은 String 을 지오코더의 getFromLocationName 메소드를 이용하여 위도 경도 국가등등 값을 최대 10개 List에 저장함
        }catch (IOException e){
            e.printStackTrace();
            Log.e("Hong","오류:입출력 오류입니다.");
        }

        if(list!=null) {
            if (list.size() == 0) { // 리스트에 값을 받지 못했을시 올바른 주소가 아닙니다라는 Toast를 띄움
                Toast.makeText(getApplicationContext(), "올바른 주소가 아닙니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                Address addr = list.get(0);//리스트의 첫번째 정보를 담은 addr에서 위도와 경도값을 Double 형태의 Lat과 Lon에 각각 저장.

                Lat = addr.getLatitude();
                Lon = addr.getLongitude();

                LatLng target = new LatLng(Lat, Lon); // 받아온 주소의 위도,경도를 new LatLng에 저장함.

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(target); //받아온 주소의 위,경도에 마커를 setting
                markerOptions.title(lastName.getText().toString() + firstName.getText().toString() + "의 집"); //Marker의 제목을 해당 주소를 가진 친구의 이름으로 setting
                googleMap.addMarker(markerOptions).showInfoWindow(); // 마커 클릭시 나오게 되는 Title을 항상 show
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(target)); // 초기화면을 서울로 set
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }else{ Toast.makeText(getApplicationContext(), "주소를 입력해 주세요.", Toast.LENGTH_SHORT).show();}


    }




    public void onBackButtonClick(View v) //뒤로가기 버튼
    {
        finish();
    }

}





