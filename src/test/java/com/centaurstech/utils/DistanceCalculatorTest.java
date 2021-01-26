package com.centaurstech.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Feliciano on 7/13/2018.
 */
public class DistanceCalculatorTest {


    @Test
    //计算两地之间的距离(单位：KM)
    public void testDistance() throws Exception {
        double fromSanyaToWuhan = DistanceCalculator.distanceBetweenInKM(18.25d,109.5d,  30.6d, 114.3d);
        System.out.println("From Sanya to Wuhan: " + fromSanyaToWuhan * 1000);
        assertThat((fromSanyaToWuhan> 1450 && fromSanyaToWuhan < 1460), is(true));
    }

}
