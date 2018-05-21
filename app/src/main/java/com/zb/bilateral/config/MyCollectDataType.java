package com.zb.bilateral.config;

/**
 * Created by ys on 2016/9/11.
 */
public enum MyCollectDataType {
    MUSEUM(1,"博物馆","博物馆"),
    COLLECT(2,"馆藏","馆藏"),
    ACTIVITY(3,"活动","活动"),
    CULTRUE(4,"文物拾遗","文物拾遗");

    int value;

    String category;

    String name;

    MyCollectDataType(int value, String category, String name){
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
        int length = MyCollectDataType.values().length;
        String[] names = new String[length];

        for (int i = 0; i < length; i++) {
            names[i] = MyCollectDataType.values()[i].getName();
        }

        return names;
    }

    public static String[] getTabNames(){
        String[] names = new String[4];

        for (int i = 0; i < 4; i++) {
            names[i] = MyCollectDataType.values()[i].getName();
        }

        return names;
    }

    public static int getMultiType(MyCollectDataType dataType){
        int retVal;
        switch (dataType){
            case MUSEUM:
                retVal = 1;
                break;
            case COLLECT:
                retVal = 2;
                break;
            case ACTIVITY:
                retVal = 3;
                break;
            case CULTRUE:
                retVal = 4;
                break;
            default:
                retVal = 5;
                break;
        }

        return retVal;
    }

}
