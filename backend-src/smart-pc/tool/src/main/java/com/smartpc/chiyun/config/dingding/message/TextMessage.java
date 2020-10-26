package com.smartpc.chiyun.config.dingding.message;

public class TextMessage extends MessageDD {

	public String content;

	public TextMessage(String content) {
		super();
		this.content = content;
	}
	public TextMessage() {
	}

	public String type() {
		return "text";
	}
}
