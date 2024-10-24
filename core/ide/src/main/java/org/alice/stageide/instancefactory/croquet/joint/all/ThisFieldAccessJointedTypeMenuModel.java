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

package org.alice.stageide.instancefactory.croquet.joint.all;

import edu.cmu.cs.dennisc.map.MapToMap;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.ThisFieldAccessArrayElementMethodInvocationFactory;
import org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory;
import org.alice.ide.instancefactory.croquet.InstanceFactoryFillIn;
import org.alice.stageide.ast.JointedTypeInfo;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.UserField;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ThisFieldAccessJointedTypeMenuModel extends JointedTypeMenuModel {
  private static MapToMap<UserField, Integer, ThisFieldAccessJointedTypeMenuModel> mapToMap = MapToMap.newInstance();

  public static ThisFieldAccessJointedTypeMenuModel getInstance(UserField value) {
    List<JointedTypeInfo> jointedTypeInfos = JointedTypeInfo.getInstances(value.getValueType());
    return getInstance(value, jointedTypeInfos, 0);
  }

  private static ThisFieldAccessJointedTypeMenuModel getInstance(UserField value, List<JointedTypeInfo> jointedTypeInfos, int index) {
    //todo
    synchronized (mapToMap) {
      ThisFieldAccessJointedTypeMenuModel rv = mapToMap.get(value, index);
      if (rv == null) {
        rv = new ThisFieldAccessJointedTypeMenuModel(value, jointedTypeInfos, index);
        mapToMap.put(value, index, rv);
      }
      return rv;
    }
  }

  private final UserField field;

  private ThisFieldAccessJointedTypeMenuModel(UserField field, List<JointedTypeInfo> jointedTypeInfos, int index) {
    super(UUID.fromString("bb23e6d5-9eab-4e8d-9aaf-0016f3465634"), jointedTypeInfos, index);
    this.field = field;
  }

  @Override
  protected JointedTypeMenuModel getInstance(List<JointedTypeInfo> jointedTypeInfos, int index) {
    return getInstance(this.field, jointedTypeInfos, index);
  }

  @Override
  protected CascadeFillIn<InstanceFactory, ?> getFillIn(AbstractMethod method) {
    if (method.getReturnType().isArray()) {
      return InstanceFactoryFillIn.getInstance(ThisFieldAccessArrayElementMethodInvocationFactory.getInstance(this.field, method));
    } else {
      return InstanceFactoryFillIn.getInstance(ThisFieldAccessMethodInvocationFactory.getInstance(this.field, method));
    }
  }
}
