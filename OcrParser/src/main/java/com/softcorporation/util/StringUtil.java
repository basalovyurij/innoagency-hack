/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softcorporation.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class StringUtil {
  private static MessageDigest md5;
  
  private static final String DELIMITERS = " \n\r\t,.()!{}[]<>#$%*_|\\/?:;=+";
  
  private static final String BR_SEPARATORS = " \n\r\t,.()!?:;=+-";
  
  private static final String CSV_SEPARATOR = ";";
  
  private static final String URL_DELIMITERS = "&?=/-";
  
  public static String arrayToString(Object[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i].toString());
        if (i < len)
          buf.append(","); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(String[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(","); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(int[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(", "); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(float[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(", "); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(long[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(", "); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(double[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(", "); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(short[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(", "); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(byte[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(", "); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(char[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(","); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String arrayToString(boolean[] array) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (array != null) {
      int len = array.length - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(array[i]);
        if (i < len)
          buf.append(", "); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String vectorToString(Vector vector) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (vector != null) {
      int len = vector.size() - 1;
      for (int i = 0; i <= len; i++) {
        buf.append(String.valueOf(vector.elementAt(i)));
        if (i < len)
          buf.append(","); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String listToString(List list) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (list != null) {
      Iterator iterator = list.iterator();
      while (iterator.hasNext()) {
        buf.append(String.valueOf(iterator.next()));
        if (iterator.hasNext())
          buf.append(","); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String setToString(Set set) {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    if (set != null) {
      Iterator iterator = set.iterator();
      while (iterator.hasNext()) {
        buf.append(String.valueOf(iterator.next()));
        if (iterator.hasNext())
          buf.append(","); 
      } 
    } 
    buf.append("]");
    return buf.toString();
  }
  
  public static String toString(Object obj) {
    if (obj == null)
      return ""; 
    if (obj instanceof String || obj instanceof Integer)
      return String.valueOf(obj); 
    if (obj instanceof int[])
      return arrayToString((int[])obj); 
    if (obj instanceof float[])
      return arrayToString((float[])obj); 
    if (obj instanceof String[])
      return arrayToString((String[])obj); 
    if (obj instanceof long[])
      return arrayToString((long[])obj); 
    if (obj instanceof double[])
      return arrayToString((double[])obj); 
    if (obj instanceof byte[])
      return arrayToString((byte[])obj); 
    if (obj instanceof short[])
      return arrayToString((short[])obj); 
    if (obj instanceof boolean[])
      return arrayToString((boolean[])obj); 
    if (obj instanceof char[])
      return arrayToString((char[])obj); 
    if (obj instanceof Vector)
      return vectorToString((Vector)obj); 
    if (obj instanceof ArrayList)
      return listToString((ArrayList)obj); 
    if (obj instanceof TreeSet)
      return setToString((TreeSet)obj); 
    return String.valueOf(obj);
  }
  
  public static int parseInt(String numString) {
    int res;
    try {
      res = Integer.parseInt(numString);
    } catch (NumberFormatException nfe) {
      res = 0;
    } 
    return res;
  }
  
  public static String replace(String src, String[] s1, String[] s2) {
    StringBuffer sb = new StringBuffer();
    int curPos = 0;
    while (true) {
      int minPos = src.length();
      int minIndex = -1;
      for (int i = 0; i < s1.length; i++) {
        int pos = src.indexOf(s1[i], curPos);
        if (pos >= 0 && pos < minPos) {
          minPos = pos;
          minIndex = i;
        } 
      } 
      if (minIndex < 0)
        break; 
      sb.append(src.substring(curPos, minPos));
      sb.append(s2[minIndex]);
      curPos = minPos + s1[minIndex].length();
    } 
    sb.append(src.substring(curPos));
    return sb.toString();
  }
  
  public static String replace(String source, char chr, String str) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < source.length(); i++) {
      char c = source.charAt(i);
      if (c == chr) {
        sb.append(str);
      } else {
        sb.append(c);
      } 
    } 
    return sb.toString();
  }
  
  public static String replace(String source, String str1, String str2) {
    StringBuffer buf = new StringBuffer();
    int i0 = str1.length();
    int i1 = 0;
    int i2;
    while ((i2 = source.indexOf(str1, i1)) >= 0) {
      buf.append(source.substring(i1, i2));
      buf.append(str2);
      i1 = i2 + i0;
    } 
    if (i1 < source.length())
      buf.append(source.substring(i1)); 
    return buf.toString();
  }
  
  public static String replaceAll(String source, String str1, String str2) {
    boolean found = true;
    while (found) {
      found = false;
      StringBuffer buf = new StringBuffer();
      int i0 = str1.length();
      int i1 = 0;
      int i2;
      while ((i2 = source.indexOf(str1, i1)) >= 0) {
        found = true;
        buf.append(source.substring(i1, i2));
        buf.append(str2);
        i1 = i2 + i0;
      } 
      if (i1 < source.length())
        buf.append(source.substring(i1)); 
      source = buf.toString();
    } 
    return source;
  }
  
  public static String encodeChar(char c) {
    StringBuffer sb = new StringBuffer(6);
    sb.append("\\u");
    int i4 = c & 0xF;
    c = (char)(c >> 4);
    int i3 = c & 0xF;
    c = (char)(c >> 4);
    int i2 = c & 0xF;
    c = (char)(c >> 4);
    int i1 = c & 0xF;
    sb.append("0123456789abcdef".charAt(i1));
    sb.append("0123456789abcdef".charAt(i2));
    sb.append("0123456789abcdef".charAt(i3));
    sb.append("0123456789abcdef".charAt(i4));
    return sb.toString();
  }
  
  public static String noNull(String text) {
    if (text == null)
      return ""; 
    return text;
  }
  
  public static String getStackTrace(Throwable throwable) {
    Writer writer = new StringWriter();
    throwable.printStackTrace(new PrintWriter(writer));
    return writer.toString();
  }
  
  public static long getMemory() {
    try {
      System.gc();
      Thread.yield();
      System.gc();
      Thread.sleep(100L);
    } catch (Exception exception) {}
    Runtime runtime = Runtime.getRuntime();
    return runtime.totalMemory() - runtime.freeMemory();
  }
  
  public static synchronized String getMD5(String input) {
    byte[] bytes = input.getBytes();
    try {
      if (md5 == null)
        md5 = MessageDigest.getInstance("MD5"); 
      md5.update(bytes);
      byte[] digest = md5.digest();
      StringBuffer hexString = new StringBuffer(32);
      for (int i = 0; i < digest.length; i++) {
        hexString.append("0123456789abcdef".charAt(0xF & digest[i] >> 4));
        hexString.append("0123456789abcdef".charAt(0xF & digest[i]));
      } 
      return hexString.toString();
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      return null;
    } 
  }
}
