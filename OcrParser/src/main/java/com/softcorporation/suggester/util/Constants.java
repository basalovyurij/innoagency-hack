/**
 * Copyright (c) 2005 SoftCorporation LLC. All rights reserved.
 *
 * The Software License, Version 1.0
 *
 * SoftCorporation LLC. grants you ("Licensee") a non-exclusive, royalty free,
 * license to use, modify and redistribute this software in source and binary
 * code form, provided that the following conditions are met:
 *
 * 1. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        SoftCorporation LLC. (http://www.softcorporation.com)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 2. The names "Suggester" and "SoftCorporation" must not be used to
 *    promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@softcorporation.com.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 * IN NO EVENT SHALL THE SOFTCORPORATION BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION).
 *
 */
package com.softcorporation.suggester.util;

/**
 * The Constants.
 */
public interface Constants
{
  /**
   * Software name
   */
  public static final String SOFTWARE = "Suggester. Copyright(c) SoftCorporation LLC. All Rights Reserved.";

  /**
   * Software version
   */
  public static final String VERSION = "1.1.2";

  /**
   * Fuzzy configuration directory
   */
  public static final String DIR_CONFIG_FUZZY = "/com/softcorporation/suggester/language/";

  /**
   * Document type text
   */
  public static final int DOC_TYPE_TEXT = 0;

  /**
   * Document type xml
   */
  public static final int DOC_TYPE_XML = 1;

  /**
   * Document type html
   */
  public static final int DOC_TYPE_HTML = 2;

  /**
   * Date format
   */
  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss z"; // ISO 8601 compatible

  /**
   * Max word length
   */
  public static final int WORD_LENGTH_MAX = 256;

  /**
   * Character set default encoding
   */
  public static final String CHARACTER_SET_ENCODING_DEFAULT = "UTF-8";

  /**
   * Language code - default is English
   */
  public static final String LANG_CODE_DEFAULT = "en";

  /**
   * Default edit distance
   */
  public static final int ED_DEFAULT = 2;

  /**
   * Maximum edit distance for soundex code
   */
  public static final int ED_SD_MAX = 4;

  /**
   * Result ID - Match not found
   */
  public static final int RESULT_ID_NO_MATCH = -1;

  /**
   * Result ID - Error
   */
  public static final int RESULT_ID_UNDEFINED = 0;

  /**
   * Result ID - Match found, no no case match
   */
  public static final int RESULT_ID_MATCH = 1;

  /**
   * Result ID - Exach match found
   */
  public static final int RESULT_ID_MATCH_EXACT = 2;

}
