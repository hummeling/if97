/*
 * Region2MetaTest.java
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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Region 2 metastable-vapour tests.
 *
 * @author Ralph Hummeling
 * (<a href="https://www.hummeling.com">www.hummeling.com</a>)
 */
public class Region2MetaTest {

    static Region2Meta region = new Region2Meta();

    @Test
    public void testIsobaricCubicExpansionCoefficientPT() {

        double[][] X = {
            {0.318819824e-2, 1, 450},
            {0.348506136e-2, 1, 440},
            {0.418276571e-2, 1.5, 450}};

        for (double[] x : X) {
            assertEquals(x[0], region.isobaricCubicExpansionCoefficientPT(x[1], x[2]), 1e-11);
        }
    }

    @Test
    public void testIsothermalCompressibilityPT() {

        double[][] X = {
            {0.109364239e1, 1, 450},
            {0.111133230e1, 1, 440},
            {0.787967952, 1.5, 450}};

        for (double[] x : X) {
            assertEquals(x[0], region.isothermalCompressibilityPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificEnthalpyPT() throws OutOfRangeException {

        double[][] X = {
            {0.276881115e4, 1, 450},
            {0.274015123e4, 1, 440},
            {0.272134539e4, 1.5, 450}};

        for (double[] x : X) {
            //System.out.println("region: " + Region.getRegionPT(x[1], x[2]).getName());
            assertEquals(x[0], region.specificEnthalpyPT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSpecificEntropyPT() {

        double[][] X = {
            {0.656660377e1, 1, 450},
            {0.650218759e1, 1, 440},
            {0.629170440e1, 1.5, 450}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificEntropyPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificInternalEnergyPT() {

        double[][] X = {
            {0.257629461e4, 1, 450},
            {0.255393894e4, 1, 440},
            {0.253881758e4, 1.5, 450}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificInternalEnergyPT(x[1], x[2]), 1e-5);
        }
    }

    @Test
    public void testSpecificIsobaricHeatCapacityPT() {

        double[][] X = {
            {0.276349265e1, 1, 450},
            {0.298166443e1, 1, 440},
            {0.362795578e1, 1.5, 450}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificIsobaricHeatCapacityPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificIsochoricHeatCapacityPT() {

        double[][] X = {
            {0.195830730e1, 1, 450},
            {0.208622142e1, 1, 440},
            {0.241213708e1, 1.5, 450}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificIsochoricHeatCapacityPT(x[1], x[2]), 1e-8);
        }
    }

    @Test
    public void testSpecificVolumePT() {

        double[][] X = {
            {0.192516540, 1, 450},
            {0.186212297, 1, 440},
            {0.121685206, 1.5, 450}};

        for (double[] x : X) {
            assertEquals(x[0], region.specificVolumePT(x[1], x[2]), 1e-9);
        }
    }

    @Test
    public void testSpeedOfSoundPT() {

        double[][] X = {
            {0.498408101e3, 1, 450},
            {0.489363295e3, 1, 440},
            {0.481941819e3, 1.5, 450}};

        for (double[] x : X) {
            assertEquals(x[0], region.speedOfSoundPT(x[1], x[2]), 1e-6);
        }
    }
}
