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
package com.hemajoo.foundation.common.resource.bundle.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("nls")
public final class RuntimeAnnotations
{
	/*
	 * This class has been imported from: https://gist.github.com/henrrich/185503f10cbb2499a0dc75ec4c29c8f2
	 */
	private static final Constructor<?> AnnotationInvocationHandler_constructor;
	private static final Constructor<?> AnnotationData_constructor;
	private static final Method Class_annotationData;
	private static final Field Class_classRedefinedCount;
	private static final Field AnnotationData_annotations;
	private static final Field AnnotationData_declaredAnotations;
	private static final Method Atomic_casAnnotationData;
	private static final Class<?> Atomic_class;

	static{
		// static initialization of necessary reflection Objects
		try {
			Class<?> AnnotationInvocationHandler_class = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
			AnnotationInvocationHandler_constructor = AnnotationInvocationHandler_class.getDeclaredConstructor(new Class[]{Class.class, Map.class});
			AnnotationInvocationHandler_constructor.setAccessible(true);

			Atomic_class = Class.forName("java.lang.Class$Atomic");
			Class<?> AnnotationData_class = Class.forName("java.lang.Class$AnnotationData");

			AnnotationData_constructor = AnnotationData_class.getDeclaredConstructor(new Class[]{Map.class, Map.class, int.class});
			AnnotationData_constructor.setAccessible(true);
			Class_annotationData = Class.class.getDeclaredMethod("annotationData");
			Class_annotationData.setAccessible(true);

			Class_classRedefinedCount= Class.class.getDeclaredField("classRedefinedCount");
			Class_classRedefinedCount.setAccessible(true);

			AnnotationData_annotations = AnnotationData_class.getDeclaredField("annotations");
			AnnotationData_annotations.setAccessible(true);
			AnnotationData_declaredAnotations = AnnotationData_class.getDeclaredField("declaredAnnotations");
			AnnotationData_declaredAnotations.setAccessible(true);

			Atomic_casAnnotationData = Atomic_class.getDeclaredMethod("casAnnotationData", Class.class, AnnotationData_class, AnnotationData_class);
			Atomic_casAnnotationData.setAccessible(true);

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Private constructor to prevent instantiation using the public default one!
	 */
	private RuntimeAnnotations() {
		// Empty.
	}

	/**
	 * Put a set of annotation properties to an annotated class.
	 * <hr>
	 * @param c Class
	 * @param annotationClass Annotation class.
	 * @param valuesMap Collection of properties of the annotation.
	 */
	public static <T extends Annotation> void putAnnotation(Class<?> c, Class<T> annotationClass, Map<String, Object> valuesMap){
		putAnnotation(c, annotationClass, annotationForMap(annotationClass, valuesMap));
	}

	/**
	 * Put an annotation of a given annotated class.
	 * <hr>
	 * @param c Class.
	 * @param annotationClass Annotation class.
	 * @param annotation Annotation.
	 */
	@SuppressWarnings("boxing")
	public static <T extends Annotation> void putAnnotation(Class<?> c, Class<T> annotationClass, T annotation){
		try {
			while (true) { // retry loop
				int classRedefinedCount = Class_classRedefinedCount.getInt(c);
				Object /*AnnotationData*/ annotationData = Class_annotationData.invoke(c);
				// null or stale annotationData -> optimistically create new instance
				Object newAnnotationData = createAnnotationData(c, annotationData, annotationClass, annotation, classRedefinedCount);
				// try to install it
				if ((boolean) Atomic_casAnnotationData.invoke(Atomic_class, c, annotationData, newAnnotationData)) {
					// successfully installed new AnnotationData
					break;
				}
			}
		} catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException | InstantiationException e){
			throw new IllegalStateException(e);
		}

	}

	/**
	 * Create annotation data.
	 * @param c Class.
	 * @param annotationData Annotation data structure.
	 * @param annotationClass Annotation class.
	 * @param annotation Annotation.
	 * @param classRedefinedCount Counter.
	 * @return Object representing the newly created annotation data.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Annotation> Object /*AnnotationData*/ createAnnotationData(Class<?> c, Object /*AnnotationData*/ annotationData, Class<T> annotationClass, T annotation, int classRedefinedCount) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) AnnotationData_annotations.get(annotationData);
		Map<Class<? extends Annotation>, Annotation> declaredAnnotations= (Map<Class<? extends Annotation>, Annotation>) AnnotationData_declaredAnotations.get(annotationData);

		Map<Class<? extends Annotation>, Annotation> newDeclaredAnnotations = new LinkedHashMap<>(annotations);
		newDeclaredAnnotations.put(annotationClass, annotation);
		Map<Class<? extends Annotation>, Annotation> newAnnotations ;
		if (declaredAnnotations == annotations) {
			newAnnotations = newDeclaredAnnotations;
		} else{
			newAnnotations = new LinkedHashMap<>(annotations);
			newAnnotations.put(annotationClass, annotation);
		}
		return AnnotationData_constructor.newInstance(newAnnotations, newDeclaredAnnotations, classRedefinedCount);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T annotationForMap(final Class<T> annotationClass, final Map<String, Object> valuesMap){
		return (T)AccessController.doPrivileged((PrivilegedAction<Annotation>) () ->
		{
			InvocationHandler handler;
			try {
				handler = (InvocationHandler) AnnotationInvocationHandler_constructor.newInstance(annotationClass,new HashMap<>(valuesMap));
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalStateException(e);
			}
			return (Annotation)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[] { annotationClass }, handler);
		});
	}
}
