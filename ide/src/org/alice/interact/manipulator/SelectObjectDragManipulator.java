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
package org.alice.interact.manipulator;

import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.RotationRingHandle;

import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

/**
 * @author David Culyba
 */
public class SelectObjectDragManipulator extends AbstractManipulator {

	protected GlobalDragAdapter globalDragAdapter;

	public SelectObjectDragManipulator( GlobalDragAdapter globalDragAdapter )
	{
		this.globalDragAdapter = globalDragAdapter;
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Select";
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		//		PrintUtilities.println("SelectObjectDragManipulator!!!");
		PickHint clickedObjectType = startInput.getClickPickHint();
		if( clickedObjectType.intersects( PickHint.PickType.SELECTABLE.pickHint() ) )
		{
			this.globalDragAdapter.triggerSgObjectSelection( startInput.getClickPickedTransformable( true ) );
		}
		else if( clickedObjectType.intersects( PickHint.PickType.THREE_D_HANDLE.pickHint() ) )
		{
			AbstractTransformable pickedHandle = startInput.getClickPickedTransformable( true );
			if( pickedHandle instanceof RotationRingHandle )
			{
				this.globalDragAdapter.triggerSgObjectSelection( ( (RotationRingHandle)pickedHandle ).getManipulatedObject() );
			}
		}
		else if( clickedObjectType.intersects( PickHint.PickType.TWO_D_HANDLE.pickHint() ) )
		{
			//Do nothing since the 2D handles don't select anything right now
		}
		else
		{
			this.globalDragAdapter.triggerImplementationSelection( null );
		}
		return true;

	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

}
