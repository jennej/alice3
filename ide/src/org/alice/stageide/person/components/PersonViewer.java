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
package org.alice.stageide.person.components;

/**
 * @author Dennis Cosgrove
 */
public class PersonViewer extends org.alice.stageide.modelviewer.ModelViewer {
	static PersonViewer singleton = null;

	public static PersonViewer getSingleton() {
		if( PersonViewer.singleton != null ) {
			//pass
		} else {
			PersonViewer.singleton = new PersonViewer();
		}
		return PersonViewer.singleton;
	}

	private org.alice.interact.CreateASimDragAdapter dragAdapter = new org.alice.interact.CreateASimDragAdapter();
	private PersonViewer() {
	}

	private void positionAndOrientCamera( double height, int index, double duration ) {
		double xzFactor;
		if( index == 0 ) {
			xzFactor = 2.333;
		} else {
			xzFactor = 0.5;
		}
		double yFactor;
		if( index == 0 ) {
			yFactor = 0.5;
		} else {
			yFactor = 0.9;
		}
		if( this.getScene() != null ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevPOV = this.getCamera().getLocalTransformation();
			this.getCamera().setTransformation( this.getScene().createOffsetStandIn( -0.3*xzFactor, height*yFactor, -height*xzFactor ) );
			this.getCamera().setOrientationOnlyToPointAt( this.getScene().createOffsetStandIn( 0, height*yFactor, 0 ) );
			edu.cmu.cs.dennisc.animation.Animator animator = this.getAnimator();
			if( duration > 0.0 && animator != null ) {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextPOV = this.getCamera().getLocalTransformation();
				this.getCamera().setLocalTransformation( prevPOV );

				edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation povAnimation = new edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation( this.getCamera().getSgComposite(), edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT, null, nextPOV );
				povAnimation.setDuration( duration );

				animator.completeAll( null );
				animator.invokeLater( povAnimation, null );
			}
		}
	}

	public org.lgna.story.implementation.sims2.NebulousPersonVisualData getPersonVisualData() {
		org.lgna.story.implementation.BipedImp bipedImplementation = this.getPerson();
		if( bipedImplementation != null ) {
			org.lgna.story.implementation.JointedModelImp.VisualData visualData = bipedImplementation.getVisualData();
			if( visualData instanceof org.lgna.story.implementation.sims2.NebulousPersonVisualData ) {
				org.lgna.story.implementation.sims2.NebulousPersonVisualData nebPersonVisualData = (org.lgna.story.implementation.sims2.NebulousPersonVisualData)visualData;
				return nebPersonVisualData;
			}
			throw new RuntimeException();
		} else {
			return null;
		}
	}
	public void setPersonVisualData( org.lgna.story.implementation.sims2.NebulousPersonVisualData personVisualData ) {
		System.err.println( "TODO: setPersonVisualData " + personVisualData );
	}
	
	public org.lgna.story.implementation.BipedImp getPerson() {
		return (org.lgna.story.implementation.BipedImp)this.getModel();
	}
	public void setPerson( org.lgna.story.implementation.BipedImp person ) {
		assert person != null;
		this.setModel( person );
		this.dragAdapter.setSelectedImplementation( person );
		double height = person.getSize().y;
		this.positionAndOrientCamera( height, 0, 0.0 );
	}
	@Override
	protected void initialize() {
		super.initialize();
		this.dragAdapter.setOnscreenLookingGlass( this.getOnscreenLookingGlass() );
		this.dragAdapter.addCameraView( org.alice.interact.AbstractDragAdapter.CameraView.MAIN, this.getCamera().getSgCamera(), null );
		this.dragAdapter.makeCameraActive( this.getCamera().getSgCamera() );
	}
}
