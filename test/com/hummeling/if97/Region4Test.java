/*
 * Region4Test.java
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
public class Region4Test {

    static Region region = new Region4();

    @Test
    public void testSaturationPressureT() {

        double[][] X = {
            {0.353658941e-2, 300},
            {0.263889776e1, 500},
            {0.123443146e2, 600}};

        for (double[] x : X) {
            assertEquals(x[0], Region4.saturationPressureT(x[1]), 1e-7);
        }
    }

    @Test
    public void testSaturationTemperatureP() {

        double[][] X = {
            {0.372755919e3, 0.1},
            {0.453035632e3, 1},
            {0.584149488e3, 10}};

        for (double[] x : X) {
            assertEquals(x[0], Region4.saturationTemperatureP(x[1]), 1e-6);
        }
    }

    @Test
    public void testTemperatureHS() {

        double[][] X = {
            {3.468475498e2, 1800, 5.3},
            {4.251373305e2, 2400, 6},
            {5.225579013e2, 2500, 5.5}};

        for (double[] x : X) {
            assertEquals(x[0], region.temperatureHS(x[1], x[2]), 1e-7);
        }
    }

    //@Test
    //public void testVapourFractionPS() {
    //
    //    double[][] X = {
    //        {1, 2.8},
    //        {1, 4.8},
    //        {10, 5.4}};
    //
    //    for (double[] x : X) {
    //        assertEquals(x[0], new Region4().vapourFractionPS(x[1]), 1e-6);
    //        System.out.format("vapourFractionPS(%.1f, %.1f): %f%n", x[0], x[1], new Region4().vapourFractionPS(x[0], x[1]));
    //    }
    //}
}
