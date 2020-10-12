package com.centaurstech.domain.eventtrack;

import java.util.*;


/**
 * Event track proxy
 *
 * @author initial Tongcheng.Tang
 * @author updated by Fangzhou.Long
 * @author updated by Weisen.Yan on 21 August 2019
 * LINKED_BLOCKING_QUEUE BuffMode advised by Dongke.Zhou
 */
public class EventTrackProxy {

    public enum BuffMode {
        @Deprecated
        HASH_SET,
        LINKED_BLOCKING_QUEUE
    }

    public static final int DEFAULT_STORAGE_SIZE = 10000;

    EventTrackHandler eventTrackHandler;

    String origin;

    BuffMode buffMode;

    public EventTrackProxy(EventTrackSender eventTrackSender, String origin) {
        this(eventTrackSender, origin, DEFAULT_STORAGE_SIZE);
    }

    public EventTrackProxy(EventTrackSender eventTrackSender, String origin, int reportSize) {
        this(eventTrackSender, origin, reportSize, BuffMode.LINKED_BLOCKING_QUEUE);
    }

    public EventTrackProxy(EventTrackSender eventTrackSender, String origin, int reportSize, BuffMode buffMode) {
        this.origin = origin;

        this.buffMode = buffMode;
        if (buffMode == BuffMode.HASH_SET) {
            eventTrackHandler = new EventTrackSetHandler(eventTrackSender, reportSize);
        } else {
            eventTrackHandler = new EventTrackQueueHandler(eventTrackSender, reportSize);
        }
    }

    public String addUserBehaviorEvent(String uid, EventTrackItem.Describable actionType, EventTrackItem.Describable permissionType) {
        return addUserBehaviorEvent(uid, origin, actionType, permissionType);
    }

    public String addUserBehaviorEvent(String uid, String origin, EventTrackItem.Describable actionType, EventTrackItem.Describable permissionType) {
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
        this.eventTrackHandler.submitTask(eventTrack);
        return "SUCCESS";
    }

    public String addBotActivationEvent(String uid, EventTrackItem.Describable platform, EventTrackItem.Describable botName) {
        return addBotActivationEvent(uid, origin, platform, botName);
    }

    public String addBotActivationEvent(String uid, String origin, EventTrackItem.Describable platform, EventTrackItem.Describable botName) {
        Map<String, String> fields = new HashMap<>();
        if (botName != null) {
            fields.put(EventTrackItem.FieldKey.BOT.value, botName.toString());
        }
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.BOT_ACTIVATION, fields);
        eventTrack.setPlatform(platform == null ? null : platform.toString());
        eventTrack.setOrigin(origin);
        this.eventTrackHandler.submitTask(eventTrack);
        return "SUCCESS";
    }

    public String addBotExceptionEvent(String uid, EventTrackItem.Platform platform, EventTrackItem.BotException exceptionType) {
        return addBotExceptionEvent(uid, origin, platform, exceptionType);
    }

    public String addBotExceptionEvent(String uid, String origin, EventTrackItem.Platform platform, EventTrackItem.BotException exceptionType) {
        Map<String, String> fields = new HashMap<>();
        if (exceptionType == null) {
            return "exception missing";
        } else {
            fields.put(EventTrackItem.FieldKey.EXCEPTION.value, exceptionType.getExceptionDescription());
        }
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.BOT_EXCEPTION, fields);
        eventTrack.setPlatform(platform == null ? null : platform.toString());
        eventTrack.setOrigin(origin);
        this.eventTrackHandler.submitTask(eventTrack);
        return "SUCCESS";
    }

    public String addBotSessionEvent(String uid, EventTrackItem.Describable platform, EventTrackItem.Describable field,
                                     EventTrackItem.Describable botName, Integer chat) {
        return addBotSessionEvent(uid, origin, platform, field, botName, chat);
    }

    public String addBotSessionEvent(String uid, String origin, EventTrackItem.Describable platform,
                                     EventTrackItem.Describable field, EventTrackItem.Describable botName, Integer chat) {
        Map<String, String> fields = new HashMap<>();
        fields.put(EventTrackItem.FieldKey.FIELD.value, field.toString());
        if (botName != null) {
            fields.put(EventTrackItem.FieldKey.BOT.value, botName.toString());
        }
        if (chat != null) {
            fields.put(EventTrackItem.FieldKey.CHAT.value, chat.toString());
        }
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.BOT_SESSION, fields);
        eventTrack.setPlatform(platform == null ? null : platform.toString());
        eventTrack.setOrigin(origin);
        this.eventTrackHandler.submitTask(eventTrack);
        return "SUCCESS";
    }

    public String addBotSessionEvent(String uid,
                                     EventTrackItem.Describable platform,
                                     EventTrackItem.Describable field,
                                     EventTrackItem.Describable botName,
                                     Integer chat,
                                     String amount) {
        Map<String, String> fields = new HashMap<>();
        fields.put(EventTrackItem.FieldKey.FIELD.value, field.toString());
        if (botName != null) {
            fields.put(EventTrackItem.FieldKey.BOT.value, botName.toString());
        }
        if (chat != null) {
            fields.put(EventTrackItem.FieldKey.CHAT.value, chat.toString());
        }
        if (amount != null) {
            fields.put(EventTrackItem.FieldKey.NUMERIC.value, amount);
        }
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.BOT_SESSION, fields);
        eventTrack.setPlatform(platform == null ? null : platform.toString());
        eventTrack.setOrigin(origin);
        this.eventTrackHandler.submitTask(eventTrack);
        return "SUCCESS";
    }

    public String addServiceDataEvent(String uid, EventTrackItem.Describable platform,
                                      EventTrackItem.Describable field, EventTrackItem.Describable botName, String data) {
        return addServiceDataEvent(uid, origin, platform, field, botName, data);
    }

    public String addServiceDataEvent(String uid, String origin, EventTrackItem.Describable platform,
                                      EventTrackItem.Describable field, EventTrackItem.Describable botName, String data) {
        Map<String, String> fields = new HashMap<>();
        fields.put(EventTrackItem.FieldKey.FIELD.value, field.toString());
        if (botName != null) {
            fields.put(EventTrackItem.FieldKey.BOT.value, botName.toString());
        }
        if (data != null) {
            fields.put(EventTrackItem.FieldKey.DATA.value, data);
        }
        EventTrack eventTrack = generateEventTrack(uid, EventTrackItem.ReportType.SERVICE_DATA, fields);
        eventTrack.setPlatform(platform == null ? null : platform.toString());
        eventTrack.setOrigin(origin);
        this.eventTrackHandler.submitTask(eventTrack);
        return "SUCCESS";
    }

    public EventTrack generateEventTrack(
            String uid,
            EventTrackItem.Describable reportType,
            Map<String, String> fields) {
        EventTrack eventTrack = new EventTrack();
        eventTrack.setUid(uid);
        EventTrackItem eventTrackItem = new EventTrackItem(reportType.toString());
        eventTrackItem.setFields(fields);
        eventTrack.addDataItem(eventTrackItem);
        return eventTrack;
    }

    public interface EventTrackSender {
        void sendEventsToServer(Set<EventTrack> eventTracks);
    }

    public BuffMode getBuffMode() {
        return buffMode;
    }

    public EventTrackHandler getEventTrackHandler() {
        return eventTrackHandler;
    }

    public Date getLastFlush() {
        return eventTrackHandler.lastFlush;
    }

}
