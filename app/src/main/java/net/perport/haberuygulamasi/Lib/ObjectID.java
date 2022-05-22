package net.perport.haberuygulamasi.Lib;

public class ObjectID {

    public static long getTime(String id){
        String sub = id.substring(0,8);

        return Long.parseLong(sub, 16)*1000;
    }
}
