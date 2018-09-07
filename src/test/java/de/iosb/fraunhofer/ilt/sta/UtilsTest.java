/*
 * Copyright (C) 2016 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
 * Karlsruhe, Germany.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.iosb.fraunhofer.ilt.sta;

import de.fraunhofer.iosb.ilt.sta.Utils;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jab
 */
public class UtilsTest {

    public UtilsTest() {
    }

    @Test
    public void testEscapeForStringConstant() {
        Assert.assertEquals("abcdefg", Utils.escapeForStringConstant("abcdefg"));
        Assert.assertEquals("''", Utils.escapeForStringConstant("'"));
        Assert.assertEquals("''''", Utils.escapeForStringConstant("''"));
    }

    @Test
    public void testUrlEncode() {
        Assert.assertEquals("http%3A//example.org/Things%5Bxyz%27xyz%5D", Utils.urlEncode("http://example.org/Things[xyz'xyz]", true));
        Assert.assertEquals("http%3A%2F%2Fexample.org%2FThings%5Bxyz%27xyz%5D", Utils.urlEncode("http://example.org/Things[xyz'xyz]", false));
    }

}
