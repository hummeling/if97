/*
 * Region3Test.java
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
 *
 * @author Ralph Hummeling &lt;<a
 * href="mailto:engineering@hummeling.com?subject=IF97%20Java%20library">engineering@hummeling.com</a>&gt;
 */
public class Region3Test {

    static Region3 region3 = new Region3();

    @Test
    public void testEnthalpy2bc() {

        double[][] X = {{2.095936454e3, 25}};

        for (double[] x : X) {
            assertEquals(x[0], region3.enthalpy3ab(x[1]), 1e-6);
        }
    }

    @Test
    public void testIsobaricCubicExpansionCoefficientRhoT() {

        double[][] X = {
            {0.168653107e-1, 500, 650},
            {0.685312229e-1, 200, 650},
            {0.441515098e-2, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.isobaricCubicExpansionCoefficientRhoT(x[1], x[2]), 1e-7);
        }
    }

    @Test
    public void testIsothermalCompressibilityRhoT() {

        double[][] X = {
            {0.345506956e-1, 500, 650},
            {0.375798565, 200, 650},
            {0.806710817e-2, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.isothermalCompressibilityRhoT(x[1], x[2]), 1e-9);
        }
    }

    @Test
    public void testPressureHS() {

        double[][] X = {
            {2.555703246e1, 1700, 3.8},
            {4.540873468e1, 2000, 4.2},
            {6.078123340e1, 2100, 4.3},
            {6.363924887e1, 2400, 4.7},
            {3.434999263e1, 2600, 5.1},
            {8.839043281e1, 2700, 5.0}};

        for (double[] x : X) {
            assertEquals(x[0], region3.pressureHS(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testPressureRhoT() {

        double[][] X = {
            {0.255837018e2, 500, 650},
            {0.222930643e2, 200, 650},
            {0.783095639e2, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.pressureRhoT(x[1], x[2]), 1e-7);
        }
    }

    @Test
    public void testSpecificEnthalpyRhoT() {

        double[][] X = {
            {0.186343019e4, 500, 650},
            {0.237512401e4, 200, 650},
            {0.225868845e4, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.specificEnthalpyRhoT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSpecificEntropyRhoT() {

        double[][] X = {
            {0.405427273e1, 500, 650},
            {0.485438792e1, 200, 650},
            {0.446971906e1, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.specificEntropyRhoT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificInternalEnergyRhoT() {

        double[][] X = {
            {0.181226279e4, 500, 650},
            {0.226365868e4, 200, 650},
            {0.210206932e4, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.specificInternalEnergyRhoT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSpecificIsobaricHeatCapacityRhoT() {

        double[][] X = {
            {0.138935717e2, 500, 650},
            {0.446579342e2, 200, 650},
            {0.634165359e1, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.specificIsobaricHeatCapacityRhoT(x[1], x[2]), 1e-7);
        }
    }

    @Test
    public void testSpecificIsochoricHeatCapacityRhoT() {

        double[][] X = {
            {0.319131787e1, 500, 650},
            {0.404118076e1, 200, 650},
            {0.271701677e1, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.specificIsochoricHeatCapacityRhoT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificVolumePH() {

        double[][] X = {
            {1.749903962e-3, 20, 1700},
            {1.908139035e-3, 50, 2000},
            {1.676229776e-3, 100, 2100},
            {6.670547043e-3, 20, 2500},
            {2.801244590e-3, 50, 2400},
            {2.404234998e-3, 100, 2700}};

        for (double[] x : X) {
            assertEquals(x[0], region3.specificVolumePH(x[1], x[2]), 1e-12);
        }
    }

    @Test
    public void testSpecificVolumePS() {

        double[][] X = {
            {1.733791463e-3, 20, 3.8},
            {1.469680170e-3, 50, 3.6},
            {1.555893131e-3, 100, 4.0},
            {6.262101987e-3, 20, 5.0},
            {2.332634294e-3, 50, 4.5},
            {2.449610757e-3, 100, 5.0}};

        for (double[] x : X) {
            assertEquals(x[0], region3.specificVolumePS(x[1], x[2]), 1e-12);
        }
    }

    @Test
    public void testSpecificVolumePT() {

        double[][] X = {
            {1.470853100e-3, 50, 630}, // a
            {1.503831359e-3, 80, 670},
            {2.204728587e-3, 50, 710}, // b
            {1.973692940e-3, 80, 750},
            {1.761696406e-3, 20, 630}, // c
            {1.819560617e-3, 30, 650},
            {2.245587720e-3, 26, 656}, // d
            {2.506897702e-3, 30, 670},
            {2.970225962e-3, 26, 661}, // e
            {3.004627086e-3, 30, 675},
            {5.019029401e-3, 26, 671}, // f
            {4.656470142e-3, 30, 690},
            {2.163198378e-3, 23.6, 649}, // g
            {2.166044161e-3, 24, 650},
            {2.651081407e-3, 23.6, 652}, // h
            {2.967802335e-3, 24, 654},
            {3.273916816e-3, 23.6, 653}, // i
            {3.550329864e-3, 24, 655},
            {4.545001142e-3, 23.5, 655}, // j
            {5.100267704e-3, 24, 660},
            {6.109525997e-3, 23, 660}, // k
            {6.427325645e-3, 24, 670},
            {2.117860851e-3, 22.6, 646}, // l
            {2.062374674e-3, 23, 646},
            {2.533063780e-3, 22.6, 648.6}, // m
            {2.572971781e-3, 22.8, 649.3},
            {2.923432711e-3, 22.6, 649.0}, // n
            {2.913311494e-3, 22.8, 649.7},
            {3.131208996e-3, 22.6, 649.1}, // o
            {3.221160278e-3, 22.8, 649.9},
            {3.715596186e-3, 22.6, 649.4}, // p
            {3.664754790e-3, 22.8, 650.2},
            {1.970999272e-3, 21.1, 640}, // q
            {2.043919161e-3, 21.8, 643},
            {5.251009921e-3, 21.1, 644}, // r
            {5.256844741e-3, 21.8, 648},
            {1.932829079e-3, 19.1, 635}, // s
            {1.985387227e-3, 20, 638},
            {8.483262001e-3, 17, 626}, // t
            {6.227528101e-3, 20, 640},
            {2.268366647e-3, 21.5, 644.6}, // u
            {2.296350553e-3, 22, 646.1},
            {2.832373260e-3, 22.5, 648.6}, // v
            {2.811424405e-3, 22.3, 647.9},
            {3.694032281e-3, 22.15, 647.5}, // w
            {3.622226305e-3, 22.3, 648.1},
            {4.528072649e-3, 22.11, 648}, // x
            {4.556905799e-3, 22.3, 649},
            {2.698354719e-3, 22, 646.84}, // y
            {2.717655648e-3, 22.064, 647.05},
            {3.798732962e-3, 22, 646.89}, // z
            {3.701940010e-3, 22.064, 647.15}};

        for (double[] x : X) {
            assertEquals(x[0], region3.specificVolumePT(x[1], x[2]), 1e-12);
        }
    }

    @Test
    public void testSpeedOfSoundRhoT() {

        double[][] X = {
            {0.502005554e3, 500, 650},
            {0.383444594e3, 200, 650},
            {0.760696041e3, 500, 750}};

        for (double[] x : X) {
            assertEquals(x[0], region3.speedOfSoundRhoT(x[1], x[2]), 1e-6);
        }
    }

    @Test
    public void testTemperaturePH() {

        double[][] X = {
            {6.293083892e2, 20, 1700},
            {6.905718338e2, 50, 2000},
            {7.336163014e2, 100, 2100},
            {6.418418053e2, 20, 2500},
            {7.351848618e2, 50, 2400},
            {8.420460876e2, 100, 2700}};

        for (double[] x : X) {
            assertEquals(x[0], region3.temperaturePH(x[1], x[2]), 1e-7);
        }
    }

    @Test
    public void testTemperaturePS() {

        double[][] X = {
            {6.282959869e2, 20, 3.8},
            {6.297158726e2, 50, 3.6},
            {7.056880237e2, 100, 4.0},
            {6.401176443e2, 20, 5.0},
            {7.163687517e2, 50, 4.5},
            {8.474332825e2, 100, 5.0}};

        for (double[] x : X) {
            assertEquals(x[0], region3.temperaturePS(x[1], x[2]), 1e-7);
        }
    }
}
