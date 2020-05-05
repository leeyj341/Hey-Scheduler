package com.second.project.heysched.FCM;

public class FCMModel {
    public String to;   //받는 사람 이름 (token or Id)
    public Data data = new Data();

    class Data {
        public String message;
        public String time;
    }

}
