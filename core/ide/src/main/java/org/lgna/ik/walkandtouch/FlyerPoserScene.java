/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.walkandtouch;

import java.util.ArrayList;

import org.lgna.ik.poser.JointSelectionSphere;
import org.lgna.ik.walkandtouch.IKMagicWand.Limb;
import org.lgna.story.SCamera;
import org.lgna.story.SFlyer;
import org.lgna.story.SJoint;
import org.lgna.story.implementation.JointImp;

import edu.cmu.cs.dennisc.java.util.Lists;

/**
 * @author Matt May
 */
public class FlyerPoserScene extends AbstractPoserScene<SFlyer> {

	public FlyerPoserScene( SCamera camera, SFlyer ogre ) {
		super( camera, ogre );
	}

	@Override
	protected void initializeJointSelectionSpheresAndLimbs() {
		JointSelectionSphere a = createJSS( model.getRightHip(), null );
		JointSelectionSphere b = createJSS( model.getRightKnee(), a );
		JointSelectionSphere c = createJSS( model.getRightAnkle(), b );
		limbToJointMap.put( Limb.RIGHT_LEG, Lists.newArrayList( a, b, c ) );
		JointSelectionSphere d = createJSS( model.getRightWingShoulder(), null );
		JointSelectionSphere e = createJSS( model.getRightWingElbow(), d );
		JointSelectionSphere f = createJSS( model.getRightWingWrist(), e );
		limbToJointMap.put( Limb.RIGHT_ARM, Lists.newArrayList( d, e, f ) );
		JointSelectionSphere g = createJSS( model.getLeftHip(), null );
		JointSelectionSphere h = createJSS( model.getLeftKnee(), g );
		JointSelectionSphere i = createJSS( model.getLeftAnkle(), h );
		limbToJointMap.put( Limb.LEFT_LEG, Lists.newArrayList( g, h, i ) );
		JointSelectionSphere j = createJSS( model.getLeftWingShoulder(), null );
		JointSelectionSphere k = createJSS( model.getLeftWingElbow(), j );
		JointSelectionSphere l = createJSS( model.getLeftWingWrist(), k );
		limbToJointMap.put( Limb.LEFT_ARM, Lists.newArrayList( j, k, l ) );

		for( IKMagicWand.Limb limb : limbToJointMap.keySet() ) {
			for( JointSelectionSphere sphere : limbToJointMap.get( limb ) ) {
				jointToLimbMap.put( sphere.getJoint(), limb );
				sphere.setOpacity( 0 );
			}
		}
		this.jssArr = Lists.newArrayList( a, b, c, d, e, f, g, g, h, i, j, k, l );
	}

	@Override
	protected void initializeLimbAnchors() {
		ArrayList<SJoint> sJointList = Lists.newArrayList( model.getRightWingShoulder(), model.getLeftWingShoulder(), model.getRightHip(), model.getLeftHip() );
		for( SJoint joint : sJointList ) {
			anchorPoints.add( ( (JointImp)org.lgna.story.EmployeesOnly.getImplementation( joint ) ).getJointId() );
		}
	}

	@Override
	public JointSelectionSphere getDefaultAnchorJoint( Limb key ) {
		return limbToJointMap.get( key ).get( 0 );
	}

}
