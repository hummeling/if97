/*
 * Region1.java
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
 * Copyright 2009-2018 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static com.hummeling.if97.IF97.*;
import static java.lang.StrictMath.*;

/**
 * Region 1.
 *
 * @author Ralph Hummeling
 * (<a href="https://www.hummeling.com">www.hummeling.com</a>)
 */
public final class Region1 extends Region {

    private final double Tref, pRef;
    private final double[][] IJnPT, IJnHS, IJnPH, IJnPS;

    Region1() {

        super("Region 1");

        Tref = 1386;
        pRef = 16.53;
        IJnPT = new double[][]{
            {0, -2, +0.14632971213167},
            {0, -1, -0.84548187169114},
            {0, 00, -0.37563603672040e1},
            {0, 01, +0.33855169168385e1},
            {0, 02, -0.95791963387872},
            {0, 03, +0.15772038513228},
            {0, 04, -0.16616417199501e-1},
            {0, 05, +0.81214629983568e-3},
            {1, -9, +0.28319080123804e-3},
            {1, -7, -0.60706301565874e-3},
            {1, -1, -0.18990068218419e-1},
            {1, 00, -0.32529748770505e-1},
            {1, 01, -0.21841717175414e-1},
            {1, 03, -0.52838357969930e-4},
            {2, -3, -0.47184321073267e-3},
            {2, 00, -0.30001780793026e-3},
            {2, 01, +0.47661393906987e-4},
            {2, 03, -0.44141845330846e-5},
            {2, 17, -0.72694996297594e-15},
            {3, -4, -0.31679644845054e-4},
            {3, 00, -0.28270797985312e-5},
            {3, 06, -0.85205128120103e-9},
            {4, -5, -0.22425281908000e-5},
            {4, -2, -0.65171222895601e-6},
            {4, 10, -0.14341729937924e-12},
            {5, -8, -0.40516996860117e-6},
            {8, -11, -0.12734301741641e-8},
            {8, -06, -0.17424871230634e-9},
            {21, -29, -0.68762131295531e-18},
            {23, -31, +0.14478307828521e-19},
            {29, -38, +0.26335781662795e-22},
            {30, -39, -0.11947622640071e-22},
            {31, -40, +0.18228094581404e-23},
            {32, -41, -0.93537087292458e-25}};
        IJnHS = new double[][]{
            {0, 0, -.691997014660582},
            {0, 1, -.183612548787560e2},
            {0, 2, -.928332409297335e1},
            {0, 4, .659639569909906e2},
            {0, 5, -.162060388912024e2},
            {0, 6, .450620017338667e3},
            {0, 8, .854680678224170e3},
            {0, 14, .607523214001162e4},
            {1, 0, .326487682621856e2},
            {1, 1, -.269408844582931e2},
            {1, 4, -.319947848334300e3},
            {1, 6, -.928354307043320e3},
            {2, 0, .303634537455249e2},
            {2, 1, -.650540422444146e2},
            {2, 10, -.430991316516130e4},
            {3, 4, -.747512324096068e3},
            {4, 1, .730000345529245e3},
            {4, 4, .114284032569021e4},
            {5, 0, -.436407041874559e3}};
        IJnPH = new double[][]{
            {0, 00, -.23872489924521e3},
            {0, 01, 0.40421188637945e3},
            {0, 02, 0.11349746881718e3},
            {0, 06, -.58457616048039e1},
            {0, 22, -.15285482413140e-3},
            {0, 32, -.10866707695377e-5},
            {1, 00, -.13391744872602e2},
            {1, 01, 0.43211039183559e2},
            {1, 02, -.54010067170506e2},
            {1, 03, 0.30535892203916e2},
            {1, 04, -.65964749423638e1},
            {1, 10, 0.93965400878363e-2},
            {1, 32, 0.11573647505340e-6},
            {2, 10, -.25858641282073e-4},
            {2, 32, -.40644363084799e-8},
            {3, 10, 0.66456186191635e-7},
            {3, 32, 0.80670734103027e-10},
            {4, 32, -.93477771213947e-12},
            {5, 32, 0.58265442020601e-14},
            {6, 32, -.15020185953503e-16}};
        IJnPS = new double[][]{
            {0, 0, 0.17478268058307e3},
            {0, 1, 0.34806930892873e2},
            {0, 2, 0.65292584978455e1},
            {0, 3, 0.33039981775489},
            {0, 11, -0.19281382923196e-6},
            {0, 31, -0.24909197244573e-22},
            {1, 0, -0.26107636489332},
            {1, 1, 0.22592965981586},
            {1, 2, -0.64256463395226e-1},
            {1, 3, 0.78876289270526e-2},
            {1, 12, 0.35672110607366e-9},
            {1, 31, 0.17332496994895e-23},
            {2, 0, 0.56608900654837e-3},
            {2, 1, -0.32635483139717e-3},
            {2, 2, 0.44778286690632e-4},
            {2, 9, -0.51322156908507e-9},
            {2, 31, -0.42522657042207e-25},
            {3, 10, 0.26400441360689e-12},
            {3, 32, 0.78124600459723e-28},
            {4, 32, -0.30732199903668e-30}};
    }

    /**
     * Dimensionless Gibbs free energy.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return gamma
     */
    private double gamma(double pi, double tau) {

        double out = 0;
        double[] x = {7.1 - pi, tau - 1.222};

        for (double[] ijn : IJnPT) {
            out += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return out;
    }

    /**
     * First partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return d/dpi gamma
     */
    private double gammaPi(double pi, double tau) {

        double out = 0;
        double[] x = {7.1 - pi, tau - 1.222};

        for (double[] ijn : IJnPT) {
            out -= ijn[2] * ijn[0] * pow(x[0], ijn[0] - 1) * pow(x[1], ijn[1]);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return d2/dpi2 gamma
     */
    private double gammaPiPi(double pi, double tau) {

        double out = 0;
        double[] x = {7.1 - pi, tau - 1.222};

        for (double[] ijn : IJnPT) {
            out += ijn[2] * ijn[0] * (ijn[0] - 1) * pow(x[0], ijn[0] - 2) * pow(x[1], ijn[1]);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to pi & tau.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return d/dpi d/dtau gamma
     */
    private double gammaPiTau(double pi, double tau) {

        double out = 0;
        double[] x = {7.1 - pi, tau - 1.222};

        for (double[] ijn : IJnPT) {
            out -= ijn[2] * ijn[0] * pow(x[0], ijn[0] - 1) * ijn[1] * pow(x[1], ijn[1] - 1);
        }
        return out;
    }

    /**
     * First partial derivative with respect to tau.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return d/dtau gamma
     */
    private double gammaTau(double pi, double tau) {

        double out = 0;
        double[] x = {7.1 - pi, tau - 1.222};

        for (double[] ijn : IJnPT) {
            out += ijn[2] * pow(x[0], ijn[0]) * ijn[1] * pow(x[1], ijn[1] - 1);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to tau.
     *
     * @param pi dimensionless pressure [MPa]
     * @param tau dimensionless temperature [K]
     * @return d2/dtau2 gamma
     */
    private double gammaTauTau(double pi, double tau) {

        double out = 0;
        double[] x = {7.1 - pi, tau - 1.222};

        for (double[] ijn : IJnPT) {
            out += ijn[2] * pow(x[0], ijn[0]) * ijn[1] * (ijn[1] - 1) * pow(x[1], ijn[1] - 2);
        }
        return out;
    }

    @Override
    double isentropicExponentPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature,
                x = gammaPi(pi, tau) - tau * gammaPiTau(pi, tau);

        return 1 / (1 - x * x / (tau * tau * gammaPiPi(pi, tau) * gammaTauTau(pi, tau)));
    }

    @Override
    double isobaricCubicExpansionCoefficientPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (1 - tau * gammaPiTau(pi, tau) / gammaPi(pi, tau)) / temperature;
    }

    @Override
    double isothermalCompressibilityPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return -pi * gammaPiPi(pi, tau) / gammaPi(pi, tau) / pressure;
    }

    @Override
    double pressureHS(double enthalpy, double entropy) {

        double pi = 0;
        double[] x = {enthalpy / 3400 + 0.05, entropy / 7.6 + 0.05};

        for (double[] ijn : IJnHS) {
            pi += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return pi * 100;
    }

    @Override
    double specificEnthalpyPT(double pressure, double temperature) {

        double tau = Tref / temperature;

        return tau * gammaTau(pressure / pRef, tau) * R * temperature;
    }

    @Override
    double specificEntropyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (tau * gammaTau(pi, tau) - gamma(pi, tau)) * R;
    }

    @Override
    double specificEntropyRhoT(double rho, double T) {

        throw new UnsupportedOperationException("Region1.specificEntropyRhoT() pending implementation. Contact Hummeling Engineering BV for assistance: www.hummeling.com.");
    }

    @Override
    double specificGibbsFreeEnergyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return gamma(pi, tau) * R * temperature;
    }

    @Override
    double specificInternalEnergyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (tau * gammaTau(pi, tau) - pi * gammaPi(pi, tau)) * R * temperature;
    }

    @Override
    double specificIsobaricHeatCapacityPT(double pressure, double temperature) {

        double tau = Tref / temperature;

        return -tau * tau * gammaTauTau(pressure / pRef, tau) * R;
    }

    @Override
    double specificIsochoricHeatCapacityPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature,
                x = gammaPi(pi, tau) - tau * gammaPiTau(pi, tau);

        return (-tau * tau * gammaTauTau(pi, tau) + x * x / gammaPiPi(pi, tau)) * R;
    }

    @Override
    double specificVolumePT(double pressure, double temperature) {

        double pi = pressure / pRef;

        return pi * gammaPi(pi, Tref / temperature) / 1e3 * R * temperature / pressure;
    }

    @Override
    double speedOfSoundPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature,
                gPi = gammaPi(pi, tau),
                x = gPi - tau * gammaPiTau(pi, tau);

        return sqrt((gPi * gPi / (x * x / (tau * tau * gammaTauTau(pi, tau)) - gammaPiPi(pi, tau))) * 1e3 * R * temperature);
    }

    @Override
    double temperatureHS(double enthalpy, double entropy) {

        return temperaturePH(pressureHS(enthalpy, entropy), enthalpy);
    }

    @Override
    double temperaturePH(double pressure, double enthalpy) {

        double out = 0, x = enthalpy / 2500 + 1;

        for (double[] ijn : IJnPH) {
            out += ijn[2] * pow(pressure, ijn[0]) * pow(x, ijn[1]);
        }
        return out;
    }

    @Override
    double temperaturePS(double pressure, double entropy) {

        double out = 0;

        for (double[] ijn : IJnPS) {
            out += ijn[2] * pow(pressure, ijn[0]) * pow(entropy + 2, ijn[1]);
        }
        return out;
    }

    @Override
    double vapourFractionHS(double enthalpy, double entropy) {

        return 0;
    }

    @Override
    double vapourFractionPH(double pressure, double enthalpy) {

        return 0;
    }

    @Override
    double vapourFractionPS(double pressure, double entropy) {

        return 0;
    }

    @Override
    double vapourFractionTS(double temperature, double entropy) {

        return 0;
    }
}
