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
package com.softcorporation.suggester.tools;

import java.applet.Applet;
import java.awt.*;
import java.util.*;

import com.softcorporation.suggester.dictionary.BasicDictionary;
import com.softcorporation.suggester.util.SpellCheckConfiguration;
import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.BasicSuggester;
import com.softcorporation.suggester.Suggestion;


public class SpellCheckApplet extends Applet
{
  public static boolean initDone;

  private Toolkit toolkit;
  private Frame frame;

  private static String language;
  private static SpellCheck spellCheck;
  private static BasicSuggester suggester;
  private static SpellCheckConfiguration configuration;

  private boolean debug;

  private Color parColor;
  private Graphics appletGraphics;

  int appWidth, appHeight;
  int resultID = -1;

  public SpellCheckApplet()
  {}

  public void init()
  {
    if (!initDone)
    {
      System.out.println(getAppletInfo());
      toolkit = getToolkit();

      String mainDicName = getParameter("dictionary"); // loaded from memory
      if (mainDicName == null)
      {
        mainDicName = "english-1";
      }

      String parName = getParameter("name");
      if (parName == null)
      {
        parName = "Suggester";
      }

      String parLanguage = getParameter("language");
      if (parLanguage != null)
      {
        language = parLanguage;
      }

      String configName = getParameter("config");
      if (configName == null)
      {
        configName = "spellCheck.config";
      }

      String s = getParameter("debug");
      if (s != null && s.toLowerCase().equals("yes"))
      {
        debug = true;
      }
      try
      {
        configuration = new SpellCheckConfiguration(configName);

        BasicDictionary dictionary = new BasicDictionary(mainDicName);
//        System.out.println("Dictionary total words: " +
//                           dictionary.getTotalWords());

//        suggester = new AdvancedSuggester(configuration);
//        suggester.attach(dictionary, 1);
        suggester = new BasicSuggester(configuration);
        suggester.attach(dictionary);
      }
      catch (Exception ex)
      {
        System.out.println("Error loading dictionary. " + ex.getMessage());
      }
    }
    initDone = true; // no init on reload in Netscape, but in IE
  }

  public String getAppletInfo()
  {
    return "Suggester Applet (ver. " + Constants.VERSION + ") Copyright (c) 2005-2006 SoftCorporation LLC.\nFree spell checking software: http://www.softcorporation.com/products/suggester";
  }

  public String getText()
  {
    return spellCheck.getText();
  }

  public int getResultID()
  {
    return resultID;
  }

  public void check(String text, int suggestionsLimit)
  {
    check(text, 0, suggestionsLimit);
  }

  public void check(String text, int offset, int suggestionsLimit)
  {
    spellCheck = new SpellCheck(configuration);
    spellCheck.setSuggester(suggester);
    spellCheck.setSuggestionLimit(suggestionsLimit);
    try
    {
      spellCheck.setText(text, Constants.DOC_TYPE_TEXT, language);
      spellCheck.check(offset);
      setResultID();
    }
    catch (Exception ex)
    {
      resultID = -1;
    }
  }

  public void check()
  {
    try
    {
      spellCheck.check();
      setResultID();
    }
    catch (Exception ex)
    {
      resultID = -1;
    }
  }

  public void checkNext()
  {
    try
    {
      spellCheck.checkNext();
      setResultID();
    }
    catch (Exception ex)
    {
      resultID = -1;
    }
  }

  public void setResultID()
  {
    if (!spellCheck.hasMisspelt())
    {
      resultID = 3; // spell check complete
      return;
    }
    ArrayList suggestions = spellCheck.getSuggestions();
    if (suggestions != null && suggestions.size() > 0)
    {
      resultID = 1; // errors found and have suggestions
    }
    else
    {
      resultID = 2; // errors found but no suggestions
    }
  }

  public String getMisspelt()
  {
    return spellCheck.getMisspelt();
  }

  public int getMisspeltOffset()
  {
    return spellCheck.getMisspeltOffset();
  }

  public int getMisspeltLength()
  {
    return spellCheck.getMisspeltLength();
  }

  public String getMisspeltText(int wordsBefore, String tagOpen,
                                String tagClose, int wordsAfter)
  {
    try
    {
      return spellCheck.getMisspeltText(wordsBefore, tagOpen, tagClose, wordsAfter);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public ArrayList getSuggestions()
  {
    ArrayList suggestions = spellCheck.getSuggestions();
    ArrayList words = new ArrayList();
    for (int i = 0; i < suggestions.size(); i++)
    {
      Suggestion suggestion = (Suggestion) suggestions.get(i);
      words.add(suggestion.getWord());
    }
    return words;
  }

  public void ignore()
  {
    spellCheck.ignore();
  }

  public void ignoreAll()
  {
    spellCheck.ignoreAll();
  }

  public void change(String word)
  {
    spellCheck.change(word);
  }

  public void changeAll(String word)
  {
    spellCheck.changeAll(word);
  }

}
