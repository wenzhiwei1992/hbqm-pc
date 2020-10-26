package com.smartpc.chiyun.config.dingding.message;

import lombok.Data;

@Data
public class MessageData {

    public Long agent_id;

    public String dept_id_list;

    public String userid_list; //多个参数请用","分隔 这里是钉钉的userid

    public Boolean to_all_user = false;

    public String task_id;

    public MessageDD msg;
    //public Object msg;
}
