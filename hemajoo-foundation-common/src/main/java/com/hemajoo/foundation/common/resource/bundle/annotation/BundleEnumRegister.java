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

import com.hemajoo.foundation.common.resource.bundle.IBundle;
import com.hemajoo.foundation.common.resource.bundle.ResourceBundleManager;

/**
 * This annotation is used to make a resource bundle enumeration file to be auto
 * registered by the {@link ResourceBundleManager}. This annotation is intended
 * to be placed on enumeration types inheriting the {@link IBundle} interface.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface BundleEnumRegister
{
	/**
	 * Priority value used for loading the resource bundle enumeration by the {@link ResourceBundleManager}.
	 * <p>
	 * Resource bundle enumerations annotated with this annotation and having a low priority value are loaded first ; default priority value is
	 * {@code 100}.
	 * <hr>
	 * @return Priority to load the resource bundle enumeration.
	 */
	int priority() default 100;
}
