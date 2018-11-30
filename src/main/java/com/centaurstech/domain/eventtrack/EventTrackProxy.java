package com.centaurstech.domain.eventtrack;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Event track proxy
 *
 * @author initial Tongcheng.Tang
 * @author updated by Fangzhou.Long
 * LINKED_BLOCKING_QUEUE BuffMode advised by Dongke.Zhou
 */
public class EventTrackProxy {

    public enum BuffMode {
        @Deprecated
        HASH_SET,
        LINKED_BLOCKING_QUEUE
    }

    public static final int DEFAULT_STORAGE_SIZE = 10000;

    EventTrackSender eventTrackSender;
    String origin;
    int reportSize;

    BuffMode buffMode;

    Date lastFlush;

    private HashSet<EventTrack> eventTrackSet;
    private LinkedBlockingQueue<EventTrack> eventTrackQueue;
    private ExecutorService cachePool = Executors.newCachedThreadPool();

    public EventTrackProxy(EventTrackSender eventTrackSender, String origin) {
        this(eventTrackSender, origin, DEFAULT_STORAGE_SIZE);
    }

    public EventTrackProxy(EventTrackSender eventTrackSender, String origin, int reportSize) {
        this(eventTrackSender, origin, reportSize, BuffMode.LINKED_BLOCKING_QUEUE);
    }

    public EventTrackProxy(EventTrackSender eventTrackSender, String origin, int reportSize, BuffMode buffMode) {
        this.eventTrackSender = eventTrackSender;
        this.origin = origin;
        this.reportSize = reportSize;

        this.buffMode = buffMode;
        if (buffMode == BuffMode.HASH_SET) {
            eventTrackSet = new HashSet<>(DEFAULT_STORAGE_SIZE);
        } else {
            eventTrackQueue = new LinkedBlockingQueue<>(DEFAULT_STORAGE_SIZE);
        }
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
        if (buffMode == BuffMode.LINKED_BLOCKING_QUEUE) {
            checkTrackQueue(reportSize);
            eventTrackQueue.add(eventTrack);
        } else {
            synchronized (eventTrackSet) {
                checkTrackSet(reportSize);
                eventTrackSet.add(eventTrack);
            }
        }
    }

    public void forceFlush(int needReportSize) {
        if (buffMode == BuffMode.LINKED_BLOCKING_QUEUE) {
            checkTrackQueue(needReportSize);
        } else {
            checkTrackSet(needReportSize);
        }
    }

    private void checkTrackQueue(int needReportSize) {
        if (eventTrackQueue.size() > needReportSize) {
            HashSet<EventTrack> eventSendSet = new HashSet<>(needReportSize * 2);
            while(eventTrackQueue.size() > needReportSize / 2) {
                eventSendSet.add(eventTrackQueue.poll());
            }
            Runnable runnable = () -> eventTrackSender.sendEventsToServer(eventSendSet);
            cachePool.execute(runnable);
            lastFlush = Calendar.getInstance().getTime();
        }
    }

    private HashSet<EventTrack> checkTrackSet(int needReportSize) {
        if (eventTrackSet.size() >= needReportSize) {
            HashSet<EventTrack> temp = new HashSet<>(eventTrackSet);
            Runnable runnable = () -> eventTrackSender.sendEventsToServer(temp);
            cachePool.execute(runnable);
            eventTrackSet.clear();
            lastFlush = Calendar.getInstance().getTime();
        }
        return eventTrackSet;
    }

    public interface EventTrackSender {
        void sendEventsToServer(Set<EventTrack> eventTracks);
    }

    public BuffMode getBuffMode() {
        return buffMode;
    }

    public Date getLastFlush() {
        return lastFlush;
    }

}
