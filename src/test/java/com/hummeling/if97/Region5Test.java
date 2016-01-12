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
 * along with IF97. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2016 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Region 2 tests.
 *
 * @author Ralph Hummeling &lt;<a
 * href="mailto:engineering@hummeling.com?subject=IF97%20Java%20library">engineering@hummeling.com</a>&gt;
 */
public class Region5Test {

    static Region region = new Region5();

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

    @Test
    public void testTemperaturePH() {

        double[][] X = {
            {0.534433241e3, 0.001, 3000},
            {0.575373370e3, 3, 3000},
            {0.101077577e4, 3, 4000},
            {0.801299102e3, 5, 3500},
            {0.101531583e4, 5, 4000},
            {0.875279054e3, 25, 3500},
            {0.743056411e3, 40, 2700},
            {0.791137067e3, 60, 2700},
            {0.882756860e3, 60, 3200}};

        for (double[] x : X) {
            assertEquals(x[0], region.temperaturePH(x[1], x[2]), 1e-5);
        }
    }
}
