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
package org.alice.stageide.tutorial;

/**
 * @author Dennis Cosgrove
 */
public class EntryPoint {
	public static void main( final String[] args ) {
		org.alice.stageide.StageIDE ide = new org.alice.stageide.StageIDE();
		ide.initialize(args);
		ide.loadProjectFrom( args[ 0 ] );
		ide.getFrame().maximize();
		ide.getFrame().setVisible( true );
		
		edu.cmu.cs.dennisc.tutorial.Tutorial tutorial = new edu.cmu.cs.dennisc.tutorial.Tutorial();
		tutorial.createAndAddMessageStep( 
				"Welcome", 
				"<html><b><center>Welcome To The Tutorial</center></b><p>First we'll show you around a bit.</html>" 
		);
		tutorial.createAndAddSpotlightStep( 
				"Scene Editor", 
				"<html>This is the scene editor.</html>", 
				ide.getSceneEditor() 
		);
		tutorial.createAndAddSpotlightStep( 
				"Constructs",
				"<html>This where loops a locals live.</html>", 
				ide.getUbiquitousPane() 
		);
		tutorial.createAndAddActionStep( 
				"Run", 
				"<html>Press the <b>Run</b> button</html>", 
				ide.getRunOperation() 
		);
		tutorial.createAndAddSpotlightStep( 
				"Instance Details", 
				"<html>This is the currently selected instance methods and fields pane.</html>", 
				ide.getMembersEditor() 
		);
		tutorial.createAndAddTabStateStep( 
				"Select Functions Tab", 
				"<html>Select the <b>Functions</b> tab.</html>", 
				ide.getMembersEditor().getFunctionsTabStateOperation() 
		);
		tutorial.createAndAddSpotlightStep( 
				"Note Functions Tab", 
				"<html>Now the functions are now available.</html>", 
				ide.getMembersEditor().getFunctionsTabStateOperation().getSingletonView() 
		);
		tutorial.createAndAddTabStateStep( 
				"Properies Tab", 
				"<html>Select the <b>Properies</b> tab.</html>", 
				ide.getMembersEditor().getFieldsTabStateOperation() 
		);
		tutorial.createAndAddSpotlightStep( 
				"Note Properies Tab", 
				"<html>Now the properties are now available.</html>", 
				ide.getMembersEditor().getFieldsTabStateOperation().getSingletonScrollPane() 
		);
		tutorial.createAndAddTabStateStep( 
				"Procedures Tab", 
				"<html>Select the <b>Procedures</b> tab.</html>", 
				ide.getMembersEditor().getProceduresTabStateOperation() 
		);
		tutorial.createAndAddSpotlightStep( 
				"Note Procedures Tab", 
				"<html>Now the procedures are now available.</html>", 
				ide.getMembersEditor().getProceduresTabStateOperation().getSingletonView() 
		);

		ide.getMembersEditor().getFunctionsTabStateOperation().setState( true );
		tutorial.setSelectedIndex( 6 );
		
		tutorial.setVisible( true );
	}
}
