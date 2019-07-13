package com.example.bobol.practice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class HeaderActivity extends Activity {

    int position;
    FriendsItems addItems;// 새로 생성된 아이템들
    FriendsItems modeItems;// 수정된 아이템들
    String domain;
    int REQUEST_CODE_ADD = 1;
    int REQUEST_CODE_EDIT = 2;
    public FriendsAdapter adapter;
    public DbOpenHelper HdbOpenHelper;
    public ListView listView;
    public ArrayList<FriendsItems> FriendsList;


    @Override
    protected void onDestroy() { //메인 액티비티가 종료 될때 DB를 닫아둠(수정단에서 닫으면 수정된걸 수정할때 db가 닫혀서 접근 안됨)
        super.onDestroy();
        HdbOpenHelper.db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //DB 에서 데이터 받아오기 위한 Helper 객체 생성& open
        HdbOpenHelper = new DbOpenHelper(this);
        HdbOpenHelper.open();



     //DB의 List 를 넣을 새로운 ArrayList 를 생성

        FriendsList = new ArrayList<>();

        Cursor cursor = DbOpenHelper.db.rawQuery("SELECT * FROM ContactList ORDER BY lastname ASC",null); // 커서를 열어서 rawQuery의 SELECT문으로  DB의 데이터를 lastName 의 오름차순으로 데이터를 받아옴


        cursor.moveToFirst(); //커서를 제일 처음의 행으로 가져온다.
        for(int i = 0; i< cursor.getCount();i++) //i는 0부터 DB의 열의 갯수만큼 증가하면서 실제 데이터를 넣는 과정
        {


            FriendsList.add(new FriendsItems(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)));// DB 테이블의 실제 데이터를 가지고 온다.
            //0:_id, 1:lastName, 2:firstName, 3:mobile, 4:emailAdd, 5:domain 6:address
            cursor.moveToNext(); //커서를 다음행으로 이동시킨다.
        }




        //어댑터 생성 및 리스트에 어댑터 setting

        adapter = new FriendsAdapter(FriendsList,getApplicationContext()); //FriendsList라는 ArrayList를 adapter의 장착해서 listView에 뿌려줌
        listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); //ListView를 뿌리는 adapter에게 데이터가 변경됨을 알려줌


        /** Modify **/
        //리스트 클릭시 이벤트 생성
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                ListView friends = (ListView) parent;
                //parent 를 통해 position 에 해당되는 아이템의 정보를 가져온다
                FriendsItems itemsList = (FriendsItems) friends.getItemAtPosition(position);
                //ModifyActivity 에 정보를 전달
                Intent intent = new Intent(getApplicationContext(), ModifyActivity.class);
                intent.putExtra("items", itemsList); // 리스트들과 해당 position 을 전달
                intent.putExtra("position", position);

                //-1번째 위치가 아니라면 ModifyActivity 로 intent
                if (position != -1) {

                    startActivityForResult(intent, REQUEST_CODE_EDIT); //REQUEST_CODE 를 전달해서 결과를 기다림

                }

            }

            });

        /** 삭제 **/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){// 롱클릭 리스너로 롱클릭했을시 데이터 삭제

                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                    ListView friends = (ListView) parent;
                    final FriendsItems itemsList = (FriendsItems) friends.getItemAtPosition(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(HeaderActivity.this); // AlertDialog 를 build 하여 사용

                    builder.setTitle("삭제 확인 상자"); //제목 설정
                    builder.setMessage("연락처를 삭제하시겠습니까?"); // 메시지 설정

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() { //positive 버튼 클릭시 선택된 data 삭제

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            HdbOpenHelper.deleteList(itemsList.get_id());// 롱클릭한 개체를 DB와 List에서 지움
                            FriendsList.remove(position);
                            adapter.notifyDataSetChanged(); //ListView 를 뿌리는 adapter 에게 데이터가 변경됨을 알려줌*/
                            Toast.makeText(getApplicationContext(),itemsList.getLastName()+itemsList.getFirstName()+"님을 삭제하였습니다.",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() { //negative 버튼 클릭시 dialog 취소
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                return true;// 리턴값을 false 로 넘겨주면 롱클릭시 클릭 이벤트도 함께 실행됨


                }

        });


    /*******************  추가   *********************/
        Button AddButton = (Button) findViewById(R.id.AddButton);//추가버튼

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.putExtra("FriendsList",FriendsList);

                startActivityForResult(intent, REQUEST_CODE_ADD);//추가버튼을 눌렀을시 REQUEST_CODE를 전달해서 결과를 기다림

            }
        });


    }

    /************** 결과값을 받아옴 ****************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)//setResult 에서 보낸 requestCode 와 resultCode, data 를 받는 메소드
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD) { //ADD 라는 REQUEST_CODE 와 RESULT_OK를 받게되면..
           // Log.e("hong","REQUEST_CODE_ADD:"+REQUEST_CODE_ADD);
            if (resultCode == RESULT_OK)
            {
                addItems = (FriendsItems) data.getSerializableExtra("addItems");
                domain = data.getStringExtra("domain");
                    int _id =0;
                    int size =FriendsList.size();
                    boolean found= false;
                    for(int i= 0 ; i<size;++i) {

                        if (FriendsList.get(i).getLastName().compareTo(addItems.lastName) > 0) {
                            _id = i;
                            found = true;
                        }
                          if(found){
                            FriendsList.add(_id, addItems); // 리스트에 추가된 아이템들을 넣음
                            break;}
                        }



                 if(!found){  FriendsList.add(size,addItems);}
                    adapter.notifyDataSetChanged();//ListView 를 뿌리는 adapter 에게 데이터가 변경됨을 알려줌

                    HdbOpenHelper.insert(addItems.lastName,addItems.firstName,addItems.mobile,addItems.emailAdd,domain,addItems.address); //openHelper 의 insert 메소드를 이용해 전달받은 각각의 데이터를 추가함
                    Toast.makeText(getApplicationContext(), "생성된 이름은 " + addItems.lastName + addItems.firstName, Toast.LENGTH_LONG).show();

            }
        }


        if (requestCode == REQUEST_CODE_EDIT) { //EDIT 라는 REQUEST_CODE 를 받게되면..

            if (resultCode == RESULT_OK) {

                modeItems = (FriendsItems) data.getSerializableExtra("modeItems"); //modeItems 는 modifyActivity 에서 받아온 data.
                position = data.getIntExtra("position", -1);
                domain = data.getStringExtra("domain");


                if (modeItems != null) {
                    boolean found= false; //

                    for(int i = 0 ; i<FriendsList.size(); i++)
                    {
                        if(FriendsList.get(i).getLastName().compareTo(modeItems.lastName)>0){

                            found = true; //
                            //수정된 데이터를 오름차순정렬하여 리스트의 set, remove 는 수정된 데이터와 이전데이터가 동시에 남아있기 때문
                            FriendsList.add(i,modeItems);
                            FriendsList.remove(position);
                            adapter.notifyDataSetChanged();
                            break;
                        }

                    }
                    if(!found) // 리스트의 오름차순 중 가장 아래위치 하게 될 경우
                    {
                        FriendsList.add(FriendsList.size(),modeItems);
                        FriendsList.remove(position);
                    }
                    HdbOpenHelper.updateList(modeItems.lastName,modeItems.firstName,modeItems.mobile,modeItems.emailAdd,modeItems.get_id(),domain,modeItems.address);//DB update 수정된 아이템이 리스트에 새로 생성되고있음
                    Toast.makeText(getApplicationContext(), "수정된 이름은 " + modeItems.lastName + modeItems.firstName, Toast.LENGTH_LONG).show();


                }

            }
        }

        adapter.notifyDataSetChanged(); //ListView를 뿌리는 adapter에게 데이터가 변경됨을 알려줌
        }


}

