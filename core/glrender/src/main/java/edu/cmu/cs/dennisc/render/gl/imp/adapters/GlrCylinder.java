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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.scenegraph.Cylinder;

/**
 * @author Dennis Cosgrove
 */
public class GlrCylinder extends GlrShape<Cylinder> {
  @Override
  protected void shapeOnContext(Context context) {
    glCylinder(context);
  }

  //todo: add scenegraph hint
  float capPortion = 1.0F / 3.0F;
  float capCenterS = 0.5F;
  float topCapCenterT = 1.0F / 6.0F;
  float bottomCapCenterT = 5.0F / 6.0F;

  private void glCylinder(Context c) {
    double topRadius;
    if (Double.isNaN(this.topRadius)) {
      topRadius = bottomRadius;
    } else {
      topRadius = this.topRadius;
    }
    switch (bottomToTopAxis) {
      case POSITIVE_X -> c.gl.glRotated(-90, 0, 1, 0);
      case POSITIVE_Y -> c.gl.glRotated(+90, 1, 0, 0);
      case NEGATIVE_X -> c.gl.glRotated(+90, 0, 1, 0);
      case NEGATIVE_Y -> c.gl.glRotated(-90, 1, 0, 0);
      case POSITIVE_Z -> c.gl.glRotated(180, 0, 1, 0);
    }

    double z = switch (originAlignment) {
      case BOTTOM -> -length;
      case CENTER -> -length * 0.5;
      case TOP -> 0;
    };
    c.gl.glTranslated(0, 0, z);

    float tMin = hasTopCap ? 1.0F / 3.0F : 0F;
    float tMax = 2.0F / 3.0F;

    c.glCylinderSide(bottomRadius, topRadius, length, tMin, tMax);

    if (hasTopCap && (topRadius > 0)) {
      c.gl.glRotated(180, 0, 1, 0);
      c.glDisk(0, topRadius, capCenterS, topCapCenterT, capPortion);
      c.gl.glRotated(180, 0, 1, 0);
    }
    if (hasBottomCap && (bottomRadius > 0)) {
      c.gl.glTranslated(0, 0, +length);
      c.gl.glRotated(180, 0, 0, 1);
      c.glDisk(0, bottomRadius, capCenterS, bottomCapCenterT, capPortion);
      c.gl.glRotated(180, 0, 0, 1);
      c.gl.glTranslated(0, 0, -length);
    }
  }

  @Override
  public Point3 getIntersectionInSource(Point3 rv, Ray ray, AffineMatrix4x4 m, int subElement) {
    double bottomValue;
    double topValue;
    switch (originAlignment) {
      case BOTTOM -> {
        bottomValue = 0;
        topValue = length;
      }
      case CENTER -> {
        bottomValue = -length * 0.5;
        topValue = +length * 0.5;
      }
      case TOP -> {
        bottomValue = length;
        topValue = 0;
      }
      default -> {
        //todo?
        throw new RuntimeException();
      }
    }

    Point3 cylinderPosition = new Point3(0, 0, 0);
    Vector3 cylinderDirection = new Vector3(0, 0, 0);

    Point3 cylinderTopPosition = new Point3(0, 0, 0);
    if (this.bottomToTopAxis == Cylinder.BottomToTopAxis.POSITIVE_X) {
      cylinderDirection.x = 1;
      cylinderPosition.x = bottomValue;
      cylinderTopPosition.x = topValue;
    } else if (this.bottomToTopAxis == Cylinder.BottomToTopAxis.POSITIVE_Y) {
      cylinderDirection.y = 1;
      cylinderPosition.y = bottomValue;
      cylinderTopPosition.y = topValue;
    } else if (this.bottomToTopAxis == Cylinder.BottomToTopAxis.POSITIVE_Z) {
      cylinderDirection.z = 1;
      cylinderPosition.z = bottomValue;
      cylinderTopPosition.z = topValue;
    } else if (this.bottomToTopAxis == Cylinder.BottomToTopAxis.NEGATIVE_X) {
      cylinderDirection.x = -1;
      cylinderPosition.x = -bottomValue;
      cylinderTopPosition.x = -topValue;
    } else if (this.bottomToTopAxis == Cylinder.BottomToTopAxis.NEGATIVE_Y) {
      cylinderDirection.y = -1;
      cylinderPosition.y = -bottomValue;
      cylinderTopPosition.y = -topValue;
    } else if (this.bottomToTopAxis == Cylinder.BottomToTopAxis.NEGATIVE_Z) {
      cylinderDirection.z = -1;
      cylinderPosition.z = -bottomValue;
      cylinderTopPosition.z = -topValue;
    } else {
      //todo?
      throw new RuntimeException();
    }
    double maxRadius = Math.max(this.bottomRadius, this.topRadius);
    m.transform(cylinderPosition);
    m.transform(cylinderDirection);

    final boolean HANDLE_CONES_SEPARATELY = false;
    double t = Double.NaN;
    final double THRESHOLD = 0.01;
    if (HANDLE_CONES_SEPARATELY && (Math.abs(this.bottomRadius - this.topRadius) < THRESHOLD)) {
      Vector3 originToOrigin = Vector3.createSubtraction(ray.accessOrigin(), cylinderPosition);
      Vector3 rayDirection_X_cylinderDirection = Vector3.createCrossProduct(ray.accessDirection(), cylinderDirection);

      double magnitude = rayDirection_X_cylinderDirection.calculateMagnitude();

      if (magnitude > EpsilonUtilities.REASONABLE_EPSILON) {
        rayDirection_X_cylinderDirection.normalize();
        double d = Math.abs(Vector3.calculateDotProduct(originToOrigin, rayDirection_X_cylinderDirection));
        if (d <= maxRadius) {
          Vector3 originToOrigin_X_cylinderDirection = Vector3.createCrossProduct(originToOrigin, cylinderDirection);
          double a = -Vector3.calculateDotProduct(originToOrigin_X_cylinderDirection, rayDirection_X_cylinderDirection) / magnitude;

          Vector3 rayDirection_X_CylinderDirection___X_cylinderDirection = Vector3.createCrossProduct(rayDirection_X_cylinderDirection, cylinderDirection);
          rayDirection_X_CylinderDirection___X_cylinderDirection.normalize();
          double b = Math.abs(Math.sqrt((maxRadius * maxRadius) - (d * d)) / Vector3.calculateDotProduct(ray.accessDirection(), rayDirection_X_CylinderDirection___X_cylinderDirection));

          t = a - b;
        }
      }
    } else {
      //todo handle cones
    }
    if (Double.isNaN(t)) {
      rv.setNaN();
    } else {
      ray.getPointAlong(rv, t);
    }

    if (rv.isNaN()) {
      //todo: check to see if hit cap
      Point3 pTopCap;
      Point3 pBottomCap;
      if (this.hasBottomCap && (this.bottomRadius > 0)) {
        pBottomCap = new Point3();
        GlrGeometry.getIntersectionInSourceFromPlaneInLocal(pBottomCap, ray, m, cylinderPosition, cylinderDirection);
      } else {
        pBottomCap = null;
      }
      if (this.hasTopCap && (this.topRadius > 0)) {
        pTopCap = new Point3();
        GlrGeometry.getIntersectionInSourceFromPlaneInLocal(pTopCap, ray, m, cylinderTopPosition, cylinderDirection);
      } else {
        pTopCap = null;
      }
      if (pBottomCap != null) {
        if (rv.isNaN() || (rv.z > pBottomCap.z)) {
          rv.set(pBottomCap);
        }
      }
      if (pTopCap != null) {
        if (rv.isNaN() || (rv.z > pTopCap.z)) {
          rv.set(pTopCap);
        }
      }
    }

    return rv;
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.length) {
      this.length = owner.length.getValue();
      markGeometryAsChanged();
    } else if (property == owner.bottomRadius) {
      this.bottomRadius = owner.bottomRadius.getValue();
      markGeometryAsChanged();
    } else if (property == owner.topRadius) {
      this.topRadius = owner.topRadius.getValue();
      markGeometryAsChanged();
    } else if (property == owner.hasBottomCap) {
      this.hasBottomCap = owner.hasBottomCap.getValue();
      markGeometryAsChanged();
    } else if (property == owner.hasTopCap) {
      this.hasTopCap = owner.hasTopCap.getValue();
      markGeometryAsChanged();
    } else if (property == owner.originAlignment) {
      this.originAlignment = owner.originAlignment.getValue();
      markGeometryAsChanged();
    } else if (property == owner.bottomToTopAxis) {
      this.bottomToTopAxis = owner.bottomToTopAxis.getValue();
      markGeometryAsChanged();
    } else {
      super.propertyChanged(property);
    }
  }

  private double length;
  private double bottomRadius;
  private double topRadius;
  private boolean hasBottomCap;
  private boolean hasTopCap;
  private Cylinder.OriginAlignment originAlignment;
  private Cylinder.BottomToTopAxis bottomToTopAxis;
}
