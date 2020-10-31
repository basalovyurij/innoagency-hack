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

import com.softcorporation.suggester.engine.core.Result;

/**
 * 
 * @version: $Revision: 1.0 $
 * @author    Vadim Permakoff
 */
public class Suggestion extends Result
{
  public int scoreED; // edit distance
  public int scoreSD; // sound
  public int scoreLN; // length
  public int scoreFC; // first character
  public int scoreLC; // last character
  public int scoreAR; // add or remove character
  public int scoreJW; // joined words
  public int scoreFP; // fuzzy phonemes

  public Suggestion()
  {
  }

  public Suggestion(String word)
  {
    super(word);
  }

  public Suggestion(String word, int score)
  {
    super(word, score);
  }

  public String getWord()
  {
    return word;
  }

  public int compareTo(Object o)
  {
    Suggestion anotherSuggestion = (Suggestion) o;
    if (word.equals(anotherSuggestion.word))
    {
      return 0;
    }
    if (score == anotherSuggestion.score)
    {
      if (scoreED == anotherSuggestion.scoreED)
      {
        if (scoreSD == anotherSuggestion.scoreSD)
        {
          if (scoreLN == anotherSuggestion.scoreLN)
          {
            if (scoreFC == anotherSuggestion.scoreFC)
            {
              if (scoreFP == anotherSuggestion.scoreFP)
              {
                if (scoreLC == anotherSuggestion.scoreLC)
                {
                  if (scoreAR == anotherSuggestion.scoreAR)
                  {
                    return -1;
                  }
                }
                return (scoreAR > anotherSuggestion.scoreAR ? 1 : -1);
              }
              return (scoreLC > anotherSuggestion.scoreLC ? 1 : -1);
            }
            return (scoreFC > anotherSuggestion.scoreFC ? 1 : -1);
          }
          return (scoreLN > anotherSuggestion.scoreLN ? 1 : -1);
        }
        return (scoreSD > anotherSuggestion.scoreSD ? 1 : -1);
      }
      return (scoreED > anotherSuggestion.scoreED ? 1 : -1);
    }
    return (score > anotherSuggestion.score ? 1 : -1);
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(word);
    sb.append(":");
    sb.append(score);
    return sb.toString();
  }  

}
