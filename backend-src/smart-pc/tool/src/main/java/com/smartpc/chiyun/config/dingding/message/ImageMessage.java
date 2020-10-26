package com.smartpc.chiyun.config.dingding.message;


public class ImageMessage extends MessageDD {

	public String media_id;

	public ImageMessage(String mediaId) {
		super();
		media_id = mediaId;
	}

	public String type() {
		return "image";
	}
}
