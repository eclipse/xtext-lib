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
public class ArrayExtensionsFloatTest {
	private float[] data;

	@Before
	public void init() {
		data = createData();
	}

	private float[] createData() {
		return new float[] {10.0f, 20.0f, 30.0f};
	}

	@Test
	public void testSetGet__float() {
		Assert.assertEquals(10.0f, data[0], 0.001f);
		Assert.assertEquals(20.0f, data[1], 0.001f);
		Assert.assertEquals(30.0f, data[2], 0.001f);
	}

	@Test
	public void testClone__float() {
		final float[] clonedData = data.clone();
		Assert.assertEquals(10.0f, clonedData[0], 0.001f);
		Assert.assertEquals(20.0f, clonedData[1], 0.001f);
		Assert.assertEquals(30.0f, clonedData[2], 0.001f);
	}

	@Test
	public void testLength__float() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__float() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals__float() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		float[] newData = createData();
		newData[1] = 0.0f;
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__float() {
		Assert.assertTrue(ArrayExtensions.contains(data, 10.0f));
		Assert.assertTrue(ArrayExtensions.contains(data, 20.0f));
		Assert.assertTrue(ArrayExtensions.contains(data, 30.0f));
		Assert.assertFalse(ArrayExtensions.contains(data, 40.0f));
	}

	@Test
	public void testContains__float_NaN() {
		final float[] nanData = { 1.0f, Float.NaN };
		Assert.assertTrue(ArrayExtensions.contains(nanData, Float.NaN));
		Assert.assertTrue(ArrayExtensions.contains(nanData, 0f / 0f));
		Assert.assertTrue(ArrayExtensions.contains(nanData, (float) Math.log(-1)));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Float.NEGATIVE_INFINITY));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Float.POSITIVE_INFINITY));
	}

	@Test
	public void testContains__float_posInfinity() {
		final float[] nanData = { 1.0f, Float.POSITIVE_INFINITY };
		Assert.assertTrue(ArrayExtensions.contains(nanData, Float.POSITIVE_INFINITY));
		Assert.assertTrue(ArrayExtensions.contains(nanData, Float.POSITIVE_INFINITY + 7.2f));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Float.NaN));
		Assert.assertFalse(ArrayExtensions.contains(nanData, 0f / 0f));
		Assert.assertFalse(ArrayExtensions.contains(nanData, (float) Math.log(-1)));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Float.NEGATIVE_INFINITY));
	}

	@Test
	public void testContains__float_negInfinity() {
		final float[] nanData = { 1.0f, Float.NEGATIVE_INFINITY };
		Assert.assertTrue(ArrayExtensions.contains(nanData, Float.NEGATIVE_INFINITY));
		Assert.assertTrue(ArrayExtensions.contains(nanData, Float.NEGATIVE_INFINITY + 7.2f));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Float.NaN));
		Assert.assertFalse(ArrayExtensions.contains(nanData, 0f / 0f));
		Assert.assertFalse(ArrayExtensions.contains(nanData, (float) Math.log(-1)));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Float.POSITIVE_INFINITY));
	}
}
