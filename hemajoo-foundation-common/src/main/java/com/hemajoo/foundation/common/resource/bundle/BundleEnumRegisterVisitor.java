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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.hemajoo.foundation.common.annotation.IAnnotationVisitor;
import com.hemajoo.foundation.common.resource.bundle.annotation.BundleEnumRegister;

import lombok.extern.log4j.Log4j;

/**
 * Class used to visit all annotated classes available on the classpath that are
 * annotated with the {@link BundleEnumRegister} annotation.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Hemajoo</a>
 * @version 1.0.0
 */
@Log4j
public final class BundleEnumRegisterVisitor implements IAnnotationVisitor
{
	/**
	 * Collection of bundles to auto-register (by priority).
	 */
	private Map<Integer, List<String>> bundles = new TreeMap<>();

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Annotation>[] annotations()
	{
		return new Class[] { BundleEnumRegister.class };
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className)
	{
		List<String> classes = null;
		Integer key = null;

		Class<? extends IBundle> bClass;
		try
		{
			bClass = (Class<? extends IBundle>) Class.forName(className);
			BundleEnumRegister a = bClass.getAnnotation(BundleEnumRegister.class);
			key = Integer.valueOf(a.priority());
			classes = (List<String>) (bundles.containsKey(key) ? bundles.get(key) : new ArrayList<>());
			classes.add(className);
			bundles.put(Integer.valueOf(a.priority()), classes);
		}
		catch (ClassNotFoundException e)
		{
			log.error(e.getMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void delegateRegistration() throws ClassNotFoundException
	{
		for (Integer i : bundles.keySet())
		{
			for (String e : bundles.get(i))
			{
				ResourceBundleManager.register((Class<? extends IBundle>) Class.forName(e));
			}
		}
	}
}
