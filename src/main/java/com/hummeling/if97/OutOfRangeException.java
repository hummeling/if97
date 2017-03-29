/*
 * OutOfRangeException.java
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

/**
 * Out of range exception.
 *
 * The global IAPWS-IF97 limits are <ul> <li> 273.15 K &lt;= T &lt;= 1073.15 K
 * &nbsp; 0 &lt; p &lt;= 100 MPa</li> <li>1073.15 K &lt; T &lt;= 2273.15 K
 * &nbsp; 0 &lt; p &lt;= 50 MPa</li> </ul>
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
public class OutOfRangeException extends IllegalArgumentException {

    private final IF97.Quantity[] QUANTITIES;
    private final double[] VALUES, LIMITS;
    private final IF97.UnitSystem UNIT_SYSTEM;

    OutOfRangeException(IF97.Quantity quantity, double value, double limit) {

        this(new IF97.Quantity[]{quantity}, new double[]{value}, new double[]{limit});
    }

    OutOfRangeException(IF97.Quantity[] quantities, double[] values, double[] limits) {

        this(quantities, values, limits, IF97.UnitSystem.DEFAULT);
    }

    OutOfRangeException(IF97.Quantity[] quantities, double[] values, double[] limits, IF97.UnitSystem unitSystem) {

        if (quantities == null || values == null || limits == null) {
            throw new IllegalArgumentException("Arguments shouldn't be null.");

        } else if (quantities.length == 0 || values.length == 0 || limits.length == 0) {
            throw new IllegalArgumentException("Argument arrays shouldn't be empty.");

        } else if (quantities.length != values.length || values.length != limits.length) {
            throw new IllegalArgumentException("Argument arrays should have equal lengths.");
        }

        QUANTITIES = quantities.clone();
        VALUES = values.clone();
        LIMITS = limits.clone();
        UNIT_SYSTEM = unitSystem;
    }

    OutOfRangeException convertFromDefault(IF97.UnitSystem unitSystem) {

        double[] values = new double[QUANTITIES.length],
                limits = new double[QUANTITIES.length];

        for (int i = 0; i < QUANTITIES.length; i++) {
            values[i] = IF97.convertFromDefault(unitSystem, QUANTITIES[i], VALUES[i]);
            limits[i] = IF97.convertFromDefault(unitSystem, QUANTITIES[i], LIMITS[i]);
        }

        return new OutOfRangeException(QUANTITIES, values, limits, unitSystem);
    }

    /**
     * Get exceeded limit.
     *
     * @return limit value
     */
    public double getLimit() {

        return LIMITS[0];
    }

    @Override
    public String getMessage() {

        String out = "";

        for (int i = 0; i < QUANTITIES.length; i++) {
            String quantity = QUANTITIES[i].toString(),
                    unit = UNIT_SYSTEM.getUnit(QUANTITIES[i]);

            switch (i) {
                case 0:
                    quantity = quantity.substring(0, 1).toUpperCase() + quantity.substring(1);

                    out += String.format("%s value %g %s should be %s than %g %s",
                            quantity, VALUES[i], unit, VALUES[i] > LIMITS[i] ? "lower" : "higher", LIMITS[i], unit);
                    break;

                default:
                    out += String.format(", when %s value %g %s is %s than %g %s",
                            quantity, VALUES[i], unit, VALUES[i] > LIMITS[i] ? "higher" : "lower", LIMITS[i], unit);
            }
        }
        out += ".";

        return out;
    }

    /**
     * Get exceeded quantity.
     *
     * @return quantity
     */
    public String getQuantity() {

        return QUANTITIES[0].toString();
    }

    /**
     * Get exceeding value.
     *
     * @return value
     */
    public double getValue() {

        return VALUES[0];
    }
}
