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
package com.softcorporation.suggester.index;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;

import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SuggesterException;
import com.softcorporation.util.Arguments;
import com.softcorporation.suggester.engine.core.Dictionary;
import com.softcorporation.suggester.dictionary.BasicDictionary;
import com.softcorporation.suggester.engine.core.Node;
import com.softcorporation.suggester.engine.core.NodeEnd;
import com.softcorporation.suggester.engine.core.NodeEntry;
import com.softcorporation.suggester.engine.core.Link;

/**
 * Basic Index Builder.
 * <p>
 * @version   1.0, 02/02/2005
 * @author   Vadim Permakoff
 */
public class BasicIndexBuilder implements IndexBuilder
{
  String prevWord = "";
  NodeEntry[] nodesPos = new NodeEntry[Constants.WORD_LENGTH_MAX];
  int posPrev;
  int currentType = -1;
  int totalNodes;
  int totalWords;
  int totalDocs;
  int offset;
  NodeEntry mainNode;
  Hashtable nodeTable = new Hashtable();
  LinkedList ll;

  BufferedWriter outChar;
  BufferedOutputStream outType;
  BufferedOutputStream outLink;

  String inputFileName;
  ArrayList inputFileNames = new ArrayList();
  String outputFileName;
  String indexName;
  String encoding;
  String language;
  String license;
  String info;
  String rules;
  boolean tokenize;
  boolean sorted;
  boolean verbose;
  String delimiters;
  String wordFileName;
  Collection words;

  int posPrint;
  char[] chars = new char[Constants.WORD_LENGTH_MAX];
  Writer writer;
  int counter;

  boolean russian;
  BufferedWriter bwRemoved;

  public static void main(String[] args)
  {
    BasicIndexBuilder ib = new BasicIndexBuilder();
    int i = ib.execute(args);
    if (i == 0)
    {
      System.out.println("Done. SUCCESS");
    }
    System.exit(i);
  }

  public void displayHelp()
  {
    System.out.println("Basic Index Builder for Suggester v." + Constants.VERSION);
    System.out.println("Usage: java -mx256m com.softcorporation.suggester.index.BasicIndexBuilder parameters ...");
    System.out.println("\nParameters format: -parameter [value]");
    System.out.println("    -input filename[,filename]  input word file(s) (mandatory)");
    System.out.println("    -output filename            output index file (mandatory)");
    System.out.println("    -name indexname             index name in output index file");
    System.out.println("    -encoding value             input word list file(s) character encoding,");
    System.out.println("        value is standard Java encoding (default encoding is UTF-8)");
    System.out.println("    -wordlist filename          output sorted word list file in UTF-8 encoding");
    System.out.println("    -language value             index language, value as ISO 639: 2-letter code");
    System.out.println("    -license value              license information");
    System.out.println("    -info value                 the index additional information");
    System.out.println("    -rules value                rules (reserved for future usage)");
    System.out.println("    -sorted                     input word list is already sored and tokenized");
    System.out.println("        use this option if your computer has little amount of memory");
    System.out.println("        but you need to compile huge word list");
    System.out.println("        the input word list must be sored and tokenized");
    System.out.println("    -tokenize                   separate words on each line in input file");
    System.out.println("    -delimiters value           delimiters (reserved for future usage)");
    System.out.println("    -verbose                    display progress information (on standard output)");
    System.out.println();
    System.out.println("Example: Create index from two ISO Latin 1 word files");
    System.out.println("         and save output to file index.zip:");
    System.out.println("    java -mx128m com.softcorporation.suggester.index.BasicIndexBuilder");
    System.out.println("         -input words1.txt,words2.txt -output index.zip -wordlist list.txt");
    System.out.println("         -encoding ISO8859_1 -verbose");
    System.out.println();
    System.out.println("For more information visit web site: http://www.softcorporation.com/products/suggester");
  }

  void log(String message)
  {
    if (verbose)
    {
      System.out.println(message);
    }
  }

  int execute(String[] args)
  {
    try
    {
      // start builder
      long memory0 = getMemory();
      long memory1;

      Arguments arguments = new Arguments(args);
      inputFileName = arguments.get("input");
      if (inputFileName == null)
      {
        displayHelp();
        return 1;
      }
      StringTokenizer st = new StringTokenizer(inputFileName, ",");
      while (st.hasMoreTokens())
      {
        inputFileNames.add(st.nextToken().trim());
      }

      outputFileName = arguments.get("output");
      if (outputFileName == null)
      {
        displayHelp();
        System.exit(1);
      }
      File outputFile = new File(outputFileName);

      encoding = arguments.get("encoding");
      if (encoding == null)
      {
        encoding = Constants.CHARACTER_SET_ENCODING_DEFAULT;
      }

      indexName = arguments.get("name");
      if (indexName == null)
      {
        indexName = makeIndexName(outputFile);
      }

      language = arguments.get("language");
      if ("ru".equals(language))
      {
        russian = true;
      }

      wordFileName = arguments.get("wordlist");
      license = arguments.get("license");
      info = arguments.get("info");
      rules = arguments.get("rules");
      tokenize = arguments.contains("tokenize");
      sorted = arguments.contains("sorted");
      delimiters = arguments.get("delimiters");
      if (delimiters == null)
      {
        delimiters = DELIMITERS;
      }
      verbose = arguments.contains("verbose"); ;

      // get start time
      Date date = new Date();
      log("Start. Date: " + date);
      log(arguments.toString());
      log("Accepted encoding: " + encoding);
      long time0 = System.currentTimeMillis();
      long time1;

      File outRemovedFile = new File("words.removed.txt");
      bwRemoved = new BufferedWriter(new OutputStreamWriter(new
          FileOutputStream(outRemovedFile),
          Constants.CHARACTER_SET_ENCODING_DEFAULT));

      if (!sorted)
      {
        words = new TreeSet();
      }
      else
      {
        words = new ArrayList();
      }

      for (int i = 0; i < inputFileNames.size(); i++)
      {
        inputFileName = (String) inputFileNames.get(i);
        File inFile = new File(inputFileName);
        readFile(inFile);
      }
      totalWords = words.size();

      time1 = System.currentTimeMillis();
      memory1 = getMemory();
      log("Total recognized words in files: " + totalWords);
      log("Done. It took " + (time1 - time0) + " milliseconds. Used memory: " +
          (memory1 - memory0) + "\n");

      log("Creating index ...");
      time0 = System.currentTimeMillis();

      makeIndex();

      time1 = System.currentTimeMillis();
      memory1 = getMemory();
      log("Done. It took " + (time1 - time0) + " milliseconds. Used memory: " +
          (memory1 - memory0) + "\n");

//      System.out.println("Main Node: " + mainNode.offset);
//      System.out.println("hashTable: " + nodeTable.size());

      if (wordFileName != null)
      {
        time0 = System.currentTimeMillis();

        File outWFile = new File(wordFileName);
        log("Writing word list to file: " + outWFile.getAbsolutePath());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new
            FileOutputStream(outWFile),
            Constants.CHARACTER_SET_ENCODING_DEFAULT));
        printWords(mainNode, bw);
        bw.close();
        time1 = System.currentTimeMillis();
        memory1 = getMemory();
        log("Total words in the list: " + (counter - 1));
        log("Done. It took " + (time1 - time0) + " milliseconds. Used memory: " +
            (memory1 - memory0) + "\n");
      }

      time0 = System.currentTimeMillis();

      File outFile = new File(outputFileName);
      log("Saving index \"" + indexName + "\" to file: " +
          outFile.getAbsolutePath());
      save(outputFileName, indexName);

      bwRemoved.close();

      time1 = System.currentTimeMillis();
      memory1 = getMemory();
      log("Done. It took " + (time1 - time0) + " milliseconds. Used memory: " +
          (memory1 - memory0) + "\n");
    }
    catch (Exception e)
    {
      System.out.println("Error: " + e.getMessage());
      return 2;
    }
    return 0;
  }

  // make index
  void makeIndex() throws SuggesterException
  {
    mainNode = new NodeEntry( (char) 0);
    nodesPos[0] = mainNode;
    Iterator iter = words.iterator();
    while (iter.hasNext())
    {
      String word = (String) iter.next();
      addWordToTrie(word);
    }
    addWordToTrie(""); // save last states
    words = null; // cleanup resources
    nodeTable = null; // cleanup resources
    countNodes();
  }

  // make index name from file name
  String makeIndexName(File file)
  {
    String indexName = file.getName();
    int i = indexName.lastIndexOf(".");
    if (i > 0)
    {
      indexName = indexName.substring(0, i).trim();
    }
    if (indexName.length() == 0)
    {
      indexName = "index";
    }
    return indexName;
  }

  void readFile(File file) throws
      SuggesterException, IOException
  {
    if (!file.exists())
    {
      throw new SuggesterException("Cannot open input file: " +
                                   file.getAbsolutePath());
    }
    log("Reading input file: " + file.getAbsolutePath());
    FileInputStream inStream = new FileInputStream(file);
    BufferedReader br = new BufferedReader(new InputStreamReader(inStream,
        encoding));

    // read all words
    String line;
    while ( (line = br.readLine()) != null)
    {
      // ignore lines starting with #
      if (line.startsWith("#"))
      {
        continue;
      }
      if (tokenize)
      {
        StringTokenizer st = new StringTokenizer(line, delimiters);
        while (st.hasMoreTokens())
        {
          String word = st.nextToken();
          add(word);
        }
      }
      else
      {
        add(line);
      }
    }
    br.close();
  }

  void add(String word) throws IOException
  {
    words.add(word);
  }

  public void save(String fileName, String dictName) throws SuggesterException
  {
    if (fileName == null || fileName.trim().length() == 0)
    {
      throw new SuggesterException(
          "Cannot save dictionary. Invalid (null) file name");
    }
    if (dictName == null || dictName.trim().length() == 0)
    {
      throw new SuggesterException(
          "Cannot save dictionary. Invalid (null) dictionary name");
    }
    try
    {
      FileOutputStream fos = new FileOutputStream(fileName);
      ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

      // save dictionary information
      zos.putNextEntry(new ZipEntry(dictName + Dictionary.IND_I));
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(zos, Constants.CHARACTER_SET_ENCODING_DEFAULT));

      bw.write(Dictionary.SFT);
      bw.write(Constants.SOFTWARE);
      bw.write("\n");

      bw.write(Dictionary.VER);
      bw.write(Constants.VERSION);
      bw.write("\n");

      bw.write(Dictionary.DAT);
      bw.write(new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date()).toString());
      bw.write("\n");

      bw.write(Dictionary.TYP);
      bw.write(BasicDictionary.TYPE);
      bw.write("\n");

      if (language != null)
      {
        bw.write(Dictionary.LNG);
        bw.write(language);
        bw.write("\n");
      }

      if (license != null)
      {
        bw.write(Dictionary.LIC);
        bw.write(license);
        bw.write("\n");
      }

      if (info != null)
      {
        bw.write(Dictionary.INF);
        bw.write(info);
        bw.write("\n");
      }

      if (rules != null)
      {
        bw.write(Dictionary.RUL);
        bw.write(rules);
        bw.write("\n");
      }

      bw.flush();
      zos.closeEntry();

      File fileCharTmp = new File(fileName + Dictionary.C_TMP);
      outChar = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
          fileCharTmp), Constants.CHARACTER_SET_ENCODING_DEFAULT));

      File fileTypeTmp = new File(fileName + Dictionary.T_TMP);
      outType = new BufferedOutputStream(new FileOutputStream(fileTypeTmp));

      File fileLinkTmp = new File(fileName + Dictionary.L_TMP);
      outLink = new BufferedOutputStream(new FileOutputStream(fileLinkTmp));

      // save number of words
      outLink.write(totalWords >> 24);
      outLink.write(totalWords >> 16);
      outLink.write(totalWords >> 8);
      outLink.write(totalWords);

      // save number of nodes
      outLink.write(totalNodes >> 24);
      outLink.write(totalNodes >> 16);
      outLink.write(totalNodes >> 8);
      outLink.write(totalNodes);

      saveNodes();

      outChar.close();
      outType.close();
      outLink.close();

      // Create a buffer for reading the files
      byte[] buf = new byte[1024];
      int len;
      InputStream is;

      is = new FileInputStream(fileCharTmp);
      zos.putNextEntry(new ZipEntry(dictName + Dictionary.IND_C));
      while ( (len = is.read(buf)) > 0)
      {
        zos.write(buf, 0, len);
      }
      zos.closeEntry();
      is.close();

      is = new FileInputStream(fileTypeTmp);
      zos.putNextEntry(new ZipEntry(dictName + Dictionary.IND_T));
      while ( (len = is.read(buf)) > 0)
      {
        zos.write(buf, 0, len);
      }
      zos.closeEntry();
      is.close();

      is = new FileInputStream(fileLinkTmp);
      zos.putNextEntry(new ZipEntry(dictName + Dictionary.IND_L));
      while ( (len = is.read(buf)) > 0)
      {
        zos.write(buf, 0, len);
      }
      zos.closeEntry();
      is.close();

      zos.close();

      fileCharTmp.delete();
      fileTypeTmp.delete();
      fileLinkTmp.delete();

    }
    catch (IOException e)
    {
      throw new SuggesterException(e.toString());
    }

  }

  void addWordToTrie(String word) throws SuggesterException
  {
    int wordLength = word.length();
    if (wordLength > Constants.WORD_LENGTH_MAX)
    {
      log("Word '" + word + "' is too long and it is ignored. It has length: " + wordLength);
      return;
    }
    NodeEntry node;
    int pos = 0;
    for (; (pos < prevWord.length()) && (pos < wordLength); pos++)
    {
      if (prevWord.charAt(pos) != word.charAt(pos))
      {
        break;
      }
    }
    if (pos < prevWord.length())
    {
      node = nodesPos[pos + 1];
      optimizeNode(node, nodesPos[pos]);
    }
    posPrev = pos;
    node = nodesPos[pos];
    prevWord = word;
    int i = pos;
    for (; i < wordLength; i++)
    {
      char c = word.charAt(i);
      NodeEntry n = (NodeEntry) node.getChild(c);
      if (n == null)
      {
        n = new NodeEntry(c);
        node.add(n);
        node = n;
      }
      nodesPos[i + 1] = node;
    }
    node.type |= Node.NODE_TYPE_END;
  }

  void optimizeNode(NodeEntry node, NodeEntry nodeParent) throws
      SuggesterException
  {
    NodeEntry n;
    for (Link link = node.link; link != null; link = link.y)
    {
      n = (NodeEntry) link.x;
      if ( (n.type & Node.NODE_TYPE_OPTIMIZED) == 0)
      {
        optimizeNode(n, node);
      }
    }

    n = (NodeEntry) nodeTable.get(node);
    if (n != null)
    {
      nodeParent.add(n);
    }
    else
    {
      nodeTable.put(node, node);
      node.type |= Node.NODE_TYPE_OPTIMIZED;
    }
  }

  void countNodes() throws SuggesterException
  {
    offset = 0;
    ll = new LinkedList();
    ll.add(mainNode);
    while (ll.size() > 0)
    {
      NodeEntry nodeParent = (NodeEntry) ll.get(0);
      nodeParent.offset = offset;
      addOffset();
      for (Link link = nodeParent.link; link != null; link = link.y)
      {
        NodeEntry node = (NodeEntry) link.x;
        if (node.offset == 0)
        {
          ll.add(node);
          node.offset = -1;
        }
        addOffset();
      }
      ll.remove(0);
    }
    totalNodes = offset;
  }

  void saveNodes() throws SuggesterException
  {
    ll = new LinkedList();
    ll.add(mainNode);
    try
    {
      while (ll.size() > 0)
      {
        NodeEntry nodeParent = (NodeEntry) ll.get(0);
        ll.remove(0);
        for (Link link = nodeParent.link; link != null; link = link.y)
        {
          NodeEntry node = (NodeEntry) link.x;
          if ( (node.type & Node.NODE_TYPE_SAVED) == 0)
          {
            ll.add(node);
            outChar.write(node.chr);
            int type = node.type & 0x0f; // clear 4 upper bits
            if (link.y == null)
            {
              type |= Node.NODE_TYPE_LAST_Y;
            }
            if (node.link == null)
            {
              type |= Node.NODE_TYPE_LAST_X;
            }
            writeType(type);
            node.type |= Node.NODE_TYPE_SAVED;
          }
          else
          {
            int type = Node.NODE_TYPE_JUMP;
            if (link.y == null)
            {
              type |= Node.NODE_TYPE_LAST_Y;
            }
            if (node.offset <= 0xff)
            {
              outLink.write(node.offset);
            }
            else if (node.offset <= 0xffff)
            {
              type |= 1;
              outLink.write(node.offset >> 8);
              outLink.write(node.offset);
            }
            else if (node.offset <= 0xffffff)
            {
              type |= 2;
              outLink.write(node.offset >> 16);
              outLink.write(node.offset >> 8);
              outLink.write(node.offset);
            }
            else
            {
              type |= 3;
              outLink.write(node.offset >> 24);
              outLink.write(node.offset >> 16);
              outLink.write(node.offset >> 8);
              outLink.write(node.offset);
            }
            writeType(type);
          }
        }
      }
      if (currentType >= 0)
      {
        writeType(0);
      }
    }
    catch (IOException e)
    {
      throw new SuggesterException(e.toString());
    }
  }

  void addOffset() throws SuggesterException
  {
    offset++;
    if (offset < 0)
    {
      throw new SuggesterException("Too many states.");
    }
  }

  void writeType(int type) throws IOException
  {
    if (currentType < 0)
    {
      currentType = type << 4;
    }
    else
    {
      outType.write(currentType + type);
      currentType = -1;
    }
  }

  public void printWords(Node node, Writer w) throws SuggesterException
  {
    writer = w;
    try
    {
      traverse(node);
    }
    catch (IOException e)
    {
      throw new SuggesterException(e.toString());
    }
  }

  void traverse(Node node) throws IOException
  {
    chars[posPrint] = node.chr;
    if (node instanceof NodeEnd ||
        (node instanceof NodeEntry &&
         ( ( (NodeEntry) node).type & Node.NODE_TYPE_END) != 0))
    {
      for (int j = 1; j <= posPrint; j++)
      {
        writer.write(chars[j]);
      }
      writer.write("\r\n"); // next line
      counter++;
    }
    posPrint++;
    for (Link link = node.link; link != null; link = link.y)
    {
      traverse(link.x);
    }
    posPrint--;
  }

  long getMemory() // this is not working well
  {
    try
    {
      System.gc();
      Thread.yield();
      System.gc();
      Thread.sleep(100);
    }
    catch (Exception e)
    {}
    Runtime runtime = Runtime.getRuntime();
    return runtime.totalMemory() - runtime.freeMemory();
  }

}
