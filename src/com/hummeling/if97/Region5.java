/*
 * Region5.java
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

import static com.hummeling.if97.IF97.*;
import static java.lang.Math.*;

/**
 * Region 5.
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
final class Region5 extends Region {

    private static final String NAME;
    static final double Tref, pRef;
    static final double[][] Jno, IJnr, IJnA, IJnB, IJnC;

    static {
        NAME = "Region 5";
        Tref = 1000;
        pRef = 1;
        Jno = new double[][]{
            {00, -.131799836742010e2},
            {01, 0.685408416344340e1},
            {-3, -.248051489334660e-1},
            {-2, 0.369015349803330},
            {-1, -.311613182139250e1},
            {02, -.329616265389170}};
        IJnr = new double[][]{
            {1, 1, 0.15736404855259e-2},
            {1, 2, 0.90153761673944e-3},
            {1, 3, -.50270077677648e-2},
            {2, 3, 0.22440037409485e-5},
            {2, 9, -.41163275453471e-5},
            {3, 7, 0.37919454822955e-7}};
        IJnA = new double[][]{
            {0, 0, 1.0898952318288e3},
            {0, 1, 8.4951654495535e2},
            {0, 2, -1.0781748091826e2},
            {0, 3, 3.3153654801263e1},
            {0, 7, -7.4232016790248e0},
            {0, 20, 1.1765048724356e1},
            {1, 0, 1.8445749355790e0},
            {1, 1, -4.1792700549624e0},
            {1, 2, 6.2478196935812e0},
            {1, 3, -1.7344563108114e1},
            {1, 7, -2.0058176862096e2},
            {1, 9, 2.7196065473796e2},
            {1, 11, -4.5511318285818e2},
            {1, 18, 3.0919688604755e3},
            {1, 44, 2.5226640357872e5},
            {2, 0, -6.1707422868339e-3},
            {2, 2, -3.1078046629583e-1},
            {2, 7, 1.1670873077107e1},
            {2, 36, 1.2812798404046e8},
            {2, 38, -9.8554909623276e8},
            {2, 40, 2.8224546973002e9},
            {2, 42, -3.5948971410703e9},
            {2, 44, 1.7227349913197e9},
            {3, 24, -1.3551334240775e4},
            {3, 44, 1.2848734664650e7},
            {4, 12, 1.3865724283226e0},
            {4, 32, 2.3598832556514e5},
            {4, 44, -1.3105236545054e7},
            {5, 32, 7.3999835474766e3},
            {5, 36, -5.5196697030060e5},
            {5, 42, 3.7154085996233e6},
            {6, 34, 1.9127729239660e4},
            {6, 44, -4.1535164835634e5},
            {7, 28, -6.2459855192507e1}};
        IJnB = new double[][]{
            {0, 0, 1.4895041079516e3},
            {0, 1, 7.4307798314034e2},
            {0, 2, -9.7708318797837e1},
            {0, 12, 2.4742464705674e0},
            {0, 18, -6.3281320016026e-1},
            {0, 24, 1.1385952129658e0},
            {0, 28, -4.7811863648625e-1},
            {0, 40, 8.5208123431544e-3},
            {1, 0, 9.3747147377932e-1},
            {1, 2, 3.3593118604916e0},
            {1, 6, 3.3809355601454e0},
            {1, 12, 1.6844539671904e-1},
            {1, 18, 7.3875745236695e-1},
            {1, 24, -4.7128737436186e-1},
            {1, 28, 1.5020273139707e-1},
            {1, 40, -2.1764114219750e-3},
            {2, 2, -2.1810755324761e-2},
            {2, 8, -1.0829784403677e-1},
            {2, 18, -4.6333324635812e-2},
            {2, 40, 7.1280351959551e-5},
            {3, 1, 1.1032831789999e-4},
            {3, 2, 1.8955248387902e-4},
            {3, 12, 3.0891541160537e-3},
            {3, 24, 1.3555504554949e-3},
            {4, 2, 2.8640237477456e-7},
            {4, 12, -1.0779857357512e-5},
            {4, 18, -7.6462712454814e-5},
            {4, 24, 1.4052392818316e-5},
            {4, 28, -3.1083814331434e-5},
            {4, 40, -1.0302738212103e-6},
            {5, 18, 2.8217281635040e-7},
            {5, 24, 1.2704902271945e-6},
            {5, 40, 7.3803353468292e-8},
            {6, 28, -1.1030139238909e-8},
            {7, 2, -8.1456365207833e-14},
            {7, 28, -2.5180545682962e-11},
            {9, 1, -1.7565233969407e-18},
            {9, 40, 8.6934156344163e-15}};
        IJnC = new double[][]{
            {-7, 0, -3.2368398555242e12},
            {-7, 4, 7.3263350902181e12},
            {-6, 0, 3.5825089945447e11},
            {-6, 2, -5.8340131851590e11},
            {-5, 0, -1.0783068217470e10},
            {-5, 2, 2.0825544563171e10},
            {-2, 0, 6.1074783564516e5},
            {-2, 1, 8.5977722535580e5},
            {-1, 0, -2.5745723604170e4},
            {-1, 2, 3.1081088422714e4},
            {0, 0, 1.2082315865936e3},
            {0, 1, 4.8219755109255e2},
            {1, 4, 3.7966001272486e0},
            {1, 8, -1.0842984880077e1},
            {2, 4, -4.5364172676660e-2},
            {6, 0, 1.4559115658698e-13},
            {6, 1, 1.1261597407230e-12},
            {6, 4, -1.7804982240686e-11},
            {6, 10, 1.2324579690832e-7},
            {6, 12, -1.1606921130984e-6},
            {6, 16, 2.7846367088554e-5},
            {6, 20, -5.9270038474176e-4},
            {6, 22, 1.2918582991878e-3}};
    }

    private static double enthalpy2bc(double pressure) {

        double[] n = {
            0.90584278514712e3,
            -.67955786399241,
            0.12809002730136e-3,
            0.26526571908428e4,
            0.45257578905948e1};

        return n[3] + sqrt((pressure - n[4]) / n[2]);
    }

    /**
     * Dimensionless Gibbs free energy.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaO(double pi, double tau) {

        double out = log(pi);

        for (double[] jno : Jno) {
            out += jno[1] * pow(tau, jno[0]);
        }
        return out;
    }

    /**
     * First partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure [MPa]
     * @return
     */
    private static double gammaOPi(double pi) {

        return 1 / pi;
    }

    /**
     * Second partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure [MPa]
     * @return
     */
    private static double gammaOPiPi(double pi) {

        return -1 / (pi * pi);
    }

    /**
     * Second partial derivative with respect to pi & tau.
     *
     * @return
     */
    private static double gammaOPiTau() {

        return 0;
    }

    /**
     * First partial derivative with respect to tau.
     *
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaOTau(double tau) {

        double out = 0;

        for (double[] jno : Jno) {
            out += jno[1] * jno[0] * pow(tau, jno[0] - 1);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to tau.
     *
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaOTauTau(double tau) {

        double out = 0;

        for (double[] jno : Jno) {
            out += jno[1] * jno[0] * (jno[0] - 1) * pow(tau, jno[0] - 2);
        }
        return out;
    }

    /**
     * Dimensionless Gibbs free energy.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaR(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * pow(tau, ijnr[1]);
        }
        return out;
    }

    /**
     * First partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaRPi(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * pow(pi, ijnr[0] - 1) * pow(tau, ijnr[1]);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaRPiPi(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * (ijnr[0] - 1) * pow(pi, ijnr[0] - 2) * pow(tau, ijnr[1]);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to pi & tau.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaRPiTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * pow(pi, ijnr[0] - 1) * ijnr[1] * pow(tau, ijnr[1] - 1);
        }
        return out;
    }

    /**
     * First partial derivative with respect to tau.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaRTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * ijnr[1] * pow(tau, ijnr[1] - 1);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to tau.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private static double gammaRTauTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * ijnr[1] * (ijnr[1] - 1) * pow(tau, ijnr[1] - 2);
        }
        return out;
    }

    @Override
    String getName() {

        return NAME;
    }

    private SubRegion getSubRegion(double pressure, double enthalpy) {

        return pressure > 4 ? (enthalpy < enthalpy2bc(pressure) ? SubRegion.C : SubRegion.B) : SubRegion.A;
    }

    @Override
    double isobaricCubicExpansionCoefficientPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (1 - tau * pi * gammaRPiTau(pi, tau) / (1 + pi * gammaRPi(pi, tau))) / temperature;
    }

    @Override
    double isothermalCompressibilityPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (1 - pi * pi * gammaRPiPi(pi, tau)) / (1 + pi * gammaRPi(pi, tau)) / pressure;
    }

    @Override
    double specificEnthalpyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return tau * (gammaOTau(tau) + gammaRTau(pi, tau)) * R * temperature;
    }

    @Override
    double specificEntropyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (tau * (gammaOTau(tau) + gammaRTau(pi, tau)) - (gammaO(pi, tau) + gammaR(pi, tau))) * R;
    }

    @Override
    double specificInternalEnergyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (tau * (gammaOTau(tau) + gammaRTau(pi, tau)) - pi * (gammaOPi(pi) + gammaRPi(pi, tau))) * R * temperature;
    }

    @Override
    double specificIsobaricHeatCapacityPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return -tau * tau * (gammaOTauTau(tau) + gammaRTauTau(pi, tau)) * R;
    }

    @Override
    double specificIsochoricHeatCapacityPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature,
                x = 1 + pi * gammaRPi(pi, tau) - tau * pi * gammaRPiTau(pi, tau);

        return (-tau * tau * (gammaOTauTau(tau) + gammaRTauTau(pi, tau)) - x * x / (1 - pi * pi * gammaRPiPi(pi, tau))) * R;
    }

    @Override
    double specificVolumePT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return pi * (gammaOPi(pi) + gammaRPi(pi, tau)) / 1e3 * R * temperature / pressure;
    }

    @Override
    double speedOfSoundPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature,
                gRPi = gammaRPi(pi, tau),
                x = 1 + pi * gRPi - tau * pi * gammaRPiTau(pi, tau);

        return sqrt((1 + 2 * pi * gRPi + pi * pi * gRPi * gRPi) / (1 - pi * pi * gammaRPiPi(pi, tau) + x * x / (tau * tau * (gammaOTauTau(tau) + gammaRTauTau(pi, tau)))) * 1e3 * R * temperature);
    }

    @Override
    double temperaturePH(double pressure, double enthalpy) {

        double eta = enthalpy / 2000;

        switch (getSubRegion(pressure, enthalpy)) {
            case A:
                return thetaA(pressure, eta);
            case B:
                return thetaB(pressure, eta);
            case C:
                return thetaC(pressure, eta);
        }
        return Double.NaN;
    }

    /**
     * Dimensionless backward equation for region 2a.
     *
     * @param pi dimensionless pressure
     * @param eta dimensionless enthalpy
     * @return
     */
    private double thetaA(double pi, double eta) {

        double out = 0;

        for (double[] ijnA : IJnA) {
            out += ijnA[2] * pow(pi, ijnA[0]) * pow(eta - 2.1, ijnA[1]);
        }
        return out;
    }

    /**
     * Dimensionless backward equation for region 2b.
     *
     * @param pi dimensionless pressure
     * @param eta dimensionless enthalpy
     * @return
     */
    private double thetaB(double pi, double eta) {

        double out = 0;

        for (double[] ijnB : IJnB) {
            out += ijnB[2] * pow(pi - 2, ijnB[0]) * pow(eta - 2.6, ijnB[1]);
        }
        return out;
    }

    /**
     * Dimensionless backward equation for region 2c.
     *
     * @param pi dimensionless pressure
     * @param eta dimensionless enthalpy
     * @return
     */
    private double thetaC(double pi, double eta) {

        double out = 0;

        for (double[] ijnC : IJnC) {
            out += ijnC[2] * pow(pi + 25, ijnC[0]) * pow(eta - 1.8, ijnC[1]);
        }
        return out;
    }

    @Override
    double pressureHS(double h, double s) {

        throw new UnsupportedOperationException("Region5.pressureHS() pending implementation. Contact Hummeling Engineering BV for assistance: www.hummeling.com.");
    }

    @Override
    double specificEntropyRhoT(double rho, double T) {

        throw new UnsupportedOperationException("Region5.specificEntropyRhoT() pending implementation. Contact Hummeling Engineering BV for assistance: www.hummeling.com.");
    }

    @Override
    double temperatureHS(double h, double s) {

        throw new UnsupportedOperationException("Region5.temperatureHS() pending implementation. Contact Hummeling Engineering BV for assistance: www.hummeling.com.");
    }

    @Override
    double temperaturePS(double p, double s) {

        throw new UnsupportedOperationException("Region5.temperaturePS() pending implementation. Contact Hummeling Engineering BV for assistance: www.hummeling.com.");
    }

    @Override
    double vapourFractionHS(double enthalpy, double entropy) {

        return 1;
    }

    @Override
    double vapourFractionPH(double pressure, double enthalpy) {

        return 1;
    }

    @Override
    double vapourFractionPS(double pressure, double entropy) {

        return 1;
    }

    @Override
    double vapourFractionTS(double temperature, double entropy) {

        return 1;
    }

    enum SubRegion {

        A, B, C;
    }
}
