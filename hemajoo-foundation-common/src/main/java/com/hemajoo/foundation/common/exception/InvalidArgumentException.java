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

import com.hemajoo.foundation.common.resource.bundle.IBundle;

/**
 * Unchecked exception thrown to indicate a method has been passed an illegal
 * or inappropriate argument.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
public class InvalidArgumentException extends AbstractUncheckedException
{
	/**
	 * Default serialization identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Thrown to indicate that a method has been passed an illegal or
	 * inappropriate argument.
	 */
	public InvalidArgumentException()
	{
		super();
	}

	/**
	 * Thrown to indicate that a method has been passed an illegal or
	 * inappropriate argument.
	 * <p>
	 * @param key Resource bundle key (enumerated value coming from an
	 * enumeration implementing the {@link IBundle} interface).
	 */
	public InvalidArgumentException(final Enum<? extends IBundle> key)
	{
		super(key);
	}

	/**
	 * Thrown to indicate that a method has been passed an illegal or
	 * inappropriate argument.
	 * <p>
	 * @param key Exception key (enumerated value coming from an enumeration
	 * implementing the {@link IExceptionType} interface).
	 * @param parameters List of parameters used to populate the exception
	 * message.
	 */
	public InvalidArgumentException(final Enum<?> key, final Object... parameters)
	{
		super(key, parameters);
	}

	/**
	 * Thrown to indicate that a method has been passed an illegal or
	 * inappropriate argument.
	 * <p>
	 * @param exception Parent {@link Exception}.
	 */
	public InvalidArgumentException(final Exception exception)
	{
		super(exception);
	}

	/**
	 * Thrown to indicate that a method has been passed an illegal or
	 * inappropriate argument.
	 * <p>
	 * @param message Message describing the error being the cause of the raised
	 * exception.
	 */
	public InvalidArgumentException(final String message)
	{
		super(message);
	}

	/**
	 * Thrown to indicate that a method has been passed an illegal or
	 * inappropriate argument.
	 * <p>
	 * @param message Message describing the error being the cause of the raised
	 * exception.
	 * @param exception Parent {@link Exception}.
	 */
	public InvalidArgumentException(final String message, final Exception exception)
	{
		super(message, exception);
	}
}