/*******************************************************************************
 * Copyright (c) 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.tests.lib;

import java.util.List;

import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author Karsten Thoms - Initial contribution and API
 */
public class ListExtensionsTest extends Assert {
	protected Integer first = Integer.valueOf(1);
	protected Integer second = Integer.valueOf(2);
	protected Integer third = Integer.valueOf(3);
	protected Integer forth = Integer.valueOf(4);
	protected Integer fifth = Integer.valueOf(5);

	protected List<Integer> operator_plus(List<Integer> first, List<Integer> second) {
		return ListExtensions.operator_plus(first, second);
	}

	protected boolean is(Iterable<Integer> input, Integer... elements) {
		return Iterables.elementsEqual(input, Lists.newArrayList(elements));
	}
	
	
	@Test public void testOperatorPlus_Same() {
		List<Integer> data = Lists.newArrayList(first, second);
		assertTrue(is(operator_plus(data, data), first, second, first, second));
	}
	
	@Test public void testOperatorPlus() {
		List<Integer> firstData = Lists.newArrayList(first, second);
		List<Integer> secondData = Lists.newArrayList(third, forth);
		assertTrue(is(operator_plus(firstData, secondData), first, second, third, forth));
	}
	
	@Test public void testOperatorPlus_NPE_left() {
		try {
			operator_plus(null, Lists.newArrayList(first, second));
			fail("expected NullPointerException");
		} catch(NullPointerException npe) {
			// expected
		}
	}
	
	@Test public void testOperatorPlus_NPE_right() {
		try {
			operator_plus(Lists.newArrayList(first, second), null);
			fail("expected NullPointerException");
		} catch(NullPointerException npe) {
			// expected
		}
	}
	

}
