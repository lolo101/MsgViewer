/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets;

import at.redeye.FrameWork.widgets.NoticeIfChangedTextField;
import java.awt.Component;
import java.awt.Container;
import javax.swing.LayoutFocusTraversalPolicy;

/**
 *
 * @author martin
 */
public class BetterKnownFocus extends LayoutFocusTraversalPolicy
{
    @Override
   public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
   {
      if( aComponent instanceof NoticeIfChangedTextField )
      {
          NoticeIfChangedTextField field = (NoticeIfChangedTextField) aComponent;

          Component comp = field.get_next_on_focus();

          if( comp != null )
              return comp;
      }

      return super.getComponentAfter(focusCycleRoot, aComponent) ;
   }

    @Override
   public Component getComponentBefore(Container focusCycleRoot, Component aComponent)
   {
      if( aComponent instanceof NoticeIfChangedTextField )
      {
          NoticeIfChangedTextField field = (NoticeIfChangedTextField) aComponent;

          Component comp = field.get_prev_on_focus();

          if( comp != null )
              return comp;
      }

      return super.getComponentBefore(focusCycleRoot, aComponent) ;
   }
}
