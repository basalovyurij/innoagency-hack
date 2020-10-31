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
package com.softcorporation.suggester.dictionary;

import com.softcorporation.suggester.engine.core.Dictionary;
import com.softcorporation.suggester.util.SuggesterException;

public class BasicDictionary extends Dictionary
{
  /**
   * Dictionary type
   */
  public static final String TYPE = "basic";

  public BasicDictionary(String fileName) throws SuggesterException
  {
    load(fileName);
  }

  public String getDictionaryType()
  {
    return TYPE;
  }

  /**
   * Load dictionary from file or resource
   */
  public synchronized void load(String fileName, String dictName) throws
      SuggesterException
  {
    super.load(fileName, dictName);
  }

  /**
   * Load dictionary from file or resource
   */
  public void load(String fileName) throws SuggesterException
  {
    super.load(fileName);
  }

  public boolean contains(String word)
  {
    return super.contains(word);
  }

}
