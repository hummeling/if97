/*
 * Region2.java
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

import static com.hummeling.if97.IF97.*;
import static java.lang.Math.*;

/**
 * Region 2.
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
class Region2 extends Region {

    private static final String NAME;
    static final double Tref, pRef;
    private static final double[][] Jno, IJnr;

    static {
        NAME = "Region 2";
        Tref = 540;
        pRef = 1;
        Jno = new double[][]{
            {0, -00.96927686500217e1},
            {1, 000.10086655968018e2},
            {-5, -0.56087911283020e-2},
            {-4, 00.71452738081455e-1},
            {-3, -0.40710498223928},
            {-2, 00.14240819171444e1},
            {-1, -0.43839511319450e1},
            {2, -00.28408632460772},
            {3, 000.21268463753307e-1}};
        IJnr = new double[][]{
            {1, 00, -1.7731742473213e-3},
            {1, 01, -1.7834862292358e-2},
            {1, 02, -4.5996013696365e-2},
            {1, 03, -5.7581259083432e-2},
            {1, 06, -5.0325278727930e-2},
            {2, 01, -3.3032641670203e-5},
            {2, 02, -1.8948987516315e-4},
            {2, 04, -3.9392777243355e-3},
            {2, 07, -4.3797295650573e-2},
            {2, 36, -2.6674547914087e-5},
            {3, 00, 2.0481737692309e-8},
            {3, 01, 4.3870667284435e-7},
            {3, 03, -3.2277677238570e-5},
            {3, 06, -1.5033924542148e-3},
            {3, 35, -4.0668253562649e-2},
            {4, 01, -7.8847309559367e-10},
            {4, 02, 1.2790717852285e-8},
            {4, 03, 4.8225372718507e-7},
            {5, 07, 2.2922076337661e-6},
            {6, 03, -1.6714766451061e-11},
            {6, 16, -2.1171472321355e-3},
            {6, 35, -2.3895741934104e1},
            {7, 00, -5.9059564324270e-18},
            {7, 11, -1.2621808899101e-6},
            {7, 25, -3.8946842435739e-2},
            {8, 8, 1.1256211360459e-11},
            {8, 36, -8.2311340897998},
            {9, 13, 1.9809712802088e-8},
            {10, 04, 1.0406965210174e-19},
            {10, 10, -1.0234747095929e-13},
            {10, 14, -1.0018179379511e-9},
            {16, 29, -8.0882908646985e-11},
            {16, 50, 1.0693031879409e-1},
            {18, 57, -3.3662250574171e-1},
            {20, 20, 8.9185845355421e-25},
            {20, 35, 3.0629316876232e-13},
            {20, 48, -4.2002467698208e-6},
            {21, 21, -5.9056029685639e-26},
            {22, 53, 3.7826947613457e-6},
            {23, 39, -1.2768608934681e-15},
            {24, 26, 7.3087610595061e-29},
            {24, 40, 5.5414715350778e-17},
            {24, 58, -9.4369707241210e-7}};
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
     * @return gamma
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
     * @return d/dpi gamma
     */
    private static double gammaOPi(double pi) {

        return 1 / pi;
    }

    /**
     * Second partial derivative with respect to pi.
     *
     * @param pi dimensionless pressure [MPa]
     * @return d2/dpi2 gamma
     */
    private static double gammaOPiPi(double pi) {

        return -1 / (pi * pi);
    }

    /**
     * Second partial derivative with respect to pi & tau.
     *
     * @return d/dpi d/dtau gamma
     */
    private static double gammaOPiTau() {

        return 0;
    }

    /**
     * First partial derivative with respect to tau.
     *
     * @param tau dimensionless temperature [K]
     * @return d/dtau gamma
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
     * @return d2/dtau2 gamma
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
    private static double gammaRPi(double pi, double tau) {

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
    private static double gammaRPiPi(double pi, double tau) {

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
    private static double gammaRPiTau(double pi, double tau) {

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
    private static double gammaRTau(double pi, double tau) {

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
    private static double gammaRTauTau(double pi, double tau) {

        double out = 0;

        for (double[] ijnr : IJnr) {
            out += ijnr[2] * pow(pi, ijnr[0]) * ijnr[1] * (ijnr[1] - 1) * pow(tau - 0.5, ijnr[1] - 2);
        }
        return out;
    }

    @Override
    String getName() {

        return NAME;
    }

    private SubRegion getSubRegionPH(double pressure, double enthalpy) {

        return pressure > 4 ? (enthalpy < enthalpy2bc(pressure) ? SubRegion.C : SubRegion.B) : SubRegion.A;
    }

    private SubRegion getSubRegionPS(double pressure, double entropy) {

        return pressure > 4 ? (entropy < 5.85 ? SubRegion.C : SubRegion.B) : SubRegion.A;
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
    double pressureHS(double enthalpy, double entropy) {

        double[] x = null,
                n = {
                    -.349898083432139e4,
                    0.257560716905876e4,
                    -.421073558227969e3,
                    0.276349063799944e2};
        double pi = 0, sigma = entropy / 1,
                eta = n[0] + (n[1] + (n[2] + n[3] * sigma) * sigma) * sigma,
                h2ab = eta * 1;

        //System.out.println("h2ab: " + h2ab);
        double[][] thisIJn = null;

        if (enthalpy > h2ab) {
            if (entropy < 5.85) {
                // region 2c
                x = new double[]{enthalpy / 3500 - 0.7, entropy / 5.9 - 1.1, 100};
                thisIJn = new double[][]{
                    {0, 0, .112225607199012e0},
                    {0, 1, -.339005953606712e1},
                    {0, 2, -.320503911730094e2},
                    {0, 3, -.197597305104900e3},
                    {0, 4, -.407693861553446e3},
                    {0, 8, .132943775222331e5},
                    {1, 0, .170846839774007e1},
                    {1, 2, .373694198142245e2},
                    {1, 5, .358144365815434e4},
                    {1, 8, .423014446424664e6},
                    {1, 14, -.751071025760063e9},
                    {2, 2, .523446127607898e2},
                    {2, 3, -.228351290812417e3},
                    {2, 7, -.960652417056937e6},
                    {2, 10, -.807059292526074e8},
                    {2, 18, .162698017225669e13},
                    {3, 0, .772465073604171e0},
                    {3, 5, .463929973837746e5},
                    {3, 8, -.137317885134128e8},
                    {3, 16, .170470392630512e13},
                    {3, 18, -.251104628187308e14},
                    {4, 18, .317748830835520e14},
                    {5, 1, .538685623675312e2},
                    {5, 4, -.553089094625169e5},
                    {5, 6, -.102861522421405e7},
                    {5, 14, .204249418756234e13},
                    {6, 8, .273918446626977e9},
                    {6, 18, -.263963146312685e16},
                    {10, 7, -.107890854108088e10},
                    {12, 7, -.296492620980124e11},
                    {16, 10, -.111754907323424e16}};

            } else {
                // region 2b
                x = new double[]{enthalpy / 4100 - 0.6, entropy / 7.9 - 1.01, 100};
                thisIJn = new double[][]{
                    {0, 0, .801496989929495e-1},
                    {0, 1, -.543862807146111e0},
                    {0, 2, .337455597421283e0},
                    {0, 4, .890555451157450e1},
                    {0, 8, .313840736431485e3},
                    {1, 0, .797367065977789e0},
                    {1, 1, -.121616973556240e1},
                    {1, 2, .872803386937477e1},
                    {1, 3, -.169769781757602e2},
                    {1, 5, -.186552827328416e3},
                    {1, 12, .951159274344237e5},
                    {2, 1, -.189168510120494e2},
                    {2, 6, -.433407037194840e4},
                    {2, 18, .543212633012715e9},
                    {3, 0, .144793408386013e0},
                    {3, 1, .128024559637516e3},
                    {3, 7, -.672309534071268e5},
                    {3, 12, .336972380095287e8},
                    {4, 1, -.586634196762720e3},
                    {4, 16, -.221403224769889e11},
                    {5, 1, .171606668708389e4},
                    {5, 12, -.570817595806302e9},
                    {6, 1, -.312109693178482e4},
                    {6, 8, -.207841384633010e7},
                    {6, 18, .305605946157786e13},
                    {7, 1, .322157004314333e4},
                    {7, 16, .326810259797295e12},
                    {8, 1, -.144104158934487e4},
                    {8, 3, .410694867802691e3},
                    {8, 14, .109077066873024e12},
                    {8, 18, -.247964654258893e14},
                    {12, 10, .188801906865134e10},
                    {14, 16, -.123651009018773e15}};
            }

        } else {
            // region 2a
            x = new double[]{enthalpy / 4200 - 0.5, entropy / 12 - 1.2, 4};
            thisIJn = new double[][]{
                {0, 1, -.182575361923032e-1},
                {0, 3, -.125229548799536},
                {0, 6, 0.592290437320145},
                {0, 16, 0.604769706185122e1},
                {0, 20, 0.238624965444474e3},
                {0, 22, -.298639090222922e3},
                {1, 0, 0.512250813040750e-1},
                {1, 1, -.437266515606486},
                {1, 2, 0.413336902999504},
                {1, 3, -.516468254574773e1},
                {1, 5, -.557014838445711e1},
                {1, 6, 0.128555037824478e2},
                {1, 10, 0.114144108953290e2},
                {1, 16, -.119504225652714e3},
                {1, 20, -.284777985961560e4},
                {1, 22, 0.431757846408006e4},
                {2, 3, 0.112894040802650e1},
                {2, 16, 0.197409186206319e4},
                {2, 20, 0.151612444706087e4},
                {3, 0, 0.141324451421235e-1},
                {3, 2, 0.585501282219601},
                {3, 3, -.297258075863012e1},
                {3, 6, 0.594567314847319e1},
                {3, 16, -.623656565798905e4},
                {4, 16, 0.965986235133332e4},
                {5, 3, 0.681500934948134e1},
                {5, 16, -.633207286824489e4},
                {6, 3, -.558919224465760e1},
                {7, 1, 0.400645798472063e-1}};
        }

        for (double[] ijn : thisIJn) {
            pi += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return pow(pi, 4) * x[2];
    }

    @Override
    double specificEnthalpyPT(double pressure, double temperature) {

        double tau = Tref / temperature;

        return tau * (gammaOTau(tau) + gammaRTau(pressure / pRef, tau)) * R * temperature;
    }

    @Override
    double specificEntropyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (tau * (gammaOTau(tau) + gammaRTau(pi, tau)) - (gammaO(pi, tau) + gammaR(pi, tau))) * R;
    }

    @Override
    double specificEntropyRhoT(double rho, double T) {

        throw new UnsupportedOperationException("Region2.specificEntropyRhoT() pending implementation. Contact Hummeling Engineering BV for assistance: www.hummeling.com.");
    }

    @Override
    double specificInternalEnergyPT(double pressure, double temperature) {

        double pi = pressure / pRef,
                tau = Tref / temperature;

        return (tau * (gammaOTau(tau) + gammaRTau(pi, tau)) - pi * (gammaOPi(pi) + gammaRPi(pi, tau))) * R * temperature;
    }

    @Override
    double specificIsobaricHeatCapacityPT(double pressure, double temperature) {

        double tau = Tref / temperature;

        return -tau * tau * (gammaOTauTau(tau) + gammaRTauTau(pressure / pRef, tau)) * R;
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

        double pi = pressure / pRef;

        return pi * (gammaOPi(pi) + gammaRPi(pi, Tref / temperature)) / 1e3 * R * temperature / pressure;
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
    double temperatureHS(double enthalpy, double entropy) {

        return temperaturePH(pressureHS(enthalpy, entropy), enthalpy);
    }

    @Override
    double temperaturePH(double pressure, double enthalpy) {

        double eta = enthalpy / 2000;

        switch (getSubRegionPH(pressure, enthalpy)) {
            case A:
                return theta2aPH(pressure, eta);

            case B:
                return theta2bPH(pressure, eta);

            case C:
                return theta2cPH(pressure, eta);

            default:
                return Double.NaN;
        }
    }

    @Override
    double temperaturePS(double pressure, double entropy) {

        switch (getSubRegionPS(pressure, entropy)) {
            case A:
                return theta2aPS(pressure, entropy / 2);

            case B:
                return theta2bPS(pressure, entropy / 0.7853);

            case C:
                return theta2cPS(pressure, entropy / 2.9251);

            default:
                return Double.NaN;
        }
    }

    /**
     * Dimensionless backward equation for region 2a.
     *
     * @param pi dimensionless pressure
     * @param eta dimensionless enthalpy
     * @return
     */
    private double theta2aPH(double pi, double eta) {

        double out = 0;
        double[][] IJn = {
            {0, 0, 1.0898952318288e3},
            {0, 1, 8.4951654495535e2},
            {0, 2, -1.0781748091826e2},
            {0, 3, 3.3153654801263e1},
            {0, 7, -7.4232016790248},
            {0, 20, 1.1765048724356e1},
            {1, 0, 1.8445749355790},
            {1, 1, -4.1792700549624},
            {1, 2, 6.2478196935812},
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
            {4, 12, 1.3865724283226},
            {4, 32, 2.3598832556514e5},
            {4, 44, -1.3105236545054e7},
            {5, 32, 7.3999835474766e3},
            {5, 36, -5.5196697030060e5},
            {5, 42, 3.7154085996233e6},
            {6, 34, 1.9127729239660e4},
            {6, 44, -4.1535164835634e5},
            {7, 28, -6.2459855192507e1}};

        for (double[] ijn : IJn) {
            out += ijn[2] * pow(pi, ijn[0]) * pow(eta - 2.1, ijn[1]);
        }
        return out;
    }

    /**
     * Dimensionless backward equation for region 2a.
     *
     * @param pi dimensionless pressure
     * @param sigma dimensionless entropy
     * @return
     */
    private double theta2aPS(double pi, double sigma) {

        double out = 0;
        double[][] IJn = {
            {-1.5, -24, -0.39235983861984e6},
            {-1.5, -23, 0.51526573827270e6},
            {-1.5, -19, 0.40482443161048e5},
            {-1.5, -13, -0.32193790923902e3},
            {-1.5, -11, 0.96961424218694e2},
            {-1.5, -10, -0.22867846371773e2},
            {-1.25, -19, -0.44942914124357e6},
            {-1.25, -15, -0.50118336020166e4},
            {-1.25, -6, 0.35684463560015},
            {-1.0, -26, 0.44235335848190e5},
            {-1.0, -21, -0.13673388811708e5},
            {-1.0, -17, 0.42163260207864e6},
            {-1.0, -16, 0.22516925837475e5},
            {-1.0, -9, 0.47442144865646e3},
            {-1.0, -8, -0.14931130797647e3},
            {-0.75, -15, -0.19781126320452e6},
            {-0.75, -14, -0.23554399470760e5},
            {-0.5, -26, -0.19070616302076e5},
            {-0.5, -13, 0.55375669883164e5},
            {-0.5, -9, 0.38293691437363e4},
            {-0.5, -7, -0.60391860580567e3},
            {-0.25, -27, 0.19363102620331e4},
            {-0.25, -25, 0.42660643698610e4},
            {-0.25, -11, -0.59780638872718e4},
            {-0.25, -6, -0.70401463926862e3},
            {0.25, 1, 0.33836784107553e3},
            {0.25, 4, 0.20862786635187e2},
            {0.25, 8, 0.33834172656196e-1},
            {0.25, 11, -0.43124428414893e-4},
            {0.5, 0, 0.16653791356412e3},
            {0.5, 1, -0.13986292055898e3},
            {0.5, 5, -0.78849547999872},
            {0.5, 6, 0.72132411753872e-1},
            {0.5, 10, -0.59754839398283e-2},
            {0.5, 14, -0.12141358953904e-4},
            {0.5, 16, 0.23227096733871e-6},
            {0.75, 0, -0.10538463566194e2},
            {0.75, 4, 0.20718925496502e1},
            {0.75, 9, -0.72193155260427e-1},
            {0.75, 17, 0.20749887081120e-6},
            {1, 7, -0.18340657911379e-1},
            {1, 18, 0.29036272348696e-6},
            {1.25, 3, 0.21037527893619},
            {1.25, 15, 0.25681239729999e-3},
            {1.5, 5, -0.12799002933781e-1},
            {1.5, 18, -0.82198102652018e-5}};

        for (double[] ijn : IJn) {
            out += ijn[2] * pow(pi, ijn[0]) * pow(sigma - 2, ijn[1]);
        }
        return out;
    }

    /**
     * Dimensionless backward equation for region 2b.
     *
     * @param pi dimensionless pressure [MPa]
     * @param eta dimensionless enthalpy [kJ/kg]
     * @return
     */
    private double theta2bPH(double pi, double eta) {

        double out = 0;
        double[][] IJn = {
            {0, 0, 1.4895041079516e3},
            {0, 1, 7.4307798314034e2},
            {0, 2, -9.7708318797837e1},
            {0, 12, 2.4742464705674},
            {0, 18, -6.3281320016026e-1},
            {0, 24, 1.1385952129658},
            {0, 28, -4.7811863648625e-1},
            {0, 40, 8.5208123431544e-3},
            {1, 0, 9.3747147377932e-1},
            {1, 2, 3.3593118604916},
            {1, 6, 3.3809355601454},
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

        for (double[] ijn : IJn) {
            out += ijn[2] * pow(pi - 2, ijn[0]) * pow(eta - 2.6, ijn[1]);
        }
        return out;
    }

    /**
     * Dimensionless backward equation for region 2b.
     *
     * @param pi dimensionless pressure
     * @param sigma dimensionless entropy
     * @return
     */
    private double theta2bPS(double pi, double sigma) {

        double out = 0;
        double[][] IJn = {
            {-6, 0, 0.31687665083497e6},
            {-6, 11, 0.20864175881858e2},
            {-5, 0, -0.39859399803599e6},
            {-5, 11, -0.21816058518877e2},
            {-4, 0, 0.22369785194242e6},
            {-4, 1, -0.27841703445817e4},
            {-4, 11, 0.99207436071480e1},
            {-3, 0, -0.75197512299157e5},
            {-3, 1, 0.29708605951158e4},
            {-3, 11, -0.34406878548526e1},
            {-3, 12, 0.38815564249115},
            {-2, 0, 0.17511295085750e5},
            {-2, 1, -0.14237112854449e4},
            {-2, 6, 0.10943803364167e1},
            {-2, 10, 0.89971619308495},
            {-1, 0, -0.33759740098958e4},
            {-1, 1, 0.47162885818355e3},
            {-1, 5, -0.19188241993679e1},
            {-1, 8, 0.41078580492196},
            {-1, 9, -0.33465378172097},
            {0, 0, 0.13870034777505e4},
            {0, 1, -0.40663326195838e3},
            {0, 2, 0.41727347159610e2},
            {0, 4, 0.21932549434532e1},
            {0, 5, -0.10320050009077e1},
            {0, 6, 0.35882943516703},
            {0, 9, 0.52511453726066e-2},
            {1, 0, 0.12838916450705e2},
            {1, 1, -0.28642437219381e1},
            {1, 2, 0.56912683664855},
            {1, 3, -0.99962954584931e-1},
            {1, 7, -0.32632037778459e-2},
            {1, 8, 0.23320922576723e-3},
            {2, 0, -0.15334809857450},
            {2, 1, 0.29072288239902e-1},
            {2, 5, 0.37534702741167e-3},
            {3, 0, 0.17296691702411e-2},
            {3, 1, -0.38556050844504e-3},
            {3, 3, -0.35017712292608e-4},
            {4, 0, -0.14566393631492e-4},
            {4, 1, 0.56420857267269e-5},
            {5, 0, 0.41286150074605e-7},
            {5, 1, -0.20684671118824e-7},
            {5, 2, 0.16409393674725e-8}};

        for (double[] ijn : IJn) {
            out += ijn[2] * pow(pi, ijn[0]) * pow(10 - sigma, ijn[1]);
        }
        return out;
    }

    /**
     * Dimensionless backward equation for region 2c.
     *
     * @param pi dimensionless pressure [MPa]
     * @param eta dimensionless enthalpy [kJ/kg]
     * @return
     */
    private double theta2cPH(double pi, double eta) {

        double out = 0;
        double[][] IJn = {
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
            {1, 4, 3.7966001272486},
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

        for (double[] ijn : IJn) {
            out += ijn[2] * pow(pi + 25, ijn[0]) * pow(eta - 1.8, ijn[1]);
        }
        return out;
    }

    /**
     * Dimensionless backward equation for region 2c.
     *
     * @param pi dimensionless pressure
     * @param sigma dimensionless entropy
     * @return
     */
    private double theta2cPS(double pi, double sigma) {

        double out = 0;
        double[][] IJn = {
            {-2, 0, 0.90968501005365e3},
            {-2, 1, 0.24045667088420e4},
            {-1, 0, -0.59162326387130e3},
            {0, 0, 0.54145404128074e3},
            {0, 1, -0.27098308411192e3},
            {0, 2, 0.97976525097926e3},
            {0, 3, -0.46966772959435e3},
            {1, 0, 0.14399274604723e2},
            {1, 1, -0.19104204230429e2},
            {1, 3, 0.53299167111971e1},
            {1, 4, -0.21252975375934e2},
            {2, 0, -0.31147334413760},
            {2, 1, 0.60334840894623},
            {2, 2, -0.42764839702509e-1},
            {3, 0, 0.58185597255259e-2},
            {3, 1, -0.14597008284753e-1},
            {3, 5, 0.56631175631027e-2},
            {4, 0, -0.76155864584577e-4},
            {4, 1, 0.22440342919332e-3},
            {4, 4, -0.12561095013413e-4},
            {5, 0, 0.63323132660934e-6},
            {5, 1, -0.20541989675375e-5},
            {5, 2, 0.36405370390082e-7},
            {6, 0, -0.29759897789215e-8},
            {6, 1, 0.10136618529763e-7},
            {7, 0, 0.59925719692351e-11},
            {7, 1, -0.20677870105164e-10},
            {7, 3, -0.20874278181886e-10},
            {7, 4, 0.10162166825089e-9},
            {7, 5, -0.16429828281347e-9}};

        for (double[] ijn : IJn) {
            out += ijn[2] * pow(pi, ijn[0]) * pow(2 - sigma, ijn[1]);
        }
        return out;
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
