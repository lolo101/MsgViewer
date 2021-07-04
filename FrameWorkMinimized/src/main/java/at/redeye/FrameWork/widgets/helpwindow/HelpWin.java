package at.redeye.FrameWork.widgets.helpwindow;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.translation.MLUtil;

public class HelpWin extends BaseDialog {

    private static final long serialVersionUID = 1L;

    String base;
    HelpWinHook hook = null;

    /**
     * Creates new form HelpWin
     */
    public HelpWin(Root root, String Base, String ModuleName) {
        super(root, "Hilfe");

        base = Base;

        initComponents();

        jHelp.addHyperlinkListener(new HyperlinkExecuter(root));

        loadHelp(ModuleName);
    }

    public HelpWin(Root root, String Base, String ModuleName, HelpWinHook hook) {
        super(root, "Hilfe");

        this.hook = hook;
        base = Base;

        initComponents();

        jHelp.addHyperlinkListener(new HyperlinkExecuter(root));

        loadHelp(ModuleName);
    }

    protected void loadHelp(final String ModuleName) {
        new AutoMBox(ModuleName, () -> {

            String locale = root.getDisplayLanguage();

            String module_name = null;

            if (haveResource(HelpFileLoader.getResourceName(base, ModuleName + "_" + locale))) {
                module_name = ModuleName + "_" + locale;
            } else if (haveResource(HelpFileLoader.getResourceName(base, ModuleName + "_" + MLUtil.getLanguageOnly(locale)))) {
                module_name = ModuleName + "_" + MLUtil.getLanguageOnly(locale);
            } else {
                if (!MLUtil.compareLanguagesOnly(root.getBaseLanguage(), root.getDisplayLanguage())) {
                    if (haveResource(HelpFileLoader.getResourceName(base, ModuleName + "_" + MLUtil.getLanguageOnly(root.getDefaultLanguage()))))
                        module_name = ModuleName + "_" + MLUtil.getLanguageOnly(root.getDefaultLanguage());
                }
            }

            if (module_name == null)
                module_name = ModuleName;

            logger.debug("Loading Help for: '" + module_name + "'");

            HelpFileLoader hfl = new HelpFileLoader();

            String res = hfl.loadHelp(base, module_name);

            if (hook != null) {
                res = res.replace(hook.getKeyword(), hook.getText());
            }

            jHelp.setText(res);
            jHelp.setCaretPosition(0);
        });
    }

    static boolean haveResource(String resource_name) {
        boolean result = MLUtil.haveResource(resource_name);

        if (logger.isDebugEnabled()) {
            if (result)
                logger.debug("loaded: " + resource_name);
            else
                logger.debug("failed loading: " + resource_name);
        }

        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jHelp = new javax.swing.JEditorPane();
        javax.swing.JButton jBClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jHelp.setContentType("text/html"); // NOI18N
        jHelp.setEditable(false);
        jScrollPane1.setViewportView(jHelp);

        jBClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/fileclose.gif"))); // NOI18N
        jBClose.setText("Schließen");
        jBClose.addActionListener(this::jBCloseActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
                                        .addComponent(jBClose, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addGap(12, 12, 12)
                                .addComponent(jBClose)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCloseActionPerformed

        close();

    }//GEN-LAST:event_jBCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jHelp;
    // End of variables declaration//GEN-END:variables


}
