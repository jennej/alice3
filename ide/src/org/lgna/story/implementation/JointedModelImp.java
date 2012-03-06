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

package org.lgna.story.implementation;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.lgna.ik.solver.Bone.Direction;

/**
 * @author Dennis Cosgrove
 */
public abstract class JointedModelImp< A extends org.lgna.story.JointedModel, R extends org.lgna.story.resources.JointedModelResource > extends ModelImp {
	public static interface VisualData { 
		public edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals();
		public edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgAppearances();
		public double getBoundingSphereRadius();
		public void setSGParent(edu.cmu.cs.dennisc.scenegraph.Composite parent);
	}
	public static interface JointImplementationAndVisualDataFactory< R extends org.lgna.story.resources.JointedModelResource > {
		public R getResource();
		public JointImp createJointImplementation( org.lgna.story.implementation.JointedModelImp<?,?> jointedModelImplementation, org.lgna.story.resources.JointId jointId );
		public VisualData createVisualData( org.lgna.story.implementation.JointedModelImp<?,?> jointedModelImplementation );
		public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointId jointId );
		public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalJointTransformation( org.lgna.story.resources.JointId jointId );
	}
	
	private final JointImplementationAndVisualDataFactory<R> factory;
	private final A abstraction;
	private final VisualData visualData;

	private final edu.cmu.cs.dennisc.scenegraph.Scalable sgScalable; 
	
	private final java.util.Map< org.lgna.story.resources.JointId, org.lgna.story.implementation.JointImp > mapIdToJoint = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public JointedModelImp( A abstraction, JointImplementationAndVisualDataFactory< R > factory ) {
		this.abstraction = abstraction;
		this.factory = factory;
		this.visualData = this.factory.createVisualData( this );

		org.lgna.story.resources.JointId[] rootIds = this.getRootJointIds();
		edu.cmu.cs.dennisc.scenegraph.Composite sgComposite;
		if( rootIds.length == 0 ) {
			this.sgScalable = null;
			sgComposite = this.getSgComposite();
		} else {
			this.sgScalable = new edu.cmu.cs.dennisc.scenegraph.Scalable();
			this.sgScalable.setParent( this.getSgComposite() );
			this.sgScalable.putBonusDataFor( ENTITY_IMP_KEY, this );
			sgComposite = this.sgScalable;

			for( org.lgna.story.resources.JointId root : rootIds ) {
				this.createJointTree( root, this );
			}
		}
		
		this.visualData.setSGParent( sgComposite );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.visualData.getSgVisuals() ) {
			sgVisual.setParent( sgComposite );
		}
	}
	
	public Iterable< JointImp > getJoints() {
		final java.util.List< JointImp > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.treeWalk( new TreeWalkObserver() {
			public void pushJoint( org.lgna.story.implementation.JointImp joint ) {
				//todo: remove null check?
				if( joint != null ) {
					rv.add( joint );
				}
			}
			public void handleBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
			}
			public void popJoint( org.lgna.story.implementation.JointImp joint ) {
			}
		} );
		return rv;
	}
	private JointImp createJointTree( org.lgna.story.resources.JointId jointId, EntityImp parent ) {
		JointImp joint = this.createJointImplementation( jointId );
		joint.setVehicle(parent);
		this.mapIdToJoint.put( jointId, joint );
		for( org.lgna.story.resources.JointId childId : jointId.getChildren( this.factory.getResource() ) ) {
			JointImp childTree = createJointTree(childId, joint);
			childTree.setVehicle(joint);
		}
		return joint;
	}
	
	public void setAllJointPivotsVisibile(boolean isPivotVisible) {
		for (java.util.Map.Entry< org.lgna.story.resources.JointId, org.lgna.story.implementation.JointImp > jointEntry : this.mapIdToJoint.entrySet()) {
			jointEntry.getValue().setPivotVisible( isPivotVisible );
		}
	}
	
	@Override
	public A getAbstraction() {
		return this.abstraction;
	}
	public R getResource() {
		return this.factory.getResource();
	}
	public VisualData getVisualData() {
		return this.visualData;
	}
	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals() {
		return this.visualData.getSgVisuals();
	}
	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgPaintAppearances() {
		return this.visualData.getSgAppearances();
	}
	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgOpacityAppearances() {
		return this.getSgPaintAppearances();
	}
	
	public org.lgna.story.implementation.JointImp getJointImplementation( org.lgna.story.resources.JointId jointId ) {
		return this.mapIdToJoint.get( jointId );
	}
	
	protected edu.cmu.cs.dennisc.math.Vector4 getFrontOffsetForJoint(org.lgna.story.implementation.JointImp jointImp) {
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = jointImp.getAxisAlignedMinimumBoundingBox(this);
		edu.cmu.cs.dennisc.math.Point3 point = bbox.getCenterOfFrontFace();
		offsetAsSeenBySubject.x = point.x;
		offsetAsSeenBySubject.y = point.y;
		offsetAsSeenBySubject.z = point.z;
		offsetAsSeenBySubject.w = 1;
		return offsetAsSeenBySubject;
	}
	
	protected edu.cmu.cs.dennisc.math.Vector4 getTopOffsetForJoint(org.lgna.story.implementation.JointImp jointImp) {
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = jointImp.getAxisAlignedMinimumBoundingBox(this);
		edu.cmu.cs.dennisc.math.Point3 point = bbox.getCenterOfTopFace();
		offsetAsSeenBySubject.x = point.x;
		offsetAsSeenBySubject.y = point.y;
		offsetAsSeenBySubject.z = point.z;
		offsetAsSeenBySubject.w = 1;
		return offsetAsSeenBySubject;
	}
	
	public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.getOriginalJointOrientation( jointId );
	}
	
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalJointTransformation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.getOriginalJointTransformation( jointId );
	}
	
	public abstract org.lgna.story.resources.JointId[] getRootJointIds();
	
	public edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getSgSkeletonVisual() {
		if (this.getSgVisuals()[ 0 ] instanceof edu.cmu.cs.dennisc.scenegraph.SkeletonVisual)
		{
			return (edu.cmu.cs.dennisc.scenegraph.SkeletonVisual)this.getSgVisuals()[ 0 ];
		}
		return null;
	}
	
	@Override
	public void addScaleListener( edu.cmu.cs.dennisc.property.event.PropertyListener listener ) {
		if( this.sgScalable != null ) {
			this.sgScalable.scale.addPropertyListener( listener );
		} else {
			this.visualData.getSgVisuals()[ 0 ].scale.addPropertyListener( listener );
		}
	}
	@Override
	public void removeScaleListener( edu.cmu.cs.dennisc.property.event.PropertyListener listener ) {
		if( this.sgScalable != null ) {
			this.sgScalable.scale.removePropertyListener( listener );
		} else {
			this.visualData.getSgVisuals()[ 0 ].scale.removePropertyListener( listener );
		}
	}
	
	@Override
	public edu.cmu.cs.dennisc.math.Dimension3 getScale() {
		if( this.sgScalable != null ) {
			return this.sgScalable.scale.getValue();
		} else {
			edu.cmu.cs.dennisc.math.Matrix3x3 scale = this.visualData.getSgVisuals()[ 0 ].scale.getValue();
			return new edu.cmu.cs.dennisc.math.Dimension3( scale.right.x, scale.up.y, scale.backward.z );
		}
	}
	@Override
	public void setScale( edu.cmu.cs.dennisc.math.Dimension3 scale ) {
		if( this.sgScalable != null ) {
			this.sgScalable.scale.setValue( new edu.cmu.cs.dennisc.math.Dimension3( scale ) );
		} else {
			edu.cmu.cs.dennisc.math.Matrix3x3 m = edu.cmu.cs.dennisc.math.Matrix3x3.createZero();
			m.right.x = scale.x;
			m.up.y = scale.y;
			m.backward.z = scale.z;
			for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.visualData.getSgVisuals() ) {
				sgVisual.scale.setValue( m );
			}
		}
	}
	
//	@Override
//	public void addScaleListener( edu.cmu.cs.dennisc.property.event.PropertyListener listener ) {
//		this.sgScalable.scale.addPropertyListener( listener );
//	}
//	@Override
//	public void removeScaleListener( edu.cmu.cs.dennisc.property.event.PropertyListener listener ) {
//		this.sgScalable.scale.removePropertyListener( listener );
//	}
//	
//	@Override
//	protected void animateApplyScale( edu.cmu.cs.dennisc.math.Vector3 axis, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
//		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( axis );
//	}
//	@Override
//	public edu.cmu.cs.dennisc.math.Dimension3 getScale() {
//		return this.sgScalable.scale.getValue();
//	}
//	@Override
//	public void setScale( edu.cmu.cs.dennisc.math.Dimension3 scale ) {
//		this.sgScalable.scale.setValue( scale );
//	}
	
	protected final org.lgna.story.implementation.JointImp createJointImplementation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.createJointImplementation( this, jointId );
	}	
	private org.lgna.story.implementation.visualization.JointedModelVisualization visualization;
	private org.lgna.story.implementation.visualization.JointedModelVisualization getVisualization() {
		if( this.visualization != null ) {
			//pass
		} else {
			this.visualization = new org.lgna.story.implementation.visualization.JointedModelVisualization( this );
		}
		return this.visualization;
	}
	public void showVisualization() {
		this.getVisualization().setParent( this.getSgComposite() );
	}
	public void hideVisualization() {
		if( this.visualization != null ) {
			this.visualization.setParent( null );
		}
	}
	
	public static interface TreeWalkObserver {
		public void pushJoint( JointImp joint );
		public void handleBone( JointImp parent, JointImp child );
		public void popJoint( JointImp joint );
	}
	
	private void treeWalk( org.lgna.story.resources.JointId parentId, TreeWalkObserver observer ) {
		JointImp parentImp = this.getJointImplementation( parentId );
		if( parentImp != null ) {
			observer.pushJoint( parentImp );
			R resource = this.getResource();
			for( org.lgna.story.resources.JointId childId : parentId.getChildren( resource ) ) {
				JointImp childImp = this.getJointImplementation( childId );
				if( childImp != null ) {
					observer.handleBone( parentImp, childImp );
				}
			}
			observer.popJoint( parentImp );
			for( org.lgna.story.resources.JointId childId : parentId.getChildren( resource ) ) {
				treeWalk( childId, observer );
			}
		}
	}
	public void treeWalk( TreeWalkObserver observer ) {
		for( org.lgna.story.resources.JointId root : this.getRootJointIds() ) {
			this.treeWalk( root, observer );
		}
	}
	
	private static enum AddOp {
		PREPEND {
			@Override
			public java.util.List< JointImp > add( java.util.List< JointImp > rv, JointImp joint, List< org.lgna.ik.solver.Bone.Direction > directions, org.lgna.ik.solver.Bone.Direction direction ) {
				rv.add( 0, joint );
				if( directions != null ) {
					directions.add( 0, direction );
				}
				return rv;
			}
		},
		APPEND {
			@Override
			public java.util.List< JointImp > add( java.util.List< JointImp > rv, JointImp joint, List< org.lgna.ik.solver.Bone.Direction > directions, org.lgna.ik.solver.Bone.Direction direction ) {
				rv.add( joint );
				if( directions != null ) {
					directions.add( direction );
				}
				return rv;
			}
		};
		public abstract java.util.List< JointImp > add( java.util.List< JointImp > rv, JointImp joint, List< org.lgna.ik.solver.Bone.Direction > directions, org.lgna.ik.solver.Bone.Direction direction );
	}
	private java.util.List< JointImp > updateJointsBetween( java.util.List< JointImp > rv, List< org.lgna.ik.solver.Bone.Direction > directions, JointImp joint, EntityImp ancestorToReach, AddOp addOp ) {
		if( joint == ancestorToReach ) {
			//pass
		} else {
			org.lgna.story.resources.JointId parentId = joint.getJointId().getParent();
			if( parentId != null ) {
				JointImp parent = this.getJointImplementation( parentId );
				this.updateJointsBetween( rv, directions, parent, ancestorToReach, addOp );
			}
		}
		org.lgna.ik.solver.Bone.Direction direction;
		if( addOp == AddOp.APPEND ) { 
			direction = org.lgna.ik.solver.Bone.Direction.DOWNSTREAM; 
		} else {
			direction = org.lgna.ik.solver.Bone.Direction.UPSTREAM;
		}
		addOp.add( rv, joint, directions, direction);
		return rv;
	}
	public java.util.List< JointImp > getInclusiveListOfJointsBetween( JointImp jointA, JointImp jointB, java.util.List< org.lgna.ik.solver.Bone.Direction > directions ) {
		assert jointA != null : this;
		assert jointB != null : this;
		java.util.List< JointImp > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		if( jointA == jointB ) {
			//?
			rv.add( jointA );
			directions.add( org.lgna.ik.solver.Bone.Direction.DOWNSTREAM );
//			throw new RuntimeException( "To Gazi: Please ensure that direction is correct in this case." );
		} else {
			if( jointA.isDescendantOf( jointB ) ) {
				this.updateJointsBetween( rv, directions, jointA, jointB, AddOp.PREPEND );
			} else if( jointB.isDescendantOf( jointA ) ) {
				this.updateJointsBetween( rv, directions, jointB, jointA, AddOp.APPEND );
			} else {
				//It shouldn't even use the joint on which direction is changed (the common ancestor) 
				//that's what the below call does
				this.updateJointsUpToAndExcludingCommonAncestor(rv, directions, jointA, jointB);
			}
		}
		return rv;
	}

	private void updateJointsUpToAndExcludingCommonAncestor(List<JointImp> rvPath, List<Direction> rvDirections, JointImp jointA, JointImp jointB) {
		java.util.List< JointImp > pathA = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		java.util.List< JointImp > pathB = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		List<Direction> directionsA = new java.util.ArrayList<Direction>();
		List<Direction> directionsB = new java.util.ArrayList<Direction>();
		
		this.updateJointsBetween( pathA, directionsA, jointA, this, AddOp.PREPEND );
		this.updateJointsBetween( pathB, directionsB, jointB, this, AddOp.APPEND );
		
		JointImp commonAncestor = null;
		
		for(JointImp jointInA: pathA) {
			if(pathB.contains(jointInA)) {
				commonAncestor = jointInA;
				break;
			}
		}
		
		if(commonAncestor == null) {
			throw new RuntimeException("Probably not connected with a chain.");
		}
		
		ListIterator< JointImp > pathAIterator = pathA.listIterator(pathA.size());
		ListIterator< Direction > directionsAIterator = directionsA.listIterator(directionsA.size());
		for (; pathAIterator.hasPrevious();) {
			JointImp jointImp = pathAIterator.previous();
			directionsAIterator.previous();
			
			pathAIterator.remove();
			directionsAIterator.remove();
			
			if(jointImp == commonAncestor) {
				break;
			}
		}
		
		ListIterator< JointImp > pathBIterator = pathB.listIterator();
		ListIterator< Direction > directionsBIterator = directionsB.listIterator();
		for (; pathBIterator.hasNext();) {
			JointImp jointImp = (JointImp) pathBIterator.next();
			directionsBIterator.next();
			
			pathBIterator.remove();
			directionsBIterator.remove();
			
			if(jointImp == commonAncestor) {
				break;
			}
		}
		
		rvPath.addAll(pathA);
		rvPath.addAll(pathB);
		rvDirections.addAll(directionsA);
		rvDirections.addAll(directionsB);
	}

	public java.util.List< JointImp > getInclusiveListOfJointsBetween( org.lgna.story.resources.JointId idA, org.lgna.story.resources.JointId idB, java.util.List< org.lgna.ik.solver.Bone.Direction > directions ) {
		return this.getInclusiveListOfJointsBetween( this.getJointImplementation( idA ), this.getJointImplementation( idB ), directions );
	}
	
	private static class JointData {
		private final JointImp jointImp;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q0;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q1;
		public JointData( JointImp jointImp ) {
			this.jointImp = jointImp;
			this.q0 = this.jointImp.getLocalOrientation().createUnitQuaternion();
			edu.cmu.cs.dennisc.math.UnitQuaternion q = this.jointImp.getOriginalOrientation();
			if( q != null ) {
				if( this.q0.isWithinReasonableEpsilonOrIsNegativeWithinReasonableEpsilon( q ) ) {
					this.q1 = null;
				} else {
					this.q1 = q;
				}
			} else {
				this.q1 = null;
			}
		}
//		public JointImp getJointImp() {
//			return this.jointImp;
//		}
//		public edu.cmu.cs.dennisc.math.UnitQuaternion getQ0() {
//			return this.q0;
//		}
//		public edu.cmu.cs.dennisc.math.UnitQuaternion getQ1() {
//			return this.q1;
//		}
		public void setPortion( double portion ) {
			if( this.q1 != null ) {
				this.jointImp.setLocalOrientationOnly( edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( this.q0, this.q1, portion ).createOrthogonalMatrix3x3() );
			} else {
				//System.err.println( "skipping: " + this.jointImp );
			}
		}
		public void epilogue() {
			if( this.q1 != null ) {
				this.jointImp.setLocalOrientationOnly( this.q1.createOrthogonalMatrix3x3() );
			} else {
				//System.err.println( "skipping: " + this.jointImp );
			}
		}
	}
	private static class StraightenTreeWalkObserver implements TreeWalkObserver {
		private java.util.List< JointData > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		public void pushJoint(JointImp jointImp) {
			if( jointImp != null ) {
				list.add( new JointData( jointImp ) );
			}
		}
		public void handleBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
		}	
		public void popJoint(JointImp joint) {
		}
	};
	public void straightenOutJoints() {
		StraightenTreeWalkObserver treeWalkObserver = new StraightenTreeWalkObserver();
		this.treeWalk( treeWalkObserver );
		for( JointData jointData : treeWalkObserver.list ) {
			jointData.epilogue();
		}
	}
	public void animateStraightenOutJoints( double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.straightenOutJoints();
		} else {
			final StraightenTreeWalkObserver treeWalkObserver = new StraightenTreeWalkObserver();
			this.treeWalk( treeWalkObserver );
			class StraightenOutJointsAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
				public StraightenOutJointsAnimation( double duration, edu.cmu.cs.dennisc.animation.Style style ) {
					super( duration, style );
				}
				@Override
				protected void prologue() {
				}
				@Override
				protected void setPortion( double portion ) {
					for( JointData jointData : treeWalkObserver.list ) {
						jointData.setPortion( portion );
					}
				}
				@Override
				protected void epilogue() {
					for( JointData jointData : treeWalkObserver.list ) {
						jointData.epilogue();
					}
				}
			}
			perform( new StraightenOutJointsAnimation( duration, style ) );
		}
	}
	public void animateStraightenOutJoints( double duration ) {
		this.animateStraightenOutJoints( duration, DEFAULT_STYLE );
	}
	public void animateStraightenOutJoints() {
		this.animateStraightenOutJoints( DEFAULT_DURATION );
	}
	
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m;
		if( this.sgScalable != null ) {
			edu.cmu.cs.dennisc.math.Dimension3 scale = this.sgScalable.scale.getValue();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 s = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
			s.orientation.right.x = scale.x;
			s.orientation.up.y = scale.y;
			s.orientation.backward.z = scale.z;
			m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createMultiplication( trans, s );
		} else {
			m = trans;
		}
		return super.updateCumulativeBound( rv, m );
	}
}
