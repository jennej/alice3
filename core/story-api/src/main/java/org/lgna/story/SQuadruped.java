/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.story;

import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.implementation.QuadrupedImp;
import org.lgna.story.resources.QuadrupedResource;

import javax.swing.JOptionPane;

/**
 * @author dculyba
 */
public class SQuadruped extends SJointedModel implements Articulable {
  private final QuadrupedImp implementation;

  @Override
  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public QuadrupedImp getImplementation() {
    return this.implementation;
  }

  public SQuadruped(QuadrupedResource resource) {
    this.implementation = resource.createImplementation(this);
  }

  @Override
  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public void walkTo(SThing entity) {
    JOptionPane.showMessageDialog(null, "todo: walkTo");
  }

  @Override
  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public void touch(SThing entity) {
    JOptionPane.showMessageDialog(null, "todo: touch");
  }

  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public SJoint getRoot() {
    return SJoint.getJoint(this, QuadrupedResource.ROOT);
  }

  public SJoint getSpineBase() {
    return SJoint.getJoint(this, QuadrupedResource.SPINE_BASE);
  }

  public SJoint getSpineMiddle() {
    return SJoint.getJoint(this, QuadrupedResource.SPINE_MIDDLE);
  }

  public SJoint getSpineUpper() {
    return SJoint.getJoint(this, QuadrupedResource.SPINE_UPPER);
  }

  public SJoint getNeck() {
    return SJoint.getJoint(this, QuadrupedResource.NECK);
  }

  public SJoint getHead() {
    return SJoint.getJoint(this, QuadrupedResource.HEAD);
  }

  public SJoint getLeftEye() {
    return SJoint.getJoint(this, QuadrupedResource.LEFT_EYE);
  }

  public SJoint getLeftEyelid() {
    return SJoint.getJoint(this, QuadrupedResource.LEFT_EYELID);
  }

  public SJoint getLeftEar() {
    return SJoint.getJoint(this, QuadrupedResource.LEFT_EAR);
  }

  public SJoint getMouth() {
    return SJoint.getJoint(this, QuadrupedResource.MOUTH);
  }

  public SJoint getRightEar() {
    return SJoint.getJoint(this, QuadrupedResource.RIGHT_EAR);
  }

  public SJoint getRightEye() {
    return SJoint.getJoint(this, QuadrupedResource.RIGHT_EYE);
  }

  public SJoint getRightEyelid() {
    return SJoint.getJoint(this, QuadrupedResource.RIGHT_EYELID);
  }

  public SJoint getFrontLeftClavicle() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_LEFT_CLAVICLE);
  }

  public SJoint getFrontLeftShoulder() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_LEFT_SHOULDER);
  }

  public SJoint getFrontLeftKnee() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_LEFT_KNEE);
  }

  public SJoint getFrontLeftAnkle() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_LEFT_ANKLE);
  }

  public SJoint getFrontLeftFoot() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_LEFT_FOOT);
  }

  public SJoint getFrontLeftToe() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_LEFT_TOE);
  }

  public SJoint getFrontRightClavicle() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_RIGHT_CLAVICLE);
  }

  public SJoint getFrontRightShoulder() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_RIGHT_SHOULDER);
  }

  public SJoint getFrontRightKnee() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_RIGHT_KNEE);
  }

  public SJoint getFrontRightAnkle() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_RIGHT_ANKLE);
  }

  public SJoint getFrontRightFoot() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_RIGHT_FOOT);
  }

  public SJoint getFrontRightToe() {
    return SJoint.getJoint(this, QuadrupedResource.FRONT_RIGHT_TOE);
  }

  public SJoint getPelvisLowerBody() {
    return SJoint.getJoint(this, QuadrupedResource.PELVIS_LOWER_BODY);
  }

  public SJoint[] getTailArray() {
    return SJoint.getJointArray(this, this.getImplementation().getResource().getTailArray());
  }

  public SJoint getTail() {
    return SJoint.getJoint(this, QuadrupedResource.TAIL_0);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  @Deprecated
  public SJoint getTail1() {
    return SJoint.getJoint(this, QuadrupedResource.TAIL_0);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  @Deprecated
  public SJoint getTail2() {
    return SJoint.getJoint(this, QuadrupedResource.TAIL_1);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  @Deprecated
  public SJoint getTail3() {
    return SJoint.getJoint(this, QuadrupedResource.TAIL_2);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  @Deprecated
  public SJoint getTail4() {
    return SJoint.getJoint(this, QuadrupedResource.TAIL_3);
  }

  public SJoint getBackLeftHip() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_LEFT_HIP);
  }

  public SJoint getBackLeftKnee() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_LEFT_KNEE);
  }

  public SJoint getBackLeftHock() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_LEFT_HOCK);
  }

  public SJoint getBackLeftAnkle() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_LEFT_ANKLE);
  }

  public SJoint getBackLeftFoot() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_LEFT_FOOT);
  }

  public SJoint getBackLeftToe() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_LEFT_TOE);
  }

  public SJoint getBackRightHip() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_RIGHT_HIP);
  }

  public SJoint getBackRightKnee() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_RIGHT_KNEE);
  }

  public SJoint getBackRightHock() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_RIGHT_HOCK);
  }

  public SJoint getBackRightAnkle() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_RIGHT_ANKLE);
  }

  public SJoint getBackRightFoot() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_RIGHT_FOOT);
  }

  public SJoint getBackRightToe() {
    return SJoint.getJoint(this, QuadrupedResource.BACK_RIGHT_TOE);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public void strikePose(QuadrupedPose pose, StrikePose.Detail... details) {
    super.strikePose(pose, details);
  }
}
