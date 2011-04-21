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
public abstract class Edit<M extends CompletionModel> {
	private static <M extends CompletionModel> CompletionContext<M> getContext( java.util.UUID contextId ) {
		return HistoryNode.lookup( contextId );
	}
	private static <M extends CompletionModel> M getModel( CompletionContext<M> context ) {
		if( context != null ) {
			return context.getModel();
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getModel() context==null" );
			return null;
		}
	}
	private static <M extends CompletionModel> Group getGroup( M model ) {
		if( model != null ) {
			return model.getGroup();
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getGroup() model==null" );
			return null;
		}
	}

	public static abstract class Memento< M extends CompletionModel > implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
		private java.util.UUID contextId;
//		private java.util.UUID stepId;
		public Memento( Edit<M> edit ) {
			this.contextId = edit.contextId;
//			this.stepId = edit.stepId;
		}
		public Memento( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			this.decode( binaryDecoder );
		}
		public CompletionContext<M> getContext() {
			return Edit.getContext( this.contextId );
		}
		public M getModel() {
			return Edit.getModel( this.getContext() );
		}
		public abstract Edit< M > createEdit();
		protected abstract void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
		protected abstract void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder );
		public final void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			this.contextId = binaryDecoder.decodeId();
//			this.stepId = binaryDecoder.decodeId();
			this.decodeInternal(binaryDecoder);
		}
		public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			binaryEncoder.encode( this.contextId );
//			binaryEncoder.encode( this.stepId );
			this.encodeInternal(binaryEncoder);
		}
	};
	
	
	private java.util.UUID contextId;
	private transient CompletionContext<M> context;
//	private java.util.UUID stepId;
	private transient org.lgna.croquet.steps.CompletionStep< M > completionStep;
	
	public Edit() {
	}
	public Edit( Memento<M> memento ) {
		this.contextId = memento.contextId;
//		this.stepId = memento.stepId;
	}
	public abstract Memento<M> createMemento();
	public CompletionContext<M> getContext() {
		if( this.context != null ) {
			//pass
		} else {
			this.context = getContext( this.contextId );
		}
		assert this.context != null;
		return this.context;
	}
	public M getModel() {
		return getModel( this.getContext() );
	}
	public Group getGroup() {
		return getGroup( this.getModel() );
	}
	public org.lgna.croquet.steps.CompletionStep< M > getCompletionStep() {
//		if( this.completionStep != null ) {
//			//pass
//		} else {
//			this.completionStep = org.lgna.croquet.steps.Step.lookup( this.stepId );
//		}
		return this.completionStep;
	}
	public void setCompletionStep( org.lgna.croquet.steps.CompletionStep< M > completionStep ) {
		this.completionStep = completionStep;
//		this.stepId = this.completionStep.getId();
	}
	public void setContext( CompletionContext<M> context ) {
		this.context = context;
		if( this.context != null ) {
			this.contextId = context.getId();
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: context == null" );
			this.contextId = null;
		}
	}
	
	protected java.util.UUID getContextId() {
		return this.contextId;
	}
	public boolean canUndo() {
		return true;
	}
	public boolean canRedo() {
		return true;
	}
	
	protected abstract void doOrRedoInternal( boolean isDo );
	protected abstract void undoInternal();
	
	public final void doOrRedo( boolean isDo ) {
		if( isDo ) {
			//pass
		} else {
			if( canRedo() ) {
				//pass
			} else {
				throw new javax.swing.undo.CannotRedoException();
			}
		}
		Manager.pushUndoOrRedo();
		try {
			this.doOrRedoInternal( isDo );
		} finally {
			Manager.popUndoOrRedo();
		}
	}
	public final void undo() {
		if( canUndo() ) {
			//pass
		} else {
			throw new javax.swing.undo.CannotRedoException();
		}
		Manager.pushUndoOrRedo();
		try {
			this.undoInternal();
		} finally {
			Manager.popUndoOrRedo();
		}
	}

	protected StringBuilder updateTutorialTransactionTitle( StringBuilder rv, UserInformation userInformation ) {
		return rv;
	}

	protected abstract StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale );
	public final String getPresentation( java.util.Locale locale ) {
		StringBuilder sb = new StringBuilder();
		this.updatePresentation( sb, locale );
		if( sb.length() == 0 ) {
			Class<?> cls = this.getClass();
			sb.append( edu.cmu.cs.dennisc.java.lang.ClassUtilities.getTrimmedClassName( cls ) );
		}
		if( sb.length() == 0 ) {
			sb.append( this.getClass() );
		}
		return sb.toString();
	}
	public String getRedoPresentation( java.util.Locale locale ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Redo:" );
		this.updatePresentation( sb, locale );
		return sb.toString();
	}
	public String getUndoPresentation( java.util.Locale locale ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Undo:" );
		this.updatePresentation( sb, locale );
		return sb.toString();
	}
	public ReplacementAcceptability getReplacementAcceptability( Edit< ? > replacementCandidate, UserInformation userInformation ) {
		if( replacementCandidate != null ) {
			return ReplacementAcceptability.PERFECT_MATCH;
		} else {
			return ReplacementAcceptability.createRejection( "replacement is null" );
		}
	}
	public void retarget( Retargeter retargeter ) {
	}
	public void addKeyValuePairs( Retargeter retargeter, Edit< ? > edit ) {
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( ": " );
		this.updatePresentation( sb, java.util.Locale.getDefault() );
		return sb.toString();
	}
}
