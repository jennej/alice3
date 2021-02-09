/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package org.alice.netbeans;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import java.io.File;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.filesystems.FileUtil;

public class Alice3ProjectTemplatePanelVisual extends JPanel implements DocumentListener {

  private static final boolean IS_AUTOMATIC_FILL_IN_ALICE_PROJECT_DESIRED = "C:\\Users\\dennisc".contentEquals(System.getProperty("user.home"));
  public static final String PROP_PROJECT_NAME = "projectName";
  public static final String browseCommand = "BROWSE";

  private Alice3ProjectTemplateWizardPanel panel;

  public Alice3ProjectTemplatePanelVisual(Alice3ProjectTemplateWizardPanel panel) {
    initComponents();
    this.panel = panel;
    // Register listener on the textFields to make the automatic updates
    projectNameTextField.getDocument().addDocumentListener(this);
    projectLocationTextField.getDocument().addDocumentListener(this);
    aliceWorldLocationTextField.getDocument().addDocumentListener(this);
  }

  public String getProjectName() {
    return this.projectNameTextField.getText();
  }

  private String getCreatedFolderPath(String projectFolder, String projectName) {
    return projectFolder + File.separatorChar + projectName;
  }

  private String getAvailableProjectName(String baseProjectName) {
    try {
      String projectFolder = projectLocationTextField.getText();
      String candidateProjectName;
      for (int i = 1; i < 100; i++) {
        if (i > 1) {
          candidateProjectName = baseProjectName + i;
        } else {
          candidateProjectName = baseProjectName;
        }
        String createdFolderPath = this.getCreatedFolderPath(projectFolder, candidateProjectName);
        File file = new File(createdFolderPath);
        if (file.exists()) {
          //pass
        } else {
          return candidateProjectName;
        }
      }
      return baseProjectName;
    } catch (Throwable t) { // should not happen
      Logger.throwable(t, baseProjectName);
      return baseProjectName;
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    projectNameLabel = new javax.swing.JLabel();
    projectNameTextField = new javax.swing.JTextField();
    projectLocationLabel = new javax.swing.JLabel();
    projectLocationTextField = new javax.swing.JTextField();
    browseButton = new javax.swing.JButton();
    createdFolderLabel = new javax.swing.JLabel();
    createdFolderTextField = new javax.swing.JTextField();
    toNetBeansHeaderLabel = new javax.swing.JLabel();
    aliceWorldLocationLabel = new javax.swing.JLabel();
    aliceWorldLocationTextField = new javax.swing.JTextField();
    aliceWorldBrowseButton = new javax.swing.JButton();
    fromAliceHeaderLabel = new javax.swing.JLabel();

    projectNameLabel.setLabelFor(projectNameTextField);
    org.openide.awt.Mnemonics.setLocalizedText(projectNameLabel, org.openide.util.NbBundle.getMessage(Alice3ProjectTemplatePanelVisual.class, "Alice3ProjectTemplatePanelVisual.projectNameLabel.text"));

    projectLocationLabel.setLabelFor(projectLocationTextField);
    org.openide.awt.Mnemonics.setLocalizedText(projectLocationLabel, org.openide.util.NbBundle.getMessage(Alice3ProjectTemplatePanelVisual.class, "Alice3ProjectTemplatePanelVisual.projectLocationLabel.text"));

    org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(Alice3ProjectTemplatePanelVisual.class, "Alice3ProjectTemplatePanelVisual.browseButton.text"));
    browseButton.setActionCommand(browseCommand);
    browseButton.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browseButtonActionPerformed(evt);
      }
    });

    createdFolderLabel.setLabelFor(createdFolderTextField);
    org.openide.awt.Mnemonics.setLocalizedText(createdFolderLabel, org.openide.util.NbBundle.getMessage(Alice3ProjectTemplatePanelVisual.class, "Alice3ProjectTemplatePanelVisual.createdFolderLabel.text"));

    createdFolderTextField.setEditable(false);

    toNetBeansHeaderLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
    org.openide.awt.Mnemonics.setLocalizedText(toNetBeansHeaderLabel, org.openide.util.NbBundle.getMessage(Alice3ProjectTemplatePanelVisual.class, "Alice3ProjectTemplatePanelVisual.toNetBeansHeaderLabel.text"));

    aliceWorldLocationLabel.setLabelFor(aliceWorldLocationTextField);
    org.openide.awt.Mnemonics.setLocalizedText(aliceWorldLocationLabel, org.openide.util.NbBundle.getMessage(Alice3ProjectTemplatePanelVisual.class, "Alice3ProjectTemplatePanelVisual.aliceWorldLocationLabel.text"));

    org.openide.awt.Mnemonics.setLocalizedText(aliceWorldBrowseButton, org.openide.util.NbBundle.getMessage(Alice3ProjectTemplatePanelVisual.class, "Alice3ProjectTemplatePanelVisual.aliceWorldBrowseButton.text"));
    aliceWorldBrowseButton.setActionCommand(browseCommand);
    aliceWorldBrowseButton.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        aliceWorldBrowseButtonActionPerformed(evt);
      }
    });

    fromAliceHeaderLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
    org.openide.awt.Mnemonics.setLocalizedText(fromAliceHeaderLabel, org.openide.util.NbBundle.getMessage(Alice3ProjectTemplatePanelVisual.class, "Alice3ProjectTemplatePanelVisual.fromAliceHeaderLabel.text"));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(toNetBeansHeaderLabel).addGap(0, 0, Short.MAX_VALUE)).addGroup(
        layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(fromAliceHeaderLabel).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(projectNameLabel).addComponent(projectLocationLabel).addComponent(createdFolderLabel).addComponent(aliceWorldLocationLabel)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(aliceWorldLocationTextField).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(aliceWorldBrowseButton)).addGroup(
            layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(projectNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE).addComponent(projectLocationTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE).addComponent(createdFolderTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(browseButton))))).addContainerGap()));
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(fromAliceHeaderLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(aliceWorldLocationLabel).addComponent(aliceWorldLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(aliceWorldBrowseButton)).addGap(18, 18, 18).addComponent(toNetBeansHeaderLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(projectNameLabel).addComponent(projectNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(projectLocationLabel).addComponent(projectLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(browseButton))
                                                                                                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(createdFolderLabel).addComponent(createdFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(0, 166, Short.MAX_VALUE)));
  } // </editor-fold>//GEN-END:initComponents

  private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_browseButtonActionPerformed
    String command = evt.getActionCommand();
    if (browseCommand.equals(command)) {
      JFileChooser chooser = new JFileChooser();
      FileUtil.preventFileChooserSymlinkTraversal(chooser, null);
      // TODO I18n
      chooser.setDialogTitle("Select Project Location");
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      String path = this.projectLocationTextField.getText();
      if (path.length() > 0) {
        File f = new File(path);
        if (f.exists()) {
          chooser.setSelectedFile(f);
        }
      }
      if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) {
        File projectDir = chooser.getSelectedFile();
        projectLocationTextField.setText(FileUtil.normalizeFile(projectDir).getAbsolutePath());
      }
      panel.fireChangeEvent();
    }

  } //GEN-LAST:event_browseButtonActionPerformed

  private static java.io.File getMyProjectsDirectory() {
    return new File(FileUtilities.getDefaultDirectory(), "Alice3/MyProjects");
  }

  private static String getProjectNameForFile(String projectFile) {
    int splitPoint = projectFile.lastIndexOf('.');
    if (splitPoint > 0) {
      return projectFile.substring(0, splitPoint);
    } else {
      return projectFile;
    }
  }

  private static boolean isAliceFile(File worldFile) {
    String extension = edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension(worldFile);
    return (extension != null && extension.equalsIgnoreCase(org.lgna.project.io.IoUtilities.PROJECT_EXTENSION));
  }

  private void aliceWorldBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_aliceWorldBrowseButtonActionPerformed
    String command = evt.getActionCommand();
    if (browseCommand.equals(command)) {
      JFileChooser chooser = new JFileChooser();
      FileUtil.preventFileChooserSymlinkTraversal(chooser, null);
      // TODO I18n
      chooser.setDialogTitle("Select Alice Project to Import");
      //chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setCurrentDirectory(getMyProjectsDirectory());
      String path = this.aliceWorldLocationTextField.getText();
      if (path.length() > 0) {
        File f = new File(path);
        if (f.exists()) {
          chooser.setSelectedFile(f);
        }
      }
      if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) {
        File aliceWorld = chooser.getSelectedFile();
        if (aliceWorld.isFile() && isAliceFile(aliceWorld)) {
          aliceWorldLocationTextField.setText(FileUtil.normalizeFile(aliceWorld).getAbsolutePath());

          String projectName = getProjectNameForFile(aliceWorld.getName());
          String availableProjectName = this.getAvailableProjectName(projectName);
          projectNameTextField.setText(availableProjectName);
        }
      }
      panel.fireChangeEvent();
    }
  } //GEN-LAST:event_aliceWorldBrowseButtonActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton aliceWorldBrowseButton;
  private javax.swing.JLabel aliceWorldLocationLabel;
  private javax.swing.JTextField aliceWorldLocationTextField;
  private javax.swing.JButton browseButton;
  private javax.swing.JLabel createdFolderLabel;
  private javax.swing.JTextField createdFolderTextField;
  private javax.swing.JLabel fromAliceHeaderLabel;
  private javax.swing.JLabel projectLocationLabel;
  private javax.swing.JTextField projectLocationTextField;
  private javax.swing.JLabel projectNameLabel;
  private javax.swing.JTextField projectNameTextField;
  private javax.swing.JLabel toNetBeansHeaderLabel;
  // End of variables declaration//GEN-END:variables

  @Override
  public void addNotify() {
    super.addNotify();
    if (IS_AUTOMATIC_FILL_IN_ALICE_PROJECT_DESIRED) {
      final File file = new File(FileUtilities.getDefaultDirectory(), "Alice3/MyProjects/a.a3p");
      if (file.exists()) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            aliceWorldLocationTextField.setText(file.getAbsolutePath());
            String projectName = getProjectNameForFile(file.getName());
            String availableProjectName = getAvailableProjectName(projectName);
            projectNameTextField.setText(availableProjectName);
          }
        });
      }
    }
    //same problem as in 31086, initial focus on Cancel button
    projectNameTextField.requestFocus();
    projectNameTextField.selectAll();
  }

  boolean valid(WizardDescriptor wizardDescriptor) {
    String aliceFile = aliceWorldLocationTextField.getText().trim();
    if (aliceFile.length() == 0) {
      //this.aliceWorldLocationLabel.setForeground(Color.RED);
      // TODO I18n
      wizardDescriptor.putProperty("WizardPanel_errorMessage", "Alice Project Location is not set yet.");
      return false;
    }
    File aliceWorldFile = new File(aliceFile);
    if (!aliceWorldFile.exists()) {
      //this.aliceWorldLocationLabel.setForeground(Color.RED);
      // TODO I18n
      wizardDescriptor.putProperty("WizardPanel_errorMessage", "Alice Project " + aliceFile + " does not exist.");
      return false;
    }

    if (projectNameTextField.getText().length() == 0) {
      // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_ERROR_MESSAGE:
      // TODO I18n
      wizardDescriptor.putProperty("WizardPanel_errorMessage", "Project Name is not a valid folder name.");
      return false; // Display name not specified
    }
    File f = FileUtil.normalizeFile(new File(projectLocationTextField.getText()).getAbsoluteFile());
    if (!f.isDirectory()) {
      // TODO I18n
      wizardDescriptor.putProperty("WizardPanel_errorMessage", "Project Folder is not a valid path.");
      return false;
    }
    final File destFolder = FileUtil.normalizeFile(new File(createdFolderTextField.getText()).getAbsoluteFile());
    // Only do these checks the first time through
    if (!Objects.equals(checkedDestinationFolder, destFolder)) {
      File projLoc = destFolder;
      while (projLoc != null && !projLoc.exists()) {
        projLoc = projLoc.getParentFile();
      }
      if (projLoc == null || !projLoc.canWrite()) {
        // TODO I18n
        wizardDescriptor.putProperty("WizardPanel_errorMessage", "Project Folder cannot be created.");
        return false;
      }

      if (FileUtil.toFileObject(projLoc) == null) {
        // TODO I18n
        wizardDescriptor.putProperty("WizardPanel_errorMessage", "Project Folder is not a valid path.");
        return false;
      }

      File[] kids = destFolder.listFiles();
      if (destFolder.exists() && kids != null && kids.length > 0) {
        // Folder exists and is not empty
        // TODO I18n
        wizardDescriptor.putProperty("WizardPanel_errorMessage", "Project Folder already exists and is not empty.");
        return false;
      }
      checkedDestinationFolder = destFolder;
    }
    wizardDescriptor.putProperty("WizardPanel_errorMessage", "");
    return true;
  }

  void store(WizardDescriptor d) {
    String aliceFile = aliceWorldLocationTextField.getText().trim();
    String name = projectNameTextField.getText().trim();
    String folder = createdFolderTextField.getText().trim();

    d.putProperty("aliceProjectFile", new File(aliceFile));
    d.putProperty("projdir", new File(folder));
    d.putProperty("name", name);
  }

  void read(WizardDescriptor settings) {
    File projectLocation = (File) settings.getProperty("projdir");
    if (projectLocation == null || projectLocation.getParentFile() == null || !projectLocation.getParentFile().isDirectory()) {
      projectLocation = ProjectChooser.getProjectsFolder();
    } else {
      projectLocation = projectLocation.getParentFile();
    }
    this.projectLocationTextField.setText(projectLocation.getAbsolutePath());

    String projectName = (String) settings.getProperty("name");
    //    if (projectName == null) {
    //      projectName = "Alice3ProjectTemplate";
    //    }
    this.projectNameTextField.setText(projectName);
    this.projectNameTextField.selectAll();
  }

  void validate(WizardDescriptor d) throws WizardValidationException {
    // nothing to validate
  }

  // Implementation of DocumentListener --------------------------------------
  @Override
  public void changedUpdate(DocumentEvent e) {
    updateTexts(e);
    if (this.projectNameTextField.getDocument() == e.getDocument()) {
      firePropertyChange(PROP_PROJECT_NAME, null, this.projectNameTextField.getText());
    }
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    updateTexts(e);
    if (this.projectNameTextField.getDocument() == e.getDocument()) {
      firePropertyChange(PROP_PROJECT_NAME, null, this.projectNameTextField.getText());
    }
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    updateTexts(e);
    if (this.projectNameTextField.getDocument() == e.getDocument()) {
      firePropertyChange(PROP_PROJECT_NAME, null, this.projectNameTextField.getText());
    }
  }

  /**
   * Handles changes in the Project name and project directory,
   */
  private void updateTexts(DocumentEvent e) {

    Document doc = e.getDocument();

    if (doc == projectNameTextField.getDocument() || doc == projectLocationTextField.getDocument()) {
      // Change in the project name

      String projectFolder = projectLocationTextField.getText();
      String projectName = projectNameTextField.getText();

      //if (projectFolder.trim().length() == 0 || projectFolder.equals(oldName)) {
      createdFolderTextField.setText(this.getCreatedFolderPath(projectFolder, projectName));
      //}

    }
    panel.fireChangeEvent(); // Notify that the panel changed
  }

  File checkedDestinationFolder;
}
