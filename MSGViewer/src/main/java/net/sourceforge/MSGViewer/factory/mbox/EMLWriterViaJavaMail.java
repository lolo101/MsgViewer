/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.mbox;

/**
 *
 * @author kingleo
 */
public class EMLWriterViaJavaMail extends MBoxWriterViaJavaMail {
 
    @Override
    public String getExtension()
    {
        return "eml";
    }
}
