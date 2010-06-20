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
package org.alice.stageide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public class CustomizeAudioSourceFillIn extends org.alice.ide.cascade.customfillin.CustomFillIn<edu.cmu.cs.dennisc.alice.ast.InstanceCreation, org.alice.apis.moveandturn.AudioSource> {
	@Override
	protected String getMenuProxyText() {
		return "Customize Audio Source...";
	}
	@Override
	protected org.alice.ide.choosers.ValueChooser createValueChooser() {
		return new org.alice.stageide.choosers.AudioSourceChooser();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.InstanceCreation createExpression( org.alice.apis.moveandturn.AudioSource value ) {
		if( value != null ) {
			org.alice.virtualmachine.resources.AudioResource audioResource = value.getAudioResource();

			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
			edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
			if( project != null ) {
				project.addResource( audioResource );
			}

			edu.cmu.cs.dennisc.alice.ast.Expression arg0Expression;
			if( audioResource != null ) {
				arg0Expression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.AudioResource.class, audioResource );				
			} else {
				arg0Expression = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
			}

			double volume = value.getVolume();
			double startTime = value.getStartTime();
			double stopTime = value.getStopTime();

			// apologies for the negative logic
			boolean isNotDefaultVolume = org.alice.apis.moveandturn.AudioSource.isWithinReasonableEpsilonOfDefaultVolume( volume ) == false;
			boolean isNotDefaultStartTime = org.alice.apis.moveandturn.AudioSource.isWithinReasonableEpsilonOfDefaultStartTime( startTime ) == false;
			boolean isNotDefaultStopTime = org.alice.apis.moveandturn.AudioSource.isDefaultStopTime_aka_NaN( stopTime ) == false;

			if( isNotDefaultVolume || isNotDefaultStartTime || isNotDefaultStopTime ) {
				edu.cmu.cs.dennisc.alice.ast.DoubleLiteral volumeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( volume );
				if( isNotDefaultStartTime || isNotDefaultStopTime ) {
					edu.cmu.cs.dennisc.alice.ast.DoubleLiteral startTimeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( startTime );
					if( isNotDefaultStopTime ) {
						edu.cmu.cs.dennisc.alice.ast.DoubleLiteral stopTimeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( stopTime );

						edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
								org.alice.apis.moveandturn.AudioSource.class, 
								org.alice.virtualmachine.resources.AudioResource.class,
								Number.class, 
								Number.class, 
								Number.class );
						return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral, startTimeLiteral, stopTimeLiteral );
					} else {
						edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
								org.alice.apis.moveandturn.AudioSource.class, 
								org.alice.virtualmachine.resources.AudioResource.class,
								Number.class, 
								Number.class );
						return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral, startTimeLiteral );
					}
				} else {
					edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
							org.alice.apis.moveandturn.AudioSource.class, 
							org.alice.virtualmachine.resources.AudioResource.class,
							Number.class );
					return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral );
				}
			} else {
				edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
						org.alice.apis.moveandturn.AudioSource.class, 
						org.alice.virtualmachine.resources.AudioResource.class );
				return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression );
			}
		} else {
			return null;
		}
	}
}
