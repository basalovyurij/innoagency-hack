/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softcorporation.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

public class Configuration {
  protected boolean isProtected;
  
  private Properties properties = new Properties();
  
  Logger logger = Logger.getLogger();
  
  public Configuration() {}
  
  public Configuration(String fileName) {
    load(fileName, null);
  }
  
  public Configuration(String fileName, String encoding) {
    load(fileName, encoding);
  }
  
  protected Properties getProperties() {
    return this.properties;
  }
  
  protected String getProperty(String name) {
    return this.properties.getProperty(name);
  }
  
  protected void setProperty(String name, String value) {
    this.properties.setProperty(name, value);
  }
  
  protected String getProperty(String name, String defValue) {
    String value = this.properties.getProperty(name);
    if (value != null)
      return value; 
    return defValue;
  }
  
  protected int getProperty(String name, int defValue) {
    String value = this.properties.getProperty(name);
    if (value != null)
      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      }  
    return defValue;
  }
  
  protected boolean getProperty(String name, boolean defValue) {
    String value = this.properties.getProperty(name);
    if (value != null) {
      if ("true".equalsIgnoreCase(value))
        return true; 
      if ("false".equalsIgnoreCase(value))
        return false; 
      this.logger.logError("Field " + name + " contains invalid data: " + value);
    } 
    return defValue;
  }
  
  protected float getProperty(String name, float defValue) {
    String value = this.properties.getProperty(name);
    if (value != null)
      try {
        return Float.parseFloat(value);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      }  
    return defValue;
  }
  
  protected long getProperty(String name, long defValue) {
    String value = this.properties.getProperty(name);
    if (value != null)
      try {
        return Long.parseLong(value);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      }  
    return defValue;
  }
  
  protected double getProperty(String name, double defValue) {
    String value = this.properties.getProperty(name);
    if (value != null)
      try {
        return Double.parseDouble(value);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      }  
    return defValue;
  }
  
  protected short getProperty(String name, short defValue) {
    String value = this.properties.getProperty(name);
    if (value != null)
      try {
        return Short.parseShort(value);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      }  
    return defValue;
  }
  
  protected byte getProperty(String name, byte defValue) {
    String value = this.properties.getProperty(name);
    if (value != null)
      try {
        return Byte.parseByte(value);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      }  
    return defValue;
  }
  
  protected char getProperty(String name, char defValue) {
    String value = this.properties.getProperty(name);
    if (value != null) {
      if (value.length() == 1)
        return value.charAt(0); 
      this.logger.logError("Field " + name + " contains invalid data: " + value);
    } 
    return defValue;
  }
  
  protected int[] getProperty(String name, int[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ", ");
    int[] array = new int[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken();
      try {
        array[i++] = Integer.parseInt(token);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      } 
    } 
    return array;
  }
  
  protected float[] getProperty(String name, float[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ", ");
    float[] array = new float[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken();
      try {
        array[i++] = Float.parseFloat(token);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      } 
    } 
    return array;
  }
  
  protected double[] getProperty(String name, double[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ", ");
    double[] array = new double[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken();
      try {
        array[i++] = Double.parseDouble(token);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      } 
    } 
    return array;
  }
  
  protected long[] getProperty(String name, long[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ", ");
    long[] array = new long[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken();
      try {
        array[i++] = Long.parseLong(token);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      } 
    } 
    return array;
  }
  
  protected byte[] getProperty(String name, byte[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ", ");
    byte[] array = new byte[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken();
      try {
        array[i++] = Byte.parseByte(token);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      } 
    } 
    return array;
  }
  
  protected short[] getProperty(String name, short[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ", ");
    short[] array = new short[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken();
      try {
        array[i++] = Short.parseShort(token);
      } catch (NumberFormatException e) {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      } 
    } 
    return array;
  }
  
  protected boolean[] getProperty(String name, boolean[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ", ");
    boolean[] array = new boolean[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken();
      if ("true".equalsIgnoreCase(token)) {
        array[i] = true;
      } else if ("false".equalsIgnoreCase(token)) {
        array[i] = false;
      } else {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      } 
      i++;
    } 
    return array;
  }
  
  protected char[] getProperty(String name, char[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ",");
    char[] array = new char[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken();
      if (token.length() == 1) {
        array[i] = token.charAt(0);
      } else {
        this.logger.logError("Field " + name + " contains invalid data: " + value);
      } 
      i++;
    } 
    return array;
  }
  
  protected String[] getProperty(String name, String[] defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ",");
    String[] array = new String[tokens.countTokens()];
    int i = 0;
    while (tokens.hasMoreTokens())
      array[i++] = tokens.nextToken(); 
    return array;
  }
  
  protected Vector getProperty(String name, Vector defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ",");
    Vector vector = new Vector();
    while (tokens.hasMoreTokens())
      vector.add(tokens.nextToken()); 
    return vector;
  }
  
  protected ArrayList getProperty(String name, ArrayList defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ",");
    ArrayList arrayList = new ArrayList();
    while (tokens.hasMoreTokens())
      arrayList.add(tokens.nextToken()); 
    return arrayList;
  }
  
  protected TreeSet getProperty(String name, TreeSet defValue) {
    String value = this.properties.getProperty(name);
    if (value == null)
      return defValue; 
    if (value.startsWith("[") && value.endsWith("]"))
      value = value.substring(1, value.length() - 1); 
    StringTokenizer tokens = new StringTokenizer(value, ",");
    TreeSet treeSet = new TreeSet();
    while (tokens.hasMoreTokens())
      treeSet.add(tokens.nextToken()); 
    return treeSet;
  }
  
  public boolean load(String fileName) {
    return load(fileName, null);
  }
  
  public synchronized boolean load(String fileName, String encoding) {
    if (this.isProtected)
      return false; 
    try {
      InputStream is;
      if (fileName.startsWith("file://")) {
        fileName = fileName.substring(7);
        File file = new File(fileName);
        fileName = file.getAbsolutePath();
        is = new FileInputStream(file);
      } else {
        if (!fileName.startsWith("/"))
          fileName = "/" + fileName; 
        is = getClass().getResourceAsStream(fileName);
      } 
      if (is == null)
        throw new IOException("Cannot open configuration file: " + fileName); 
      if (encoding != null) {
        InputStreamReader isr = new InputStreamReader(is, encoding);
        StringBuffer buffer = new StringBuffer();
        Reader reader = new BufferedReader(isr);
        int chr;
        while ((chr = reader.read()) >= 0) {
          if (chr < 128) {
            buffer.append((char)chr);
            continue;
          } 
          buffer.append(StringUtil.encodeChar((char)chr));
        } 
        reader.close();
        is.close();
        String input = buffer.toString();
        is = new ByteArrayInputStream(input.getBytes("8859_1"));
      } 
      this.properties.load(is);
      is.close();
    } catch (Exception e) {
      this.logger.logError("Cannot read configuration properties from file: " + 
          fileName);
      return false;
    } 
    String logError = this.properties.getProperty("LOG_ERROR");
    if (logError != null)
      this.logger.setLogError((new Boolean(logError)).booleanValue()); 
    String logInfo = this.properties.getProperty("LOG_INFO");
    if (logInfo != null)
      this.logger.setLogInfo((new Boolean(logInfo)).booleanValue()); 
    String logDebug = this.properties.getProperty("LOG_DEBUG");
    if (logDebug != null)
      this.logger.setLogDebug((new Boolean(logDebug)).booleanValue()); 
    String sProtect = this.properties.getProperty("CFG_PROTECT");
    if (sProtect != null)
      this.isProtected = (new Boolean(sProtect)).booleanValue(); 
    init();
    if (this.logger.isLogInfo())
      this.logger.logInfo("Configuration properties loaded from file: " + fileName); 
    return true;
  }
  
  private void init() {
    ArrayList<Field> fields = getFields();
    for (int i = 0; i < fields.size(); i++) {
      Field field = fields.get(i);
      try {
        String name = field.getName();
        String type = field.getType().toString();
        int mod = field.getModifiers();
        if (!Modifier.isFinal(mod))
          if (this.properties.getProperty(name) != null) {
            Object obj = null;
            if ("class java.lang.String".equals(type)) {
              obj = getProperty(name, (String)get(field));
              type = "String";
            } else if ("int".equals(type)) {
              obj = new Integer(getProperty(name, ((Integer)get(field)).intValue()));
            } else if ("boolean".equals(type)) {
              obj = new Boolean(getProperty(name, ((Boolean)get(field)).booleanValue()));
            } else if ("float".equals(type)) {
              obj = new Float(getProperty(name, ((Float)get(field)).floatValue()));
            } else if ("long".equals(type)) {
              obj = new Long(getProperty(name, ((Long)get(field)).longValue()));
            } else if ("double".equals(type)) {
              obj = new Double(getProperty(name, ((Double)get(field)).doubleValue()));
            } else if ("short".equals(type)) {
              obj = new Short(getProperty(name, ((Short)get(field)).shortValue()));
            } else if ("byte".equals(type)) {
              obj = new Byte(getProperty(name, ((Byte)get(field)).byteValue()));
            } else if ("char".equals(type)) {
              obj = new Character(getProperty(name, ((Character)get(field)).charValue()));
            } else if ("class [I".equals(type)) {
              obj = getProperty(name, (int[])get(field));
              type = "int[]";
            } else if ("class [F".equals(type)) {
              obj = getProperty(name, (float[])get(field));
              type = "float[]";
            } else if ("class [Ljava.lang.String;".equals(type)) {
              obj = getProperty(name, (String[])get(field));
              type = "String[]";
            } else if ("class [J".equals(type)) {
              obj = getProperty(name, (long[])get(field));
              type = "long[]";
            } else if ("class [D".equals(type)) {
              obj = getProperty(name, (double[])get(field));
              type = "double[]";
            } else if ("class [S".equals(type)) {
              obj = getProperty(name, (short[])get(field));
              type = "short[]";
            } else if ("class [B".equals(type)) {
              obj = getProperty(name, (byte[])get(field));
              type = "byte[]";
            } else if ("class [Z".equals(type)) {
              obj = getProperty(name, (boolean[])get(field));
              type = "boolean[]";
            } else if ("class [C".equals(type)) {
              obj = getProperty(name, (char[])get(field));
              type = "char[]";
            } else if ("class java.util.Vector".equals(type)) {
              obj = getProperty(name, (Vector)get(field));
              type = "Vector";
            } else if ("class java.util.ArrayList".equals(type)) {
              obj = getProperty(name, (ArrayList)get(field));
              type = "ArrayList";
            } else if ("class java.util.TreeSet".equals(type)) {
              obj = getProperty(name, (TreeSet)get(field));
              type = "TreeSet";
            } else {
              if (this.logger.isLogDebug() && !this.isProtected)
                this.logger.logDebug("Configuration data of unknown type: " + 
                    field.getDeclaringClass() + 
                    " (" + type + ") " + name); 
              i++;
            } 
            set(field, obj);
            if (this.logger.isLogDebug() && !this.isProtected)
              this.logger.logDebug("Configuration data: " + field.getDeclaringClass() + 
                  " (" + type + ") " + name + "=" + StringUtil.toString(get(field))); 
          }  
      } catch (Throwable e) {
        this.logger.logError("Configuration: " + field.getDeclaringClass() + " Field " + field.getName() + " initialization error. " + e.getLocalizedMessage());
      } 
    } 
    initialize();
  }
  
  public synchronized boolean save(String fileName) {
    if (this.isProtected)
      return false; 
    ArrayList<Field> fields = getFields();
    for (int i = 0; i < fields.size(); i++) {
      Field field = fields.get(i);
      try {
        String name = field.getName();
        String type = field.getType().toString();
        int mod = field.getModifiers();
        if (this.properties.containsKey(name))
          this.properties.setProperty(name, StringUtil.toString(get(field))); 
      } catch (Throwable e) {
        this.logger.logError("Configuration: " + field.getDeclaringClass() + " Field " + field.getName() + " save error. " + e.getLocalizedMessage());
      } 
    } 
    save();
    if (fileName != null) {
      try {
        if (fileName.startsWith("file://"))
          fileName = fileName.substring(7); 
        File file = new File(fileName);
        fileName = file.getAbsolutePath();
        OutputStream os = new FileOutputStream(file);
        if (os == null)
          throw new IOException("Cannot open output stream to file: " + fileName); 
        this.properties.store(os, "com.softcorporation.util.Configuration. ATTENTION: This file will be replaced during next save configuration call.");
        os.close();
      } catch (Exception e) {
        this.logger.logError("Cannot save configuration properties to file: " + 
            fileName + " Exception: " + e.getLocalizedMessage());
        return false;
      } 
      if (this.logger.isLogInfo())
        this.logger.logInfo("Configuration properties saved to file: " + fileName); 
    } 
    return true;
  }
  
  public synchronized String getOSProperty(String key) {
    String property = null;
    StringBuffer sb = new StringBuffer();
    try {
      String[] cmd = new String[3];
      String os = System.getProperty("os.name");
      if (os.startsWith("Windows")) {
        key = "%" + key + "%";
        cmd[0] = "cmd.exe";
        cmd[1] = "/c";
        cmd[2] = "echo " + key;
        Process process = Runtime.getRuntime().exec(cmd);
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        int chr;
        while ((chr = isr.read()) != -1)
          sb.append((char)chr); 
        process.waitFor();
        property = sb.toString().trim();
        if (key.equals(property))
          return null; 
      } else {
        cmd[0] = "sh";
        cmd[1] = "-c";
        cmd[2] = "echo $" + key;
        Process process = Runtime.getRuntime().exec(cmd);
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        int chr;
        while ((chr = isr.read()) != -1)
          sb.append((char)chr); 
        process.waitFor();
        property = sb.toString().trim();
      } 
    } catch (Exception exception) {}
    if (property.length() == 0)
      return null; 
    return property;
  }
  
  public String toString() {
    if (this.isProtected)
      return "Configuration is protected"; 
    StringBuffer buf = new StringBuffer("Configuration (");
    ArrayList<Field> fields = getFields();
    for (int i = 0; i < fields.size(); i++) {
      Field field = fields.get(i);
      try {
        buf.append("  ");
        buf.append(field.getName());
        buf.append("=");
        buf.append(StringUtil.toString(get(field)));
      } catch (Exception exception) {}
    } 
    buf.append("  )");
    return buf.toString();
  }
  
  private ArrayList<Field> getFields() {
    Class cls = getClass();
    ArrayList<Field> fields = new ArrayList<>();
    Field[] fieldArray = cls.getDeclaredFields();
    int i;
    for (i = 0; i < fieldArray.length; i++)
      fields.add(fieldArray[i]); 
    fieldArray = cls.getFields();
    for (i = 0; i < fieldArray.length; i++) {
      Field field = fieldArray[i];
      if (!fields.contains(field))
        fields.add(field); 
    } 
    return fields;
  }
  
  protected void initialize() {}
  
  protected void save() {}
  
  protected Object get(Field field) throws IllegalArgumentException, IllegalAccessException {
    return field.get(this);
  }
  
  protected void set(Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
    field.set(this, value);
  }
}
