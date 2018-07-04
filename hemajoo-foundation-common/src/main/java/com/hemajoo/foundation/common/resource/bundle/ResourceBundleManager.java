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
package com.hemajoo.foundation.common.resource.bundle;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.hemajoo.foundation.common.exception.InvalidArgumentException;
import com.hemajoo.foundation.common.exception.ResourceBundleException;
import com.hemajoo.foundation.common.resource.bundle.annotation.BundleEnum;
import com.hemajoo.foundation.common.resource.bundle.annotation.BundleEnumRegister;

import eu.infomas.annotation.AnnotationDetector;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;

/**
 * Resource Bundle Manager used to discover, register and access resource bundles.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
@Log4j
@UtilityClass
public final class ResourceBundleManager
{
	/**
	 * Default resource bundle properties filename.
	 */
	@SuppressWarnings({ "nls", "unused" })
	private static final String DEFAULT_INITIALIZATION_FILE = "base-bundle.properties";

	/**
	 * Bundle filename enumerated value.
	 */
	@SuppressWarnings("nls")
	private static final String BUNDLE_FILENAME = "BundleFilename";

	/**
	 * Collection of resource bundles by resource bundle class.
	 */
	private static final Map<Class<? extends IBundle>, ResourceBundle> BUNDLES = new HashMap<>(1, 0.75f);

	/**
	 * Collection of resource bundle names by resource bundle class.
	 */
	private static final Map<Class<? extends IBundle>, String> NAMES = new HashMap<>(1, 0.75f);

	private static final Map<Class<? extends Enum<?>>, Map<Locale, ResourceBundle>> METHOD_BUNDLES = new HashMap<>(10, 0.75f);

	/**
	 * English locale.
	 */
	@SuppressWarnings("nls")
	private static final Locale ENGLISH = new Locale("en", "US");

	/**
	 * Locale of manager (set to english by default).
	 */
	private static Locale locale = ENGLISH;

	/**
	 * Configuration property file.
	 */
	private static PropertiesConfiguration properties = null;

	/**
	 * Is the manager initialized?
	 */
	private static boolean isInitialized = false;

	/**
	 * Returns a resource string from its key using an enumerated value.
	 * <p>
	 * @param key Resource string key (enumerated value from an enumeration
	 * implementing the {@code IBundle} interface).
	 * @return Resource string.
	 */
	@Synchronized
	public static final String getMessage(final Enum<? extends IBundle> key)
	{
		return getMessage(key, (Object[]) null);
	}

	/**
	 * Returns a message from a resource bundle file handled by the resource
	 * bundle manager based on a given enumerated value representing the
	 * resource key of the message to retrieve.
	 * <p>
	 * @param key Enumerated resource key.
	 * @param parameters Parameters to inject in the message during message
	 * formatting.
	 * @return Message associated to the resource key or an exception message if
	 * the corresponding resource string cannot be loaded.
	 */
	@Synchronized
	public static final String getMessage(final Enum<? extends IBundle> key, final Object... parameters)
	{
		if (key == null)
		{
			throw new InvalidArgumentException(BundleHemajooFoundationCommon.ResourceBundleInvalidKey);
		}

		return retrieve(key, parameters);
	}

	/**
	 * Do a default initialization of the resource bundle manager.
	 * <p>
	 * The default {@code Locale} used is {@code en_US}.
	 * @throws ResourceBundleException Thrown if the initialization of the
	 * resource bundle manager has failed.
	 */
	private static final void initialize()
	{
		// Auto register classes annotated with @BundleEnumRegister annotation.
		autoRegisterAnnotated();
	}

	/**
	 * Auto register resource bundle enumeration classes annotated with {@link BundleEnumRegister}
	 * annotation.
	 */
	private static final void autoRegisterAnnotated()
	{
		try
		{
			BundleEnumRegisterVisitor visitor = new BundleEnumRegisterVisitor();
			final AnnotationDetector detector = new AnnotationDetector(visitor);
			detector.detect();
			visitor.delegateRegistration();
		}
		catch (Exception e)
		{
			setInitialized(false);
			throw new ResourceBundleException(e.getMessage(), e);
		}
	}

	/**
	 * Initializes the resource bundle manager with a custom resource bundle
	 * properties file and with a specific language.
	 * <p>
	 * @param filename Path and name of the property file containing
	 * configuration for the resource bundles.
	 * @param locale {@link Locale} to use for the language.
	 * @throws ResourceBundleException Thrown if a resource bundle file cannot
	 * be found.
	 */
	@SuppressWarnings("unused")
	private static final void initialize(final String filename, final Locale locale)
	{
		if (filename == null || filename.length() == 0)
		{
			throw new InvalidArgumentException(BundleHemajooFoundationCommon.ResourceBundleInvalidName);
		}

		if (locale == null)
		{
			throw new InvalidArgumentException(BundleHemajooFoundationCommon.ResourceBundleInvalidLocale);
		}

		synchronized (BUNDLES)
		{
			try
			{
				setProperties(new PropertiesConfiguration(filename));
				setLocale(locale);
				register(properties);
			}
			catch (final ConfigurationException e)
			{
				setInitialized(false);
				throw new ResourceBundleException(e.getMessage(), e);
			}
		}
	}

	/**
	 * Sets if the resource bundle manager is initialized.
	 * <p>
	 * @param value {@code True} to set the resource bundle manager as being
	 * initialized, {@code false} otherwise.
	 */
	private static final void setInitialized(final boolean value)
	{
		isInitialized = value;
	}

	/**
	 * Sets the properties file.
	 * <p>
	 * @param properties New {@link PropertiesConfiguration} file to use.
	 */
	private static final void setProperties(final PropertiesConfiguration properties)
	{
		ResourceBundleManager.properties = properties;
	}

	/**
	 * Registers a resource bundle using a resource bundle enumeration class
	 * (implementing the {@link IBundle} interface).
	 * <p>
	 * @param bundleEnumClass Resource bundle enumeration class.
	 */
	@Synchronized
	public static final void register(final Class<? extends IBundle> bundleEnumClass)
	{
		ResourceBundle bundle = null;

		IBundle value = bundleEnumClass.getEnumConstants()[0]; // BUNDLE_FILENAME must be at index 0!
		if (value.toString().equals(BUNDLE_FILENAME))
		{
			String filename = value.getKey();

			try
			{
				bundle = ResourceBundle.getBundle(filename, locale);

				// Ensure the loaded bundle is for the required language.
				if (bundle.getLocale().getISO3Language().equals(locale.getISO3Language()))
				{
					register(bundleEnumClass, bundle);
				}
				else
				{
					throw new ResourceBundleException(BundleHemajooFoundationCommon.ResourceBundleError, filename, locale);
				}
			}
			catch (MissingResourceException e)
			{
				// The resource string file does not exist in the given language ... is it an error?
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Registers a new resource bundle and add it to the collection of resource
	 * bundles already managed by the resource bundle manager.
	 * <p>
	 * @param filename Bundle properties configuration file to append.
	 * @throws ResourceBundleException Thrown if the resource bundle file cannot
	 * be found.
	 */
	@Synchronized
	public static final void register(final String filename)
	{
		if (!isInitialized)
		{
			initialize();
		}

		try
		{
			register(new PropertiesConfiguration(filename));

			if (properties != null)
			{
				properties.append(properties);
			}
			else
			{
				setProperties(properties);
			}
		}
		catch (final ConfigurationException e)
		{
			throw new ResourceBundleException(BundleHemajooFoundationCommon.ResourceBundleNotFound, filename, getLocale(), e);
		}
	}

	/**
	 * Registers the resource bundles defined in a given properties file.
	 * <p>
	 * @param properties {@link PropertiesConfiguration} file containing the
	 * resource bundles to register.
	 */
	@SuppressWarnings({ "nls", "unchecked" })
	private static final void register(final PropertiesConfiguration properties)
	{
		String value;
		String name;

		if (properties == null)
		{
			throw new InvalidArgumentException(BundleHemajooFoundationCommon.ResourceBundleInvalidConfiguration);
		}

		final Iterator<String> iter = properties.getKeys();
		while (iter != null && iter.hasNext())
		{
			value = iter.next();
			name = properties.getString(value);

			final ResourceBundle bundle = ResourceBundle.getBundle(name, locale);

			// Ensure the loaded bundle is for the required language.
			if (bundle.getLocale().getLanguage().equals(locale.getLanguage()))
			{
				try
				{
					register((Class<? extends IBundle>) Class.forName(value), bundle);
				}
				catch (ClassNotFoundException e)
				{
					String message = MessageFormat.format("Cannot register properties [file={0}, class={1}, reason={2}, resolution={3}]", name, value, "The given class cannot be found", "Ensure the given class exist as an enumeration class and implements the IBundle interface");

					log.error(message, e);

					// Cannot get resource bundle, so message must be in raw format.
					throw new ResourceBundleException(MessageFormat.format("Cannot register properties [file={0}, class={1}, reason={2}, resolution={3}]", name, value, "The given class cannot be found", "Ensure the given class exist as an enumeration class and implements the IBundle interface"));
				}
			}
			else
			{
				// Cannot get resource bundle, so message must be in raw format.
				throw new ResourceBundleException(MessageFormat.format("Bundle cannot be found [name={0}, locale={1}]", name, locale));
			}
		}
	}

	/**
	 * Registers a resource bundle.
	 * <p>
	 * @param bundleClass Class of the resource bundle to register.
	 * @param bundle Resource bundle to register.
	 * @throws ResourceBundleException Thrown if an error occurred while trying
	 * to register a resource bundle.
	 */
	@SuppressWarnings("nls")
	private static final void register(final Class<? extends IBundle> bundleClass, final ResourceBundle bundle)
	{
		ResourceBundle previous = null;

		try
		{
			if (!BUNDLES.containsKey(bundleClass))
			{
				BUNDLES.put(bundleClass, bundle);
				NAMES.put(bundleClass, bundleClass.getEnumConstants()[0].getKey());

				setInitialized(true);

				String filename = bundleClass.getEnumConstants()[0].getKey() + "_" + locale.toString() + ".properties";
				int index = filename.indexOf(".");
				if (index > -1)
				{
					filename = filename.substring(index + 1, filename.length());
				}

				log.debug(getMessage(BundleHemajooFoundationCommon.ResourceBundleRegistered, bundleClass.getSimpleName(), locale.toString(), Integer.valueOf(bundle.keySet().size()), filename));
			}
			else
			{
				/*
				 * Case where the bundle class already exists. If the locale is
				 * the same, do nothing but if the locale is different then
				 * replace it with the new one.
				 */
				previous = BUNDLES.get(bundleClass);
				if (previous.getLocale().equals(bundle.getLocale()))
				{
					log.debug(getMessage(BundleHemajooFoundationCommon.ResourceBundleAlreadyRegistered, bundleClass.getSimpleName(), locale.toString()));
				}
				else
				{
					BUNDLES.put(bundleClass, bundle);

					log.debug(getMessage(BundleHemajooFoundationCommon.ResourceBundleReplaced, bundleClass.getSimpleName(), previous.getLocale().toString(), locale.toString(), Integer.valueOf(bundle.keySet().size())));
				}
			}
		}
		catch (final Exception e)
		{
			log.error(e.getMessage(), e);
			throw (ResourceBundleException) e;
		}
	}

	/**
	 * Retrieves a message from a resource bundle from its key.
	 * <p>
	 * @param key Resource key to retrieve.
	 * @param parameters Parameters to inject while formatting the message.
	 * @return The formatted message.
	 */
	@SuppressWarnings("nls")
	private static final String retrieve(final Enum<? extends IBundle> key, final Object... parameters)
	{
		final Class<? extends IBundle> bundleClass = key.getDeclaringClass();
		Locale oldLocale = null;
		String message = null;

		try
		{
			if (!isInitialized)
			{
				initialize();
			}

			if (BUNDLES.containsKey(bundleClass))
			{
				try
				{
					return MessageFormat.format(BUNDLES.get(bundleClass).getString(((IBundle) key).getKey()), parameters);
				}
				catch (MissingResourceException e)
				{
					throw new ResourceBundleException(BundleHemajooFoundationCommon.ResourceBundleInvalidKey, bundleClass.getSimpleName(), key.name(), bundleClass.getEnumConstants()[0].getKey(), getLocale(), e);
				}
			}
		}
		catch (Exception e)
		{
			return "Resource bundle key cannot be found [bundle=" + bundleClass.getName() + ", key=" + key.name() + "] due to: " + e.getMessage();
		}

		return "Resource bundle key cannot be found [bundle=" + bundleClass.getName() + ", key=" + key.name() + "]";
	}

	/**
	 * Sets the language used by the {@link ResourceBundleManager}.
	 * <p>
	 * <b>Note:</b> Calling this service forces the
	 * {@link ResourceBundleManager} to reload all the resource bundle files
	 * according to the new locale.
	 * <p>
	 * @param locale {@link Locale} corresponding to the new language to set.
	 */
	@Synchronized
	public static final void setLocale(final Locale locale)
	{
		if (!locale.getLanguage().equals(ResourceBundleManager.locale.getLanguage()))
		{
			refresh(locale);
		}
	}

	/**
	 * Refresh all the resource bundles already handled by the
	 * {@link ResourceBundleManager} for the given {@link Locale}.
	 * <p>
	 * @param locale {@link Locale} corresponding to the new language to use.
	 */
	@SuppressWarnings("nls")
	private static final void refresh(final Locale locale)
	{
		BUNDLES.clear();
		for (Class<? extends IBundle> bundleClass : NAMES.keySet())
		{
			final ResourceBundle bundle = ResourceBundle.getBundle(NAMES.get(bundleClass), locale);
			if (bundle != null && !bundle.getLocale().getLanguage().equals(locale.getLanguage()))
			{
				log.error("Bundle cannot be found [name=" + NAMES.get(bundleClass) + ", locale=" + locale.toString() + "]. Using default one [name=" + NAMES.get(bundleClass) + ", locale=" + getLocale().toString() + "]");
			}

			register(bundleClass, bundle);
		}

		ResourceBundleManager.locale = locale;
	}

	/**
	 * Returns a copy of the locale used by the {@link ResourceBundleManager}.
	 * <p>
	 * @return {@link Locale}
	 */
	public static final Locale getLocale()
	{
		return new Locale(locale.getLanguage(), locale.getCountry(), locale.getVariant());
	}

	/**
	 * Returns the resource bundle string of a given enumerated value for the given enumeration class. This
	 * method is generally used by enumeration classes using the {@code BundleEnum} annotation.
	 * <hr>
	 * @param eClass Class of the enumeration.
	 * @param e Enumerated value.
	 * @return Resource bundle string.
	 */
	public static final String getResourceForMethodName(@NonNull final Class<? extends Enum<?>> eClass, final Enum<?> e)
	{
		return getResourceForMethodName(eClass, e, getLocale());
	}

	/**
	 * Returns the resource bundle string of a given enumerated value for the given enumeration class. This
	 * method is generally used by enumeration classes using the {@code BundleEnum} annotation.
	 * <hr>
	 * @param eClass Class of the enumeration.
	 * @param e Enumerated value.
	 * @param locale {@link Locale} to use for resource string retrieval.
	 * @return Resource bundle string.
	 */
	@SuppressWarnings({ "nls", "unchecked" })
	public static final String getResourceForMethodName(@NonNull final Class<? extends Enum<?>> eClass, final Enum<?> e, final Locale locale)
	{
		String key = null;
		ResourceBundle bundle;
		String methodName = null;
		String className;

		int index = 1;
		boolean found = false;
		while (!found)
		{
			className = Thread.currentThread().getStackTrace()[index].getClassName();
			if (className.equals(eClass.getName()))
			{
				methodName = Thread.currentThread().getStackTrace()[index].getMethodName();
				found = true;
			}
			else
			{
				index++;
			}
		}

		if (found)
		{
			for (Method method : eClass.getMethods())
			{
				BundleEnum annotation = method.getAnnotation(BundleEnum.class);
				if (annotation != null && method.getName().equals(methodName))
				{
					// Does the resource bundle has already been loaded?
					Class<?> aClass = method.getDeclaringClass();
					Map<Locale, ResourceBundle> map = METHOD_BUNDLES.get(aClass);
					if (map == null)
					{
						map = new HashMap<>();
						bundle = ResourceBundle.getBundle(annotation.file(), locale);
						map.put(locale, bundle);
						METHOD_BUNDLES.put((Class<? extends Enum<?>>) aClass, map);
					}
					else
					{
						bundle = map.get(locale);
						if (bundle == null)
						{
							bundle = ResourceBundle.getBundle(annotation.file(), locale);
							map.put(locale, bundle);
							METHOD_BUNDLES.put((Class<? extends Enum<?>>) aClass, map);
						}
					}

					//bundle = ResourceBundle.getBundle(annotation.file(), locale);
					key = annotation.path() + "." + e.name();
					return bundle.getString(key);
				}
			}
		}

		throw new ResourceBundleException(BundleHemajooFoundationCommon.ResourceBundleInvalidKey, null, key, null, locale, e);
	}
}
