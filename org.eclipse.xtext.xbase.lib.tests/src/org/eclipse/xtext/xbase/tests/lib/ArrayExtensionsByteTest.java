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
public class ArrayExtensionsByteTest {
	private byte[] data;

	private final byte _10 = 10;

	private final byte _20 = 20;

	private final byte _30 = 30;

	@Before
	public void init() {
		data = createData();
	}

	private byte[] createData() {
		return new byte[] {_10, _20, _30};
	}

	@Test
	public void testSetGet__byte() {
		Assert.assertEquals(_10, data[0]);
		Assert.assertEquals(_20, data[1]);
		Assert.assertEquals(_30, data[2]);
	}

	@Test
	public void testClone__byte() {
		byte[] clonedData = data.clone();
		Assert.assertEquals(_10, clonedData[0]);
		Assert.assertEquals(_20, clonedData[1]);
		Assert.assertEquals(_30, clonedData[2]);
	}

	@Test
	public void testLength__byte() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__byte() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals__byte() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		byte[] newData = createData();
		newData[1] = (byte) 0;
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__byte() {
		Assert.assertTrue(ArrayExtensions.contains(data, (byte) 10));
		Assert.assertTrue(ArrayExtensions.contains(data, (byte) 20));
		Assert.assertTrue(ArrayExtensions.contains(data, (byte) 30));
		Assert.assertFalse(ArrayExtensions.contains(data, (byte) 40));
	}
}
