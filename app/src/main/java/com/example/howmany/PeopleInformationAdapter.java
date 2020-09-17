package com.example.howmany;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PeopleInformationAdapter extends RecyclerView.Adapter<PeopleInformationAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_num;
        TextView textView_nick;
        TextView textView_time;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView_num = itemView.findViewById(R.id.person_num) ;
            textView_nick = itemView.findViewById(R.id.person_nick) ;
            textView_time = itemView.findViewById(R.id.person_time) ;
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    PeopleInformationAdapter(ArrayList<String> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public PeopleInformationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.information_recyclerview_item, parent, false) ;
        PeopleInformationAdapter.ViewHolder vh = new PeopleInformationAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(PeopleInformationAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView_num.setText(text) ;
        holder.textView_nick.setText(text) ;
        holder.textView_time.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}