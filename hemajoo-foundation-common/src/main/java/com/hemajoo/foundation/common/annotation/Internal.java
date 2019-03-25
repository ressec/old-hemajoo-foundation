/*
 * Copyright(c) 2018 Hemajoo Ltd.
 * ---------------------------------------------------------------------------
 * This file is part of the Hemajoo's Incubation Software (HIS) project which is licensed
 * under the Apache license version 2 and use is subject to license terms.
 * You should have received a copy of the license with the project's artifact
 * binaries and/or sources.
 * 
 * License can be consulted at http://www.apache.org/licenses/LICENSE-2.0
 * ---------------------------------------------------------------------------
 */
package com.hemajoo.foundation.common.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation used to declare a Java type as being reserved by Hemajoo development team.<br>
 * Using such annotated classes by other developer is highly risky.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Hemajoo</a>
 * @version 1.0.0
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Internal
{
	// EMPTY
}
