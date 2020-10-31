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
package com.softcorporation.suggester.engine.core;

/**
 *
 * @version: $Revision:   1.0  $
 */
public class Node
{
  public static final byte NODE_TYPE_END = 1;
  public static final byte NODE_TYPE_LAST_X = 2;
  public static final byte NODE_TYPE_LAST_Y = 4;
  public static final byte NODE_TYPE_JUMP = 8;
  public static final byte NODE_TYPE_CAPITAL = 16; 
  public static final byte NODE_TYPE_OPTIMIZED = 32;
  public static final byte NODE_TYPE_SAVED = 64;

  public char chr;
  public Link link;

  public Node(char c)
  {
    chr = c;
  }

  public Node getChild(char c)
  {
    char cl = Character.toLowerCase(c);
    for (Link l = link; l != null; l = l.y)
    {
      Node node = l.x;
      if (node.chr == c)
      {
        return node;
      }
      if (node.chr > cl)
      {
        return null;
      }
    }
    return null;
  }

  public void add(Node node)
  {
    char cl = Character.toLowerCase(node.chr);
    Link l;
    Link l1 = null;
    for (l = link; l != null; l = l.y)
    {
      Node n = l.x;
      if (n.chr == node.chr)
      {
        l.x = node;
        return;
      }
      if (Character.toLowerCase(n.chr) >= cl)
      {
        break;
      }
      l1 = l;
    }
    Link l2 = new Link(node);
    if (l1 == null)
    {
      link = l2;
    }
    else
    {
      l1.y = l2;
    }
    l2.y = l;
  }

}
