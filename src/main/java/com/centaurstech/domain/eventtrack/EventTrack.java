package com.centaurstech.domain.eventtrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Event track class, used to send to server
 *
 * @author Tongcheng.Tang
 */
public class EventTrack {
    private String uid;
    private Map<String, String> did;
    private String platform;
    private String origin;
    private List<EventTrackItem> data;

    public EventTrack() {
        this.data = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, String> getDid() {
        return did;
    }

    public void setDid(Map<String, String> did) {
        this.did = did;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<EventTrackItem> getData() {
        return data;
    }

    public void setData(List<EventTrackItem> data) {
        this.data = data;
    }

    public void addDataItem(EventTrackItem item) {
        this.data.add(item);
    }
}
