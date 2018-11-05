/*
 * If97Suite.java
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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Ralph Hummeling
 * (<a href="https://www.hummeling.com">www.hummeling.com</a>)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    com.hummeling.if97.IF97Test.class,
    com.hummeling.if97.Region1Test.class,
    com.hummeling.if97.Region2MetaTest.class,
    com.hummeling.if97.Region2Test.class,
    com.hummeling.if97.Region3Test.class,
    com.hummeling.if97.Region4Test.class,
    com.hummeling.if97.Region5Test.class,
    com.hummeling.if97.RegionTest.class
})
public class If97Suite {
}
