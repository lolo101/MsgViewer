package at.redeye.FrameWork.widgets.helpwindow;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.translation.MLUtil;

public class HelpWin extends BaseDialog {

    private static final long serialVersionUID = 1L;

    final String base;
    final HelpWinHook hook;

    public HelpWin(Root root, String Base, String ModuleName) {
        this(root, Base, ModuleName, null);
    }

    public HelpWin(Root root, String Base, String ModuleName, HelpWinHook hook) {
        super(root, "Hilfe");

        this.hook = hook;
        base = Base;

        initComponents();

        jHelp.addHyperlinkListener(new HyperlinkExecuter(root));

        loadHelp(ModuleName);
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

    protected final void loadHelp(final String ModuleName) {
        new AutoMBox<>(ModuleName, () -> {

            String module_name = getModuleName(ModuleName);

            logger.debug("Loading Help for: '" + module_name + "'");

            HelpFileLoader hfl = new HelpFileLoader();

            String res = hfl.loadHelp(base, module_name);

            return hook == null ? res : res.replace(hook.getKeyword(), hook.getText());
        }).onSuccess(result -> {
            jHelp.setText(result);
            jHelp.setCaretPosition(0);
        });
    }

    private String getModuleName(String ModuleName) {
        String locale = root.getDisplayLanguage();

        if (haveResource(HelpFileLoader.getResourceName(base, ModuleName + "_" + locale))) {
            return ModuleName + "_" + locale;
        } else if (haveResource(HelpFileLoader.getResourceName(base, ModuleName + "_" + MLUtil.getLanguageOnly(locale)))) {
            return ModuleName + "_" + MLUtil.getLanguageOnly(locale);
        } else {
            if (!MLUtil.compareLanguagesOnly(root.getBaseLanguage(), root.getDisplayLanguage())
                    && haveResource(HelpFileLoader.getResourceName(base, ModuleName + "_" + MLUtil.getLanguageOnly(root.getDefaultLanguage()))))
                return ModuleName + "_" + MLUtil.getLanguageOnly(root.getDefaultLanguage());
        }

        return ModuleName;
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
}
