/*
 * IF97.java
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

import static com.hummeling.if97.Region.*;
import static java.lang.Math.*;

/**
 * <p>
 * Steam tables for industrial use according to the international standard for
 * the properties of water and steam, the IAPWS-IF97 formulation and the
 * international standards for transport and other properties.</p>
 *
 * <p>
 * By default, units are as given in the book cited below. See
 * {@link UnitSystem} for options.</p>
 *
 * <p>
 * Properties are generally available as functions of three quantity
 * combinations: pressure &amp; temperature (p, T), pressure &amp; specific
 * enthalpy (p, h), and specific enthalpy &amp; specific entropy (h, s).</p>
 *
 * <ul> <li>Wagner, Wolfgang &amp; Kretzschmar, Hans-Joachim, 2008,
 * <i>International Steam Tables &mdash; Properties of Water and Steam Based on
 * the Industrial Formulation IAPWS-IF97</i>, 2<sup>nd</sup> Edition,
 * Springer-Verlag, Berlin Heidelberg, ISBN 978-3-540-21419-9</li> </ul>
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
public class IF97 {

// <editor-fold defaultstate="collapsed" desc="fields">
    private UnitSystem UNIT_SYSTEM;
    /**
     * Specific gas constant of ordinary water [kJ/kg-K].
     */
    public static final double R = 0.461526;
    /**
     * Molar gas constant of ordinary water [kJ/kmol-K].
     */
    public static final double Rm = 8.31451;
    /**
     * Molar mass of ordinary water [kg/kmol].
     */
    public static final double M = 18.015257;
    /**
     * Critical temperature [K].
     */
    public static final double Tc = 647.096;
    /**
     * Critical pressure [MPa].
     */
    public static final double pc = 22.064;
    /**
     * Critical enthalpy [kJ/kg].
     */
    public static final double hc = 2.087546845e3;
    /**
     * Critical entropy [kJ/kg-K].
     */
    public static final double sc = 4.41202148223476;
    /**
     * Critical density [kg/m3].
     */
    public static final double rhoc = 322;
    static final double BTU, ft, ft2, ft3, g, hr, in, in2, lb, lbf, psi, Ra;
// </editor-fold>

    static {
        BTU = 1.055056; // British thermal unit acc. International standard ISO 31-4 on Quantities and units—Part 4: Heat, Appendix A [kJ]
        ft = 0.3048; // foot [m]
        ft2 = ft * ft; // square foot [m^2]
        ft3 = ft * ft2; // cubic foot [m^3]
        g = 9.80665; // gravitational accelleration [m/s^2]
        hr = 3600; // hour [s]
        in = ft / 12; // inch [m]
        in2 = in * in; // square inch [m^2]
        lb = 0.45359237; // pound [kg]
        lbf = lb * g;
        psi = 1e-6 * lb / in2; // pounds per square inch [MPa]
        Ra = 5.0 / 9.0; // Rankine [K]
    }

    /**
     * Instantiate an IF97 object with the default unit system.
     */
    public IF97() {

        this(UnitSystem.DEFAULT);
    }

    /**
     * Instantiate an IF97 object with the specified unit system.
     *
     * @param unitSystem unit system
     */
    public IF97(UnitSystem unitSystem) {

        setUnitSystem(unitSystem);
    }

    /**
     * Prandtl number.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return Prandtl number
     * @throws OutOfRangeException out-of-range exception
     */
    public double PrandtlHS(double enthalpy, double entropy) throws OutOfRangeException {

        try {
            double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

            Region region = getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);

            return Calculate.PrandtlPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Prandtl number.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return Prandtl number
     * @throws OutOfRangeException out-of-range exception
     */
    public double PrandtlPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h);

            return Calculate.PrandtlPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Prandtl number.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return Prandtl number
     * @throws OutOfRangeException out-of-range exception
     */
    public double PrandtlPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            return Calculate.PrandtlPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isothermal compressibility as a function of specific enthalpy & specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return isothermal compressibility
     * @throws OutOfRangeException out-of-range exception
     */
    public double compressibilityHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            Region region = getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s),
                    kappaT = region.isothermalCompressibilityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.COMPRESSIBILITY, kappaT);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isothermal compressibility as a function of pressure & specific enthalpy.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return isothermal compressibility
     * @throws OutOfRangeException out-of-range exception
     */
    public double compressibilityPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    kappaT = getRegionPT(p, T).isothermalCompressibilityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.COMPRESSIBILITY, kappaT);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isothermal compressibility as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return isothermal compressibility
     * @throws OutOfRangeException out-of-range exception
     */
    public double compressibilityPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    kappaT = getRegionPT(p, T).isothermalCompressibilityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.COMPRESSIBILITY, kappaT);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    static double convertFromDefault(UnitSystem unitSystem, IF97.Quantity quantity, double value) {

        switch (quantity) {
            case rho:
                return convertFromDefault(unitSystem.DENSITY, value);

            case p:
                return convertFromDefault(unitSystem.PRESSURE, value);

            case u:
                return convertFromDefault(unitSystem.SPECIFIC_ENERGY, value);

            case h:
                return convertFromDefault(unitSystem.SPECIFIC_ENTHALPY, value);

            case s:
                return convertFromDefault(unitSystem.SPECIFIC_ENTROPY, value);

            case nu:
                return convertFromDefault(unitSystem.SPECIFIC_VOLUME, value);

            case T:
                return convertFromDefault(unitSystem.TEMPERATURE, value);

            case lambda:
                return convertFromDefault(unitSystem.WAVELENGTH, value);

            default:
                throw new IllegalArgumentException("No conversion available for: " + quantity);
        }
    }

    static double convertFromDefault(double[] quantity, double value) {

        return (value - quantity[1]) / quantity[0];
    }

    static double convertToDefault(double[] quantity, double value) {

        return value * quantity[0] + quantity[1];
    }

    /**
     * Density as a function of pressure & specific enthalpy.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>1/specificVolumePH(pressure, enthalpy)</code>.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return density
     * @throws OutOfRangeException out-of-range exception
     * @see #specificVolumePH(double, double)
     */
    public double densityPH(double pressure, double enthalpy) throws OutOfRangeException {

        return 1 / specificVolumePH(pressure, enthalpy);
    }

    /**
     * Density as a function of pressure & temperature.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>1/specificVolumePT(pressure, temperature)</code>.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return density
     * @throws OutOfRangeException out-of-range exception
     * @see #specificVolumePT(double, double)
     */
    public double densityPT(double pressure, double temperature) throws OutOfRangeException {

        return 1 / specificVolumePT(pressure, temperature);
    }

    /**
     * Dielectric constant.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return dielectric constant [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double dielectricConstantPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    rho = 1 / Calculate.specificVolumePT(p, T);

            return Calculate.dielectricConstantRhoT(rho, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dielectric constant.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return dielectric constant [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double dielectricConstantPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    rho = 1 / Calculate.specificVolumePT(p, T);

            return Calculate.dielectricConstantRhoT(rho, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dielectric constant.
     *
     * @param density density
     * @param temperature temperature
     * @return dielectric constant [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double dielectricConstantRhoT(double density, double temperature) throws OutOfRangeException {

        try {
            double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

            return Calculate.dielectricConstantRhoT(rho, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dynamic viscosity as a function of pressure & specific enthalpy.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return dynamic viscosity
     * @throws OutOfRangeException out-of-range exception
     */
    public double dynamicViscosityPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    rho = 1 / Calculate.specificVolumePT(p, T),
                    eta = Calculate.dynamicViscosityRhoT(rho, T);

            return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, eta);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dynamic viscosity.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return dynamic viscosity
     * @throws OutOfRangeException out-of-range exception
     */
    public double dynamicViscosityPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    rho = 1 / Calculate.specificVolumePT(p, T),
                    eta = Calculate.dynamicViscosityRhoT(rho, T);

            return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, eta);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dynamic viscosity.
     *
     * @param density density
     * @param temperature temperature
     * @return dynamic viscosity
     * @throws OutOfRangeException out-of-range exception
     */
    public double dynamicViscosityRhoT(double density, double temperature) throws OutOfRangeException {

        try {
            double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    eta = Calculate.dynamicViscosityRhoT(rho, T);

            return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, eta);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isobaric cubic expansion coefficient.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return isobaric cubic expansion coefficient
     * @throws OutOfRangeException out-of-range exception
     */
    public double isobaricCubicExpansionCoefficientPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    alphav = getRegionPH(p, h).isobaricCubicExpansionCoefficientPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.ISOBARIC_CUBIC_EXPANSION_COEFFICIENT, alphav);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isobaric cubic expansion coefficient.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return isobaric cubic expansion coefficient
     * @throws OutOfRangeException out-of-range exception
     */
    public double isobaricCubicExpansionCoefficientPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    alphav = getRegionPT(p, T).isobaricCubicExpansionCoefficientPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.ISOBARIC_CUBIC_EXPANSION_COEFFICIENT, alphav);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific isobaric heat capacity as a function of pressure & specific
     * enthalpy.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return specific isobaric heat capacity
     * @throws OutOfRangeException out-of-range exception
     */
    public double isobaricHeatCapacityPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    cp = getRegionPT(p, T).specificIsobaricHeatCapacityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cp);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific isobaric heat capacity as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return specific isobaric heat capacity
     * @throws OutOfRangeException out-of-range exception
     */
    public double isobaricHeatCapacityPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    cp = getRegionPT(p, T).specificIsobaricHeatCapacityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cp);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific isochoric heat capacity as a function of pressure & specific
     * enthalpy.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return specific isochoric heat capacity
     * @throws OutOfRangeException out-of-range exception
     */
    public double isochoricHeatCapacityPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    cv = getRegionPT(p, T).specificIsochoricHeatCapacityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cv);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific isochoric heat capacity as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return specific isochoric heat capacity
     * @throws OutOfRangeException out-of-range exception
     */
    public double isochoricHeatCapacityPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    cv = getRegionPT(p, T).specificIsochoricHeatCapacityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cv);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Kinematic viscosity.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return kinematic viscosity
     * @throws OutOfRangeException out-of-range exception
     */
    public double kinematicViscosityPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    rho = 1 / Calculate.specificVolumePT(p, T),
                    nu = Calculate.dynamicViscosityRhoT(rho, T) / rho;

            return convertFromDefault(UNIT_SYSTEM.KINEMATIC_VISCOSITY, nu);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Kinematic viscosity.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return kinematic viscosity
     * @throws OutOfRangeException out-of-range exception
     */
    public double kinematicViscosityPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    rho = 1 / Calculate.specificVolumePT(p, T),
                    nu = Calculate.dynamicViscosityRhoT(rho, T) / rho;

            return convertFromDefault(UNIT_SYSTEM.KINEMATIC_VISCOSITY, nu);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Kinematic viscosity.
     *
     * @param density density
     * @param temperature temperature
     * @return kinematic viscosity
     * @throws OutOfRangeException out-of-range exception
     */
    public double kinematicViscosityRhoT(double density, double temperature) throws OutOfRangeException {

        try {
            double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    nu = Calculate.dynamicViscosityRhoT(rho, T) / rho;

            return convertFromDefault(UNIT_SYSTEM.KINEMATIC_VISCOSITY, nu);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Partial derivative of z with respect to x for constant y, as a function
     * of pressure and temperature.
     *
     * <p>
     * (<sup>&part;z</sup>/<sub>&part;x</sub>)<sub>y</sub>(p, T) </p>
     *
     * @param pressure pressure
     * @param temperature temperature
     * @param x any {@link Quantity}
     * @param y any {@link Quantity}
     * @param z any {@link Quantity}
     * @return partial derivative in default units
     * @throws OutOfRangeException
     */
    public double partialDerivativePT(double pressure, double temperature, Quantity x, Quantity y, Quantity z) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            Region region = Region.getRegionPT(p, T);

            if (region.getName().equals("Region 3")) {
                double rho = 1 / region.specificVolumePT(p, T);
                return Calculate.partialDerivativeRhoT(rho, T, x, y, z);
            }
            return Calculate.partialDerivativePT(p, T, x, y, z);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Partial derivative of z with respect to x for constant y, as a function
     * of density and temperature.
     *
     * <p>
     * (<sup>&part;z</sup>/<sub>&part;x</sub>)<sub>y</sub>(&rho;, T) </p>
     *
     * @param density density
     * @param temperature temperature
     * @param x any {@link Quantity}
     * @param y any {@link Quantity}
     * @param z any {@link Quantity}
     * @return partial derivative in default units
     * @throws OutOfRangeException
     */
    public double partialDerivativeRhoT(double density, double temperature, Quantity x, Quantity y, Quantity z) throws OutOfRangeException {

        double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            return Calculate.partialDerivativeRhoT(rho, T, x, y, z);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }

        //Region region = Region.getRegionPT(p, T);
        //
        //if (region.getName().equals("Region 3")) {
        //}
        //
        //throw new OutOfRangeException(null, Double.NaN, Double.NaN);
    }

    /**
     * Pressure as a function of specific enthalpy & specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return pressure
     * @throws OutOfRangeException out-of-range exception
     */
    public double pressureHS(double enthalpy, double entropy) throws OutOfRangeException {

        try {
            double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                    p = getRegionHS(h, s).pressureHS(h, s);

            return convertFromDefault(UNIT_SYSTEM.PRESSURE, p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Refractive index.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @param wavelength wavelength
     * @return refractive index [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double refractiveIndexPTLambda(double pressure, double temperature, double wavelength) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    rho = 1 / Calculate.specificVolumePT(p, T),
                    lambda = convertToDefault(UNIT_SYSTEM.WAVELENGTH, wavelength);

            return Calculate.refractiveIndexRhoTLambda(rho, T, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Refractive index.
     *
     * @param density density [kg/m3]
     * @param temperature temperature [K]
     * @param waveLength wave length [10^-6 m]
     * @return refractive index [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double refractiveIndexRhoTLambda(double density, double temperature, double waveLength) throws OutOfRangeException {

        try {
            double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    lambda = convertToDefault(UNIT_SYSTEM.WAVELENGTH, waveLength);

            return Calculate.refractiveIndexRhoTLambda(rho, T, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Boundary saturation pressure for the boundary between regions 3 and 4.
     *
     * @param enthalpy specific enthalpy
     * @return saturation pressure
     */
    public double saturationPressureH(double enthalpy) {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                p = Region4.saturationPressureH(h);

        return convertFromDefault(UNIT_SYSTEM.PRESSURE, p);
    }

    /**
     * Boundary saturation pressure for the boundary between regions 3 and 4.
     *
     * @param entropy specific entropy
     * @return saturation pressure
     */
    public double saturationPressureS(double entropy) {

        double s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                p = Region4.saturationPressureS(s);

        return convertFromDefault(UNIT_SYSTEM.PRESSURE, p);
    }

    /**
     * Saturation pressure as a function of temperature.
     *
     * @param temperature saturation temperature
     * @return saturation pressure
     * @throws com.hummeling.if97.OutOfRangeException
     */
    public double saturationPressureT(double temperature) throws OutOfRangeException {

        try {
            double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    p = Region4.saturationPressureT(T);

            return convertFromDefault(UNIT_SYSTEM.PRESSURE, p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Saturation temperature as a function of pressure.
     *
     * @param pressure saturation pressure
     * @return saturation temperature
     * @throws com.hummeling.if97.OutOfRangeException
     */
    public double saturationTemperatureP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure);
        double[] pLimits = {0.000611212677, 22.064};

        try {
            if (p < pLimits[0]) {
                throw new OutOfRangeException(Quantity.p, p, pLimits[0]);

            } else if (p > pLimits[1]) {
                throw new OutOfRangeException(Quantity.p, p, pLimits[1]);
            }
            double T = Region4.saturationTemperatureP(p);

            return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Set (change) the unit system.
     *
     * @param unitSystem unit system
     */
    public final void setUnitSystem(UnitSystem unitSystem) {

        UNIT_SYSTEM = unitSystem;
    }

    /**
     * Specific enthalpy as a function of pressure & specific entropy.
     *
     * @param pressure pressure
     * @param entropy specific entropy
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEnthalpyPS(double pressure, double entropy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);
            Region region = getRegionPS(p, s);
            double T = region.temperaturePS(p, s),
                    h = region.specificEnthalpyPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific enthalpy as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEnthalpyPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    h = getRegionPT(p, T).specificEnthalpyPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific enthalpy as a function of pressure & vapour fraction.
     *
     * @param pressure pressure
     * @param vapourFraction vapour fraction [-]
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEnthalpyPX(double pressure, double vapourFraction) throws OutOfRangeException {

        if (vapourFraction < 0) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 0);

        } else if (vapourFraction > 1) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 1);
        }

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = new Region4().specificEnthalpyPX(p, vapourFraction);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific enthalpy as a function of pressure for saturated liquid.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>specificEnthalpyPX(pressure, 0)</code>.
     *
     * @param pressure saturation pressure
     * @return specific enthalpy
     * @throws OutOfRangeException
     * @see #specificEnthalpyPX(double, double)
     */
    public double specificEnthalpySaturatedLiquidP(double pressure) throws OutOfRangeException {

        return specificEnthalpyPX(pressure, 0);
    }

    /**
     * Specific enthalpy as a function of temperature for saturated liquid.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>specificEnthalpyTX(temperature, 0)</code>.
     *
     * @param temperature saturation temperature
     * @return specific enthalpy
     * @throws OutOfRangeException
     * @see #specificEnthalpyTX(double, double)
     */
    public double specificEnthalpySaturatedLiquidT(double temperature) throws OutOfRangeException {

        return specificEnthalpyTX(temperature, 0);
    }

    /**
     * Specific enthalpy as a function of pressure for saturated vapour.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>specificEnthalpyPX(pressure, 1)</code>.
     *
     * @param pressure saturation pressure
     * @return specific enthalpy
     * @throws OutOfRangeException
     * @see #specificEnthalpyPX(double, double)
     */
    public double specificEnthalpySaturatedVapourP(double pressure) throws OutOfRangeException {

        return specificEnthalpyPX(pressure, 1);
    }

    /**
     * Specific enthalpy as a function of temperature for saturated vapour.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>specificEnthalpyTX(temperature, 1)</code>.
     *
     * @param temperature saturation temperature
     * @return specific enthalpy
     * @throws OutOfRangeException
     * @see #specificEnthalpyTX(double, double)
     */
    public double specificEnthalpySaturatedVapourT(double temperature) throws OutOfRangeException {

        return specificEnthalpyTX(temperature, 1);
    }

    /**
     * Specific enthalpy as a function of temperature & vapour fraction.
     *
     * @param temperature temperature
     * @param vapourFraction vapour fraction [-]
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEnthalpyTX(double temperature, double vapourFraction) throws OutOfRangeException {

        if (vapourFraction < 0) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 0);

        } else if (vapourFraction > 1) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 1);
        }

        try {
            double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    h = new Region4().specificEnthalpyTX(T, vapourFraction);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific entropy as a function of pressure & specific enthalpy.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEntropyPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    s = getRegionPT(p, T).specificEntropyPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific entropy as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEntropyPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            Region region = getRegionPT(p, T);
            double s = region.specificEntropyPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific entropy as a function of pressure & vapour fraction.
     *
     * @param pressure pressure
     * @param vapourFraction vapour fraction [-]
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEntropyPX(double pressure, double vapourFraction) throws OutOfRangeException {

        if (vapourFraction < 0) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 0);

        } else if (vapourFraction > 1) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 1);
        }

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    s = new Region4().specificEntropyPX(p, vapourFraction);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific entropy as a function of pressure for saturated liquid.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>specificEntropyPX(pressure, 0)</code>.
     *
     * @param pressure saturation pressure
     * @return specific entropy
     * @throws OutOfRangeException
     * @see #specificEntropyPX(double, double)
     */
    public double specificEntropySaturatedLiquidP(double pressure) throws OutOfRangeException {

        return specificEntropyPX(pressure, 0);
    }

    /**
     * Specific entropy as a function of temperature for saturated liquid.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>specificEntropyTX(temperature, 0)</code>.
     *
     * @param temperature saturation temperature
     * @return specific entropy
     * @throws OutOfRangeException
     * @see #specificEntropyTX(double, double)
     */
    public double specificEntropySaturatedLiquidT(double temperature) throws OutOfRangeException {

        return specificEntropyTX(temperature, 0);
    }

    /**
     * Specific entropy as a function of pressure for saturated vapour.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>specificEntropyPX(pressure, 1)</code>.
     *
     * @param pressure saturation pressure
     * @return specific entropy
     * @throws OutOfRangeException
     * @see #specificEntropyPX(double, double)
     */
    public double specificEntropySaturatedVapourP(double pressure) throws OutOfRangeException {

        return specificEntropyPX(pressure, 1);
    }

    /**
     * Specific entropy as a function of temperature for saturated vapour.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>specificEntropyTX(temperature, 1)</code>.
     *
     * @param temperature saturation temperature
     * @return specific entropy
     * @throws OutOfRangeException
     * @see #specificEntropyTX(double, double)
     */
    public double specificEntropySaturatedVapourT(double temperature) throws OutOfRangeException {

        return specificEntropyTX(temperature, 1);
    }

    /**
     * Specific entropy as a function of temperature & vapour fraction.
     *
     * @param temperature temperature
     * @param vapourFraction vapour fraction [-]
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEntropyTX(double temperature, double vapourFraction) throws OutOfRangeException {

        if (vapourFraction < 0) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 0);

        } else if (vapourFraction > 1) {
            throw new OutOfRangeException(Quantity.x, vapourFraction, 1);
        }

        try {
            double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    s = new Region4().specificEntropyTX(T, vapourFraction);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific internal energy as a function of specific enthalpy & specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificInternalEnergyHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            Region region = getRegionHS(h, s);
            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s),
                    u = region.specificInternalEnergyPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENERGY, u);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific internal energy as a function of pressure & specific enthalpy.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificInternalEnergyPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    u = getRegionPT(p, T).specificInternalEnergyPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENERGY, u);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific internal energy as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificInternalEnergyPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    u = getRegionPT(p, T).specificInternalEnergyPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENERGY, u);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific volume as a function of specific enthalpy & specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    //public double specificVolumeHS(double enthalpy, double entropy) throws OutOfRangeException {
    //
    //    try {
    //        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
    //                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
    //                nu = Calculate.specificVolumeHS(h, s);
    //
    //        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, nu);
    //
    //    } catch (OutOfRangeException e) {
    //        throw e.convertFromDefault(UNIT_SYSTEM);
    //    }
    //}
    /**
     * Specific volume as a function of pressure & specific enthalpy.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumePH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    nu = Calculate.specificVolumePT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, nu);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Specific volume as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumePT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    nu = Calculate.specificVolumePT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, nu);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Speed of sound as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return speed of sound
     * @throws OutOfRangeException out-of-range exception
     */
    public double speedOfSoundPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    w = Calculate.speedOfSoundPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, w);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Surface tension as a function of temperature.
     *
     * @param temperature temperature
     * @return surface tension
     * @throws OutOfRangeException out-of-range exception
     */
    public double surfaceTensionT(double temperature) throws OutOfRangeException {

        try {
            double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    sigma = Calculate.surfaceTensionT(T);

            return convertFromDefault(UNIT_SYSTEM.SURFACE_TENSION, sigma);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Temperature. [IF97 Supplementary Release S04]
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return temperature
     * @throws OutOfRangeException out-of-range exception
     */
    public double temperatureHS(double enthalpy, double entropy) throws OutOfRangeException {

        try {
            double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                    T = getRegionHS(h, s).temperatureHS(h, s);

            return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Temperature.
     *
     * @param pressure pressure [MPa]
     * @param enthalpy specific enthalpy [kJ/(kg)]
     * @return temperature [K]
     * @throws OutOfRangeException out-of-range exception
     */
    public double temperaturePH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h);

            return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Temperature.
     *
     * @param pressure pressure
     * @param entropy specific entropy
     * @return temperature
     * @throws OutOfRangeException out-of-range exception
     */
    public double temperaturePS(double pressure, double entropy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                    T = getRegionPS(p, s).temperaturePS(p, s);

            return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Thermal conductivity as a function of pressure & specific enthalpy.
     *
     * @param pressure pressure
     * @param enthalpy specific enthalpy
     * @return thermal conductivity
     * @throws OutOfRangeException out-of-range exception
     */
    public double thermalConductivityPH(double pressure, double enthalpy) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                    T = Calculate.temperaturePH(p, h),
                    lambda = Calculate.thermalConductivityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.THERMAL_CONDUCTIVITY, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Thermal conductivity as a function of pressure & temperature.
     *
     * @param pressure pressure
     * @param temperature temperature
     * @return thermal conductivity
     * @throws OutOfRangeException out-of-range exception
     */
    public double thermalConductivityPT(double pressure, double temperature) throws OutOfRangeException {

        try {
            double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                    T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                    lambda = Calculate.thermalConductivityPT(p, T);

            return convertFromDefault(UNIT_SYSTEM.THERMAL_CONDUCTIVITY, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Vapour fraction as a function of specific enthalpy & specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return vapour fraction [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double vapourFractionHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            return getRegionHS(h, s).vapourFractionHS(h, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Vapour fraction as a function of pressure & specific entropy.
     *
     * <p>
     * Note: for highest accuracy, use enthalpy to determine vapour fraction
     * ({@link #vapourFractionHS(double, double)}).
     *
     * @param pressure pressure
     * @param entropy specific entropy
     * @return vapour fraction [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #vapourFractionHS(double, double)
     */
    public double vapourFractionPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            return getRegionPS(p, s).vapourFractionPS(p, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Vapour fraction as a function of temperature & specific entropy.
     *
     * <p>
     * Note: whenever possible, use enthalpy to determine vapour fraction
     * ({@link #vapourFractionHS(double, double)}) for highest accuracy.
     *
     * @param temperature temperature
     * @param entropy specific entropy
     * @return vapour fraction [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #vapourFractionHS(double, double)
     */
    public double vapourFractionTS(double temperature, double entropy) {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                pSat = Region4.saturationPressureT(T),
                s1 = new Region1().specificEntropyPT(pSat, T),
                s2 = new Region2().specificEntropyPT(pSat, T);

        return min(1, (s - s1) / (s2 - s1));
    }

    /**
     * Calculate in default units.
     */
    private static class Calculate {

        /**
         * Prandtl number.
         *
         * @param p pressure [MPa]
         * @param T temperature [K]
         * @return Prandtl number [-]
         * @throws OutOfRangeException out-of-range exception
         */
        static double PrandtlPT(double p, double T) throws OutOfRangeException {

            double rho = 1 / specificVolumePT(p, T),
                    eta = dynamicViscosityRhoT(rho, T),
                    cp = getRegionPT(p, T).specificIsobaricHeatCapacityPT(p, T),
                    lambda = thermalConductivityPT(p, T) / 1e3;

            return eta * cp / lambda;
        }

        /**
         * Dielectric constant.
         *
         * @param rho density [kg/m3]
         * @param T temperature [K]
         * @return dielectric constant [-]
         * @throws OutOfRangeException out-of-range exception
         */
        static double dielectricConstantRhoT(double rho, double T) throws OutOfRangeException {

            double k = 1.380658e-23,
                    NA = 6.0221367e23,
                    alpha = 1.636e-40,
                    epsilon0 = 8.854187817e-12,
                    mu = 6.138e-30,
                    M = 0.018015268,
                    n12 = 0.196096504426e-2,
                    delta = rho / rhoc,
                    tau = Tc / T,
                    g = 1 + n12 * delta * pow(Tc / 228 / tau - 1, -1.2);
            double[][] IJn = {
                {1, 0.25, 0.978224486826},
                {1, 1.0, -0.957771379375},
                {1, 2.5, 0.237511794148},
                {2, 1.5, 0.714692244396},
                {3, 1.5, -.298217036956},
                {3, 2.5, -.108863472196},
                {4, 2, 0.949327488264e-1},
                {5, 2, -.980469816509e-2},
                {6, 5, 0.165167634970e-4},
                {7, 0.5, 0.937359795772e-4},
                {10, 10, -.123179218720e-9}};

            for (double[] ijn : IJn) {
                g += ijn[2] * pow(delta, ijn[0]) * pow(tau, ijn[1]);
            }

            double A = NA * mu * mu * rho * g / (M * epsilon0 * k * T),
                    B = NA * alpha * rho / (3 * M * epsilon0);

            return (1 + A + 5 * B + sqrt(9 + 2 * A + 18 * B + A * A + 10 * A * B + 9 * B * B)) / (4 * (1 - B));
        }

        /**
         * Dynamic viscosity.
         *
         * @param rho density [kg/m3]
         * @param T temperature [K]
         * @return dynamic viscosity [Pa-s]
         * @throws OutOfRangeException out-of-range exception
         */
        static double dynamicViscosityRhoT(double rho, double T) throws OutOfRangeException {

            double delta = rho / rhoc,
                    theta = T / Tc,
                    psi0 = 0,
                    psi1 = 0;
            double[] n0 = {0.167752e-1, 0.220462e-1, 0.6366564e-2, -0.241605e-2};
            double[][] IJn = {
                {0, 0, 0.520094},
                {0, 1, 0.850895e-1},
                {0, 2, -.108374e1},
                {0, 3, -.289555},
                {1, 0, 0.222531},
                {1, 1, 0.999115},
                {1, 2, 0.188797e1},
                {1, 3, 0.126613e1},
                {1, 5, 0.120573},
                {2, 0, -.281378},
                {2, 1, -.906851},
                {2, 2, -.772479},
                {2, 3, -.489837},
                {2, 4, -.257040},
                {3, 0, 0.161913},
                {3, 1, 0.257399},
                {4, 0, -.325372e-1},
                {4, 3, 0.698452e-1},
                {5, 4, 0.872102e-2},
                {6, 3, -.435673e-2},
                {6, 5, -.593264e-3}};

            for (int i = 0; i < n0.length; i++) {
                psi0 += n0[i] / pow(theta, i);
            }
            psi0 = sqrt(theta) / psi0;

            double[] x = {delta - 1, 1 / theta - 1};

            for (double[] ijn : IJn) {
                psi1 += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
            }
            psi1 = exp(delta * psi1);

            return psi0 * psi1 * 1e-6;
        }

        /**
         * Partial derivative of z with respect to x for constant y, as a
         * function of pressure and temperature.
         *
         * This method is for regions described by specific Gibbs free energy.
         *
         * @param p pressure [MPa]
         * @param T temperature [K]
         * @param x any quantity
         * @param y any quantity
         * @param z any quantity
         * @return partial derivative
         * @throws OutOfRangeException
         */
        private static double partialDerivativePT(double p, double T, Quantity x, Quantity y, Quantity z) throws OutOfRangeException {

            Region region = Region.getRegionPT(p, T);

            double nu = region.specificVolumePT(p, T),
                    rho = 1.0 / nu,
                    s = region.specificEntropyRhoT(rho, T),
                    cp = region.specificIsobaricHeatCapacityPT(p, T),
                    alphanu = region.isobaricCubicExpansionCoefficientPT(p, T),
                    kappaT = region.isothermalCompressibilityPT(p, T);

            double[] dx = partialDerivativesPT(p, T, x, nu, s, cp, alphanu, kappaT),
                    dy = partialDerivativesPT(p, T, y, nu, s, cp, alphanu, kappaT),
                    dz = partialDerivativesPT(p, T, z, nu, s, cp, alphanu, kappaT);

            double dx_dT = dx[0],
                    dy_dT = dy[0],
                    dz_dT = dz[0],
                    dx_dp = dx[1],
                    dy_dp = dy[1],
                    dz_dp = dz[1];

            return (dz_dp * dy_dT - dz_dT * dy_dp) / (dx_dp * dy_dT - dx_dT * dy_dp);
        }

        /**
         * Partial derivative of z with respect to x for constant y, as a
         * function of specific volume and temperature.
         *
         * This method is for region 3 described by specific Helmholtz free
         * energy.
         *
         * @param rho density [kg/m&sup3;]
         * @param T temperature [K]
         * @param x any quantity
         * @param y any quantity
         * @param z any quantity
         * @return partial derivative
         * @throws OutOfRangeException
         */
        private static double partialDerivativeRhoT(double rho, double T, Quantity x, Quantity y, Quantity z) throws OutOfRangeException {

            Region3 region3 = new Region3();
            double nu = 1.0 / rho,
                    p = region3.pressureRhoT(rho, T),
                    s = region3.specificEntropyRhoT(rho, T),
                    cv = region3.specificIsochoricHeatCapacityRhoT(rho, T),
                    alphap = region3.relativePressureCoefficientRhoT(rho, T),
                    betap = region3.isothermalStressCoefficientRhoT(rho, T);

            double[] dx = partialDerivativesNuT(nu, T, x, p, s, cv, alphap, betap),
                    dy = partialDerivativesNuT(nu, T, y, p, s, cv, alphap, betap),
                    dz = partialDerivativesNuT(nu, T, z, p, s, cv, alphap, betap);

            double dx_dnu = dx[0],
                    dy_dnu = dy[0],
                    dz_dnu = dz[0],
                    dx_dT = dx[1],
                    dy_dT = dy[1],
                    dz_dT = dz[1];

            return (dz_dnu * dy_dT - dz_dT * dy_dnu) / (dx_dnu * dy_dT - dx_dT * dy_dnu);
        }

        /**
         * Partial derivatives of the given quantity with respect to specific
         * volume and temperature for region 3 described by specific Helmholtz
         * free energy.
         *
         * @param nu specific volume [m&sup3;/kg]
         * @param T temperature [K]
         * @param quantity quantity
         * @param p pressure [MPa]
         * @param s specific entropy [kJ/kg-K]
         * @param cv
         * @param alphap
         * @param betap
         * @return partial derivatives d/d&nu; and d/dT
         * @throws Exception
         */
        private static double[] partialDerivativesNuT(double nu, double T, Quantity quantity, double p, double s, double cv, double alphap, double betap) {

            double d_dnu = Double.NaN,
                    d_dT = Double.NaN;

            switch (quantity) {
                case p:
                    d_dnu = -p * betap;
                    d_dT = p * alphap;
                    break;

                case T:
                    d_dnu = 0;
                    d_dT = 1;
                    break;

                case nu:
                    d_dnu = 1;
                    d_dT = 0;
                    break;

                case u:
                    d_dnu = p * (T * alphap - 1);
                    d_dT = cv;
                    break;

                case h:
                    d_dnu = p * (T * alphap - nu * betap);
                    d_dT = cv + p * nu * alphap;
                    break;

                case s:
                    d_dnu = p * alphap;
                    d_dT = cv / T;
                    break;

                case g:
                    d_dnu = -p * nu * betap;
                    d_dT = p * nu * alphap - s;
                    break;

                case f:
                    d_dnu = -p;
                    d_dT = -s;
                    break;
            }

            return new double[]{d_dnu, d_dT};
        }

        /**
         * Partial derivatives of the given quantity with respect to pressure
         * and temperature for regions described by specific Gibbs free energy.
         *
         * @param p pressure [MPa]
         * @param T temperature [K]
         * @param quantity quantity
         * @param nu specific volume [m&sup3;/kg]
         * @param s specific entropy [kJ/kg-K]
         * @param cp specific isobaric heat capacity
         * @param alphanu isobaric cubic expansion coefficient
         * @param kappaT isothermal compressibility
         * @return partial derivatives d/dT and d/dp
         * @throws Exception
         */
        private static double[] partialDerivativesPT(double p, double T, Quantity quantity, double nu, double s, double cp, double alphanu, double kappaT) {

            double d_dT = Double.NaN,
                    d_dp = Double.NaN;

            switch (quantity) {
                case p:
                    d_dT = 0;
                    d_dp = 1;
                    break;

                case T:
                    d_dT = 1;
                    d_dp = 0;
                    break;

                case nu:
                    d_dT = nu * alphanu;
                    d_dp = -nu * kappaT;
                    break;

                case u:
                    d_dT = cp - p * nu * alphanu;
                    d_dp = nu * (p * kappaT - T * alphanu);
                    break;

                case h:
                    d_dT = cp;
                    d_dp = nu * (1 - T * alphanu);
                    break;

                case s:
                    d_dT = cp / T;
                    d_dp = -nu * alphanu;
                    break;

                case g:
                    d_dT = -s;
                    d_dp = nu;
                    break;

                case f:
                    d_dT = -p * nu * alphanu - s;
                    d_dp = p * nu * kappaT;
                    break;
            }

            return new double[]{d_dT, d_dp};
        }

        /**
         * Refractive index.
         *
         * @param rho density [kg/m&sup3;]
         * @param T temperature [K]
         * @param lambda wavelength [&mu;m]
         * @return refractive index [-]
         * @throws OutOfRangeException out-of-range exception
         */
        static double refractiveIndexRhoTLambda(double rho, double T, double lambda) throws OutOfRangeException {

            if (T < 261.15) {
                throw new OutOfRangeException(Quantity.T, T, 261.15);

            } else if (T > 773.15) {
                throw new OutOfRangeException(Quantity.T, T, 773.15);

            } else if (rho <= 0) {
                throw new OutOfRangeException(Quantity.rho, rho, 0);

            } else if (rho > 1060) {
                throw new OutOfRangeException(Quantity.rho, rho, 1060);

            } else if (lambda < 0.2) {
                throw new OutOfRangeException(Quantity.lambda, lambda, 0.2);

            } else if (lambda > 1.1) {
                throw new OutOfRangeException(Quantity.lambda, lambda, 1.1);
            }

            double[] a = {0.244257733, 0.974634476e-2, -.373234996e-2, 0.268678472e-3, 0.158920570e-2, 0.245934259e-2, 0.900704920, -.166626219e-1};
            double delta = rho / 1e3,
                    theta = T / 273.15,
                    Lambda = lambda / 0.589,
                    Lambda2 = Lambda * Lambda,
                    LambdaIR = 5.432937,
                    LambdaUV = -0.229202,
                    A = delta * (a[0] + a[1] * delta + a[2] * theta + a[3] * Lambda2 * theta + a[4] / Lambda2 + a[5] / (Lambda2 - LambdaUV * LambdaUV) + a[6] / (Lambda2 - LambdaIR * LambdaIR) + a[7] * delta * delta);

            return sqrt((2 * A + 1) / (1 - A));
        }

        /**
         * Specific volume as a function of pressure & temperature.
         *
         * @param p pressure [MPa]
         * @param T temperature [K]
         * @return specific volume [m&sup3;/kg]
         * @throws OutOfRangeException out-of-range exception
         */
        static double specificVolumePT(double p, double T) throws OutOfRangeException {

            return getRegionPT(p, T).specificVolumePT(p, T);
        }

        /**
         * Speed of sound as a function of pressure & temperature.
         *
         * @param p pressure [MPa]
         * @param T temperature [K]
         * @return speed of sound [m/s]
         * @throws OutOfRangeException out-of-range exception
         */
        static double speedOfSoundPT(double p, double T) throws OutOfRangeException {

            return getRegionPT(p, T).speedOfSoundPT(p, T);
        }

        /**
         * Surface tension as a function of temperature.
         *
         * @param T temperature [K]
         * @return surface tension [N/m]
         * @throws OutOfRangeException out-of-range exception
         */
        static double surfaceTensionT(double T) throws OutOfRangeException {

            if (T < 273.15) {
                throw new OutOfRangeException(Quantity.T, T, 273.15);

            } else if (T > 647.096) {
                throw new OutOfRangeException(Quantity.T, T, 647.096);
            }

            double theta = T / 647.096;

            return 235.8 * pow(1 - theta, 1.256) * (1 - 0.625 * (1 - theta)) * 1e-3;
        }

        /**
         * Temperature.
         *
         * @param p pressure [MPa]
         * @param h specific enthalpy [kJ/kg]
         * @return temperature [K]
         * @throws OutOfRangeException out-of-range exception
         */
        static double temperaturePH(double p, double h) throws OutOfRangeException {

            return getRegionPH(p, h).temperaturePH(p, h);
        }

        /**
         * Thermal conductivity as a function of pressure & temperature.
         *
         * @param p pressure [MPa]
         * @param T temperature [K]
         * @return thermal conductivity []
         * @throws OutOfRangeException out-of-range exception
         */
        static double thermalConductivityPT(double p, double T) throws OutOfRangeException {

            /*
             * Coefficients
             */
            double[] n0 = {0.102811e-1, 0.299621e-1, 0.156146e-1, -.422464e-2},
                    n1 = {-.397070, 0.400302, 0.106000e1, -.171587, 0.239219e1},
                    n2 = {0.701309e-1, 0.118520e-1, 0.642857, 0.169937e-2, -.102000e1, -.411717e1, -.617937e1, 0.822994e-1, 0.100932e2, 0.308976e-2};

            double theta = T / 647.26,
                    DeltaTheta = abs(theta - 1) + n2[9],
                    rho = 1 / specificVolumePT(p, T),
                    delta = rho / 317.7,
                    Lambda0 = 0,
                    A, B = 2 + n2[7] * pow(DeltaTheta, -0.6);

            //System.out.println("rho: " + rho + ", theta: " + theta);
            for (int i = 0; i < 4; i++) {
                Lambda0 += n0[i] * pow(theta, i);
            }
            A = theta < 1 ? n2[8] / pow(DeltaTheta, 0.6) : 1 / DeltaTheta;

            double Lambda1 = n1[0] + n1[1] * delta + n1[2] * exp(n1[3] * pow(delta + n1[4], 2)),
                    Lambda2 = (n2[0] / pow(theta, 10) + n2[1]) * pow(delta, 1.8) * exp(n2[2] * (1.0 - pow(delta, 2.8))) + n2[3] * A * pow(delta, B) * exp(B / (1.0 + B) * (1.0 - pow(delta, 1.0 + B))) + n2[4] * exp(n2[5] * pow(theta, 1.5) + n2[6] / pow(delta, 5));

            return sqrt(theta) * Lambda0 + Lambda1 + Lambda2;
        }
    }

    /**
     * Quantities for partial derivatives.
     */
    public enum Quantity {

        /**
         * Pressure.
         */
        p,
        /**
         * Temperature.
         */
        T,
        /**
         * Specific volume.
         */
        nu,
        /**
         * Density.
         */
        rho,
        /**
         * Specific internal energy.
         */
        u,
        /**
         * Specific enthalpy.
         */
        h,
        /**
         * Specific entropy.
         */
        s,
        /**
         * Wavelength.
         */
        lambda,
        /**
         * Specific Gibbs free energy.
         */
        g,
        /**
         * Specific Helmholtz free energy.
         */
        f,
        /**
         * Vapour fraction.
         */
        x;

        @Override
        public String toString() {

            switch (this) {
                case p:
                    return "pressure";

                case T:
                    return "temperature";

                case u:
                    return "specific internal energy";

                case nu:
                    return "specific volume";

                case h:
                    return "specific enthalpy";

                case s:
                    return "specific entropy";

                case lambda:
                    return "wavelength";

                case rho:
                    return "density";

                case x:
                    return "vapour fraction";

                default:
                    return name().toLowerCase().replaceAll("_", " ");
            }
        }
    }

    /**
     * Unit systems.
     *
     * <table border="1"> <tr>
     * <th></th><th></th><th>Default</th><th>Engineering</th><th>SI</th><th>Imperial</th>
     * </tr>
     * <tr>
     * <td><i>&rho;</i></td><td>density</td><td>kg/m&sup3;</td><td>kg/m&sup3;</td><td>kg/m&sup3;</td><td>lb/ft&sup3;</td>
     * </tr>
     * <tr>
     * <td><i>&epsilon;</i></td><td>dielectric
     * constant</td><td>-</td><td>-</td><td>-</td><td>-</td>
     * </tr>
     * <tr>
     * <td><i>&eta;</i></td><td>dynamic
     * viscosity</td><td>Pa&middot;s</td><td>Pa&middot;s</td><td>Pa&middot;s</td><td>cP</td>
     * </tr>
     * <tr>
     * <td><i>&alpha;<sub>v</sub></i></td><td>isobaric cubic expansion
     * coefficient</td><td>1/K</td><td>1/K</td><td>1/K</td><td>1/R</td>
     * </tr>
     * <tr>
     * <td><i>&kappa;<sub>T</sub></i></td><td>isothermal
     * compressibility</td><td>1/MPa</td><td>1/MPa</td><td>1/Pa</td><td>in&sup2;/lb</td>
     * </tr>
     * <tr>
     * <td><i>&nu;</i></td><td>kinematic
     * viscosity</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>cSt</td>
     * </tr>
     * <tr>
     * <td><i>Pr</i></td><td>Prandtl
     * number</td><td>-</td><td>-</td><td>-</td><td>-</td>
     * </tr>
     * <tr>
     * <td><i>p</i></td><td>absolute
     * pressure</td><td>MPa</td><td>bar</td><td>Pa</td><td>psi</td>
     * </tr>
     * <tr>
     * <td><i>n</i></td><td>refractive
     * index</td><td>-</td><td>-</td><td>-</td><td>-</td>
     * </tr>
     * <tr>
     * <td><i>h</i></td><td>specific
     * enthalpy</td><td>kJ/kg</td><td>kJ/kg</td><td>J/kg</td><td>BTU/lb</td>
     * </tr>
     * <tr>
     * <td><i>s</i></td><td>specific
     * entropy</td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td>
     * </tr>
     * <tr>
     * <td><i>u</i></td><td>specific internal
     * energy</td><td>kJ/kg</td><td>kJ/kg</td><td>J/kg</td><td>BTU/lb</td>
     * </tr>
     * <tr>
     * <td><i>c<sub>p</sub></i></td><td>specific isobaric heat
     * capacity</td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td>
     * </tr>
     * <tr>
     * <td><i>c<sub>v</sub></i></td><td>specific isochoric heat
     * capacity</td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td>
     * </tr>
     * <tr>
     * <td><i>v</i></td><td>specific
     * volume</td><td>m&sup3;/kg</td><td>m&sup3;/kg</td><td>m&sup3;/kg</td><td>ft&sup3;/lb</td>
     * </tr>
     * <tr>
     * <td><i>w</i></td><td>speed of
     * sound</td><td>m/s</td><td>m/s</td><td>m/s</td><td>ft/s</td>
     * </tr>
     * <tr>
     * <td><i>&sigma;</i></td><td>surface
     * tension</td><td>N/m</td><td>N/m</td><td>N/m</td><td>lbf/ft</td>
     * </tr>
     * <tr>
     * <td><i>T</i></td><td>temperature</td><td>K</td><td>&deg;C</td><td>K</td><td>&deg;F</td>
     * </tr>
     * <tr>
     * <td><i>&lambda;</i></td><td>thermal
     * conductivity</td><td>W/(m&middot;K)</td><td>kW/(m&middot;K)</td><td>W/(m&middot;K)</td><td>BTU/(hr&middot;ft&middot;R)</td>
     * </tr>
     * <tr>
     * <td><i>x</i></td><td>vapour
     * fraction</td><td>-</td><td>-</td><td>-</td><td>-</td>
     * </tr>
     * <tr>
     * <td><i>&lambda;</i></td><td>wave
     * length</td><td>&mu;m</td><td>&mu;m</td><td>m</td><td>in</td>
     * </tr>
     * </table>
     */
    public enum UnitSystem {

        /**
         * Default unit system.
         */
        DEFAULT(new double[]{1, 0}, new double[]{1, 0}, new double[]{1, 0},
                new double[]{1, 0}, new double[]{1, 0}, new double[]{1, 0},
                new double[]{1, 0}, new double[]{1, 0}, new double[]{1, 0},
                new double[]{1, 0}, new double[]{1, 0}, new double[]{1, 0},
                new double[]{1, 0}, new double[]{1, 0}, new double[]{1, 0},
                new double[]{1, 0}),
        /**
         * Engineering units.
         */
        ENGINEERING(new double[]{1, 0}, // compressibility
                new double[]{1, 0}, // density
                new double[]{1, 0}, // dynamic viscosity
                new double[]{1, 0}, // isobaric cubic expansion coefficient
                new double[]{1, 0}, // kinematic viscosity
                new double[]{0.1, 0}, // pressure
                new double[]{1, 0}, // specific energy
                new double[]{1, 0}, // specific enthalpy
                new double[]{1, 0}, // specific entropy
                new double[]{1, 0}, // specific heat capacity
                new double[]{1, 0}, // specific volume
                new double[]{1, 0}, // speed of sound
                new double[]{1, 0}, // surface tension
                new double[]{1, 273.15}, // temperature
                new double[]{1e3, 0}, // thermal conductivity
                new double[]{1, 0}), // wavelength
        /**
         * SI unit system.
         */
        SI(new double[]{1e6, 0}, // compressibility
                new double[]{1, 0}, // density
                new double[]{1, 0}, // dynamic viscosity
                new double[]{1, 0}, // isobaric cubic expansion coefficient
                new double[]{1, 0}, // kinematic viscosity
                new double[]{1e-6, 0}, // pressure
                new double[]{1, 0}, // specific energy
                new double[]{1e-3, 0}, // specific enthalpy
                new double[]{1e-3, 0}, // specific entropy
                new double[]{1e-3, 0}, // specific heat capacity
                new double[]{1, 0}, // specific volume
                new double[]{1, 0}, // speed of sound
                new double[]{1, 0}, // surface tension
                new double[]{1, 0}, // temperature
                new double[]{1, 0}, // thermal conductivity
                new double[]{1e6, 0}), // wavelength
        /**
         * British imperial unit system.
         */
        IMPERIAL(new double[]{1e6 * in2 / lb, 0}, // compressibility
                new double[]{lb / ft3, 0}, // density
                new double[]{1e-3, 0}, // dynamic viscosity
                new double[]{1 / Ra, 0}, // isobaric cubic expansion coefficient
                new double[]{1e-6, 0}, // kinematic viscosity [centiStokes]
                new double[]{psi, 0}, // pressure
                new double[]{BTU / lb, 0}, // specific energy
                new double[]{BTU / lb, 0}, // specific enthalpy
                new double[]{BTU / (lb * Ra), 0}, // specific entropy
                new double[]{BTU / (lb * Ra), 0}, // specific heat capacity
                new double[]{ft3 / lb, 0}, // specific volume
                new double[]{ft, 0}, // speed of sound
                new double[]{lbf / ft, 0}, // surface tension
                new double[]{5d / 9, 459.67 * 5 / 9},// temperature
                new double[]{1e3 * BTU / (hr * ft * Ra), 0}, // thermal conductivity
                new double[]{in * 1e6, 0}); // wavelength

        final double[] COMPRESSIBILITY,
                DENSITY,
                DYNAMIC_VISCOSITY,
                ISOBARIC_CUBIC_EXPANSION_COEFFICIENT,
                KINEMATIC_VISCOSITY,
                PRESSURE,
                SPECIFIC_ENERGY,
                SPECIFIC_ENTHALPY,
                SPECIFIC_ENTROPY,
                SPECIFIC_HEAT_CAPACITY,
                SPECIFIC_VOLUME,
                SPEED_OF_SOUND,
                SURFACE_TENSION,
                TEMPERATURE,
                THERMAL_CONDUCTIVITY,
                WAVELENGTH;

        /**
         * Scale and bias values for conversion to default unit system.
         *
         * @param compressibility
         * @param density
         * @param dynamicViscosity
         * @param isobaricCubicExpansionCoefficient
         * @param kinematicViscosity
         * @param pressure
         * @param specificEnergy
         * @param specificEnthalpy
         * @param specificEntropy
         * @param specificHeatCapacity
         * @param specificVolume
         * @param surfaceTension
         * @param temperature
         * @param thermalConductivity
         * @param wavelength
         */
        UnitSystem(double[] compressibility,
                double[] density,
                double[] dynamicViscosity,
                double[] isobaricCubicExpansionCoefficient,
                double[] kinematicViscosity,
                double[] pressure,
                double[] specificEnergy,
                double[] specificEnthalpy,
                double[] specificEntropy,
                double[] specificHeatCapacity,
                double[] specificVolume,
                double[] speedOfSound,
                double[] surfaceTension,
                double[] temperature,
                double[] thermalConductivity,
                double[] wavelength) {

            COMPRESSIBILITY = compressibility;
            DENSITY = density;
            DYNAMIC_VISCOSITY = dynamicViscosity;
            ISOBARIC_CUBIC_EXPANSION_COEFFICIENT = isobaricCubicExpansionCoefficient;
            KINEMATIC_VISCOSITY = kinematicViscosity;
            PRESSURE = pressure;
            SPECIFIC_ENERGY = specificEnergy;
            SPECIFIC_ENTHALPY = specificEnthalpy;
            SPECIFIC_ENTROPY = specificEntropy;
            SPECIFIC_HEAT_CAPACITY = specificHeatCapacity;
            SPECIFIC_VOLUME = specificVolume;
            SPEED_OF_SOUND = speedOfSound;
            SURFACE_TENSION = surfaceTension;
            TEMPERATURE = temperature;
            THERMAL_CONDUCTIVITY = thermalConductivity;
            WAVELENGTH = wavelength;
        }
    }
}
