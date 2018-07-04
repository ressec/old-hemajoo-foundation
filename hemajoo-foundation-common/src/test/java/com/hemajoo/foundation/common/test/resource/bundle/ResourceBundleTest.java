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
package com.hemajoo.foundation.common.test.resource.bundle;

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
import com.hemajoo.foundation.common.resource.bundle.BundleHemajooFoundationCommon;
import com.hemajoo.foundation.common.resource.bundle.ResourceBundleManager;
import com.hemajoo.foundation.common.test.resource.bundle.type.TestHonorificType;


/**
 * A test case for the {@link BundleHemajooFoundationCommon} class using the {@link ResourceBundleManager} class.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Hemajoo</a>
 * @version 1.0.0
 */
@SuppressWarnings("nls")
public final class ResourceBundleTest
{
	/**
	 * Component name to test.
	 */
	private static final String COMPONENT_NAME = "hemajoo-foundation-common";

	/**
	 * Dummy message in english.
	 */
	private static final String DUMMY_ENGLISH = "A test message from component: " + COMPONENT_NAME;

	/**
	 * Dummy message in french.
	 */
	private static final String DUMMY_FRENCH = "Un message de test du composant: " + COMPONENT_NAME;

	/**
	 * Dummy message in german.
	 */
	private static final String DUMMY_GERMAN = "Ein Test Nachricht von der Komponente: " + COMPONENT_NAME;

	/**
	 * Dummy message in spanish.
	 */
	private static final String DUMMY_SPANISH = "Un mensaje de prueba a partir del componente: " + COMPONENT_NAME;

	/**
	 * Dummy message in italian.
	 */
	private static final String DUMMY_ITALIAN = "Un messaggio di prova dal componente: " + COMPONENT_NAME;

	/**
	 * Locale for english.
	 */
	private static Locale ENGLISH;

	/**
	 * Locale for french.
	 */
	private static Locale FRENCH;

	/**
	 * Locale for german.
	 */
	private static Locale GERMAN;

	/**
	 * Locale for italian.
	 */
	private static Locale ITALIAN;

	/**
	 * Locale for spanish.
	 */
	private static Locale SPANISH;

	/**
	 * Initialization of the test cases.
	 * <p>
	 * @throws Exception In case an error occurs during the initialization.
	 */
	@BeforeClass
	public static final void setUpBeforeClass() throws Exception
	{
		ITALIAN = new Locale("it", "IT");
		FRENCH = new Locale("fr", "FR");
		ENGLISH = new Locale("en", "US");
		GERMAN = new Locale("de", "DE");
		SPANISH = new Locale("es", "ES");
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
	 * Test the registration of a resource bundle in english using an
	 * enumeration class.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void registerBundleByEnumClassInEnglish()
	{
		ResourceBundleManager.setLocale(ENGLISH);
		ResourceBundleManager.register(BundleHemajooFoundationCommon.class);

		Assert.assertTrue(ResourceBundleManager.getLocale().getLanguage() == ENGLISH.getLanguage());
	}

	/**
	 * Test the registration of a resource bundle in french using an enumeration
	 * class.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void registerBundleByEnumClassInFrench()
	{
		ResourceBundleManager.setLocale(FRENCH);
		ResourceBundleManager.register(BundleHemajooFoundationCommon.class);

		Assert.assertTrue(ResourceBundleManager.getLocale().getLanguage() == FRENCH.getLanguage());
	}

	/**
	 * Test the registration of a resource bundle in german using an enumeration
	 * class.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void registerBundleByEnumClassInGerman()
	{
		ResourceBundleManager.register(BundleHemajooFoundationCommon.class);
		ResourceBundleManager.setLocale(GERMAN);

		Assert.assertTrue(ResourceBundleManager.getLocale().getLanguage() == GERMAN.getLanguage());
	}

	/**
	 * Test the registration of a resource bundle in spanish using an
	 * enumeration class.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void registerBundleByEnumClassInSpanish()
	{
		ResourceBundleManager.setLocale(SPANISH);
		ResourceBundleManager.register(BundleHemajooFoundationCommon.class);

		Assert.assertTrue(ResourceBundleManager.getLocale().getLanguage() == SPANISH.getLanguage());
	}

	/**
	 * Test the registration of a resource bundle in italian using an
	 * enumeration class.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void registerBundleByEnumClassInItalian()
	{
		ResourceBundleManager.setLocale(ITALIAN);
		ResourceBundleManager.register(BundleHemajooFoundationCommon.class);

		Assert.assertTrue(ResourceBundleManager.getLocale().getLanguage() == ITALIAN.getLanguage());
	}

	/**
	 * Test the extraction of a dummy message in english.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void retrieveDummyMessageInEnglish()
	{
		try
		{
			ResourceBundleManager.setLocale(ENGLISH);

			Assert.assertTrue("Not the english dummy message!",
					Collator.getInstance(ENGLISH).equals(ResourceBundleManager.getMessage(BundleHemajooFoundationCommon.TestDummy), DUMMY_ENGLISH));

		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test the extraction of a dummy message in french.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void retrieveDummyMessageInFrench()
	{
		try
		{
			ResourceBundleManager.setLocale(FRENCH);

			Assert.assertTrue("Not the french dummy message!",
					Collator.getInstance(FRENCH).equals(ResourceBundleManager.getMessage(BundleHemajooFoundationCommon.TestDummy), DUMMY_FRENCH));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test the extraction of a dummy message in german.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void retrieveDummyMessageInGerman()
	{
		try
		{
			ResourceBundleManager.setLocale(GERMAN);

			Assert.assertTrue("Not the german dummy message!",
					Collator.getInstance(GERMAN).equals(ResourceBundleManager.getMessage(BundleHemajooFoundationCommon.TestDummy), DUMMY_GERMAN));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test the extraction of a dummy message in spanish.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void retrieveDummyMessageInSpanish()
	{
		try
		{
			ResourceBundleManager.setLocale(SPANISH);

			Assert.assertTrue("Not the spanish dummy message!",
					Collator.getInstance(SPANISH).equals(ResourceBundleManager.getMessage(BundleHemajooFoundationCommon.TestDummy), DUMMY_SPANISH));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test the extraction of a dummy message in italian.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void retrieveDummyMessageInItalian()
	{
		try
		{
			ResourceBundleManager.setLocale(ITALIAN);

			Assert.assertTrue("Not the italian dummy message!",
					Collator.getInstance(ITALIAN).equals(ResourceBundleManager.getMessage(BundleHemajooFoundationCommon.TestDummy), DUMMY_ITALIAN));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}

	//	/**
	//	 * This test is using a fake resource bundle module enumeration class named: 'MyCustomTestBundle' that contains one
	//	 * enumerated value mapped to an externalized resource string in resource bundle file named: 'example-module_fr.properties'.
	//	 * This resource bundle file in french contains a key named: 'prefix1-prefix2.test.dummy'.
	//	 * <p>
	//	 * It tests the extraction of the dummy message entry in french from the custom test resource bundle module.
	//	 */
	//	@SuppressWarnings("static-method")
	//	@Test
	//	public final void retrieveDummyMessageInFrenchFromCustomModule()
	//	{
	//		try
	//		{
	//			ResourceBundleManager.setLocale(FRENCH);
	//
	//			Assert.assertTrue("Not the expected dummy message in french!",
	//					Collator.getInstance(FRENCH).equals(ResourceBundleManager.getMessage(MyCustomTestBundle.TestDummy), "Un message de test du composant: example-module"));
	//		}
	//		catch (final ResourceException e)
	//		{
	//			fail(e.getLocalizedMessage());
	//		}
	//	}

	/**
	 * Test the extraction of the 'Mister' {@link TestHonorificType} in english.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void retrieveHonorificTypeForMisterInEnglish()
	{
		try
		{
			ResourceBundleManager.setLocale(ENGLISH);

			Assert.assertTrue("Not the expected long value returned!",
					Collator.getInstance(ENGLISH).equals(TestHonorificType.MR.getLongTitle(), "Mister"));

			Assert.assertTrue("Not the expected short value returned!",
					Collator.getInstance(ENGLISH).equals(TestHonorificType.MR.getShortTitle(), "MR"));

			Assert.assertTrue("Not the expected help value returned!",
					Collator.getInstance(ENGLISH).equals(TestHonorificType.MR.getHelpTitle(), "For men, regardless of marital status."));
		}
		catch (final ResourceException e)
		{
			fail(e.getLocalizedMessage());
		}
	}
}
