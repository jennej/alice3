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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class TextComponent<J extends javax.swing.text.JTextComponent> extends ViewController<J, org.lgna.croquet.StringState> {
	private final javax.swing.border.BevelBorder outsideBorder = new javax.swing.border.BevelBorder( javax.swing.border.BevelBorder.LOWERED );
	private final edu.cmu.cs.dennisc.javax.swing.border.EmptyBorder insideBorder = new edu.cmu.cs.dennisc.javax.swing.border.EmptyBorder();
	private final javax.swing.border.CompoundBorder border = new javax.swing.border.CompoundBorder( outsideBorder, insideBorder );

	private final java.awt.event.FocusListener selectAllFocusListener = new java.awt.event.FocusListener() {
		@Override
		public void focusGained( java.awt.event.FocusEvent e ) {
			getAwtComponent().selectAll();
		}

		@Override
		public void focusLost( java.awt.event.FocusEvent e ) {
		}
	};

	public TextComponent( org.lgna.croquet.StringState model ) {
		super( model );
		J jTextComponent = this.getAwtComponent();
		model.getSwingModel().install( this );
		jTextComponent.setBorder( this.border );
		jTextComponent.setEnabled( model.isEnabled() );
		this.setMargin( new java.awt.Insets( 4, 4, 2, 2 ) );
		this.setBackgroundColor( new java.awt.Color( 255, 255, 221 ) );
	}

	public boolean isEditable() {
		return this.getAwtComponent().isEditable();
	}

	public void setEditable( boolean isEditable ) {
		this.getAwtComponent().setEditable( isEditable );
	}

	public void enableSelectAllWhenFocusGained() {
		this.getAwtComponent().addFocusListener( this.selectAllFocusListener );
	}

	public void disableSelectAllWhenFocusGained() {
		this.getAwtComponent().removeFocusListener( this.selectAllFocusListener );
	}

	public java.awt.Insets getMargin() {
		//return this.getAwtComponent().getMargin();
		return this.insideBorder.getBorderInsets();
	}

	public void setMargin( java.awt.Insets margin ) {
		//this.getAwtComponent().setMargin( margin );
		this.insideBorder.setBorderInsets( margin );
	}

	public void selectAll() {
		this.getAwtComponent().selectAll();
	}

	public abstract void updateTextForBlankCondition( String textForBlankCondition );
}
