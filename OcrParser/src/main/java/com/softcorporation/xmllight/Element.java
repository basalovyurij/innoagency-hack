/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softcorporation.xmllight;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class Element implements Serializable {
  private byte status = 0;
  
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
  
  private int[] pos = new int[3];
  
  private String name;
  
  private Hashtable attributes = new Hashtable();
  
  private StringBuffer content = new StringBuffer();
  
  public boolean validate = true;
  
  public int errorCode;
  
  public Element() {}
  
  public Element(String name) {
    setName(name);
  }
  
  public Element(Element elem) {
    this.name = elem.name;
    this.content = new StringBuffer(new String(elem.content));
    this.attributes = (Hashtable)elem.attributes.clone();
  }
  
  public void setElem(String doc) throws XMLLightException {
    setElem(doc, 0);
  }
  
  public void setElem(String doc, int start) throws XMLLightException {
    int i1;
    this.name = null;
    this.attributes.clear();
    this.content = new StringBuffer();
    this.errorCode = 0;
    if (doc == null)
      return; 
    int i = start;
    this.pos[1] = -1;
    int docLength = doc.length();
    while (true) {
      i1 = doc.indexOf('<', i);
      if (i1 < 0 || i1 >= docLength - 1)
        return; 
      i = i1 + 1;
      char c = doc.charAt(i);
      if (c == '?' || c == '!') {
        i = doc.indexOf('>', i);
        if (i < 0) {
          this.errorCode = 2;
          return;
        } 
        continue;
      } 
      break;
    } 
    while (i < docLength) {
      char c = doc.charAt(i);
      if (Character.isWhitespace(c) || c == '/' || c == '>') {
        this.pos[0] = i1;
        String elemName = doc.substring(i1 + 1, i);
        setElem(doc, elemName, i1);
        return;
      } 
      i++;
    } 
    this.errorCode = 2;
  }
  
  public void setElem(String doc, String tag) throws XMLLightException {
    setElem(doc, tag, 0);
  }
  
  public void setElem(String doc, String tag, int start) throws XMLLightException {
    int ic1, ic2;
    this.name = null;
    this.attributes.clear();
    this.content = new StringBuffer();
    this.errorCode = 0;
    if (doc == null)
      return; 
    this.pos[0] = start;
    int i = XMLLight.findOpeningTag(doc, tag, this.pos, this.validate);
    int io1 = this.pos[1];
    int io2 = this.pos[2];
    if (i == -1)
      return; 
    setName(tag);
    if (i == 0)
      return; 
    if (i == 1) {
      setAttributes(getAttributes(doc.substring(io1 + tag.length() + 1, io2 - 1)));
      return;
    } 
    setAttributes(getAttributes(doc.substring(io1 + tag.length() + 1, io2)));
    int[] pos1 = new int[3];
    pos1[0] = io2 + 1;
    while (true) {
      i = XMLLight.findClosingTag(doc, tag, pos1, this.validate);
      if (i == -1 || i == 0) {
        if (this.validate)
          throw new XMLLightException(0, this.pos[2]); 
        i = this.pos[2];
        while (++i < doc.length()) {
          if (doc.charAt(i) == '<')
            break; 
        } 
        setCont(doc.substring(this.pos[2] + 1, i));
        this.pos[2] = i - 1;
        return;
      } 
      ic1 = pos1[1];
      ic2 = pos1[2];
      pos1[0] = io2 + 1;
      i = XMLLight.findOpeningTag(doc, tag, pos1, this.validate);
      io1 = pos1[1];
      io2 = pos1[2];
      if (i == 2)
        if (io1 < ic1) {
          pos1[0] = ic2 + 1;
          continue;
        }  
      break;
    } 
    setCont(doc.substring(this.pos[2] + 1, ic1));
    this.pos[2] = ic2;
  }
  
  private Properties getAttributes(String doc) throws XMLLightException {
    Properties attributes = new Properties();
    String attrName = "";
    int docLen = doc.length();
    int quote = -1;
    int i0 = 0;
    int i = 0;
    int status = 2;
    while (i < docLen) {
      char c = doc.charAt(i);
      if (status == 2) {
        if (!Character.isWhitespace(c)) {
          if (c == '&' || c == '\'' || c == '"') {
            if (this.validate)
              throw new XMLLightException(1, i); 
            return attributes;
          } 
          status = 3;
          i0 = i;
        } 
      } else if (status == 3) {
        if (Character.isWhitespace(c)) {
          attrName = doc.substring(i0, i);
          status = 4;
        } else if (c == '=') {
          attrName = doc.substring(i0, i);
          status = 5;
        } else if (c == '&' || c == '\'' || c == '"') {
          if (this.validate)
            throw new XMLLightException(1, i); 
          return attributes;
        } 
      } else if (status == 4) {
        if (c == '=') {
          status = 5;
        } else if (!Character.isWhitespace(c)) {
          if (this.validate)
            throw new XMLLightException(1, i); 
          attributes.put(attrName, attrName);
          status = 2;
        } 
      } else if (status == 5) {
        if (c == '"' || c == '\'') {
          quote = c;
          i0 = i + 1;
          status = 6;
        } else if (!Character.isWhitespace(c)) {
          if (this.validate)
            throw new XMLLightException(1, i); 
          quote = -1;
          status = 6;
        } 
      } else if (status == 6) {
        if (c == quote) {
          attributes.put(attrName, doc.substring(i0, i));
          status = 7;
        } else if (quote == -1) {
          if (Character.isWhitespace(c)) {
            attributes.put(attrName, doc.substring(i0, i));
            status = 2;
          } 
        } 
      } else if (status == 7) {
        if (Character.isWhitespace(c)) {
          status = 2;
        } else {
          if (this.validate)
            throw new XMLLightException(1, i); 
          status = 2;
          break;
        } 
      } 
      i++;
    } 
    if (status != 2 && status != 7) {
      if (this.validate)
        throw new XMLLightException(1, i); 
      if (status == 3) {
        attributes.put(attrName, attrName);
      } else if (status == 6) {
        attributes.put(attrName, doc.substring(i0, i));
      } 
    } 
    return attributes;
  }
  
  public String toString() {
    if (this.name == null || this.name.length() == 0)
      return ""; 
    StringBuffer sb = new StringBuffer("<");
    sb.append(this.name);
    Enumeration enumeration = this.attributes.keys();
    while (enumeration.hasMoreElements()) {
      String s = (String)enumeration.nextElement();
      sb.append(" ");
      sb.append(s);
      s = (String)this.attributes.get(s);
      if (s == null) {
        sb.append("=\"\"");
        continue;
      } 
      if (s.indexOf("\"") >= 0) {
        if (s.indexOf("'") < 0) {
          sb.append("='");
          sb.append(s);
          sb.append("'");
          continue;
        } 
        sb.append("=\"");
        sb.append(XMLLight.encode(s, false));
        sb.append("\"");
        continue;
      } 
      sb.append("=\"");
      sb.append(s);
      sb.append("\"");
    } 
    if (this.content.length() == 0) {
      sb.append("/>");
    } else {
      sb.append(">");
      sb.append(this.content.toString());
      sb.append("</");
      sb.append(this.name);
      sb.append(">");
    } 
    return sb.toString();
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.errorCode = 0;
    if (name == null) {
      this.errorCode = 3;
    } else {
      name = name.trim();
      if (name.length() == 0)
        this.errorCode = 3; 
    } 
    if (this.errorCode == 0) {
      this.name = name;
    } else {
      XMLLight.writeLog("Element name '" + name + "' not valid.");
    } 
  }
  
  public String getAttr(String attr) {
    return XMLLight.decode((String)this.attributes.get(attr));
  }
  
  public String getAttr(String attr, String defValue) {
    String val = getAttr(attr);
    return (val == null) ? defValue : XMLLight.decode(val);
  }
  
  public void setAttr(String attr, String value) {
    if (this.attributes.get(attr) != null)
      this.attributes.remove(attr); 
    if (value != null)
      this.attributes.put(attr, XMLLight.encode(value, false)); 
  }
  
  public void setAttr(String attr, int value) {
    setAttr(attr, (new Integer(value)).toString());
  }
  
  public void remAttr(String attr) {
    this.attributes.remove(attr);
  }
  
  public Properties getAttributes() {
    Properties p = new Properties();
    for (Enumeration e = this.attributes.keys(); e.hasMoreElements(); ) {
      String key = (String)e.nextElement();
      String val = XMLLight.decode((String)this.attributes.get(key));
      p.put(key, val);
    } 
    return p;
  }
  
  public void setAttributes(Properties attributes) {
    this.attributes = attributes;
  }
  
  public String getCont() {
    return new String(this.content);
  }
  
  public void setCont(String cont) {
    this.pos[0] = 0;
    this.content = new StringBuffer((cont == null) ? "" : cont);
  }
  
  public void addCont(String cont) {
    if (cont == null)
      return; 
    this.content.append(cont);
  }
  
  public void addCont(Element elem) {
    this.content.append(elem.toString());
  }
  
  public void insCont(String cont) {
    if (cont == null)
      return; 
    this.content.insert(0, cont);
  }
  
  public void insCont(String cont, int position) {
    if (cont == null)
      return; 
    this.content.insert(position, cont);
  }
  
  public void insCont(Element elem) {
    this.content.insert(0, elem.toString());
  }
  
  public void insCont(Element elem, int position) {
    this.content.insert(position, elem.toString());
  }
  
  public void addElem(Element elem) {
    this.content.append(elem.toString());
  }
  
  public void addComment(String comment) {
    if (comment == null)
      return; 
    this.content.append("<!--");
    this.content.append(comment);
    this.content.append("-->");
  }
  
  public void addCData(String cdata) {
    if (cdata == null)
      return; 
    int i = 0;
    int i1 = cdata.indexOf("]]>");
    while (i1 >= 0) {
      this.content.append("<![CDATA[");
      this.content.append(cdata.substring(i, ++i1));
      this.content.append("]]>");
      i = i1;
      i1 = cdata.indexOf("]]>", i);
    } 
    this.content.append("<![CDATA[");
    this.content.append(cdata.substring(i));
    this.content.append("]]>");
  }
  
  public void addText(String text) {
    if (text == null)
      return; 
    this.content.append(XMLLight.encode(text, false));
  }
  
  public String getText() {
    return getText(true);
  }
  
  public String getText(boolean decode) {
    String cont = new String(this.content);
    int i = cont.indexOf('<', this.pos[0]);
    String s = "";
    if (i < 0) {
      if (this.pos[0] >= cont.length()) {
        this.pos[1] = -1;
      } else {
        s = cont.substring(this.pos[0], cont.length());
        this.pos[1] = this.pos[0];
        this.pos[0] = cont.length();
      } 
    } else if (i > 0) {
      s = cont.substring(this.pos[0], i);
      this.pos[1] = this.pos[0];
      this.pos[0] = i;
    } else {
      this.pos[1] = -1;
    } 
    this.pos[2] = this.pos[0] - 1;
    if (decode) {
      s = XMLLight.decode(s);
      if (s == null) {
        this.errorCode = 2;
        XMLLight.writeLog("Element name '" + this.name + "' text not valid.");
        return "";
      } 
    } 
    this.errorCode = 0;
    return s;
  }
  
  public String getChildText() {
    return getChildText(true);
  }
  
  public String getChildText(boolean decode) {
    StringBuffer sb = new StringBuffer();
    String s = new String(this.content);
    int i1 = 0;
    int i2 = s.indexOf("<");
    while (i2 >= 0) {
      sb.append(s.substring(i1, i2));
      i1 = s.indexOf(">", i2 + 1);
      if (i1 < 0) {
        this.pos[1] = -1;
        this.errorCode = 2;
        XMLLight.writeLog("Element name '" + this.name + "' content not valid.");
        return "";
      } 
      i1++;
      i2 = s.indexOf("<", i1);
    } 
    sb.append(s.substring(i1, s.length()));
    this.pos[1] = 0;
    this.pos[2] = this.content.length() - 1;
    this.errorCode = 0;
    if (decode)
      return XMLLight.decode(sb.toString()); 
    return sb.toString();
  }
  
  public Element getElem() throws XMLLightException {
    Element elem = new Element();
    elem.validate = this.validate;
    elem.setElem(new String(this.content), this.pos[0]);
    if (elem.pos[1] >= 0)
      this.pos[0] = elem.pos[2] + 1; 
    return elem;
  }
  
  public Element getElem(String elemName) throws XMLLightException {
    Element elem = new Element();
    elem.validate = this.validate;
    elem.setElem(new String(this.content), elemName, this.pos[0]);
    if (elem.pos[1] >= 0) {
      this.pos[0] = elem.pos[2] + 1;
      this.pos[1] = elem.pos[1];
      this.pos[2] = elem.pos[2];
    } 
    return elem;
  }
  
  public Element getFirstElem(String elemName) throws XMLLightException {
    this.pos[0] = 0;
    return getElem(elemName);
  }
  
  public Element getChildElem(String elemName) throws XMLLightException {
    String cont = new String(this.content);
    Element elem = new Element();
    elem.validate = this.validate;
    elem.setElem(cont, this.pos[0]);
    while (elem.pos[1] >= 0) {
      if (elemName.equals(elem.getName())) {
        this.pos[0] = elem.pos[0];
        this.pos[1] = elem.pos[1];
        this.pos[2] = elem.pos[2];
        return elem;
      } 
      elem.setElem(cont, elem.pos[2] + 1);
    } 
    this.pos[1] = -1;
    this.pos[2] = -1;
    return new Element();
  }
  
  public void resetPosition() {
    setPosition(0);
  }
  
  public void setPosition(int position) {
    this.pos[0] = position;
  }
  
  public int getPosition() {
    return this.pos[0];
  }
  
  public int getPosition(int id) {
    return this.pos[id];
  }
  
  public boolean isNull() {
    return (this.name == null);
  }
}