package com.second.project.heysched.plan.adapter;

public class PlanItem {
    String plan_no;
    String title;
    String startdatetime;
    String loc_x;
    String loc_y;
    String content;
    String location;
    String enddatetime;

    public PlanItem(String plan_no, String title, String startdatetime, String loc_x, String loc_y, String content, String location, String enddatetime) {
        this.plan_no = plan_no;
        this.title = title;
        this.startdatetime = startdatetime;
        this.loc_x = loc_x;
        this.loc_y = loc_y;
        this.content = content;
        this.location = location;
        this.enddatetime = enddatetime;
    }

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
                '}';
    }
}
