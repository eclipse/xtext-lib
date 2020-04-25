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
public class ArrayExtensionsIntTest {
	private int[] data;

	@Before
	public void init() {
		data = createData();
	}

	private int[] createData() {
		return new int[] {10, 20, 30};
	}

	@Test
	public void testSetGet__int() {
		Assert.assertEquals(10, data[0]);
		Assert.assertEquals(20, data[1]);
		Assert.assertEquals(30, data[2]);
	}

	@Test
	public void testClone__int() {
		int[] clonedData = data.clone();
		Assert.assertEquals(10, clonedData[0]);
		Assert.assertEquals(20, clonedData[1]);
		Assert.assertEquals(30, clonedData[2]);
	}

	@Test
	public void testLength__int() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__int() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals__int() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		int[] newData = createData();
		newData[1] = 0;
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__int() {
		Assert.assertTrue(ArrayExtensions.contains(data, 10));
		Assert.assertTrue(ArrayExtensions.contains(data, 20));
		Assert.assertTrue(ArrayExtensions.contains(data, 30));
		Assert.assertFalse(ArrayExtensions.contains(data, 40));
	}
}
