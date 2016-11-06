package com.xyp.entity;

import java.awt.image.BufferedImage;

import java.util.List;
import javax.imageio.metadata.IIOMetadata;

public class HImageHeader {
    IIOMetadata metadata=null;
    List<BufferedImage> thumbnails=null;
    
    public HImageHeader(IIOMetadata metadata, List thumbs){
        this.metadata=metadata;
        this.thumbnails=thumbs;
    }
    public List<BufferedImage> getThumbnails(){
        return thumbnails;
    }
    public IIOMetadata getMetadata(){
        return metadata;
    }
    
}
