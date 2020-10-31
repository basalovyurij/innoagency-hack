/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softcorporation.util;

import java.util.ArrayList;

public class Arguments {
  private ArrayList<String> names = new ArrayList<>();
  
  private ArrayList<String> values = new ArrayList<>();
  
  public Arguments(String[] args) {
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.startsWith("-")) {
        this.names.add(arg.substring(1));
        int j = i + 1;
        if (j < args.length) {
          String value = args[j];
          if (value.startsWith("-")) {
            this.values.add(null);
          } else {
            this.values.add(value);
            i++;
          } 
        } else {
          this.values.add(null);
        } 
      } 
    } 
  }
  
  public int length() {
    return this.names.size();
  }
  
  public String get(String arg) {
    int i = this.names.indexOf(arg);
    if (i >= 0)
      return this.values.get(i); 
    return null;
  }
  
  public boolean contains(String arg) {
    int i = this.names.indexOf(arg);
    if (i >= 0)
      return true; 
    return false;
  }
  
  public String toString() {
    StringBuffer buf = new StringBuffer("Arguments: [");
    for (int i = 0; i < this.names.size(); i++) {
      String name = this.names.get(i);
      String value = this.values.get(i);
      buf.append(" -");
      buf.append(name);
      if (value != null) {
        buf.append("=");
        buf.append(value);
      } 
    } 
    buf.append(" ]");
    return buf.toString();
  }
}
