package com.zb.bilateral.config;

/**
 * Created by ys on 2016/9/11.
 */
public enum DynamicDataType {
    ACTIVITY(1,"活动","活动"),
    INFOMATE(2,"资讯","资讯"),
    ANNOUNCEM(3,"公告","公告");

    int value;

    String category;

    String name;

    DynamicDataType(int value, String category, String name){
        this.value = value;
        this.category = category;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public static String[] getAllNames(){
        int length = DynamicDataType.values().length;
        String[] names = new String[length];

        for (int i = 0; i < length; i++) {
            names[i] = DynamicDataType.values()[i].getName();
        }

        return names;
    }

    public static String[] getTabNames(){
        String[] names = new String[4];

        for (int i = 0; i < 4; i++) {
            names[i] = DynamicDataType.values()[i].getName();
        }

        return names;
    }

    public static int getMultiType(DynamicDataType dataType){
        int retVal;
        switch (dataType){
            case ACTIVITY:
                retVal = 1;
                break;
            case INFOMATE:
                retVal = 2;
                break;
            case ANNOUNCEM:
                retVal = 3;
                break;
            default:
                retVal = 4;
                break;
        }

        return retVal;
    }

}
