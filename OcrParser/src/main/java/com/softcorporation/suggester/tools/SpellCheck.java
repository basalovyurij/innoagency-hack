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

import java.util.*;

import com.softcorporation.util.Configuration;
import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SuggesterException;
import com.softcorporation.suggester.util.SpellCheckConfiguration;
import com.softcorporation.suggester.dictionary.BasicDictionary;
import com.softcorporation.suggester.Suggester;
import com.softcorporation.suggester.Suggestion;
import com.softcorporation.suggester.text.Word;
import com.softcorporation.suggester.text.Document;
import com.softcorporation.suggester.text.TextDocument;
import com.softcorporation.suggester.text.XmlDocument;
import com.softcorporation.suggester.text.HtmlDocument;

/**
 *
 * @version: $Revision:   1.0  $
 */
public class SpellCheck
{
  private SpellCheckConfiguration configuration;
  private Suggester suggester;
//  private String text;
  private Document document;
  private int position;
  private boolean isMisspelt;
  private String language;
  private String misspWord;

  private TreeMap ignoredWords = new TreeMap();
  private TreeMap autoReplaceWords = new TreeMap();
  private ArrayList suggestions;
  private int errors;
  private int suggestionLimit;

  public SpellCheck()
  {
    configuration = new SpellCheckConfiguration();
  }

  public SpellCheck(SpellCheckConfiguration spellCheckConfiguration)
  {
    configuration = spellCheckConfiguration;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }

  public void setConfiguration(SpellCheckConfiguration spellCheckConfiguration)
  {
    configuration = spellCheckConfiguration;
  }

  public int getPosition()
  {
    return position;
  }

  public void setPosition(int pos)
  {
    position = pos;
  }

  public Document getDocument()
  {
    return document;
  }

  public Suggester getSuggester()
  {
    return suggester;
  }

  public void setSuggester(Suggester suggester)
  {
    this.suggester = suggester;
  }

  public String getText()
  {
    return document.getText();
  }

  public void setSuggestionLimit(int limit)
  {
    suggestionLimit = limit;
  }

  public int getSuggestionLimit()
  {
    return suggestionLimit;
  }

  public void setText(String text) throws SuggesterException
  {
    setText(text, Constants.DOC_TYPE_TEXT);
  }

  public void setText(String text, int textType) throws SuggesterException
  {
    setText(text, textType, null);
  }

  public void setText(String text, int textType, String language) throws
      SuggesterException
  {
    position = 0;
//    this.text = text;
    this.language = language;
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

// DEBUG
//    System.out.println("DOC: " + document);

  }

  public boolean hasMisspelt()
  {
    return isMisspelt;
  }

  public String getMisspelt()
  {
    if (isMisspelt)
    {
      return misspWord;
    }
    else
    {
      return null;
    }
  }

  public Word getMisspeltWord()
  {
    if (isMisspelt)
    {
      return document.getWord();
    }
    else
    {
      return null;
    }
  }

  public int getMisspeltOffset()
  {
    return getMisspeltWord().offset;
  }

  public int getMisspeltLength()
  {
    return getMisspeltWord().length;
  }

  public String getMisspeltText(int wordsBefore, String tagOpen,
                                String tagClose, int wordsAfter) throws
      SuggesterException
  {
    StringBuffer sb = new StringBuffer();
    sb.append(document.getText( -wordsBefore));
    if (tagOpen != null)
    {
      sb.append(tagOpen);
    }
    sb.append(document.toString(document.getWord()));
    if (tagClose != null)
    {
      sb.append(tagClose);
    }
    sb.append(document.getText(wordsAfter));
    return sb.toString();
  }

  public boolean isIgnoredType(Word word)
  {
    if ( (configuration.IGNORE_MIXED_CASE && word.isType(Word.TYPE_CASE_MIXED)) ||
        (configuration.IGNORE_UPPER_CASE && word.isType(Word.TYPE_CASE_UPPER)) ||
        (configuration.IGNORE_DIGIT_WORDS && word.isType(Word.TYPE_CHAR_DIGIT)) ||
        (configuration.IGNORE_INET_WORDS && word.isType(Word.TYPE_CHAR_INET)) ||
        (configuration.IGNORE_NON_WORDS && word.isType(Word.TYPE_CHAR_NON_LETTER)))
    {
      return true;
    }
    return false;
  }

  public boolean isIgnored(Word word)
  {
    if (ignoredWords.containsKey(document.toString(word)))
    {
      return true;
    }
    return false;
  }

  public boolean isInDictionary(String sWord, boolean exactCase) throws
      SuggesterException
  {
    if (exactCase)
    {
      return suggester.hasExactWord(sWord);
    }
    else
    {
      int result = suggester.hasWord(sWord);
      if (result == Constants.RESULT_ID_MATCH ||
          result == Constants.RESULT_ID_MATCH_EXACT)
      {
        return true;
      }
    }
    return false;
  }

  public boolean isInDictionary(String sWord, Word word) throws SuggesterException
  {
    if (suggester.hasExactWord(sWord))
    {
      return true;
    }
    if (word.isType(Word.TYPE_CASE_CAPITALIZED) && !word.isType(Word.TYPE_CASE_MIXED))
    {
      String lWord = sWord.toLowerCase();
      if (suggester.hasExactWord(lWord)) // word is OK in lowercase
      {
        return true;
      }
    }
    if (word.isType(Word.TYPE_CASE_UPPER))
    {
      if (suggester.hasWord(sWord) == Constants.RESULT_ID_MATCH)
      {
        return true;
      }
    }
    return false;
  }

  public boolean isAutoReplace(String word)
  {
    return autoReplaceWords.containsKey(word);
  }

  private void capitalizeSuggestions(Word word, List suggestions)
  {
    boolean all = word.isType(Word.TYPE_CASE_UPPER);
    TreeSet words = new TreeSet();
    Iterator iterator = suggestions.iterator();
    while (iterator.hasNext())
    {
      Suggestion suggestion = (Suggestion) iterator.next();
      if (all)
      {
        suggestion.word = suggestion.word.toUpperCase();
      }
      else
      {
        suggestion.word = Character.toUpperCase(suggestion.word.charAt(0)) +
                                                suggestion.word.substring(1);
      }
      if (words.contains(suggestion.word))
      {
        iterator.remove();
      }
      else
      {
        words.add(suggestion.word);
      }
    }
  }

  private boolean needCapitalizePhraseWord(Word word)
  {
    return (word.isType(Word.TYPE_PHRASE_FIRST) &&
            !word.isType(Word.TYPE_CASE_CAPITALIZED) &&
            configuration.CAPITALIZE_FIRST_LETTER);
  }

  public void check() throws SuggesterException
  {
//DEBUG
//    System.out.println("pos (check): " + position);

    isMisspelt = false;
    misspWord = "";
    for (; position < document.getTotalWords(); position++)
    {
      if (!document.goWord(position))
      {
        throw new SuggesterException("Internal error: Invalid document");
      }
      Word word = document.getWord();
      if (isIgnored(word)) // continue if ignored
      {
        continue;
      }
      if (isIgnoredType(word)) // type first because for capitalization I need types
      {
        continue;
      }
      String sWord = document.toString(word);
      boolean isOk = isInDictionary(sWord, word);
      if (isOk) // word is OK
      {
        if (needCapitalizePhraseWord(word))
        {
          errors++;
          isMisspelt = true;
          misspWord = sWord;
          suggestions = new ArrayList();
          Suggestion suggestion = new Suggestion(sWord);
          suggestions.add(suggestion);
          capitalizeSuggestions(word, suggestions);
          return;
        }
        continue;
      }
      else
      {
        // divided word
        if (word.isType(Word.TYPE_LINE_BREAK) && position < document.totalWords - 1) // line break
        {
          Word wordNext = document.getWord(position + 1);
          if (wordNext.isType(Word.TYPE_LINE_FIRST) && (wordNext.isType(Word.TYPE_CASE_LOWER) ||
                                                        wordNext.isType(Word.TYPE_CASE_UPPER)))
          {
            String sWordNext = document.toString(wordNext);
            char c = sWord.charAt(sWord.length() - 1);
            if (c == '-' || c == '\u2014')
            {
              if (isInDictionary(sWord.substring(0, sWord.length() - 1) + sWordNext, word))
              {
                isOk = true;
                position++;
              }
            }
            else
            {
              if (isInDictionary(sWord + sWordNext, word))
              {
                isOk = true;
                position++;
              }
            }
          }
        }

        // joined words
        if (word.isType(Word.TYPE_JOINED))
        {
          StringTokenizer tokens = new StringTokenizer(sWord, configuration.DELIMITERS_JOINED);
          isOk = true;
          while (tokens.hasMoreTokens())
          {
            String sWrd = tokens.nextToken();
            if (!isInDictionary(sWrd, word))
            {
              isOk = false;
            }
          }
        }
      }

      // Can not ignore this misspelt word
      errors++;
      if (isAutoReplace(sWord))
      {
        document.replace( (String) autoReplaceWords.get(sWord));
        continue;
      }
      isMisspelt = true;
      misspWord = sWord;
      suggestions = suggester.getSuggestions(sWord, suggestionLimit, language);
      if (word.isType(Word.TYPE_CASE_CAPITALIZED))
      {
        capitalizeSuggestions(word, suggestions);
      }
      return;
    }
  }

  public void check(int offset) throws SuggesterException
  {
    ArrayList words = document.getWords();
    int i = 0;
    for (; i < words.size(); i++)
    {
      Word word = (Word) words.get(i);
      if (word.offset > offset)
      {
        break;
      }
    }
    if (i == 0)
    {
      position = 0;
    }
    else
    {
      position = i - 1;
    }
    check();
  }

  public void checkNext() throws SuggesterException
  {
    position++;
    check();
  }

  public void ignore()
  {
  }

  public void ignoreAll()
  {
    String word = document.toString(document.getWord());
    ignoredWords.put(word, word);
  }

  public void change(String word)
  {
    document.replace(word);
  }

  public void changeAll(String word)
  {
    document.replace(word);
    autoReplaceWords.put(word, word);
  }

  public boolean attach(BasicDictionary dictionary) throws SuggesterException
  {
    return suggester.attach(dictionary);
  }

  public ArrayList getSuggestions()
  {
    return suggestions;
  }

  /*
    public int checkNext() throws SpellException
    {
      wCursor_ += wLength_;
      int i = wCursor_;
      CharChecker charchecker = getCharChecker();
      for (; wCursor_ < inputSeqLen_; wCursor_++)
      {
        char c = inputSeq_.charAt(wCursor_);
        if (!charchecker.isWordStart(c))
        {
          if (!Character.isWhitespace(c))
          {
            prevWordLength_ = 0;
          }
        }
        else
        {
          if (!punctuationOK(i, wCursor_))
          {
            return 5;
          }
          int j;
          for (j = wCursor_ + 1; j < inputSeqLen_; j++)
          {
            if (!charchecker.isWordChar(inputSeq_.charAt(j)))
            {
              break;
            }
          }

   for (; j > wCursor_ && !charchecker.isWordEnd(inputSeq_.charAt(j - 1));
               j--)
          {
            ;
          }
          wLength_ = j - wCursor_;
          if (wLength_ > 0)
          {
            if (!ignoreDuplicates_)
            {
              int k = wLength_;
              if (prevWordLength_ == wLength_)
              {
                while (--k >= 0)
                {
                  if (inputSeq_.charAt(wCursor_ + k) != prevWord_[k])
                  {
                    break;
                  }
                }
              }
              int i1 = Math.min(wLength_, prevWord_.length);
              for (int j1 = 0; j1 < i1; j1++)
              {
                prevWord_[j1] = inputSeq_.charAt(wCursor_ + j1);
              }

              prevWordLength_ = i1;
              if (k < 0)
              {
                return 4;
              }
            }
            int l = checkWord(inputSeq_, wCursor_, wLength_);
            if (l != 0)
            {
              return l;
            }
            boSentence_ = false;
            i = j;
            wCursor_ = j - 1;
          }
        }
      }

      return punctuationOK(i, wCursor_) ? 0 : 5;
    }

   void learnWord(String s, String s1)void learnSuggestion(String s, String s1,
        String s2)void learnBannedWord(String s,
   String s1)void open()void clear(boolean flag)public void setInput(String
        s)public CharSequence getInput()

        public int getPosition()public String getWord()public int checkWord(
        String s)public Suggestions getSuggestions()public Suggestions
   getSuggestions(String s)public void learnWord(String s, String s1)public void
        learnSuggestion(String s, String s1, String s2)public void
        learnBannedWord(String s,
        String s1)public void setSelectedLanguage(String s)public String
   getSelectedLanguage()public void setLogger(Logger logger1)private void
        log(String s)
   */

}
