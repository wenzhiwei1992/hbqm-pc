package com.smartpc.chiyun.voutils;

import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.vo.ResultVO;

/**
 * @Author yue
 * @create 2020/6/11 12:25 上午
 */
public class ResultVOUtils {
    public static ResultVO success() {
        ResultVO resultVO = new ResultVO();

        resultVO.setStatus(ResultEnum.SUCCESS.getMsg());
        resultVO.setMsg(ResultEnum.SUCCESS.getMsg());
        return resultVO;
    }

    public static ResultVO success(Object data) {
        ResultVO resultVO = new ResultVO();

        resultVO.setStatus(ResultEnum.SUCCESS.getMsg());
        resultVO.setMsg(ResultEnum.SUCCESS.getMsg());
        resultVO.setEntity(data);
        return resultVO;
    }

    public static ResultVO error(String status, String msg, Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus(status);
        resultVO.setMsg(msg);
        resultVO.setEntity(data);
        return resultVO;
    }

    public static ResultVO error(String status, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus(status);
        resultVO.setMsg(msg);
        return resultVO;
    }


    public static ResultVO error(ResultEnum resultEnum) {
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus(resultEnum.getStatus());
        resultVO.setMsg(resultEnum.getMsg());
        return resultVO;
    }


    public static ResultVO success(ResultEnum resultEnum) {
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus(resultEnum.getStatus());
        resultVO.setMsg(resultEnum.getMsg());
        return resultVO;
    }

    public static ResultVO success(String message) {
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus("success");
        resultVO.setMsg(message);
        resultVO.setEntity(message);
        return resultVO;
    }
}
