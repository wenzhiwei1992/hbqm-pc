package com.smartpc.chiyun.config.dingding.message;

public class LinkMessage extends MessageDD {

	public String messageUrl;
    public String picUrl;
    public String title;
    public String text;

	public LinkMessage(String messageUrl, String picUrl, String title, String text) {
		super();
		this.messageUrl = messageUrl;
		this.picUrl = picUrl;
		this.title = title;
		this.text = text;
	}

	public String type() {
		return "link";
	}
}
