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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to associate a resource bundle file to a resource bundle enumeration.
 * <p>
 * This annotation is intended to be placed on methods of an enumeration for which you want to
 * get an automatic binding between enumerated values and resource bundle entries in a resource
 * bundle file.
 * <p>
 * For an example on how to use it, take a look at the {@link TestHonorificType} enumeration.
 * This is an enumeration that is getting the string values for each enumerated values
 * from resource bundle files.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BundleEnum
{
	/**
	 * Relative path name of the resource bundle file without extension and language extension.
	 * <p>
	 * Folders must be separated by a '.' (dot) character such as in the following example:
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * If the resource bundles are located in the 'src/main/resources/bundle/type/resource-bundle-file_**.properties'
	 * resource files then the 'file' property will have the value:
	 * <p>
	 * <code>file="bundle.type.resource-bundle-file"</code>
	 * <hr>
	 * @return Resource bundle file name.
	 */
	String file();

	/**
	 * Path of the resource key.
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * <code>path="path.to.the.resource.string.key"</code>
	 * <hr>
	 * @return Path of the resource string to use.
	 */
	String path();
}
