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
package com.softcorporation.suggester.engine;

import java.util.*;

import com.softcorporation.util.Configuration;
import com.softcorporation.suggester.util.SuggesterException;
import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.BasicSuggesterConfiguration;
import com.softcorporation.suggester.engine.core.Result;
import com.softcorporation.suggester.engine.core.JoinedResult;
import com.softcorporation.suggester.engine.core.Node;

/**
 *
 * @version: $Revision:   1.0  $
 * @author    Vadim Permakoff
 */
public class BasicSearcher extends Searcher
{
  int x;
  ArrayList results;
  ArrayList resultsPartial;
  boolean searchJoined;
  int maxED;
  int maxEditDistance;
  char[] charArray = new char[Constants.WORD_LENGTH_MAX + 10]; // maxED = 0, but it can be more !!!
  int[] typeArray = new int[Constants.WORD_LENGTH_MAX + 10];
  char[] wordChars;
  char[] wordCharsLC;
  String wordLC;
  String word = "";
  String searchWord = "";
  int wordLength;
  int wordLength2;

  int[][] ed;
  int[] dictionaryArray;

  JoinedResult result1;
  TreeMap resultsMap;
  String joinedWord;
  ArrayList resultsAll;

  public BasicSearcher()
  {
  }

  public BasicSearcher(Configuration configuration)
  {
    this.configuration = configuration;
  }

  public ArrayList searchResults(String searchWord, int maxEditDistance) throws
      SuggesterException
  {
    this.searchWord = searchWord;
    this.maxEditDistance = maxEditDistance;
    searchJoined = ( (BasicSuggesterConfiguration) configuration).SEARCH_JOINED;
    results = new ArrayList(100);
    resultsPartial = new ArrayList();
    dictionaryArray = dictionary.getDictionaryArray();
    searchCloseWords(searchWord, maxEditDistance);
    resultsAll = results;
    if (searchJoined)
    {
      addJoinedWords();
    }
    results = resultsAll;
//    deleteRemovedWords();
    return results;
  }

  void searchCloseWords(String searchWord, int maxEditDistance) throws
      SuggesterException
  {
    word = searchWord;
    maxED = maxEditDistance;
    wordLength = word.length();
    wordLength2 = word.length() -
        ( (BasicSuggesterConfiguration) configuration).JOINED_WORD_LENGTH_MIN;
    wordChars = word.toCharArray();
    wordLC = word.toLowerCase(); // locale !!!
    wordCharsLC = wordLC.toCharArray();

    int lenX = wordLength + maxED + 4;
    int lenY = wordLength + 2;
    ed = new int[lenX][lenY];
    for (int x = 0; x < lenX; x++)
    {
      ed[x][0] = x;
    }
    for (int y = 0; y < lenY; y++)
    {
      ed[0][y] = y;
    }
    for (int pos = 1; ; pos++)
    {
      x = 0;
      int lnk = dictionaryArray[pos];
      if (lnk > 0)
      {
        findMatch(lnk, 0, 0, 0);
      }
      else
      {
        findMatch( -lnk, 0, 0, 0);
        break;
      }
    }
//    searchAddedWords();
  }

  void addJoinedWords() throws SuggesterException
  {
    ArrayList results1 = resultsPartial;
    if (results1.size() > 0)
    {
      searchJoined = false;
      joinedWord = word;
      resultsMap = new TreeMap();
      Iterator iter1 = results1.iterator();
      while (iter1.hasNext())
      {
        result1 = (JoinedResult) iter1.next();
        searchJoinedWords(result1.word.length());
        if ( ( (BasicSuggesterConfiguration) configuration).DELIMITERS_JOINED.
            indexOf(joinedWord.charAt(result1.word.length())) >= 0)
        {
          searchJoinedWords(result1.word.length() + 1);
          if ( (result1.word.length() != result1.wordLength) &&
              (result1.word.length() + 1 != result1.wordLength))
          {
            searchJoinedWords(result1.wordLength);
          }
        }
        else if (result1.word.length() != result1.wordLength)
        {
          searchJoinedWords(result1.wordLength);
        }
      }
    }
  }

  void searchJoinedWords(int wordLength1) throws SuggesterException
  {
    int wordLength2 = joinedWord.length() - wordLength1;
    if (wordLength2 >=
        ( (BasicSuggesterConfiguration) configuration).JOINED_WORD_LENGTH_MIN)
    {
      Integer iWordLength2 = new Integer(wordLength2);
      if (resultsMap.containsKey(iWordLength2))
      {
        results = (ArrayList) resultsMap.get(iWordLength2);
      }
      else
      {
        int joinEditDistance;
        if (wordLength2 > ( (BasicSuggesterConfiguration) configuration).JOINED_WORD_LENGTH_EDT - 1)
        {
          joinEditDistance = 1;
        }
        else
        {
          joinEditDistance = 0;
        }
        results = new ArrayList();
        resultsPartial = new ArrayList();
        String word2 = joinedWord.substring(wordLength1);
        searchCloseWords(word2, joinEditDistance);
        resultsMap.put(iWordLength2, results);
      }
      Iterator iter2 = results.iterator();
      while (iter2.hasNext())
      {
        Result result2 = (Result) iter2.next();
        int joinedScore = result1.score + result2.score + 1;
        if (joinedScore <= maxEditDistance)
        {
          JoinedResult result = new JoinedResult(result1.word + " " +
                                                 result2.word, joinedScore);
          result.joinedScore = ( (BasicSuggesterConfiguration) configuration).WEIGHT_JOINED_WORD;
          resultsAll.add(result);
        }
      }
    }
  }

  void findMatch(int position, int curED, int yBegin, int yEnd)
  {
    int c = dictionaryArray[position];
    int type = c >> 16;
    char chr = (char) c;
    charArray[x] = chr;
    typeArray[x] = type;
    int begin = yBegin;
    int e = 0;
    int curL = yBegin;
    for (int y = yBegin; y < wordChars.length; y++)
    {
      if (chr == wordCharsLC[y])
      {
        e = ed[x][y];
      }
      else
      {
        if (y > 0 && x > 0 && (chr == wordCharsLC[y - 1]) &&
            (charArray[x - 1] == wordCharsLC[y]))
        {
          e = ed[x - 1][y - 1];
        }
        else
        {
          e = ed[x][y];
        }
        if (e > ed[x + 1][y])
        {
          e = ed[x + 1][y];
        }
        if (e > ed[x][y + 1])
        {
          e = ed[x][y + 1];
        }
        e++;
      }
      ed[x + 1][y + 1] = e;
      if (y == begin)
      {
        if (e > maxED)
        {
          ed[x + 2][y + 1] = e;
          yBegin++;
        }
        curED = e;
      }
      else if (e < curED)
      {
        curED = e;
        curL = y;
      }
      else if (e > maxED)
      {
        ed[x + 1][y + 2] = e;
        if (y > yEnd)
        {
          yEnd = y;
          break;
        }
      }
    }
    if ( (type & Node.NODE_TYPE_END) != 0)
    {
      String foundWord = null;
      if (searchJoined && x < wordLength2 &&
          ( (curED == 0 && x >= ((BasicSuggesterConfiguration)configuration).JOINED_WORD_LENGTH_MIN - 1) ||
           (curED == 1 && x >= ((BasicSuggesterConfiguration)configuration).JOINED_WORD_LENGTH_EDT - 1)))

      {
        int len = x + 1;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++)
        {
          if ( (typeArray[i] & Node.NODE_TYPE_CAPITAL) == 0)
          {
            chars[i] = charArray[i];
          }
          else
          {
            chars[i] = Character.toUpperCase(charArray[i]);
          }
        }
        foundWord = new String(chars);
        JoinedResult result = new JoinedResult(foundWord, curED, curL + 1);
        resultsPartial.add(result);
      }
      if (e <= maxED) // results
      {
        if (foundWord == null)
        {
          int len = x + 1;
          char[] chars = new char[len];
          for (int i = 0; i < len; i++)
          {
            if ( (typeArray[i] & Node.NODE_TYPE_CAPITAL) == 0)
            {
              chars[i] = charArray[i];
            }
            else
            {
              chars[i] = Character.toUpperCase(charArray[i]);
            }
          }
          foundWord = new String(chars);
        }
        Result result = new Result(foundWord, e);
        results.add(result);
      }
    }
    if (curED <= maxED && (type & Node.NODE_TYPE_LAST_X) == 0)
    {
      x++;
      for (int pos = position + 1; ; pos++)
      {
        int lnk = dictionaryArray[pos];
        if (lnk > 0)
        {
          findMatch(lnk, curED, yBegin, yEnd);
        }
        else
        {
          findMatch( -lnk, curED, yBegin, yEnd);
          break;
        }
      }
      x--;
    }
  }

  public int searchWord(String word) throws SuggesterException
  {
    wordCharsLC = word.toLowerCase().toCharArray(); // locale
    wordLength = wordCharsLC.length - 1;
    dictionaryArray = dictionary.getDictionaryArray();
    return searchDictWord();
  }

  public int searchDictWord()
  {
    x = 0;
    int res;
    for (int pos = 1; ; pos++)
    {
      int lnk = dictionaryArray[pos];
      if (lnk > 0)
      {
        if ( (res = findMatch(lnk)) == RESULT_ID_MATCH)
        {
          return res;
        }
      }
      else
      {
        if ( (res = findMatch( -lnk)) == RESULT_ID_MATCH)
        {
          return res;
        }
        break;
      }
    }
    return RESULT_ID_NO_MATCH;
  }

  int findMatch(int position)
  {
    char cl = wordCharsLC[x];
    int c = dictionaryArray[position];
    int type = c >> 16;
    char chr = (char) c;
    if (chr < cl)
    {
      return RESULT_ID_UNDEFINED;
    }
    if (chr == cl)
    {
      if (x == wordLength)
      {
        if ( (type & Node.NODE_TYPE_END) != 0)
        {
          return RESULT_ID_MATCH;
        }
        return RESULT_ID_UNDEFINED;
      }
      else if (x < wordLength && (type & Node.NODE_TYPE_LAST_X) == 0)
      {
        x++;
        int res;
        for (int pos = position + 1; ; pos++)
        {
          int lnk = dictionaryArray[pos];
          if (lnk > 0)
          {
            if ( (res = findMatch(lnk)) == RESULT_ID_MATCH)
            {
              return res;
            }
          }
          else
          {
            if ( (res = findMatch( -lnk)) == RESULT_ID_MATCH)
            {
              return res;
            }
            break;
          }
        }
        x--;
      }
    }
    return RESULT_ID_NO_MATCH;
  }

}
