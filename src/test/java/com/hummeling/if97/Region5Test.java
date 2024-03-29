/*
 * Region5Test.java
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
 * along with IF97. If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2023 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Region 2 tests.
 *
 * @author Ralph Hummeling (<a href="https://www.hummeling.com">www.hummeling.com</a>)
 */
public class Region5Test {

    static Region region;

    @BeforeClass
    public static void setUpClass() throws Exception {
        region = new Region5();
    }

    //@Test
    //public void testEnthalpy2bc() {
    //
    //
    //    double[][] X = {
    //        {0.3516004323e4, 0.1e3}
    //    };
    //    for (double[] x : X) {
    //        assertEquals(x[0], Region2.enthalpy2bc(x[1]), tol);
    //    }
    //}
    @Test
    public void testHeatCapacityRatioPT() {

        double[][] X = {
            {0.261609445e1 / 0.215337784e1, 0.5, 1500},
            {0.272724317e1 / 0.219274829e1, 30, 1500},
            {0.288569882e1 / 0.239589436e1, 30, 2000}}; // test values for cp/cv

        for (double[] x : X) {
            assertEquals(x[0], region.heatCapacityRatioPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testIsentropicExponentPT() {

        double[][] X = {
            {0.917068690e3, 0.5, 1500},
            {0.928548002e3, 30, 1500},
            {0.106736948e4, 30, 2000}}; // test values for speed of sound

        for (double[] x : X) {
            double w2 = x[0] * x[0], // speed of sound [m/s] squared [m2/s2]
                    p = x[1] * 1e6, // [Pa]
                    rho = 1 / region.specificVolumePT(x[1], x[2]), // [kg/m3]
                    kappa = w2 * rho / p; // [-]
            assertEquals(kappa, region.isentropicExponentPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testIsobaricCubicExpansionCoefficientPT() {

        double[][] X = {
            {0.667539000e-3, 0.5, 1500},
            {0.716950754e-3, 30, 1500},
            {0.508830641e-3, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.isobaricCubicExpansionCoefficientPT(x[1], x[2]), 1e-12);
        }
    }

    @Test
    public void testIsothermalCompressibilityPT() {

        double[][] X = {
            {0.200003859e1, 0.5, 1500},
            {0.332881253e-1, 30, 1500},
            {0.329193892e-1, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.isothermalCompressibilityPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificEnthalpyPT() {

        double[][] X = {
            {0.521976855e4, 0.5, 1500},
            {0.516723514e4, 30, 1500},
            {0.657122604e4, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificEnthalpyPT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSpecificEntropyPT() {

        double[][] X = {
            {0.965408875e1, 0.5, 1500},
            {0.772970133e1, 30, 1500},
            {0.853640523e1, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificEntropyPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificInternalEnergyPT() {

        double[][] X = {
            {0.452749310e4, 0.5, 1500},
            {0.447495124e4, 30, 1500},
            {0.563707038e4, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificInternalEnergyPT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSpecificIsobaricHeatCapacityPT() {

        double[][] X = {
            {0.261609445e1, 0.5, 1500},
            {0.272724317e1, 30, 1500},
            {0.288569882e1, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificIsobaricHeatCapacityPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificIsochoricHeatCapacityPT() {

        double[][] X = {
            {0.215337784e1, 0.5, 1500},
            {0.219274829e1, 30, 1500},
            {0.239589436e1, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificIsochoricHeatCapacityPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificVolumePT() {

        double[][] X = {
            {0.138455090e1, 0.5, 1500},
            {0.230761299e-1, 30, 1500},
            {0.311385219e-1, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificVolumePT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpeedOfSoundPT() {

        double[][] X = {
            {0.917068690e3, 0.5, 1500},
            {0.928548002e3, 30, 1500},
            {0.106736948e4, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], region.speedOfSoundPT(x[1], x[2]), 1e-5);
        }
    }
}
