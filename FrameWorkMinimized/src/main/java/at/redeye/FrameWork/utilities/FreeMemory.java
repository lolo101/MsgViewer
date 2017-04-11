/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities;

public class FreeMemory {
    
   private static long fm = 0;	
   private static long tm = 0;
   private static long mm = 0;
   private static long um = 0;
           
   public static String getMeminfo()
   {
      Runtime rt = Runtime.getRuntime();
      
      fm = rt.freeMemory();
      tm = rt.totalMemory();
      mm = rt.maxMemory();      
      um = tm-fm;
      
      StringBuilder res = new StringBuilder();
            
       res.append("Free memory = " + (fm / 1024 / 1024) + "m\n");
       res.append("Total memory = " + (tm / 1024 / 1024) + "m\n");
       res.append("Maximum memory = " + (mm / 1024 / 1024) + "m\n");
       res.append("Memory used = " + (um / 1024 / 1024) + "m\n");

      
      return res.toString();
   }   
}
