/*
 * Region2Meta.java
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

import static com.hummeling.if97.IF97.*;
import static java.lang.StrictMath.*;

/**
 * Region 2 metastable-vapour.
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
@Deprecated
class Region2Meta extends Region2 {

    private final double[][] Jno, IJnr;

    Region2Meta() {

        super("Region 2 metastable-vapour");

        Jno = new double[][]{
            {0, -00.96937268393049e1},
            {1, 000.10087275970006e2},
            {-5, -0.56087911283020e-2},
            {-4, 00.71452738081455e-1},
            {-3, -0.40710498223928},
            {-2, 00.14240819171444e1},
            {-1, -0.43839511319450e1},
            {2, -00.28408632460772},
            {3, 000.21268463753307e-1}};
        IJnr = new double[][]{
            {1, 0, -0.73362260186506e-2},
            {1, 2, -0.88223831943146e-1},
            {1, 5, -0.72334555213245e-1},
            {1, 11, -.40813178534455e-2},
            {2, 1, 00.20097803380207e-2},
            {2, 7, -0.53045921898642e-1},
            {2, 16, -.76190409086970e-2},
            {3, 4, -0.63498037657313e-2},
            {3, 16, -.86043093028588e-1},
            {4, 7, 00.75321581522770e-2},
            {4, 10, -.79238375446139e-2},
            {5, 9, -0.22888160778447e-3},
            {5, 10, -.26456501482810e-2}};
    }

    /**
     * Dimensionless Gibbs free energy.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
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
     * @param pi dimensionless pressure [MPa]
     * @return
     */
    private double gammaOPi(double pi) {

        return 1 / pi;
    }

    /**
     * Second partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure [MPa]
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
     * @param tau dimensionless temperature [K]
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
     * @param tau dimensionless temperature [K]
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
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private double gammaR(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * pow(tau - 0.5, ijnr[1]);
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
    private double gammaRPi(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * pow(pi, ijnr[0] - 1) * pow(tau - 0.5, ijnr[1]);
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
    private double gammaRPiPi(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * (ijnr[0] - 1) * pow(pi, ijnr[0] - 2) * pow(tau - 0.5, ijnr[1]);
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
    private double gammaRPiTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * ijnr[0] * pow(pi, ijnr[0] - 1) * ijnr[1] * pow(tau - 0.5, ijnr[1] - 1);
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
    private double gammaRTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * ijnr[1] * pow(tau - 0.5, ijnr[1] - 1);
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
    private double gammaRTauTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * ijnr[1] * (ijnr[1] - 1) * pow(tau - 0.5, ijnr[1] - 2);
        }
        return out;
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
}
