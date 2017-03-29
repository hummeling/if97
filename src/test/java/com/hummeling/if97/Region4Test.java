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
 * Copyright 2009-2017 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * For methods related to liquid or vapour state above 16.5292 MPa (surrounded
 * by region 3), iterations are implemented. Tabulated values from Wagner are
 * used as test values, as well as values from Zittau's Fluid Property
 * Calculator. At pressures near critical pressure (22.064 MPa) Zittau's values
 * are not consistent with values from Wagner's Table 1 and 2, in which cases
 * Wagner's values are assumed correct.
 *
 * @author Ralph Hummeling &lt;<a
 * href="mailto:engineering@hummeling.com?subject=IF97%20Java%20library">engineering@hummeling.com</a>&gt;
 */
public class Region4Test {

    static Region4 region4 = new Region4();

    @Test
    public void testSaturationPressureB34H() {

        double[][] X = {
            {1.724175718e1, 1700},
            {2.193442957e1, 2000},
            {2.018090839e1, 2400}};

        for (double[] x : X) {
            assertEquals(x[0], region4.saturationPressureB34H(x[1]), 1e-8);
        }
    }

    @Test
    public void testSaturationPressureB34S() {

        double[][] X = {
            {1.687755057e1, 3.8},
            {2.164451789e1, 4.2},
            {1.668968482e1, 5.2}};

        for (double[] x : X) {
            assertEquals(x[0], region4.saturationPressureB34S(x[1]), 1e-8);
        }
    }

    @Test
    public void testSaturationPressureT() {

        double[][] X = {
            {0.353658941e-2, 300},
            {0.263889776e1, 500},
            {0.123443146e2, 600}};

        for (double[] x : X) {
            assertEquals(x[0], region4.saturationPressureT(x[1]), 1e-7);
        }
    }

    @Test
    public void testSaturationTemperatureP() {

        double[][] X = {
            {0.372755919e3, 0.1},
            {0.453035632e3, 1},
            {0.584149488e3, 10}};

        for (double[] x : X) {
            assertEquals(x[0], region4.saturationTemperatureP(x[1]), 1e-6);
        }
    }

    @Test
    public void testTemperatureHS() {

        double[][] X = {
            //{Double.NaN, 1800, 5.2}, // should throw an out-of-range exception
            {3.468475498e2, 1800, 5.3},
            {4.251373305e2, 2400, 6},
            {5.225579013e2, 2500, 5.5}};

        for (double[] x : X) {
            assertEquals(x[0], region4.temperatureHS(x[1], x[2]), 1e-7);
        }
    }

    /**
     * Test values from http://thermodynamik.hszg.de/fpc/
     */
    @Test
    public void testSpecificEnthalpyPX() {

        double[][] X = {
            {1827.1005, 20, 0},
            {1903.0579, 20, 0.13},
            {2119.2443, 20, 0.5},
            {2405.5451, 20, 0.99},
            {2411.3880, 20, 1}};

        for (double[] x : X) {
            assertEquals(x[0], region4.specificEnthalpyPX(x[1], x[2]), 1e-3);
        }
    }

    /**
     * Test values from Table 2 Wagner.
     */
    @Test
    public void testSpecificEnthalpySaturatedLiquidP() {

        double[][] X = {
            {1407.87, 10},
            {1610.15, 15},
            {1827.10, 20},
            {1855.90, 20.5},
            {1889.40, 21},
            {1932.81, 21.5},
            {2021.92, 22},
            //FAIL {1933.0015, 21.5}, // Zittau
            //FAIL {2013.3573, 22}, // Zittau
            {2087.55, IF97.pc}};

        for (double[] x : X) {
            assertEquals(x[0], region4.specificEnthalpySaturatedLiquidP(x[1]), 1e-2);
        }
    }

    /**
     * Test values from Table 2 Wagner.
     *
     * The 22 MPa case fails on the last two digits (.18 is wrong), possibly
     * because of a typo: the previous tabulated value does end with .18.
     */
    @Test
    public void testSpecificEnthalpySaturatedVapourP() {

        double[][] X = {
            {2725.47, 10},
            {2610.86, 15},
            {2411.39, 20},
            {2378.16, 20.5},
            {2337.54, 21},
            {2282.18, 21.5},
            //FAIL {2164.18, 22},
            //FAIL {2281.8484, 21.5}, // Zittau
            //FAIL {2163.2117, 22}, // Zittau
            {2087.55, IF97.pc}};

        for (double[] x : X) {
            assertEquals(x[0], region4.specificEnthalpySaturatedVapourP(x[1]), 1e-2);
        }
    }

    /**
     * Test values from http://thermodynamik.hszg.de/fpc/
     */
    @Test
    public void testSpecificEnthalpyTX() {

        double[][] X = {
            {1686.2747, 625, 0},
            {1798.6443, 625, 0.13},
            {2118.4653, 625, 0.5},
            {2542.0121, 625, 0.99},
            {2550.6559, 625, 1}};

        for (double[] x : X) {
            double ps = region4.saturationPressureT(x[1]);

            assertEquals(x[0], region4.specificEnthalpyPX(ps, x[2]), 1e-2);
        }
    }

    /**
     * Test values from http://thermodynamik.hszg.de/fpc/ and Table 2 Wagner.
     */
    @Test
    public void testSpecificEntropySaturatedLiquidP() {

        double[][] X = {
            {3.36029, 10},
            {3.68445, 15},
            {4.01538, 20},
            {4.0588, 20.5},
            {4.10926, 21},
            {4.1749, 21.5}, // Wagner Table 2
            {4.3109, 22}, // Wagner Table 2
            //FAIL {4.17520, 21.5}, // Zittau
            //FAIL {4.29763, 22}, // Zittau
            {4.41202, IF97.pc}};

        for (double[] x : X) {
            assertEquals(x[0], region4.specificEntropySaturatedLiquidP(x[1]), 1e-4);
        }
    }

    /**
     * Test values from http://thermodynamik.hszg.de/fpc/ and Table 2 Wagner.
     */
    @Test
    public void testSpecificEntropySaturatedVapourP() {

        double[][] X = {
            {5.61589, 10},
            {5.31080, 15},
            {4.92991, 20},
            {4.87357, 20.5},
            {4.80624, 21},
            {4.7166, 21.5}, // Wagner Table 2
            {4.5308, 22}, // Wagner Table 2
            //FAIL {4.71608, 21.5}, // Zittau
            //FAIL {4.5293, 22}, // Zittau
            {4.41202, IF97.pc}};

        for (double[] x : X) {
            assertEquals(x[0], region4.specificEntropySaturatedVapourP(x[1]), 1e-5);
        }
    }

    /**
     * Test values from http://thermodynamik.hszg.de/fpc/
     */
    @Test
    public void testSpecificVolumePX() {

        double[][] X = {
            {0.00145262, 10, 0},
            {0.00203865, 20, 0},
            {0.0025352, 20, 0.13},
            {0.00394847, 20, 0.5},
            {0.00582009, 20, 0.99},
            {0.00585828, 20, 1},
            {0.0180336, 10, 1}};

        for (double[] x : X) {
            assertEquals(x[0], region4.specificVolumePX(x[1], x[2]), 1e-7);
        }
    }

    @Test
    public void testSpecificVolumeSaturatedLiquidP() {

        double[][] X = {
            {0.00145262, 10},
            {0.00165696, 15},
            {0.00168249, 15.5},
            {0.00170954, 16},
            {0.00173833, 16.5},
            {0.00176934, 17},
            {0.00203865, 20},
            {0.00211358, 20.5},
            {0.00221186, 21},
            {0.00236016, 21.5},
            {0.00275039, 22},
            //FAIL {0.00270572, 22}, // Zittau
            {0.00310559, IF97.pc}};

        for (double[] x : X) {
            assertEquals(x[0], region4.specificVolumeSaturatedLiquidP(x[1]), 1e-8);
        }
    }

    @Test
    public void testSpecificVolumeSaturatedLiquidT() {

        double[][] X = {
            {0.00229020, 644.15},
            {0.00238170, 645.15},
            {0.00252643, 646.15}};

        for (double[] x : X) {
            double ps = region4.saturationPressureT(x[1]);

            assertEquals(x[0], region4.specificVolumeSaturatedLiquidP(ps), 1e-8);
        }
    }

    @Test
    public void testSpecificVolumeSaturatedVapourP() {

        double[][] X = {
            {0.0103401, 15},
            {0.00981114, 15.5},
            {0.00930813, 16},
            {0.00882826, 16.5},
            {0.00836934, 17},
            {0.00585828, 20},
            {0.00543778, 20.5},
            {0.00498768, 21},
            {0.00446300, 21.5},
            {0.00357662, 22},
            {0.00310559, IF97.pc}};

        for (double[] x : X) {
            assertEquals(x[0], region4.specificVolumeSaturatedVapourP(x[1]), 1e-8);
        }
    }

    /**
     * Test values from http://thermodynamik.hszg.de/fpc/
     */
    @Test
    public void testVapourFractionPH() {

        double[][] X = {
            {0.00496239, 20, 1830},
            {0.295915, 20, 2000},
            {0.966917, 16, 2550},
            {0.980510, 20, 2400},
            {0.999641, 10, 2725}};

        for (double[] x : X) {
            assertEquals(x[0], region4.vapourFractionPH(x[1], x[2]), 1e-6);
        }
    }

    /**
     * Test values from http://thermodynamik.hszg.de/fpc/
     */
    @Test
    public void testVapourFractionPS() {

        double[][] X = {
            {0.681062, 5, 5},
            {0.726951, 10, 5},
            {0.420567, 20, 4.4},
            {0, 20, 4.01538},
            {0, 21, 4.10926},
            {0, 22, 4.31085},
            {1, 20, 4.92991},
            {1, 21, 4.80624},
            {1, 22, 4.5308}};

        for (double[] x : X) {
            assertEquals(x[0], region4.vapourFractionPS(x[1], x[2]), 1e-4);
        }
    }
}
