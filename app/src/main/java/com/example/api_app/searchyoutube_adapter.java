package com.example.api_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class searchyoutube_adapter extends RecyclerView.Adapter<searchyoutube_adapter.ViewHolder> {
	private static final String TAG= "log";

	Context context;
	LayoutInflater inflacter;
	Glide glide;
	int layout;
	//adapter에 들어갈 list
	public static ArrayList<searchyoutube_data> info = new ArrayList<>();

	//MainActivity 에서 context, info 받아오기
	public searchyoutube_adapter(Context context, int layout){
		this.context = context;
		inflacter = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		//recycler_view_item.xml을 inflate 시킨다.
		View view = LayoutInflater.from(context).inflate(R.layout.activity_searchyoutube_item, parent, false);
		
		return new ViewHolder(view);
	}
	
	
	/*onbindviewholder 란 ListView / RecyclerView 는 inflate를 최소화 하기 위해서 뷰를 재활용 하는데,
	이 때 각 뷰의 내용을 업데이트 하기 위해 findViewById 를 매번 호출 해야합니다.
	이로 인해 성능저하가 일어남에 따라 ItemView의 각 요소를 바로 엑세스 할 수 있도록 저장해두고 사용하기 위한 객체입니다.*/
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
		//list 에 아이템 하나하나 보여주는 메소드 입니다.
		
		searchyoutube_data item = info.get(position);
		holder.tv_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(context,position,Toast.LENGTH_LONG).show();
			}
		});

		//Glide.with(holder.itemView).load("https://i.ytimg.com/vi/I7Xx1J5wBaY/default.jpg").into(holder.iv_thumbnails);

		glide.with(holder.itemView.getContext()).load(item.getThumbnail()).into(holder.iv_thumbnails);
		holder.tv_title.setText(item.getTitle());
		//holder.tv_address_name.setText(item.getAddress_name());
		//holder.tv_road_address_name.setText(item.getRoad_address_name());
		//holder.imageView.getResources().getDrawable(item.getImageResource());
	}
	
	//리스트의 아이템 갯수
	@Override
	public int getItemCount() {
		return info.size();
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {
		ImageView iv_thumbnails;
		TextView tv_title;
		
		ViewHolder(final View view) {
			super(view);

			iv_thumbnails =  view.findViewById(R.id.iv_thumbnails);
			tv_title =  view.findViewById(R.id.tv_title);
			
			//아이템을 클릭 했을 때
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					int pos = getAdapterPosition() ;
					System.out.println("pos"+pos);
					
					if (pos != RecyclerView.NO_POSITION) {

						searchyoutube_data test;
						test = info.get(pos);
						Log.v(TAG, String.valueOf(test));

						String video_id = test.getVideo_id() ;

						Log.i("video_id","video_id : "+ video_id);

						Intent intent = new Intent(context, playyoutube_activity.class);
						intent.putExtra("video_id", video_id);

						int from = pos;
						String to = Integer.toString(from);

						intent.putExtra("position", to);

						Log.e(TAG, "position"+to);
						context.startActivity(intent) ;



					}
				}
			});
		}
	}
	
	public void addInfo(searchyoutube_data data){
		info.add(data);
	}
	
	public void remove(int pos) {
		info.remove(pos);
	}
	
	public void modify(int pos, searchyoutube_data data){
		info.set(pos, data);
	}
}