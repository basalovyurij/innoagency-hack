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
package com.softcorporation.suggester;

import java.util.*;

import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SuggesterException;
import com.softcorporation.suggester.engine.core.Dictionary;

/**
 * 
 * @version: $Revision: 1.0 $
 * @author    Vadim Permakoff
 */
public abstract class Suggester
{
  abstract public boolean attach(Dictionary dictionary) throws
      SuggesterException;

  abstract public boolean detach(Dictionary dictionary);

  public ArrayList getSuggestions(String word) throws SuggesterException
  {
    return getSuggestions(word, 0);
  }

  public ArrayList getSuggestions(String word, int limit) throws
      SuggesterException
  {
    return getSuggestions(word, limit, null);
  }

  abstract public ArrayList getSuggestions(String word, int limit,
      String langCode) throws SuggesterException;

  abstract public boolean hasExactWord(String word) throws SuggesterException;

  abstract public int hasWord(String word) throws SuggesterException;

  abstract protected TreeSet sortSuggestions(String word, ArrayList suggestions,
                                      String langCode);

  protected TreeSet sortSuggestions(String word, ArrayList suggestions)
  {
    return sortSuggestions(word, suggestions, Constants.LANG_CODE_DEFAULT);
  }

}
