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

package org.lgna.project.ast;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class AnonymousUserType extends UserType<AnonymousUserConstructor> {
  public AnonymousUserType() {
  }

  public AnonymousUserType(AbstractType<?, ?, ?> superType, UserMethod[] methods, UserField[] fields) {
    super(superType, methods, fields);
  }

  public AnonymousUserType(Class<?> superCls, UserMethod[] methods, UserField[] fields) {
    this(JavaType.getInstance(superCls), methods, fields);
  }

  @Override
  public List<AnonymousUserConstructor> getDeclaredConstructors() {
    //todo?
    if (this.constructors == null) {
      this.constructors = Lists.newArrayList();
      this.constructors.add(AnonymousUserConstructor.get(this));
    }
    return this.constructors;
  }

  @Override
  public StringProperty getNamePropertyIfItExists() {
    return null;
  }

  @Override
  public AbstractPackage getPackage() {
    //todo?
    return null;
  }

  @Override
  public AccessLevel getAccessLevel() {
    //todo?
    return null;
  }

  //An anonymous class is never abstract (8.1.1.1). An anonymous class is always an inner class (8.1.3); it is never static (8.1.1, 8.5.2). An anonymous class is always implicitly final (8.1.1.2).
  @Override
  public boolean isAbstract() {
    return false;
  }

  @Override
  public boolean isStatic() {
    return false;
  }

  @Override
  public boolean isFinal() {
    return true;
  }

  @Override
  public boolean isStrictFloatingPoint() {
    //todo?
    return false;
  }

  private ArrayList<AnonymousUserConstructor> constructors;
}
