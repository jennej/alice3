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
package org.lgna.ik.poser.scene;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.util.List;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.render.PickResult;
import edu.cmu.cs.dennisc.render.PickSubElementPolicy;
import edu.cmu.cs.dennisc.render.RenderTarget;
import org.alice.interact.handle.ManipulationHandle3D;

import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Sphere;
import org.lgna.ik.poser.PoserSphereManipulatorListener;
import org.lgna.ik.poser.controllers.PoserEvent;
import org.lgna.ik.poser.jselection.JointSelectionSphere;
//import org.lgna.story.SMovableTurnable;
import org.lgna.story.SSphere;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

import javax.swing.SwingUtilities;

/**
 * @author Matt May
 */
public class PoserPicturePlaneInteraction extends PicturePlaneInteraction {
  private final AbstractPoserScene<?> scene;
  private final CameraImp camera;
  private final List<PoserSphereManipulatorListener> listeners = Lists.newCopyOnWriteArrayList();
  private JointSelectionSphere selected;
  private JointSelectionSphere anchor;
  private Joint joint;

  public PoserPicturePlaneInteraction(OnscreenRenderTarget renderTarget, AbstractPoserScene scene) {
    super(renderTarget, scene.getImplementation().findFirstCamera().getSgCamera());
    this.scene = scene;
    SceneImp sceneImp = scene.getImplementation();
    this.camera = sceneImp.findFirstCamera();

    // this debug overlay doesn't update when you move things around, it is mildly useful for debugging
    // calculateJointSelectionSphereAtPoint (and picking) but it is unclear that justifies its existence
    final boolean IS_DEBUG_DESIRED = false;
    if (IS_DEBUG_DESIRED) {
      OnscreenRenderTarget onscreenPicturePlane = getOnscreenPicturePlane();
      onscreenPicturePlane.addRenderTargetListener(new DebugOverlay(point -> {
        JointSelectionSphere jointSelectionSphere = calculateJointSelectionSphereAtPoint(point);
        if (jointSelectionSphere != null) {
          System.out.println("found joint: " + jointSelectionSphere.getName() +"[ "+ jointSelectionSphere.getAbsoluteTranslation() + "] at: " + point);
          return Color.RED;
        } else {
          return Color.BLUE;
        }
      }));
    }
  }

  private JointSelectionSphere calculateJointSelectionSphereAtPoint(Point point) {
    Ray rayAtPixel = this.getOnscreenPicturePlane().getViewportRayAtAwtPoint(point, this.getSgCamera());
    double closest = Double.MAX_VALUE;
    JointSelectionSphere selected = null;
    for (JointSelectionSphere sphere : scene.getJointSelectionSpheres()) {
      double rayLength = getSphereRayIntersection(rayAtPixel, sphere);
      if (!Double.isNaN(rayLength) && (rayLength > 0) && (rayLength < closest)) {
        System.out.println( "selected(m): " + sphere );
        System.out.println( "rayLength: " + rayLength );
        selected = sphere;
        closest = rayLength;
      }
    }
    return selected;
  }

  // would love to replace this with any other picking code?
  @Override
  protected Transformable pick(MouseEvent e) {
    ManipulationHandle3D handle = checkIfHandleSelected(e);
    if (handle != null) {
      joint = (Joint) handle.getManipulatedObject();
      return handle;
    }

    JointSelectionSphere selected = this.calculateJointSelectionSphereAtPoint(e.getPoint());
    if (selected != null) {
      System.out.println( "selectedFinal: " + selected );
      if (SwingUtilities.isLeftMouseButton(e)) {
        this.selected = selected;
      } else if (SwingUtilities.isRightMouseButton(e)) {

        this.anchor = selected;
      }
      return selected.getTransformable();
    } else {
      return null;
    }
  }

  private double getSphereRayIntersection(Ray ray, SSphere sSphere) {
    EntityImp sphere = sSphere.getImplementation();
    Point3 center = sphere.getTransformation(camera).translation();
    Sphere mSphereInCameraSpace = new Sphere(center, sSphere.getRadius());
    return mSphereInCameraSpace.intersect(ray);
  }
//
//  private JointSelectionSphere pickJoint(JointSelectionSphere one, JointSelectionSphere two, double distOne, double distTwo) {
//    double oneCameraDistance = one.getDistanceTo((SMovableTurnable) camera.getAbstraction());
//    double twoCameraDistance = two.getDistanceTo((SMovableTurnable) camera.getAbstraction());
//    double cameraDelta = oneCameraDistance - twoCameraDistance;
//    if (Math.abs(cameraDelta) > .1) {
//      System.out.println("short");
//      return oneCameraDistance < twoCameraDistance ? one : two;
//    } else {
//      System.out.println(cameraDelta);
//    }
//    System.out.println("=============");
//    System.out.println(one.getJoint());
//    System.out.println("oneC:  " + oneCameraDistance);
//    System.out.println("dist1: " + distOne);
//    System.out.println("twoC:  " + twoCameraDistance);
//    System.out.println("dist2: " + distTwo);
//    System.out.println(two.getJoint());
//    System.out.println("=============");
//    if ((oneCameraDistance < twoCameraDistance)) {
//      if (distOne < distTwo) {
//        System.out.println("a1");
//        return one;
//      } else if ((distTwo * 2) < distOne) {
//        System.out.println("a2");
//        return two;
//      } else {
//        System.out.println("a3");
//        return one;
//      }
//    } else if (twoCameraDistance < oneCameraDistance) {
//      if (distTwo < distOne) {
//        System.out.println("b1");
//        return two;
//      } else if ((distOne * 2) < distTwo) {
//        System.out.println("b2");
//        return one;
//      } else {
//        System.out.println("b3");
//        return two;
//      }
//    } else {
//      if (distOne < distTwo) {
//        System.out.println("c1");
//        return one;
//      } else {
//        System.out.println("c2");
//        return two;
//      }
//    }
//  }

  private ManipulationHandle3D checkIfHandleSelected(MouseEvent e) {
    SceneImp implementation = scene.getImplementation();
    RenderTarget rt = implementation.getProgram().getOnscreenRenderTarget();
    PickResult pickResult = rt.getSynchronousPicker().pickFrontMost(e.getPoint(), PickSubElementPolicy.NOT_REQUIRED);
    if ((pickResult != null) && (pickResult.getVisual() != null)) {
      Composite composite = pickResult.getVisual().getParent();
      if (composite != null) {
        if (composite.getParent() instanceof ManipulationHandle3D) {
          return (ManipulationHandle3D) composite.getParent();
        }
      }
    }
    return null;
  }

  public void addListener(PoserSphereManipulatorListener sphereDragListener) {
    this.listeners.add(sphereDragListener);
  }

  private void fireMousePressed(MouseEvent e) {
    if (selected != null) {
      for (PoserSphereManipulatorListener listener : listeners) {
        listener.fireStart(new PoserEvent(selected));
      }
    } else if (anchor != null) {
      for (PoserSphereManipulatorListener listener : listeners) {
        listener.fireAnchorUpdate(new PoserEvent(anchor));
      }
    }
  }

  private void fireMouseReleased(MouseEvent e) {
    if (joint != null) {
      JointSelectionSphere[] arr = scene.getJointSelectionSpheres().toArray(new JointSelectionSphere[0]);
      for (JointSelectionSphere sphere : arr) {
        if (sphere.getJoint().getSgComposite() == joint) {
          selected = sphere;
        }
      }
    }
    if (selected != null) {
      for (PoserSphereManipulatorListener listener : listeners) {
        listener.fireFinish(new PoserEvent(selected));
      }
      selected = null;
    }
    anchor = null;

  }

  @Override
  protected void handleMousePressed(MouseEvent e) {
    super.handleMousePressed(e);
    fireMousePressed(e);
  }

  @Override
  protected void handleMouseReleased(MouseEvent e) {
    super.handleMouseReleased(e);
    fireMouseReleased(e);
  }

  @Override
  protected void handleMouseDragged(MouseEvent e) {
    if (selected != null) {
      super.handleMouseDragged(e);
      fireMousePressed(e);
    }
  }
}
