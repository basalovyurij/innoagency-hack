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

//import com.softcorporation.suggester.util.SpellCheckConfiguration;
import com.softcorporation.suggester.util.BasicSuggesterConfiguration;

/**
 *
 * @version: $Revision:   1.0  $
 */
public class TextDocument extends Document
{
  public TextDocument(BasicSuggesterConfiguration configuration)
  {
    super(configuration);
  }

  public TextDocument(String text, String language, BasicSuggesterConfiguration configuration)
  {
    super(configuration);
    this.language = language;
    parse(text);
  }

  public void parse(Word word)
  {
    // line first
    int i = word.offset - 1;
    for (; i >= 0; i--)
    {
      char c = text.charAt(i);
      if (c == '\n')
      {
        word.addType(Word.TYPE_LINE_FIRST);
      }
      else if (Character.isWhitespace(c))
      {
        continue;
      }
      break;
    }
    // line last and line break
    boolean lineBreak = false;
    i = word.offset + word.length - 1;
    char c = text.charAt(i);
    if (c == '-' || c == '\u2014')
    {
      lineBreak = true;
      i++;
    }
    else if (++i < textLength)
    {
      c = text.charAt(i);
      if (c == '-' || c == '\u2014')
      {
        lineBreak = true;
        i++;
      }
    }
    for (; i < textLength; i++)
    {
      c = text.charAt(i);
      if (c == '\n')
      {
        word.addType(Word.TYPE_LINE_LAST);
        if (lineBreak)
        {
          word.addType(Word.TYPE_LINE_BREAK);
        }
      }
      else if (Character.isWhitespace(c))
      {
        continue;
      }
      break;
    }
    // phrase last
    if (word.isType(Word.TYPE_LINE_LAST))
    {
      i = word.offset + word.length;
      if (i < textLength && text.charAt(i) == '.')
      {
        word.addType(Word.TYPE_PHRASE_LAST);
      }
    }
    parseWord(word);
  }

/*
  protected static final int PARSER_STATUS_WHITE_SPACE = 0;
  protected static final int PARSER_STATUS_WITHIN_WORD = 1;
  protected static final int PARSER_STATUS_DELIM_BEFORE_WORD = 2;
  protected static final int PARSER_STATUS_WITHIN_WORD = 1;
  protected static final int PARSER_STATUS_WITHIN_WORD = 1;
  protected static final int PARSER_STATUS_WITHIN_WORD = 1;

  public void parse(String text)
  {
    setText(text);
    boolean withinWord = false;
    int pos = 0;
    int pos1 = 0;
    int pos2 = 0;
    int pos3 = 0;
    int parserStatus = PARSER_STATUS_WHITE_SPACE;

    int i1 = 0;
    for (int i = 0; i < textLength; i++)
    {
      char c = text.charAt(i);
      switch (parserStatus)
      {
        case PARSER_STATUS_WITHIN_WORD:
        {
          if (Character.isWhitespace(c))
          {
            addWord(pos1, i);
            status = PARSER_STATUS_WHITE_SPACE;
          }
          else if (configuration.DELIMITERS.indexOf(c) >= 0)
          {
            pos1 = i;
            status = PARSER_STATUS_DELIM_BEFORE_WORD;
          }
          else
          {
            pos1 = i;
            status = PARSER_STATUS_WITHIN_WORD;
          }
          break;
        }
        case PARSER_STATUS_WHITE_SPACE:
        {
          if (Character.isWhitespace(c))
          {
            continue;
          }
          else if (configuration.DELIMITERS.indexOf(c) >= 0)
          {
            pos1 = i;
            status = PARSER_STATUS_DELIM_BEFORE_WORD;
          }
          else
          {
            pos1 = i;
            status = PARSER_STATUS_WITHIN_WORD;
          }
          break;
        }
  case '&':
  {
    sb.append("&amp;");
    break;
  }
  case '"':
  {
    sb.append("&quot;");
    break;
  }
  case '\'':
  {
    sb.append("&apos;");
    break;
  }
  case '\r': case '\n':
  {
    if (canonical)
    {
      sb.append("&#");
      sb.append(Integer.toString(ch));
      sb.append(';');
      break;
    }
  }
  default:
  {
    sb.append(ch);
  }






      if (Character.isWhitespace(c))
      {
        if (status == PARSER_STATUS_WITHIN_WORD)
        {
          addWords(pos, i);
          withinWord = false;
        }
      }
      else
      {
        if (status != PARSER_STATUS_WITHIN_WORD)
        {
          pos = i;
          withinWord = true;
        }
      }
    }
    if (withinWord)
    {
      addWords(pos, textLength);
    }
    totalWords = words.size();
  }
*/

  public void parse(String text)
  {
//DEBUG
//    System.out.println("text: " + text);

    setText(text);
    boolean withinWord = false;
    int pos = 0;

    int i1 = 0;
    for (int i = 0; i < textLength; i++)
    {
      char c = text.charAt(i);
      if (Character.isWhitespace(c))
      {
        if (withinWord)
        {
          addWords(pos, i);
          withinWord = false;
        }
      }
      else
      {
        if (!withinWord)
        {
          pos = i;
          withinWord = true;
        }
      }
    }
    if (withinWord)
    {
      addWords(pos, textLength);
    }

    totalWords = words.size();
    if (totalWords > 0)
    {
      Word word = (Word) words.get(0);
      word.addType(Word.TYPE_LINE_FIRST);
      word.addType(Word.TYPE_PHRASE_FIRST);

      word = (Word) words.get(totalWords - 1);
      word.addType(Word.TYPE_LINE_LAST);
      word.addType(Word.TYPE_PHRASE_LAST);
    }
  }

  private void addWord(int pos1, int pos2, int type)
  {
    Word w = new Word();
    w.offset = pos1;
    w.length = pos2 - pos1;
    w.type = type;
    parse(w);
    addWord(w);
  }

  private void addWords(int pos1, int pos2)
  {
//DEBUG
//    System.out.println("\n\nconfig: " + configuration);

    while (configuration.DELIMITERS.indexOf(text.charAt(pos1)) >= 0)
    {
      if (++pos1 >= pos2)
      {
        return;
      }
    }
    while (configuration.DELIMITERS.indexOf(text.charAt(pos2 - 1)) >= 0)
    {
      if (pos1 >= --pos2)
      {
        return;
      }
    }
    String lowerCaseWord = text.substring(pos1, pos2).toLowerCase();
    if (isINet(lowerCaseWord))
    {
      addWord(pos1, pos2, Word.TYPE_CHAR_INET);
      return;
    }
    else if (isFile(lowerCaseWord))
    {
      addWord(pos1, pos2, Word.TYPE_CHAR_FILE);
      return;
    }
    int i = pos1;
    while (i < pos2)
    {
      if (configuration.DELIMITERS.indexOf(text.charAt(i)) >= 0)
      {
        addWord(pos1, i, 0);
        addWords(i + 1, pos2);
        return;
      }
      else
      {
        i++;
      }
    }
    addWord(pos1, i, 0);
  }
/*
  public void parse0(String text)
  {
    setText(text);
    boolean withinWord = false;
    int pos = 0;

    int i1 = 0;
    for (int i = 0; i < textLength; i++)
    {
      char c = text.charAt(i);
      if (Character.isWhitespace(c) || configuration.DELIMITERS.indexOf(c) >= 0)
      {
        if (withinWord)
        {
          if (!configuration.IGNORE_INET_WORDS)
          {
            if (c == ':' || c == '.' || c == '@')
            {

            }
          }
          Word w = new Word();
          w.offset = pos;
          w.length = i - pos;
          parse(w);
          addWord(w);
          withinWord = false;
        }
      }
      else
      {
        if (!withinWord)
        {
          pos = i;
          withinWord = true;
        }
      }
    }
    if (withinWord)
    {
      Word w = new Word();
      w.offset = pos;
      w.length = textLength - pos;
      parse(w);
      addWord(w);
    }
    totalWords = words.size();
  }

  public synchronized void replace0(int pos, String word)
  {
    Word w = getWord(pos);
    String text = getText();
    setText(text.substring(0, w.offset) + word +
            text.substring(w.offset + w.length));
    int diff = w.length - word.length();
    if (diff != 0)
    {
      ArrayList wes = getWords();
      for (int i = pos + 1; i < wes.size(); i++)
      {
        Word w1 = getWord(i);
        w1.offset += diff;
      }
      w.length = word.length();
    }
  }
*/
  public synchronized void replace(int pos, String word)
  {
    Word w = getWord(pos);
    String text = getText();
    parse(text.substring(0, w.offset) + word +
          text.substring(w.offset + w.length));
  }

  public String getText(int positionOffset)
  {
//DEBUG
//    System.out.println("pos (getText): " + position + " posOffset: " + positionOffset);

    int position1 = position + positionOffset;
    if (position1 >= totalWords)
    {
      position1 = totalWords - 1;
    }
    else if (position1 < 0)
    {
      position1 = 0;
    }
    else if (position1 == position)
    {
      return "";
    }
    Word word = (Word) words.get(position);
    Word word1 = (Word) words.get(position1);
    if (word.offset < word1.offset)
    {
      return text.substring(word.offset + word.length,
                            word1.offset + word1.length);
    }
    else
    {
      return text.substring(word1.offset, word.offset);
    }
  }

}
