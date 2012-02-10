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
package org.alice.ide.croquet.models.help;

/**
 * @author Dennis Cosgrove
 */
public abstract class ShowPathPropertyOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	private String propertyName;
	public ShowPathPropertyOperation( java.util.UUID id, String propertyName ) {
		super( id );
		this.propertyName = propertyName;
	}
	public String getPropertyName() {
		return this.propertyName;
	}
	@Override
	protected void performInternal( org.lgna.croquet.history.OperationStep step ) {
		org.lgna.croquet.components.RowsSpringPanel formPane = new org.lgna.croquet.components.RowsSpringPanel( 8, 2 ) {
			private org.lgna.croquet.components.Component< ? >[][] createComponentRowsForSystemProperty( String name, String separator ) {
				String value = System.getProperty( name );
				assert value != null;
				String[] array = value.split( separator );
				org.lgna.croquet.components.Component< ? >[][] rv = new org.lgna.croquet.components.Component< ? >[ array.length ][];
				for( int i=0; i<array.length; i++ ) {
					String prefix;
					if( i==0 ) {
						prefix = name;
					} else {
						prefix = "";
					}
					rv[ i ] = org.lgna.croquet.components.SpringUtilities.createRow( org.lgna.croquet.components.SpringUtilities.createTrailingLabel( prefix+"[" + i + "]:" ), new org.lgna.croquet.components.Label( array[ i ] ) );
				}
				return rv;
			}
			@Override
			protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
				String pathSepartor = System.getProperty( "path.separator" );
				for( org.lgna.croquet.components.Component< ? >[] componentRow : createComponentRowsForSystemProperty( propertyName, pathSepartor ) ) {
					rv.add( componentRow );
				}
				return rv;
			}
		};
		org.lgna.croquet.Application.getActiveInstance().showMessageDialog( formPane, "System Property: " + this.propertyName, org.lgna.croquet.MessageType.INFORMATION ); 
	}
}
