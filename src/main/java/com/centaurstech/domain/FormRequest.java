package com.centaurstech.domain;


import java.util.ArrayList;
import java.util.List;

/**
 * 新平台表单提交请求
 * @author yws
 * @date 2020/05/21
 */
public class FormRequest<Q, G> {

    private String chatKey;

    private String uid;

    private String channelId;

    private String queryText;

    private String botAccount;

    private Q vars;

    private List<G> groupVars;

    public List<G> getGroupVars() {
        if (this.groupVars == null) {
            this.groupVars = new ArrayList<>();
        }
        return groupVars;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getBotAccount() {
        return botAccount;
    }

    public void setBotAccount(String botAccount) {
        this.botAccount = botAccount;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "FormRequest{" +
                "chatKey='" + chatKey + '\'' +
                ", queryText='" + queryText + '\'' +
                ", botAccount='" + botAccount + '\'' +
                ", vars=" + vars +
                ", groupVars=" + groupVars +
                '}';
    }

    public FormRequest() {
    }
}
