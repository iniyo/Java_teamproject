package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//리사이클러뷰에 보여줄 아이템들 함수 정의
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<Board> board_lists;
    private Context context;

    public MainAdapter(ArrayList<Board> list, Context context) {
        board_lists = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_board, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    // 리스트 아이템
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position); //커스텀 리스트 뷰의 각각의 리스트를 의미
        Board item = board_lists.get(position); //보드 리스트에서 아이템 가져오기
        item.setShowpeople(item.getCount()+"/"+item.getPeople_number()); // 표시
        holder.board_name.setText(item.getBoard_name()); // 장소
        holder.people_number.setText(item.getShowpeople()); // 총인원
        holder.time.setText(item.getTime()); // 시키는 시간
        holder.title_info.setText(item.getTitle_info()); // 제목
        // 리스트뷰 클릭시 화면전환
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;//인텐트 선언
                intent = new Intent(context, ShowPlaceActivity.class); //look_memo.class부분에 원하는 화면 연결
                intent.putExtra("place_name", item.getBoard_name()); //장소이름
                intent.putExtra("title_info", item.getTitle_info()); //제목
                intent.putExtra("people_number", item.getPeople_number()); //총인원
                intent.putExtra("delevery_info", item.getDelevery_info()); //상세정보
                intent.putExtra("time", item.getTime()); //등록시간
                intent.putExtra("count",item.getCount()); //현재 인원수
                if (item.getSolo() != null){
                    intent.putExtra("solo", item.getSolo()); //혼자
                }else if(item.getTogether() != null){
                    intent.putExtra("together", item.getTogether()); //같이
                }
                intent.putExtra("id", item.getId());//id

                context.startActivity(intent); //액티비티 열기
            }
        });
    }

    // board 리스트 사이즈
    @Override
    public int getItemCount() {
        return board_lists.size();
    }

    // 뷰홀더에 보여줄 아이템들
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView board_name; //장소
        TextView people_number; //총인원
        TextView time; //시간
        TextView title_info; //제목

        ViewHolder(View itemView) {
            super(itemView);
            board_name = itemView.findViewById(R.id.item_board_name);
            people_number = itemView.findViewById(R.id.item_people_number);
            time = itemView.findViewById(R.id.item_time);
            title_info = itemView.findViewById(R.id.item_title_info);
        }
    }
}