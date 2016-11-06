package com.xyp.process;

import com.xyp.entity.HImage;
import com.xyp.method.GenericAlgorithm;

public class Threader implements Runnable {
    HImage image=null;
    String cls=null;
    public Threader(HImage img, String className){
        image=img;
        cls=className;
    }
    
    @Override
    public void run() {
        try {
            GenericAlgorithm pi=(GenericAlgorithm)Class.forName(cls).newInstance();
            pi.setHImage(image);
            pi.run();
            image=pi.getProcessedImage();
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
        } catch (InstantiationException e) {
        	e.printStackTrace();
        } catch (IllegalAccessException e) {
        	e.printStackTrace();
        }                    
    }
    
   
}
