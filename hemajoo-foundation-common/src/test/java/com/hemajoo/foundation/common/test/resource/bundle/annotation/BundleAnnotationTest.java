/*
 * Copyright(c) 2018 Hemajoo Ltd.
 * ---------------------------------------------------------------------------
 * This file is part of the Hemajoo's Foundation project which is licensed
 * under the Apache license version 2 and use is subject to license terms.
 * You should have received a copy of the license with the project's artifact
 * binaries and/or sources.
 * 
 * License can be consulted at http://www.apache.org/licenses/LICENSE-2.0
 * ---------------------------------------------------------------------------
 */
package com.hemajoo.foundation.common.test.resource.bundle.annotation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hemajoo.foundation.common.exception.ResourceBundleException;
import com.hemajoo.foundation.common.resource.bundle.annotation.Bundle;
import com.hemajoo.foundation.common.resource.bundle.visitor.BundleVisitor;

import eu.infomas.annotation.AnnotationDetector;


/**
 * A test case for the {@link Bundle} annotation.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Hemajoo</a>
 * @version 1.0.0
 */
public final class BundleAnnotationTest
{
	/**
	 * Initialization of the test cases.
	 * <p>
	 * @throws Exception In case an error occurs during the initialization.
	 */
	@BeforeClass
	public static final void setUpBeforeClass() throws Exception
	{
		// Empty
	}

	/**
	 * Finalization of the test cases.
	 * <p>
	 * @throws Exception In case an error occurs during the finalization.
	 */
	@AfterClass
	public static final void tearDownAfterClass() throws Exception
	{
		// Empty
	}

	/**
	 * Sets up the fixture.
	 * <p>
	 * @throws Exception In case an error occurs during the setup phase.
	 */
	@Before
	public final void setUp() throws Exception
	{
		// Empty
	}

	/**
	 * Tears down the fixture.
	 * <p>
	 * @throws Exception In case an error occurs during the tear down phase.
	 */
	@After
	public final void tearDown() throws Exception
	{
		// Empty
	}

	/**
	 * Test the registration of resource bundle files.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void registerAnnotatedClasses()
	{
		try
		{
			BundleVisitor visitor = new BundleVisitor();
			final AnnotationDetector detector = new AnnotationDetector(visitor);
			detector.detect();
			visitor.delegateRegistration();
		}
		catch (Exception e)
		{
			throw new ResourceBundleException(e.getMessage(), e);
		}
	}
}
