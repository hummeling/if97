/*
 * Region4.java
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

import static com.hummeling.if97.IF97.*;
import static java.lang.StrictMath.*;

/**
 * Region 4.
 *
 * Note that for these arguments (p, T) this region is never selected.
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
final class Region4 extends Region {

    private final int ITERATION_LIMIT;
    private final double Tref, pRef, TOLERANCE;
    private final double[] n;
    private final double[][] IJnH, IJnS, IJnHS;

    Region4() {

        super("Region 4");

        Tref = 1;
        pRef = 1;
        ITERATION_LIMIT = 100;
        TOLERANCE = 1e-9;
        n = new double[]{
            00.11670521452767e4,
            -0.72421316703206e6,
            -0.17073846940092e2,
            00.12020824702470e5,
            -0.32325550322333e7,
            00.14915108613530e2,
            -0.48232657361591e4,
            00.40511340542057e6,
            -0.23855557567849,
            00.65017534844798e3};
        IJnH = new double[][]{
            {0, 0, 0.600073641753024},
            {1, 1, -.936203654849857e1},
            {1, 3, 0.246590798594147e2},
            {1, 4, -.107014222858224e3},
            {1, 36, -.915821315805768e14},
            {5, 03, -.862332011700662e4},
            {7, 00, -.235837344740032e2},
            {8, 24, 0.252304969384128e18},
            {14, 16, -.389718771997719e19},
            {20, 16, -.333775713645296e23},
            {22, 03, 0.356499469636328e11},
            {24, 18, -.148547544720641e27},
            {28, 8, 0.330611514838798e19},
            {36, 24, 0.813641294467829e38}};
        IJnS = new double[][]{
            {0, 0, .639767553612785},
            {1, 1, -.129727445396014e2},
            {1, 32, -.224595125848403e16},
            {4, 7, .177466741801846e7},
            {12, 4, .717079349571538e10},
            {12, 14, -.378829107169011e18},
            {16, 36, -.955586736431328e35},
            {24, 10, .187269814676188e24},
            {28, 0, .119254746466473e12},
            {32, 18, .110649277244882e37}};
        IJnHS = new double[][]{
            {0, 0, .179882673606601},
            {0, 3, -.267507455199603},
            {0, 12, .116276722612600e1},
            {1, 0, .147545428713616},
            {1, 1, -.512871635973248},
            {1, 2, .421333567697984},
            {1, 5, .563749522189870},
            {2, 0, .429274443819153},
            {2, 5, -.335704552142140e1},
            {2, 8, .108890916499278e2},
            {3, 0, -.248483390456012},
            {3, 2, .304153221906390},
            {3, 3, -.494819763939905},
            {3, 4, .107551674933261e1},
            {4, 0, .733888415457688e-1},
            {4, 1, .140170545411085e-1},
            {5, 1, -.106110975998808},
            {5, 2, .168324361811875e-1},
            {5, 4, .125028363714877e1},
            {5, 16, .101316840309509e4},
            {6, 6, -.151791558000712e1},
            {6, 8, .524277865990866e2},
            {6, 22, .230495545563912e5},
            {8, 1, .249459806365456e-1},
            {10, 20, .210796467412137e7},
            {10, 36, .366836848613065e9},
            {12, 24, -.144814105365163e9},
            {14, 1, -.179276373003590e-2},
            {14, 28, .489955602100459e10},
            {16, 12, .471262212070518e3},
            {16, 32, -.829294390198652e11},
            {18, 14, -.171545662263191e4},
            {18, 22, .355777682973575e7},
            {18, 36, .586062760258436e12},
            {20, 24, -.129887635078195e8},
            {28, 36, .317247449371057e11}};
    }

    void checkB34H(double enthalpy) throws OutOfRangeException {

        if (enthalpy < hs13) {
            throw new OutOfRangeException(Quantity.h, enthalpy, hs13);

        } else if (enthalpy > hs23) {
            throw new OutOfRangeException(Quantity.h, enthalpy, hs23);
        }
    }

    void checkB34S(double entropy) throws OutOfRangeException {

        if (entropy < ss13) {
            throw new OutOfRangeException(Quantity.s, entropy, ss13);

        } else if (entropy > ss23) {
            throw new OutOfRangeException(Quantity.s, entropy, ss23);
        }
    }

    void checkHS(double enthalpy, double entropy) throws OutOfRangeException {

        if (entropy < ss23) {
            throw new OutOfRangeException(Quantity.s, entropy, ss23);

        } else if (entropy > s2) {
            throw new OutOfRangeException(Quantity.s, entropy, s2);

        } else if (entropy <= IF97.sc) {
            double h3a = specificEnthalpy3a(entropy);

            if (enthalpy > h3a) {
                throw new OutOfRangeException(Quantity.h, enthalpy, h3a);
            }
        } else if (entropy < s2bc) {
            double h2c3b = specificEnthalpy2c3b(entropy);

            if (enthalpy > h2c3b) {
                throw new OutOfRangeException(Quantity.h, enthalpy, h2c3b);
            }
        } else {
            double h2ab = specificEnthalpy2ab(entropy);

            if (enthalpy > h2ab) {
                throw new OutOfRangeException(Quantity.h, enthalpy, h2ab);
            }
        }
    }

    void checkP(double pressure) throws OutOfRangeException {

        if (pressure < p0) {
            throw new OutOfRangeException(Quantity.p, pressure, p0);

        } else if (pressure > pc) {
            throw new OutOfRangeException(Quantity.p, pressure, pc);
        }
    }

    void checkT(double temperature) throws OutOfRangeException {

        if (temperature < T0) {
            throw new OutOfRangeException(Quantity.T, temperature, T0);

        } else if (temperature > Tc) {
            throw new OutOfRangeException(Quantity.T, temperature, Tc);
        }
    }

    void checkX(double vapourFraction) throws OutOfRangeException {

        if (vapourFraction < 0) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 0);

        } else if (vapourFraction > 1) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 1);
        }

    }

    @Override
    double isobaricCubicExpansionCoefficientPT(double pressure, double temperature) {

        return Double.NaN;
    }

    @Override
    double isothermalCompressibilityPT(double pressure, double temperature) {

        return Double.NaN;
    }

    /**
     * [IF97 Supplementary Release S04, June 2014]
     *
     * @param enthalpy specific enthalpy [kJ/kg]
     * @param entropy specific entropy [kJ/kg-K]
     * @return saturation pressure [MPa]
     */
    @Override
    double pressureHS(double enthalpy, double entropy) {

        double T = temperatureHS(enthalpy, entropy);

        return saturationPressureT(T);
    }

    /**
     * Boundary saturation pressure for the boundary between regions 3 and 4.
     *
     * @param enthalpy specific enthalpy [kJ/kg]
     * @return saturation pressure [MPa]
     * @throws OutOfRangeException out-of-range exception
     */
    double saturationPressureB34H(double enthalpy) throws OutOfRangeException {

        checkB34H(enthalpy);

        double eta = enthalpy / 2600, out = 0;

        for (double[] ijn : IJnH) {
            out += ijn[2] * pow(eta - 1.02, ijn[0]) * pow(eta - 0.608, ijn[1]);
        }
        return out * 22;
    }

    /**
     * Boundary saturation pressure for the boundary between regions 3 and 4,
     * only!
     *
     * @param entropy specific entropy [kJ/(kg K)]
     * @return saturation pressure [MPa]
     * @throws OutOfRangeException out-of-range exception
     */
    double saturationPressureB34S(double entropy) throws OutOfRangeException {

        checkB34S(entropy);

        double sigma = entropy / 5.2, pi = 0;
        double[] x = {sigma - 1.03, sigma - 0.699};

        for (double[] ijn : IJnS) {
            pi += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return pi * 22;
    }

    /**
     * Saturation pressure.
     *
     * Out-of-range exceptions not thrown because this method is also used for
     * finding regions.
     *
     * @param saturationTemperature saturation temperature [K]
     * @return saturation pressure [MPa]
     */
    double saturationPressureT(double saturationTemperature) {

        double Ts_Tref = saturationTemperature / Tref,
                theta = Ts_Tref + n[8] / (Ts_Tref - n[9]),
                theta2 = theta * theta,
                A = theta2 + n[0] * theta + n[1],
                B = n[2] * theta2 + n[3] * theta + n[4],
                C = n[5] * theta2 + n[6] * theta + n[7];

        return pow(2 * C / (-B + sqrt(B * B - 4 * A * C)), 4) * pRef;
    }

    /**
     * Saturation temperature.
     *
     * Out-of-range exceptions not thrown because this method is also used for
     * finding regions.
     *
     * @param saturationPressure saturation pressure [MPa]
     * @return saturation temperature [K]
     */
    double saturationTemperatureP(double saturationPressure) {

        double beta = pow(saturationPressure / pRef, 0.25),
                beta2 = beta * beta,
                E = beta2 + n[2] * beta + n[5],
                F = n[0] * beta2 + n[3] * beta + n[6],
                G = n[1] * beta2 + n[4] * beta + n[7],
                D = 2 * G / (-F - sqrt(F * F - 4 * E * G)),
                n9plusD = n[9] + D;

        return (n9plusD - sqrt(n9plusD * n9plusD - 4 * (n[8] + n[9] * D))) / 2 * Tref;
    }

    /**
     * Method copied from [Numerical Recipes, 2007].
     *
     * @param a
     * @param b
     * @return
     */
    private double sign(double a, double b) {

        return b >= 0 ^ a >= 0 ? -a : a; // simlified using XOR
    }

    @Override
    double specificEnthalpyPS(double pressure, double entropy) {

        double x = vapourFractionPS(pressure, entropy);

        return specificEnthalpyPX(pressure, x);
    }

    @Override
    double specificEnthalpyPT(double pressure, double temperature) {

        return Double.NaN;
    }

    /**
     * Specific enthalpy as a function of pressure & vapour fraction.
     *
     * @param pressure pressure [MPa]
     * @param vapourFraction vapour fraction [-]
     * @return specific enthalpy [kJ/kg]
     */
    double specificEnthalpyPX(double pressure, double vapourFraction) {

        double h1 = specificEnthalpySaturatedLiquidP(pressure),
                h2 = specificEnthalpySaturatedVapourP(pressure);

        return h1 + (h2 - h1) * vapourFraction;
    }

    /**
     * Specific enthalpy saturated liquid.
     *
     * For pressure &gt; 16.5292 MPa the answer is obtained by iteration using
     * Ridders' root finding algorithm [Numerical Recipes, 3rd ed, 2007].
     *
     * @param pressure absolute pressure [MPa]
     * @return specific enthalpy [kJ/kg]
     */
    double specificEnthalpySaturatedLiquidP(double pressure) {

        if (pressure > ps13) {
            if (pressure == pc) {
                return hc;
            }
            double[] h = {hs13, hc, Double.NaN, Double.NaN},
                    dp = {ps13 - pressure, pc - pressure, Double.NaN, Double.NaN};

            for (int i = 0; i < ITERATION_LIMIT; i++) {
                h[2] = (h[0] + h[1]) / 2;
                dp[2] = saturationPressureB34H(h[2]) - pressure;
                h[3] = h[2] + (h[2] - h[0]) * signum(dp[0] - dp[1]) * dp[2] / sqrt(dp[2] * dp[2] - dp[0] * dp[1]);
                dp[3] = saturationPressureB34H(h[3]) - pressure;

                if (dp[3] < 0) {
                    h[0] = h[3];
                    h[1] = h[2];
                    dp[0] = dp[3];
                    dp[1] = dp[2];
                } else {
                    h[0] = h[2];
                    h[1] = h[3];
                    dp[0] = dp[2];
                    dp[1] = dp[3];
                }
                if (abs(dp[3]) < TOLERANCE) {
                    break;
                }
            }
            return h[3];

        } else {
            double Ts = saturationTemperatureP(pressure);

            return REGION1.specificEnthalpyPT(pressure, Ts);
        }
    }

    /**
     * Specific enthalpy saturated vapour.
     *
     * For pressure &gt; 16.5292 MPa the answer is obtained by iteration using
     * Ridders' root finding algorithm [Numerical Recipes, 3rd ed, 2007].
     *
     * @param pressure absolute pressure [MPa]
     * @return specific enthalpy [kJ/kg]
     */
    double specificEnthalpySaturatedVapourP(double pressure) {

        if (pressure > ps13) {
            if (pressure == pc) {
                return hc;
            }
            double[] h = {hc, hs23, Double.NaN, Double.NaN},
                    p = {pc - pressure, ps13 - pressure, Double.NaN, Double.NaN};

            for (int i = 0; i < ITERATION_LIMIT; i++) {
                h[2] = (h[0] + h[1]) / 2;
                p[2] = saturationPressureB34H(h[2]) - pressure;
                h[3] = h[2] + (h[2] - h[0]) * signum(p[0] - p[1]) * p[2] / sqrt(p[2] * p[2] - p[0] * p[1]);
                p[3] = saturationPressureB34H(h[3]) - pressure;

                if (p[3] < 0) {
                    h[0] = h[3];
                    h[1] = h[2];
                    p[0] = p[3];
                    p[1] = p[2];
                } else {
                    h[0] = h[2];
                    h[1] = h[3];
                    p[0] = p[2];
                    p[1] = p[3];
                }
                if (abs(p[3]) < TOLERANCE) {
                    break;
                }
            }
            return h[3];

        } else {
            double Ts = saturationTemperatureP(pressure);

            return REGION2.specificEnthalpyPT(pressure, Ts);
        }
    }

    /**
     * Specific entropy as a function of pressure & specific enthalpy.
     *
     * @param pressure pressure [MPa]
     * @param enthalpy specific enthalpy [kJ/kg]
     * @return specific entropy [kJ/(kg K)]
     */
    double specificEntropyPH(double pressure, double enthalpy) {

        double x = vapourFractionPH(pressure, enthalpy);

        return specificEntropyPX(pressure, x);
    }

    @Override
    double specificEntropyPT(double pressure, double temperature) {

        return Double.NaN;
    }

    /**
     * Specific entropy as a function of pressure & vapour fraction.
     *
     * @param pressure pressure [MPa]
     * @param vapourFraction vapour fraction [-]
     * @return specific entropy [kJ/(kg K)]
     */
    double specificEntropyPX(double pressure, double vapourFraction) {

        double s1 = specificEntropySaturatedLiquidP(pressure),
                s2 = specificEntropySaturatedVapourP(pressure);

        return s1 + (s2 - s1) * vapourFraction;
    }

    @Override
    double specificEntropyRhoT(double density, double temperature) {

        throw new UnsupportedOperationException("Region4.specificEntropyRhoT() pending implementation. Contact Hummeling Engineering BV for assistance: www.hummeling.com.");
    }

    double specificEntropySaturatedLiquidP(double pressure) {

        double Ts = saturationTemperatureP(pressure);

        if (pressure > ps13) {
            double v = specificVolumeSaturatedLiquidP(pressure);

            return REGION3.specificEntropyRhoT(1 / v, Ts);

        } else {
            return REGION1.specificEntropyPT(pressure, Ts);
        }
    }

    double specificEntropySaturatedVapourP(double pressure) {

        double Ts = saturationTemperatureP(pressure);

        if (pressure > ps13) {
            double v = specificVolumeSaturatedVapourP(pressure);

            return REGION3.specificEntropyRhoT(1 / v, Ts);

        } else {

            return REGION2.specificEntropyPT(pressure, Ts);
        }
    }

    @Override
    double specificGibbsFreeEnergyPT(double pressure, double temperature) {

        return Double.NaN;
    }

    @Override
    double specificInternalEnergyPT(double pressure, double temperature) {

        return Double.NaN;
    }

    @Override
    double specificIsobaricHeatCapacityPT(double pressure, double temperature) {

        return Double.NaN;
    }

    @Override
    double specificIsochoricHeatCapacityPT(double pressure, double temperature) {

        return Double.NaN;
    }

    @Override
    double specificVolumeHS(double enthalpy, double entropy) {

        double p = pressureHS(enthalpy, entropy),
                x = vapourFractionHS(enthalpy, entropy);

        return specificVolumePX(p, x);
    }

    @Override
    double specificVolumePH(double pressure, double enthalpy) {

        double x = vapourFractionPH(pressure, enthalpy);

        return specificVolumePX(pressure, x);
    }

    @Override
    double specificVolumePS(double pressure, double entropy) {

        double x = vapourFractionPS(pressure, entropy);

        return specificVolumePX(pressure, x);
    }

    @Override
    double specificVolumePT(double pressure, double temperature) {

        return Double.NaN;
    }

    double specificVolumePX(double pressure, double vapourFraction) {

        double v1 = specificVolumeSaturatedLiquidP(pressure),
                v2 = specificVolumeSaturatedVapourP(pressure);

        return v1 + (v2 - v1) * vapourFraction;
    }

    /**
     * Specific volume saturated liquid.
     *
     * For pressure &gt; 16.5292 MPa the answer is obtained by iteration using
     * Van Wijngaarden/Dekker/Brent root finding algorithm [Numerical Recipes,
     * 3rd ed, 2007].
     *
     * @param pressure absolute pressure [MPa]
     * @return specific volume [m&sup3;/kg]
     */
    double specificVolumeSaturatedLiquidP(double pressure) {

        double Ts = saturationTemperatureP(pressure);

        if (pressure > ps13) {
            if (pressure == pc) {
                return 1 / rhoc;
            }
            double[] v = {REGION1.specificVolumePT(ps13, T13), 1 / rhoc, Double.NaN, Double.NaN, Double.NaN},
                    dp = {REGION3.pressureRhoT(1 / v[0], Ts) - pressure, REGION3.pressureRhoT(1 / v[1], Ts) - pressure, Double.NaN};

            v[2] = v[1];
            dp[2] = dp[1];

            for (int i = 0; i < ITERATION_LIMIT; i++) {
                if (dp[1] * dp[2] > 0) {
                    v[2] = v[0];
                    dp[2] = dp[0];
                    v[4] = v[3] = v[1] - v[0];
                }
                if (abs(dp[2]) < abs(dp[1])) {
                    v[0] = v[1];
                    v[1] = v[2];
                    v[2] = v[0];
                    dp[0] = dp[1];
                    dp[1] = dp[2];
                    dp[2] = dp[0];
                }
                double tol1 = 2 * Double.MIN_VALUE * abs(v[1]) + TOLERANCE / 2,
                        xm = (v[2] - v[1]) / 2;

                if (abs(xm) <= tol1 || dp[1] == 0) {
                    break;
                }
                if (abs(v[4]) >= tol1 && abs(dp[0]) > abs(dp[1])) {
                    double s = dp[1] / dp[0], p, q, r;

                    if (v[0] == v[2]) {
                        p = 2 * xm * s;
                        q = 1 - s;
                    } else {
                        q = dp[0] / dp[2];
                        r = dp[1] / dp[2];
                        p = s * (2 * xm * q * (q - r) - (v[1] - v[0]) * (r - 1));
                        q = (q - 1) * (r - 1) * (s - 1);
                    }
                    if (p > 0) {
                        q = -q;
                    }
                    p = abs(p);
                    double min1 = 3 * xm * q - abs(tol1 * q),
                            min2 = abs(v[4] * q);

                    if (2 * p < min(min1, min2)) {
                        v[4] = v[3];
                        v[3] = p / q;
                    } else {
                        v[3] = xm;
                        v[4] = v[3];
                    }
                } else {
                    v[3] = xm;
                    v[4] = v[3];
                }
                v[0] = v[1];
                dp[0] = dp[1];
                v[1] += abs(v[3]) > tol1 ? v[3] : sign(tol1, xm);
                dp[1] = REGION3.pressureRhoT(1 / v[1], Ts) - pressure;
            }
            return v[1];

        } else {
            return REGION1.specificVolumePT(pressure, Ts);
        }
    }

    /**
     * Specific volume saturated vapour.
     *
     * For pressure &gt; 16.5292 MPa the answer is obtained by iteration using
     * Van Wijngaarden/Dekker/Brent root finding algorithm [Numerical Recipes,
     * 3rd ed, 2007].
     *
     * @param pressure absolute pressure [MPa]
     * @return specific volume [m&sup3;/kg]
     */
    double specificVolumeSaturatedVapourP(double pressure) {

        double Ts = saturationTemperatureP(pressure);

        if (pressure > ps13) {
            if (pressure == pc) {
                return 1 / rhoc;
            }
            double[] v = {Double.NaN, REGION2.specificVolumePT(ps13, T13), Double.NaN, Double.NaN, Double.NaN},
                    dp = {Double.NaN, REGION3.pressureRhoT(1 / v[1], Ts) - pressure, Double.NaN};

            /*
             Bracket Root
             */
            for (int i = 0; i < 1000; i++) {
                v[0] = v[1] - i * 0.001;
                dp[0] = REGION3.pressureRhoT(1 / v[0], Ts) - pressure;

                if (dp[0] * dp[1] < 0) {
                    break;
                }
            }
            v[2] = v[1];
            dp[2] = dp[1];

            for (int i = 0; i < ITERATION_LIMIT; i++) {
                if (dp[1] * dp[2] > 0) {
                    v[2] = v[0];
                    dp[2] = dp[0];
                    v[4] = v[3] = v[1] - v[0];
                }
                if (abs(dp[2]) < abs(dp[1])) {
                    v[0] = v[1];
                    v[1] = v[2];
                    v[2] = v[0];
                    dp[0] = dp[1];
                    dp[1] = dp[2];
                    dp[2] = dp[0];
                }
                double tol1 = 2 * Double.MIN_VALUE * abs(v[1]) + TOLERANCE / 2,
                        xm = (v[2] - v[1]) / 2;

                if (abs(xm) <= tol1 || dp[1] == 0) {
                    break;
                }
                if (abs(v[4]) >= tol1 && abs(dp[0]) > abs(dp[1])) {
                    double s = dp[1] / dp[0], p, q, r;

                    if (v[0] == v[2]) {
                        p = 2 * xm * s;
                        q = 1 - s;
                    } else {
                        q = dp[0] / dp[2];
                        r = dp[1] / dp[2];
                        p = s * (2 * xm * q * (q - r) - (v[1] - v[0]) * (r - 1));
                        q = (q - 1) * (r - 1) * (s - 1);
                    }
                    if (p > 0) {
                        q = -q;
                    }
                    p = abs(p);
                    double min1 = 3 * xm * q - abs(tol1 * q),
                            min2 = abs(v[4] * q);

                    if (2 * p < min(min1, min2)) {
                        v[4] = v[3];
                        v[3] = p / q;
                    } else {
                        v[3] = xm;
                        v[4] = v[3];
                    }
                } else {
                    v[3] = xm;
                    v[4] = v[3];
                }
                v[0] = v[1];
                dp[0] = dp[1];
                v[1] += abs(v[3]) > tol1 ? v[3] : sign(tol1, xm);
                dp[1] = REGION3.pressureRhoT(1 / v[1], Ts) - pressure;
            }
            return v[1];

        } else {
            return REGION2.specificVolumePT(pressure, Ts);
        }
    }

    @Override
    double speedOfSoundPT(double pressure, double temperature) {

        return Double.NaN;
    }

    /**
     * Surface tension as a function of temperature.
     *
     * @param temperature temperature [K]
     * @return surface tension [N/m]
     */
    double surfaceTensionT(double temperature) {

        double theta = temperature / Tc;

        return 235.8 * pow(1 - theta, 1.256) * (1 - 0.625 * (1 - theta)) * 1e-3;
    }

    /**
     * [IF97 Supplementary Release S04, June 2014]
     *
     * @param enthalpy specific enthalpy [kJ/kg]
     * @param entropy specific entropy [kJ/(kg K)]
     * @return saturation temperature [K]
     */
    @Override
    double temperatureHS(double enthalpy, double entropy) {

        double theta = 0, eta = enthalpy / 2800, sigma = entropy / 9.2;
        double[] x = {eta - 0.119, sigma - 1.07};

        for (double[] ijn : IJnHS) {
            theta += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }

        return theta * 550;
    }

    @Override
    double temperaturePH(double pressure, double dummy) {

        return saturationTemperatureP(pressure);
    }

    @Override
    double temperaturePS(double pressure, double dummy) {

        return saturationTemperatureP(pressure);
    }

    /**
     * [IF97 Supplementary Release S04, June 2014]
     *
     * Note that region 3 is not used.
     *
     * @param enthalpy specific enthalpy [kJ/kg]
     * @param entropy specific entropy [kJ/(kg K)]
     * @return vapour fraction [-]
     */
    @Override
    double vapourFractionHS(double enthalpy, double entropy) {

        //checkHS(enthalpy, entropy);
        double Ts = temperatureHS(enthalpy, entropy),
                ps = saturationPressureT(Ts),
                h1 = REGION1.specificEnthalpyPT(ps, Ts),
                h2 = REGION2.specificEnthalpyPT(ps, Ts);

        return max(0, min(1, (enthalpy - h1) / (h2 - h1)));
    }

    @Override
    double vapourFractionPH(double pressure, double enthalpy) {

        double h1 = specificEnthalpySaturatedLiquidP(pressure),
                h2 = specificEnthalpySaturatedVapourP(pressure);

        return max(0, min(1, (enthalpy - h1) / (h2 - h1)));
    }

    @Override
    double vapourFractionPS(double pressure, double entropy) {

        double s1 = specificEntropySaturatedLiquidP(pressure),
                s2 = specificEntropySaturatedVapourP(pressure);

        return max(0, min(1, (entropy - s1) / (s2 - s1)));
    }

    @Override
    double vapourFractionTS(double temperature, double entropy) {

        double ps = saturationPressureT(temperature);

        return vapourFractionPS(ps, entropy);
    }
}
