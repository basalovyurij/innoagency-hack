/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softcorporation.util;

import java.util.Date;
/**
 *
 * @author yurij
 */
public class Logger {
    protected static Logger logger = new Logger();
  
  private boolean logError;
  
  private boolean logInfo;
  
  private boolean logDebug;
  
  public Logger() {
    this.logError = true;
    this.logInfo = false;
    this.logDebug = false;
    System.out.println("Logger: " + getClass().getName());
  }
  
  public static synchronized void setLogger(Logger logger) {
    Logger.logger = logger;
  }
  
  public static Logger getLogger() {
    return logger;
  }
  
  public boolean isLogError() {
    return this.logError;
  }
  
  public void setLogError(boolean logging) {
    this.logError = logging;
  }
  
  public boolean isLogInfo() {
    return this.logInfo;
  }
  
  public void setLogInfo(boolean logging) {
    this.logInfo = logging;
  }
  
  public boolean isLogDebug() {
    return this.logDebug;
  }
  
  public void setLogDebug(boolean logging) {
    this.logDebug = logging;
  }
  
  public synchronized void logError(String message) {
    if (this.logError)
      System.err.println(new Date() + "\tERROR\t" + message); 
  }
  
  public synchronized void logInfo(String message) {
    if (this.logInfo)
      System.out.println(new Date() + "\tINFO\t" + message); 
  }
  
  public synchronized void logDebug(String message) {
    if (this.logDebug)
      System.out.println(new Date() + "\tDEBUG\t" + message); 
  }
}
