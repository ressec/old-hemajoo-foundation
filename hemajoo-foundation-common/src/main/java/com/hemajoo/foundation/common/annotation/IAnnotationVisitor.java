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
package com.hemajoo.foundation.common.annotation;

import eu.infomas.annotation.AnnotationDetector.TypeReporter;

/**
 * Extend the {@link TypeReporter} interface to be able to construct by reflection
 * classes implementing the {@link IAnnotationVisitor} interface.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Hemajoo</a>
 * @version 1.0.0
 */
public interface IAnnotationVisitor extends TypeReporter
{
	/**
	 * Delegates registration of annotated classes.
	 * <hr>
	 * @throws ClassNotFoundException Thrown in case an error occurred while trying to
	 * delegate the registration of the annotated class.
	 */
	public void delegateRegistration() throws ClassNotFoundException;
}
