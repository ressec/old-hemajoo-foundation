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

import static org.jeasy.props.PropertiesInjectorBuilder.aNewPropertiesInjector;

import java.util.Locale;

import org.jeasy.props.annotations.I18NProperty;
import org.jeasy.props.api.PropertiesInjector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hemajoo.foundation.common.resource.bundle.BundleLoadStrategyType;
import com.hemajoo.foundation.common.resource.bundle.ResourceBundleManager;


/**
 * A test case for the {@link BundleField} annotation.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Hemajoo</a>
 * @version 1.0.0
 */
public final class BundleFieldAnnotationTest
{
	/**
	 * Properties injector.
	 */
	protected PropertiesInjector propertiesInjector;

	/**
	 * Initialization of the test cases.
	 * <p>
	 * @throws Exception In case an error occurs during the initialization.
	 */
	@BeforeClass
	public static final void setUpBeforeClass() throws Exception
	{
		// Force early initialization of resource bundle manager.

		// If for some of the resource bundles, they do not exist in the required language, the default one will be used instead.
		ResourceBundleManager.setStrategy(BundleLoadStrategyType.LENIENT);
		ResourceBundleManager.setLocale(Locale.GERMAN);
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
		propertiesInjector = aNewPropertiesInjector();
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
	 * Test the {@link I18NProperty} annotation of the {@code easy-props} library.
	 */
	@SuppressWarnings("nls")
	@Test
	public final void testBundleField()
	{
		ColorBundleField color = new ColorBundleField();
		propertiesInjector.injectProperties(color);

		System.out.println(String.format("Locale: '%s', Color: '%s'", Locale.getDefault(), color.getColorCode()));
	}
}
