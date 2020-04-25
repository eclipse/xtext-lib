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
public class ArrayExtensionsDoubleTest {
	private double[] data;

	@Before
	public void init() {
		data = createData();
	}

	private double[] createData() {
		return new double[] {10.0, 20.0, 30.0};
	}

	@Test
	public void testSetGet__double() {
		Assert.assertEquals(10.0, data[0], 0.001);
		Assert.assertEquals(20.0, data[1], 0.001);
		Assert.assertEquals(30.0, data[2], 0.001);
	}

	@Test
	public void testClone__double() {
		double[] clonedData = data.clone();
		Assert.assertEquals(10.0, clonedData[0], 0.001);
		Assert.assertEquals(20.0, clonedData[1], 0.001);
		Assert.assertEquals(30.0, clonedData[2], 0.001);
	}

	@Test
	public void testLength__double() {
		Assert.assertEquals(3, data.length);
	}

	@Test
	public void testHashCode__double() {
		Assert.assertEquals(Objects.hashCode(data), data.hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals__double() {
		Assert.assertTrue(data.equals(data));
		Assert.assertFalse(data.equals(createData()));
		Assert.assertFalse(data.equals(new Object[3]));
		Assert.assertFalse(data.equals(null));
		double[] newData = createData();
		newData[1] = 0.0;
		Assert.assertFalse(data.equals(newData));
	}

	@Test
	public void testContains__double() {
		Assert.assertTrue(ArrayExtensions.contains(data, 10.0));
		Assert.assertTrue(ArrayExtensions.contains(data, 20.0));
		Assert.assertTrue(ArrayExtensions.contains(data, 30.0));
		Assert.assertFalse(ArrayExtensions.contains(data, 40.0));
	}

	@Test
	public void testContains__float_NaN() {
		double[] nanData = { 1.0, Double.NaN };
		Assert.assertTrue(ArrayExtensions.contains(nanData, Double.NaN));
		Assert.assertTrue(ArrayExtensions.contains(nanData, 0f / 0f));
		Assert.assertTrue(ArrayExtensions.contains(nanData, Math.log(-1)));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Double.NEGATIVE_INFINITY));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Double.POSITIVE_INFINITY));
	}

	@Test
	public void testContains__double_posInfinity() {
		double[] nanData = { 1.0f, Double.POSITIVE_INFINITY };
		Assert.assertTrue(ArrayExtensions.contains(nanData, Double.POSITIVE_INFINITY));
		Assert.assertTrue(ArrayExtensions.contains(nanData, Double.POSITIVE_INFINITY + 7.2f));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Double.NaN));
		Assert.assertFalse(ArrayExtensions.contains(nanData, 0f / 0f));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Math.log(-1)));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Double.NEGATIVE_INFINITY));
	}

	@Test
	public void testContains__double_negInfinity() {
		double[] nanData = { 1.0f, Double.NEGATIVE_INFINITY };
		Assert.assertTrue(ArrayExtensions.contains(nanData, Double.NEGATIVE_INFINITY));
		Assert.assertTrue(ArrayExtensions.contains(nanData, Double.NEGATIVE_INFINITY + 7.2f));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Double.NaN));
		Assert.assertFalse(ArrayExtensions.contains(nanData, 0f / 0f));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Math.log(-1)));
		Assert.assertFalse(ArrayExtensions.contains(nanData, Double.POSITIVE_INFINITY));
	}
}
