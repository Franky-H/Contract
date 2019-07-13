package com.example.bobol.practice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class  FriendsAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public ArrayList<FriendsItems> items;


    public FriendsAdapter(ArrayList<FriendsItems> items, Context context) // List를 받아 오기 위한 어댑터 정의
    {

        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflater 정의
    }


    @Override//인자로 받은 아이템의 위치의 데이터가 화면에 표시될 뷰를 반환
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position; //아이템의 위치정보정의
        final Context context = parent.getContext();
        if (convertView == null) {//화면에 리스트를 띄우기 위해 convertView가 null인 상태에서 inflate 된다. 한번 inflate된 화면의 리스트는 다시 되지않음 convertView는 리스트가 생성되어야 할때.ex)스크롤을 내릴때 null값을 받음
            // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//inflate(View에 실제 객체를 띄움(?)기 위해 inflater를 정의 한다.
            convertView = inflater.inflate(R.layout.list_view_item, parent, false);

        }

        //화면에 표시될 View
        TextView LastNameTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView MoBileTextView = (TextView) convertView.findViewById(R.id.textView3);
        FriendsItems listViewItem = items.get(pos);//

        //속성값 변경
        LastNameTextView.setText(listViewItem.full_name());
        MoBileTextView.setText(listViewItem.getMobile());

        return convertView;
    }



    @Override//전체 아이템의 갯수를 리턴하는 메소드
    public int getCount() {
        return items.size();
    }

    public void addItem(FriendsItems item) {
        items.add(item);
    }
    //리스트 추가 메소드


    @Override// 해당 postion의 아이템을 불러오는 메소드
    public FriendsItems getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) // getItem에서 불러오는 item에 ID를 반환 메소드
    {
        return position;
    }

}