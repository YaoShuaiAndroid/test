package com.zb.bilateral.config;

/**
 * Created by ys on 2016/9/11.
 */
public enum ShowDataType {
    BASE(1,"基本展览","基本展览"),
    TEMP(2,"临时展览","临时展览");

    int value;

    String category;

    String name;

    ShowDataType(int value, String category, String name){
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
        int length = ShowDataType.values().length;
        String[] names = new String[length];

        for (int i = 0; i < length; i++) {
            names[i] = ShowDataType.values()[i].getName();
        }

        return names;
    }

    public static String[] getTabNames(){
        String[] names = new String[4];

        for (int i = 0; i < 4; i++) {
            names[i] = ShowDataType.values()[i].getName();
        }

        return names;
    }

    public static int getMultiType(ShowDataType dataType){
        int retVal;
        switch (dataType){
            case BASE:
                retVal = 1;
                break;
            case TEMP:
                retVal = 2;
                break;
            default:
                retVal =3;
                break;
        }

        return retVal;
    }

}
