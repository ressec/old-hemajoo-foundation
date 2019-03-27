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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import com.hemajoo.foundation.common.exception.InvalidArgumentException;
import com.hemajoo.foundation.common.exception.ResourceBundleException;
import com.hemajoo.foundation.common.resource.ResourceException;
import com.hemajoo.foundation.common.resource.bundle.annotation.Bundle;
import com.hemajoo.foundation.common.resource.bundle.annotation.BundleMethod;
import com.hemajoo.foundation.common.resource.bundle.visitor.BundleVisitor;

import eu.infomas.annotation.AnnotationDetector;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;

/**
 * Resource Bundle Manager is used to discover, register and access resource bundle entries.
 * <p>
 * Generally heavily used for internationalization purposes.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
@Log4j
@UtilityClass
public final class ResourceBundleManager
{
	/**
	 * The dot character.
	 */
	@SuppressWarnings("nls")
	private static final String CHARACTER_DOT = ".";

	/**
	 * Bundle load strategy type.
	 */
	private static BundleLoadStrategyType strategy = BundleLoadStrategyType.LENIENT;

	/**
	 * Thread-safe collection of all resource bundle entries (flat mode).
	 */
	private static final Map<String, String> ENTRIES = new ConcurrentHashMap<>(1000);

	/**
	 * Thread-safe collection of resource bundles registered through annotated class.
	 */
	private static final Map<Class<?>, List<ResourceBundle>> CLASSES = new ConcurrentHashMap<>();

	/**
	 * Thread-safe collection used to store resource bundle files directly registered (by opposition to resource bundle files
	 * registered through annotated class.
	 */
	private static final Map<String, String> OTHER = new ConcurrentHashMap<>();

	/**
	 * Default fall-back locale.
	 */
	public static Locale defaultLocale = Locale.ENGLISH;

	/**
	 * Locale of manager (set to english by default).
	 */
	private static Locale locale = ResourceBundleManager.defaultLocale;

	/**
	 * Is the manager initialized?
	 */
	private static boolean isInitialized = false;

	/**
	 * Is the manager initializing?
	 */
	private static boolean isInitializing = false;

	static
	{
		// Forces the JVM to have the same language default than the resource bundle manager.
		Locale.setDefault(defaultLocale);
	}

	/**
	 * Returns a resource bundle value given its key.
	 * <p>
	 * @param key Resource bundle key.
	 * @return Resource bundle value.
	 */
	public static final String getMessage(final @NonNull String key)
	{
		initialize();

		return retrieve(key);
	}

	/**
	 * Returns a resource string from its key using an enumerated value.
	 * <p>
	 * @param key Resource string key (enumerated value from an enumeration
	 * implementing the {@code IBundle} interface).
	 * @return Resource string.
	 */
	public static final String getMessage(final Enum<? extends IBundle> key)
	{
		initialize();

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
	public static final String getMessage(final Enum<? extends IBundle> key, final Object... parameters)
	{
		initialize();

		if (key == null)
		{
			throw new InvalidArgumentException(HemajooFoundationCommonBundle.RESOURCE_BUNDLE_INVALIDKEY);
		}

		return retrieve(key, parameters);
	}

	/**
	 * Do a default initialization of the resource bundle manager.
	 * <p>
	 * The default {@code Locale} used is {@code en_US}.
	 * @return {@code True} if the initialization is successful, {@code false} otherwise.
	 * @throws ResourceBundleException Thrown if the initialization of the
	 * resource bundle manager has failed.
	 */
	private static final boolean initialize()
	{
		if (!isInitialized && !isInitializing)
		{
			isInitializing = true;

			// Auto register classes annotated with @BundleEnumRegister annotation.
			autoRegisterAnnotated();

			isInitializing = false;
			isInitialized = true;
		}

		return true;
	}

	/**
	 * Auto register resource bundle enumeration classes annotated with {@link Bundle}
	 * annotation.
	 */
	private static final void autoRegisterAnnotated()
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
			setInitialized(false);
			throw new ResourceBundleException(e.getMessage(), e);
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
	 * Registers a resource bundle through a class annotated with the {@link Bundle} annotation.
	 * <p>
	 * @param annotatedClass Class annotated with the {@link Bundle} annotation.
	 */
	public static final void register(final @NonNull Class<?> annotatedClass)
	{
		initialize();

		ResourceBundle bundle = null;

		// Extract the resource bundle file name.
		Bundle annotation = annotatedClass.getAnnotation(Bundle.class);
		String filename = annotation.file();

		try
		{
			bundle = ResourceBundle.getBundle(filename, locale);

			// Ensure the loaded bundle is for the required language.
			if (bundle.getLocale().getISO3Language().equals(locale.getISO3Language()))
			{
				register(annotatedClass, bundle);
			}
			else
			{
				// Are we in LENIENT mode?
				if (strategy == BundleLoadStrategyType.LENIENT)
				{
					// Then try to load the default resource bundle.
					bundle = ResourceBundle.getBundle(filename, defaultLocale);
					register(annotatedClass, bundle);
				}
				else
				{
					throw new ResourceBundleException(HemajooFoundationCommonBundle.RESOURCE_BUNDLE_ERROR, filename, locale);
				}
			}
		}
		catch (MissingResourceException e)
		{
			// The resource string file does not exist in the given language ... is it an error?
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Registers a new resource bundle file.
	 * <p>
	 * @param filename Resource bundle file to register.
	 * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
	 */
	@SuppressWarnings("nls")
	public static final void register(final String filename)
	{
		initialize();

		register(filename, "", locale);
	}

	/**
	 * Registers directly a new resource bundle file with a root path to ease key access.
	 * <p>
	 * @param filename Resource bundle file to register.
	 * @param root Root path to access the keys.
	 * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
	 */
	public static final void register(final @NonNull String filename, final @NonNull String root)
	{
		initialize();

		register(filename, root, locale);
	}

	/**
	 * Registers directly a new resource bundle file with a given locale.
	 * <p>
	 * @param filename Resource bundle file to register.
	 * @param locale Locale to use to register the resource bundle.
	 * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
	 */
	@SuppressWarnings({ "nls", "hiding" })
	public static final void register(final @NonNull String filename, final @NonNull Locale locale)
	{
		initialize();

		register(filename, "", locale);
	}

	/**
	 * Sets the resource bundle load strategy.
	 * <hr>
	 * @param strategy Load strategy type.
	 * @see BundleLoadStrategyType
	 */
	@SuppressWarnings("nls")
	public static final void setStrategy(final @NonNull BundleLoadStrategyType strategy)
	{
		// This service does not auto initialize the manager!
		ResourceBundleManager.strategy = strategy;

		log.info(String.format("Resource bundle manager load strategy set to: '%s'", strategy));
	}

	/**
	 * Registers directly a new resource bundle file with a root path to ease key access and a given locale.
	 * <p>
	 * @param filename Resource bundle file to register.
	 * @param root Root path to access the keys.
	 * @param locale Locale to use to register the resource bundle.
	 * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
	 */
	@SuppressWarnings({ "nls", "hiding" })
	public static final void register(final @NonNull String filename, final @NonNull String root, final @NonNull Locale locale)
	{
		String message;

		initialize();

		try
		{
			ResourceBundle bundle = ResourceBundle.getBundle(filename, locale);

			// Ensure the registered resource bundle is for the required language.
			if (!bundle.getLocale().getISO3Language().equals(ResourceBundleManager.locale.getISO3Language()))
			{
				if (ResourceBundleManager.strategy == BundleLoadStrategyType.LENIENT)
				{
					// As load strategy is set to lenient, let's try to load one with correct locale.
					bundle = ResourceBundle.getBundle(filename, ResourceBundleManager.locale);
					if (!bundle.getLocale().getISO3Language().equals(ResourceBundleManager.locale.getISO3Language()))
					{
						message = String.format("Resource bundle: '%s' cannot be found for language: '%s'",filename, ResourceBundleManager.locale);
						log.warn(message);
					}

					mergeEntries(filename, root);
					log.info(String.format("Resource bundle: '%s' registered for language: '%s' due to LENIENT mode", filename, bundle.getLocale().getLanguage().length() == 0 ? ResourceBundleManager.defaultLocale : bundle.getLocale()));
				}
				else
				{
					message = String.format("Resource bundle: '%s' with language: '%s' ignored because it does not match resource bundle manager language set to: '%s'", filename, locale, ResourceBundleManager.locale);
					log.error(message);
					throw new ResourceBundleException(message);
				}
			}
			else
			{
				mergeEntries(filename, root);
				log.info(String.format("Resource bundle: '%s' registered for language: '%s'", filename, bundle.getLocale()));
			}
		}
		catch (MissingResourceException e)
		{
			// The resource string file does not exist in the given language ... is it an error?
			log.error(e.getMessage(), e);
			throw new ResourceBundleException(e.getMessage(), e);
		}
	}

	/**
	 * Merge the resource bundle entries with the resource manager entries.
	 * <hr>
	 * @param filename Resource bundle file to register.
	 * @param root Root path to access the keys.
	 */
	private static final void mergeEntries(final @NonNull String filename, final @NonNull String root)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(filename, ResourceBundleManager.locale);

		OTHER.put(filename, root);

		String key;
		Enumeration<String> enumeration = bundle.getKeys();
		while (enumeration.hasMoreElements())
		{
			key = enumeration.nextElement();
			ENTRIES.put(key, bundle.getString(key));
		}
	}

	/**
	 * Registers a resource bundle and its associated annotated class with the {@link Bundle} annotation.
	 * <p>
	 * @param annotatedClass Annotated class to register.
	 * @param bundle Resource bundle to register.
	 * @throws ResourceBundleException Thrown if an error occurred while trying
	 * to register a resource bundle.
	 */
	@SuppressWarnings("nls")
	private static final void register(final @NonNull Class<?> annotatedClass, final @NonNull ResourceBundle bundle)
	{
		List<ResourceBundle> bundles = new ArrayList<>();
		Bundle annotation = null;

		// Extract the Bundle annotation.
		annotation = annotatedClass.getAnnotation(Bundle.class);
		if (annotation == null)
		{
			throw new ResourceBundleException(String.format("Cannot find @Bundle annotation for class %s", annotatedClass.getName()));
		}

		if (checkAlreadyRegistered(annotatedClass))
		{
			// Ensure we do not register the same annotated class twice.
			log.warn(String.format("Annotated class: '%s' already registered with bundle: '%s'", annotatedClass.getName(), annotation.file()));

			return;
		}

		// Ensure we do not register the same resource bundle twice.
		Optional<Class<?>> optionalClass = checkAlreadyRegistered(annotation);
		if (optionalClass.isPresent())
		{
			log.warn(String.format("Resource bundle: '%s' ignored because already registered", annotation.file()));

			return;
		}

		// No similar annotated class already registered, so let's register it.
		bundles.add(bundle);
		CLASSES.putIfAbsent(annotatedClass, bundles);

		mergeEntries(annotation.file(), annotation.root());
		if (!bundle.getLocale().getISO3Language().equals(ResourceBundleManager.locale.getISO3Language()))
		{
			log.info(String.format("Resource bundle: '%s' registered for language: '%s' due to LENIENT mode", annotation.file(), bundle.getLocale()));
		}
		else
		{
			log.info(String.format("Resource bundle: '%s' registered for language: '%s'", annotation.file(), bundle.getLocale()));
		}
	}

	/**
	 * Checks if the given resource bundle file contained in the given annotation is already registered
	 * through another annotated class.
	 * <hr>
	 * @param annotatedClass Class annotated with the {@link Bundle} annotation.
	 * @return {@code True} if the given annotated class is already registered, {@code false} otherwise.
	 */
	private static final boolean checkAlreadyRegistered(final @NonNull Class<?> annotatedClass)
	{
		return (CLASSES.keySet()
				.stream()
				.filter(e -> e == annotatedClass)
				.findFirst()).isPresent() ? true : false;
	}

	/**
	 * Checks if the given resource bundle file contained in the given annotation is already registered
	 * through another annotated class.
	 * <hr>
	 * @param annotation {@link Bundle} annotation containing the resource bundle file to check.
	 * @return {@link Optional} containing the other annotated class in case the resource bundle file is already registered.
	 */
	private static final Optional<Class<?>> checkAlreadyRegistered(final @NonNull Bundle annotation)
	{
		String name = annotation.file();

		return CLASSES.keySet().stream()
				.filter(e -> e.getAnnotation(Bundle.class).file().equals(name))
				.findAny();
	}

	/**
	 * Retrieves a resource bundle value given its key.
	 * <p>
	 * @param key Resource bundle key.
	 * @return Resource bundle value.
	 */
	@SuppressWarnings("nls")
	private static final String retrieve(final @NonNull String key)
	{
		for (String filename : OTHER.keySet())
		{
			// Try first without the root.
			if (ENTRIES.containsKey(key))
			{
				return ENTRIES.get(key);
			}

			// Try using with the root path.
			if (ENTRIES.containsKey(OTHER.get(filename) + ResourceBundleManager.CHARACTER_DOT + key))
			{
				return ENTRIES.get(OTHER.get(filename) + ResourceBundleManager.CHARACTER_DOT + key);
			}
		}

		String message = String.format("Cannot find key: %s", key);
		log.error(message);
		throw new ResourceBundleException(message);
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
		String theKey = ((IBundle) key).getKey();

		if (!isInitialized)
		{
			initialize();
		}

		Optional<Class<?>> optionalClass = CLASSES.keySet()
				.stream().filter(e -> bundleClass == e).findFirst();

		if (optionalClass.isPresent())
		{
			Optional<ResourceBundle> optionalBundle = CLASSES.get(optionalClass.get())
					.stream().filter(e -> e.containsKey(theKey)).findFirst();

			if (optionalBundle.isPresent())
			{
				return MessageFormat.format(optionalBundle.get().getString(theKey), parameters);
			}
		}

		String message = String.format("Cannot find resource bundle key: '%s' in annotated class: '%s'", theKey, bundleClass.getName());
		log.error(message);

		throw new ResourceBundleException(message);
	}

	/**
	 * Sets the default locale (for fall-back scenarios).
	 * <p>
	 * @param locale {@link Locale} corresponding to the new language to set.
	 */
	public static final void setDefaultLocale(final @NonNull Locale locale)
	{
		ResourceBundleManager.defaultLocale = locale;
	}

	/**
	 * Sets the language used by the {@link ResourceBundleManager} and also sets the
	 * JVM default language.
	 * <p>
	 * <b>Note:</b> Calling this service forces the
	 * {@link ResourceBundleManager} to reload all the resource bundle files
	 * according to the new locale.
	 * <p>
	 * @param locale {@link Locale} corresponding to the new language to set.
	 */
	@SuppressWarnings("nls")
	public static final void setLocale(final @NonNull Locale locale)
	{
		if (!isInitialized && !isInitializing)
		{
			initialize();
		}

		if (!locale.getLanguage().equals(ResourceBundleManager.locale.getLanguage()))
		{
			log.info(String.format("Resource bundle manager language set to: '%s', previous was: '%s'", locale, ResourceBundleManager.locale));

			refresh(locale);
		}
	}

	/**
	 * Refresh all the resource bundles already handled by the
	 * {@link ResourceBundleManager} for the given {@link Locale}.
	 * <p>
	 * @param locale {@link Locale} corresponding to the new language to use.
	 */
	@SuppressWarnings({ "hiding", "nls" })
	private static final void refresh(final @NonNull Locale locale)
	{
		Locale.setDefault(locale);
		ResourceBundleManager.locale = locale;

		log.info(String.format("Refreshing resource bundle files for language: '%s'...", ResourceBundleManager.locale));

		// We need to reload everything as the language has changed.
		ENTRIES.clear();
		CLASSES.clear();

		// Re-launch the auto registration of all annotated classes.
		autoRegisterAnnotated();

		// Register back all bundles registered directly (without annotated class).
		autoRegisterNotAnnotated();

		log.info("Finished refreshing resource bundle files");
	}

	/**
	 * Automatically register all resource bundles not declared through an annotated class.
	 */
	private static final void autoRegisterNotAnnotated()
	{
		OTHER.keySet().stream().forEach(e -> ResourceBundleManager.register(e, OTHER.get(e)));
	}

	/**
	 * Returns a copy of the locale used by the {@link ResourceBundleManager}.
	 * <p>
	 * @return {@link Locale}
	 */
	public static final Locale getLocale()
	{
		// This service does not auto initialize the manager.
		return locale;
	}

	/**
	 * Returns the resource bundle value matching the given service called using an enumerated value.
	 * <hr>
	 * @param annotatedClass Enumeration class.
	 * @param enumerated Enumerated value.
	 * @return Resource bundle value.
	 */
	public static final String getBundleValue(final @NonNull Class<? extends Enum<?>> annotatedClass, final @NonNull Enum<?> enumerated)
	{
		initialize();

		return getBundleValue(annotatedClass, enumerated, ResourceBundleManager.locale);
	}

	/**
	 * Returns the resource bundle value matching the given service called using an enumerated value.
	 * <hr>
	 * @param annotatedClass Enumeration class.
	 * @param enumerated Enumerated value.
	 * @param locale Locale to use.
	 * @return Resource bundle value.
	 */
	@SuppressWarnings({ "nls", "hiding" })
	public static final String getBundleValue(final @NonNull Class<? extends Enum<?>> annotatedClass, final @NonNull Enum<?> enumerated, final @NonNull Locale locale)
	{
		initialize();

		String message = null;

		Bundle annotationClass = annotatedClass.getAnnotation(Bundle.class);
		if (annotationClass == null)
		{
			message = String.format("The class: '%s' must be annotated with the @Bundle annotation!", annotatedClass.getName());
			log.error(message);
			throw new ResourceException(message);
		}

		Optional<String> methodName = findDeclaringMethod(annotatedClass);
		if (methodName.isPresent())
		{
			for (Method method : annotatedClass.getMethods())
			{
				BundleMethod annotationMethod = method.getAnnotation(BundleMethod.class);
				if (annotationMethod != null && method.getName().equals(methodName.get()))
				{
					String key = composeKeyForMethod(annotationClass, annotationMethod, enumerated);
					return ENTRIES.get(key);
				}
			}
		}

		message = String.format("Cannot find a suitable method annotated with the @BundleMethod annotation in class: %s", annotatedClass.getName());
		log.error(message);
		throw new ResourceBundleException(message);
	}

	/**
	 * Find the method name of a given class being the caller.
	 * <hr>
	 * @param annotatedClass Class to lookup for the method.
	 * @return Method name being the caller if found, otherwise {@code null}.
	 */
	private static final Optional<String> findDeclaringMethod(final @NonNull Class<? extends Enum<?>> annotatedClass)
	{
		int index = 1;
		String className;
		Optional<String> methodName = null;
		boolean found = false;

		while (!found)
		{
			className = Thread.currentThread().getStackTrace()[index].getClassName();
			if (className.equals(annotatedClass.getName()))
			{
				methodName = Optional.of(Thread.currentThread().getStackTrace()[index].getMethodName());
				found = true;
			}
			else
			{
				index++;
			}
		}

		return methodName;
	}

	/**
	 * Compose the full key of a resource bundle entry based on properties stored in annotations of the class and the method.
	 * <hr>
	 * @param annotationClass Class annotated with the {@link Bundle} annotation.
	 * @param annotationMethod Method annotated with the {@link BundleMethod} annotation.
	 * @param enumerated Enumerated value.
	 * @return The resource bundle entry full key.
	 */
	private static final String composeKeyForMethod(final @NonNull Bundle annotationClass, final @NonNull BundleMethod annotationMethod, final @NonNull Enum<?> enumerated)
	{
		String first = annotationClass.root().endsWith(CHARACTER_DOT) ? annotationClass.root() : annotationClass.root() + CHARACTER_DOT;
		String second = annotationMethod.key().endsWith(CHARACTER_DOT) ? annotationMethod.key() : annotationMethod.key() + CHARACTER_DOT;

		return second.startsWith(first) == false ? first + second + enumerated.name() : second + enumerated.name();
	}
}
