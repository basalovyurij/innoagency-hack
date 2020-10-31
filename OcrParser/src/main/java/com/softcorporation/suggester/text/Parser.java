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
package com.softcorporation.suggester.text;

import java.util.*;

import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SuggesterException;
import com.softcorporation.suggester.util.SpellCheckConfiguration;

/**
 *
 * @version: $Revision:   1.0  $
 */
public class Parser
{
  protected SpellCheckConfiguration configuration;
  private static TreeMap configurations;

  public Parser(SpellCheckConfiguration spellCheckConfiguration)
  {
    configuration = spellCheckConfiguration;
  }

  public void setConfiguration(SpellCheckConfiguration spellCheckConfiguration)
  {
    configuration = spellCheckConfiguration;
  }

  public static Parser getParser(SpellCheckConfiguration spellCheckConfiguration)
  {
    Parser parser = (Parser) configurations.get(spellCheckConfiguration);
    if (parser == null)
    {
      parser = new Parser(spellCheckConfiguration);
    }
    return parser;
  }

  public String getText(int startEntry, int endEntry)
  {
    return "";
  }

//    Parser parser = Parser.getParser(configuration);
//    Document document = parser.parse(text);

  public Document parse(String text, int textType, String language) throws SuggesterException
  {
    Document document = null;
    if (textType == Constants.DOC_TYPE_TEXT)
    {
      document = new TextDocument(text, language, configuration);
    }
    else if (textType == Constants.DOC_TYPE_XML)
    {
      document = new XmlDocument(text, language, configuration);
    }
    else if (textType == Constants.DOC_TYPE_HTML)
    {
      document = new HtmlDocument(text, language, configuration);
    }
    else
    {
      throw new SuggesterException("Unknown document type: " + textType);
    }
    return document;
  }

}
