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
package com.hemajoo.foundation.common.exception;

import java.text.MessageFormat;

import com.hemajoo.foundation.common.resource.bundle.IBundle;
import com.hemajoo.foundation.common.resource.bundle.ResourceBundleManager;


/**
 * An abstract implementation of an unchecked exception.
 * <p>
 * Unchecked runtime exceptions represent conditions that, generally speaking, reflect errors in program's logic and cannot be reasonably recovered
 * from at run time. Unchecked exceptions are subclasses of {@link RuntimeException}, and are usually implemented using
 * {@link InvalidArgumentException}, {@link NullPointerException}, or {@link IllegalStateException} a method is not obliged to establish a policy for
 * the unchecked exceptions thrown by its implementation (and they almost always do not do so).
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
public abstract class AbstractUncheckedException extends RuntimeException
{
	/**
	 * Default serialization identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Exception key.
	 */
	protected Enum<? extends IExceptionType> key;

	/**
	 * Creates a new unchecked empty exception.
	 */
	public AbstractUncheckedException()
	{
		super();
	}

	/**
	 * Creates a new unchecked exception based on an exception key.
	 * <p>
	 * @param key Resource bundle key (enumerated value coming from an enumeration implementing the {@link IBundle} interface).
	 */
	public AbstractUncheckedException(final Enum<? extends IBundle> key)
	{
		super(ResourceBundleManager.getMessage(key));
	}

	/**
	 * Creates a new unchecked exception based on an enumerated value and given parameters.
	 * <p>
	 * @param key Exception key (enumerated value coming from an enumeration implementing the {@link IExceptionType} interface).
	 * @param parameters List of parameters used to populate the exception message.
	 */
	@SuppressWarnings({ "unchecked" })
	public AbstractUncheckedException(final Enum<?> key, final Object... parameters)
	{
		super(key instanceof IBundle ? MessageFormat.format(ResourceBundleManager.getMessage((Enum<? extends IBundle>) key), parameters) : MessageFormat.format(((IExceptionType) key).getMessage(), parameters));

		// Do we have an exception in the parameter list?
		for (Object o : parameters)
		{
			if (o instanceof AbstractUncheckedException) // Do not override the original error code!
			{
				this.key = ((AbstractUncheckedException) o).getKey();
				return;
			}
		}

		if (key instanceof IExceptionType)
		{
			this.key = (Enum<? extends IExceptionType>) key;
		}
	}

	/**
	 * Creates a new unchecked exception based on a parent exception.
	 * <p>
	 * @param exception Parent exception.
	 */
	public AbstractUncheckedException(final Exception exception)
	{
		super(exception);
	}

	/**
	 * Creates a new unchecked exception based on a message.
	 * <p>
	 * @param message Message of the exception.
	 */
	public AbstractUncheckedException(final String message)
	{
		super(message);
	}

	/**
	 * Creates a new unchecked exception based on a message and a parent exception.
	 * <p>
	 * @param message Message of the exception.
	 * @param exception Parent exception.
	 */
	public AbstractUncheckedException(final String message, final Exception exception)
	{
		super(message, exception);
	}

	/**
	 * Returns the exception key.
	 * <p>
	 * @return Exception key.
	 */
	public final Enum<? extends IExceptionType> getKey()
	{
		return key;
	}

	/**
	 * Returns the exception error class.
	 * <p>
	 * @return Exception error class.
	 */
	public final Class<? extends IExceptionType> getErrorClass()
	{
		return key.getDeclaringClass();
	}

	@Override
	protected final Object clone() throws CloneNotSupportedException
	{
		return new CloneNotSupportedException();
	}
}