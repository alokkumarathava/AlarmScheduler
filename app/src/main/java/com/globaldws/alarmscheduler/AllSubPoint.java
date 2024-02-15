package com.globaldws.alarmscheduler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AllSubPoint implements Serializable {

    @SerializedName("SubPointid")
    @Expose
    public long subPointid;
    @SerializedName("ClientNo")
    @Expose
    public long clientNo;
    @SerializedName("scheduleId")
    @Expose
    public long scheduleId;
    @SerializedName("pointid")
    @Expose
    public long pointid;
    @SerializedName("isSpray")
    @Expose
    public long isSpray;
    @SerializedName("SpryDuration")
    @Expose
    public long spryDuration;
    @SerializedName("isLight")
    @Expose
    public long isLight;
    @SerializedName("LightDuration")
    @Expose
    public long lightDuration;
    @SerializedName("StartDateTime")
    @Expose
    public Object startDateTime;
    @SerializedName("EndDateTime")
    @Expose
    public Object endDateTime;
    @SerializedName("status")
    @Expose
    public Object status;
    @SerializedName("insertDate")
    @Expose
    public Object insertDate;
    @SerializedName("DeleteDateTime")
    @Expose
    public Object deleteDateTime;
    @SerializedName("isDelete")
    @Expose
    public Object isDelete;
    @SerializedName("timeStart")
    @Expose
    public Object timeStart;
    @SerializedName("isDefult")
    @Expose
    public Object isDefult;

    public AllSubPoint() {
    }

    public AllSubPoint(long subPointid, long clientNo, long scheduleId, long pointid, long isSpray, long spryDuration, long isLight, long lightDuration, Object startDateTime, Object endDateTime, Object status, Object insertDate, Object deleteDateTime, Object isDelete, Object timeStart, Object isDefult) {
        this.subPointid = subPointid;
        this.clientNo = clientNo;
        this.scheduleId = scheduleId;
        this.pointid = pointid;
        this.isSpray = isSpray;
        this.spryDuration = spryDuration;
        this.isLight = isLight;
        this.lightDuration = lightDuration;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.insertDate = insertDate;
        this.deleteDateTime = deleteDateTime;
        this.isDelete = isDelete;
        this.timeStart = timeStart;
        this.isDefult = isDefult;
    }

    public long getSubPointid() {
        return subPointid;
    }

    public void setSubPointid(long subPointid) {
        this.subPointid = subPointid;
    }

    public long getClientNo() {
        return clientNo;
    }

    public void setClientNo(long clientNo) {
        this.clientNo = clientNo;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getPointid() {
        return pointid;
    }

    public void setPointid(long pointid) {
        this.pointid = pointid;
    }

    public long getIsSpray() {
        return isSpray;
    }

    public void setIsSpray(long isSpray) {
        this.isSpray = isSpray;
    }

    public long getSpryDuration() {
        return spryDuration;
    }

    public void setSpryDuration(long spryDuration) {
        this.spryDuration = spryDuration;
    }

    public long getIsLight() {
        return isLight;
    }

    public void setIsLight(long isLight) {
        this.isLight = isLight;
    }

    public long getLightDuration() {
        return lightDuration;
    }

    public void setLightDuration(long lightDuration) {
        this.lightDuration = lightDuration;
    }

    public Object getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Object startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Object getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Object endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Object insertDate) {
        this.insertDate = insertDate;
    }

    public Object getDeleteDateTime() {
        return deleteDateTime;
    }

    public void setDeleteDateTime(Object deleteDateTime) {
        this.deleteDateTime = deleteDateTime;
    }

    public Object getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Object isDelete) {
        this.isDelete = isDelete;
    }

    public Object getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Object timeStart) {
        this.timeStart = timeStart;
    }

    public Object getIsDefult() {
        return isDefult;
    }

    public void setIsDefult(Object isDefult) {
        this.isDefult = isDefult;
    }
}
