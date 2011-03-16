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

package org.alice.ide.croquet.models.cascade;

/**
 * @author Dennis Cosgrove
 */
public class IncompleteStaticMethodInvocationFillIn extends ExpressionFillInWithExpressionBlanks< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractMethod, IncompleteStaticMethodInvocationFillIn > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static IncompleteStaticMethodInvocationFillIn getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		synchronized( map ) {
			IncompleteStaticMethodInvocationFillIn rv = map.get( method );
			if( rv != null ) {
				//pass
			} else {
				rv = new IncompleteStaticMethodInvocationFillIn( method );
				map.put( method, rv );
			}
			return rv;
		}
	}
	public static IncompleteStaticMethodInvocationFillIn getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > type, String methodName, Class<?>... parameterClses ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = type.getDeclaredMethod( methodName, parameterClses );
		assert method != null : methodName;
		return getInstance( method );
	}
	public static IncompleteStaticMethodInvocationFillIn getInstance( Class cls, String methodName, Class<?>... parameterClses ) {
		return getInstance( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ), methodName, parameterClses );
	}
	private final edu.cmu.cs.dennisc.alice.ast.MethodInvocation transientValue;
	private IncompleteStaticMethodInvocationFillIn( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		super( java.util.UUID.fromString( "fb3e7243-639b-43e7-8b70-ef7988ed7a97" ) );
		this.transientValue = org.alice.ide.ast.NodeUtilities.createIncompleteStaticMethodInvocation( method );
		//java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = method.getParameters();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : method.getParameters() ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > desiredType = parameter.getDesiredValueType();
			edu.cmu.cs.dennisc.croquet.CascadeBlank< edu.cmu.cs.dennisc.alice.ast.Expression > blank = CascadeManager.getBlankForType( desiredType );
			//this.addBlank( blank );
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.MethodInvocation createValue( edu.cmu.cs.dennisc.croquet.CascadeFillInContext context ) {
		edu.cmu.cs.dennisc.croquet.CascadeBlank< edu.cmu.cs.dennisc.alice.ast.Expression >[] blanks = this.getBlanks();
		
		//return null;
		return this.transientValue;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.MethodInvocation getTransientValue( edu.cmu.cs.dennisc.croquet.CascadeFillInContext context ) {
		return this.transientValue;
	}
}
