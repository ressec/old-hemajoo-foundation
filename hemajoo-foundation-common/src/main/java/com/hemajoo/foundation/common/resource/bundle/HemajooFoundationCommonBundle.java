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

import com.hemajoo.foundation.common.resource.bundle.annotation.Bundle;

/**
 * Enumeration of the resource bundle externalized string of the <b>hemajoo common</b> module.
 * Each enumerated value maps to a key in the corresponding resource bundle file.
 * <p>
 * <b>Note:</b><br>
 * In the Hemajoo's framework, the enumerated values are not in full uppercase.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
@SuppressWarnings("nls")
@Bundle(file="bundle/hemajoo-foundation-common", root="hemajoo-foundation-common.", priority = 10) // This one must be loaded first!
public enum HemajooFoundationCommonBundle implements IBundle
{
	/*
	 * ---------- Test.* ----------
	 */

	/**
	 * A dummy message from component: 'component name'.
	 */
	TEST_DUMMY("test.dummy"),

	/**
	 * A dummy language.
	 */
	TEST_DUMMY_LANGUAGE("test.dummy.language"),

	/*
	 * ---------- Resource.Bundle.* ----------
	 */

	/**
	 * The test resource bundle key that does not exists in the resource bundle file.
	 */
	RESOURCE_BUNDLE_KEYDOESNOTEXIST("resource.bundle.keyDoesNotExist"),

	/**
	 * The given resource bundle cannot be found.
	 */
	RESOURCE_BUNDLE_NOTFOUND("resource.bundle.notFound"),

	/**
	 * An error occurred with the given resource bundle.
	 */
	RESOURCE_BUNDLE_ERROR("resource.bundle.error"),

	/**
	 * The given resource bundle has been registered.
	 */
	RESOURCE_BUNDLE_REGISTERED("resource.bundle.registered"),

	/**
	 * The given resource bundle has been replaced.
	 */
	RESOURCE_BUNDLE_REPLACED("resource.bundle.replaced"),

	/**
	 * The given resource bundle is already registered.
	 */
	RESOURCE_BUNDLE_ALREADYREGISTERED("resource.bundle.alreadyRegistered"),

	/**
	 * The given resource bundle file name cannot be null or empty.
	 */
	RESOURCE_BUNDLE_INVALIDNAME("resource.bundle.invalidName"),

	/**
	 * The given resource bundle key cannot be null or empty.
	 */
	RESOURCE_BUNDLE_INVALIDKEY("resource.bundle.invalidKey"),

	/**
	 * The given resource bundle locale cannot be null.
	 */
	RESOURCE_BUNDLE_INVALIDLOCALE("resource.bundle.invalidLocale"),

	/**
	 * Resource bundle configuration file cannot be null or empty
	 */
	RESOURCE_BUNDLE_INVALIDCONFIGURATION("resource.bundle.invalidConfiguration");

	/**
	 * Resource bundle key.
	 */
	private final String key;

	/**
	 * Creates a new enumerated value based on the resource bundle key.
	 * <p>
	 * @param key Resource bundle key.
	 */
	private HemajooFoundationCommonBundle(final String key)
	{
		this.key = key;
	}

	@Override
	public final String getKey()
	{
		Bundle annotation = this.getClass().getAnnotation(Bundle.class);
		if (annotation != null)
		{
			return annotation.root().endsWith(".") ? annotation.root() + key : annotation.root() + "." + key;
		}

		return key;
	}

	@Override
	public final String getValue()
	{
		return ResourceBundleManager.getMessage(this);
	}
}
