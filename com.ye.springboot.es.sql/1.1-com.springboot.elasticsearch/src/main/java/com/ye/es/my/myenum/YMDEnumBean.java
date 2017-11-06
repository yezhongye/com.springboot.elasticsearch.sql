package com.ye.es.my.myenum;

/**
 * Created by zjx on 2017/10/20 0020.
 */
public enum YMDEnumBean {
    YEAR("yyyy","年"),MONTH("MM","月"),DAY("dd","日"),HOUR("HH","时"),MINUTE("mm","分"),SECOND("ss","秒");
    // 成员变量
    private String name;
    private String value;
    // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
    private YMDEnumBean(String name, String value) {
        this.name = name;
        this.value = value;
    }
    // 普通方法
    public static String getMyUTF8Name(String name) {
        for (YMDEnumBean e : YMDEnumBean .values()) {
            if (e.getName().equals(name)) {
                return e.value;
            }       }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
