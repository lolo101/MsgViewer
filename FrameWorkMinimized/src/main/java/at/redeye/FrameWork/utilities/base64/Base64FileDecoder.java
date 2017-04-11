package at.redeye.FrameWork.utilities.base64;

// Sample program to decode a Base64 text file into a binary file.
// Author: Christian d'Heureuse (www.source-code.biz)

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class Base64FileDecoder {

public static void main (String args[]) throws IOException {
   if (args.length != 2) {
      System.out.println ("Command line parameters: inputFileName outputFileName");
      System.exit (9); }
   decodeFile (args[0], args[1]); }

private static void decodeFile (String inputFileName, String outputFileName) throws IOException {
   BufferedReader in = null;
   BufferedOutputStream out = null;
   try {
      in = new BufferedReader(new FileReader(inputFileName));
      out = new BufferedOutputStream(new FileOutputStream(outputFileName));
      decodeStream (in, out);
      out.flush(); }
    finally {
      if (in != null) in.close();
      if (out != null) out.close(); }}

private static void decodeStream (BufferedReader in, OutputStream out) throws IOException {
   while (true) {
      String s = in.readLine();
      if (s == null) break;
      byte[] buf = Base64Coder.decodeLines(s);
      out.write (buf); }}

} // end class Base64FileDecoder
