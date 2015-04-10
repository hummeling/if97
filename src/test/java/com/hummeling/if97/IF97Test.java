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
 * Copyright 2009-2015 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Ralph Hummeling &lt;<a
 * href="mailto:engineering@hummeling.com?subject=IF97%20Java%20library">engineering@hummeling.com</a>&gt;
 */
public class IF97Test {

    private final IF97 if97 = new IF97();

    /**
     * Bug filed by Klaus Heckmann on SourceForge forum.
     *
     * First tested with new release which show consistent results.
     */
    @Test
    public void bug20141015() {

        double s = 2.222172673702562, // [kJ/kg-K]
                p0 = 1.2165; // [MPa]

        for (int i = 0; i < 5; i++) {
            double p = p0 + 0.0001 * i, // [MPa]
                    x = if97.vapourFractionPS(p, s), // [-]
                    T = if97.temperaturePS(p, s), // [K]
                    h = if97.specificEnthalpyPS(p, s), // [kJ/kg]
                    vPH = if97.specificVolumePH(p, h), // [m3/kg]
                    vPT = if97.specificVolumePT(p, T); // [m3/kg]
            System.out.format("s=%.12f J/kg-K, p=%.1f Pa%n  if97: x=%f, v(p,h)=%.12f m\u00b3/kg, v(p,T)=%.12f m\u00b3/kg, T=%.6f K, h=%.3f J/kg%n", s * 1e3, p * 1e6, x, vPH, vPT, T, h * 1e3);
        }
    }

    /**
     * Bug filed by Klaus Heckmann on SourceForge forum.
     *
     * Given pressure isn't the saturation pressure.
     */
    @Test
    public void bug20141020() {

        double p = 63.8325 / 10, // [MPa]
                s = 3064 / 1e3; // [kJ/kg-K]

        double T = if97.temperaturePS(p, s), // [K]
                pSat = if97.saturationPressureT(T), // [MPa]
                x = if97.vapourFractionPS(p, s), // [-]
                Tsat = if97.saturationTemperatureP(p); // [K]
        System.out.format("p=%f bar, s=%.1f J/kg-K%n  if97: pSat=%f bar, T=%.6f K, x=%f, Tsat=%.6f K%n", p * 10, s * 1e3, pSat * 10, T, x, Tsat);
    }

    /**
     * Bug filed by Philippe Mack.
     *
     * Returned enthalpy not as expected (Region2 value).
     */
    @Test
    public void bug20150113() {
        System.out.print("bug20150113: ");

        if97.setUnitSystem(IF97.UnitSystem.ENGINEERING);

        double p = 86.643, // [bar(a)]
                T = 480; // [Celsius]
        System.out.format("p=%.3f bar(a), T=%.0f\u00b0C: h=%.3f kJ/kg%n", p, T, if97.specificEnthalpyPT(p, T));
        System.out.format("Region2:     p=%.3f bar(a), T=%.0f\u00b0C: h=%.3f kJ/kg%n", p, T, new Region2().specificEnthalpyPT(p / 10, T + IF97.T0));
        System.out.format("Region2Meta: p=%.3f bar(a), T=%.0f\u00b0C: h=%.3f kJ/kg%n", p, T, new Region2Meta().specificEnthalpyPT(p / 10, T + IF97.T0));
    }

    /**
     * Bug filed by Philippe Mack.
     *
     * Returned enthalpy not as expected using Imperial units.
     */
    @Test
    public void bug20150410() {
        System.out.print("bug20150410: ");

        if97.setUnitSystem(IF97.UnitSystem.IMPERIAL);

        double p = 720, // [psia]
                T = 704; // [Fahrenheit]
        System.out.format("p=%f psi(a), T=%f\u00b0F: h=%f BTU/lb%n", p, T, if97.specificEnthalpyPT(p, T));

        if97.setUnitSystem(IF97.UnitSystem.ENGINEERING);
        p = 49.6422525; // [bara]
        T = 373.333333; // [Celsius]
        System.out.format("p=%f bar(a), T=%f\u00b0C: h=%f kJ/kg%n", p, T, if97.specificEnthalpyPT(p, T));
    }

    @Test
    public void testDielectricConstantPT() {

        double[][] X = {
            {0.785907250e2, 5, 298.15},
            //meta {0.112620970e1, 10, 873.15},
            {0.103126058e2, 40, 673.15}};

        for (double[] x : X) {
            assertEquals(x[0], if97.dielectricConstantPT(x[1], x[2]), 1e-4);
        }
    }

    @Test
    public void testRefractiveIndexPTLambda() {

        double[][] X = {
            {0.139277824e1, 0.1, 298.15, 0.2265},
            {0.133285819e1, 0.1, 298.15, 0.5893},
            //meta {0.101098988e1, 10, 773.15, 0.2265},
            //meta {0.100949307e1, 10, 773.15, 0.5893},
            {0.119757252e1, 40, 673.15, 0.2265},
            {0.116968699e1, 40, 673.15, 0.5893}};

        for (double[] x : X) {
            assertEquals(x[0], if97.refractiveIndexPTLambda(x[1], x[2], x[3]), 1e-6);
        }
    }

    @Test
    public void testPressureHS() {

        double[][] X = {
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
            {8.839043281e1, 2700, 5.0}};

        for (double[] x : X) {
            assertEquals(x[0], if97.pressureHS(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSaturationPressureT() {

        double[][] X = {
            {0.353658941e-2, 300},
            {0.263889776e1, 500},
            {0.123443146e2, 600}};

        for (double[] x : X) {
            assertEquals(x[0], if97.saturationPressureT(x[1]), 1e-7);
        }
    }

    @Test
    public void testSaturationTemperatureP() {

        double[][] X = {
            {0.372755919e3, 0.1},
            {0.453035632e3, 1},
            {0.584149488e3, 10}};

        for (double[] x : X) {
            assertEquals(x[0], if97.saturationTemperatureP(x[1]), 1e-6);
        }
    }

    @Test
    public void testPartialDerivativePT() {

        double[][] X = {};
        for (double[] x : X) {
            //System.out.println("region: " + Region.getRegionPT(x[1], x[2]).getName());
            assertEquals(x[0], if97.partialDerivativePT(x[1], x[2], IF97.Quantity.p, IF97.Quantity.v, IF97.Quantity.u), 1e-5);
        }
    }

    @Test
    public void testSpecificEnthalpyPT() {

        double[][] X = {
            {0.115331273e3, 3, 300}, // region 1
            {0.184142828e3, 80, 300},
            {0.975542239e3, 3, 500},
            {0.254991145e4, 0.0035, 300}, // region 2 ==> actually metastable region
            {0.333568375e4, 0.0035, 700}, // region 2 ==> actually metastable region
            {0.263149474e4, 30, 700}, // region 2
            //{0.276881115e4, 1, 450}, // region 2 metastable-vapour ==> actually region 1
            //{0.274015123e4, 1, 440},
            //{0.272134539e4, 1.5, 450},
            {0.521976855e4, 0.5, 1500}, // region 5
            {0.516723514e4, 30, 1500},
            {0.657122604e4, 30, 2000}};

        for (double[] x : X) {
            //System.out.println("region: " + Region.getRegionPT(x[1], x[2]).getName());
            assertEquals(x[0], if97.specificEnthalpyPT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSpecificEnthalpyPX() {

        double[][] X = {
            {0.1, 0},
            {1, 0.5},
            {0.1, 1}};

        for (double[] x : X) {
            System.out.format("specificEnthalpyPX(%.2f bar, %.2f): %.2f%n", x[0] * 10, x[1], if97.specificEnthalpyPX(x[0], x[1]));
        }
    }

    @Test
    public void testSpecificEnthalpyTX() {

        double[][] X = {
            {373.15, 0},
            {373.15, 0.5},
            {373.15, 1}};

        for (double[] x : X) {
            System.out.format("specificEnthalpyTX(%.2f K, %.2f): %.2f%n", x[0], x[1], if97.specificEnthalpyTX(x[0], x[1]));
        }
    }

    /**
     * Tests specific entropy as a function of pressure and temperature.
     *
     * Disabled region 2 and region 2 meta tests aren't actually in these
     * regions.
     */
    @Test
    public void testSpecificEntropyPT() {

        double[][] X = {
            {0.392294792, 3, 300}, // region 1 tests
            {0.368563852, 80, 300},
            {0.258041912e1, 3, 500},
            //{0.852238967e1, 0.0035, 300}, // region 2 tests
            //{0.101749996e2, 0.0035, 700},
            {0.517540298e1, 30, 700},
            //{0.656660377e1, 1, 450}, // region 2 meta tests
            //{0.650218759e1, 1, 440},
            //{0.629170440e1, 1.5, 450},
            {0.965408875e1, 0.5, 1500}, // region 5 tests
            {0.772970133e1, 30, 1500},
            {0.853640523e1, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], if97.specificEntropyPT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSpecificEntropyPX() {

        double[][] X = {
            {0.1, 0},
            {1, 0.5},
            {0.1, 1}};

        for (double[] x : X) {
            System.out.format("specificEntropyPX(%.2f bar, %.2f): %.2f%n", x[0] * 10, x[1], if97.specificEntropyPX(x[0], x[1]));
        }
    }

    @Test
    public void testSpecificEntropyTX() {

        double[][] X = {
            {373.15, 0},
            {373.15, 0.5},
            {373.15, 1}};

        for (double[] x : X) {
            System.out.format("specificEntropyTX(%.2f K, %.2f): %.2f%n", x[0], x[1], if97.specificEntropyTX(x[0], x[1]));
        }
    }

    @Test
    public void testSpecificVolumePT() {

        double[][] X = {
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
            {6.227528101e-3, 20, 640}};

        for (double[] x : X) {
            assertEquals(x[0], if97.specificVolumePT(x[1], x[2]), 1e-12);
        }
    }

    @Test
    public void testSpeedOfSoundPT() {

        double[][] X = {
            {0.150773921e4, 3, 300}, // region 1 tests
            {0.163469054e4, 80, 300},
            {0.124071337e4, 3, 500},
            //            {0.427920172e3, 0.0035, 300}, // region 2 tests
            //            {0.644289068e3, 0.0035, 700},
            {0.480386523e3, 30, 700},
            //{0.498408101e3, 1, 450}, // region 2 meta tests
            //{0.489363295e3, 1, 440},
            //{0.481941819e3, 1.5, 450},
            //            {0.502005554e3, 500, 650}, // region 3 tests
            //            {0.383444594e3, 200, 650},
            //            {0.760696041e3, 500, 750},
            {0.917068690e3, 0.5, 1500}, // region 5 tests
            {0.928548002e3, 30, 1500},
            {0.106736948e4, 30, 2000}};

        for (double[] x : X) {
            assertEquals(x[0], if97.speedOfSoundPT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSurfaceTensionT() {

        double[][] X = {
            {0.0716859625, 300},
            {0.0428914992, 450},
            {0.00837561087, 600}
        };
        for (double[] x : X) {
            assertEquals(x[0], if97.surfaceTensionT(x[1]), 1e-10);
        }
    }

    @Test
    public void testTemperatureHS() {

        double[][] X = {
            {3.468475498e2, 1800, 5.3}, // region 4
            {4.251373305e2, 2400, 6},
            {5.225579013e2, 2500, 5.5}};

        for (double[] x : X) {
            assertEquals(x[0], if97.temperatureHS(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testTemperaturePH() {

        double[][] X = {
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
            {0.882756860e3, 60, 3200}};

        for (double[] x : X) {
            assertEquals(x[0], if97.temperaturePH(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testTemperaturePS() {

        double[][] X = {
            {0.307842258e3, 3, 0.5},
            {0.309979785e3, 80, 0.5},
            {0.565899909e3, 80, 3},
            {0.399517097e3, 0.1, 7.5},
            {0.514127081e3, 0.1, 8},
            {0.103984917e4, 2.5, 8},
            {0.600484040e3, 8, 6},
            {0.106495556e4, 8, 7.5},
            {0.103801126e4, 90, 6},
            {0.697992849e3, 20, 5.75},
            {0.854011484e3, 80, 5.25},
            {0.949017998e3, 80, 5.75},
            {6.282959869e2, 20, 3.8},
            {6.297158726e2, 50, 3.6},
            {7.056880237e2, 100, 4.0},
            {6.401176443e2, 20, 5.0},
            {7.163687517e2, 50, 4.5},
            {8.474332825e2, 100, 5.0}};

        for (double[] x : X) {
            assertEquals(x[0], if97.temperaturePS(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testThermalConductivityRhoT() {

        double[][] X = {
            {0.607509806, 0.1, 298.15},
            //{0.867570353e-1, 10, 873.15},
            {0.398506911, 40, 673.15}};

        for (double[] x : X) {
            assertEquals(x[0], if97.thermalConductivityRhoT(if97.densityPT(x[1], x[2]), x[2]), 1e-6);
        }
    }

    @Test
    public void testViscosityPT() {

        double[][] X = {
            {0.890022551e-3, .1, 298.15},
            {0.339743835e-4, 20, 873.15},
            {0.726093560e-4, 60, 673.15}};

        for (double[] x : X) {
            assertEquals(x[0], if97.dynamicViscosityPT(x[1], x[2]), 1e-12);
        }
    }

    @Test
    public void testUnitSystem() {

        assertEquals(4.4482216152605, IF97.lb * IF97.g, 1e-13);

        if97.setUnitSystem(IF97.UnitSystem.ENGINEERING);
        double Teng = 100; // [Celsius]
        //System.out.format("PsatT(%.1f C): %f bar%n", Teng, if97.saturationPressureT(Teng));

        if97.setUnitSystem(IF97.UnitSystem.IMPERIAL);
        double Timp = 212; // [F]
        //System.out.format("PsatT(%.1f F): %f bar%n", Timp, if97.saturationPressureT(Timp) * IF97.psi * 10);

        if97.setUnitSystem(IF97.UnitSystem.DEFAULT);
    }
}
