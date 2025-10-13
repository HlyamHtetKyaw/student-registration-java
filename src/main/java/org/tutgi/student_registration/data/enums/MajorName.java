package org.tutgi.student_registration.data.enums;

public enum MajorName {
    Civil("Civil Engineering", "ပြို့ပြအင်ဂျင်နီယာသင်တန်း"),
    EC("Electronic Engineering", "အီလက်ထရောနစ်အင်ဂျင်နီယာသင်တန်း"),
    EP("Electrical Power Engineering", "လျှပ်စစ်စွမ်းအားအင်ဂျင်နီယာသင်တန်း"),
    Mech("Mechanical Engineering", "စက်မှုအင်ဂျင်နီယာသင်တန်း"),
    IT("Information Technology", "သုတနည်းပညာသင်တန်း"),
    MN("Mining Engineering", "သတ္တုတူးဖော်ရေးအင်ဂျင်နီယာသင်တန်း");

    private final String engName;
    private final String mmName;

    MajorName(String engName, String mmName) {
        this.engName = engName;
        this.mmName = mmName;
    }

    public String getEngName() {
        return engName;
    }

    public String getMmName() {
        return mmName;
    }
}

