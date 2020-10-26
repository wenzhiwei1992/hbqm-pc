package com.smartpc.chiyun.config.dingding.message;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public  class MessageDD {
	public String msgtype ;
	@JsonInclude
	private TextMessage text;
	@JsonInclude
	private ImageMessage image;
	@JsonInclude
	private OAMessage oa;
	@JsonInclude
	private LinkMessage link;

	public MessageDD() {
	}

	public MessageDD(String msgtype) {
		this.msgtype = msgtype;
	}

}
