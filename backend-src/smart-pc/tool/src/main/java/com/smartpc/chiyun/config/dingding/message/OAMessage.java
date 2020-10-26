package com.smartpc.chiyun.config.dingding.message;

import java.util.List;

public class OAMessage extends MessageDD {

	public String message_url;
	public Head head;
	public Body body;


	public String type() {
		return "oa";
	}

	//content
	public static class Head {
		public String bgcolor;
	}

	public static class Body {
		public String title;
		public List<Form> form;
		public Rich rich;
		public String content;
		public String image;
		public String file_found;
		public String author;

		public static class Form {
			public String key;
			public String value;
		}

		public static class Rich {
			public String num;
			public String unit;
		}
	}
}
