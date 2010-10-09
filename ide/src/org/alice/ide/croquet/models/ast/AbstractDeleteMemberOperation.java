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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDeleteMemberOperation< N extends edu.cmu.cs.dennisc.alice.ast.AbstractMember > extends org.alice.ide.operations.ActionOperation implements org.alice.ide.croquet.models.ResponsibleModel {
	private N member;
	private edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> delaringType;
	
	//todo
	//note: index not preserved and restored
	//in the case where it is undone across sessions, it will not know where to insert the declaration
	private transient int index = -1;
	
	public AbstractDeleteMemberOperation( java.util.UUID individualId, N node, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> delaringType ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, individualId );
		this.member = node;
		this.delaringType = delaringType;
	}
	protected abstract Class<N> getNodeParameterType();
	@Override
	protected edu.cmu.cs.dennisc.croquet.CodableResolver< AbstractDeleteMemberOperation > createCodableResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< AbstractDeleteMemberOperation >( this, new edu.cmu.cs.dennisc.alice.ast.Node[] {
				this.member,
				this.delaringType
		}, new Class[] {
				this.getNodeParameterType(),
				edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice.class
		} );
	}
	
	protected N getMember() {
		return this.member;
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.NodeListProperty<N> getNodeListProperty( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> declaringType );
	protected abstract boolean isClearToDelete( N node );
	
	public void doOrRedoInternal( boolean isDo ) {
		edu.cmu.cs.dennisc.alice.ast.NodeListProperty<N> owner = this.getNodeListProperty( this.delaringType );
		this.index = owner.indexOf( this.member );
		owner.remove( index );
	}
	public void undoInternal() {
		edu.cmu.cs.dennisc.alice.ast.NodeListProperty<N> owner = this.getNodeListProperty( this.delaringType );
		if( this.index == -1 ) {
			this.index += owner.size();
		}
		owner.add( this.index, member );
	}
	public void addKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		org.alice.ide.croquet.edits.DependentEdit<AbstractDeleteMemberOperation<N>> replacementEdit = (org.alice.ide.croquet.edits.DependentEdit<AbstractDeleteMemberOperation<N>>)edit;
		AbstractDeleteMemberOperation<N> replacement = replacementEdit.getModel();
		retargeter.addKeyValuePair( this.member, replacement.member );
		retargeter.addKeyValuePair( this.delaringType, replacement.delaringType );
	}
	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.member = retargeter.retarget( this.member );
		this.delaringType = retargeter.retarget( this.delaringType );
	}
	public edu.cmu.cs.dennisc.croquet.ReplacementAcceptability getReplacementAcceptability( edu.cmu.cs.dennisc.croquet.Edit< ? > replacementCandidate, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		return edu.cmu.cs.dennisc.croquet.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK;
	}
	public StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		rv.append( "delete: " );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr(rv, member, locale);
		return rv;
	}
	
	@Override
	protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		if( this.isClearToDelete( this.member ) ) {
			context.commitAndInvokeDo( new org.alice.ide.croquet.edits.DependentEdit< AbstractDeleteMemberOperation< N > >() );
//			final edu.cmu.cs.dennisc.alice.ast.NodeListProperty<N> owner = this.getNodeListProperty( this.delaringType );
//			final int index = owner.indexOf( this.node );
//			final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
//			final Object instance;
//			if( this.node instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
//				field = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)this.node;
//				instance = this.getIDE().getSceneEditor().getInstanceInJavaForUndo( field );
//			} else {
//				field = null;
//				instance = null;
//			}
//			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
//				@Override
//				protected final void doOrRedoInternal( boolean isDo ) {
//					owner.remove( index );
//				}
//				@Override
//				protected final void undoInternal() {
//					if( instance != null ) {
//						getIDE().getSceneEditor().putInstanceForInitializingPendingField( field, instance );
//					}
//					owner.add( index, node );
////					if( field != null && instance != null ) {
////						getIDE().getSceneEditor().handleFieldCreation((edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)field.getDeclaringType(), field, instance, false );
////					}
//				}
//				@Override
//				protected StringBuilder updatePresentation(StringBuilder rv, java.util.Locale locale) {
//					rv.append( "delete: " );
//					edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr(rv, node, locale);
//					return rv;
//				}
//			} );
		} else {
			context.cancel();
		}
	}
}
