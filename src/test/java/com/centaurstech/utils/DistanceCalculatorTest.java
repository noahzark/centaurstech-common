package com.centaurstech.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Feliciano on 7/13/2018.
 */
public class DistanceCalculatorTest {


    @Test
    public void testDistance() throws Exception {
        double fromSanyaToWuhan = DistanceCalculator.distanceBetween(18.25d,109.5d,  30.6d, 114.3d);
        assertThat((fromSanyaToWuhan> 1456 && fromSanyaToWuhan < 1457), is(true));
    }

}
