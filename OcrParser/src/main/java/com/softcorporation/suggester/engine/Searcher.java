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
import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SuggesterException;
import com.softcorporation.suggester.engine.core.Dictionary;

/**
 * 
 * @version: $Revision: 1.0 $
 * @author    Vadim Permakoff
 */
public abstract class Searcher implements Constants
{
  protected Dictionary dictionary;
  protected Configuration configuration;

  public Searcher()
  {
  }

  public Searcher(Configuration configuration)
  {
    this.configuration = configuration;
  }

  public void setDictionary(Dictionary dictionary)
  {
    this.dictionary = dictionary;
  }

  public Dictionary getDictionary()
  {
    return dictionary;
  }

  public void setConfiguration(Configuration configuration)
  {
    this.configuration = configuration;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }

  public abstract ArrayList searchResults(String text, int maxED) throws SuggesterException;

  public abstract int searchWord(String word) throws SuggesterException;

  public static int getED(String word1, String word2, int stopED)
  {
    String sX, sY;
    int lenX;
    int lenY;
    int len1 = word1.length();
    int len2 = word2.length();
    if (len1 < len2)
    {
      sX = word2;
      lenX = len2;
      sY = word1;
      lenY = len1;
    }
    else
    {
      sX = word1;
      lenX = len1;
      sY = word2;
      lenY = len2;
    }

    int minED = -1;
    int[][] ed = new int[lenX + 1][lenY + 1];
    for (int x = 0; x <= lenX; x++)
    {
      ed[x][0] = x;
    }
    for (int y = 0; y <= lenY; y++)
    {
      ed[0][y] = y;
    }
    int x;
    int xBegin;
    int xEnd;
    int y;
    int yBegin;
    int yEnd;
    char cX;
    char cY;

    int minLength;
    if (lenX < lenY)
    {
      minLength = lenX;
    }
    else
    {
      minLength = lenY;
    }
    for (x = 0; x < minLength; x++)
    {
      if (sX.charAt(x) != sY.charAt(x))
      {
        break;
      }
    }
    xBegin = yBegin = x;
    for (y = 1; y < minLength - yBegin; y++)
    {
      if (sX.charAt(lenX - y) != sY.charAt(lenY - y))
      {
        break;
      }
    }
    xEnd = lenX - y + 1;
    yEnd = lenY - y + 1;

    if (xBegin == xEnd)
    {
      minED = yEnd - yBegin;
    }
    else if (yBegin == yEnd)
    {
      minED = xEnd - xBegin;
    }
    else
    {
      int xDelta = xEnd - xBegin;
      int yDelta = yEnd - yBegin;
      if (xDelta - yDelta > stopED)
      {
        minED = stopED + 1;
      }
      else
      {
        int min = 0;
        for (x = 0; x < xDelta; x++)
        {
          if (min >= stopED)
          {
            ed[xDelta][yDelta] = stopED + 1;
            break;
          }
          cX = sX.charAt(xBegin + x);
          min = stopED;
          for (y = 0; y < yDelta; y++)
          {
            cY = sY.charAt(yBegin + y);
            int e;
            if (cX == cY)
            {
              e = ed[x][y];
            }
            else
            {
              if (x > 0 && y > 0 && cY == sX.charAt(xBegin + x - 1) &&
                  cX == sY.charAt(yBegin + y - 1))
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
            if (e < min)
            {
              min = e;
            }
          }
        }
        minED = ed[xDelta][yDelta];
      }
    }
    return minED;
  }

  public static int getED(String word1, String word2)
  {
    String sX, sY;
    int lenX;
    int lenY;
    int len1 = word1.length();
    int len2 = word2.length();
    if (len1 < len2)
    {
      sX = word2;
      lenX = len2;
      sY = word1;
      lenY = len1;
    }
    else
    {
      sX = word1;
      lenX = len1;
      sY = word2;
      lenY = len2;
    }
    int minED;
    int[][] ed = new int[lenX + 1][lenY + 1];
    for (int x = 0; x <= lenX; x++)
    {
      ed[x][0] = x;
    }
    for (int y = 0; y <= lenY; y++)
    {
      ed[0][y] = y;
    }
    int x;
    int xBegin;
    int xEnd;
    int y;
    int yBegin;
    int yEnd;
    char cX;
    char cY;

    int minLength;
    if (lenX < lenY)
    {
      minLength = lenX;
    }
    else
    {
      minLength = lenY;
    }
    for (x = 0; x < minLength; x++)
    {
      if (sX.charAt(x) != sY.charAt(x))
      {
        break;
      }
    }
    xBegin = yBegin = x;
    for (y = 1; y < minLength - yBegin; y++)
    {
      if (sX.charAt(lenX - y) != sY.charAt(lenY - y))
      {
        break;
      }
    }
    xEnd = lenX - y + 1;
    yEnd = lenY - y + 1;

    if (xBegin == xEnd)
    {
      minED = yEnd - yBegin;
    }
    else if (yBegin == yEnd)
    {
      minED = xEnd - xBegin;
    }
    else
    {
      int xDelta = xEnd - xBegin;
      int yDelta = yEnd - yBegin;
      for (x = 0; x < xDelta; x++)
      {
        cX = sX.charAt(xBegin + x);
        for (y = 0; y < yDelta; y++)
        {
          cY = sY.charAt(yBegin + y);
          int e;
          if (cX == cY)
          {
            e = ed[x][y];
          }
          else
          {
            e = ed[x][y];
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
        }
      }
      minED = ed[xDelta][yDelta];
    }
    return minED;
  }

}
