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

/**
 * <code>Phoneme</code> - The phoneme class.
 * <p>
 * @version   1.0 02/22/2006
 */
public class Phoneme implements Comparable
{
  /**
   * Phoneme value
   */
  String value;

  /**
   * Phoneme weight
   */
  int weight;

  /**
   * Compares this string to the specified object.
   * The result is <code>true</code> if and only if the argument is not
   * <code>null</code> and is a <code>String</code> object that represents
   * the same sequence of characters as this object.
   *
   * @param   anObject   the object to compare this <code>String</code>
   *                     against.
   * @return  <code>true</code> if the <code>String </code>are equal;
   *          <code>false</code> otherwise.
   * @see     java.lang.String#compareTo(java.lang.String)
   * @see     java.lang.String#equalsIgnoreCase(java.lang.String)
   */
  public boolean equals(Object anObject)
  {
    Phoneme anotherPhoneme = (Phoneme) anObject;
    if (value.equals(anotherPhoneme.value))
    {
      return true;
    }
    return false;
  }

  /**
    * Compares two phonemes.
    * @param   anotherPhoneme   the <code>Phoneme</code> to be compared.
    * @return  the value <code>0</code> if the argument phoneme is equal to
    *          this phoneme; a value less than <code>0</code> if this phoneme
    *          is less than the phoneme argument; and a
    *          value greater than <code>0</code> if this phoneme is
    *          greater than the phoneme argument.
    */
   public int compareTo(Object anObject)
   {
     Phoneme anotherPhoneme = (Phoneme) anObject;
     return value.compareTo(anotherPhoneme.value);
   }

   /**
    * Creates a printable representation of this object.
    * @return String representing this object.
    */
   public String toString()
   {
     StringBuffer buf = new StringBuffer();
     buf.append(value);
     buf.append(":");
     buf.append(weight);
     return buf.toString();
   }

}
