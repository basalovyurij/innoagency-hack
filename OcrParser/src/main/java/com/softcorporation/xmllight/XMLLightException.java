/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softcorporation.xmllight;

/**
 *
 * @author yurij
 */
public class XMLLightException extends Exception {
  private int position;
  
  static final byte ERROR_INV_TAG = 0;
  
  static final byte ERROR_INV_ATTR = 1;
  
  static final byte ERROR_INV_OPN_TAG = 2;
  
  static final byte ERROR_INV_CLS_TAG = 3;
  
  static final byte ERROR_EXP_QUOT_1 = 4;
  
  static final byte ERROR_EXP_QUOT_2 = 5;
  
  static final byte ERROR_EXP_GT = 6;
  
  static final byte ERROR_EXP_LT = 7;
  
  private static final String[] errors = new String[] { "Invalid tag", 
      "Invalid attribute", 
      "Invalid opening tag", 
      "Invalid closing tag", 
      "Expected '\"'", 
      "Expected '''", 
      "Expected '>'", 
      "Expected '<'" };
  
  public XMLLightException(String msg) {
    super(msg);
    this.position = -1;
  }
  
  public XMLLightException(int errorCode, int position) {
    super(String.valueOf(errors[errorCode]) + " at position: " + position);
    this.position = position;
  }
  
  public int getPosition() {
    return this.position;
  }
}

