package com.globaldws.alarmscheduler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ScheduleModel implements Serializable {

    @SerializedName("AllSubPoint")
    @Expose
    public List<AllSubPoint> allSubPoint;
    @SerializedName("isMobile")
    @Expose
    public Object isMobile;
    @SerializedName("IsDraft")
    @Expose
    public boolean isDraft;
    @SerializedName("DsrSpeed")
    @Expose
    public long dsrSpeed;
    @SerializedName("scheduleId")
    @Expose
    public int scheduleId;
    @SerializedName("ClientNo")
    @Expose
    public long clientNo;
    @SerializedName("scheduleName")
    @Expose
    public String scheduleName;
    @SerializedName("scheduleDate")
    @Expose
    public String scheduleDate;
    @SerializedName("scheduleDate_dt")
    @Expose
    public Object scheduleDateDt;
    @SerializedName("scheduleSatrtTime")
    @Expose
    public String scheduleSatrtTime;
    @SerializedName("scheduleDescription")
    @Expose
    public String scheduleDescription;
    @SerializedName("Status")
    @Expose
    public Object status;
    @SerializedName("doneEndDateTime")
    @Expose
    public Object doneEndDateTime;
    @SerializedName("isDelete")
    @Expose
    public Object isDelete;
    @SerializedName("insertDate")
    @Expose
    public String insertDate;
    @SerializedName("DeleteDateTime")
    @Expose
    public Object deleteDateTime;
    @SerializedName("BuildingNo")
    @Expose
    public long buildingNo;
    @SerializedName("FloorNo")
    @Expose
    public long floorNo;
    @SerializedName("RobotConnectionDevice")
    @Expose
    public String robotConnectionDevice;
    @SerializedName("RobotHardwareId")
    @Expose
    public String robotHardwareId;
    @SerializedName("RepeatType")
    @Expose
    public long repeatType;
    @SerializedName("isActive")
    @Expose
    public boolean isActive;
    @SerializedName("scheduleFinishDate")
    @Expose
    public String scheduleFinishDate;

    public ScheduleModel() {
    }

    public ScheduleModel(List<AllSubPoint> allSubPoint, Object isMobile, boolean isDraft, long dsrSpeed, int scheduleId, long clientNo, String scheduleName, String scheduleDate, Object scheduleDateDt, String scheduleSatrtTime, String scheduleDescription, Object status, Object doneEndDateTime, Object isDelete, String insertDate, Object deleteDateTime, long buildingNo, long floorNo, String robotConnectionDevice, String robotHardwareId, long repeatType, boolean isActive, String scheduleFinishDate) {
        this.allSubPoint = allSubPoint;
        this.isMobile = isMobile;
        this.isDraft = isDraft;
        this.dsrSpeed = dsrSpeed;
        this.scheduleId = scheduleId;
        this.clientNo = clientNo;
        this.scheduleName = scheduleName;
        this.scheduleDate = scheduleDate;
        this.scheduleDateDt = scheduleDateDt;
        this.scheduleSatrtTime = scheduleSatrtTime;
        this.scheduleDescription = scheduleDescription;
        this.status = status;
        this.doneEndDateTime = doneEndDateTime;
        this.isDelete = isDelete;
        this.insertDate = insertDate;
        this.deleteDateTime = deleteDateTime;
        this.buildingNo = buildingNo;
        this.floorNo = floorNo;
        this.robotConnectionDevice = robotConnectionDevice;
        this.robotHardwareId = robotHardwareId;
        this.repeatType = repeatType;
        this.isActive = isActive;
        this.scheduleFinishDate = scheduleFinishDate;
    }

    public List<AllSubPoint> getAllSubPoint() {
        return allSubPoint;
    }

    public void setAllSubPoint(List<AllSubPoint> allSubPoint) {
        this.allSubPoint = allSubPoint;
    }

    public Object getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(Object isMobile) {
        this.isMobile = isMobile;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public long getDsrSpeed() {
        return dsrSpeed;
    }

    public void setDsrSpeed(long dsrSpeed) {
        this.dsrSpeed = dsrSpeed;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getClientNo() {
        return clientNo;
    }

    public void setClientNo(long clientNo) {
        this.clientNo = clientNo;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Object getScheduleDateDt() {
        return scheduleDateDt;
    }

    public void setScheduleDateDt(Object scheduleDateDt) {
        this.scheduleDateDt = scheduleDateDt;
    }

    public String getScheduleSatrtTime() {
        return scheduleSatrtTime;
    }

    public void setScheduleSatrtTime(String scheduleSatrtTime) {
        this.scheduleSatrtTime = scheduleSatrtTime;
    }

    public String getScheduleDescription() {
        return scheduleDescription;
    }

    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getDoneEndDateTime() {
        return doneEndDateTime;
    }

    public void setDoneEndDateTime(Object doneEndDateTime) {
        this.doneEndDateTime = doneEndDateTime;
    }

    public Object getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Object isDelete) {
        this.isDelete = isDelete;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public Object getDeleteDateTime() {
        return deleteDateTime;
    }

    public void setDeleteDateTime(Object deleteDateTime) {
        this.deleteDateTime = deleteDateTime;
    }

    public long getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(long buildingNo) {
        this.buildingNo = buildingNo;
    }

    public long getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(long floorNo) {
        this.floorNo = floorNo;
    }

    public String getRobotConnectionDevice() {
        return robotConnectionDevice;
    }

    public void setRobotConnectionDevice(String robotConnectionDevice) {
        this.robotConnectionDevice = robotConnectionDevice;
    }

    public String getRobotHardwareId() {
        return robotHardwareId;
    }

    public void setRobotHardwareId(String robotHardwareId) {
        this.robotHardwareId = robotHardwareId;
    }

    public long getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(long repeatType) {
        this.repeatType = repeatType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getScheduleFinishDate() {
        return scheduleFinishDate;
    }

    public void setScheduleFinishDate(String scheduleFinishDate) {
        this.scheduleFinishDate = scheduleFinishDate;
    }
}