/*
 * Region1Test.java
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
 * Region 1 tests.
 *
 * @author Ralph Hummeling &lt;<a
 * href="mailto:engineering@hummeling.com?subject=IF97%20Java%20library">engineering@hummeling.com</a>&gt;
 */
public class Region1Test {

    static Region1 region = new Region1();

    @Test
    public void testIsobaricCubicExpansionCoefficientPT() {

        double[][] X = {
            {0.277354533e-3, 3, 300},
            {0.344095843e-3, 80, 300},
            {0.164118128e-2, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.isobaricCubicExpansionCoefficientPT(x[1], x[2]), 1e-12);
        }
    }

    @Test
    public void testIsothermalCompressibilityPT() {

        double[][] X = {
            {0.446382123e-3, 3, 300},
            {0.372039437e-3, 80, 300},
            {0.112892188e-2, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.isothermalCompressibilityPT(x[1], x[2]), 1e-11);
        }
    }

    @Test
    public void testPressureHS() {

        double[][] X = {
            {9.800980612e-4, 0.001, 0}, // ok with tol = 1e-13
            {9.192954727e1, 90, 0},
            {5.868294423e1, 1500, 3.4}};

        for (double[] x : X) {
            assertEquals(x[0], region.pressureHS(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificEnthalpyPT() {

        double[][] X = {
            {0.115331273e3, 3, 300},
            {0.184142828e3, 80, 300},
            {0.975542239e3, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificEnthalpyPT(x[1], x[2]), 1e-6);
        }
    }

    @Test
    public void testSpecificEntropyPT() {

        double[][] X = {
            {0.392294792, 3, 300},
            {0.368563852, 80, 300},
            {0.258041912e1, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificEntropyPT(x[1], x[2]), 1e-9);
        }
    }

    @Test
    public void testSpecificInternalEnergyPT() {

        double[][] X = {
            {0.112324818e3, 3, 300},
            {0.106448356e3, 80, 300},
            {0.971934985e3, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificInternalEnergyPT(x[1], x[2]), 1e-6);
        }
    }

    @Test
    public void testSpecificIsobaricHeatCapacityPT() {

        double[][] X = {
            {0.417301218e1, 3, 300},
            {0.401008987e1, 80, 300},
            {0.465580682e1, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificIsobaricHeatCapacityPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificIsochoricHeatCapacityPT() {

        double[][] X = {
            {0.412120160e1, 3, 300},
            {0.391736606e1, 80, 300},
            {0.322139223e1, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificIsochoricHeatCapacityPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificVolumePT() {

        double[][] X = {
            {0.100215168e-2, 3, 300},
            {0.971180894e-3, 80, 300},
            {0.120241800e-2, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificVolumePT(x[1], x[2]), 1e-11);
        }
    }

    @Test
    public void testSpeedOfSoundPT() {

        double[][] X = {
            {0.150773921e4, 3, 300},
            {0.163469054e4, 80, 300},
            {0.124071337e4, 3, 500}};

        for (double[] x : X) {
            assertEquals(x[0], region.speedOfSoundPT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testTemperaturePH() {

        double[][] X = {
            {0.391798509e3, 3, 500},
            {0.378108626e3, 80, 500},
            {0.611041229e3, 80, 1500}};

        for (double[] x : X) {
            assertEquals(x[0], region.temperaturePH(x[1], x[2]), 1e-6);
        }
    }

    @Test
    public void testTemperaturePS() {

        double[][] X = {
            {0.307842258e3, 3, 0.5},
            {0.309979785e3, 80, 0.5},
            {0.565899909e3, 80, 3}};

        for (double[] x : X) {
            assertEquals(x[0], region.temperaturePS(x[1], x[2]), 1e-6);
        }
    }
}
