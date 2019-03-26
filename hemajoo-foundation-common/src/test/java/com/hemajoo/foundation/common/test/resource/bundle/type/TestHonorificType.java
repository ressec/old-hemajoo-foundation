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
package com.hemajoo.foundation.common.test.resource.bundle.type;

import com.hemajoo.foundation.common.IHemajoo;
import com.hemajoo.foundation.common.resource.bundle.ResourceBundleManager;
import com.hemajoo.foundation.common.resource.bundle.annotation.Bundle;
import com.hemajoo.foundation.common.resource.bundle.annotation.BundleMethod;
import com.neovisionaries.i18n.LanguageCode;

/**
 * Test enumeration for the honorific titles a physical person can have.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Hemajoo</a>
 * @version 1.0.0
 */
@Bundle(file="bundle/type.honorific-title", root="enum.honorific")
public enum TestHonorificType implements IHemajoo
{
	/**
	 * For men, regardless of marital status.
	 */
	MR,

	/**
	 * For girls and young women who are usually unmarried.
	 */
	MISS,

	/**
	 * For women, regardless of marital status.
	 */
	MS,

	/**
	 * For married women.
	 */
	MRS,

	/**
	 * For gender neutral or for general use as a gender neutral title.
	 */
	MX,

	/**
	 * For men, formally if they have a British knighthood or if they are a baronet,
	 * or generally as a term of general respect or flattery. Equivalent to the opposite
	 * meaning of "Madam".
	 */
	SIR,

	/**
	 * For women, a term of general respect or flattery. Equivalent to "Sir".
	 */
	MADAM,

	/**
	 * For women who have been honored with a British knighthood in their own right.
	 * Women married to knighted individuals, but not knighted in their own right,
	 * are commonly referred to as "Lady".
	 */
	DAME,

	/**
	 * For male barons, viscounts, earls, and marquesses, as well as some of their children.
	 * (Style: Lordship or My Lord).
	 */
	LORD,

	/**
	 * For female peers with the rank of baroness, viscountess, countess, and marchioness,
	 * or the wives of men who hold the equivalent titles. (Style: Your Ladyship or My Lady)
	 */
	LADY,

	/**
	 * Advocate for notable lawyers and jurists, used in Scotland, South Africa and other countries.
	 */
	ADV,

	/**
	 * Doctor for a person who has an academic research degree.
	 */
	DR,

	/**
	 * Professor for a person in a Commonwealth country who holds the academic rank of professor
	 * in a university.
	 */
	PROF,

	/**
	 * Brother for men generally in some religious organizations.
	 */
	BR,

	/**
	 * Sister Nun or other religious sister in the Catholic Church.
	 */
	SR,

	/**
	 * Father for priests in Catholic and Eastern Christianity,
	 * as well as some Anglican or Episcopalian groups; Generally equivalent to 'Reverend'.
	 */
	FR,

	/**
	 * Reverend used generally for members of the Christian clergy regardless of affiliation,
	 * but especially in Protestant denominations. Equivalent to 'Father'.
	 */
	REV,

	/**
	 * Pastor used generally for members of the Christian clergy regardless of affiliation,
	 * but especially in Protestant denominations. Equivalent to 'Reverend'.
	 */
	PR;

	/**
	 * Returns the short honorific title in the current language.
	 * <hr>
	 * @return Short honorific title.
	 */
	@BundleMethod(key="short")
	public final String getShortTitle()
	{
		return ResourceBundleManager.getBundleValue(this.getClass(), this);
	}

	/**
	 * Returns the short honorific title in the given language.
	 * <hr>
	 * @param language {@link LanguageCode} to be used.
	 * @return Short honorific title.
	 */
	@BundleMethod(key="short")
	public final String getShortTitle(final LanguageCode language)
	{
		return ResourceBundleManager.getBundleValue(this.getClass(), this, language.toLocale());
	}

	/**
	 * Returns the long honorific title in the current language.
	 * <hr>
	 * @return Long honorific title.
	 */
	@BundleMethod(key="enum.honorific.long")
	public final String getLongTitle()
	{
		return ResourceBundleManager.getBundleValue(this.getClass(), this);
	}

	/**
	 * Returns the long honorific title in the given language.
	 * <hr>
	 * @param language {@link LanguageCode} to be used.
	 * @return Long honorific title.
	 */
	@BundleMethod(key="enum.honorific.long")
	public final String getLongTitle(final LanguageCode language)
	{
		return ResourceBundleManager.getBundleValue(this.getClass(), this, language.toLocale());
	}

	/**
	 * Returns the honorific title help in the current language.
	 * <hr>
	 * @return Honorific title help.
	 */
	@BundleMethod(key="enum.honorific.help")
	public final String getHelpTitle()
	{
		return ResourceBundleManager.getBundleValue(this.getClass(), this);
	}

	/**
	 * Returns the honorific title help in the given language.
	 * <hr>
	 * @param language {@link LanguageCode} to be used.
	 * @return Honorific title help.
	 */
	@BundleMethod(key="enum.honorific.help")
	public final String getHelpTitle(final LanguageCode language)
	{
		return ResourceBundleManager.getBundleValue(this.getClass(), this, language.toLocale());
	}
}
