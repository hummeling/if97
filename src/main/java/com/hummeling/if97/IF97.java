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
 * along with IF97. If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2018 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static java.lang.StrictMath.*;
import java.util.*;

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
 * Website: <a href="https://www.if97.software/">www.if97.software</a>.
 *
 * @author Ralph Hummeling
 * (<a href="https://www.hummeling.com">www.hummeling.com</a>)
 */
public class IF97 {

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
    public static final double T0 = 273.15;
    public static final double p0 = Region.REGION4.saturationPressureT(T0);
    /**
     * Critical pressure [MPa].
     */
    public static final double pc = 22.064;
    /**
     * Critical temperature [K].
     */
    public static final double Tc = 647.096;
    /**
     * Critical enthalpy [kJ/kg].
     */
    public static final double hc = 2087.546845;
    /**
     * Critical density [kg/m3].
     */
    public static final double rhoc = 322;
    /**
     * Critical entropy [kJ/kg-K].
     */
    public static final double sc = Region.REGION3.specificEntropyRhoT(rhoc, Tc);
    /**
     * British thermal unit acc. International standard ISO 31-4 on Quantities
     * and units—Part 4: Heat, Appendix A [kJ]
     */
    public static final double BTU = 1.055056;
    /**
     * Gravitational accelleration [m/s^2]
     */
    public static final double g = 9.80665;
    /**
     * International avoirdupois pound-mass [kg]
     */
    public static final double lb = 0.45359237;
    private static final double ft, ft2, ft3, hr, in, in2, lbf, psi, Ra;
    private static final Collection<Quantity> PARTIAL_DERIVATIVE_QUANTITIES;

    static {
        ft = 0.3048; // foot [m]
        ft2 = ft * ft; // square foot [m^2]
        ft3 = ft * ft2; // cubic foot [m^3]
        hr = 3600; // hour [s]
        in = ft / 12; // inch [m]
        in2 = in * in; // square inch [m^2]
        lbf = lb * g;
        psi = 1e-6 * lbf / in2; // pounds per square inch [MPa]
        Ra = 5.0 / 9.0; // Rankine [K]
        PARTIAL_DERIVATIVE_QUANTITIES = Quantity.getPartialDerivatives();
    }

    private UnitSystem UNIT_SYSTEM;

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

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            Region region = Region.getRegionHS(h, s);

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
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return Prandtl number
     * @throws OutOfRangeException out-of-range exception
     */
    public double PrandtlPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy);

        try {
            double T = Region.getRegionPH(p, h).temperaturePH(p, h);

            return Calculate.PrandtlPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Prandtl number.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return Prandtl number
     * @throws OutOfRangeException out-of-range exception
     */
    public double PrandtlPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            double T = Region.getRegionPS(p, s).temperaturePS(p, s);

            return Calculate.PrandtlPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Prandtl number.
     *
     * @param pressure absolute pressure
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
     * Isothermal compressibility as a function of specific enthalpy &amp;
     * specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return isothermal compressibility
     * @throws OutOfRangeException out-of-range exception
     */
    public double compressibilityHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                kappaT;

        try {
            Region region = Region.getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);

            kappaT = region.isothermalCompressibilityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.COMPRESSIBILITY, kappaT);
    }

    /**
     * Isothermal compressibility as a function of pressure &amp; specific
     * enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return isothermal compressibility
     * @throws OutOfRangeException out-of-range exception
     */
    public double compressibilityPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                kappaT;

        try {
            Region region = Region.getRegionPH(p, h);

            double T = region.temperaturePH(p, h);

            kappaT = region.isothermalCompressibilityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.COMPRESSIBILITY, kappaT);
    }

    /**
     * Isothermal compressibility as a function of pressure &amp; specific
     * entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return isothermal compressibility
     * @throws OutOfRangeException out-of-range exception
     */
    public double compressibilityPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                kappaT;

        try {
            Region region = Region.getRegionPS(p, s);

            double T = region.temperaturePS(p, s);

            kappaT = region.isothermalCompressibilityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.COMPRESSIBILITY, kappaT);
    }

    /**
     * Isothermal compressibility as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return isothermal compressibility
     * @throws OutOfRangeException out-of-range exception
     */
    public double compressibilityPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                kappaT;

        try {
            kappaT = Region.getRegionPT(p, T).isothermalCompressibilityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.COMPRESSIBILITY, kappaT);
    }

    /**
     * Compression factor (real-gas factor) as a function of pressure &amp;
     * temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return compression factor
     * @throws OutOfRangeException out-of-range exception
     */
    public double compressionFactorPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            return 1e3 * p * Region.getRegionPT(p, T).specificVolumePT(p, T) / (R * T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    public static double convertFromDefault(UnitSystem unitSystem, IF97.Quantity quantity, double value) {

        switch (quantity) {
            case T:
                return convertFromDefault(unitSystem.TEMPERATURE, value);

            case f:
            case g:
            case u:
                return convertFromDefault(unitSystem.SPECIFIC_ENERGY, value);

            case h:
                return convertFromDefault(unitSystem.SPECIFIC_ENTHALPY, value);

            case lambda:
                return convertFromDefault(unitSystem.THERMAL_CONDUCTIVITY, value);

            case lambdaL:
                return convertFromDefault(unitSystem.WAVELENGTH, value);

            case p:
                return convertFromDefault(unitSystem.PRESSURE, value);

            case rho:
                return convertFromDefault(unitSystem.DENSITY, value);

            case s:
                return convertFromDefault(unitSystem.SPECIFIC_ENTROPY, value);

            case v:
                return convertFromDefault(unitSystem.SPECIFIC_VOLUME, value);

            case x:
                return value;

            default:
                throw new IllegalArgumentException("No conversion available for: " + quantity);
        }
    }

    public static double convertFromDefault(double[] quantity, double value) {
        return (value - quantity[1]) / quantity[0];
    }

    public static double convertToDefault(double[] quantity, double value) {
        return value * quantity[0] + quantity[1];
    }

    /**
     * Density as a function of specific enthalpy &amp; specific entropy.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>1.0 / specificVolumeHS(enthalpy, entropy)</code>.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return density
     * @throws OutOfRangeException out-of-range exception
     * @see #specificVolumeHS(double, double)
     */
    public double densityHS(double enthalpy, double entropy) throws OutOfRangeException {
        return 1 / specificVolumeHS(enthalpy, entropy);
    }

    /**
     * Density as a function of pressure &amp; specific enthalpy.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>1.0 / specificVolumePH(pressure, enthalpy)</code>.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return density
     * @throws OutOfRangeException out-of-range exception
     * @see #specificVolumePH(double, double)
     */
    public double densityPH(double pressure, double enthalpy) throws OutOfRangeException {
        return 1 / specificVolumePH(pressure, enthalpy);
    }

    /**
     * Density as a function of pressure &amp; specific entropy.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>1.0 / specificVolumePS(pressure, entropy)</code>.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return density
     * @throws OutOfRangeException out-of-range exception
     * @see #specificVolumePS(double, double)
     */
    public double densityPS(double pressure, double entropy) throws OutOfRangeException {
        return 1 / specificVolumePS(pressure, entropy);
    }

    /**
     * Density as a function of pressure &amp; temperature.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>1.0 / specificVolumePT(pressure, temperature)</code>.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return density
     * @throws OutOfRangeException out-of-range exception
     * @see #specificVolumePT(double, double)
     */
    public double densityPT(double pressure, double temperature) throws OutOfRangeException {
        return 1 / specificVolumePT(pressure, temperature);
    }

    /**
     * Density as a function of pressure &amp; vapour fraction.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>1.0 / specificVolumePX(pressure, vapour fraction)</code>.
     *
     * @param pressure absolute pressure
     * @param vapourFraction vapour fraction
     * @return density
     * @throws OutOfRangeException out-of-range exception
     * @see #specificVolumePX(double, double)
     */
    public double densityPX(double pressure, double vapourFraction) throws OutOfRangeException {
        return 1 / specificVolumePX(pressure, vapourFraction);
    }

    /**
     * Density as a function of temperature &amp; vapour fraction.
     *
     * <p>
     * This is a convenience method which simply calls
     * <code>1.0 / specificVolumeTX(temperature, vapour fraction)</code>.
     *
     * @param temperature temperature
     * @param vapourFraction vapour fraction
     * @return density
     * @throws OutOfRangeException out-of-range exception
     * @see #specificVolumeTX(double, double)
     */
    public double densityTX(double temperature, double vapourFraction) throws OutOfRangeException {
        return 1 / specificVolumeTX(temperature, vapourFraction);
    }

    /**
     * Dielectric constant as a function of specific enthalpy and specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return dielectric constant [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #dielectricConstantRhoT(double, double)
     */
    public double dielectricConstantHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            Region region = Region.getRegionHS(h, s);

            double v = region.specificVolumeHS(h, s),
                    T = region.temperatureHS(h, s);

            return Calculate.dielectricConstantRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dielectric constant as a function of pressure and specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return dielectric constant [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #dielectricConstantRhoT(double, double)
     */
    public double dielectricConstantPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy);

        try {
            Region region = Region.getRegionPH(p, h);

            double v = region.specificVolumePH(p, h),
                    T = region.temperaturePH(p, h);

            return Calculate.dielectricConstantRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dielectric constant as a function of pressure and specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return dielectric constant [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #dielectricConstantRhoT(double, double)
     */
    public double dielectricConstantPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            Region region = Region.getRegionPS(p, s);

            double v = region.specificVolumePS(p, s),
                    T = region.temperaturePS(p, s);

            return Calculate.dielectricConstantRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dielectric constant as a function of pressure and temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return dielectric constant [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #dielectricConstantRhoT(double, double)
     */
    public double dielectricConstantPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            double v = Region.getRegionPT(p, T).specificVolumePT(p, T);

            return Calculate.dielectricConstantRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dielectric constant (relative static dielectric constant or relative
     * static permittivity) as a function of density and temperature.
     *
     * @param density density
     * @param temperature temperature
     * @return dielectric constant [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double dielectricConstantRhoT(double density, double temperature) throws OutOfRangeException {

        double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            return Calculate.dielectricConstantRhoT(rho, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Dynamic viscosity as a function of specific enthalpy &amp; specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return dynamic viscosity
     * @throws OutOfRangeException out-of-range exception
     * @see #dynamicViscosityRhoT(double, double)
     */
    public double dynamicViscosityHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                eta;

        try {
            Region region = Region.getRegionHS(h, s);

            double v = region.specificVolumeHS(h, s),
                    T = region.temperatureHS(h, s);

            eta = Calculate.dynamicViscosityRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, eta);
    }

    /**
     * Dynamic viscosity as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return dynamic viscosity
     * @throws OutOfRangeException out-of-range exception
     * @see #dynamicViscosityRhoT(double, double)
     */
    public double dynamicViscosityPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                eta;

        try {
            Region region = Region.getRegionPH(p, h);

            double v = region.specificVolumePH(p, h),
                    T = region.temperaturePH(p, h);

            eta = Calculate.dynamicViscosityRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, eta);
    }

    /**
     * Dynamic viscosity as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return dynamic viscosity
     * @throws OutOfRangeException out-of-range exception
     * @see #dynamicViscosityRhoT(double, double)
     */
    public double dynamicViscosityPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                eta;

        try {
            Region region = Region.getRegionPS(p, s);

            double v = region.specificVolumePS(p, s),
                    T = region.temperaturePS(p, s);

            eta = Calculate.dynamicViscosityRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, eta);
    }

    /**
     * Dynamic viscosity as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return dynamic viscosity
     * @throws OutOfRangeException out-of-range exception
     * @see #dynamicViscosityRhoT(double, double)
     */
    public double dynamicViscosityPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                eta;

        try {
            double v = Region.getRegionPT(p, T).specificVolumePT(p, T);

            eta = Calculate.dynamicViscosityRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, eta);
    }

    /**
     * Dynamic viscosity as a function of density &amp; temperature.
     *
     * @param density density
     * @param temperature temperature
     * @return dynamic viscosity
     * @throws OutOfRangeException out-of-range exception
     */
    public double dynamicViscosityRhoT(double density, double temperature) throws OutOfRangeException {

        double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                eta;

        try {
            eta = Calculate.dynamicViscosityRhoT(rho, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, eta);
    }

    public String getRegionPT(double pressure, double temperature) throws OutOfRangeException {

        double p = IF97.convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = IF97.convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            return Region.getRegionPT(p, T).getName();

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Gets the unit system.
     *
     * @return unit system
     */
    public final UnitSystem getUnitSystem() {
        return UNIT_SYSTEM;
    }

    /**
     * Isentropic exponent as a function of specific enthalpy &amp; specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return isentropic exponent
     * @throws OutOfRangeException out-of-range exception
     * @see #isentropicExponentPT(double, double)
     */
    public double isentropicExponentHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            Region region = Region.getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);

            return region.isentropicExponentPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isentropic exponent as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return isentropic exponent
     * @throws OutOfRangeException out-of-range exception
     * @see #isentropicExponentPT(double, double)
     */
    public double isentropicExponentPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy);

        try {
            Region region = Region.getRegionPH(p, h);

            double T = region.temperaturePH(p, h);

            return region.isentropicExponentPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isentropic exponent as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return isentropic exponent
     * @throws OutOfRangeException out-of-range exception
     * @see #isentropicExponentPT(double, double)
     */
    public double isentropicExponentPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            Region region = Region.getRegionPS(p, s);

            double T = region.temperaturePS(p, s);

            return region.isentropicExponentPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isentropic exponent as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return isentropic exponent
     * @throws OutOfRangeException out-of-range exception
     */
    public double isentropicExponentPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            return Region.getRegionPT(p, T).isentropicExponentPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Isobaric cubic expansion coefficient as a function of specific enthalpy
     * &amp; specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return isobaric cubic expansion coefficient
     * @throws OutOfRangeException out-of-range exception
     * @see #isobaricCubicExpansionCoefficientPT(double, double)
     */
    public double isobaricCubicExpansionCoefficientHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                alphaV;

        try {
            Region region = Region.getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);

            alphaV = region.isobaricCubicExpansionCoefficientPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.ISOBARIC_CUBIC_EXPANSION_COEFFICIENT, alphaV);
    }

    /**
     * Isobaric cubic expansion coefficient as a function of pressure &amp;
     * specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return isobaric cubic expansion coefficient
     * @throws OutOfRangeException out-of-range exception
     * @see #isobaricCubicExpansionCoefficientPT(double, double)
     */
    public double isobaricCubicExpansionCoefficientPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                alphaV;

        try {
            Region region = Region.getRegionPH(p, h);

            double T = region.temperaturePH(p, h);

            alphaV = region.isobaricCubicExpansionCoefficientPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.ISOBARIC_CUBIC_EXPANSION_COEFFICIENT, alphaV);
    }

    /**
     * Isobaric cubic expansion coefficient as a function of pressure &amp;
     * specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return isobaric cubic expansion coefficient
     * @throws OutOfRangeException out-of-range exception
     * @see #isobaricCubicExpansionCoefficientPT(double, double)
     */
    public double isobaricCubicExpansionCoefficientPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                alphaV;

        try {
            Region region = Region.getRegionPS(p, s);

            double T = region.temperaturePS(p, s);

            alphaV = region.isobaricCubicExpansionCoefficientPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.ISOBARIC_CUBIC_EXPANSION_COEFFICIENT, alphaV);
    }

    /**
     * Isobaric cubic expansion coefficient as a function of pressure &amp;
     * temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return isobaric cubic expansion coefficient
     * @throws OutOfRangeException out-of-range exception
     */
    public double isobaricCubicExpansionCoefficientPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                alphaV;

        try {
            alphaV = Region.getRegionPT(p, T).isobaricCubicExpansionCoefficientPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.ISOBARIC_CUBIC_EXPANSION_COEFFICIENT, alphaV);
    }

    /**
     * Specific isobaric heat capacity as a function of specific enthalpy &amp;
     * specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return specific isobaric heat capacity
     * @throws OutOfRangeException out-of-range exception
     * @see #isobaricHeatCapacityPT(double, double)
     */
    public double isobaricHeatCapacityHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                cp;

        try {
            Region region = Region.getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);

            cp = region.specificIsobaricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cp);
    }

    /**
     * Specific isobaric heat capacity as a function of pressure &amp; specific
     * enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return specific isobaric heat capacity
     * @throws OutOfRangeException out-of-range exception
     * @see #isobaricHeatCapacityPT(double, double)
     */
    public double isobaricHeatCapacityPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                cp;

        try {
            Region region = Region.getRegionPH(p, h);

            double T = region.temperaturePH(p, h);

            cp = region.specificIsobaricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cp);
    }

    /**
     * Specific isobaric heat capacity as a function of pressure &amp; specific
     * entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return specific isobaric heat capacity
     * @throws OutOfRangeException out-of-range exception
     * @see #isobaricHeatCapacityPT(double, double)
     */
    public double isobaricHeatCapacityPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                cp;

        try {
            Region region = Region.getRegionPS(p, s);

            double T = region.temperaturePS(p, s);

            cp = region.specificIsobaricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cp);
    }

    /**
     * Specific isobaric heat capacity as a function of pressure &amp;
     * temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return specific isobaric heat capacity
     * @throws OutOfRangeException out-of-range exception
     */
    public double isobaricHeatCapacityPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                cp;

        try {
            cp = Region.getRegionPT(p, T).specificIsobaricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cp);
    }

    /**
     * Specific isochoric heat capacity as a function of specific enthalpy &amp;
     * specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return specific isochoric heat capacity
     * @throws OutOfRangeException out-of-range exception
     * @see #isochoricHeatCapacityPT(double, double)
     */
    public double isochoricHeatCapacityHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                cv;

        try {
            Region region = Region.getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);

            cv = region.specificIsochoricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cv);
    }

    /**
     * Specific isochoric heat capacity as a function of pressure &amp; specific
     * enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return specific isochoric heat capacity
     * @throws OutOfRangeException out-of-range exception
     * @see #isochoricHeatCapacityPT(double, double)
     */
    public double isochoricHeatCapacityPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                cv;

        try {
            Region region = Region.getRegionPH(p, h);

            double T = region.temperaturePH(p, h);

            cv = region.specificIsochoricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cv);
    }

    /**
     * Specific isochoric heat capacity as a function of pressure &amp; specific
     * entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return specific isochoric heat capacity
     * @throws OutOfRangeException out-of-range exception
     * @see #isochoricHeatCapacityPT(double, double)
     */
    public double isochoricHeatCapacityPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                cv;

        try {
            Region region = Region.getRegionPS(p, s);

            double T = region.temperaturePS(p, s);

            cv = region.specificIsochoricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cv);
    }

    /**
     * Specific isochoric heat capacity as a function of pressure &amp;
     * temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return specific isochoric heat capacity
     * @throws OutOfRangeException out-of-range exception
     */
    public double isochoricHeatCapacityPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                cv;

        try {
            cv = Region.getRegionPT(p, T).specificIsochoricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_HEAT_CAPACITY, cv);
    }

    /**
     * Kinematic viscosity as a function of specific enthalpy &amp; specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return kinematic viscosity
     * @throws OutOfRangeException out-of-range exception
     * @see #kinematicViscosityRhoT(double, double)
     */
    public double kinematicViscosityHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                nu;

        try {
            Region region = Region.getRegionHS(h, s);

            double v = region.specificVolumeHS(h, s),
                    T = region.temperatureHS(h, s);

            nu = Calculate.dynamicViscosityRhoT(1 / v, T) * v;

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, nu);
    }

    /**
     * Kinematic viscosity as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return kinematic viscosity
     * @throws OutOfRangeException out-of-range exception
     * @see #kinematicViscosityRhoT(double, double)
     */
    public double kinematicViscosityPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                nu;

        try {
            Region region = Region.getRegionPH(p, h);

            double v = region.specificVolumePH(p, h),
                    T = region.temperaturePH(p, h);

            nu = Calculate.dynamicViscosityRhoT(1 / v, T) * v;

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.KINEMATIC_VISCOSITY, nu);
    }

    /**
     * Kinematic viscosity as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return kinematic viscosity
     * @throws OutOfRangeException out-of-range exception
     * @see #kinematicViscosityRhoT(double, double)
     */
    public double kinematicViscosityPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                nu;

        try {
            Region region = Region.getRegionPS(p, s);

            double v = region.specificVolumePS(p, s),
                    T = region.temperaturePS(p, s);

            nu = Calculate.dynamicViscosityRhoT(1 / v, T) * v;

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.DYNAMIC_VISCOSITY, nu);
    }

    /**
     * Kinematic viscosity as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return kinematic viscosity
     * @throws OutOfRangeException out-of-range exception
     * @see #kinematicViscosityRhoT(double, double)
     */
    public double kinematicViscosityPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                nu;

        try {
            double v = Region.getRegionPT(p, T).specificVolumePT(p, T);

            nu = Calculate.dynamicViscosityRhoT(1 / v, T) * v;

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.KINEMATIC_VISCOSITY, nu);
    }

    /**
     * Kinematic viscosity as a function of density &amp; temperature.
     *
     * @param density density
     * @param temperature temperature
     * @return kinematic viscosity
     * @throws OutOfRangeException out-of-range exception
     */
    public double kinematicViscosityRhoT(double density, double temperature) throws OutOfRangeException {

        double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                nu;

        try {
            nu = Calculate.dynamicViscosityRhoT(rho, T) / rho;

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.KINEMATIC_VISCOSITY, nu);
    }

    /**
     * Partial derivative of z with respect to x for constant y, as a function
     * of pressure and temperature.
     *
     * <p>
     * (<sup>&part;z</sup>/<sub>&part;x</sub>)<sub>y</sub>(p, T) </p>
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @param x any {@link Quantity} part of the set returned by
     * {@link Quantity#getPartialDerivatives()}
     * @param y any {@link Quantity} part of the set returned by
     * {@link Quantity#getPartialDerivatives()}
     * @param z any {@link Quantity} part of the set returned by
     * {@link Quantity#getPartialDerivatives()}
     * @return partial derivative in default units
     * @throws OutOfRangeException out-of-range exception
     * @see Quantity#getPartialDerivatives()
     */
    public double partialDerivativePT(double pressure, double temperature, Quantity x, Quantity y, Quantity z) throws OutOfRangeException {

        if (!PARTIAL_DERIVATIVE_QUANTITIES.contains(x)) {
            throw new IllegalArgumentException("Partial derivative with respect to " + x + " is not supported, see IF97.Quantity.getPartialDerivatives() for supported quantities.");

        } else if (!PARTIAL_DERIVATIVE_QUANTITIES.contains(y)) {
            throw new IllegalArgumentException("Partial derivative for constant " + y + " is not supported, see IF97.Quantity.getPartialDerivatives() for supported quantities.");

        } else if (!PARTIAL_DERIVATIVE_QUANTITIES.contains(z)) {
            throw new IllegalArgumentException("Partial derivative of " + z + " is not supported, see IF97.Quantity.getPartialDerivatives() for supported quantities.");
        }
        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            Region region = Region.getRegionPT(p, T);

            if (region instanceof Region3) {
                double v = region.specificVolumePT(p, T);

                return Calculate.partialDerivativeRhoT(1 / v, T, x, y, z);
            }
            return Calculate.partialDerivativePT(region, p, T, x, y, z);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Partial derivative of z with respect to x for constant y, as a function
     * of density and temperature, valid in region 3 only!
     *
     * <p>
     * (<sup>&part;z</sup>/<sub>&part;x</sub>)<sub>y</sub>(&rho;, T) </p>
     *
     * @param density density
     * @param temperature temperature
     * @param x any {@link Quantity} part of the set returned by
     * {@link Quantity#getPartialDerivatives()}
     * @param y any {@link Quantity} part of the set returned by
     * {@link Quantity#getPartialDerivatives()}
     * @param z any {@link Quantity} part of the set returned by
     * {@link Quantity#getPartialDerivatives()}
     * @return partial derivative in default units
     * @throws OutOfRangeException out-of-range exception
     * @see Quantity#getPartialDerivatives()
     */
    public double partialDerivativeRhoT(double density, double temperature, Quantity x, Quantity y, Quantity z) throws OutOfRangeException {

        double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature);

        try {
            return Calculate.partialDerivativeRhoT(rho, T, x, y, z);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Pressure as a function of specific enthalpy &amp; specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return pressure
     * @throws OutOfRangeException out-of-range exception
     */
    public double pressureHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                p;

        try {
            p = Region.getRegionHS(h, s).pressureHS(h, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.PRESSURE, p);
    }

    /**
     * Refractive index as a function of specific enthalpy, specific entropy
     * &amp; wave length.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @param wavelength wavelength
     * @return refractive index [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double refractiveIndexHSLambda(double enthalpy, double entropy, double wavelength) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                lambda = convertToDefault(UNIT_SYSTEM.WAVELENGTH, wavelength);

        try {
            Region region = Region.getRegionHS(h, s);

            double v = region.specificVolumeHS(h, s),
                    T = region.temperatureHS(h, s);

            return Calculate.refractiveIndexRhoTLambda(1 / v, T, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Refractive index as a function of pressure, specific enthalpy &amp; wave
     * length.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @param wavelength wavelength
     * @return refractive index [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double refractiveIndexPHLambda(double pressure, double enthalpy, double wavelength) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                lambda = convertToDefault(UNIT_SYSTEM.WAVELENGTH, wavelength);

        try {
            Region region = Region.getRegionPH(p, h);

            double v = region.specificVolumePH(p, h),
                    T = region.temperaturePH(p, h);

            return Calculate.refractiveIndexRhoTLambda(1 / v, T, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Refractive index as a function of pressure, specific entropy &amp; wave
     * length.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @param wavelength wavelength
     * @return refractive index [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double refractiveIndexPSLambda(double pressure, double entropy, double wavelength) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                lambda = convertToDefault(UNIT_SYSTEM.WAVELENGTH, wavelength);

        try {
            Region region = Region.getRegionPS(p, s);

            double v = region.specificVolumePS(p, s),
                    T = region.temperaturePS(p, s);

            return Calculate.refractiveIndexRhoTLambda(1 / v, T, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Refractive index as a function of pressure, temperature &amp; wave
     * length.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @param wavelength wavelength
     * @return refractive index [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double refractiveIndexPTLambda(double pressure, double temperature, double wavelength) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                lambda = convertToDefault(UNIT_SYSTEM.WAVELENGTH, wavelength);

        try {
            double v = Region.getRegionPT(p, T).specificVolumePT(p, T);

            return Calculate.refractiveIndexRhoTLambda(1 / v, T, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Refractive index as a function of density, temperature &amp; wave length.
     *
     * @param density density
     * @param temperature temperature
     * @param waveLength wave length
     * @return refractive index [-]
     * @throws OutOfRangeException out-of-range exception
     */
    public double refractiveIndexRhoTLambda(double density, double temperature, double waveLength) throws OutOfRangeException {

        double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                lambda = convertToDefault(UNIT_SYSTEM.WAVELENGTH, waveLength);

        try {
            return Calculate.refractiveIndexRhoTLambda(rho, T, lambda);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Saturation pressure as a function of specific enthalpy &amp; specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return saturation pressure
     * @throws OutOfRangeException out-of-range exception
     */
    public double saturationPressureHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                p;

        try {
            Region.REGION4.checkHS(h, s);

            p = Region.REGION4.pressureHS(h, s);

            if (p < p0) {
                throw new OutOfRangeException(Quantity.p, p, p0);
            }
        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.PRESSURE, p);
    }

    /**
     * Saturation pressure as a function of temperature.
     *
     * @param temperature saturation temperature
     * @return saturation pressure
     * @throws OutOfRangeException out-of-range exception
     */
    public double saturationPressureT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                p;

        try {
            Region.REGION4.checkT(T);

            p = Region.REGION4.saturationPressureT(T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.PRESSURE, p);
    }

    /**
     * Saturation temperature as a function of specific enthalpy &amp; specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return saturation temperature
     * @throws OutOfRangeException out-of-range exception
     */
    public double saturationTemperatureHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                T;

        try {
            Region.REGION4.checkHS(h, s);

            T = Region.REGION4.temperatureHS(h, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);
    }

    /**
     * Saturation temperature as a function of pressure.
     *
     * @param pressure saturation pressure
     * @return saturation temperature
     * @throws OutOfRangeException out-of-range exception
     */
    public double saturationTemperatureP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T;

        try {
            Region.REGION4.checkP(p);

            T = Region.REGION4.saturationTemperatureP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);
    }

    /**
     * Sets (changes) the unit system.
     *
     * @param unitSystem unit system
     */
    public final void setUnitSystem(UnitSystem unitSystem) {
        UNIT_SYSTEM = unitSystem;
    }

    /**
     * Specific enthalpy as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEnthalpyPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                h;

        try {
            h = Region.getRegionPS(p, s).specificEnthalpyPS(p, s);

        } catch (OutOfRangeException exception) {
            throw exception.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);
    }

    /**
     * Specific enthalpy as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEnthalpyPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                h;

        try {
            h = Region.getRegionPT(p, T).specificEnthalpyPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);
    }

    /**
     * Specific enthalpy as a function of pressure &amp; vapour fraction.
     *
     * @param pressure absolute pressure
     * @param vapourFraction vapour fraction [-]
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEnthalpyPX(double pressure, double vapourFraction) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h;

        try {
            Region.REGION4.checkP(p);
            Region.REGION4.checkX(vapourFraction);

            h = Region.REGION4.specificEnthalpyPX(p, vapourFraction);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);
    }

    /**
     * Specific enthalpy as a function of pressure for saturated liquid.
     *
     * @param pressure saturation pressure
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificEnthalpyPX(double, double)
     */
    public double specificEnthalpySaturatedLiquidP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h;

        try {
            Region.REGION4.checkP(p);

            h = Region.REGION4.specificEnthalpySaturatedLiquidP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);
    }

    /**
     * Specific enthalpy as a function of temperature for saturated liquid.
     *
     * @param temperature saturation temperature
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificEnthalpyTX(double, double)
     */
    public double specificEnthalpySaturatedLiquidT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                h;

        try {
            Region.REGION4.checkT(T);

            h = Region.REGION4.specificEnthalpySaturatedLiquidP(Region.REGION4.saturationPressureT(T));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);
    }

    /**
     * Specific enthalpy as a function of pressure for saturated vapour.
     *
     * @param pressure saturation pressure
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificEnthalpyPX(double, double)
     */
    public double specificEnthalpySaturatedVapourP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h;

        try {
            Region.REGION4.checkP(p);

            h = Region.REGION4.specificEnthalpySaturatedVapourP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);
    }

    /**
     * Specific enthalpy as a function of temperature for saturated vapour.
     *
     * @param temperature saturation temperature
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificEnthalpyTX(double, double)
     */
    public double specificEnthalpySaturatedVapourT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                h;

        try {
            Region.REGION4.checkT(T);

            h = Region.REGION4.specificEnthalpySaturatedVapourP(Region.REGION4.saturationPressureT(T));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);
    }

    /**
     * Specific enthalpy as a function of temperature &amp; vapour fraction.
     *
     * @param temperature temperature
     * @param vapourFraction vapour fraction [-]
     * @return specific enthalpy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEnthalpyTX(double temperature, double vapourFraction) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                h;

        try {
            Region.REGION4.checkT(T);
            Region.REGION4.checkX(vapourFraction);

            h = Region.REGION4.specificEnthalpyPX(Region.REGION4.saturationPressureT(T), vapourFraction);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, h);
    }

    /**
     * Specific entropy as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEntropyPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s;

        try {
            Region region = Region.getRegionPH(p, h);

            if (region instanceof Region4) {
                s = Region.REGION4.specificEntropyPH(p, h);

            } else {
                double T = region.temperaturePH(p, h);

                s = region.specificEntropyPT(p, T);
            }

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific entropy as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEntropyPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                s;

        try {
            s = Region.getRegionPT(p, T).specificEntropyPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }

        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific entropy as a function of pressure &amp; vapour fraction.
     *
     * @param pressure absolute pressure
     * @param vapourFraction vapour fraction [-]
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEntropyPX(double pressure, double vapourFraction) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s;

        try {
            Region.REGION4.checkP(p);
            Region.REGION4.checkX(vapourFraction);

            s = Region.REGION4.specificEntropyPX(p, vapourFraction);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific entropy as a function of pressure for saturated liquid.
     *
     * @param pressure saturation pressure
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificEntropyPX(double, double)
     */
    public double specificEntropySaturatedLiquidP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s;

        try {
            Region.REGION4.checkP(p);

            s = Region.REGION4.specificEntropySaturatedLiquidP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific entropy as a function of temperature for saturated liquid.
     *
     * @param temperature saturation temperature
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificEntropyTX(double, double)
     */
    public double specificEntropySaturatedLiquidT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                s;

        try {
            Region.REGION4.checkT(T);

            s = Region.REGION4.specificEntropySaturatedLiquidP(Region.REGION4.saturationPressureT(T));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific entropy as a function of pressure for saturated vapour.
     *
     * @param pressure saturation pressure
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificEntropyPX(double, double)
     */
    public double specificEntropySaturatedVapourP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s;

        try {
            Region.REGION4.checkP(p);

            s = Region.REGION4.specificEntropySaturatedVapourP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific entropy as a function of temperature for saturated vapour.
     *
     * @param temperature saturation temperature
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificEntropyTX(double, double)
     */
    public double specificEntropySaturatedVapourT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                s;

        try {
            Region.REGION4.checkT(T);

            s = Region.REGION4.specificEntropySaturatedVapourP(Region.REGION4.saturationPressureT(T));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific entropy as a function of temperature &amp; vapour fraction.
     *
     * @param temperature temperature
     * @param vapourFraction vapour fraction [-]
     * @return specific entropy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificEntropyTX(double temperature, double vapourFraction) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                s;

        try {
            Region.REGION4.checkT(T);
            Region.REGION4.checkX(vapourFraction);

            s = Region.REGION4.specificEntropyPX(Region.REGION4.saturationPressureT(T), vapourFraction);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific Gibbs free energy as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return specific Gibbs free energy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificGibbsFreeEnergyPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                g;

        try {
            g = Region.getRegionPT(p, T).specificGibbsFreeEnergyPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENERGY, g);
    }

    /**
     * Specific internal energy as a function of specific enthalpy &amp;
     * specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificInternalEnergyPT(double, double)
     */
    public double specificInternalEnergyHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                u;

        try {
            Region region = Region.getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);

            u = region.specificInternalEnergyPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENERGY, u);
    }

    /**
     * Specific internal energy as a function of pressure &amp; specific
     * enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificInternalEnergyPT(double, double)
     */
    public double specificInternalEnergyPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                u;

        try {
            Region region = Region.getRegionPH(p, h);

            double T = region.temperaturePH(p, h);

            u = region.specificInternalEnergyPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENERGY, u);
    }

    /**
     * Specific internal energy as a function of pressure &amp; specific
     * entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificInternalEnergyPT(double, double)
     */
    public double specificInternalEnergyPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                u;

        try {
            Region region = Region.getRegionPS(p, s);

            double T = region.temperaturePS(p, s);

            u = region.specificInternalEnergyPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENERGY, u);
    }

    /**
     * Specific internal energy as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificInternalEnergyPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                u;

        try {
            u = Region.getRegionPT(p, T).specificInternalEnergyPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENERGY, u);
    }

    /**
     * Specific internal energy as a function of pressure &amp; vapour fraction.
     *
     * @param pressure absolute pressure
     * @param vapourFraction vapour fraction [-]
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificInternalEnergyPX(double pressure, double vapourFraction) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                v;

        try {
            Region.REGION4.checkP(p);
            Region.REGION4.checkX(vapourFraction);

            v = Region.REGION4.specificInternalEnergyPX(p, vapourFraction);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific internal energy as a function of pressure for saturated liquid.
     *
     * @param pressure saturation pressure
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificInternalEnergyPX(double, double)
     */
    public double specificInternalEnergySaturatedLiquidP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s;

        try {
            Region.REGION4.checkP(p);

            s = Region.REGION4.specificInternalEnergySaturatedLiquidP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific internal energy as a function of temperature for saturated
     * liquid.
     *
     * @param temperature saturation temperature
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificInternalEnergyTX(double, double)
     */
    public double specificInternalEnergySaturatedLiquidT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                s;

        try {
            Region.REGION4.checkT(T);

            s = Region.REGION4.specificInternalEnergySaturatedLiquidP(Region.REGION4.saturationPressureT(T));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific internal energy as a function of pressure for saturated vapour.
     *
     * @param pressure saturation pressure
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificInternalEnergyPX(double, double)
     */
    public double specificInternalEnergySaturatedVapourP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s;

        try {
            Region.REGION4.checkP(p);

            s = Region.REGION4.specificInternalEnergySaturatedVapourP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific internal energy as a function of temperature for saturated
     * vapour.
     *
     * @param temperature saturation temperature
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     * @see #specificInternalEnergyTX(double, double)
     */
    public double specificInternalEnergySaturatedVapourT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                s;

        try {
            Region.REGION4.checkT(T);

            s = Region.REGION4.specificInternalEnergySaturatedVapourP(Region.REGION4.saturationPressureT(T));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, s);
    }

    /**
     * Specific internal energy as a function of temperature &amp; vapour
     * fraction.
     *
     * @param temperature temperature
     * @param vapourFraction vapour fraction [-]
     * @return specific internal energy
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificInternalEnergyTX(double temperature, double vapourFraction) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                v;

        try {
            Region.REGION4.checkT(T);
            Region.REGION4.checkX(vapourFraction);

            v = Region.REGION4.specificInternalEnergyPX(Region.REGION4.saturationPressureT(T), vapourFraction);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of specific enthalpy &amp; specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumeHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                v;

        try {
            v = Region.getRegionHS(h, s).specificVolumeHS(h, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumePH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                v;

        try {
            v = Region.getRegionPH(p, h).specificVolumePH(p, h);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumePS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                v;

        try {
            v = Region.getRegionPS(p, s).specificVolumePS(p, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumePT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                v;

        try {
            v = Region.getRegionPT(p, T).specificVolumePT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of pressure &amp; vapour fraction.
     *
     * @param pressure absolute pressure
     * @param vapourFraction vapour fraction [-]
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumePX(double pressure, double vapourFraction) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                v;

        try {
            Region.REGION4.checkP(p);
            Region.REGION4.checkX(vapourFraction);

            v = Region.REGION4.specificVolumePX(p, vapourFraction);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of pressure for saturated liquid.
     *
     * @param pressure absolute pressure
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumeSaturatedLiquidP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                v;

        try {
            Region.REGION4.checkP(p);

            v = Region.REGION4.specificVolumeSaturatedLiquidP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of temperature for saturated liquid.
     *
     * @param temperature temperature
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumeSaturatedLiquidT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                v;

        try {
            Region.REGION4.checkT(T);

            v = Region.REGION4.specificVolumeSaturatedLiquidP(Region.REGION4.saturationPressureT(T));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of pressure for saturated vapour.
     *
     * @param pressure absolute pressure
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumeSaturatedVapourP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                v;

        try {
            Region.REGION4.checkP(p);

            v = Region.REGION4.specificVolumeSaturatedVapourP(p);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of temperature for saturated vapour.
     *
     * @param temperature temperature
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumeSaturatedVapourT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                v;

        try {
            Region.REGION4.checkT(T);

            v = Region.REGION4.specificVolumeSaturatedVapourP(Region.REGION4.saturationPressureT(T));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Specific volume as a function of temperature &amp; vapour fraction.
     *
     * @param temperature temperature
     * @param vapourFraction vapour fraction [-]
     * @return specific volume
     * @throws OutOfRangeException out-of-range exception
     */
    public double specificVolumeTX(double temperature, double vapourFraction) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                v;

        try {
            Region.REGION4.checkT(T);
            Region.REGION4.checkX(vapourFraction);

            v = Region.REGION4.specificVolumePX(Region.REGION4.saturationPressureT(T), vapourFraction);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPECIFIC_VOLUME, v);
    }

    /**
     * Speed of sound as a function of specific enthalpy &amp; specific entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return speed of sound
     * @throws OutOfRangeException out-of-range exception
     * @see #speedOfSoundPT(double, double)
     */
    public double speedOfSoundHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                w;

        try {
            Region region = Region.getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);

            w = region.speedOfSoundPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPEED_OF_SOUND, w);
    }

    /**
     * Speed of sound as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return speed of sound
     * @throws OutOfRangeException out-of-range exception
     * @see #speedOfSoundPT(double, double)
     */
    public double speedOfSoundPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                w;

        try {
            Region region = Region.getRegionPH(p, h);

            double T = region.temperaturePH(p, h);

            w = region.speedOfSoundPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPEED_OF_SOUND, w);
    }

    /**
     * Speed of sound as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return speed of sound
     * @throws OutOfRangeException out-of-range exception
     * @see #speedOfSoundPT(double, double)
     */
    public double speedOfSoundPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                w;

        try {
            Region region = Region.getRegionPS(p, s);

            double T = region.temperaturePS(p, s);

            w = region.speedOfSoundPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPEED_OF_SOUND, w);
    }

    /**
     * Speed of sound as a function of pressure &amp; temperature.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return speed of sound
     * @throws OutOfRangeException out-of-range exception
     */
    public double speedOfSoundPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                w;

        try {
            w = Region.getRegionPT(p, T).speedOfSoundPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SPEED_OF_SOUND, w);
    }

    /**
     * Surface tension as a function of pressure.
     *
     * @param pressure absolute pressure
     * @return surface tension
     * @throws OutOfRangeException out-of-range exception
     */
    public double surfaceTensionP(double pressure) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                sigma;

        try {
            Region.REGION4.checkP(p);

            sigma = Region.REGION4.surfaceTensionT(Region.REGION4.saturationTemperatureP(p));

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SURFACE_TENSION, sigma);
    }

    /**
     * Surface tension as a function of temperature.
     *
     * @param temperature temperature
     * @return surface tension
     * @throws OutOfRangeException out-of-range exception
     */
    public double surfaceTensionT(double temperature) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                sigma;

        try {
            Region.REGION4.checkT(T);

            sigma = Region.REGION4.surfaceTensionT(T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.SURFACE_TENSION, sigma);
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

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                T;

        try {
            T = Region.getRegionHS(h, s).temperatureHS(h, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);
    }

    /**
     * Temperature.
     *
     * @param pressure absolute pressure [MPa]
     * @param enthalpy specific enthalpy [kJ/(kg)]
     * @return temperature [K]
     * @throws OutOfRangeException out-of-range exception
     */
    public double temperaturePH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                T;

        try {
            T = Region.getRegionPH(p, h).temperaturePH(p, h);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);
    }

    /**
     * Temperature.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return temperature
     * @throws OutOfRangeException out-of-range exception
     */
    public double temperaturePS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                T;

        try {
            T = Region.getRegionPS(p, s).temperaturePS(p, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.TEMPERATURE, T);
    }

    /**
     * Thermal conductivity as a function of specific enthalpy &amp; specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return thermal conductivity
     * @throws OutOfRangeException out-of-range exception
     * @see #thermalConductivityRhoT(double, double)
     */
    public double thermalConductivityHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                lambda;

        try {
            Region region = Region.getRegionHS(h, s);

            double v = region.specificVolumeHS(h, s),
                    T = region.temperatureHS(h, s);

            lambda = Calculate.thermalConductivityRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_CONDUCTIVITY, lambda);
    }

    /**
     * Thermal conductivity as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return thermal conductivity
     * @throws OutOfRangeException out-of-range exception
     * @see #thermalConductivityRhoT(double, double)
     */
    public double thermalConductivityPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                lambda;

        try {
            Region region = Region.getRegionPH(p, h);

            double v = region.specificVolumePH(p, h),
                    T = region.temperaturePH(p, h);

            lambda = Calculate.thermalConductivityRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_CONDUCTIVITY, lambda);
    }

    /**
     * Thermal conductivity as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return thermal conductivity
     * @throws OutOfRangeException out-of-range exception
     * @see #thermalConductivityRhoT(double, double)
     */
    public double thermalConductivityPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                lambda;

        try {
            Region region = Region.getRegionPS(p, s);

            double v = region.specificVolumePS(p, s),
                    T = region.temperaturePS(p, s);

            lambda = Calculate.thermalConductivityRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_CONDUCTIVITY, lambda);
    }

    /**
     * Thermal conductivity as a function of pressure &amp; temperature.
     *
     * Note that is method is not accurate in the two-phase region.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return thermal conductivity
     * @throws OutOfRangeException out-of-range exception
     * @see #thermalConductivityRhoT(double, double)
     */
    public double thermalConductivityPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                lambda;

        try {
            double v = Region.getRegionPT(p, T).specificVolumePT(p, T);

            lambda = Calculate.thermalConductivityRhoT(1 / v, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_CONDUCTIVITY, lambda);
    }

    /**
     * Thermal conductivity as a function of density &amp; temperature.
     *
     * @param density density
     * @param temperature temperature
     * @return thermal conductivity
     * @throws OutOfRangeException out-of-range exception
     */
    public double thermalConductivityRhoT(double density, double temperature) throws OutOfRangeException {

        double rho = convertToDefault(UNIT_SYSTEM.DENSITY, density),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                lambda;

        try {
            lambda = Calculate.thermalConductivityRhoT(rho, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_CONDUCTIVITY, lambda);
    }

    /**
     * Thermal diffusivity as a function of specific enthalpy &amp; specific
     * entropy.
     *
     * @param enthalpy specific enthalpy
     * @param entropy specific entropy
     * @return thermal diffusivity
     * @throws OutOfRangeException out-of-range exception
     */
    public double thermalDiffusivityHS(double enthalpy, double entropy) throws OutOfRangeException {

        double h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                lambda,
                rho,
                cp;

        try {
            Region region = Region.getRegionHS(h, s);

            double p = region.pressureHS(h, s),
                    T = region.temperatureHS(h, s);
            rho = 1 / region.specificVolumeHS(h, s);
            lambda = Calculate.thermalConductivityRhoT(rho, T);
            cp = region.specificIsobaricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_DIFFUSIVITY, lambda / (rho * cp));
    }

    /**
     * Thermal diffusivity as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return thermal diffusivity
     * @throws OutOfRangeException out-of-range exception
     */
    public double thermalDiffusivityPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy),
                lambda,
                rho,
                cp;

        try {
            Region region = Region.getRegionPH(p, h);

            double T = region.temperaturePH(p, h);
            rho = 1 / region.specificVolumePH(p, h);
            lambda = Calculate.thermalConductivityRhoT(rho, T);
            cp = region.specificIsobaricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_DIFFUSIVITY, lambda / (rho * cp));
    }

    /**
     * Thermal diffusivity as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return thermal diffusivity
     * @throws OutOfRangeException out-of-range exception
     */
    public double thermalDiffusivityPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy),
                lambda,
                rho,
                cp;

        try {
            Region region = Region.getRegionPS(p, s);

            double T = region.temperaturePS(p, s);
            rho = 1 / region.specificVolumePS(p, s);
            lambda = Calculate.thermalConductivityRhoT(rho, T);
            cp = region.specificIsobaricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_DIFFUSIVITY, lambda / (rho * cp));
    }

    /**
     * Thermal diffusivity as a function of pressure &amp; temperature.
     *
     * Note that is method is not accurate in the two-phase region.
     *
     * @param pressure absolute pressure
     * @param temperature temperature
     * @return thermal diffusivity
     * @throws OutOfRangeException out-of-range exception
     */
    public double thermalDiffusivityPT(double pressure, double temperature) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                lambda,
                rho,
                cp;

        try {
            Region region = Region.getRegionPT(p, T);

            rho = 1 / region.specificVolumePT(p, T);
            lambda = Calculate.thermalConductivityRhoT(rho, T);
            cp = region.specificIsobaricHeatCapacityPT(p, T);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
        return convertFromDefault(UNIT_SYSTEM.THERMAL_DIFFUSIVITY, lambda / (rho * cp));
    }

    /**
     * Vapour fraction as a function of specific enthalpy &amp; specific
     * entropy.
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
            return Region.getRegionHS(h, s).vapourFractionHS(h, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Vapour fraction as a function of pressure &amp; specific enthalpy.
     *
     * @param pressure absolute pressure
     * @param enthalpy specific enthalpy
     * @return vapour fraction [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #vapourFractionHS(double, double)
     */
    public double vapourFractionPH(double pressure, double enthalpy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                h = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTHALPY, enthalpy);

        try {
            return Region.getRegionPH(p, h).vapourFractionPH(p, h);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Vapour fraction as a function of pressure &amp; specific entropy.
     *
     * @param pressure absolute pressure
     * @param entropy specific entropy
     * @return vapour fraction [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #vapourFractionHS(double, double)
     */
    public double vapourFractionPS(double pressure, double entropy) throws OutOfRangeException {

        double p = convertToDefault(UNIT_SYSTEM.PRESSURE, pressure),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            return Region.getRegionPS(p, s).vapourFractionPS(p, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
    }

    /**
     * Vapour fraction as a function of temperature &amp; specific entropy.
     *
     * This method only returns values in the two-phase region.
     *
     * @param temperature temperature
     * @param entropy specific entropy
     * @return vapour fraction [-]
     * @throws OutOfRangeException out-of-range exception
     * @see #vapourFractionHS(double, double)
     */
    public double vapourFractionTS(double temperature, double entropy) throws OutOfRangeException {

        double T = convertToDefault(UNIT_SYSTEM.TEMPERATURE, temperature),
                s = convertToDefault(UNIT_SYSTEM.SPECIFIC_ENTROPY, entropy);

        try {
            Region.REGION4.checkT(T);
            //TODO Region.REGION4.checkS(s);

            return Region.REGION4.vapourFractionTS(T, s);

        } catch (OutOfRangeException e) {
            throw e.convertFromDefault(UNIT_SYSTEM);
        }
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

            Region region = Region.getRegionPT(p, T);

            double rho = 1 / region.specificVolumePT(p, T),
                    eta = dynamicViscosityRhoT(rho, T),
                    cp = region.specificIsobaricHeatCapacityPT(p, T),
                    lambda = thermalConductivityRhoT(rho, T) / 1e3;

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

            if (T < 238.15) {
                throw new OutOfRangeException(Quantity.T, T, 238.15);

            } else if (T > 873.15) {
                throw new OutOfRangeException(Quantity.T, T, 873.15);
            }

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
         */
        static double dynamicViscosityRhoT(double rho, double T) {

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
         * @param region region
         * @param p pressure [MPa]
         * @param T temperature [K]
         * @param x any quantity
         * @param y any quantity
         * @param z any quantity
         * @return partial derivative
         * @throws OutOfRangeException out-of-range exception
         */
        private static double partialDerivativePT(Region region, double p, double T, Quantity x, Quantity y, Quantity z) throws OutOfRangeException {

            double v = region.specificVolumePT(p, T),
                    s = region.specificEntropyPT(p, T),
                    cp = region.specificIsobaricHeatCapacityPT(p, T),
                    alphaV = region.isobaricCubicExpansionCoefficientPT(p, T),
                    kappaT = region.isothermalCompressibilityPT(p, T);

            double[] dx = partialDerivativesPT(p, T, x, v, s, cp, alphaV, kappaT),
                    dy = partialDerivativesPT(p, T, y, v, s, cp, alphaV, kappaT),
                    dz = partialDerivativesPT(p, T, z, v, s, cp, alphaV, kappaT);

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
         */
        private static double partialDerivativeRhoT(double rho, double T, Quantity x, Quantity y, Quantity z) {

            double v = 1 / rho,
                    p = Region.REGION3.pressureRhoT(rho, T),
                    s = Region.REGION3.specificEntropyRhoT(rho, T),
                    cv = Region.REGION3.specificIsochoricHeatCapacityRhoT(rho, T),
                    alphap = Region.REGION3.relativePressureCoefficientRhoT(rho, T),
                    betap = Region.REGION3.isothermalStressCoefficientRhoT(rho, T);

            double[] dx = partialDerivativesVT(v, T, x, p, s, cv, alphap, betap),
                    dy = partialDerivativesVT(v, T, y, p, s, cv, alphap, betap),
                    dz = partialDerivativesVT(v, T, z, p, s, cv, alphap, betap);

            double dx_dv = dx[0],
                    dy_dv = dy[0],
                    dz_dv = dz[0],
                    dx_dT = dx[1],
                    dy_dT = dy[1],
                    dz_dT = dz[1];

            return (dz_dv * dy_dT - dz_dT * dy_dv) / (dx_dv * dy_dT - dx_dT * dy_dv);
        }

        /**
         * Partial derivatives of the given quantity with respect to specific
         * volume and temperature for region 3 described by specific Helmholtz
         * free energy.
         *
         * @param v specific volume [m&sup3;/kg]
         * @param T temperature [K]
         * @param quantity quantity
         * @param p pressure [MPa]
         * @param s specific entropy [kJ/kg-K]
         * @param cv
         * @param alphap
         * @param betap
         * @return partial derivatives d/d&nu; and d/dT
         */
        private static double[] partialDerivativesVT(double v, double T, Quantity quantity, double p, double s, double cv, double alphap, double betap) {

            double d_dv = Double.NaN,
                    d_dT = Double.NaN;

            switch (quantity) {
                case p:
                    d_dv = -p * betap;
                    d_dT = p * alphap;
                    break;

                case T:
                    d_dv = 0;
                    d_dT = 1;
                    break;

                case v:
                    d_dv = 1;
                    d_dT = 0;
                    break;

                case u:
                    d_dv = p * (T * alphap - 1);
                    d_dT = cv;
                    break;

                case h:
                    d_dv = p * (T * alphap - v * betap);
                    d_dT = cv + p * v * alphap;
                    break;

                case s:
                    d_dv = p * alphap;
                    d_dT = cv / T;
                    break;

                case g:
                    d_dv = -p * v * betap;
                    d_dT = p * v * alphap - s;
                    break;

                case f:
                    d_dv = -p;
                    d_dT = -s;
                    break;

                case rho:
                    d_dv = -1 / (v * v);
                    d_dT = 0;
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported quantity for partial derivative: " + quantity);
            }
            return new double[]{d_dv, d_dT};
        }

        /**
         * Partial derivatives of the given quantity with respect to pressure
         * and temperature for regions described by specific Gibbs free energy.
         *
         * @param p pressure [MPa]
         * @param T temperature [K]
         * @param quantity quantity
         * @param v specific volume [m&sup3;/kg]
         * @param s specific entropy [kJ/kg-K]
         * @param cp specific isobaric heat capacity
         * @param alphaV isobaric cubic expansion coefficient
         * @param kappaT isothermal compressibility
         * @return partial derivatives d/dT and d/dp
         */
        private static double[] partialDerivativesPT(double p, double T, Quantity quantity, double v, double s, double cp, double alphaV, double kappaT) {

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

                case v:
                    d_dT = v * alphaV;
                    d_dp = -v * kappaT;
                    break;

                case u:
                    d_dT = cp - p * v * alphaV;
                    d_dp = v * (p * kappaT - T * alphaV);
                    break;

                case h:
                    d_dT = cp;
                    d_dp = v * (1 - T * alphaV);
                    break;

                case s:
                    d_dT = cp / T;
                    d_dp = -v * alphaV;
                    break;

                case g:
                    d_dT = -s;
                    d_dp = v;
                    break;

                case f:
                    d_dT = -p * v * alphaV - s;
                    d_dp = p * v * kappaT;
                    break;

                case rho:
                    d_dT = -alphaV / v;
                    d_dp = kappaT / v;
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported quantity for partial derivative: " + quantity);
            }

            return new double[]{d_dT, d_dp};
        }

        /**
         * Refractive index.
         *
         * @param rho density [kg/m&sup3;]
         * @param T temperature [K]
         * @param lambdaL wavelength [&mu;m]
         * @return refractive index [-]
         * @throws OutOfRangeException out-of-range exception
         */
        static double refractiveIndexRhoTLambda(double rho, double T, double lambdaL) throws OutOfRangeException {

            if (T < 261.15) {
                throw new OutOfRangeException(Quantity.T, T, 261.15);

            } else if (T > 773.15) {
                throw new OutOfRangeException(Quantity.T, T, 773.15);

            } else if (rho <= 0) {
                throw new OutOfRangeException(Quantity.rho, rho, 0);

            } else if (rho > 1060) {
                throw new OutOfRangeException(Quantity.rho, rho, 1060);

            } else if (lambdaL < 0.2) {
                throw new OutOfRangeException(Quantity.lambdaL, lambdaL, 0.2);

            } else if (lambdaL > 1.1) {
                throw new OutOfRangeException(Quantity.lambdaL, lambdaL, 1.1);
            }

            double[] a = {0.244257733, 0.974634476e-2, -.373234996e-2, 0.268678472e-3, 0.158920570e-2, 0.245934259e-2, 0.900704920, -.166626219e-1};

            double delta = rho / 1e3,
                    theta = T / IF97.T0,
                    Lambda = lambdaL / 0.589,
                    Lambda2 = Lambda * Lambda,
                    LambdaIR = 5.432937,
                    LambdaUV = -0.229202,
                    A = delta * (a[0] + a[1] * delta + a[2] * theta + a[3] * Lambda2 * theta + a[4] / Lambda2 + a[5] / (Lambda2 - LambdaUV * LambdaUV) + a[6] / (Lambda2 - LambdaIR * LambdaIR) + a[7] * delta * delta);

            return sqrt((2 * A + 1) / (1 - A));
        }

        static double thermalConductivityRhoT(double rho, double T) {

            /*
             Coefficients
             */
            double[] n0 = {0.102811e-1, 0.299621e-1, 0.156146e-1, -.422464e-2},
                    n1 = {-.397070, 0.400302, 0.106000e1, -.171587, 0.239219e1},
                    n2 = {0.701309e-1, 0.118520e-1, 0.642857, 0.169937e-2, -.102000e1, -.411717e1, -.617937e1, 0.822994e-1, 0.100932e2, 0.308976e-2};

            double theta = T / 647.26,
                    DeltaTheta = abs(theta - 1) + n2[9],
                    delta = rho / 317.7,
                    Lambda0 = 0,
                    A, B = 2 + n2[7] * pow(DeltaTheta, -0.6);

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
     * Quantities as defined by reference given above.
     */
    public enum Quantity {

        /**
         * Absolute pressure.
         */
        p("absolute pressure", "p"),
        /**
         * Temperature.
         */
        T("temperature", "T"),
        /**
         * Specific volume.
         */
        v("specific volume", "v"),
        /**
         * Specific internal energy.
         */
        u("specific internal energy", "u"),
        /**
         * Specific enthalpy.
         */
        h("specific enthalpy", "h"),
        /**
         * Specific entropy.
         */
        s("specific entropy", "s"),
        /**
         * Specific Gibbs free energy.
         */
        g("specific Gibbs free energy", "g"),
        /**
         * Specific Helmholtz free energy.
         */
        f("specific Helmholtz free energy", "f"),
        /**
         * Density.
         */
        rho("density", "\u03c1"),
        /**
         * Thermal diffusivity.
         */
        a("thermal diffusivity", "a"),
        /**
         * Specific isobaric heat capacity.
         */
        cp("specific isobaric heat capacity", "<html>c<sub>p</sub></html>"),
        /**
         * Specific isochoric heat capacity.
         */
        cv("specific isochoric heat capacity", "<html>c<sub>v</sub></html>"),
        /**
         * Refractive index.
         */
        n("refractive index", "n"),
        /**
         * Speed of sound.
         */
        w("speed of sound", "w"),
        /**
         * Vapour fraction.
         */
        x("vapour fraction", "x"),
        /**
         * Compression factor.
         */
        z("compression factor", "z"),
        /**
         * Isobaric cubic expansion coefficient.
         */
        alphav("isobaric cubic expansion coefficient", "<html>&alpha;<sub>v</sub></html>"),
        /**
         * Dielectric constant.
         */
        epsilon("dielectric constant", "\u03b5"),
        /**
         * Dynamic viscosity.
         */
        eta("dynamic viscosity", "\u03bc"),
        /**
         * Isentropic exponent.
         */
        kappa("isentropic exponent", "\u03ba"),
        /**
         * Isothermal compressibility.
         */
        kappaT("isothermal compressibility", "<html>&kappa;<sub>T</sub></html>"),
        /**
         * Thermal conductivity.
         */
        lambda("thermal conductivity", "\u03bb"),
        /**
         * Wavelength of light.
         */
        lambdaL("wavelength", "<html>&lambda;<sub>L</sub></html>"),
        /**
         * Kinematic viscosity.
         */
        nu("kinematic viscosity", "\u03bd"),
        /**
         * Surface tension.
         */
        sigma("surface tension", "\u03c3"),
        /**
         * Prandtl number.
         */
        Pr("Prandtl number", "Pr");

        private final String NAME, SYMBOL;

        Quantity(String name, String symbol) {

            NAME = name;
            SYMBOL = symbol;
        }

        /**
         * Gets the quantities supported by partial derivatives relations.
         *
         * @return set of quantities
         * @see #partialDerivativePT(double, double,
         * com.hummeling.if97.IF97.Quantity, com.hummeling.if97.IF97.Quantity,
         * com.hummeling.if97.IF97.Quantity)
         * @see #partialDerivativeRhoT(double, double,
         * com.hummeling.if97.IF97.Quantity, com.hummeling.if97.IF97.Quantity,
         * com.hummeling.if97.IF97.Quantity)
         */
        public static Set<Quantity> getPartialDerivatives() {
            return EnumSet.range(p, rho);
        }

        /**
         * Gets the symbol for this quantity, according cited reference above.
         *
         * @return Unicode symbol
         */
        public String getSymbol() {
            return SYMBOL;
        }

        @Override
        public String toString() {
            return NAME;
        }
    }

    /**
     * <table border="1">
     * <caption>Unit systems.</caption>
     * <tr>
     * <th></th><th></th><th>Default</th><th>Engineering</th><th>SI</th><th>Imperial</th>
     * </tr>
     * <tr>
     * <td><i>p</i></td><td>absolute
     * pressure</td><td>MPa</td><td>bar</td><td>Pa</td><td>psi</td>
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
     * <td><i>a</i></td><td>thermal
     * diffusivity</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>cSt</td>
     * </tr>
     * <tr>
     * <td><i>x</i></td><td>vapour
     * fraction</td><td>-</td><td>-</td><td>-</td><td>-</td>
     * </tr>
     * <tr>
     * <td><i>&lambda;<sub>L</sub></i></td><td>wavelength of
     * light</td><td>&mu;m</td><td>&mu;m</td><td>m</td><td>in</td>
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
                new double[]{1, 0}, new double[]{1, 0}),
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
                new double[]{1, 0}, // specific entropy
                new double[]{1, 0}, // specific volume
                new double[]{1, 0}, // speed of sound
                new double[]{1, 0}, // surface tension
                new double[]{1, IF97.T0}, // temperature
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
                new double[]{1e-3, 0}, // specific energy
                new double[]{1e-3, 0}, // specific entropy
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
                new double[]{BTU / (lb * Ra), 0}, // specific entropy
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
                THERMAL_DIFFUSIVITY,
                WAVELENGTH;
        final Map<Quantity, String> UNITS;

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
         * @param specificEntropy
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
                double[] specificEntropy,
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
            SPECIFIC_ENTHALPY = specificEnergy.clone(); // same unit
            SPECIFIC_ENTROPY = specificEntropy;
            SPECIFIC_HEAT_CAPACITY = specificEntropy.clone(); // same unit
            SPECIFIC_VOLUME = specificVolume;
            SPEED_OF_SOUND = speedOfSound;
            SURFACE_TENSION = surfaceTension;
            TEMPERATURE = temperature;
            THERMAL_CONDUCTIVITY = thermalConductivity;
            THERMAL_DIFFUSIVITY = kinematicViscosity.clone(); // same unit
            WAVELENGTH = wavelength;

            UNITS = new EnumMap<Quantity, String>(Quantity.class);
            double tol = 1e-9;

            if (abs(pressure[0] - 1) < tol) {
                /*
                 Default
                 */
                UNITS.put(Quantity.p, "MPa");
                UNITS.put(Quantity.T, "K");
                UNITS.put(Quantity.v, "m³/kg");
                UNITS.put(Quantity.cp, "kJ/(kg·K)");
                UNITS.put(Quantity.cv, "kJ/(kg·K)");
                UNITS.put(Quantity.rho, "kg/m³");
                UNITS.put(Quantity.eta, "Pa·s");
                UNITS.put(Quantity.nu, "m²/s");
                UNITS.put(Quantity.a, "m²/s");
                UNITS.put(Quantity.u, "kJ/kg");
                UNITS.put(Quantity.h, "kJ/kg");
                UNITS.put(Quantity.s, "kJ/(kg·K)");
                UNITS.put(Quantity.lambda, "W/(m·K)");
                UNITS.put(Quantity.lambdaL, "μm");
                UNITS.put(Quantity.g, "kJ/kg");
                UNITS.put(Quantity.f, "kJ/kg");
                UNITS.put(Quantity.x, "");
                UNITS.put(Quantity.alphav, "1/K");
                UNITS.put(Quantity.kappa, "m²/s");
                UNITS.put(Quantity.kappaT, "1/MPa");
                UNITS.put(Quantity.w, "m/s");
                UNITS.put(Quantity.sigma, "N/m");

            } else if (abs(pressure[0] - 0.1) < tol) {
                /*
                 Engineering
                 */
                UNITS.put(Quantity.p, "bar");
                UNITS.put(Quantity.T, "°C");
                UNITS.put(Quantity.v, "m³/kg");
                UNITS.put(Quantity.cp, "kJ/(kg·K)");
                UNITS.put(Quantity.cv, "kJ/(kg·K)");
                UNITS.put(Quantity.rho, "kg/m³");
                UNITS.put(Quantity.eta, "Pa·s");
                UNITS.put(Quantity.nu, "m²/s");
                UNITS.put(Quantity.a, "m²/s");
                UNITS.put(Quantity.u, "kJ/kg");
                UNITS.put(Quantity.h, "kJ/kg");
                UNITS.put(Quantity.s, "kJ/(kg·K)");
                UNITS.put(Quantity.lambda, "kW/(m·K)");
                UNITS.put(Quantity.lambdaL, "μm");
                UNITS.put(Quantity.g, "kJ/kg");
                UNITS.put(Quantity.f, "kJ/kg");
                UNITS.put(Quantity.x, "");
                UNITS.put(Quantity.alphav, "1/K");
                UNITS.put(Quantity.kappa, "m²/s");
                UNITS.put(Quantity.kappaT, "1/MPa");
                UNITS.put(Quantity.w, "m/s");
                UNITS.put(Quantity.sigma, "N/m");

            } else if (abs(pressure[0] - 1e-6) < tol) {
                /*
                 SI
                 */
                UNITS.put(Quantity.p, "Pa");
                UNITS.put(Quantity.T, "K");
                UNITS.put(Quantity.v, "m³/kg");
                UNITS.put(Quantity.cp, "J/(kg·K)");
                UNITS.put(Quantity.cv, "J/(kg·K)");
                UNITS.put(Quantity.rho, "kg/m³");
                UNITS.put(Quantity.eta, "Pa·s");
                UNITS.put(Quantity.nu, "m²/s");
                UNITS.put(Quantity.a, "m²/s");
                UNITS.put(Quantity.u, "J/kg");
                UNITS.put(Quantity.h, "J/kg");
                UNITS.put(Quantity.s, "J/(kg·K)");
                UNITS.put(Quantity.lambda, "W/(m·K)");
                UNITS.put(Quantity.lambdaL, "m");
                UNITS.put(Quantity.g, "J/kg");
                UNITS.put(Quantity.f, "J/kg");
                UNITS.put(Quantity.x, "");
                UNITS.put(Quantity.alphav, "1/K");
                UNITS.put(Quantity.kappa, "m²/s");
                UNITS.put(Quantity.kappaT, "1/Pa");
                UNITS.put(Quantity.w, "m/s");
                UNITS.put(Quantity.sigma, "N/m");

            } else if (abs(pressure[0] - psi) < tol) {
                /*
                 Imperial
                 */
                UNITS.put(Quantity.p, "psi");
                UNITS.put(Quantity.T, "°F");
                UNITS.put(Quantity.v, "ft³/lb");
                UNITS.put(Quantity.cp, "BTU/(lb·R)");
                UNITS.put(Quantity.cv, "BTU/(lb·R)");
                UNITS.put(Quantity.rho, "lb/ft³");
                UNITS.put(Quantity.eta, "cP");
                UNITS.put(Quantity.nu, "cSt");
                UNITS.put(Quantity.a, "cSt");
                UNITS.put(Quantity.u, "BTU/lb");
                UNITS.put(Quantity.h, "BTU/lb");
                UNITS.put(Quantity.s, "BTU/(lb·R)");
                UNITS.put(Quantity.lambda, "BTU/(hr·ft·R)");
                UNITS.put(Quantity.lambdaL, "in");
                UNITS.put(Quantity.g, "BTU/lb");
                UNITS.put(Quantity.f, "BTU/lb");
                UNITS.put(Quantity.x, "");
                UNITS.put(Quantity.alphav, "1/R");
                UNITS.put(Quantity.kappa, "cSt");
                UNITS.put(Quantity.kappaT, "in²/lb");
                UNITS.put(Quantity.w, "ft/s");
                UNITS.put(Quantity.sigma, "lbf/ft");

            } else {
                throw new IllegalStateException("Unsupported unit system");
            }
        }

        public String getLabel(Quantity quantity) {

            if (!UNITS.containsKey(quantity)) {
                throw new IllegalArgumentException("Unknown label for quantity: " + quantity);
            }
            return String.format("%s [%s]", quantity, getUnit(quantity));
        }

        public String getUnit(Quantity quantity) {

//            if (!UNITS.containsKey(quantity)) {
//                throw new IllegalArgumentException("Unknown unit for quantity: " + quantity);
//            }
//            return UNITS.get(quantity);
            return UNITS.containsKey(quantity) ? UNITS.get(quantity) : "-";
        }

        @Override
        public String toString() {

            switch (this) {
                case SI:
                    return name();

                default:
                    return name().toLowerCase();
            }
        }
    }
}
