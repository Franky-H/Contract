package com.example.bobol.practice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bobol on 2018-01-29.
 */

public class DbOpenHelper {
    private static final String DATABASE_NAME = "ContactLIst.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase db;
    private Context context;
    private DatabaseHelper DBHelper;


    public DbOpenHelper(Context context) { //Open헬퍼를 이용하는  액티비티 자신을 나타냄
        this.context = context;
    } //OpenHelper정의

    private class DatabaseHelper extends SQLiteOpenHelper { //SQLiteOpenHelper 클래스를 상속 받아서 DB생성 및 업그레이드 연결,추가,삭제 등등 작업하는 클래스

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) { // DB 생성 메소드 , 표준 커서를 이용할시 3번째 변수는 null이 된다.
            super(context, name, factory, version);
        }


        @Override //DB가 open 될때 생성된 DB가 없을때 한번만 호출됨(DB생성) //DB있는지 먼저 체크하는단 만들고...예외처리 DB가 있을때 drop함.
        public void onCreate(SQLiteDatabase db) {

            try{
                db.execSQL("DROP TABLE IF EXISTS ContactList");
            }catch (Exception ex){
                Log.e("hong", "hong:", ex);}


            // ContactList라는 TABLE 을 생성한 후 자동적으로 1씩 추가되는 _id 값과, 각각의 TEXT형식의 각각의 칼럼을 추가
            db.execSQL("Create table ContactList(_id INTEGER PRIMARY KEY AUTOINCREMENT,lastName TEXT,firstName TEXT,mobile TEXT,emailAdd TEXT,domain TEXT,address TEXT);");
            //DB안에 기본적으로 밀어넣어둔 주소록
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('가','부장','01072222531','gaSH','@google.com','강원도 강릉시 명주동')");//1
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('루','비석','01072222532','naSH','@hanmail.com','인천광역시 중구 중앙동2가')");//2
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('자','전거','01072222533','daSH','@nate.com','서울특별시 성동구 하왕십리동')");//3
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('고','길동','01072222534','saSH','@google.com','전라북도 전주시 완산구')");//4
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('상','상력','01072222535','maSH','@entermate.com','전라남도 순천시 옥천동')");//5
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('형','돈아','01072222536','saSH','@naver.com','경상남도 진주시 주약동')");//6
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('탁','구채','01072222537','AaSH','@google.com','경상남도 창원시 의창구 소답동')");//7
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('김','태희','01072222538','jaSH','@naver.com','경기도 안산시 단원구')");//8
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('강','건너','01072222539','chaSH','@nate.com','경기도 안양시 만안구')");//9
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('홍','성현','01172222531','kaSH','@hanmail.com','경기도 용인시 수지구 상현동')");//10
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('유','강남','01172222532','taSH','@entermate.com','경기도 수원시 권선구' )");//11
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('최','민현','01172222533','paSH','@google.com','경기도 용인시 기흥구')");//12
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) Values('장','현수','01172222534','haSH','@hanmail.com','경상북도 포항시 북구')");//13

        }

        @Override // version 안바뀜 현재단계 사용 x
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

        public void insert(String lastName, String firstName, String mobile, String emailAdd,String domain,String address)// 추가받아 넣는 메소드
        {
            db = DBHelper.getWritableDatabase(); //  읽기, 쓰기 가능하게 db열기
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd,domain,address) VALUES ('" + lastName + "','" + firstName + "','" + mobile + "','" + emailAdd + "','"+domain+"','"+address+"');"); //DB에 입력한 값들로 항 추가
            db.close();
        }

        public void updateList(String lastName, String firstName, String mobile, String emailAdd,int _id,String domain,String address) { //update (수정) 메소드
            db = DBHelper.getWritableDatabase();
            db.execSQL("UPDATE ContactList SET  lastName = '"+lastName+"', firstName='" + firstName + "', mobile = '" + mobile + "', emailAdd = '" + emailAdd + "',domain = '"+domain+"' ,address = '"+address+"' WHERE _id = '"+_id+"';");// 입력한 이름과 일치하는 정보 업데이트

        }

        public void deleteList(long _id) { // 삭제 메소드
            db = DBHelper.getWritableDatabase();
            db.execSQL("DELETE FROM ContactList WHERE _id= '" + _id + "';"); // 클릭된 리스트의 id를 받아 삭제하는 메소드
            db.close();
        }
         public DbOpenHelper open() throws android.database.SQLException // Header에서 DB를 open할 메소드
         {
        DBHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = DBHelper.getWritableDatabase();// DB읽기, DB가 없다면 onCreate가 호출됨. Version이 바뀌었다면 onUpgrade 호출
        return this;
          }
        public void close() {
        db.close();
    } //db 연결을 해제하는 메소드


}


// DB에서 초기에 저장되어있는 List
     /*       db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('가','성현','01072222531','gaSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('나','성현','01072222532','naSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('다','성현','01072222533','daSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('라','성현','01072222534','saSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('마','성현','01072222535','maSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('사','성현','01072222536','saSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('아','성현','01072222537','AaSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('자','성현','01072222538','jaSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('차','성현','01072222539','chaSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('카','성현','01172222531','kaSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('타','성현','01172222532','taSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('파','성현','01172222533','paSH')");
            db.execSQL("INSERT INTO ContactList(lastName,firstName,mobile,emailAdd) Values('하','성현','01172222534','haSH')");*/