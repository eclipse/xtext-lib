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
public class ArrayExtensionsBooleanTest {
	private boolean[] data;

	@Before
	public void init() {
		data = createData();
	}

	private boolean[] createData() {
		return new boolean[] {true, false, true};
	}

	@Test
	public void testSetGet__boolean() {
		Assert.assertTrue(data[0]);
		Assert.assertFalse(data[1]);
		Assert.assertTrue(data[2]);
	}

	@Test
	public void testClone__boolean() {
		boolean[] clonedData = data.clone();
		Assert.assertTrue(clonedData[0]);
		Assert.assertFalse(clonedData[1]);
		Assert.assertTrue(clonedData[2]);
	}

	@Test
	public void testLength__boolean() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__boolean() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals__boolean() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		boolean[] newData = createData();
		newData[1] = true;
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__boolean() {
		Assert.assertTrue(ArrayExtensions.contains(data, true));
		Assert.assertTrue(ArrayExtensions.contains(data, false));
		boolean[] onlyFalse = new boolean[] {false, false};
		Assert.assertFalse(ArrayExtensions.contains(onlyFalse, true));
		Assert.assertTrue(ArrayExtensions.contains(onlyFalse, false));
	}
}
