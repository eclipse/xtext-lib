/**
 * Copyright (c) 2019, 2020 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.xtext.xbase.tests.lib;

import java.util.Objects;

import org.eclipse.xtext.xbase.lib.ArrayExtensions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mathias Rieder - Initial contribution and API
 */
public class ArrayExtensionsCharTest {
	private char[] data;

	private final char a = 'a';

	private final char b = 'b';

	private final char c = 'c';

	private final char x = 'x';

	@Before
	public void init() {
		data = createData();
	}

	private char[] createData() {
		return new char[] {a, b, c};
	}

	@Test
	public void testSetGet__char() {
		Assert.assertEquals(a, data[0]);
		Assert.assertEquals(b, data[1]);
		Assert.assertEquals(c, data[2]);
	}

	@Test
	public void testClone__char() {
		char[] clonedData = data.clone();
		Assert.assertEquals(a, clonedData[0]);
		Assert.assertEquals(b, clonedData[1]);
		Assert.assertEquals(c, clonedData[2]);
	}

	@Test
	public void testLength__char() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__char() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals__char() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		char[] newData = createData();
		newData[1] = a;
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__char() {
		Assert.assertTrue(ArrayExtensions.contains(data, a));
		Assert.assertTrue(ArrayExtensions.contains(data, b));
		Assert.assertTrue(ArrayExtensions.contains(data, c));
		Assert.assertFalse(ArrayExtensions.contains(data, x));
	}
}
