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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class BooleanStateOperation extends Operation {
	private javax.swing.ButtonModel buttonModel = new javax.swing.JToggleButton.ToggleButtonModel();
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
		}
	};
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			boolean prev;
			boolean next;
			if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
				prev = false;
				next = true;
			} else {
				prev = true;
				next = false;
			}
			Application.performIfAppropriate( BooleanStateOperation.this, e, Application.CANCEL_IS_FUTILE, prev, next );
		}
	};

	private String trueText = null;
	private String falseText = null;
	public BooleanStateOperation( java.util.UUID groupUUID, Boolean initialState, String trueText, String falseText ) {
		super( groupUUID );
		this.buttonModel.setSelected( initialState );
		this.buttonModel.addItemListener( this.itemListener );
		this.setTrueText( trueText );
		this.setFalseText( falseText );
	}
	public BooleanStateOperation( java.util.UUID groupUUID, Boolean initialState, String trueAndFalseText ) {
		this( groupUUID, initialState, trueAndFalseText, trueAndFalseText );
	}
	public Boolean getState() {
		return this.buttonModel.isSelected();
	}

	public javax.swing.ButtonModel getButtonModel() {
		return this.buttonModel;
	}

	public String getTrueText() {
		return this.trueText;
	}

	public void setTrueText( String trueText ) {
		this.trueText = trueText;
		this.updateName();
	}

	public String getFalseText() {
		return this.falseText;
	}

	public void setFalseText( String falseText ) {
		this.falseText = falseText;
		this.updateName();
	}

	public void addAbstractButton(javax.swing.AbstractButton abstractButton) {
		abstractButton.setAction( this.action );
		abstractButton.setModel( this.buttonModel );
		this.addComponent(abstractButton);
	}
	public void removeAbstractButton(javax.swing.AbstractButton abstractButton) {
		this.removeComponent(abstractButton);
		//abstractButton.setModel( null );
		//abstractButton.setAction( null );
	}

	public final void performStateChange( BooleanStateContext booleanStateContext ) {
		class BooleanStateEdit extends Edit {
			private boolean prevValue;
			private boolean nextValue;

			public BooleanStateEdit( boolean prevValue, boolean nextValue ) {
				this.prevValue = prevValue;
				this.nextValue = nextValue;
			}
			@Override
			public void doOrRedo( boolean isDo ) {
				BooleanStateOperation.this.setValue( this.nextValue );
			}

			@Override
			public void undo() {
				BooleanStateOperation.this.setValue( this.prevValue );
			}

			@Override
			protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
				rv.append( "boolean: " );
				rv.append( this.nextValue );
				return rv;
			}
		}
		booleanStateContext.commitAndInvokeDo( new BooleanStateEdit( booleanStateContext.getPreviousValue(), booleanStateContext.getNextValue() ) );
	}
	
	private void updateName() {
		String name;
		if( this.getState() ) {
			name = this.trueText;
		} else {
			name = this.falseText;
		}
		this.action.putValue( javax.swing.Action.NAME, name );
	}
	private void setValue( boolean value ) {
		this.buttonModel.removeItemListener( itemListener );
		this.buttonModel.setSelected( value );
		//this.handleStateChange( value );
		this.buttonModel.addItemListener( itemListener );
		this.updateName();
	}
}
