/*******************************************************************************
 * Copyright (c) 2023 Carnegie Mellon University. All rights reserved.
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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;
import org.lgna.story.SVRUser;

public class VrUserImp extends TransformableImp {

  public VrUserImp(String name, SVRUser abstraction) {
    this.abstraction = abstraction;
    setName(name);
  }

  @Override
  protected void updateCumulativeBound(CumulativeBound rv, AffineMatrix4x4 trans) {
    rv.addBoundingBox(new AxisAlignedBox(Point3.ORIGIN, Point3.ORIGIN), trans);
  }

  public void animateSetScale(double newScale, double duration, Style style) {
    double actualDuration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(actualDuration, 0.0)) {
      scale.setValue(newScale);
      applyAnimation();
    } else {
      class ScaleAnimation extends DoubleAnimation {
        private ScaleAnimation(double duration, Style style, double scale0, double scale1) {
          super(duration, style, scale0, scale1);
        }

        @Override
        protected void updateValue(Double v) {
          scale.setValue(v);
          applyAnimation();
        }
      }
      perform(new ScaleAnimation(duration, style, scale.getValue(), newScale));
    }

  }

  public void animateSetTransformationToAGoodVantagePointOf(EntityImp other, double duration, Style style) {
    PreSetVantagePointData data =
        new PreSetVantagePointData(this, SymmetricPerspectiveCameraImp.createGoodVantagePointStandIn(other));
    animateVantagePoint(data, duration, style);
  }

  @Override
  public SVRUser getAbstraction() {
    return abstraction;
  }
  private final SVRUser abstraction;

  Double userScale = 1.0;
  public final DoubleProperty scale = new DoubleProperty(VrUserImp.this) {
    @Override
    public Double getValue() {
      return VrUserImp.this.userScale;
    }

    @Override
    protected void handleSetValue(Double value) {
      double scaleChange = value / userScale;
      userScale = value;
      abstraction.getHeadset().getImplementation().scaleBy(scaleChange);
      abstraction.getLeftHand().getImplementation().scaleBy(scaleChange);
      abstraction.getRightHand().getImplementation().scaleBy(scaleChange);
    }
  };
}
