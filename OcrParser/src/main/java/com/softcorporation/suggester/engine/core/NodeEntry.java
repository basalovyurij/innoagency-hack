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
public class NodeEntry extends Node
{
  public byte type;
  public int offset;
  public int hash;

  public NodeEntry(char c)
  {
    super(c);
  }

  public int hashCode()
  {
    if (hash == 0)
    {
      hash = chr;
      for (Link l = link; l != null; l = l.y)
      {
        NodeEntry node = (NodeEntry) l.x;
        hash = (hash << 5) - hash + node.hashCode() +
            (node.type & NODE_TYPE_END);
      }
    }
    return hash;
  }

  public boolean equals(Object o)
  {
//    if (o == this)
//    {
//      return true;
//    }
//    if (! (o instanceof NodeEntry))
//    {
//      return false;
//    }
    NodeEntry n = (NodeEntry) o;
    if (n == null)
    {
      return false;
    }
    if (chr != n.chr)
    {
      return false;
    }
    if ( (type & NODE_TYPE_END) != (n.type & NODE_TYPE_END))
    {
      return false;
    }
    Link l = n.link;
    for (Link link = this.link; link != null; link = link.y)
    {
      if (l == null)
      {
        return false;
      }
      n = (NodeEntry) l.x;
      NodeEntry node = (NodeEntry) link.x;
      if (!node.equals(n))
      {
        return false;
      }
      l = l.y;
    }
    if (l != null)
    {
      return false;
    }
    return true;
  }

}
