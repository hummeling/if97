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
 * Copyright 2009-2014 Hummeling Engineering BV (www.hummeling.com)
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

    private final IF97.Quantity QUANTITY;
    private final double VALUE, LIMIT;

    OutOfRangeException(IF97.Quantity quantity, double value, double limit) {

        QUANTITY = quantity;
        VALUE = value;
        LIMIT = limit;
    }

    OutOfRangeException convertFromDefault(IF97.UnitSystem unitSystem) {

        double value = IF97.convertFromDefault(unitSystem, QUANTITY, VALUE),
                limit = IF97.convertFromDefault(unitSystem, QUANTITY, LIMIT);

        return new OutOfRangeException(QUANTITY, value, limit);
    }

    /**
     * Get exceeded limit.
     *
     * @return limit value
     */
    public double getLimit() {
        return LIMIT;
    }

    @Override
    public String getMessage() {
        return String.format("%s value %g should be %s than %g.",
                QUANTITY, VALUE, VALUE > LIMIT ? "lower" : "higher", LIMIT);
    }

    /**
     * Get exceeded quantity.
     *
     * @return quantity
     */
    public String getQuantity() {
        return QUANTITY.toString();
    }

    /**
     * Get exceeding value.
     *
     * @return value
     */
    public double getValue() {
        return VALUE;
    }
}
