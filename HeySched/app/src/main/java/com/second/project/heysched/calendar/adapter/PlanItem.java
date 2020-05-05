package com.second.project.heysched.calendar.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class PlanItem implements Parcelable {
    String plan_no;
    String title;
    String startdatetime;
    String loc_x;
    String loc_y;
    String content;
    String location;
    String enddatetime;
    String color;
    String host_id;

    public PlanItem(){}
    public PlanItem(String plan_no, String title, String content, String color) {
        this.plan_no = plan_no;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public PlanItem(String plan_no, String title, String startdatetime, String loc_x, String loc_y, String content, String location, String enddatetime, String color, String host_id) {
        this.plan_no = plan_no;
        this.title = title;
        this.startdatetime = startdatetime;
        this.loc_x = loc_x;
        this.loc_y = loc_y;
        this.content = content;
        this.location = location;
        this.enddatetime = enddatetime;
        this.color = color;
        this.host_id = host_id;
    }

    public PlanItem(Parcel in) {
        plan_no = in.readString();
        title = in.readString();
        startdatetime = in.readString();
        loc_x = in.readString();
        loc_y = in.readString();
        content = in.readString();
        location = in.readString();
        enddatetime = in.readString();
        color = in.readString();
        host_id = in. readString();
    }

    public static final Creator<PlanItem> CREATOR = new Creator<PlanItem>() {
        @Override
        public PlanItem createFromParcel(Parcel in) {
            return new PlanItem(in);
        }

        @Override
        public PlanItem[] newArray(int size) {
            return new PlanItem[size];
        }
    };

    @Override
    public String toString() {
        return "PlanItem{" +
                "plan_no='" + plan_no + '\'' +
                ", title='" + title + '\'' +
                ", startdatetime='" + startdatetime + '\'' +
                ", loc_x='" + loc_x + '\'' +
                ", loc_y='" + loc_y + '\'' +
                ", content='" + content + '\'' +
                ", location='" + location + '\'' +
                ", enddatetime='" + enddatetime + '\'' +
                ", color='" + color + '\'' +
                ", host_id='" + host_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(plan_no);
        dest.writeString(title);
        dest.writeString(startdatetime);
        dest.writeString(loc_x);
        dest.writeString(loc_y);
        dest.writeString(content);
        dest.writeString(location);
        dest.writeString(enddatetime);
        dest.writeString(color);
        dest.writeString(host_id);
    }

    public String getPlan_no() {
        return plan_no;
    }

    public void setPlan_no(String plan_no) {
        this.plan_no = plan_no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartdatetime() {
        return startdatetime;
    }

    public void setStartdatetime(String startdatetime) {
        this.startdatetime = startdatetime;
    }

    public String getLoc_x() {
        return loc_x;
    }

    public void setLoc_x(String loc_x) {
        this.loc_x = loc_x;
    }

    public String getLoc_y() {
        return loc_y;
    }

    public void setLoc_y(String loc_y) {
        this.loc_y = loc_y;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(String enddatetime) {
        this.enddatetime = enddatetime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }
}
