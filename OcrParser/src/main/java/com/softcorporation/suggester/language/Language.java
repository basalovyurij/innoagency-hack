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
package com.softcorporation.suggester.language;

import java.util.*;

/**
 * 
 * @version: $Revision: 1.0 $
 */
public abstract class Language
{
  public String langCode;
  private static TreeMap languages = new TreeMap();

  private static final int FUZZY_SHORT_WORD_LEN = 7;

  public static Language getLanguage(String langCode)
  {
    return (Language) languages.get(langCode);
  }

  public abstract String getCloseChars(char chr);

  public int compareWords(String word1, String word2)
  {
    int weight = 0;
    int len1 = word1.length() - 1;
    int len2 = word2.length() - 1;
    if (len1 == len2)
    {
      if (len1 < FUZZY_SHORT_WORD_LEN)
      {
        for (int i = 0; i <= len1; i++)
        {
          weight +=
              countWeight(getCloseChars(word1.charAt(i)),
                          getCloseChars1(word2, i));
          weight +=
              countWeight(getCloseChars(word2.charAt(i)),
                          getCloseChars1(word1, i)); // << 2;
        }
      }
      else
      {
        for (int i = 0; i <= len1; i++)
        {
          weight +=
              countWeight(getCloseChars(word1.charAt(i)),
                          getCloseChars2(word2, i));
          weight +=
              countWeight(getCloseChars(word2.charAt(i)),
                          getCloseChars2(word1, i)); // << 2;
        }
      }
    }
    else
    {
      if (len2 < FUZZY_SHORT_WORD_LEN)
      {
        for (int i = 0; i <= len1; i++)
        {
          weight +=
              countWeight(getCloseChars(word1.charAt(i)),
                          getCloseChars1(word2, i));
          weight +=
              countWeight(getCloseChars(word1.charAt(len1 - i)),
                          getCloseChars1(word2, len2 - i));
        }
      }
      else
      {
        for (int i = 0; i <= len1; i++)
        {
          weight +=
              countWeight(getCloseChars(word1.charAt(i)),
                          getCloseChars2(word2, i));
          weight +=
              countWeight(getCloseChars(word1.charAt(len1 - i)),
                          getCloseChars2(word2, len2 - i));
        }
      }
      if (len1 < FUZZY_SHORT_WORD_LEN)
      {
        for (int i = 0; i <= len2; i++)
        {
          weight +=
              countWeight(getCloseChars(word2.charAt(i)),
                          getCloseChars1(word1, i));
          weight +=
              countWeight(getCloseChars(word2.charAt(len2 - i)),
                          getCloseChars1(word1, len1 - i));
        }
      }
      else
      {
        for (int i = 0; i <= len2; i++)
        {
          weight +=
              countWeight(getCloseChars(word2.charAt(i)),
                          getCloseChars2(word1, i));
          weight +=
              countWeight(getCloseChars(word2.charAt(len2 - i)),
                          getCloseChars2(word1, len1 - i));
        }
      }

    }
    return weight;
  }

  int countWeight(String chars1, String chars2)
  {
    if (chars2.indexOf(chars1.charAt(0), 0) >= 0)
    {
      return 0;
    }
    for (int i = 1; i < chars1.length(); i++)
    {
      if (chars2.indexOf(chars1.charAt(i), 0) >= 0)
      {
        return 1;
      }
    }
    return 2;
  }

  String getCloseChars1(String word, int position)
  {
    int wordLength = word.length();
    int pos1 = position - 1;
    int pos2 = position + 2;
    if (pos1 < 0)
    {
      pos1 = 0;
      pos2 = 2;
    }
    if (pos2 > wordLength)
    {
      pos1 = wordLength - 2;
      pos2 = wordLength;
      if (pos1 < 0)
      {
        pos1 = 0;
      }
    }
    return word.substring(pos1, pos2);
  }

  String getCloseChars2(String word, int position)
  {
    int wordLength = word.length();
    int pos1 = position - 2;
    int pos2 = position + 3;
    if (pos1 < 0)
    {
      if (pos1 < -1)
      {
        pos2 = 2;
      }
      else
      {
        pos2 = 3;
      }
      pos1 = 0;
    }
    if (pos2 > wordLength)
    {
      if (pos2 > wordLength + 1)
      {
        pos1 = wordLength - 2;
      }
      else
      {
        pos1 = wordLength - 3;
      }
      if (pos1 < 0)
      {
        pos1 = 0;
      }
      pos2 = wordLength;
    }
    return word.substring(pos1, pos2);
  }

}

/*

AA "Afar"
AB "Abkhazian"
AF "Afrikaans"
AM "Amharic"
AR "Arabic"
AS "Assamese"
AY "Aymara"
AZ "Azerbaijani"
BA "Bashkir"
BE "Byelorussian"
BG "Bulgarian"
BH "Bihari"
BI "Bislama"
BN "Bengali" "Bangla"
BO "Tibetan"
BR "Breton"
CA "Catalan"
CO "Corsican"
CS "Czech"
CY "Welsh"
DA "Danish"
DE "German"
DZ "Bhutani"
EL "Greek"
EN "English" "American"
EO "Esperanto"
ES "Spanish"
ET "Estonian"
EU "Basque"
FA "Persian"
FI "Finnish"
FJ "Fiji"
FO "Faeroese"
FR "French"
FY "Frisian"
GA "Irish"
GD "Gaelic" "Scots Gaelic"
GL "Galician"
GN "Guarani"
GU "Gujarati"
HA "Hausa"
HI "Hindi"
HR "Croatian"
HU "Hungarian"
HY "Armenian"
IA "Interlingua"
IE "Interlingue"
IK "Inupiak"
IN "Indonesian"
IS "Icelandic"
IT "Italian"
IW "Hebrew"
JA "Japanese"
JI "Yiddish"
JW "Javanese"
KA "Georgian"
KK "Kazakh"
KL "Greenlandic"
KM "Cambodian"
KN "Kannada"
KO "Korean"
KS "Kashmiri"
KU "Kurdish"
KY "Kirghiz"
LA "Latin"
LN "Lingala"
LO "Laothian"
LT "Lithuanian"
LV "Latvian" "Lettish"
MG "Malagasy"
MI "Maori"
MK "Macedonian"
ML "Malayalam"
MN "Mongolian"
MO "Moldavian"
MR "Marathi"
MS "Malay"
MT "Maltese"
MY "Burmese"
NA "Nauru"
NE "Nepali"
NL "Dutch"
NO "Norwegian"
OC "Occitan"
OM "Oromo" "Afan"
OR "Oriya"
PA "Punjabi"
PL "Polish"
PS "Pashto" "Pushto"
PT "Portuguese"
QU "Quechua"
RM "Rhaeto-Romance"
RN "Kirundi"
RO "Romanian"
RU "Russian"
RW "Kinyarwanda"
SA "Sanskrit"
SD "Sindhi"
SG "Sangro"
SH "Serbo-Croatian"
SI "Singhalese"
SK "Slovak"
SL "Slovenian"
SM "Samoan"
SN "Shona"
SO "Somali"
SQ "Albanian"
SR "Serbian"
SS "Siswati"
ST "Sesotho"
SU "Sudanese"
SV "Swedish"
SW "Swahili"
TA "Tamil"
TE "Tegulu"
TG "Tajik"
TH "Thai"
TI "Tigrinya"
TK "Turkmen"
TL "Tagalog"
TN "Setswana"
TO "Tonga"
TR "Turkish"
TS "Tsonga"
TT "Tatar"
TW "Twi"
UK "Ukrainian"
UR "Urdu"
UZ "Uzbek"
VI "Vietnamese"
VO "Volapuk"
WO "Wolof"
XH "Xhosa"
YO "Yoruba"
ZH "Chinese"
ZU "Zulu"

*/