/*
 * RegionTest.java
 *
 * This file is part of IF97.
 *
 * IF97 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * IF97 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with IF97. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2014 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Ralph Hummeling &lt;<a
 * href="mailto:engineering@hummeling.com?subject=IF97%20Java%20library">engineering@hummeling.com</a>&gt;
 */
public class RegionTest {

    @Test
    public void testPressureB23() {

        double[][] X = {{0.1652916425e2, 0.623150000e3}};

        for (double[] x : X) {
            assertEquals(x[0], Region.pressureB23(x[1]), 1e-8);
        }
    }

    @Test
    public void testRegionHS() {

        double[][] X = {
            {1200, 3},
            {3000, 7},
            {1900, 4},
            {1000, 3}};
        String[] expected = {
            "Region 1",
            "Region 2",
            "Region 3",
            "Region 4"};

        for (int i = 0; i < X.length; i++) {
            Region region = Region.getRegionHS(X[i][0], X[i][1]);
            //System.out.println("region.NAME: " + region.getName());
            assertEquals(expected[i], region.getName());
        }
    }

    @Test
    public void testRegionPH() {

        double[][] X = {
            {10, 1000},
            {10, 3000},
            {25, 2000},
            {10, 2000},
            {25, 5000}};
        String[] expected = {
            "Region 1",
            "Region 2",
            "Region 3",
            "Region 4",
            "Region 5"};

        for (int i = 0; i < X.length; i++) {
            Region region = Region.getRegionPH(X[i][0], X[i][1]);
            //System.out.println("region.NAME: " + region.getName());
            assertEquals(expected[i], region.getName());
        }
    }

    @Test
    public void testSaturationPressure3() {

        double[][] X = {
            {1.687755057e1, 3.8},
            {2.164451789e1, 4.2},
            {1.668968482e1, 5.2}};

        for (double[] x : X) {
            assertEquals(x[0], Region.saturationPressure3(x[1]), 1e-8);
        }
    }

    @Test
    public void testSpecificEnthalpy1() {

        double[][] X = {
            {3.085509647e2, 1.0},
            {7.006304472e2, 2.0},
            {1.198359754e3, 3.0}};

        for (double[] x : X) {
            assertEquals(x[0], Region.specificEnthalpy1(x[1]), 1e-6);
        }
    }

    @Test
    public void testSpecificEnthalpy2ab() {

        double[][] X = {
            {2.723729985e3, 7.0},
            {2.599047210e3, 8.0},
            {2.511861477e3, 9.0}};

        for (double[] x : X) {
            assertEquals(x[0], Region.specificEnthalpy2ab(x[1]), 1e-6);
        }
    }

    @Test
    public void testSpecificEnthalpy2c3b() {

        double[][] X = {
            {2.687693850e3, 5.5},
            {2.451623609e3, 5.0},
            {2.144360448e3, 4.5}};

        for (double[] x : X) {
            assertEquals(x[0], Region.specificEnthalpy2c3b(x[1]), 1e-6);
        }
    }

    @Test
    public void testSpecificEnthalpy3a() {

        double[][] X = {
            {1.685025565e3, 3.8},
            {1.816891476e3, 4.0},
            {1.949352563e3, 4.2}};

        for (double[] x : X) {
            assertEquals(x[0], Region.specificEnthalpy3a(x[1]), 1e-6);
        }
    }

    @Test
    public void testSpecificEnthalpyB13() {

        double[][] X = {
            {1.632525047e3, 3.7},
            {1.593027214e3, 3.6},
            {1.566104611e3, 3.5}};

        for (double[] x : X) {
            assertEquals(x[0], Region.specificEnthalpyB13(x[1]), 1e-6);
        }
    }

    @Test
    public void testTemperatureB23HS() {

        double[][] X = {
            {7.135259364e2, 2600, 5.1},
            {7.685345532e2, 2700, 5.15},
            {8.176202120e2, 2800, 5.2}};

        for (double[] x : X) {
            assertEquals(x[0], Region.temperatureB23HS(x[1], x[2]), 1e-7);
        }
    }

    @Test
    public void testTemperatureB23P() {

        double[][] X = {{0.623150000e3, 0.1652916425e2}};

        for (double[] x : X) {
            assertEquals(x[0], Region.temperatureB23P(x[1]), 1e-7);
        }
    }
}
