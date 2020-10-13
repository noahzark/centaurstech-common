package com.centaurstech.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 新平台请求响应结果
 * @author yws
 * @date 2020/05/21
 */
public class FormResponse {
    public static class FormData<Q, G> {
        private Q vars;

        private List<G> groupVars;

        public List<G> getGroupVars() {
            if (groupVars == null) {
                this.groupVars = new ArrayList<>();
            }
            return groupVars;
        }

        public Q getVars() {
            return vars;
        }

        public void setVars(Q vars) {
            this.vars = vars;
        }

        public void setGroupVars(List<G> groupVars) {
            this.groupVars = groupVars;
        }

        public FormData() {
        }

        public FormData(Q vars, List<G> groupVars) {
            this.vars = vars;
            this.groupVars = groupVars;
        }
    }

    private Integer code;

    private String msg;

    private FormData data;

    public FormResponse() {
    }

    public FormResponse(Integer code, String msg, FormData data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FormData getData() {
        return data;
    }

    public void setData(FormData data) {
        this.data = data;
    }
}
