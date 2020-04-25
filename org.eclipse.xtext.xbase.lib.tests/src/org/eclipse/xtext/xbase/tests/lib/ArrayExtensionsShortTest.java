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
public class ArrayExtensionsShortTest {
	private short[] data;

	private final short _10 = 10;

	private final short _20 = 20;

	private final short _30 = 30;

	@Before
	public void init() {
		data = createData();
	}

	private short[] createData() {
		return new short[] {_10, _20, _30};
	}

	@Test
	public void testSetGet__short() {
		Assert.assertEquals(_10, data[0]);
		Assert.assertEquals(_20, data[1]);
		Assert.assertEquals(_30, data[2]);
	}

	@Test
	public void testClone__short() {
		short[] clonedData = data.clone();
		Assert.assertEquals(_10, clonedData[0]);
		Assert.assertEquals(_20, clonedData[1]);
		Assert.assertEquals(_30, clonedData[2]);
	}

	@Test
	public void testLength__short() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__short() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals__short() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		short[] newData = createData();
		newData[1] = (short) 0;
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__short() {
		Assert.assertTrue(ArrayExtensions.contains(data, (short) 10));
		Assert.assertTrue(ArrayExtensions.contains(data, (short) 20));
		Assert.assertTrue(ArrayExtensions.contains(data, (short) 30));
		Assert.assertFalse(ArrayExtensions.contains(data, (short) 40));
	}
}
