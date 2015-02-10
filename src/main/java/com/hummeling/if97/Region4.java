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
 * Copyright 2009-2015 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

//import static com.hummeling.if97.OutOfRangeException.Quantity.TEMPERATURE;
import static java.lang.Math.*;

/**
 * Region 4.
 *
 * Note that methods as a function of pressure & temperature are relayed to
 * region 1. However, for these arguments (p, T) this region is never selected.
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
final class Region4 extends Region {

    private static final String NAME;
    static final double Tref, pRef;
    static final double[] n;

    static {
        NAME = "Region 4";
        Tref = 1;
        pRef = 1;
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
    }

    static void checkP(double pressure) throws OutOfRangeException {

        if (pressure < IF97.p0) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, IF97.p0);

        } else if (pressure > IF97.pc) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, IF97.pc);
        }
    }

    static void checkT(double temperature) throws OutOfRangeException {

        if (temperature < IF97.T0) {
            throw new OutOfRangeException(IF97.Quantity.T, temperature, IF97.T0);

        } else if (temperature > IF97.Tc) {
            throw new OutOfRangeException(IF97.Quantity.T, temperature, IF97.Tc);
        }
    }

    static void checkX(double vapourFraction) throws OutOfRangeException {

        if (vapourFraction < 0) {
            throw new OutOfRangeException(IF97.Quantity.x, vapourFraction, 0);

        } else if (vapourFraction > 1) {
            throw new OutOfRangeException(IF97.Quantity.x, vapourFraction, 1);
        }

    }

    @Override
    String getName() {

        return NAME;
    }

    @Override
    double isobaricCubicExpansionCoefficientPT(double p, double T) {

        return new Region1().isobaricCubicExpansionCoefficientPT(p, T);
    }

    @Override
    double isothermalCompressibilityPT(double p, double T) {

        return new Region1().isothermalCompressibilityPT(p, T);
    }

    /**
     * Saturation pressure.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return saturation pressure [MPa]
     * @throws OutOfRangeException
     */
    @Override
    double pressureHS(double enthalpy, double entropy) {
        try {
            return saturationPressureT(temperatureHS(enthalpy, entropy));
        } catch (OutOfRangeException ex) {
            return Double.NaN;
        }
    }

    /**
     * Boundary saturation pressure for the boundary between regions 3 and 4.
     *
     * @param enthalpy specific enthalpy
     * @return saturation pressure
     */
    static double saturationPressureH(double enthalpy) {

        double eta = enthalpy / 2600, out = 0;
        double[][] IJn = {
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

        for (double[] ijn : IJn) {
            out += ijn[2] * pow(eta - 1.02, ijn[0]) * pow(eta - 0.608, ijn[1]);
        }
        return out * 22;
    }

    /**
     * Boundary saturation pressure for the boundary between regions 3 and 4.
     *
     * @param entropy specific entropy
     * @return saturation pressure
     */
    static double saturationPressureS(double entropy) { //TODO saturationPressureS throw OutOfRangeException

        double sigma = entropy / 5.2, pi = 0;
        double[] x = {sigma - 1.03, sigma - 0.699};
        double[][] IJn = {
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

        for (double[] ijn : IJn) {
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
    static double saturationPressureT(double saturationTemperature) {

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
    static double saturationTemperatureP(double saturationPressure) {

        double beta = pow(saturationPressure / pRef, 0.25),
                beta2 = beta * beta,
                E = beta2 + n[2] * beta + n[5],
                F = n[0] * beta2 + n[3] * beta + n[6],
                G = n[1] * beta2 + n[4] * beta + n[7],
                D = 2 * G / (-F - sqrt(F * F - 4 * E * G)),
                n9plusD = n[9] + D;

        return (n9plusD - sqrt(n9plusD * n9plusD - 4 * (n[8] + n[9] * D))) / 2 * Tref;
    }

    @Override
    double specificEnthalpyPT(double p, double T) {

        return new Region1().specificEnthalpyPT(p, T);
    }

    /**
     * Specific enthalpy as a function of pressure & vapour fraction.
     *
     * @param p pressure [MPa]
     * @param x vapour fraction [-]
     * @return specific enthalpy [kJ/kg]
     * @throws OutOfRangeException out-of-range exception
     */
    static double specificEnthalpyPX(double p, double x) throws OutOfRangeException {

        checkP(p);
        checkX(x);

        double Tsat = saturationTemperatureP(p),
                h1 = new Region1().specificEnthalpyPT(p, Tsat),
                h2 = new Region2().specificEnthalpyPT(p, Tsat);

        return h1 + (h2 - h1) * x;
    }

    /**
     * Specific enthalpy as a function of temperature & vapour fraction.
     *
     * @param T temperature [K]
     * @param x vapour fraction [-]
     * @return specific enthalpy [kJ/kg]
     * @throws OutOfRangeException out-of-range exception
     */
    static double specificEnthalpyTX(double T, double x) throws OutOfRangeException {

        checkT(T);
        checkX(x);

        double pSat = saturationPressureT(T),
                h1 = new Region1().specificEnthalpyPT(pSat, T),
                h2 = new Region2().specificEnthalpyPT(pSat, T);

        return h1 + (h2 - h1) * x;
    }

    /**
     * Specific entropy as a function of pressure & specific enthalpy.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return specific entropy [kJ/(kg K)]
     * @throws OutOfRangeException out-of-range exception
     */
    static double specificEntropyPH(double p, double h) throws OutOfRangeException {

        return specificEntropyPX(p, new Region4().vapourFractionPH(p, h));
    }

    @Override
    double specificEntropyPT(double p, double T) {

        return Double.NaN;
    }

    /**
     * Specific entropy as a function of pressure & vapour fraction.
     *
     * @param p pressure [MPa]
     * @param x vapour fraction [-]
     * @return specific entropy [kJ/(kg K)]
     * @throws OutOfRangeException out-of-range exception
     */
    static double specificEntropyPX(double p, double x) throws OutOfRangeException {

        checkP(p);
        checkX(x);

        double Tsat = saturationTemperatureP(p),
                s1 = new Region1().specificEntropyPT(p, Tsat),
                s2 = new Region2().specificEntropyPT(p, Tsat);

        return s1 + (s2 - s1) * x;
    }

    @Override
    double specificEntropyRhoT(double rho, double T) {

        throw new UnsupportedOperationException("Region4.specificEntropyRhoT() pending implementation. Contact Hummeling Engineering BV for assistance: www.hummeling.com.");
    }

    /**
     * Specific entropy as a function of temperature & vapour fraction.
     *
     * @param T temperature [K]
     * @param x vapour fraction [-]
     * @return specific entropy [kJ/(kg K)]
     */
    static double specificEntropyTX(double T, double x) throws OutOfRangeException {

        checkT(T);
        checkX(x);

        double pSat = saturationPressureT(T),
                s1 = new Region1().specificEntropyPT(pSat, T),
                s2 = new Region2().specificEntropyPT(pSat, T);

        return s1 + (s2 - s1) * x;
    }

    @Override
    double specificInternalEnergyPT(double p, double T) {

        return new Region1().specificInternalEnergyPT(p, T);
    }

    @Override
    double specificIsobaricHeatCapacityPT(double p, double T) {

        return new Region1().specificIsobaricHeatCapacityPT(p, T);
    }

    @Override
    double specificIsochoricHeatCapacityPT(double p, double T) {

        return new Region1().specificIsochoricHeatCapacityPT(p, T);
    }

    @Override
    double specificVolumePT(double p, double T) {

        return new Region1().specificVolumePT(p, T);
    }

    static double specificVolumePX(double p, double x) throws OutOfRangeException {//TODO Check specificVolumePX

        checkP(p);
        checkX(x);

        double Tsat = saturationTemperatureP(p),
                v1 = new Region1().specificVolumePT(p, Tsat),
                v2 = new Region2().specificVolumePT(p, Tsat);

        return v1 + (v2 - v1) * x;
    }

    static double specificVolumeTX(double T, double x) throws OutOfRangeException {//TODO Check specificVolumeTX

        checkT(T);
        checkX(x);

        double pSat = saturationPressureT(T),
                v1 = new Region1().specificVolumePT(pSat, T),
                v2 = new Region2().specificVolumePT(pSat, T);

        return v1 + (v2 - v1) * x;
    }

    @Override
    double speedOfSoundPT(double p, double T) {

        return new Region1().speedOfSoundPT(p, T);
    }

    /**
     * Surface tension as a function of temperature.
     *
     * @param T temperature [K]
     * @return surface tension [N/m]
     * @throws OutOfRangeException out-of-range exception
     */
    static double surfaceTensionT(double T) throws OutOfRangeException {

        checkT(T);

        double theta = T / IF97.Tc;

        return 235.8 * pow(1 - theta, 1.256) * (1 - 0.625 * (1 - theta)) * 1e-3;
    }

    /**
     * Saturation temperature as a function of specific enthalpy & specific
     * entropy. [IF97 Supplementary Release S04]
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return saturation temperature
     */
    @Override
    double temperatureHS(double enthalpy, double entropy) {

        double theta = 0, eta = enthalpy / 2800, sigma = entropy / 9.2;
        double[] x = {eta - 0.119, sigma - 1.07};
        double[][] IJn = {
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

        for (double[] ijn : IJn) {
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
     * Vapour fraction as a function of specific enthalpy & specific entropy.
     * [IF97 Supplementary Release S04]
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return vapour fraction [-]
     */
    @Override
    double vapourFractionHS(double enthalpy, double entropy) {

        double Tsat = temperatureHS(enthalpy, entropy),
                pSat = saturationPressureT(Tsat),
                h1 = new Region1().specificEnthalpyPT(pSat, Tsat),
                h2 = new Region2().specificEnthalpyPT(pSat, Tsat);

        return min(1, (enthalpy - h1) / (h2 - h1));
    }

    @Override
    double vapourFractionPH(double pressure, double enthalpy) throws OutOfRangeException {

        checkP(pressure);

        if (pressure < saturationPressureT(IF97.T0)) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, saturationPressureT(IF97.T0));
        }

        double Tsat = saturationTemperatureP(pressure),
                h1 = new Region1().specificEnthalpyPT(pressure, Tsat),
                h2 = new Region2().specificEnthalpyPT(pressure, Tsat);

        return min(1, (enthalpy - h1) / (h2 - h1));
    }

    @Override
    double vapourFractionPS(double pressure, double entropy) {

        checkP(pressure);

        double Tsat = saturationTemperatureP(pressure),
                s1 = new Region1().specificEntropyPT(pressure, Tsat),
                s2 = new Region2().specificEntropyPT(pressure, Tsat);

        return min(1, (entropy - s1) / (s2 - s1));
    }

    @Override
    double vapourFractionTS(double temperature, double entropy) throws OutOfRangeException {

        checkT(temperature);

        double pSat = Region4.saturationPressureT(temperature),
                s1 = new Region1().specificEntropyPT(pSat, temperature),
                s2 = new Region2().specificEntropyPT(pSat, temperature);

        return min(1, (entropy - s1) / (s2 - s1));
    }
}
