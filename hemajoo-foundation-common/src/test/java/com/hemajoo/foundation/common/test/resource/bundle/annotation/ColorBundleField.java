/*
 * Copyright(c) 2018 Hemajoo Ltd.
 * ---------------------------------------------------------------------------
 * This file is part of the Hemajoo's Foundation Software (HFS) project which
 * is licensed under the Apache license version 2 and use is subject to license
 * terms. You should have received a copy of the license with the project's
 * artifact binaries and/or sources.
 * 
 * License can be consulted at http://www.apache.org/licenses/LICENSE-2.0
 * ---------------------------------------------------------------------------
 */
package com.hemajoo.foundation.common.test.resource.bundle.annotation;

import org.jeasy.props.annotations.I18NProperty;

import com.hemajoo.foundation.common.resource.bundle.annotation.Bundle;

import lombok.Getter;
import lombok.Setter;

/**
 * Test class for the {@link I18NProperty} annotation from the {@code easy-props} library.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Hemajoo</a>
 * @version 1.0.0
 */
@Bundle(file="bundle/color/color", root="color")
public class ColorBundleField
{
	/**
	 * Color code.
	 * <p>
	 * Directly linked to a resource bundle file entry key.
	 */
	@Getter
	@Setter
	@I18NProperty(bundle = "bundle/color/color", key = "color.yellow.name")
	private String colorCode;
}
