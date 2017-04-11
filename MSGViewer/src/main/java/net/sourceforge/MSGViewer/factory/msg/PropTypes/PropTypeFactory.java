/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.PropTypes;

import java.util.HashMap;

/**
 *
 * @author martin
 */
public class PropTypeFactory 
{
   HashMap<String,PropType> props = new HashMap();
    
   public PropTypeFactory() 
   {
       // addFixed("0002");
   }
   
   
   public PropType getPropertyType( String type )
   {
       return props.get(type);
   }
}
