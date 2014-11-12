/*
 * Region.java
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

import static com.hummeling.if97.Region4.*;
import static java.lang.Math.*;

/**
 * Abstract region class.
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
abstract class Region {

// <editor-fold defaultstate="collapsed" desc="fields">
    private static final String NAME = "Region";
    //final String NAME = null;
    private static final double[] nB23;
// </editor-fold>

    static {

        //double pSat = Region4.saturationPressureT(IF97.T0);
        //h1 = new double[]{
        //    new Region1().specificEnthalpyPT(pSat, IF97.T0),
        //    new Region1().specificEnthalpyPT(100, IF97.T0)};
        nB23 = new double[]{
            0.34805185628969e3,
            -.11671859879975e1,
            0.10192970039326e-2,
            0.57254459862746e3,
            0.13918839778870e2};
    }

    String getName() {

        return NAME;
    }

    /**
     * Get region as a function of specific enthalpy & specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return region
     * @throws OutOfRangeException out-of-range exception
     */
    static Region getRegionHS(double enthalpy, double entropy) throws OutOfRangeException {

        Region region1 = new Region1(), region2 = new Region2();
        Region3 region3 = new Region3();
        double[] hB23limits = {2.563592004e3, 2.812942061e3},
                sB23limits = {5.048096828, 5.260578707};

        /*
         Outer boundary Checks
         */
        double s1 = region1.specificEntropyPT(100, IF97.T0),
                s2 = region2.specificEntropyPT(100, 1073.15),
                pSat = Region4.saturationPressureT(IF97.T0);

        double[] h1 = {
            new Region1().specificEnthalpyPT(pSat, IF97.T0),
            new Region1().specificEnthalpyPT(100, IF97.T0)};

        if (enthalpy < h1[0]) {
            throw new OutOfRangeException(IF97.Quantity.h, enthalpy, h1[0]);

        } else if (entropy < 4.7516100567e-4) {
            double p1 = region1.pressureHS(enthalpy, entropy);

            if (region1.temperaturePH(p1, enthalpy) + 0.024 < IF97.T0) {
                throw new OutOfRangeException(IF97.Quantity.s, entropy, region1.specificEntropyPT(p1, IF97.T0));
            }

        }
        if (s1 <= entropy && entropy <= s2) {
            if (entropy <= region1.specificEntropyPT(100, 623.15)) {
                double h1Lim = region1.specificEnthalpyPT(100, region1.temperaturePS(100, entropy));

                if (enthalpy > h1Lim) {
                    throw new OutOfRangeException(IF97.Quantity.h, enthalpy, h1Lim);
                }

                //} else if (entropy <= IF97.sc) {
                //    double rho = 1 / region3.specificVolumePS(100, entropy),
                //            T = region3.temperaturePS(100, entropy),
                //            hLim = region3.specificEnthalpyRhoT(rho, T);
                //
                //    if (enthalpy > hLim) {
                //        throw new OutOfRangeException(IF97.Quantity.h, enthalpy, hLim);
                //    }
            } else if (entropy <= region2.specificEntropyPT(100, 863.15)) {
                double rho = 1 / region3.specificVolumePS(100, entropy),
                        T = region3.temperaturePS(100, entropy),
                        hLim = region3.specificEnthalpyRhoT(rho, T);

                if (enthalpy > hLim) {
                    throw new OutOfRangeException(IF97.Quantity.h, enthalpy, hLim);
                }

            } else { //TODO Finish getRegionHS boundary checks

            }
        }

        /*
         Select Region
         */
        if (entropy <= 3.778281340) {
            // region 1, 3, or 4
            if (enthalpy <= specificEnthalpy1(entropy)) {
                return new Region4();

            } else if (enthalpy > specificEnthalpyB13(entropy)) {
                return region3;

            } else {
                return new Region1();
            }

        } else if (entropy <= IF97.sc) {
            // region 3 or 4
            return enthalpy > specificEnthalpy3a(entropy) ? region3 : new Region4();

        } else if (entropy < 5.85) {
            if (enthalpy <= specificEnthalpy2c3b(entropy)) {
                return new Region4();

            } else {
                // region 2 or region 3
                if (enthalpy <= hB23limits[0] || entropy <= sB23limits[0]) {
                    return region3;

                } else if (enthalpy >= hB23limits[1] || entropy >= sB23limits[1]) {
                    return region2;

                } else if (hB23limits[0] < enthalpy && enthalpy < hB23limits[1] && sB23limits[0] < entropy && entropy < sB23limits[1]) {
                    return region2.pressureHS(enthalpy, entropy) > pressureB23(temperatureB23HS(enthalpy, entropy)) ? region3 : region2;
                }
            }

        } else if (entropy <= 9.155759395) {
            if (enthalpy <= specificEnthalpy2ab(entropy)) {
                return new Region4();
            }
        }

        return region2;
    }

    static Region getRegionPH(double pressure, double enthalpy) throws OutOfRangeException {

        /*
         Checks
         */
        double pSat0 = saturationPressureT(IF97.T0);

        if (pressure < pSat0) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, pSat0);

        } else if (pressure > 100) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, 100);
        }

        Region region1 = new Region1(), region2 = new Region2();
        double T623 = 623.15, T1073 = 1073.15,
                h2 = region2.specificEnthalpyPT(pressure, T1073);


        /*
         Select Region
         */
        if (enthalpy > h2) {
            if (pressure > 50) {
                throw new OutOfRangeException(new IF97.Quantity[]{IF97.Quantity.p, IF97.Quantity.h}, new double[]{pressure, enthalpy}, new double[]{50, h2});
            }
            return new Region5();
        }
        double pSat623 = saturationPressureT(T623);

        if (pressure < pSat623) {
            // region 1, 4, or 2
            if (enthalpy < region1.specificEnthalpyPT(pressure, saturationTemperatureP(pressure))) {
                return region1;

            } else if (enthalpy > region2.specificEnthalpyPT(pressure, saturationTemperatureP(pressure))) {
                return region2;

            } else {
                return new Region4();
            }

        } else if (region1.specificEnthalpyPT(pSat623, T623) <= enthalpy && enthalpy <= region2.specificEnthalpyPT(pSat623, T623)) {
            // region 3 or 4
            return pressure > saturationPressureH(enthalpy) ? new Region3() : new Region4();

        } else if (enthalpy <= region1.specificEnthalpyPT(pressure, T623)) {
            return region1;

        } else if (enthalpy > region2.specificEnthalpyPT(pressure, temperatureB23P(pressure))) {
            return region2;

        } else {
            return new Region3();
        }
    }

    /**
     * Returns the appropriate region.
     *
     * Note that this method never returns region 4, so prevent when possible.
     *
     * @param unitSystem unit system
     * @param pressure pressure [MPa]
     * @param temperature temperature [K]
     * @return region
     * @throws OutOfRangeException
     */
    static Region getRegionPT(IF97.UnitSystem unitSystem, double pressure, double temperature) throws OutOfRangeException {

        double p = IF97.convertToDefault(unitSystem.PRESSURE, pressure),
                T = IF97.convertToDefault(unitSystem.TEMPERATURE, temperature);

        try {
            return getRegionPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(unitSystem);
        }

    }

    /**
     * Returns the appropriate region.
     *
     * Note that this method never returns region 4, so prevent when possible.
     *
     * @param pressure pressure [MPa]
     * @param temperature temperature [K]
     * @return region
     * @throws OutOfRangeException
     */
    static Region getRegionPT(double pressure, double temperature) throws OutOfRangeException {

        /*
         Checks
         */
        if (pressure <= 0) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, 0);

        } else if (pressure > 100) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, 100);

        } else if (temperature < IF97.T0) {
            throw new OutOfRangeException(IF97.Quantity.T, temperature, IF97.T0);

        } else if (temperature > 1073.15 && pressure > 50) {
            throw new OutOfRangeException(new IF97.Quantity[]{IF97.Quantity.p, IF97.Quantity.T}, new double[]{pressure, temperature}, new double[]{50, 1073.15});

        } else if (temperature > 2073.15) {
            throw new OutOfRangeException(IF97.Quantity.T, temperature, 2073.15);
        }

        /*
         Select Region
         */
        if (temperature > 1073.15) {
            return new Region5();

        } else if (temperature > 623.15) {
            if (pressure > pressureB23(temperature)) {
                return new Region3();
            }
        } else if (pressure > saturationPressureT(temperature)) {
            return new Region1();
        }
        return pressure > 10 ? new Region2() : new Region2Meta();
    }

    /**
     * Get region as a function of pressure & specific entropy.
     *
     * @param pressure pressure [MPa]
     * @param entropy specific entropy [kJ/(kg K)]
     * @return region
     * @throws OutOfRangeException out-of-range exception
     */
    static Region getRegionPS(double pressure, double entropy) throws OutOfRangeException {

        /*
         Checks
         */
        Region region1 = new Region1(), region2 = new Region2();
        double T623 = 623.15,
                pSat273 = saturationPressureT(IF97.T0),
                pSat623 = saturationPressureT(T623),
                s1 = region1.specificEntropyPT(pressure, IF97.T0),
                s2 = region2.specificEntropyPT(pressure, 1073.15);

        if (pressure < pSat273) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, pSat273);

        } else if (pressure > 100) {
            throw new OutOfRangeException(IF97.Quantity.p, pressure, 100);

        } else if (entropy < s1) {
            throw new OutOfRangeException(IF97.Quantity.s, entropy, s1);

        } else if (entropy > s2) {
            throw new OutOfRangeException(IF97.Quantity.s, entropy, s2);
        }

        /*
         Select Region
         */
        if (pressure < pSat623) {
            if (entropy < region1.specificEntropyPT(pressure, saturationTemperatureP(pressure))) {
                return region1;

            } else if (entropy > region2.specificEntropyPT(pressure, saturationTemperatureP(pressure))) {
                return region2;

            } else {
                return new Region4();
            }

        } else if (region1.specificEntropyPT(pSat623, T623) <= entropy && entropy <= region2.specificEntropyPT(pSat623, T623) && pressure < saturationPressure3(entropy)) {
            return new Region4();

        } else if (entropy <= region1.specificEntropyPT(pressure, T623)) {
            return region1;

        } else if (entropy < region2.specificEntropyPT(pressure, temperatureB23P(pressure))) {
            return new Region3();

        } else {
            return region2;
        }
    }

    /**
     * Isobaric cubic expansion coefficient.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return isobaric cubic expansion coefficient [1/K]
     */
    //abstract double isobaricCubicExpansionCoefficientPH(double p, double h);
    /**
     * Isobaric cubic expansion coefficient.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return isobaric cubic expansion coefficient [1/K]
     */
    abstract double isobaricCubicExpansionCoefficientPT(double p, double T);

    /**
     * Isobaric cubic expansion coefficient.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return isobaric cubic expansion coefficient [1/K]
     */
    //abstract double isobaricCubicExpansionCoefficientRhoT(double rho, double T);
    /**
     * Isothermal compressibility.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return isothermal compressibility [1/MPa]
     */
    //abstract double isothermalCompressibilityPH(double p, double h);
    /**
     * Isothermal compressibility.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return isothermal compressibility [1/MPa]
     */
    abstract double isothermalCompressibilityPT(double p, double T);

    /**
     * Isothermal compressibility.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return isothermal compressibility [1/MPa]
     */
    //abstract double isothermalCompressibilityRhoT(double rho, double T);
    /**
     * Isothermal stress coefficient.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return isothermal stress coefficient
     */
    //abstract double isothermalStressCoefficientRhoT(double rho, double T);
    /**
     * Auxiliary equation for the boundary between regions 2 and 3.
     *
     * @param T temperature [K]
     * @return pressure [MPa]
     */
    static double pressureB23(double T) {

        return nB23[0] + nB23[1] * T + nB23[2] * T * T;
    }

    /**
     * Pressure as a function of specific enthalpy & specific entropy.
     *
     * @param h specific enthalpy [kJ/kg]
     * @param s specific entropy [kJ/kg-K]
     * @return pressure [MPa]
     */
    abstract double pressureHS(double h, double s);

    static double saturationPressure3(double s) {

        double pi = 0, sigma = s / 5.2;
        double[] x = {sigma - 1.03, sigma - 0.699};
        double[][] IJn = {
            {0, 0, 0.639767553612785},
            {1, 1, -0.129727445396014e2},
            {1, 32, -0.224595125848403e16},
            {4, 7, 0.177466741801846e7},
            {12, 4, 0.717079349571538e10},
            {12, 14, -0.378829107169011e18},
            {16, 36, -0.955586736431328e35},
            {24, 10, 0.187269814676188e24},
            {28, 0, 0.119254746466473e12},
            {32, 18, 0.110649277244882e37}};

        for (double[] ijn : IJn) {
            pi += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return pi * 22;
    }

    /*
     * Pressure as a function of density & temperature.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return pressure [MPa]
     */
    //abstract double pressureRhoT(double rho, double T);
    static double specificEnthalpy1(double s) {

        double eta = 0, sigma = s / 3.8;
        double[] x = {sigma - 1.09, sigma + 0.366e-4, 1700};
        double[][] IJn = {
            {0, 14, .332171191705237},
            {0, 36, .611217706323496e-3},
            {1, 3, -.882092478906822e1},
            {1, 16, -.455628192543250},
            {2, 0, -.263483840850452e-4},
            {2, 5, -.223949661148062e2},
            {3, 4, -.428398660164013e1},
            {3, 36, -.616679338856916},
            {4, 4, -.146823031104040e2},
            {4, 16, .284523138727299e3},
            {4, 24, -.113398503195444e3},
            {5, 18, .115671380760859e4},
            {5, 24, .395551267359325e3},
            {7, 1, -.154891257229285e1},
            {8, 4, .194486637751291e2},
            {12, 2, -.357915139457043e1},
            {12, 4, -.335369414148819e1},
            {14, 1, -.664426796332460},
            {14, 22, .323321885383934e5},
            {16, 10, .331766744667084e4},
            {20, 12, -.223501257931087e5},
            {20, 28, .573953875852936e7},
            {22, 8, .173226193407919e3},
            {24, 3, -.363968822121321e-1},
            {28, 0, .834596332878346e-6},
            {32, 6, .503611916682674e1},
            {32, 8, .655444787064505e2}
        };
        for (double[] ijn : IJn) {
            eta += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return eta * x[2];
    }

    static double specificEnthalpy2ab(double s) {

        double eta = 0;
        double[] x = {5.21 / s - 0.513, s / 9.2 - 0.524, 2800};
        double[][] IJn = {
            {1, 8, -.524581170928788e3},
            {1, 24, -.926947218142218e7},
            {2, 4, -.237385107491666e3},
            {2, 32, .210770155812776e11},
            {4, 1, -.239494562010986e2},
            {4, 2, .221802480294197e3},
            {7, 7, -.510472533393438e7},
            {8, 5, .124981396109147e7},
            {8, 12, .200008436996201e10},
            {10, 1, -.815158509791035e3},
            {12, 0, -.157612685637523e3},
            {12, 7, -.114200422332791e11},
            {18, 10, .662364680776872e16},
            {20, 12, -.227622818296144e19},
            {24, 32, -.171048081348406e32},
            {28, 8, .660788766938091e16},
            {28, 12, .166320055886021e23},
            {28, 20, -.218003784381501e30},
            {28, 22, -.787276140295618e30},
            {28, 24, .151062329700346e32},
            {32, 2, .795732170300541e7},
            {32, 7, .131957647355347e16},
            {32, 12, -.325097068299140e24},
            {32, 14, -.418600611419248e26},
            {32, 24, .297478906557467e35},
            {36, 10, -.953588761745473e20},
            {36, 12, .166957699620939e25},
            {36, 20, -.175407764869978e33},
            {36, 22, .347581490626396e35},
            {36, 28, -.710971318427851e39}
        };
        for (double[] ijn : IJn) {
            eta += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return exp(eta) * x[2];
    }

    static double specificEnthalpy2c3b(double s) {

        double eta = 0, sigma = s / 5.9;
        double[] x = {sigma - 1.02, sigma - 0.726};
        double[][] IJn = {
            {0, 0, .104351280732769e1},
            {0, 3, -.227807912708513e1},
            {0, 4, .180535256723202e1},
            {1, 0, .420440834792042},
            {1, 12, -.105721244834660e6},
            {5, 36, .436911607493884e25},
            {6, 12, -.328032702839753e12},
            {7, 16, -.678686760804270e16},
            {8, 2, .743957464645363e4},
            {8, 20, -.356896445355761e20},
            {12, 32, .167590585186801e32},
            {16, 36, -.355028625419105e38},
            {22, 2, .396611982166538e12},
            {22, 32, -.414716268484468e41},
            {24, 7, .359080103867382e19},
            {36, 20, -.116994334851995e41}};

        for (double[] ijn : IJn) {
            eta += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return pow(eta, 4) * 2800;
    }

    static double specificEnthalpy3a(double s) {

        double eta = 0, sigma = s / 3.8;
        double[] x = {sigma - 1.09, sigma + 0.366e-4, 1700};
        double[][] IJn = {
            {0, 1, .822673364673336},
            {0, 4, .181977213534479},
            {0, 10, -.112000260313624e-1},
            {0, 16, -.746778287048033e-3},
            {2, 1, -.179046263257381},
            {3, 36, .424220110836657e-1},
            {4, 3, -.341355823438768},
            {4, 16, -.209881740853565e1},
            {5, 20, -.822477343323596e1},
            {5, 36, -.499684082076008e1},
            {6, 4, .191413958471069},
            {7, 2, .581062241093136e-1},
            {7, 28, -.165505498701029e4},
            {7, 32, .158870443421201e4},
            {10, 14, -.850623535172818e2},
            {10, 32, -.317714386511207e5},
            {10, 36, -.945890406632871e5},
            {32, 0, -.139273847088690e-5},
            {32, 6, .631052532240980}
        };
        for (double[] ijn : IJn) {
            eta += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return eta * x[2];
    }

    static double specificEnthalpyB13(double s) {

        double eta = 0, sigma = s / 3.8;
        double[] x = {sigma - 0.884, sigma - 0.864, 1700};
        double[][] IJn = {
            {0, 0, .913965547600543},
            {1, -2, -.430944856041991e-4},
            {1, 2, .603235694765419e2},
            {3, -12, .117518273082168e-17},
            {5, -4, .220000904781292},
            {6, -3, -.690815545851641e2}
        };
        for (double[] ijn : IJn) {
            eta += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return eta * x[2];
    }

    double specificEnthalpyPS(double p, double s) throws OutOfRangeException {

        if (this instanceof Region4) {
            return Region4.specificEnthalpyPX(p, vapourFractionPS(p, s));

        } else {
            return specificEnthalpyPT(p, temperaturePS(p, s));
        }
    }

    /**
     * Specific enthalpy.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return specific enthalpy [kJ/kg]
     */
    abstract double specificEnthalpyPT(double p, double T);

    /**
     * Specific enthalpy.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return specific enthalpy [kJ/kg]
     */
    //abstract double specificEnthalpyRhoT(double rho, double T);
    /**
     * Specific entropy.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return specific entropy [kJ/kg-K]
     */
    //abstract double specificEntropyPH(double p, double h);
    /**
     * Specific entropy.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return specific entropy [kJ/kg-K]
     */
    abstract double specificEntropyPT(double p, double T);

    /**
     * Specific entropy.
     *
     * @param rho density [kg/m&sup3;]
     * @param h specific enthalpy [kJ/kg]
     * @return specific entropy [kJ/kg-K]
     */
    //abstract double specificEntropyRhoH(double rho, double h);
    /**
     * Specific entropy.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return specific entropy [kJ/kg-K]
     */
    abstract double specificEntropyRhoT(double rho, double T);

    /**
     * Specific internal energy.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return specific internal energy [kJ/kg]
     */
    //abstract double specificInternalEnergyPH(double p, double h);
    /**
     * Specific internal energy.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return specific internal energy [kJ/kg]
     */
    abstract double specificInternalEnergyPT(double p, double T);

    /**
     * Specific internal energy.
     *
     * @param rho density [kg/m&sup3;]
     * @param h specific enthalpy [kJ/kg]
     * @return specific internal energy [kJ/kg]
     */
    //abstract double specificInternalEnergyRhoH(double rho, double h);
    /**
     * Specific internal energy.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return specific internal energy [kJ/kg]
     */
    //abstract double specificInternalEnergyRhoT(double rho, double T);
    /**
     * Specific isobaric heat capacity.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return specific isobaric heat capacity [kJ/kg-K]
     */
    //abstract double specificIsobaricHeatCapacityPH(double p, double h);
    /**
     * Specific isobaric heat capacity.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return specific isobaric heat capacity [kJ/kg-K]
     */
    abstract double specificIsobaricHeatCapacityPT(double p, double T);

    /**
     * Specific isobaric heat capacity.
     *
     * @param rho density [kg/m&sup3;]
     * @param h specific enthalpy [kJ/kg]
     * @return specific isobaric heat capacity [kJ/kg-K]
     */
    //abstract double specificIsobaricHeatCapacityRhoH(double rho, double h);
    /**
     * Specific isobaric heat capacity.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return specific isobaric heat capacity [kJ/kg-K]
     */
    //abstract double specificIsobaricHeatCapacityRhoT(double rho, double T);
    /**
     * Specific isochoric heat capacity.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return specific isochoric heat capacity [kJ/kg-K]
     */
    //abstract double specificIsochoricHeatCapacityPH(double p, double h);
    /**
     * Specific isochoric heat capacity.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return specific isochoric heat capacity [kJ/kg-K]
     */
    abstract double specificIsochoricHeatCapacityPT(double p, double T);

    /**
     * Specific isochoric heat capacity.
     *
     * @param rho density [kg/m&sup3;]
     * @param h specific enthalpy [kJ/kg]
     * @return specific isochoric heat capacity [kJ/kg-K]
     */
    //abstract double specificIsochoricHeatCapacityRhoH(double rho, double h);
    /**
     * Specific isochoric heat capacity.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return specific isochoric heat capacity [kJ/kg-K]
     */
    //abstract double specificIsochoricHeatCapacityRhoT(double rho, double T);
    /**
     * Specific volume as a function of specific enthalpy & specific entropy.
     *
     * @param h specific enthalpy [kJ/kg]
     * @param s specific entropy [kJ/kg-K]
     * @return specific volume [m&sup3;/kg]
     * @throws OutOfRangeException out-of-range exception
     */
    double specificVolumeHS(double h, double s) throws OutOfRangeException {

        double p = pressureHS(h, s);

        if (this instanceof Region4) {
            double x = vapourFractionHS(h, s);

            return Region4.specificVolumePX(p, x);
        }

        double T = temperaturePS(p, s);

        return specificVolumePT(p, T);
    }

    /**
     * Specific volume.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return specific volume [m&sup3;/kg]
     * @throws OutOfRangeException out-of-range exception
     */
    double specificVolumePH(double p, double h) throws OutOfRangeException {

        if (this instanceof Region4) {
            double x = vapourFractionPH(p, h);

            return Region4.specificVolumePX(p, x);
        }

        return specificVolumePT(p, temperaturePH(p, h));
    }

    /**
     * Specific volume as a function of pressure & specific entropy.
     *
     * @param p pressure [MPa]
     * @param s specific entropy [kJ/kg-K]
     * @return specific volume [m&sup3;/kg]
     * @throws OutOfRangeException out-of-range exception
     */
    double specificVolumePS(double p, double s) throws OutOfRangeException {

        if (this instanceof Region4) {
            double x = vapourFractionPS(p, s);

            return Region4.specificVolumePX(p, x);
        }

        double T = temperaturePS(p, s);

        return specificVolumePT(p, T);
    }

    /**
     * Specific volume.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return specific volume [m&sup3;/kg]
     */
    abstract double specificVolumePT(double p, double T);

    /**
     * Speed of sound.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return speed of sound [m/s]
     */
    //abstract double speedOfSoundPH(double p, double h);
    /**
     * Speed of sound.
     *
     * @param p pressure [MPa]
     * @param T temperature [K]
     * @return speed of sound [m/s]
     */
    abstract double speedOfSoundPT(double p, double T);

    /**
     * Speed of sound.
     *
     * @param rho density [kg/m&sup3;]
     * @param T temperature [K]
     * @return speed of sound [m/s]
     */
    //abstract double speedOfSoundRhoT(double rho, double T);
    /**
     * Auxiliary equation for the boundary between regions 2 and 3.
     *
     * @param p pressure [MPa]
     * @return temperature [K]
     */
    static double temperatureB23P(double p) {

        return nB23[3] + sqrt((p - nB23[4]) / nB23[2]);
    }

    /**
     * Auxiliary equation for the boundary between regions 2 and 3.
     *
     * @param h specific enthalpy [kJ/kg]
     * @param s specific entropy [kJ/kg-K]
     * @return temperature [K]
     */
    static double temperatureB23HS(double h, double s) {

        double theta = 0, eta = h / 3e3, sigma = s / 5.3;
        double[] x = new double[]{eta - 0.727, sigma - 0.864, 900};
        double[][] IJn = new double[][]{
            {-12, 10, .629096260829810e-3},
            {-10, 8, -.823453502583165e-3},
            {-8, 3, .515446951519474e-7},
            {-4, 4, -.117565945784945e1},
            {-3, 3, .348519684726192e1},
            {-2, -6, -.507837382408313e-11},
            {-2, 2, -.284637670005479e1},
            {-2, 3, -.236092263939673e1},
            {-2, 4, .601492324973779e1},
            {0, 0, .148039650824546e1},
            {1, -3, .360075182221907e-3},
            {1, -2, -.126700045009952e-1},
            {1, 10, -.122184332521413e7},
            {3, -2, .149276502463272},
            {3, -1, .698733471798484},
            {5, -5, -.252207040114321e-1},
            {6, -6, .147151930985213e-1},
            {6, -3, -.108618917681849e1},
            {8, -8, -.936875039816322e-3},
            {8, -2, .819877897570217e2},
            {8, -1, -.182041861521835e3},
            {12, -12, .261907376402688e-5},
            {12, -1, -.291626417025961e5},
            {14, -12, .140660774926165e-4},
            {14, 1, .783237062349385e7}
        };

        for (double[] ijn : IJn) {
            theta += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }

        return theta * x[2];
    }

    /**
     * Temperature.
     *
     * @param h specific enthalpy [kJ/kg]
     * @param s specific entropy [kJ/kg-K]
     * @return temperature [K]
     */
    abstract double temperatureHS(double h, double s);

    /**
     * Temperature.
     *
     * @param p pressure [MPa]
     * @param h specific enthalpy [kJ/kg]
     * @return temperature [K]
     */
    abstract double temperaturePH(double p, double h);

    /**
     * Temperature as a function of pressure & specific entropy.
     *
     * @param p pressure [MPa]
     * @param s specific entropy [kJ/kg-K]
     * @return temperature [K]
     */
    abstract double temperaturePS(double p, double s);

    /**
     * Temperature.
     *
     * @param rho density [kg/m&sup3;]
     * @param h specific enthalpy [kJ/kg]
     * @return temperature [K]
     */
    //abstract double temperatureRhoH(double rho, double h);
    /**
     * Vapour fraction.
     *
     * @param h specific enthalpy [kJ/kg]
     * @param s specific entropy [kJ/kg-K]
     * @return vapour fraction [-]
     */
    abstract double vapourFractionHS(double h, double s);

    abstract double vapourFractionPH(double p, double h);

    abstract double vapourFractionPS(double p, double s);

    abstract double vapourFractionTS(double T, double s);
}
