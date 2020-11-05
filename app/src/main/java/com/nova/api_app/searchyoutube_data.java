package com.nova.api_app;

public class searchyoutube_data {
	private static final String TAG= "log";

	String Thumbnail;
	String Title;
	String Video_id;

	public String getThumbnail() {
		return Thumbnail;
	}
	public String getTitle() {
		return Title;
	}
	public String getVideo_id() {
		return Video_id;
	}

	public searchyoutube_data(String Thumbnail, String Title, String Video_id){
		this.Thumbnail = Thumbnail;
		this.Title = Title;
		this.Video_id = Video_id;
	}
}