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
package com.hemajoo.foundation.common.resource.bundle.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hemajoo.foundation.common.resource.bundle.ResourceBundleManager;

/**
 * This annotation is used to declare a resource bundle file and make it automatically
 * registered by the {@link ResourceBundleManager}.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bundle
{
	/**
	 * Relative path name of the resource bundle file without any extension (language and
	 * file extension must be omitted).
	 * <br>
	 * Folders are separated by a '/' (slash) character such as in the following example:
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * If the resource bundles are located in the 'src/main/resources/bundle/type/resource-bundle-file_**.properties'
	 * resource files then the 'file' property will have the value:
	 * <p>
	 * <code>file="bundle/type/resource-bundle-file"</code>
	 * <hr>
	 * @return Resource bundle file name.
	 */
	String file();

	/**
	 * Root path for the resource keys.
	 * <p>
	 * If the key to access is defined in the properties as: {@code path.to.the.resource.string.key.foo.bar.key}
	 * then the root should have the value: {@code path.to.the.resource.string.key} and the key will have the
	 * value {@code foo.bar.key}.
	 * <hr>
	 * @return Path of the resource string to use.
	 */
	String root();

	/**
	 * Priority value used for loading the resource bundle file by the {@link ResourceBundleManager}.
	 * <br>
	 * Resource bundle declared with the lowest priority value are loaded first ; default priority value is
	 * {@code 100}.
	 * <hr>
	 * @return Priority to load the resource bundle file.
	 */
	int priority() default 100;
}
