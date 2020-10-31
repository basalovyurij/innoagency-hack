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

/**
 *
 * @version: $Revision:   1.0  $
 */
public class Word
{
  public static final int TYPE_CASE_LOWER        = 1;     // L
  public static final int TYPE_CASE_UPPER        = 2;     // U
  public static final int TYPE_CASE_MIXED        = 4;     // M
  public static final int TYPE_CASE_CAPITALIZED  = 8;     // C
  public static final int TYPE_CHAR_DIGIT        = 16;    // D
  public static final int TYPE_CHAR_INET         = 32;    // I
  public static final int TYPE_CHAR_FILE         = 64;    // F
  public static final int TYPE_CHAR_NON_LETTER   = 128;   // N
  public static final int TYPE_PHRASE_FIRST      = 256;   // P
  public static final int TYPE_PHRASE_LAST       = 512;   // T
  public static final int TYPE_LINE_FIRST        = 1024;  // S
  public static final int TYPE_LINE_LAST         = 2048;  // E
  public static final int TYPE_LINE_BREAK        = 4096;  // B
  public static final int TYPE_JOINED            = 8192;  // J

  public int offset;
  public int length;
  public int type;

  public boolean isType(int pattern)
  {
    if ((pattern & type) > 0)
    {
      return true;
    }
    return false;
  }

  public void addType(int pattern)
  {
    type |= pattern;
  }

  public boolean isParsed()
  {
    if (type == 0)
    {
      return true;
    }
    return false;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append('(');
    sb.append(offset);
    sb.append('-');
    sb.append(offset + length);
    sb.append(' ');
    sb.append(typeToString());
    sb.append(')');
    return sb.toString();
  }
/*
  public static final int TYPE_CASE_LOWER        = 1;     // L
  public static final int TYPE_CASE_UPPER        = 2;     // U
  public static final int TYPE_CASE_MIXED        = 4;     // M
  public static final int TYPE_CASE_CAPITALIZED  = 8;     // C
  public static final int TYPE_CHAR_DIGIT        = 16;    // D
  public static final int TYPE_CHAR_INET         = 32;    // I
  public static final int TYPE_CHAR_FILE         = 64;    // F
  public static final int TYPE_CHAR_NON_LETTER   = 128;   // N
  public static final int TYPE_PHRASE_FIRST      = 256;   // P
  public static final int TYPE_PHRASE_LAST       = 512;   // E
  public static final int TYPE_LINE_FIRST        = 2048;  // S
  public static final int TYPE_LINE_LAST         = 4096;  // R
  public static final int TYPE_LINE_BREAK        = 1024;  // B
  public static final int TYPE_JOINED            = 8192;  // J
*/
  public String typeToString()
  {
    StringBuffer sb = new StringBuffer();
//    sb.append('(');
    if ((type & Word.TYPE_CASE_LOWER) != 0)
    {
      sb.append('L');
    }
    if ((type & Word.TYPE_CASE_UPPER) != 0)
    {
      sb.append('U');
    }
    if ((type & Word.TYPE_CASE_MIXED) != 0)
    {
      sb.append('M');
    }
    if ((type & Word.TYPE_CASE_CAPITALIZED) != 0)
    {
      sb.append('C');
    }
    if ((type & Word.TYPE_CHAR_DIGIT) != 0)
    {
      sb.append('D');
    }
    if ((type & Word.TYPE_CHAR_INET) != 0)
    {
      sb.append('I');
    }
    if ((type & Word.TYPE_CHAR_FILE) != 0)
    {
      sb.append('F');
    }
    if ((type & Word.TYPE_CHAR_NON_LETTER) != 0)
    {
      sb.append('N');
    }
    if ((type & Word.TYPE_PHRASE_FIRST) != 0)
    {
      sb.append('P');
    }
    if ((type & Word.TYPE_PHRASE_LAST) != 0)
    {
      sb.append('E');
    }
    if ((type & Word.TYPE_LINE_FIRST) != 0)
    {
      sb.append('S');
    }
    if ((type & Word.TYPE_LINE_LAST) != 0)
    {
      sb.append('R');
    }
    if ((type & Word.TYPE_LINE_BREAK) != 0)
    {
      sb.append('B');
    }
    if ((type & Word.TYPE_JOINED) != 0)
    {
      sb.append('J');
    }
//    sb.append(')');
    return sb.toString();
  }

}
