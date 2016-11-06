package com.xyp.util;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 大象
 */
public class Random {
    static List x=new ArrayList<Integer>();
    
    public static int random(){
        int y;
        do{
            y=(int)(Math.random() * Integer.MAX_VALUE);
        }while(x.contains(y));
        x.add(y);
        return y;
    }
}
