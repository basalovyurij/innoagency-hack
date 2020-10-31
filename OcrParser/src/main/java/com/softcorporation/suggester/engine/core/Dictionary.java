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
package com.softcorporation.suggester.engine.core;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SuggesterException;

public abstract class Dictionary
{
  protected int[] dictionaryArray;
  protected String dictionaryName;

  protected int totalStates;
  protected int totalWords;

  public String version;
  public String license;
  public String date;
  public String language;
  public String rules;

  public static final String IND_I = ".i.ind";
  public static final String IND_C = ".c.ind";
  public static final String IND_T = ".t.ind";
  public static final String IND_L = ".l.ind";

  public static final String I_TMP = ".i.tmp";
  public static final String C_TMP = ".c.tmp";
  public static final String T_TMP = ".t.tmp";
  public static final String L_TMP = ".l.tmp";

  public static final String SFT = "sft=";
  public static final String VER = "ver=";
  public static final String TYP = "typ=";
  public static final String LIC = "lic=";
  public static final String DAT = "dat=";
  public static final String LNG = "lng=";
  public static final String RUL = "rul=";
  public static final String INF = "inf=";

  int offset;
  int currentType;
  int posPrint;
  char[] chars;

  Writer writer;
  BufferedReader brInfo = null;
  InputStreamReader inChar = null;
  InputStream inType = null;
  InputStream inLink = null;

  public Dictionary()
  {
  }

  public String getName()
  {
    return dictionaryName;
  }

  public int getTotalWords()
  {
    return totalWords;
  }

  public int getTotalStates()
  {
    return totalStates;
  }

  public int[] getDictionaryArray()
  {
    return dictionaryArray;
  }

  public void setDictionaryArray(int[] dictionaryArray)
  {
    this.dictionaryArray = dictionaryArray;
  }

//  public abstract void load(String fileName) throws SuggesterException;

//  public abstract boolean contains(String text);

  abstract public String getDictionaryType();

  /**
   * Extract default dictionary name and load dictionary from file or resource
   */
  public void load(String fileName) throws SuggesterException
  {
    if (fileName == null || fileName.trim().length() == 0)
    {
      throw new SuggesterException(
          "Cannot load dictionary. Invalid (null) file name");
    }
    String dictName = fileName;
    int i = dictName.lastIndexOf("/");
    if (i >= 0)
    {
      dictName = dictName.substring(i + 1);
    }
    i = dictName.lastIndexOf("\\");
    if (i >= 0)
    {
      dictName = dictName.substring(i + 1);
    }
    if (dictName.endsWith(".zip") || dictName.endsWith(".jar") ||
        dictName.endsWith(".ind"))
    {
      dictName = dictName.substring(0, dictName.length() - 4);
    }
    load(fileName, dictName);
  }

  /**
   * Load dictionary from file or resource
   */
  public synchronized void load(String fileName, String dictName) throws
      SuggesterException
  {
    if (fileName == null || fileName.trim().length() == 0)
    {
      throw new SuggesterException(
          "Cannot load dictionary. Invalid (null) file name");
    }
    if (dictName == null || dictName.trim().length() == 0)
    {
      throw new SuggesterException(
          "Cannot load dictionary. Invalid (null) dictionary name");
    }
    dictionaryName = dictName;

    ZipFile zipFile = null;
    LinkedList ll = new LinkedList();
    currentType = -1;
    offset = 0;
    try
    {
      if (fileName.startsWith("file://")) // file
      {
        fileName = fileName.substring(7);
        if (fileName.trim().length() == 0)
        {
          throw new SuggesterException(
              "Cannot load dictionary. Invalid (empty) file name");
        }
        File file = new File(fileName);
        fileName = file.getCanonicalPath();
        if (!file.exists())
        {
          throw new SuggesterException("Dictionary file: " + fileName +
                                       " does not exist.");
        }
        zipFile = new ZipFile(file);
        ZipEntry ze = zipFile.getEntry(dictName + IND_I);
        if (ze == null)
        {
          throw new SuggesterException("Invalid dictionary: " + fileName);
        }
        brInfo = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze),
            Constants.CHARACTER_SET_ENCODING_DEFAULT));
        ze = zipFile.getEntry(dictName + IND_C);
        if (ze == null)
        {
          throw new SuggesterException("Invalid dictionary: " + fileName);
        }
        inChar = new InputStreamReader(zipFile.getInputStream(ze),
            Constants.CHARACTER_SET_ENCODING_DEFAULT);
        ze = zipFile.getEntry(dictName + IND_T);
        if (ze == null)
        {
          throw new SuggesterException("Invalid dictionary: " + fileName);
        }
        inType = zipFile.getInputStream(ze);
        ze = zipFile.getEntry(dictName + IND_L);
        if (ze == null)
        {
          throw new SuggesterException("Invalid dictionary: " + fileName);
        }
        inLink = zipFile.getInputStream(ze);
      }
      else // resource
      {
        if (!dictName.startsWith("/"))
        {
          dictName = "/" + dictName;
        }

        InputStream is = getClass().getResourceAsStream(dictName + IND_I);
        if (is == null)
        {
          throw new SuggesterException("Invalid dictionary: " + fileName);
        }
        brInfo = new BufferedReader(new InputStreamReader(is, Constants.CHARACTER_SET_ENCODING_DEFAULT));
        is = getClass().getResourceAsStream(dictName + IND_C);
        if (is == null)
        {
          throw new SuggesterException("Invalid dictionary: " + fileName);
        }
        inChar = new InputStreamReader(is, Constants.CHARACTER_SET_ENCODING_DEFAULT);
        inType = getClass().getResourceAsStream(dictName + IND_T);
        if (inType == null)
        {
          throw new SuggesterException("Invalid dictionary: " + fileName);
        }
        inLink = getClass().getResourceAsStream(dictName + IND_L);
        if (inLink == null)
        {
          throw new SuggesterException("Invalid dictionary: " + fileName);
        }
      }

      // read information
      String info;
      String software = null;
      int verDict = -1;
      while ( (info = brInfo.readLine()) != null)
      {
        if (info.startsWith(SFT))
        {
          software = info.substring(SFT.length());
        }
        else if (info.startsWith(VER))
        {
          String ver = info.substring(VER.length());
          int i = ver.indexOf(".");
          if (i > 0)
          {
            try
            {
              verDict = Integer.parseInt(ver.substring(0, i));
            }
            catch (NumberFormatException nfe)
            {
            }
          }
        }
        else if (info.startsWith(DAT))
        {
          date = info.substring(DAT.length());
        }
        else if (info.startsWith(LNG))
        {
          language = info.substring(LNG.length());
        }
        else if (info.startsWith(TYP))
        {
          if (getDictionaryType().indexOf(info.substring(TYP.length())) < 0)
          {
            throw new SuggesterException("Only " + getDictionaryType() + " suggester dictionary is accepted");
          }
        }
        else if (info.startsWith(LIC))
        {
          license = info.substring(LNG.length());
        }
        else if (info.startsWith(RUL))
        {
          rules = info.substring(RUL.length());
        }
      }
      if (software == null || !software.equalsIgnoreCase(Constants.SOFTWARE))
      {
        throw new SuggesterException("Invalid software.");
      }
      int verSugg = -1;
      int i = Constants.VERSION.indexOf(".");
      if (i > 0)
      {
        try
        {
          verSugg = Integer.parseInt(Constants.VERSION.substring(0, i));
        }
        catch (NumberFormatException nfe)
        {
        }
      }
      if (verDict < 0 || verDict > verSugg)
      {
        throw new SuggesterException("This dictionary version is newer then your current suggester version. Please update the software.");
      }

      // read number of words
      totalWords = (inLink.read() << 24) + (inLink.read() << 16) +
          (inLink.read() << 8) + inLink.read();

      // read number of states
      totalStates = (inLink.read() << 24) + (inLink.read() << 16) +
          (inLink.read() << 8) + inLink.read();

      if (totalStates <= 0 || totalWords <= 0)
      {
        throw new SuggesterException("Invalid index.");
      }

      dictionaryArray = new int[totalStates];
      offset = -1;
      ll.add(new Integer(0));

      while (ll.size() > 0)
      {
        int parentLink = ( (Integer) ll.get(0)).intValue();
        ll.remove(0);
        addOffset();
        dictionaryArray[offset] = dictionaryArray[parentLink];
        int type = dictionaryArray[offset] >> 16;
        if ( (type & Node.NODE_TYPE_LAST_Y) == 0)
        {
          dictionaryArray[parentLink] = offset;
        }
        else
        {
          dictionaryArray[parentLink] = -offset;
        }
        if ( (type & Node.NODE_TYPE_LAST_X) != 0)
        {
          continue;
        }

        while ( (type = readType()) >= 0)
        {
          addOffset();
          if ( (type & Node.NODE_TYPE_JUMP) == 0)
          {
            int c;
            if ( (c = inChar.read()) < 0)
            {
              break;
            }
            char chr = Character.toLowerCase( (char) c);
            if (chr != c)
            {
              c = chr;
              type |= Node.NODE_TYPE_CAPITAL;
            }
            dictionaryArray[offset] = (type << 16) | c;
            ll.add(new Integer(offset));
            if ( (type & Node.NODE_TYPE_LAST_Y) != 0)
            {
              break;
            }
          }
          else
          {
            int t = type & (Node.NODE_TYPE_END | Node.NODE_TYPE_LAST_X);
            int link;
            if (t == 0)
            {
              link = inLink.read();
            }
            else if (t == 1)
            {
              link = (inLink.read() << 8) + inLink.read();
            }
            else if (t == 2)
            {
              link = (inLink.read() << 16) + (inLink.read() << 8) + inLink.read();
            }
            else
            {
              link = (inLink.read() << 24) + (inLink.read() << 16) +
                  (inLink.read() << 8) + inLink.read();
            }
            if ( (type & Node.NODE_TYPE_LAST_Y) != 0)
            {
              dictionaryArray[offset] = -link;
              break;
            }
            else
            {
              dictionaryArray[offset] = link;
            }
          }
        }
      }
      brInfo.close();
      inChar.close();
      inType.close();
      inLink.close();
      if (zipFile != null)
      {
        zipFile.close();
      }
    }
    catch (IOException e)
    {
      if (brInfo != null)
      {
        try
        {
          brInfo.close();
        }
        catch (IOException eio)
        {}
      }
      if (inChar != null)
      {
        try
        {
          inChar.close();
        }
        catch (IOException eio)
        {}
      }
      if (inType != null)
      {
        try
        {
          inType.close();
        }
        catch (IOException eio)
        {}
      }
      if (inLink != null)
      {
        try
        {
          inLink.close();
        }
        catch (IOException eio)
        {}
      }
      if (zipFile != null)
      {
        try
        {
          zipFile.close();
        }
        catch (IOException eio)
        {}
      }
      throw new SuggesterException(e.toString());
    }
  }

  public boolean contains(String word)
  {
    return arrayContains(word);
  }

  private boolean arrayContains(String word)
  {
    int position = 0;
    int wl = word.length();
    for (int i = 0; i < wl; i++)
    {
      if ( (position = getChild(position, word.charAt(i))) == -1)
      {
        return false;
      }
    }
    int c = dictionaryArray[position];
    int type = c >> 16;
    if ( (type & Node.NODE_TYPE_END) != 0)
    {
      return true;
    }
    return false;
  }

  int getChild(int position, char character)
  {
    char cl = Character.toLowerCase(character);
    int c = dictionaryArray[position];
    int type = c >> 16;
    if ( (type & Node.NODE_TYPE_LAST_X) == 0)
    {
//	for (int pos = position + 1; pos < dictionaryArray.length; pos++)
      for (int pos = position + 1; ; pos++)
      {
        int lnk = dictionaryArray[pos];
        if (lnk > 0)
        {
          c = dictionaryArray[lnk];
          type = c >> 16;
          char chr = (char) c;
          if (chr < cl)
          {
            continue;
          }
          if (chr == cl)
          {
            if (character == cl) // lowercase
            {
              if ( (type & Node.NODE_TYPE_CAPITAL) == 0)
              {
                return lnk;
              }
            }
            else if ( (type & Node.NODE_TYPE_CAPITAL) != 0)
            {
              return lnk;
            }
          }
          else
          {
            return -1;
          }
        }
        else
        {
          c = dictionaryArray[ -lnk];
          type = c >> 16;
          char chr = (char) c;
          if (chr == cl)
          {
            if (character == cl) // lowercase
            {
              if ( (type & Node.NODE_TYPE_CAPITAL) == 0)
              {
                return -lnk;
              }
            }
            else if ( (type & Node.NODE_TYPE_CAPITAL) != 0)
            {
              return -lnk;
            }
          }
          return -1;
        }
      }
    }
    return -1;
  }

  private void addOffset() throws SuggesterException
  {
    if (++offset < 0)
    {
      throw new SuggesterException("Too many states.");
    }
  }

  private int readType() throws IOException
  {
    if (currentType < 0)
    {
      currentType = inType.read();
      if (currentType < 0)
      {
        return -1;
      }
      return (currentType >> 4);
    }
    int t = currentType & 0xf;
    currentType = -1;
    return t;
  }

  public synchronized void print(Writer w) throws SuggesterException
  {
    writer = w;
    posPrint = 0;
    chars = new char[Constants.WORD_LENGTH_MAX];
    try
    {
      traverse(0);
    }
    catch (IOException e)
    {
      throw new SuggesterException(e.toString());
    }
  }

  private void traverse(int position) throws IOException
  {
    int c = dictionaryArray[position];
    int type = c >> 16;
    char chr = (char) c;
    if ( (type & Node.NODE_TYPE_CAPITAL) != 0)
    {
      chars[posPrint] = Character.toUpperCase(chr);
    }
    else
    {
      chars[posPrint] = chr;
    }
    if ( (type & Node.NODE_TYPE_END) != 0)
    {
      for (int j = 1; j <= posPrint; j++)
      {
        writer.write(chars[j]);
      }
      writer.write("\r\n"); // next line
//	writer.flush();
    }
    if ( (type & Node.NODE_TYPE_LAST_X) == 0)
    {
      posPrint++;
      for (int pos = position + 1; pos < dictionaryArray.length; pos++)
//      for (int pos = position + 1; ; pos++)
      {
        int lnk = dictionaryArray[pos];
        if (lnk > 0)
        {
          traverse(lnk);
        }
        else
        {
          traverse( -lnk);
          break;
        }
      }
      posPrint--;
    }
  }

}
