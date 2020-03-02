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
 * along with IF97. If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2020 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static com.hummeling.if97.IF97.*;
import static java.lang.StrictMath.*;

/**
 * Region 5.
 *
 * @author Ralph Hummeling
 * (<a href="https://www.hummeling.com">www.hummeling.com</a>)
 */
final class Region5 extends Region {

    final double Tref, pRef;
    final double[][] Jno, IJnr;

    Region5() {

        super("Region 5");

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
    }

    private double enthalpy2bc(double pressure) {

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
     * @param pi dimensionless pressure
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaO(double pi, double tau) {

        double out = log(pi);

        for (double[] jno : Jno) {
            out += jno[1] * pow(tau, jno[0]);
        }
        return out;
    }

    /**
     * First partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure
     * @return
     */
    private double gammaOPi(double pi) {
        return 1 / pi;
    }

    /**
     * Second partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure
     * @return
     */
    private double gammaOPiPi(double pi) {
        return -1 / (pi * pi);
    }

    /**
     * Second partial derivative with respect to pi & tau.
     *
     * @return
     */
    private double gammaOPiTau() {
        return 0;
    }

    /**
     * First partial derivative with respect to tau.
     *
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaOTau(double tau) {

        double out = 0;

        for (double[] jno : Jno) {
            out += jno[1] * jno[0] * pow(tau, jno[0] - 1);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to tau.
     *
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaOTauTau(double tau) {

        double out = 0;

        for (double[] jno : Jno) {
            out += jno[1] * jno[0] * (jno[0] - 1) * pow(tau, jno[0] - 2);
        }
        return out;
    }

    /**
     * Dimensionless Gibbs free energy.
     *
     * @param pi dimensionless pressure
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaR(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * pow(tau, ijnr[1]);
        }
        return out;
    }

    /**
     * First partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaRPi(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * pow(pi, ijnr[0] - 1) * pow(tau, ijnr[1]);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaRPiPi(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * (ijnr[0] - 1) * pow(pi, ijnr[0] - 2) * pow(tau, ijnr[1]);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to pi & tau.
     *
     * @param pi dimensionless pressure
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaRPiTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * pow(pi, ijnr[0] - 1) * ijnr[1] * pow(tau, ijnr[1] - 1);
        }
        return out;
    }

    /**
     * First partial derivative with respect to tau.
     *
     * @param pi dimensionless pressure
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaRTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * ijnr[1] * pow(tau, ijnr[1] - 1);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to tau.
     *
     * @param pi dimensionless pressure
     * @param tau dimensionless temperature
     * @return
     */
    private double gammaRTauTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * ijnr[1] * (ijnr[1] - 1) * pow(tau, ijnr[1] - 2);
        }
        return out;
    }

    private SubRegion getSubRegion(double pressure, double enthalpy) {
        return pressure > 4 ? (enthalpy < enthalpy2bc(pressure) ? SubRegion.C : SubRegion.B) : SubRegion.A;
    }

    @Override
    double isentropicExponentPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature,
                x = 1 + pi * gammaRPi(pi, tau) - tau * pi * gammaRPiTau(pi, tau);

        return 1 / (1 + x * x / (tau * tau * (gammaOTauTau(tau) + gammaRTauTau(pi, tau)) * (1 - pi * pi * gammaRPiPi(pi, tau))));
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
    double specificGibbsFreeEnergyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (gammaO(pi, tau) + gammaR(pi, tau)) * R * temperature;
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
        return Double.NaN;
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

    enum SubRegion {
        A, B, C;
    }
}
