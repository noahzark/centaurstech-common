package com.centaurstech.domain.eventtrack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Event track proxy
 *
 * @author Tongcheng.Tang
 * updated by Fangzhou.Long
 */
public class EventTrackProxy {

    private static final int DEFAULT_SIZE_STORAGE = 50;

    EventTrackSender eventTrackSender;
    String origin;
    int reportSize;

    private HashSet<EventTrack> eventTrackSet = new HashSet<>(DEFAULT_SIZE_STORAGE);
    private ExecutorService cachePool = Executors.newCachedThreadPool();

    public EventTrackProxy(EventTrackSender eventTrackSender, String origin) {
        this(eventTrackSender, origin, DEFAULT_SIZE_STORAGE);
    }

    public EventTrackProxy(EventTrackSender eventTrackSender, String origin, int reportSize) {
        this.eventTrackSender = eventTrackSender;
        this.origin = origin;
        this.reportSize = reportSize;
    }

    public String addUserBehaviorEvent(String uid, EventTrackItem.ActionType actionType, EventTrackItem.PermissionType permissionType) {
        Map<String, String> fields = new HashMap<>();
        fields.put(EventTrackItem.FieldKey.ACTION.value, actionType.toString());
        if (actionType == EventTrackItem.ActionType.IMPOWER) {
            if (permissionType == null) {
                return "permission missing";
            }
        }
        if (permissionType != null)
            fields.put(EventTrackItem.FieldKey.PERMISSION.value, permissionType.toString());
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.USER_BEHAVIOR, fields);
        eventTrack.setOrigin(origin);
        this.submitTask(eventTrack);
        return "SUCCESS";
    }

    public String addBotActivationEvent(String uid, EventTrackItem.Platform platform, EventTrackItem.BotName botName) {
        Map<String, String> fields = new HashMap<>();
        if (botName != null) {
            fields.put(EventTrackItem.FieldKey.BOT.value, botName.toString());
        }
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.BOT_ACTIVATION, fields);
        eventTrack.setPlatform(platform == null ? null : platform.toString());
        eventTrack.setOrigin(origin);
        this.submitTask(eventTrack);
        return "SUCCESS";
    }

    public String addBotExceptionEvent(String uid, EventTrackItem.Platform platform, EventTrackItem.BotException exceptionType) {
        Map<String, String> fields = new HashMap<>();
        if (exceptionType == null) {
            return "exception missing";
        } else {
            fields.put(EventTrackItem.FieldKey.EXCEPTION.value, exceptionType.getExceptionDescription());
        }
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.BOT_EXCEPTION, fields);
        eventTrack.setPlatform(platform == null ? null : platform.toString());
        eventTrack.setOrigin(origin);
        this.submitTask(eventTrack);
        return "SUCCESS";
    }

    public String addBotSessionEvent(String uid, EventTrackItem.Platform platform, EventTrackItem.Field field, EventTrackItem.BotName botName, Integer chat) {
        Map<String, String> fields = new HashMap<>();
        fields.put(EventTrackItem.FieldKey.FIELD.value, field.toString());
        if (field == EventTrackItem.Field.ORDER_QUERY || field == EventTrackItem.Field.ORDER_RECOMMEND ||
                field == EventTrackItem.Field.ORDER_RECOMMEND_FIRST || field == EventTrackItem.Field.ORDER_COMMIT ||
                field == EventTrackItem.Field.ORDER_CHECKOUT || field == EventTrackItem.Field.ORDER_REFUND ||
                field == EventTrackItem.Field.ORDER_TICKET) {
            //必须带bot name
            if (botName == null) {
                return "bot name missing";
            } else {
                fields.put(EventTrackItem.FieldKey.BOT.value, botName.toString());
            }
        }
        if (field == EventTrackItem.Field.SESSION_FINISHED) {
            //必须带chat number
            if (chat == null) {
                return "chat number missing";
            } else {
                fields.put(EventTrackItem.FieldKey.CHAT.value, chat.toString());
            }
        }
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.BOT_SESSION, fields);
        eventTrack.setPlatform(platform == null ? null : platform.toString());
        eventTrack.setOrigin(origin);
        this.submitTask(eventTrack);
        return "SUCCESS";
    }

    private EventTrack generateEventTrack(String uid, EventTrackItem.ReportType reportType, Map<String, String> fields) {
        EventTrack eventTrack = new EventTrack();
        eventTrack.setUid(uid);
        EventTrackItem eventTrackItem = new EventTrackItem(reportType.toString());
        eventTrackItem.setFields(fields);
        eventTrack.addDataItem(eventTrackItem);
        return eventTrack;
    }

    private void submitTask(EventTrack eventTrack) {
        Runnable runnable = () -> checkTrackSet(eventTrack);
        cachePool.execute(runnable);
    }

    private synchronized void checkTrackSet(EventTrack eventTrack) {
        eventTrackSet.add(eventTrack);
        if (eventTrackSet.size() >= reportSize) {
            eventTrackSender.sendEventsToServer(eventTrackSet);
            eventTrackSet.clear();
        }
    }

    public interface EventTrackSender {
        void sendEventsToServer(Set<EventTrack> eventTracks);
    }

}
