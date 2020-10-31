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
 * The Configuration.
 */
public class SpellCheckConfiguration extends AdvancedSuggesterConfiguration
{
  /*
   * Default Constructor
   */
  public SpellCheckConfiguration()
  {
  }

  /*
   * Constructor with properties file name
   *
   * @param fileName the file name of the configuration properties file.
   */
  public SpellCheckConfiguration(String fileName)
  {
    load(fileName);
    initialize();
  }

  public void initialize()
  {
//		WEIGHT_EDIT_DISTANCE = getProperty("WEIGHT_EDIT_DISTANCE", WEIGHT_EDIT_DISTANCE);
  }

  /**
   * Ignore mixed case words
   */
  public boolean IGNORE_MIXED_CASE = true;

  /**
   * Ignore upper case words
   */
  public boolean IGNORE_UPPER_CASE = false;

  /**
   * Ignore upper case words
   */
  public boolean IGNORE_DIGIT_WORDS = true;

  /**
   * Ignore upper case words
   */
  public boolean IGNORE_INET_WORDS = true;

  /**
   * Ignore upper case words
   */
  public boolean CAPITALIZE_FIRST_LETTER = true;

  /**
   * Ignore upper case words
   */
  public boolean IGNORE_NON_WORDS = true;

}
