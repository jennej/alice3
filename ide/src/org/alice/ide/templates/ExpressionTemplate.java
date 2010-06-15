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
package org.alice.ide.templates;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionTemplate extends org.alice.ide.common.ExpressionCreatorPane {
	public ExpressionTemplate() {
		this.setDragAndDropOperation( new org.alice.ide.operations.DefaultDragOperation( edu.cmu.cs.dennisc.alice.Project.GROUP ) );
//		
//		//todo
//		this.setPopupOperation( new org.alice.ide.operations.InconsequentialActionOperation( java.util.UUID.fromString( "20e0121d-2166-4479-af43-d45e6ae425e6" ) ) {
//			@Override
//			protected void performInternal(edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.KAbstractButton< ? > button) {
//				context.cancel();
//			}
//		} );
	}
//	@Override
//	protected boolean isFauxDragDesired() {
//		return true;
//	}
	
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createIncompleteExpression();
	private boolean isInitialized = false;
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		if( this.isInitialized ) {
			//pass
		} else {
			this.refresh();
			this.isInitialized = true;
		}
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		//this.removeAllComponents();
		super.handleRemovedFrom( parent );
	}
	protected void refresh() {
		this.removeAllComponents();
		edu.cmu.cs.dennisc.alice.ast.Expression incompleteExpression = this.createIncompleteExpression();
		this.setBackgroundPaint( getIDE().getColorFor( incompleteExpression ) );
		this.addComponent( getIDE().getTemplatesFactory().createComponent( incompleteExpression ) );
	}
	@Override
	protected boolean isPressed() {
		return false;
	}
	
	protected boolean isFieldInScope() {
		return getIDE().isSelectedAccessibleInScope();
	}
	
	@Override
	protected boolean contains( int x, int y, boolean jContains ) {
		if( this.isFieldInScope() ) {
			return super.contains( x, y, jContains );
		} else {
			return false;
		}
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		super.paintEpilogue( g2, x, y, width, height );
		if( this.isFieldInScope() ) {
			//pass
		} else {
			g2.setPaint( edu.cmu.cs.dennisc.zoot.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
}
