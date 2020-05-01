package com.second.project.heysched.dbtest;

public class MemberVO {
    String id;
    String pass;
    String birthday;
    String token;

    public MemberVO(String id, String pass, String birthday, String token) {
        this.id = id;
        this.pass = pass;
        this.birthday = birthday;
        this.token = token;
    }

    @Override
    public String toString() {
        return "MemberVO{" +
                "id='" + id + '\'' +
                ", pass='" + pass + '\'' +
                ", birthday='" + birthday + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
