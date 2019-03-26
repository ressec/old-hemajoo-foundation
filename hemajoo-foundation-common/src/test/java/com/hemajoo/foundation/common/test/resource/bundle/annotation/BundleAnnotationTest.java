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

import static org.junit.Assert.fail;

import java.text.Collator;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hemajoo.foundation.common.resource.ResourceException;
import com.hemajoo.foundation.common.resource.bundle.BundleLoadStrategyType;
import com.hemajoo.foundation.common.resource.bundle.HemajooFoundationCommonBundle;
import com.hemajoo.foundation.common.resource.bundle.ResourceBundleManager;
import com.hemajoo.foundation.common.resource.bundle.annotation.Bundle;
import com.hemajoo.foundation.common.test.resource.bundle.type.TestHonorificType;


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
		// Ensure the HemajooFoundationCommonBundle and associated resource bundle file is loaded first.
		ResourceBundleManager.getMessage(HemajooFoundationCommonBundle.TEST_DUMMY_LANGUAGE);
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
	 * Test the direct registration of a resource bundle file (not thorugh the use of an annotation).
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test
	public final void registerAnnotatedClasses()
	{
		ResourceBundleManager.getLocale();
		ResourceBundleManager.register("bundle/hemajoo-foundation-common");
	}

	/**
	 * Test the retrieving of a resource bundle string through its key.
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test
	public final void testRetrieveMessageByKey()
	{
		try
		{
			ResourceBundleManager.setLocale(Locale.ENGLISH);
			Assert.assertTrue("Not the expected value!", Collator.getInstance(ResourceBundleManager.ENGLISH)
					.equals(ResourceBundleManager.getMessage("queen.album.ten-first.9"), "Flash Gordon"));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test
	public final void testDirectBundleRegistrationUsingRoot()
	{
		try
		{
			ResourceBundleManager.setLocale(Locale.ENGLISH);
			ResourceBundleManager.register("bundle/color/color", "color");

			Assert.assertTrue("Not the expected value!", Collator.getInstance(ResourceBundleManager.ENGLISH)
					.equals(ResourceBundleManager.getMessage("blue.name"), "Blue"));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test the retrieving of a resource bundle string through an enumerated value.
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test
	public final void testRetrieveMessageByEnum()
	{
		try
		{
			ResourceBundleManager.setLocale(Locale.FRENCH);
			Assert.assertTrue("Not the expected value!",
					ResourceBundleManager.getMessage(HemajooFoundationCommonBundle.TEST_DUMMY_LANGUAGE).equals("Français"));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test the registration of a resource bundle in a different language than the resource
	 * bundle manager one.
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test
	public final void testRegisterBundleWithNonCompatibleLocaleLenient()
	{
		ResourceBundleManager.setStrategy(BundleLoadStrategyType.LENIENT);
		ResourceBundleManager.setLocale(Locale.ENGLISH);
		ResourceBundleManager.register("bundle/hemajoo-foundation-common", Locale.FRENCH);
	}

	/**
	 * Test the registration of a resource bundle in a different language than the resource
	 * bundle manager one.
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test(expected = ResourceException.class)
	public final void testRegisterBundleWithNonCompatibleLocaleStrict()
	{
		ResourceBundleManager.setStrategy(BundleLoadStrategyType.STRICT);
		ResourceBundleManager.setLocale(Locale.ENGLISH);
		ResourceBundleManager.register("bundle/hemajoo-foundation-common", Locale.FRENCH);
	}

	/**
	 * Test the change of the resource bundle manager locale.
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test
	public final void testChangeLocale()
	{
		try
		{
			ResourceBundleManager.setLocale(Locale.FRENCH);

			Assert.assertTrue("Not the expected value!", ResourceBundleManager.getMessage("fruit.apple.name").equals("Pomme"));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test a non existing resource bundle entry through the use of an enumerated value.
	 */
	@SuppressWarnings("static-method")
	@Test(expected = ResourceException.class)
	public final void testNonExistingResourceBundleKey()
	{
		ResourceBundleManager.setLocale(Locale.FRENCH);
		ResourceBundleManager.getMessage(HemajooFoundationCommonBundle.RESOURCE_BUNDLE_KEYDOESNOTEXIST);
	}

	/**
	 * Test a non existing resource bundle entry through the use of an enumerated value.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testNotInitialized()
	{
		ResourceBundleManager.getMessage(HemajooFoundationCommonBundle.TEST_DUMMY_LANGUAGE);
	}

	/**
	 * Test the retrieving of the resource bundle value through an enumerated value.
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test
	public final void testHonorificTypeShort()
	{
		Assert.assertTrue("Not the expected value!", TestHonorificType.DAME.getShortTitle().equals("Dame"));
	}

	/**
	 * Test the retrieving of the resource bundle value through an enumerated value.
	 */
	@SuppressWarnings({ "static-method", "nls" })
	@Test
	public final void testHonorificTypeHelpinFrench()
	{
		ResourceBundleManager.setLocale(Locale.FRENCH);
		Assert.assertTrue("Not the expected value!", TestHonorificType.MADAM.getHelpTitle() != null);
	}
}
