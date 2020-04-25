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
public class ArrayExtensionsLongTest {
	private long[] data;

	@Before
	public void init() {
		data = createData();
	}

	private long[] createData() {
		return new long[] {10L, 20L, 30L};
	}

	@Test
	public void testSetGet__long() {
		Assert.assertEquals(10L, data[0]);
		Assert.assertEquals(20L, data[1]);
		Assert.assertEquals(30L, data[2]);
	}

	@Test
	public void testClone__long() {
		long[] clonedData = data.clone();
		Assert.assertEquals(10L, clonedData[0]);
		Assert.assertEquals(20L, clonedData[1]);
		Assert.assertEquals(30L, clonedData[2]);
	}

	@Test
	public void testLength__long() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__long() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals__long() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		long[] newData = createData();
		newData[1] = 0L;
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__long() {
		Assert.assertTrue(ArrayExtensions.contains(data, 10L));
		Assert.assertTrue(ArrayExtensions.contains(data, 20L));
		Assert.assertTrue(ArrayExtensions.contains(data, 30L));
		Assert.assertFalse(ArrayExtensions.contains(data, 40L));
	}
}
