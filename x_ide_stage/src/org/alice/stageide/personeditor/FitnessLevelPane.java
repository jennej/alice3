/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.personeditor;


/**
 * @author Dennis Cosgrove
 */
//todo: note, not really inconsequential
class FitnessLevelActionOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	private javax.swing.JSlider slider;
	private int value;
	public FitnessLevelActionOperation( javax.swing.JSlider slider, int value, String name ) {
		this.slider = slider;
		this.value = value;
		this.putValue( javax.swing.Action.NAME, name );
	}
	@Override
	protected void performInternal(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		this.slider.setValue( this.value );
	}
}

/**
 * @author Dennis Cosgrove
 */
class FitnessLevelPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	private FitnessLevelSlider slider = new FitnessLevelSlider();
	public FitnessLevelPane() {
		this.add( new edu.cmu.cs.dennisc.zoot.ZButton( new FitnessLevelActionOperation( slider, 0, "SOFT" ) ), java.awt.BorderLayout.WEST );
		this.add( this.slider, java.awt.BorderLayout.CENTER );
		this.add( new edu.cmu.cs.dennisc.zoot.ZButton( new FitnessLevelActionOperation( slider, 100, "CUT" ) ), java.awt.BorderLayout.EAST );
	}

	public void setFitnessLevel( Double fitnessLevel ) {
		this.slider.setValue( (int)((fitnessLevel+0.005)*100) );
	}
}
