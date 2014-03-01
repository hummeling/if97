/*
 * IF97Test.java
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
 *
 * @author Ralph Hummeling &lt;<a
 * href="mailto:engineering@hummeling.com?subject=IF97%20Java%20library">engineering@hummeling.com</a>&gt;
 */
public class IF97Test {

    double TOLERANCE = 1e-8;
    IF97 if97 = new IF97();

    @Test
    public void testDielectricConstantPT() throws OutOfRangeException {
        double tol = 1e-4;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.785907250e2, 5, 298.15},
            //meta {0.112620970e1, 10, 873.15},
            {0.103126058e2, 40, 673.15}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.dielectricConstantPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testRefractiveIndexPTLambda() throws OutOfRangeException {
        double tol = 1e-6;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.139277824e1, 0.1, 298.15, 0.2265},
            {0.133285819e1, 0.1, 298.15, 0.5893},
            //meta {0.101098988e1, 10, 773.15, 0.2265},
            //meta {0.100949307e1, 10, 773.15, 0.5893},
            {0.119757252e1, 40, 673.15, 0.2265},
            {0.116968699e1, 40, 673.15, 0.5893}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.refractiveIndexPTLambda(x[1], x[2], x[3]), tol);
        }
    }

    @Test
    public void testPressureHS() throws OutOfRangeException {
        double tol = 1e-8;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {9.800980612e-4, 0.001, 0}, // region 1: ok with tol = 1e-13
            {9.192954727e1, 90, 0},
            {5.868294423e1, 1500, 3.4},
            {1.371012767, 2800, 6.5}, // region 2
            {1.879743844e-3, 2800, 9.5},
            {1.024788997e-1, 4100, 9.5},
            {4.793911442, 2800, 6},
            {8.395519209e1, 3600, 6},
            {7.527161441, 3600, 7},
            {9.439202060e1, 2800, 5.1},
            {8.414574124, 2800, 5.8},
            {8.376903879e1, 3400, 5.8},
            {2.555703246e1, 1700, 3.8}, // region 3
            {4.540873468e1, 2000, 4.2},
            {6.078123340e1, 2100, 4.3},
            {6.363924887e1, 2400, 4.7},
            {3.434999263e1, 2600, 5.1},
            {8.839043281e1, 2700, 5.0}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.pressureHS(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSaturationPressureH() {
        double tol = 1e-8;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {1.724175718e1, 1700},
            {2.193442957e1, 2000},
            {2.018090839e1, 2400}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.saturationPressureH(x[1]), tol);
        }
    }

    @Test
    public void testSaturationPressureS() {
        double tol = 1e-8;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {1.687755057e1, 3.8},
            {2.164451789e1, 4.2},
            {1.668968482e1, 5.2}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.saturationPressureS(x[1]), tol);
        }
    }

    @Test
    public void testSaturationPressureT() throws OutOfRangeException {
        double tol = 1e-7;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.353658941e-2, 300},
            {0.263889776e1, 500},
            {0.123443146e2, 600}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.saturationPressureT(x[1]), tol);
        }
        //IF97 if97 = new IF97(IF97.UnitSystem.ENGINEERING);
        if97.setUnitSystem(IF97.UnitSystem.ENGINEERING);
        double Teng = 100;
        System.out.format("PsatT(%f): %f bar", Teng,
                if97.saturationPressureT(Teng));
        if97.setUnitSystem(IF97.UnitSystem.DEFAULT);
    }

    @Test
    public void testSaturationTemperatureP() throws OutOfRangeException {
        double tol = 1e-6;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.372755919e3, 0.1},
            {0.453035632e3, 1},
            {0.584149488e3, 10}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.saturationTemperatureP(x[1]), tol);
        }
    }

    @Test
    public void testPartialDerivativePT() throws OutOfRangeException {
        double tol = 1e-5;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{};
        for (double[] x : X) {
            System.out.println("region: " + Region.getRegionPT(x[1], x[2]).getName());
            assertEquals(x[0], if97.partialDerivativePT(x[1], x[2],
                    IF97.Quantity.p, IF97.Quantity.nu, IF97.Quantity.u), tol);
        }
    }

    @Test
    public void testSpecificEnthalpyPT() throws OutOfRangeException {
        double tol = 1e-5;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.115331273e3, 3, 300}, // region 1
            {0.184142828e3, 80, 300},
            {0.975542239e3, 3, 500},
            //meta {0.254991145e4, 0.0035, 300}, // region 2
            //meta {0.333568375e4, 0.0035, 700},
            {0.263149474e4, 30, 700},
            //{0.276881115e4, 1, 450}, // region 2 metastable-vapour
            //{0.274015123e4, 1, 440},
            //{0.272134539e4, 1.5, 450},
            {0.521976855e4, 0.5, 1500}, // region 5
            {0.516723514e4, 30, 1500},
            {0.657122604e4, 30, 2000}
        };
        for (double[] x : X) {
            System.out.println("region: " + Region.getRegionPT(x[1], x[2]).getName());
            assertEquals(x[0], if97.specificEnthalpyPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSpecificVolumePT() throws OutOfRangeException {
        double tol = 1e-12;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {1.470853100e-3, 50, 630}, // region 3a
            {1.503831359e-3, 80, 670},
            {2.204728587e-3, 50, 710}, // region 3b
            {1.973692940e-3, 80, 750},
            {1.761696406e-3, 20, 630}, // region 3c
            {1.819560617e-3, 30, 650},
            {2.245587720e-3, 26, 656}, // region 3d
            {2.506897702e-3, 30, 670},
            {2.970225962e-3, 26, 661}, // region 3e
            {3.004627086e-3, 30, 675},
            {5.019029401e-3, 26, 671}, // region 3f
            {4.656470142e-3, 30, 690},
            {2.163198378e-3, 23.6, 649}, // region 3g
            {2.166044161e-3, 24, 650},
            {2.651081407e-3, 23.6, 652}, // region 3h
            {2.967802335e-3, 24, 654},
            {3.273916816e-3, 23.6, 653}, // region 3i
            {3.550329864e-3, 24, 655},
            {4.545001142e-3, 23.5, 655}, // region 3j
            {5.100267704e-3, 24, 660},
            {6.109525997e-3, 23, 660}, // region 3k
            {6.427325645e-3, 24, 670},
            {2.117860851e-3, 22.6, 646}, // region 3l
            {2.062374674e-3, 23, 646},
            {2.533063780e-3, 22.6, 648.6}, // region 3m
            {2.572971781e-3, 22.8, 649.3},
            {2.923432711e-3, 22.6, 649.0}, // region 3n
            {2.913311494e-3, 22.8, 649.7},
            {3.131208996e-3, 22.6, 649.1}, // region 3o
            {3.221160278e-3, 22.8, 649.9},
            {3.715596186e-3, 22.6, 649.4}, // region 3p
            {3.664754790e-3, 22.8, 650.2},
            {1.970999272e-3, 21.1, 640}, // region 3q
            {2.043919161e-3, 21.8, 643},
            {5.251009921e-3, 21.1, 644}, // region 3r
            {5.256844741e-3, 21.8, 648},
            {1.932829079e-3, 19.1, 635}, // region 3s
            {1.985387227e-3, 20, 638},
            {8.483262001e-3, 17, 626}, // region 3t
            {6.227528101e-3, 20, 640}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.specificVolumePT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testSurfaceTensionT() throws OutOfRangeException {
        double tol = 1e-10;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.0716859625, 300},
            {0.0428914992, 450},
            {0.00837561087, 600}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.surfaceTensionT(x[1]), tol);
        }
    }

    @Test
    public void testTemperatureHS() throws OutOfRangeException {
        double tol = 1e-5;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {3.468475498e2, 1800, 5.3}, // region 4
            {4.251373305e2, 2400, 6},
            {5.225579013e2, 2500, 5.5}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.temperatureHS(x[1], x[2]), tol);
        }
    }

    @Test
    public void testTemperaturePH() throws OutOfRangeException {
        double tol = 1e-5;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.391798509e3, 3, 500}, // region 1
            {0.378108626e3, 80, 500},
            {0.611041229e3, 80, 1500},
            {0.534433241e3, 0.001, 3000}, // region 2a
            {0.575373370e3, 3, 3000},
            {0.101077577e4, 3, 4000},
            {0.801299102e3, 5, 3500}, // region 2b
            {0.101531583e4, 5, 4000},
            {0.875279054e3, 25, 3500},
            {0.743056411e3, 40, 2700}, // region 2c
            {0.791137067e3, 60, 2700},
            {0.882756860e3, 60, 3200}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.temperaturePH(x[1], x[2]), tol);
        }
    }

    @Test
    public void testThermalConductivityPT() throws OutOfRangeException {
        double tol = 1e-6;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.607509806, 0.1, 298.15},
            //meta {0.867570353e-1, 10, 873.15},
            {0.398506911, 40, 673.15}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.thermalConductivityPT(x[1], x[2]), tol);
        }
    }

    @Test
    public void testViscosityPT() throws OutOfRangeException {
        double tol = 1e-12;
        System.out.println(getClass().getSimpleName() + " tolerance: " + tol);
        double[][] X = new double[][]{
            {0.890022551e-3, .1, 298.15},
            {0.339743835e-4, 20, 873.15},
            {0.726093560e-4, 60, 673.15}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.dynamicViscosityPT(x[1], x[2]), tol);
        }
    }
}
