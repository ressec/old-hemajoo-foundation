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
package com.hemajoo.foundation.common.resource;

import com.hemajoo.foundation.common.exception.AbstractUncheckedException;
import com.hemajoo.foundation.common.exception.IExceptionType;
import com.hemajoo.foundation.common.resource.bundle.IBundle;

/**
 * Unchecked exception thrown to indicate an error occurred while processing a resource.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
public class ResourceException extends AbstractUncheckedException
{
	/**
	 * Serialization identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Exception thrown to indicate an error occurred while processing a resource.
	 */
	public ResourceException()
	{
		super();
	}

	/**
	 * Exception thrown to indicate an error occurred while processing a resource.
	 * <p>
	 * @param key Resource bundle key (enumerated value coming from an
	 * enumeration implementing the {@link IBundle} interface).
	 */
	public ResourceException(final Enum<? extends IBundle> key)
	{
		super(key);
	}

	/**
	 * Exception thrown to indicate an error occurred while processing a resource.
	 * <p>
	 * @param key Exception key (enumerated value coming from an enumeration
	 * implementing the {@link IExceptionType} interface).
	 * @param parameters List of parameters used to populate the exception
	 * message.
	 */
	public ResourceException(final Enum<?> key, final Object... parameters)
	{
		super(key, parameters);
	}

	/**
	 * Exception thrown to indicate an error occurred while processing a resource.
	 * <p>
	 * @param exception Parent exception.
	 */
	public ResourceException(final Exception exception)
	{
		super(exception);
	}

	/**
	 * Exception thrown to indicate an error occurred while processing a resource.
	 * <p>
	 * @param message Message describing the error being the cause of the raised
	 * exception.
	 */
	public ResourceException(final String message)
	{
		super(message);
	}

	/**
	 * Exception thrown to indicate an error occurred while processing a resource.
	 * <p>
	 * @param message Message describing the error being the cause of the raised
	 * exception.
	 * @param exception Parent exception.
	 */
	public ResourceException(final String message, final Exception exception)
	{
		super(message, exception);
	}
}