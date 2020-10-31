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
package com.softcorporation.suggester.language.fuzzy;

import java.util.*;

import com.softcorporation.util.StringUtil;
import com.softcorporation.util.Logger;

/**
 * 
 * @version: $Revision: 1.0 $
 */
public class Matcher extends MatcherConfiguration
{
  TreeMap phonemeMap = new TreeMap();
  private static TreeMap matchers = new TreeMap();

  public static Matcher getMatcher(String langCode)
  {
    Matcher matcher = (Matcher) matchers.get(langCode);
    if (matcher == null)
    {
      matcher = new Matcher("/com/softcorporation/suggester/language/" + langCode + ".config");
      if (!langCode.equals(matcher.LANGUAGE))
      {
        Logger.getLogger().logError("Invalid Fuzzy configuration: " + langCode);
      }
      matchers.put(langCode, matcher);
    }
    return matcher;
  }

  /**
   * Language.
   */
//  public String LANGUAGE;

  /*
   * Constructor with properties file name
   *
   * @param fileName the file name of the configuration properties file.
   */
  public Matcher(String fileName)
  {
    load(fileName, "UTF-8");
  }

  /*
   * Gets phonemes map
   */
  public TreeMap getPhonemeMap()
  {
    return phonemeMap;
  }

  public void save()
  {
  }

  /*
   * Custom parameters initialization
   */
  public void initialize()
  {
    Properties properties = getProperties();
    // set default request parameters
    for (Enumeration e = properties.keys(); e.hasMoreElements(); )
    {
      String key = (String) e.nextElement();
      String val = (String) properties.get(key);
      if (key.equals("LANGUAGE"))
      {
        LANGUAGE = val;
        continue;
      }
      else if (key.length() == 0)
      {
        continue;
      }
      if (val == null)
      {
        val = "";
      }
      int j = val.indexOf("#");
      if (j >= 0)
      {
        val = val.substring(0, j).trim();
      }
      ArrayList al = new ArrayList();
      Phoneme phoneme = new Phoneme();
      phoneme.value = key;
      phoneme.weight = 100;
      al.add(phoneme);
      StringTokenizer st = new StringTokenizer(val, ",");
      while (st.hasMoreTokens())
      {
        String sPhoneme = null;
        int weight = 0;
        String s = st.nextToken();
        int k = s.indexOf(":");
        if (k >= 0)
        {
          sPhoneme = s.substring(0, k).trim();
          String sWeight = s.substring(k + 1).trim();
          weight = StringUtil.parseInt(sWeight);
        }
        else
        {
          sPhoneme = s.trim();
          weight = 100;
        }
        if (sPhoneme.length() == 0 || weight <= 0 || weight > 100)
        {
          continue;
        }
        phoneme = new Phoneme();
        phoneme.value = sPhoneme;
        phoneme.weight = weight;
        if (!al.contains(phoneme))
        {
          al.add(phoneme);
        }
      }
      Phoneme[] pa = new Phoneme[al.size()];
      for (int i = 0; i < al.size(); i++)
      {
        pa[i] = (Phoneme) al.get(i);
      }
      phonemeMap.put(key, pa);
    }
  }

  /*
   * Gets word phonemes
   */
  public Phoneme[][] getPhonemes(String word)
  {
    Phoneme[][] phonemeArray = new Phoneme[word.length()][];
    Iterator iterator = phonemeMap.values().iterator();
    while (iterator.hasNext())
    {
      Phoneme[] phonemes = (Phoneme[]) iterator.next();
      String sPhoneme = phonemes[0].value;
      int p = -1;
      while ((p = word.indexOf(sPhoneme, p + 1)) >= 0)
      {
        Phoneme[] phonemes1 = phonemeArray[p]; 
        if (phonemes1 == null)
        {
          phonemeArray[p] = phonemes;
        }
        else
        {
          Phoneme[] phonemes2 = new Phoneme[phonemes1.length + phonemes.length];
          System.arraycopy(phonemes1, 0, phonemes2, 0, phonemes1.length);
          System.arraycopy(phonemes, 0, phonemes2, phonemes1.length, phonemes.length);
          phonemeArray[p] = phonemes2;
        }
      }
    }
    return phonemeArray;
  }

  /*
   * Gets word weight
   */
  public int getWeight(String word, String word2, Phoneme[][] phonemes)
  {
    return (getWeight(word2, phonemes) + getWeight(word, getPhonemes(word2))) / 2;
  }

  /*
   * Gets word weight
   */
  public int getWeight(String word, Phoneme[][] phonemes)
  {
    int[] pw = new int[phonemes.length];
    int wordLength = word.length();
    int delta = wordLength - phonemes.length;
    for (int i = 0; i < phonemes.length; i++)
    {
      Phoneme[] posPhonemes = phonemes[i];
      if (posPhonemes == null)
      {
        continue;
      }
      int pos1 = i - 1; // prev position (- 1 inclusive)
      int pos2 = i + 2; // next position (+ 2 exclusive)
      if (wordLength != phonemes.length)
      {
        if (delta > 0)
        {
          pos2 += delta;
        }
        else
        {
          pos1 += delta;
        }
      }
      if (pos1 < 0)
      {
        pos1 = 0;
      }
      else if (pos1 >= wordLength)
      {
        break;
      }
      if (pos2 > wordLength)
      {
        pos2 = wordLength;
      }
      String subWord = word.substring(pos1, pos2);
      for (int j = 0; j < posPhonemes.length; j++)
      {
        Phoneme phoneme = posPhonemes[j];    
        if (subWord.indexOf(phoneme.value) >= 0)
        {
          if (pw[i] < phoneme.weight)
          {
            pw[i] = phoneme.weight;
            if (phoneme.weight == 100)
            {
              break;
            }
          }
        }
      }
    }
    int weight = 0;
    for (int i = 0; i < pw.length; i++)
    {
      weight += pw[i];
    }
    weight = weight / pw.length;
    return weight;
  }

  /**
   * Creates a printable representation of phoneme array.
   * @return String representing phoneme array.
   */
  public static String toString(Phoneme[][] phonemes)
  {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < phonemes.length; i++)
    {
      buf.append(i);
      buf.append("=");
      if (phonemes[i] == null)
      {
        buf.append("null");
      }
      else
      {
        for (int j = 0; j < phonemes[i].length; j++)
        {
          if (j > 0)
          {
            buf.append(",");
          }
          buf.append(phonemes[i][j].toString());
        }
      }
      buf.append(" ");
    }
    return buf.toString();
  }
  
}
