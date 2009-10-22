/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.preferencesinputpane;

/**
 * @author Dennis Cosgrove
 */
public class ProgrammingOuterPreferencesPane extends OuterPreferencesPane {
	class ConfigurationSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation<org.alice.ide.preferences.programming.Configuration> {
		public ConfigurationSelectionOperation( org.alice.ide.preferences.programming.Configuration... panes ) {
			super( new javax.swing.DefaultComboBoxModel( panes ) );
		}
		
		@Override
		protected void handleSelectionChange(org.alice.ide.preferences.programming.Configuration value) {
			ProgrammingOuterPreferencesPane.this.preview.updateValues(value);
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
	}

	class ConfigurationComboBox extends edu.cmu.cs.dennisc.zoot.ZComboBox {
		public ConfigurationComboBox( org.alice.ide.preferences.programming.Configuration[] configurations ) {
			super( new ConfigurationSelectionOperation( configurations ) );
			//this.setCellRenderer( new PerspectiveListCellRenderer() );
		}
	}
	
	class ConfigurationPreview extends edu.cmu.cs.dennisc.croquet.swing.FormPane {
		private edu.cmu.cs.dennisc.zoot.ZLabel isDefaultFieldNameGenerationDesiredLabel;
		private edu.cmu.cs.dennisc.zoot.ZLabel isSyntaxNoiseDesiredLabel;
		private void ensureLabelsExist() {
			if( this.isDefaultFieldNameGenerationDesiredLabel != null ) {
				//pass
			} else {
				this.isDefaultFieldNameGenerationDesiredLabel = edu.cmu.cs.dennisc.zoot.ZLabel.acquire();
				this.isDefaultFieldNameGenerationDesiredLabel.setForeground( java.awt.Color.GRAY );
			}
			if( this.isSyntaxNoiseDesiredLabel != null ) {
				//pass
			} else {
				this.isSyntaxNoiseDesiredLabel = edu.cmu.cs.dennisc.zoot.ZLabel.acquire();
				this.isSyntaxNoiseDesiredLabel.setForeground( java.awt.Color.GRAY );
			}
		}
		@Override
		protected java.util.List<java.awt.Component[]> addComponentRows(java.util.List<java.awt.Component[]> rv) {
			this.ensureLabelsExist();
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createColumn0Label( "isDefaultFieldNameGenerationDesired:" ), this.isDefaultFieldNameGenerationDesiredLabel ) );
			rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createColumn0Label( "isSyntaxNoiseDesired:" ), this.isSyntaxNoiseDesiredLabel ) );
			return rv;
		}
		
		public void updateValues( org.alice.ide.preferences.programming.Configuration value ) {
			this.ensureLabelsExist();
			this.isDefaultFieldNameGenerationDesiredLabel.setText( Boolean.toString( value.isDefaultFieldNameGenerationDesired() ).toUpperCase() );
			this.isSyntaxNoiseDesiredLabel.setText( Boolean.toString( value.isSyntaxNoiseDesired() ).toUpperCase() );
		}
	}

	private ConfigurationPreview preview;
	
	class EditVariantOperation extends org.alice.ide.operations.AbstractActionOperation {
		public EditVariantOperation() {
			this.putValue( javax.swing.Action.NAME, "Edit..." );
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		}
	}
	class RemoveVariantOperation extends org.alice.ide.operations.AbstractActionOperation {
		public RemoveVariantOperation() {
			this.putValue( javax.swing.Action.NAME, "Remove" );
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		}
	}
	class NewVariantOperation extends org.alice.ide.operations.AbstractActionOperation {
		public NewVariantOperation() {
			this.putValue( javax.swing.Action.NAME, "New..." );
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		}
	}
	class ImportVariantOperation extends org.alice.ide.operations.AbstractActionOperation {
		public ImportVariantOperation() {
			this.putValue( javax.swing.Action.NAME, "Import..." );
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		}
	}
	
	public ProgrammingOuterPreferencesPane() {
		super("Programming", org.alice.ide.preferences.ProgrammingPreferences.getSingleton());
	}
	
	@Override
	protected java.awt.Component createCenterComponent(edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences ) {
		org.alice.ide.preferences.programming.Configuration[] configurations = org.alice.ide.preferences.ProgrammingPreferences.getSingleton().getBuiltInPreferenceNodes();
		ConfigurationComboBox activeConfigurationComboBox = new ConfigurationComboBox( configurations );
		activeConfigurationComboBox.setSelectedIndex( 0 );
		edu.cmu.cs.dennisc.zoot.ZButton editButton = new edu.cmu.cs.dennisc.zoot.ZButton( new EditVariantOperation() );
		editButton.setEnabled( false );
		editButton.setToolTipText( "coming soon" );
		edu.cmu.cs.dennisc.zoot.ZButton removeButton = new edu.cmu.cs.dennisc.zoot.ZButton( new RemoveVariantOperation() );
		removeButton.setEnabled( false );
		removeButton.setToolTipText( "coming soon" );
		edu.cmu.cs.dennisc.zoot.ZButton newButton = new edu.cmu.cs.dennisc.zoot.ZButton( new NewVariantOperation() );
		newButton.setEnabled( false );
		newButton.setToolTipText( "coming soon" );
		edu.cmu.cs.dennisc.zoot.ZButton importButton = new edu.cmu.cs.dennisc.zoot.ZButton( new ImportVariantOperation() );
		importButton.setEnabled( false );
		importButton.setToolTipText( "coming soon" );
		edu.cmu.cs.dennisc.croquet.swing.LineAxisPane northTopPane = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane( activeConfigurationComboBox, editButton, removeButton );
		edu.cmu.cs.dennisc.croquet.swing.LineAxisPane northBottomPane = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane( newButton, importButton, javax.swing.Box.createHorizontalGlue() );

		this.preview = new ConfigurationPreview();
		this.preview.updateValues( (org.alice.ide.preferences.programming.Configuration)activeConfigurationComboBox.getSelectedItem() );
		edu.cmu.cs.dennisc.croquet.swing.PageAxisPane rv = new edu.cmu.cs.dennisc.croquet.swing.PageAxisPane(
				edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "active variant:" ),
				javax.swing.Box.createVerticalStrut( 4 ),  
				northTopPane, 
				javax.swing.Box.createVerticalStrut( 4 ),  
				northBottomPane,
				javax.swing.Box.createVerticalStrut( 32 ),
				//edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "preview:" ),
				//javax.swing.Box.createVerticalStrut( 4 ),  
				this.preview
		);
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 4 ) );
		return rv;
	}
	@Override
	protected boolean isCenterComponentScrollPaneDesired() {
		return false;
	}
}
