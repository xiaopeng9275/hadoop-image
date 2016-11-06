/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xyp.method;

import com.xyp.entity.HImage;

/**
 * 自定义算法接口规范，用户编写自己的算法必须遵守此接口规范
 * @author 大象
 *
 */
public interface GenericAlgorithm {
    void run();
    HImage getProcessedImage();
    void setHImage(HImage himage);
}
