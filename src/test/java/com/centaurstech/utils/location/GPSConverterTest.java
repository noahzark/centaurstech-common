package com.centaurstech.utils.location;

import com.centaurstech.domain.GPSLocation;
import com.centaurstech.utils.DistanceCalculator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Feliciano on 9/26/2018.
 */
public class GPSConverterTest {

    @Test
    public void testGPSToGaode() throws Exception {
        GPSLocation original = new GPSLocation(22.530448,113.952733);
        GPSLocation apiConverted = new GPSLocation(22.527450629341,113.957622070313);
        GPSLocation converted = GPSConverter.gps84_To_Gcj02(original.getLat(), original.getLng());

        // Count the difference (meter)
        double distance = DistanceCalculator.distanceBetweenInKM(apiConverted, converted) * 1000;

        System.out.println(original);
        System.out.println(apiConverted);
        System.out.println(converted);
        System.out.println(distance);

        // Assert the difference is less than 0.1 meter
        assertThat((distance < 0.1), is(true));
    }

}
