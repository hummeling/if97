/*
 * Region2Test.java
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

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Region 2 tests.
 *
 * @author Ralph Hummeling &lt;<a
 * href="mailto:engineering@hummeling.com?subject=IF97%20Java%20library">engineering@hummeling.com</a>&gt;
 */
public class Region2Test {

    static Region region = new Region2();

    //@Test
    //public void testEnthalpy2bc() {
    //    double tol = 1e-6;
    //    System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
    //    double[][] X = new double[][]{
    //        {0.3516004323e4, 0.1e3}
    //    };
    //    for (double[] x : X) {
    //        assertEquals(x[0], Region2.enthalpy2bc(x[1]), tol);
    //    }
    //}
    @Test
    public void testIsobaricCubicExpansionCoefficientPT() {
        double tol = 1e-10;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.337578289e-2, 0.0035, 300},
            {0.142878736e-2, 0.0035, 700},
            {0.126019688e-1, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.isobaricCubicExpansionCoefficientPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testIsothermalCompressibilityPT() {
        double tol = 1e-6;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.286239651e3, 0.0035, 300},
            {0.285725461e3, 0.0035, 700},
            {0.818411389e-1, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.isothermalCompressibilityPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testPressureHS() {
        double tol = 1e-8;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {1.371012767, 2800, 6.5},
            {1.879743844e-3, 2800, 9.5},
            {1.024788997e-1, 4100, 9.5},
            {4.793911442, 2800, 6},
            {8.395519209e1, 3600, 6},
            {7.527161441, 3600, 7},
            {9.439202060e1, 2800, 5.1},
            {8.414574124, 2800, 5.8},
            {8.376903879e1, 3400, 5.8}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.pressureHS(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSpecificEnthalpyPT() {
        double tol = 1e-5;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.254991145e4, 0.0035, 300},
            {0.333568375e4, 0.0035, 700},
            {0.263149474e4, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.specificEnthalpyPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSpecificEntropyPT() {
        double tol = 1e-7;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.852238967e1, 0.0035, 300},
            {0.101749996e2, 0.0035, 700},
            {0.517540298e1, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.specificEntropyPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSpecificInternalEnergyPT() {
        double tol = 1e-5;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.241169160e4, 0.0035, 300},
            {0.301262819e4, 0.0035, 700},
            {0.246861076e4, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.specificInternalEnergyPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSpecificIsobaricHeatCapacityPT() {
        double tol = 1e-8;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.191300162e1, 0.0035, 300},
            {0.208141274e1, 0.0035, 700},
            {0.103505092e2, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.specificIsobaricHeatCapacityPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSpecificIsochoricHeatCapacityPT() {
        double tol = 1e-8;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.144132662e1, 0.0035, 300},
            {0.161978333e1, 0.0035, 700},
            {0.297553837e1, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.specificIsochoricHeatCapacityPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSpecificVolumePT() {
        double tol = 1e-4;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.394913866e2, 0.0035, 300},
            {0.923015898e2, 0.0035, 700},
            {0.542946619e-2, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.specificVolumePT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSpeedOfSoundPT() {
        
        double tol = 1e-6;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.427920172e3, 0.0035, 300},
            {0.644289068e3, 0.0035, 700},
            {0.480386523e3, 30, 700}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.speedOfSoundPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testTemperaturePH() {
        double tol = 1e-5;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.534433241e3, 0.001, 3000},
            {0.575373370e3, 3, 3000},
            {0.101077577e4, 3, 4000},
            {0.801299102e3, 5, 3500},
            {0.101531583e4, 5, 4000},
            {0.875279054e3, 25, 3500},
            {0.743056411e3, 40, 2700},
            {0.791137067e3, 60, 2700},
            {0.882756860e3, 60, 3200}
        };
        for (double[] x : X) {
            assertEquals(x[0], region.temperaturePH(x[1], x[2]), tol);
        }
    }
}
