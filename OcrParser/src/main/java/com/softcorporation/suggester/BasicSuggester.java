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

import com.softcorporation.suggester.language.sound.SoundEncoder;

import com.softcorporation.util.Configuration;
import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SuggesterException;
import com.softcorporation.suggester.util.BasicSuggesterConfiguration;
import com.softcorporation.suggester.engine.core.Dictionary;
import com.softcorporation.suggester.Suggestion;
import com.softcorporation.suggester.engine.Searcher;
import com.softcorporation.suggester.engine.BasicSearcher;
import com.softcorporation.suggester.engine.core.Result;
import com.softcorporation.suggester.engine.core.JoinedResult;
import com.softcorporation.suggester.language.fuzzy.Matcher;
import com.softcorporation.suggester.language.fuzzy.Phoneme;

/**
 * Basic Suggester class searches the dictionary and returns suggestions.
 * <p>
 * @version   1.0, 02/02/2005
 * @author   Vadim Permakoff
 */
public class BasicSuggester extends Suggester
{
  protected BasicSuggesterConfiguration configuration;

  private Dictionary dictionary;

  public BasicSuggester()
  {
    configuration = new BasicSuggesterConfiguration();
  }

  public BasicSuggester(BasicSuggesterConfiguration suggesterConfiguration)
  {
    configuration = suggesterConfiguration;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }

  public void setConfiguration(BasicSuggesterConfiguration configuration)
  {
    this.configuration = configuration;
  }

  public boolean attach(Dictionary dictionary) throws SuggesterException
  {
    String dicName = dictionary.getName();
    if (dicName != null && !dictionary.equals(this.dictionary))
    {
      this.dictionary = dictionary;
      return true;
    }
    return false;
  }

  public boolean detach(Dictionary dictionary)
  {
    String dicName = dictionary.getName();
    if (dicName != null && dictionary.equals(this.dictionary))
    {
      this.dictionary = null;
      return true;
    }
    return false;
  }

  public ArrayList getSuggestions(String word) throws SuggesterException
  {
    return getSuggestions(word, -1, null);
  }

  public ArrayList getSuggestions(String word, int limit)
      throws SuggesterException
  {
    return getSuggestions(word, limit, null);
  }

  protected int getMaxED(int length)
  {
    if (length >= configuration.LENGTH_MIN_ED_4
        && configuration.LENGTH_MIN_ED_4 > 0)
    {
      return 4;
    }
    if (length >= configuration.LENGTH_MIN_ED_3
        && configuration.LENGTH_MIN_ED_3 > 0)
    {
      return 3;
    }
    if (length >= configuration.LENGTH_MIN_ED_2
        && configuration.LENGTH_MIN_ED_2 > 0)
    {
      return 2;
    }
    if (length >= configuration.LENGTH_MIN_ED_1
        && configuration.LENGTH_MIN_ED_1 > 0)
    {
      return 1;
    }
    return 0;
  }

  public ArrayList getSuggestions(String word, int limit, String langCode)
      throws SuggesterException
  {
    ArrayList suggestions = new ArrayList();
    if (word == null)
    {
      return suggestions;
    }
    word = word.trim();
    int wordLenght = word.length();
    if (wordLenght == 0)
    {
      return suggestions;
    }
    int maxED = getMaxED(wordLenght);

    BasicSearcher searcher = new BasicSearcher(configuration);
    if (dictionary == null)
    {
      throw new SuggesterException("No dictionary assigned to search");
    }
    searcher.setDictionary(dictionary);
    ArrayList results = searcher.searchResults(word, maxED);
    for (int i = 0; i < results.size(); i++)
    {
      Result result = (Result) results.get(i);
      Suggestion suggestion = new Suggestion(result.word);
      suggestion.scoreED = result.score;
      if (result instanceof JoinedResult)
      {
        suggestion.scoreJW = configuration.WEIGHT_JOINED_WORD;
      }
      results.set(i, suggestion);
    }

    if (langCode == null)
    {
      langCode = dictionary.language;
    }
    if (langCode == null)
    {
      langCode = BasicSuggesterConfiguration.LANG_CODE_DEFAULT;
    }
    TreeSet sortedSuggestions = sortSuggestions(word, results, langCode);

    int i = 0;
    int cutScore = 0;
    Iterator iter = sortedSuggestions.iterator();
    while (iter.hasNext())
    {
      Suggestion suggestion = (Suggestion) iter.next();
      if (configuration.CLOSE_WORDS_CUT > 0)
      {
        if (i == 0)
        {
          cutScore = suggestion.score * configuration.CLOSE_WORDS_CUT / 10;
        }
        else if (suggestion.score > cutScore)
        {
          break;
        }
      }
      suggestions.add(suggestion);
      i++;
      if (limit > 0 && i >= limit)
      {
        break;
      }
    }
    return suggestions;
  }

  public boolean hasExactWord(String word)
  {
    if (dictionary.contains(word))
    {
      return true;
    }
    return false;
  }

  public int hasWord(String word) throws SuggesterException
  {
    if (dictionary.contains(word))
    {
      return Constants.RESULT_ID_MATCH_EXACT;
    }
    BasicSearcher searcher = new BasicSearcher(configuration);
    searcher.setDictionary(dictionary);
    return searcher.searchWord(word);
  }

  protected TreeSet sortSuggestions(String word, ArrayList suggestions, String langCode)
  {
    Suggestion joinedSuggestion = null;
    TreeSet sortedSuggestions = new TreeSet();

    String sCode = null;
    SoundEncoder soundEncoder = SoundEncoder.getEncoder(langCode);
    if (soundEncoder != null)
    {
      sCode = soundEncoder.getSoundCode(word);
    }

    Phoneme[][] phonemes = null;
    Matcher matcher = Matcher.getMatcher(langCode);
    if (matcher != null)
    {
      phonemes = matcher.getPhonemes(word.toLowerCase());
    }

    String word1 = word.toLowerCase();
    int len1 = word1.length();

    TreeMap acceptedResults = new TreeMap();
    Iterator iter = suggestions.iterator();
    while (iter.hasNext())
    {
      Suggestion suggestion = (Suggestion) iter.next();
      if (soundEncoder != null)
      {
        String sCode1 = soundEncoder.getSoundCode(suggestion.word);
        suggestion.scoreSD = Searcher.getED(sCode, sCode1, Constants.ED_SD_MAX);
      }

      // calculate first character score
      char c = word.charAt(0);
      char c1 = suggestion.word.charAt(0);
      if (c != c1)
      {
        char cL = Character.toLowerCase(c);
        char cU = Character.toUpperCase(c);
        char c1L = Character.toLowerCase(c1);
        char c1U = Character.toUpperCase(c1);
        if (cL == c1L)
        {
          if (cL != c1)
          {
            suggestion.scoreFC = configuration.WEIGHT_FIRST_CHAR_UPPER;
          }
          else
          {
            suggestion.scoreFC = configuration.WEIGHT_FIRST_CHAR_LOWER;
          }
        }
        else
        {
          suggestion.scoreFC = configuration.WEIGHT_FIRST_CHAR;
          if (c1U == c1 && cU != c)
          {
            suggestion.scoreFC += configuration.WEIGHT_FIRST_CHAR_UPPER;
          }
          else if (c1L == c1 && cL != c)
          {
            suggestion.scoreFC += configuration.WEIGHT_FIRST_CHAR_LOWER;
          }
        }
      }

      String word2 = suggestion.word.toLowerCase();
      int len2 = suggestion.word.length();

      suggestion.scoreLN = Math.abs(len1 - len2);

      // calculate append remove score
      if (configuration.WEIGHT_ADD_REM_CHAR > 0)
      {
        int len;
        if (len2 < len1)
        {
          len = len1;
        }
        else
        {
          len = len2;
        }
        for (int i = 0; i < len; i++)
        {
          if (i < len1)
          {
            if (word2.indexOf(word1.charAt(i)) < 0)
            {
              suggestion.scoreAR++;
            }
          }
          if (i < len2)
          {
            if (word1.indexOf(word2.charAt(i)) < 0)
            {
              suggestion.scoreAR++;
            }
          }
        }
        suggestion.scoreAR = suggestion.scoreAR;
      }

      if (word.charAt(len1 - 1) != suggestion.word.charAt(len2 - 1))
      {
        suggestion.scoreLC = configuration.WEIGHT_LAST_CHAR;
      }

      if (matcher != null)
      {
        suggestion.scoreFP = (100 - matcher.getWeight(word, word2, phonemes));
      }

      // calculate total score
      suggestion.score = suggestion.scoreED
          * configuration.WEIGHT_EDIT_DISTANCE + suggestion.scoreSD
          * configuration.WEIGHT_SOUNDEX + suggestion.scoreLN
          * configuration.WEIGHT_LENGTH + suggestion.scoreFC
          + suggestion.scoreLC + suggestion.scoreAR
          * configuration.WEIGHT_ADD_REM_CHAR + suggestion.scoreJW
          + suggestion.scoreFP * configuration.WEIGHT_FUZZY;

      // remove variations of joined words
      if (suggestion.scoreJW > 0 && configuration.REMOVE_JOINED_VARIATIONS)
      {
        if (joinedSuggestion == null)
        {
          joinedSuggestion = suggestion;
        }
        else
        {
          if (suggestion.score < joinedSuggestion.score)
          {
            sortedSuggestions.remove(joinedSuggestion);
            joinedSuggestion = suggestion;
          }
          else
          {
            continue;
          }
        }
      }

      // remove duplicates (EN: Tab / tab)
      if (configuration.REMOVE_CASE_DUPLICATES)
      {
        Suggestion suggestion0 = (Suggestion) acceptedResults.get(word2);
        if (suggestion0 == null)
        {
          sortedSuggestions.add(suggestion);
          acceptedResults.put(word2, suggestion);
        }
        else if (suggestion.score < suggestion0.score)
        {
          sortedSuggestions.remove(suggestion0);
          sortedSuggestions.add(suggestion);
          acceptedResults.put(word2, suggestion);
        }
      }
      else
      {
        sortedSuggestions.add(suggestion);
      }
    }
    return sortedSuggestions;
  }

}
