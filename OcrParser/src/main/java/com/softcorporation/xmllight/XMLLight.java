/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softcorporation.xmllight;

import java.io.PrintStream;

public class XMLLight {
  private static final String DEBUG_MSG = "XMLLight DEBUG: ";
  
  private static PrintStream out = System.out;
  
  private static final byte STATUS_UNSET = 0;
  
  private static final byte STATUS_TAG_NAME = 1;
  
  private static final byte STATUS_TAG_SPACE = 2;
  
  private static final byte STATUS_TAG_ATTR_NAME = 3;
  
  private static final byte STATUS_TAG_ATTR_EQ1 = 4;
  
  private static final byte STATUS_TAG_ATTR_EQ2 = 5;
  
  private static final byte STATUS_TAG_ATTR_VAL = 6;
  
  private static final byte STATUS_TAG_ATTR_END = 7;
  
  private static final byte STATUS_TAG_CLOSING = 8;
  
  private static final byte STATUS_TAG_CLOSED = 9;
  
  private static final byte STATUS_TAG_END = 10;
  
  public static boolean debug;
  
  public static String XML_DEFINITION = "<?xml version=\"1.0\"?>";
  
  public static void setLogWriter(PrintStream output) {
    synchronized (out) {
      out.flush();
      out = output;
    } 
  }
  
  public static void writeLog(String log) {
    if (debug)
      synchronized (out) {
        out.print("XMLLight DEBUG: ");
        out.println(log);
      }  
  }
  
  public static void writeLog(Exception ex) {
    if (debug)
      synchronized (out) {
        out.print("XMLLight DEBUG: ");
        ex.printStackTrace(out);
      }  
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
    return new String(sb);
  }
  
  public static String encode(String value) {
    return encode(value, false);
  }
  
  public static String encode(String value, boolean canonical) {
    if (value == null)
      return null; 
    StringBuffer sb = new StringBuffer();
    int len = value.length();
    for (int i = 0; i < len; i++) {
      char ch = value.charAt(i);
      switch (ch) {
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '&':
          sb.append("&amp;");
          break;
        case '"':
          sb.append("&quot;");
          break;
        case '\'':
          sb.append("&apos;");
          break;
        case '\n':
        case '\r':
          if (canonical) {
            sb.append("&#");
            sb.append(Integer.toString(ch));
            sb.append(';');
            break;
          } 
        default:
          sb.append(ch);
          break;
      } 
    } 
    return new String(sb);
  }
  
  public static String decode(String s) {
    if (s == null)
      return null; 
    StringBuffer sb = new StringBuffer();
    int i = 0, i1 = 0;
    while (i < s.length()) {
      char c = Character.MIN_VALUE;
      i1 = s.indexOf('&', i);
      if (i1 >= 0) {
        sb.append(s.substring(i, i1));
        i = i1 + 1;
        i1 = s.indexOf(';', i);
        if (i1 >= 0) {
          String s1 = s.substring(i, i1);
          i = i1 + 1;
          if (s1.equals("amp")) {
            c = '&';
          } else if (s1.equals("lt")) {
            c = '<';
          } else if (s1.equals("gt")) {
            c = '>';
          } else if (s1.equals("quot")) {
            c = '"';
          } else if (s1.equals("apos")) {
            c = '\'';
          } else if (s1.equals("nbsp")) {
            c = ' ';
          } else if (s1.length() > 0 && s1.charAt(0) == '#') {
            try {
              c = (char)Integer.parseInt(s1.substring(1, s1.length()));
            } catch (NumberFormatException e) {
              return null;
            } 
          } else {
            return null;
          } 
        } else {
          return null;
        } 
        sb.append(c);
        continue;
      } 
      sb.append(s.substring(i, s.length()));
      break;
    } 
    return new String(sb);
  }
  
  public static String clearComments(String doc) {
    if (doc == null)
      return null; 
    StringBuffer sb = new StringBuffer();
    int i1 = 0;
    int i2 = 0;
    while (i1 >= 0) {
      i1 = doc.indexOf("<!--", i2);
      if (i1 < 0) {
        sb.append(doc.substring(i2));
        continue;
      } 
      if (i1 > i2)
        sb.append(doc.substring(i2, i1)); 
      i2 = doc.indexOf("-->", i1);
      if (i2 < 0)
        break; 
      i2 += 3;
    } 
    return new String(sb);
  }
  
  public static String getXMLDocument(Element elem) {
    return String.valueOf(XML_DEFINITION) + "\n" + elem.toString();
  }
  
  public static Element getElem(String doc) throws XMLLightException {
    return getElem(doc, 0, true);
  }
  
  public static Element getElem(String doc, boolean validate) throws XMLLightException {
    return getElem(doc, 0, validate);
  }
  
  public static Element getElem(String doc, int start, boolean validate) throws XMLLightException {
    Element elem = new Element();
    elem.validate = validate;
    elem.setElem(doc, start);
    return elem;
  }
  
  public static Element getElem(String doc, String elemName) throws XMLLightException {
    return getElem(doc, elemName, 0, true);
  }
  
  public static Element getElem(String doc, String elemName, boolean validate) throws XMLLightException {
    return getElem(doc, elemName, 0, validate);
  }
  
  public static Element getElem(String doc, String tag, int start, boolean validate) throws XMLLightException {
    Element elem = new Element();
    elem.validate = validate;
    elem.setElem(doc, tag, start);
    return elem;
  }
  
  public static int findOpeningTag(String doc, String tag, int[] pos, boolean validate) throws XMLLightException {
    throw new UnsupportedOperationException();
    // Byte code:
    //   0: new java/lang/StringBuffer
    //   3: dup
    //   4: ldc '<'
    //   6: invokespecial <init> : (Ljava/lang/String;)V
    //   9: aload_1
    //   10: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   13: invokevirtual toString : ()Ljava/lang/String;
    //   16: astore #4
    //   18: aload_1
    //   19: invokevirtual length : ()I
    //   22: istore #5
    //   24: aload_2
    //   25: iconst_1
    //   26: aload_2
    //   27: iconst_2
    //   28: iconst_m1
    //   29: dup_x2
    //   30: iastore
    //   31: dup_x2
    //   32: iastore
    //   33: istore #6
    //   35: aload_2
    //   36: iconst_0
    //   37: iaload
    //   38: istore #7
    //   40: iconst_m1
    //   41: istore #9
    //   43: aload_0
    //   44: aload #4
    //   46: iload #7
    //   48: invokevirtual indexOf : (Ljava/lang/String;I)I
    //   51: istore #6
    //   53: iload #6
    //   55: ifge -> 60
    //   58: iconst_m1
    //   59: ireturn
    //   60: iload #6
    //   62: iload #5
    //   64: iadd
    //   65: iconst_1
    //   66: iadd
    //   67: istore #7
    //   69: aload_0
    //   70: iload #7
    //   72: invokevirtual charAt : (I)C
    //   75: istore #8
    //   77: iload #8
    //   79: invokestatic isWhitespace : (C)Z
    //   82: ifeq -> 110
    //   85: aload_0
    //   86: iinc #7, 1
    //   89: iload #7
    //   91: invokestatic skipSpaces : (Ljava/lang/String;I)I
    //   94: istore #7
    //   96: aload_0
    //   97: iload #7
    //   99: invokevirtual charAt : (I)C
    //   102: istore #8
    //   104: iconst_0
    //   105: istore #9
    //   107: goto -> 127
    //   110: iload #8
    //   112: bipush #62
    //   114: if_icmpeq -> 124
    //   117: iload #8
    //   119: bipush #47
    //   121: if_icmpne -> 43
    //   124: iconst_0
    //   125: istore #9
    //   127: iconst_m1
    //   128: istore #10
    //   130: goto -> 241
    //   133: iload #10
    //   135: iconst_m1
    //   136: if_icmpeq -> 152
    //   139: iload #8
    //   141: iload #10
    //   143: if_icmpne -> 230
    //   146: iconst_m1
    //   147: istore #10
    //   149: goto -> 230
    //   152: iload #8
    //   154: bipush #47
    //   156: if_icmpne -> 199
    //   159: aload_0
    //   160: iinc #7, 1
    //   163: iload #7
    //   165: invokevirtual charAt : (I)C
    //   168: istore #8
    //   170: iload #8
    //   172: bipush #62
    //   174: if_icmpne -> 183
    //   177: iconst_1
    //   178: istore #9
    //   180: goto -> 248
    //   183: iload_3
    //   184: ifeq -> 241
    //   187: new com/softcorporation/xmllight/XMLLightException
    //   190: dup
    //   191: bipush #6
    //   193: iload #7
    //   195: invokespecial <init> : (II)V
    //   198: athrow
    //   199: iload #8
    //   201: bipush #62
    //   203: if_icmpne -> 212
    //   206: iconst_2
    //   207: istore #9
    //   209: goto -> 248
    //   212: iload #8
    //   214: bipush #34
    //   216: if_icmpeq -> 226
    //   219: iload #8
    //   221: bipush #39
    //   223: if_icmpne -> 230
    //   226: iload #8
    //   228: istore #10
    //   230: aload_0
    //   231: iinc #7, 1
    //   234: iload #7
    //   236: invokevirtual charAt : (I)C
    //   239: istore #8
    //   241: iload #8
    //   243: bipush #60
    //   245: if_icmpne -> 133
    //   248: iload #9
    //   250: ifne -> 306
    //   253: iload_3
    //   254: ifeq -> 269
    //   257: new com/softcorporation/xmllight/XMLLightException
    //   260: dup
    //   261: bipush #6
    //   263: iload #7
    //   265: invokespecial <init> : (II)V
    //   268: athrow
    //   269: aload_0
    //   270: iinc #7, 1
    //   273: iload #7
    //   275: invokevirtual charAt : (I)C
    //   278: istore #8
    //   280: goto -> 241
    //   283: astore #10
    //   285: iload_3
    //   286: ifeq -> 300
    //   289: new com/softcorporation/xmllight/XMLLightException
    //   292: dup
    //   293: iconst_2
    //   294: iload #7
    //   296: invokespecial <init> : (II)V
    //   299: athrow
    //   300: iinc #7, -1
    //   303: iconst_0
    //   304: istore #9
    //   306: aload_2
    //   307: iconst_1
    //   308: iload #6
    //   310: iastore
    //   311: aload_2
    //   312: iconst_2
    //   313: iload #7
    //   315: iastore
    //   316: iload #9
    //   318: ireturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #476	-> 0
    //   #477	-> 18
    //   #478	-> 24
    //   #479	-> 35
    //   #481	-> 40
    //   #486	-> 43
    //   #487	-> 53
    //   #489	-> 58
    //   #491	-> 60
    //   #492	-> 69
    //   #493	-> 77
    //   #495	-> 85
    //   #496	-> 96
    //   #497	-> 104
    //   #498	-> 107
    //   #500	-> 110
    //   #502	-> 124
    //   #506	-> 127
    //   #509	-> 130
    //   #511	-> 133
    //   #513	-> 139
    //   #515	-> 146
    //   #518	-> 152
    //   #520	-> 159
    //   #521	-> 170
    //   #523	-> 177
    //   #524	-> 180
    //   #526	-> 183
    //   #528	-> 187
    //   #532	-> 199
    //   #534	-> 206
    //   #535	-> 209
    //   #537	-> 212
    //   #539	-> 226
    //   #541	-> 230
    //   #509	-> 241
    //   #543	-> 248
    //   #545	-> 253
    //   #547	-> 257
    //   #549	-> 269
    //   #507	-> 280
    //   #557	-> 283
    //   #559	-> 285
    //   #561	-> 289
    //   #563	-> 300
    //   #564	-> 303
    //   #566	-> 306
    //   #567	-> 311
    //   #568	-> 316
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   0	319	0	doc	Ljava/lang/String;
    //   0	319	1	tag	Ljava/lang/String;
    //   0	319	2	pos	[I
    //   0	319	3	validate	Z
    //   18	301	4	sample	Ljava/lang/String;
    //   24	295	5	tagLen	I
    //   35	284	6	i1	I
    //   40	279	7	i	I
    //   77	206	8	c	C
    //   43	276	9	result	I
    //   130	153	10	quot	I
    //   285	21	10	e	Ljava/lang/StringIndexOutOfBoundsException;
    // Exception table:
    //   from	to	target	type
    //   43	58	283	java/lang/StringIndexOutOfBoundsException
    //   60	283	283	java/lang/StringIndexOutOfBoundsException
  }
  
  public static int findClosingTag(String doc, String tag, int[] pos, boolean validate) throws XMLLightException {
    String sample = "</" + tag;
    int tagLen = tag.length();
    pos[2] = -1;
    int i1 = pos[1] = -1;
    int i = pos[0];
    int result = -1;
    try {
      while (true) {
        i1 = doc.indexOf(sample, i);
        if (i1 < 0)
          return -1; 
        i = i1 + tagLen + 2;
        char c = doc.charAt(i);
        if (c == '>') {
          result = 1;
          break;
        } 
        if (Character.isWhitespace(c)) {
          result = 0;
          i = skipSpaces(doc, ++i);
          c = doc.charAt(i);
          if (c != '>') {
            if (validate)
              throw new XMLLightException(6, i); 
            while (c != '>')
              c = doc.charAt(++i); 
          } 
          result = 1;
          break;
        } 
      } 
    } catch (StringIndexOutOfBoundsException e) {
      if (validate)
        throw new XMLLightException(3, i); 
      i--;
      result = 0;
    } 
    pos[1] = i1;
    pos[2] = i;
    return result;
  }
  
  public static int skipSpaces(String doc, int start) {
    while (start < doc.length()) {
      if (Character.isWhitespace(doc.charAt(start))) {
        start++;
        continue;
      } 
      break;
    } 
    return start;
  }
  
  public static Element createTextElement(String name, String value) {
    Element element = new Element(name);
    element.addText(value);
    return element;
  }
  
  public static Element createTextElement(String name, int value) {
    return createTextElement(name, String.valueOf(value));
  }
  
  public static Element createTextElement(String name, Object value) {
    return createTextElement(name, value.toString());
  }
}