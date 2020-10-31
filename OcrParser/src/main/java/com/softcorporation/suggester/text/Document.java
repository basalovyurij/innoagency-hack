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

import com.softcorporation.util.Configuration;
import com.softcorporation.suggester.util.BasicSuggesterConfiguration;

/**
 *
 * @version: $Revision:   1.0  $
 */
public abstract class Document
{
  public static int docCount;

  int type;
  protected String text;
  protected String language;
  protected int position;
  protected int textLength;
  public ArrayList words;
  public int totalWords;

  protected BasicSuggesterConfiguration configuration;

  public Document(BasicSuggesterConfiguration basicSuggesterConfiguration)
  {
    configuration = basicSuggesterConfiguration;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }

  public void setConfiguration(BasicSuggesterConfiguration
                               basicSuggesterConfiguration)
  {
    configuration = basicSuggesterConfiguration;
  }

  public void replace(String word)
  {
    replace(position, word);

//DEBUG
//    System.out.println("text (Document.replace): " + text);
  }

  public abstract void replace(int position, String word);

  public abstract void parse(String text);

  public abstract void parse(Word word);

//  public abstract void remove(int position);

  public abstract String getText(int wordOffset);

  public ArrayList getWords()
  {
    return words;
  }

  public String getLanguage()
  {
    return language;
  }

  public void parseWord(Word word)
  {
    int upCnt = 0;
    int loCnt = 0;
    boolean isLetter = true;
    for (int i = 0; i < word.length; i++)
    {
      char c = text.charAt(word.offset + i);
      isLetter = Character.isLetter(c);
      if (!isLetter)
      {
        if (configuration.DELIMITERS_JOINED.indexOf(c) >= 0)
        {
          word.addType(Word.TYPE_JOINED);
        }
        else
        {
          word.addType(Word.TYPE_CHAR_NON_LETTER);
        }
      }
      if (Character.isDigit(c))
      {
        word.addType(Word.TYPE_CHAR_DIGIT);
      }
      if (Character.isUpperCase(c))
      {
        if (i == 0 && isLetter)
        {
          word.addType(Word.TYPE_CASE_CAPITALIZED);
        }
        upCnt++;
      }
      else
      {
        loCnt++;
      }
    }
    if (upCnt == word.length)
    {
      word.addType(Word.TYPE_CASE_UPPER);
    }
    else if (upCnt > 1 && word.isType(Word.TYPE_CASE_CAPITALIZED))
    {
      word.addType(Word.TYPE_CASE_MIXED);
    }
    else if (loCnt == word.length)
    {
      word.addType(Word.TYPE_CASE_LOWER);
    }
  }

  /*
   * Check that word has many digits inside.
   * @param word input word
   * @return true if word contains more digits then letters
   */
  public static boolean isNumeric(String word)
  {
    int i0 = word.length();
    int i1 = 0;
    for (int i = 0; i < i0; i++)
    {
      char c = word.charAt(i);
      if (c >= '0' && c <= '9')
      {
        i1++;
      }
    }
    if (i1 * 2 > i0)
    {
      return true;
    }
    return false;
  }

  public final static boolean isINet(String lowerCaseWord)
  {
    return lowerCaseWord.startsWith("http://") ||
        lowerCaseWord.startsWith("www.") || lowerCaseWord.startsWith("ftp://") ||
        lowerCaseWord.startsWith("https://") ||
        lowerCaseWord.startsWith("ftps://") ||
        lowerCaseWord.startsWith("file://") ||
        lowerCaseWord.startsWith("mailto://") || lowerCaseWord.endsWith(".com") ||
        lowerCaseWord.endsWith(".net") || lowerCaseWord.endsWith(".org") ||
        lowerCaseWord.endsWith(".gov") || lowerCaseWord.endsWith(".ru") ||
        lowerCaseWord.endsWith(".uk") || lowerCaseWord.endsWith(".gr");
  }

  public final static boolean isFile(String lowerCaseWord)
  {
    return lowerCaseWord.endsWith(".exe") || lowerCaseWord.endsWith(".txt") ||
        lowerCaseWord.endsWith(".java") || lowerCaseWord.endsWith(".xml") ||
        lowerCaseWord.endsWith(".xsl") || lowerCaseWord.endsWith(".doc") ||
        lowerCaseWord.endsWith(".pdf");
  }

  public synchronized void setLanguage(String language)
  {
    this.language = language;
  }

  public synchronized String getText()
  {
    return text;
  }

  public synchronized void setType(int type)
  {
    this.type = type;
  }

  public synchronized void setText(String text)
  {
    this.text = text;
    textLength = text.length();
    position = 0;
    words = new ArrayList();
  }

  public boolean goWord(int pos)
  {
    if (pos >= 0 && pos < words.size())
    {
      position = pos;
      return true;
    }
    return false;
  }

  public boolean goNextWord()
  {
    return goWord(position + 1);
  }

  public boolean goPrevWord()
  {
    return goWord(position - 1);
  }

  public synchronized Word getWord()
  {
    return getWord(position);
  }

  public Word getWord(int pos)
  {
//DEBUG
//    System.out.println("pos (Document.getWord): " + position + " words: " + words);

    return (Word) words.get(pos);
  }

  public synchronized String toString(int pos)
  {
    return toString( (Word) words.get(pos));
  }

  public synchronized String toString(Word word)
  {
    return text.substring(word.offset, word.offset + word.length);
  }

  public String toString()
  {
    return words.toString();
  }

  public void addWord(Word word)
  {
    int i = words.size();
    if (i > 0)
    {
      Word wordPrev = (Word) words.get(i - 1);
      if (wordPrev.isType(Word.TYPE_PHRASE_LAST))
      {
        word.addType(Word.TYPE_PHRASE_FIRST);
      }
    }
    words.add(word);
  }

  public int getTotalWords()
  {
    return words.size();
  }

}
