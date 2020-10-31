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

import com.softcorporation.util.Configuration;

/**
 * Basic suggester configuration.
 */
public class BasicSuggesterConfiguration extends Configuration implements
    Constants
{
  /**
   * Weight
   */
  public int WEIGHT_EDIT_DISTANCE = 28;

  /**
   * Weight
   */
  public int WEIGHT_SOUNDEX = 28;

  /**
   * Weight
   */
  public int WEIGHT_LENGTH = 3;

  /**
   * Weight
   */
  public int WEIGHT_LAST_CHAR = 1;

  /**
   * Weight
   */
  public int WEIGHT_FIRST_CHAR = 5;

  /**
   * Weight
   */
  public int WEIGHT_FIRST_CHAR_UPPER = 1;

  /**
   * Weight
   */
  public int WEIGHT_FIRST_CHAR_LOWER = 4;

  /**
   * Weight
   */
  public int WEIGHT_ADD_REM_CHAR = 2;

  /**
   * Weight
   */
  public int WEIGHT_JOINED_WORD = 30;

  /**
   * Weight
   */
  public int WEIGHT_FUZZY = 4;

  /**
   * Allowe to search joined words
   */
  public boolean SEARCH_JOINED = true;

  /**
   * Min joined word length
   */
  public int JOINED_WORD_LENGTH_MIN = 2;

  /**
   * Edited joined word length
   */
  public int JOINED_WORD_LENGTH_EDT = 10;

  /**
   * Minimum word length to use edit distance 1
   */
  public int LENGTH_MIN_ED_1 = 3;

  /**
   * Minimum word length to use edit distance 2
   */
  public int LENGTH_MIN_ED_2 = 6;

  /**
   * Minimum word length to use edit distance 3
   */
  public int LENGTH_MIN_ED_3 = 8;

  /**
   * Minimum word length to use edit distance 4
   */
  public int LENGTH_MIN_ED_4 = 0;

  /**
   * Remove duplicates in different case from suggestion list
   */
  public boolean REMOVE_CASE_DUPLICATES = true;

  /**
   * Remove variations in joined results from suggestion list
   */
  public boolean REMOVE_JOINED_VARIATIONS = true;
  
  /**
   * Remove unrelated suggestions
   */
  public int CLOSE_WORDS_CUT = 0;
  
  /**
   * Symbols to separate words
   */
  public String DELIMITERS = " ,.\n\r\t\f:+!?;{}()[]^<>=/\"��_#$\\|���";

  /**
   * Symbols to separate joined words
   */
  public String DELIMITERS_JOINED = "@%&*-�";

  /*
   * Default constructor
   */
  public BasicSuggesterConfiguration()
  {
    super();
  }

  /*
   * Constructor with properties file name
   *
   * @param fileName the file name of the configuration properties file.
   */
  public BasicSuggesterConfiguration(String fileName)
  {
    super();
    if (fileName != null)
    {
      load(fileName, "UTF-8");
    }
  }

  public void initialize()
  {
  }

  public void save()
  {
  }

}
