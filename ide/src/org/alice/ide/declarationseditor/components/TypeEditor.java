/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.alice.ide.declarationseditor.components;

/**
 * @author Dennis Cosgrove
 */
public class TypeEditor extends org.lgna.croquet.components.BorderPanel {
	private final org.lgna.croquet.State.ValueListener<Boolean> isEmphasizingClassesListener = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			TypeEditor.this.handleIsEmphasizingClassesChanged();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.project.ast.NamedUserType> typeListener = new org.lgna.croquet.State.ValueListener<org.lgna.project.ast.NamedUserType>() {
		public void changing( org.lgna.croquet.State<org.lgna.project.ast.NamedUserType> state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<org.lgna.project.ast.NamedUserType> state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			TypeEditor.this.handleTypeStateChanged( nextValue );
		}
	};

	private final org.lgna.croquet.components.FolderTabbedPane<org.alice.ide.declarationseditor.DeclarationComposite> tabbedPane;
	private final org.lgna.croquet.components.PopupButton popupButton;

	public TypeEditor( org.alice.ide.declarationseditor.DeclarationsEditorComposite composite ) {
		super( composite );
		org.lgna.croquet.components.LineAxisPanel headerTrailingComponent = new org.lgna.croquet.components.LineAxisPanel(
				composite.getControlsComposite().getView(),
				org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 12 ),
				org.alice.ide.clipboard.Clipboard.SINGLETON.getDragComponent()
				);

		final boolean IS_RECYCLE_BIN_READY_FOR_PRIME_TIME = false;
		if( IS_RECYCLE_BIN_READY_FOR_PRIME_TIME ) {
			headerTrailingComponent.addComponent( new org.alice.ide.recyclebin.RecycleBinView() );
		}
		headerTrailingComponent.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 2, 0, 2 ) );
		this.tabbedPane = composite.getTabState().createFolderTabbedPane();
		this.tabbedPane.setHeaderTrailingComponent( headerTrailingComponent );
		this.popupButton = org.alice.ide.declarationseditor.TypeState.getInstance().getCascadeRoot().getPopupPrepModel().createPopupButton();
		this.addCenterComponent( tabbedPane );
	}

	public org.alice.ide.codedrop.CodePanelWithDropReceptor getCodeDropReceptorInFocus() {
		org.alice.ide.declarationseditor.DeclarationsEditorComposite composite = (org.alice.ide.declarationseditor.DeclarationsEditorComposite)this.getComposite();
		org.alice.ide.declarationseditor.DeclarationComposite item = composite.getTabState().getValue();
		if( item != null ) {
			org.lgna.croquet.components.JComponent<?> component = this.tabbedPane.getMainComponentFor( item );
			if( component instanceof org.alice.ide.declarationseditor.code.components.CodeDeclarationView ) {
				return ( (org.alice.ide.declarationseditor.code.components.CodeDeclarationView)component ).getCodePanelWithDropReceptor();
			}
		}
		return null;
	}

	private void handleIsEmphasizingClassesChanged() {
		org.lgna.croquet.components.JComponent<?> component;
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			component = this.popupButton;
		} else {
			component = null;
		}
		this.tabbedPane.setHeaderLeadingComponent( component );
	}

	private void handleTypeStateChanged( org.lgna.project.ast.NamedUserType nextValue ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = org.alice.ide.declarationseditor.TypeState.getInstance().getValue();
		org.alice.ide.common.TypeDropDownIcon icon = new org.alice.ide.common.TypeDropDownIcon( type, this.popupButton.getAwtComponent().getModel() );
		this.popupButton.setClobberIcon( icon );
		this.popupButton.revalidateAndRepaint();
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().addAndInvokeValueListener( this.isEmphasizingClassesListener );
		org.alice.ide.declarationseditor.TypeState.getInstance().addAndInvokeValueListener( this.typeListener );
	}

	@Override
	protected void handleUndisplayable() {
		org.alice.ide.declarationseditor.TypeState.getInstance().removeValueListener( this.typeListener );
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().removeValueListener( this.isEmphasizingClassesListener );
		super.handleUndisplayable();
	}
}
