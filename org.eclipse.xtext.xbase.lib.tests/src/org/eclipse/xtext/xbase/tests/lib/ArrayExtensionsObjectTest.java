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
public class ArrayExtensionsObjectTest {
	private Object[] data;

	@Before
	public void init() {
		data = createData();
	}

	private Object[] createData() {
		Object[] array = new Object[3];
		array[0] = Integer.valueOf(0);
		array[1] = Integer.valueOf(1);
		array[2] = null;
		return array;
	}

	@Test
	public void testSetGet__Object() {
		Assert.assertSame(Integer.valueOf(0), data[0]);
		Assert.assertSame(Integer.valueOf(1), data[1]);
		Assert.assertNull(null, data[2]);
	}

	@Test
	public void testClone__Object() {
		Object[] clonedData = data.clone();
		Assert.assertSame(Integer.valueOf(0), clonedData[0]);
		Assert.assertSame(Integer.valueOf(1), clonedData[1]);
		Assert.assertNull(null, data[2]);
	}

	@Test
	public void testLength__Object() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__Object() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@Test
	public void testEquals__Object() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		Object[] newData = createData();
		newData[1] = "Hello World";
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__Object() {
		Assert.assertTrue(ArrayExtensions.contains(data, Integer.valueOf(0)));
		Assert.assertTrue(ArrayExtensions.contains(data, Integer.valueOf(1)));
		Assert.assertTrue(ArrayExtensions.contains(data, null));
		Assert.assertFalse(ArrayExtensions.contains(data, Integer.valueOf(3)));
		Assert.assertFalse(ArrayExtensions.contains(data, Integer.valueOf(4)));
	}
}
