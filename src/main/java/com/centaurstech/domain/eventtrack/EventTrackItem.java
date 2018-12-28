package com.centaurstech.domain.eventtrack;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Event track items
 *
 * @author Tongcheng.Tang
 */
public class EventTrackItem {
    private String reportType;
    private String timestamp;
    private Map<String, String> fields;

    public EventTrackItem(String reportType, Map<String, String> fields) {
        this.reportType = reportType;
        this.timestamp = new Date().getTime() + "";
        this.fields = fields;
    }

    public EventTrackItem(String reportType) {
        this.reportType = reportType;
        this.timestamp = new Date().getTime() + "";
        this.fields = new HashMap<>();
    }

    public EventTrackItem() {
        this.fields = new HashMap<>();
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public void putAttribute(String key, String value) {
        this.fields.put(key, value);
    }

    /**
     * Restricted, tell Hengry before you change this part.
     */
    public enum ReportType{
        USER_BEHAVIOR("上报⽤户⾏为数据"),
        BOT_ACTIVATION("上报BOT唤醒数据"),
        BOT_EXCEPTION("上报BOT异常数据"),
        BOT_SESSION("上报BOT会话数据");
        private String description;

        ReportType(String description) {
            this.description = description;
        }

    }

    public enum Platform implements Describable {
        SMART_LIFE_APP("智慧生活App"),
        XIAO_MI("小爱音箱");
        private String description;

        Platform(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public enum ActionType implements Describable {
        INSTALL("⽤户安装\"你好⼩悟\""),
        STARTUP("用户启动\"你好小悟\""),
        REGISTER("用户注册"),
        LOGIN("用户登录"),
        UPVOTE("点赞"),
        DOWNVOTE("点踩"),
        IMPOWER("用户授权");
        private String description;

        ActionType(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public enum PermissionType implements Describable {
        LOCATION("位置权限"),
        MICROPHONE("⻨克⻛权限"),
        CONTACTS("通讯录权限"),
        PHONE("通话权限"),
        CAMERA("相机权限"),
        GALLERY("相册权限");
        private String description;

        PermissionType(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    private enum Field implements Describable {
        NAVIGATION_ARRIVED("导航到达⽬的地"),
        USE_NAVIGATION_FOR_BIKING("使⽤导航骑⾏"),
        USE_NAVIGATION_FOR_DRIVING("使⽤导航驾驶"),
        USE_NAVIGATION_FOR_WALK("使⽤导航步⾏"),
        USE_NAVIGATION_FOR_PUB_TRANSPORT("使⽤导航乘坐公共交通⼯具"),
        ORDER_QUERY("订单请求"),
        ORDER_RECOMMEND("订单推荐"),
        ORDER_RECOMMEND_FIRST("⾸次订单推荐"),
        ORDER_COMMIT("订单提交"),
        ORDER_CHECKOUT("订单成交"),
        ORDER_TICKET("订单出票"),
        ORDER_REFUND("订单退款"),//该行需要bot
        SESSION_FINISHED("对话完成"),
        FLIGHT_CHANGE_SPACE("机票换舱位"),
        FLIGHT_CHANGE_TIME("机票换时间"),
        FLIGHT_CHANGE_PRICE("机票换价格"),
        FLIGHT_CHANGE_AIRLINE("机票换航空公司"),
        TRAIN_CHANGE_SEAT("⽕⻋票换座位"),
        TRAIN_CHANGE_TIME("⽕⻋票换时间"),
        TRAIN_CHANGE_PRICE("⽕⻋票换价格"),
        TRAIN_CHANGE_MODEL("⽕⻋票换⻋类型"),
        HOTEL_CHANGE_STAR("酒店换星级"),
        HOTEL_CHANGE_DATE("酒店换⽇期"),
        HOTEL_CHANGE_PRICE("酒店换价格"),
        HOTEL_CHANGE_MODEL("酒店换房型"),
        WEATHER_QUERY_DATE("天⽓查询⽇期"),
        WEATHER_QUERY_SITE("天⽓查询地点"),
        WEATHER_MULTI_DAY("天⽓查询多天"),
        NAVIGATION_QUERY_ROUND("导航查询周边"),
        NAVIGATION_QUERY_SITE("导航查询地点"),
        NAVIGATION_START("开始导航"),
        NAVIGATION_END("结束导航"),
        NAVIGATION_SWITCH("导航切换"),
        ALL_TAXIS_CHANGE_CAR_TYPE("打车更换车型"),
        ALL_TAXIS_CHANGE_CAR_PRICE("打车更换价格"),
        ALL_TAXIS_CHANGE_CAR_PLATFORM("打车更换车平台"),
        ALL_TAXIS_FAST_CAR("更快接驾");
        private String description;

        Field(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public enum BotName implements Describable {
        TRAIN("火车"),
        FLIGHT("机票"),
        HOTEL("酒店"),
        NAVIGATION("导航"),
        WEATHER("天气"),
        ALLTAXIS("打车"),
        FREECHAT("闲聊"),
        UNKNOWN("未知");
        private String description;

        BotName(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    /**
     * Restricted, tell Hengry before you change this part.
     */
    public enum FieldKey {
        ACTION("action"),
        PERMISSION("permission"),
        BOT("bot"),
        EXCEPTION("exception"),
        FIELD("field"),
        CHAT("chat");
        public String value;

        FieldKey(String value) {
            this.value = value;
        }
    }

    public interface BotException {
        String getExceptionDescription();
    }

    public interface Describable {
        String getDescription();
    }

}
