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
package org.alice.stageide;

import org.alice.stageide.ast.SceneAdapter;
import org.lgna.croquet.components.JComponent;
import org.lgna.project.ast.JavaType;
import org.lgna.project.virtualmachine.VirtualMachine;
import org.lgna.story.Scene;
import org.lgna.story.resourceutilities.ModelResourceTreeNode;
import org.lgna.story.resourceutilities.StorytellingResources;

import edu.cmu.cs.dennisc.javax.swing.models.TreeNode;

public class StageIDE extends org.alice.ide.IDE {
	public static StageIDE getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance(  org.alice.ide.IDE.getActiveInstance(), StageIDE.class );
	}
	private org.alice.ide.cascade.CascadeManager cascadeManager = new org.alice.stageide.cascade.CascadeManager();
	public StageIDE() {
		org.alice.ide.common.BeveledShapeForType.addRoundType( org.lgna.story.Entity.class );
		this.getFrame().addWindowStateListener( new java.awt.event.WindowStateListener() {
			public void windowStateChanged( java.awt.event.WindowEvent e ) {
				int oldState = e.getOldState();
				int newState = e.getNewState();
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "windowStateChanged", oldState, newState, java.awt.Frame.ICONIFIED );
				if( (oldState & java.awt.Frame.ICONIFIED) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
				}
				if( (newState & java.awt.Frame.ICONIFIED) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
				}
			}
		} );

		final int SMALL_ICON_SIZE = 32;
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lgna.story.Sun.class, new javax.swing.Icon() {

			public int getIconWidth() {
				return SMALL_ICON_SIZE;
			}
			public int getIconHeight() {
				return SMALL_ICON_SIZE;
			}
			
			private java.awt.Shape createArc( float size ) {
				java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
				rv.moveTo( 0.0f, 0.0f );
				rv.lineTo( size, 0.0f );
				rv.quadTo( size, size, 0.0f, size );
				rv.closePath();
				return rv;
			}
			public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				java.awt.geom.AffineTransform m = g2.getTransform();
				Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				try {
					java.awt.Shape innerArc = this.createArc( 20.0f );
					java.awt.Shape outerArc = this.createArc( 22.0f );
					
					g2.translate( 4.0f, 4.0f );
					java.awt.geom.GeneralPath pathRays = new java.awt.geom.GeneralPath();
					double thetaN = Math.PI/2.0;
					double thetaDelta = thetaN/8.0;
					g2.setColor( new java.awt.Color( 255, 210, 0 ) );
					for( double theta = 0.0; theta<=thetaN; theta += thetaDelta ) {
						pathRays.moveTo( 0.0f, 0.0f );
						pathRays.lineTo( (float)( Math.cos( theta ) * 26.0 ), (float)( Math.sin( theta ) * 26.0 ) ); 
					}
					g2.draw( pathRays );
					g2.fill( outerArc );

					g2.setColor( new java.awt.Color( 230, 230, 0 ) );
					g2.fill( innerArc );
				} finally {
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
					g2.setTransform( m );
				}
			}
		} );
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lgna.story.Camera.class, edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( org.alice.stageide.gallerybrowser.ResourceManager.class.getResource( "images/SymmetricPerspectiveCamera.png" ) ) );
//		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lookingglassandalice.storytelling.Camera.class, new javax.swing.Icon() {
//			public int getIconWidth() {
//				return SMALL_ICON_SIZE;
//			}
//			public int getIconHeight() {
//				return SMALL_ICON_SIZE;
//			}
//			public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
//				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//				java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//				path.moveTo( 4,4 );
//				path.lineTo( 20, 4 );
//				path.lineTo( 20, 12 );
//				path.lineTo( 28, 8 );
//				path.lineTo( 28, 20 );
//				path.lineTo( 20, 16 );
//				path.lineTo( 20, 24 );
//				path.lineTo( 4, 24 );
//				path.closePath();
//				g2.setColor( java.awt.Color.GRAY );
//				g2.fill( path );
//				g2.setColor( java.awt.Color.BLACK );
//				g2.draw( path );
//			}
//		} );
	}
	
	@Override
	public org.alice.ide.ApiConfigurationManager getApiConfigurationManager() {
		return StoryApiConfigurationManager.SINGLETON;
	}
	@Override
	protected void registerAdapters(VirtualMachine vm) {
		vm.registerAnonymousAdapter( Scene.class, SceneAdapter.class );
	}
	private org.lgna.project.ast.JavaType MOUSE_BUTTON_LISTENER_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.event.MouseButtonListener.class );
	private org.lgna.project.ast.JavaType KEY_LISTENER_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.event.KeyListener.class );
	@Override
	protected org.lgna.project.ast.Expression createPredeterminedExpressionIfAppropriate( org.lgna.project.ast.AbstractType< ?, ?, ? > desiredValueType ) {
		if( desiredValueType == MOUSE_BUTTON_LISTENER_TYPE ) {
			org.lgna.project.ast.UserParameter[] parameters = new org.lgna.project.ast.UserParameter[] {
					new org.lgna.project.ast.UserParameter( "e", org.lgna.story.event.MouseButtonEvent.class )
			};
			org.lgna.project.ast.BlockStatement body = new org.lgna.project.ast.BlockStatement();
			org.lgna.project.ast.UserMethod method = new org.lgna.project.ast.UserMethod( "mouseButtonClicked", Void.TYPE, parameters, body );
			method.isSignatureLocked.setValue( true );
			org.lgna.project.ast.AnonymousUserType type = new org.lgna.project.ast.AnonymousUserType();
			type.superType.setValue( desiredValueType );
			type.methods.add( method );
			org.lgna.project.ast.AnonymousUserConstructor constructor = org.lgna.project.ast.AnonymousUserConstructor.get( type );
			return new org.lgna.project.ast.InstanceCreation( constructor );
		} else if( desiredValueType == KEY_LISTENER_TYPE ) {
			org.lgna.project.ast.UserParameter[] parameters = new org.lgna.project.ast.UserParameter[] {
					new org.lgna.project.ast.UserParameter( "e", org.lgna.story.event.KeyEvent.class )
			};
			org.lgna.project.ast.BlockStatement body = new org.lgna.project.ast.BlockStatement();
			org.lgna.project.ast.UserMethod method = new org.lgna.project.ast.UserMethod( "keyPressed", Void.TYPE, parameters, body );
			method.isSignatureLocked.setValue( true );
			org.lgna.project.ast.AnonymousUserType type = new org.lgna.project.ast.AnonymousUserType();
			type.superType.setValue( desiredValueType );
			type.methods.add( method );
			org.lgna.project.ast.AnonymousUserConstructor constructor = org.lgna.project.ast.AnonymousUserConstructor.get( type );
			return new org.lgna.project.ast.InstanceCreation( constructor );
		} else {
			return super.createPredeterminedExpressionIfAppropriate( desiredValueType );
		}
	}
	@Override
	public org.alice.ide.cascade.CascadeManager getCascadeManager() {
		return this.cascadeManager;
	}
	
	@Override
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			edu.cmu.cs.dennisc.eula.EulaUtilities.promptUserToAcceptEULAIfNecessary( org.lgna.project.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 3): Alice 3", org.lgna.project.License.TEXT, "Alice" );
//			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.wustl.cse.lookingglass.apis.walkandtouch.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 2 of 3): Looking Glass Walk & Touch API",
//					edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT_FOR_USE_IN_ALICE, "the Looking Glass Walk & Touch API" );
			edu.cmu.cs.dennisc.eula.EulaUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.nebulous.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 3 of 3): The Sims (TM) 2 Art Assets",
					edu.cmu.cs.dennisc.nebulous.License.TEXT, "The Sims (TM) 2 Art Assets" );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			this.showMessageDialog( "You must accept the license agreements in order to use Alice 3, the Looking Glass Walk & Touch API, and The Sims (TM) 2 Art Assets.  Exiting." );
			System.exit( -1 );
		}
	}

	private static final org.lgna.project.ast.JavaType COLOR_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Color.class );
	private static final JavaType JOINTED_MODEL_RESOURCE_TYPE = JavaType.getInstance( org.lgna.story.resources.JointedModelResource.class );

	private java.util.Map< org.lgna.project.ast.AbstractField, org.alice.ide.swing.icons.ColorIcon > mapFieldToIcon = new java.util.HashMap< org.lgna.project.ast.AbstractField, org.alice.ide.swing.icons.ColorIcon >();

	private javax.swing.Icon getIconFor( org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
		org.lgna.project.ast.AbstractType< ?,?,? > valueType = field.getDeclaringType();
		if( declaringType == COLOR_TYPE && valueType == COLOR_TYPE ) {
			org.alice.ide.swing.icons.ColorIcon rv = this.mapFieldToIcon.get( field );
			if( rv != null ) {
				//pass
			} else {
				try {
					org.lgna.project.ast.JavaField fieldInJava = (org.lgna.project.ast.JavaField)field;
					org.lgna.story.Color color = (org.lgna.story.Color)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fieldInJava.getFieldReflectionProxy().getReification(), null );
					rv = new org.alice.ide.swing.icons.ColorIcon( org.lgna.story.ImplementationAccessor.getColor4f( color ).getAsAWTColor() );
					this.mapFieldToIcon.put( field, rv );
				} catch( RuntimeException re ) {
					//pass
				}
			}
			return rv;
		} else if( declaringType.isAssignableTo( JOINTED_MODEL_RESOURCE_TYPE ) && valueType.isAssignableTo( JOINTED_MODEL_RESOURCE_TYPE ) ) {
			Class<?> resourceClass = ((org.lgna.project.ast.JavaType)field.getValueType()).getClassReflectionProxy().getReification();
			java.awt.image.BufferedImage thumbnail = org.lgna.story.resourceutilities.ModelResourceUtilities.getThumbnail(resourceClass, field.getName());
			return new edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon( thumbnail, 32, 32 );
		}
		return null;
	}
	
//	@Override
//	protected boolean isInclusionOfTypeDesired(edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> valueTypeInAlice) {
//		return super.isInclusionOfTypeDesired(valueTypeInAlice) || valueTypeInAlice.isAssignableTo( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.Camera.class ) );
//	}

	@Override
	protected boolean isAccessibleDesired( org.lgna.project.ast.Accessible accessible ) {
		if( super.isAccessibleDesired( accessible ) ) {
//			if( accessible.getValueType().isAssignableTo( org.lookingglassandalice.storytelling.Marker.class) ) {
//				return false;
//			} else {
				return accessible.getValueType().isAssignableTo( org.lgna.story.Entity.class );
//			}
		} else {
			return false;
		}
	}
	@Override
	public org.lgna.croquet.components.Component< ? > getPrefixPaneForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess ) {
		org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
		javax.swing.Icon icon = getIconFor( field );
		if( icon != null ) {
			return new org.lgna.croquet.components.Label( icon );
		}
		return super.getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
	}

	protected org.alice.ide.common.DeclarationNameLabel createDeclarationNameLabel( org.lgna.project.ast.AbstractField field ) {
		//todo: better name
		class ThisFieldAccessNameLabel extends org.alice.ide.common.DeclarationNameLabel {
			public ThisFieldAccessNameLabel( org.lgna.project.ast.AbstractField field ) {
				super( field );
			}
			@Override
			protected String getNameText() {
				if( org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().getValue() ) {
					return "this." + super.getNameText();
				} else {
					return super.getNameText();
				}
			}
		}
		return new ThisFieldAccessNameLabel( field );
	}
	@Override
	public org.lgna.croquet.components.JComponent< ? > getOverrideComponent( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.Expression expression ) {
		if( expression instanceof org.lgna.project.ast.FieldAccess ) {
			org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)expression;
			org.lgna.project.ast.Expression fieldExpression = fieldAccess.expression.getValue();
			if( fieldExpression instanceof org.lgna.project.ast.ThisExpression ) {
				org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
				org.lgna.project.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
				if( declaringType != null && declaringType.isAssignableTo( org.lgna.story.Scene.class ) ) {
					if( field.getValueType().isAssignableTo( org.lgna.story.Entity.class ) ) {
						return new org.alice.ide.common.ExpressionPane( expression, this.createDeclarationNameLabel( field ) ) {
							@Override
							protected boolean isExpressionTypeFeedbackDesired() {
								return true;
							}
						};
					}
				}
			}
		}
		return super.getOverrideComponent( factory, expression );
	}
	@Override
	public boolean isDropDownDesiredFor( org.lgna.project.ast.Expression expression ) {
		if( super.isDropDownDesiredFor( expression ) ) {
			if( expression != null ) {
				if (expression instanceof org.lgna.project.ast.InstanceCreation) {
					org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation) expression;
					org.lgna.project.ast.AbstractType<?,?,?> type = instanceCreation.getType();
					if( type instanceof org.lgna.project.ast.AnonymousUserType ) {
						if( type.isAssignableTo( org.lgna.story.event.KeyListener.class ) || type.isAssignableTo( org.lgna.story.event.MouseButtonListener.class ) ) {
							return false;
						}
					}
				} else {
					org.lgna.project.ast.Node parent = expression.getParent();
					if( parent instanceof org.lgna.project.ast.FieldAccess ) {
						org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)parent;
						org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
						assert field != null;
						org.lgna.project.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
						if( declaringType != null && declaringType.isAssignableTo( org.lgna.story.Scene.class ) ) {
							if( field.getValueType().isAssignableTo( org.lgna.story.Turnable.class ) ) {
								return false;
							}
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	@Override
	public org.lgna.croquet.Operation<?> getRunOperation() {
		return EPIC_HACK_getRunDialogOperation();
	}
	
	public org.lgna.croquet.PlainDialogOperation EPIC_HACK_getRunDialogOperation() {
		return org.alice.stageide.croquet.models.run.RunOperation.getInstance();
	}
	
	
	
	@Override
	public org.lgna.croquet.Operation< ? > getRestartOperation() {
		return org.alice.stageide.croquet.models.run.RestartOperation.getInstance();
	}
	@Override
	public org.lgna.croquet.Operation<?> createPreviewOperation( org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate ) {
		return new org.alice.stageide.croquet.models.run.PreviewMethodOperation( procedureInvocationTemplate );
	}
//	@Override
//	public void handlePreviewMethod( edu.cmu.cs.dennisc.croquet.ModelContext context, edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation ) {
//		this.ensureProjectCodeUpToDate();
//		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getFieldSelectionState().getValue();
//		if( field == this.getSceneField() ) {
//			field = null;
//		}
//		TestMethodProgram testProgram = new TestMethodProgram( this.getSceneType(), field, emptyExpressionMethodInvocation );
//		this.disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM );
//		try {
//			testProgram.showInJDialog( this.getFrame().getAwtWindow(), true, new String[] { "X_LOCATION=" + xLocation, "Y_LOCATION=" + yLocation } );
//		} finally {
//			this.enableRendering();
//			try {
//				this.xLocation = Integer.parseInt( testProgram.getParameter( "X_LOCATION" ) );
//			} catch( Throwable t ) {
//				this.xLocation = 0;
//			}
//			try {
//				this.yLocation = Integer.parseInt( testProgram.getParameter( "Y_LOCATION" ) );
//			} catch( Throwable t ) {
//				this.yLocation = 0;
//			}
//		}
//	}

	@Override
	public org.alice.stageide.sceneeditor.StorytellingSceneEditor getSceneEditor() {
		return org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance();
	}
	
	@Override
	public org.lgna.croquet.Operation< ? > getAboutOperation() {
		return org.alice.stageide.croquet.models.help.AboutOperation.getInstance();
	}

	private java.util.Map< org.lgna.project.ast.AbstractType, String > mapTypeToText;

	private static org.lgna.project.ast.UserMethod getDeclaredMethod( org.lgna.project.ast.NamedUserType type, String name, Class< ? >... paramClses ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( type.getDeclaredMethod( name, paramClses ), org.lgna.project.ast.UserMethod.class );
	}
	private static org.lgna.project.ast.NamedUserConstructor getDeclaredConstructor( org.lgna.project.ast.NamedUserType type, Class< ? >... paramClses ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( type.getDeclaredConstructor( paramClses ), org.lgna.project.ast.NamedUserConstructor.class );
	}

	@Override
	public void setProject( org.lgna.project.Project project ) {
		super.setProject( project );
		if( project != null ) {
			org.lgna.project.ast.NamedUserType programType = project.getProgramType();
			org.lgna.project.ast.NamedUserType sceneType = getSceneTypeFromProgramType( programType );
			if( sceneType != null ) {
				this.setFocusedCode( sceneType.findMethod( "myFirstMethod" ) );
			}
		}
	}

	private static String createExampleText( String examples ) {
		return "<html><em>examples:</em> " + examples + "</html>";
	}
	@Override
	public String getTextFor( org.lgna.project.ast.AbstractType type ) {
		if( mapTypeToText != null ) {
			//pass
		} else {
			mapTypeToText = new java.util.HashMap< org.lgna.project.ast.AbstractType, String >();
			mapTypeToText.put( org.lgna.project.ast.JavaType.DOUBLE_OBJECT_TYPE, createExampleText( "0.25, 1.0, 3.14, 98.6" ) );
			mapTypeToText.put( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE, createExampleText( "1, 2, 42, 100" ) );
			mapTypeToText.put( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE, createExampleText( "true, false" ) );
			mapTypeToText.put( org.lgna.project.ast.JavaType.getInstance( String.class ), createExampleText( "\"hello\", \"goodbye\"" ) );
		}
		return mapTypeToText.get( type );
	}

	@Override
	protected java.util.List< ? super org.lgna.project.ast.JavaType > addPrimeTimeJavaTypes( java.util.List< ? super org.lgna.project.ast.JavaType > rv ) {
		rv = super.addPrimeTimeJavaTypes( rv );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Biped.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Model.class ) );
		return rv;
	}

	@Override
	protected java.util.List<? super org.lgna.project.ast.JavaType> addSecondaryJavaTypes(java.util.List<? super org.lgna.project.ast.JavaType> rv) {
		super.addSecondaryJavaTypes(rv);
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Color.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.MoveDirection.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.TurnDirection.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.RollDirection.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Model.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Marker.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.ObjectMarker.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.CameraMarker.class ) );
		return rv;
	}
	
	@Override
	protected JComponent<?> createClassGalleryBrowser(TreeNode<JavaType> root) {
		assert root instanceof ModelResourceTreeNode;
		//return new org.alice.stageide.gallerybrowser.ClassBasedGalleryBrowser( (ModelResourceTreeNode)root );
		return new org.alice.stageide.gallerybrowser.GalleryBrowser();
	}
	
	@Override
	public ModelResourceTreeNode getClassGalleryRoot() {
		return StorytellingResources.getInstance().getGalleryTree();
	}
	
//	@Override
//	public boolean isDeclareFieldOfPredeterminedTypeSupported( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice valueType ) {
//		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = valueType.getFirstTypeEncounteredDeclaredInJava();
//		if( typeInJava == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Adult.class ) ) {
//			this.showMessageDialog( "todo: isDeclareFieldOfPredeterminedTypeSupported" );
//			return false;
//		} else {
//			return super.isDeclareFieldOfPredeterminedTypeSupported( valueType );
//		}
//	}
	@Override
	public boolean isInstanceCreationAllowableFor( org.lgna.project.ast.NamedUserType typeInAlice ) {
		org.lgna.project.ast.JavaType typeInJava = typeInAlice.getFirstTypeEncounteredDeclaredInJava();
		return false == edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( typeInJava.getClassReflectionProxy().getReification(), org.lgna.story.Scene.class, org.lgna.story.Camera.class );
	}
	@Override
	public edu.cmu.cs.dennisc.animation.Program createRuntimeProgramForMovieEncoding( org.lgna.project.virtualmachine.VirtualMachine vm, org.lgna.project.ast.NamedUserType programType, int frameRate ) {
		throw new RuntimeException( "todo" );
//		return new MoveAndTurnRuntimeProgram( sceneType, vm ) {
//			@Override
//			protected java.awt.Component createSpeedMultiplierControlPanel() {
//				return null;
//			}
//			@Override
//			protected edu.cmu.cs.dennisc.animation.Animator createAnimator() {
//				return new edu.cmu.cs.dennisc.animation.FrameBasedAnimator( frameRate );
//			}
//
//			@Override
//			protected void postRun() {
//				super.postRun();
//				this.setMovieEncoder( null );
//			}
//		};
	}

	private static final int THUMBNAIL_WIDTH = 160;
	private static final int THUMBNAIL_HEIGHT = THUMBNAIL_WIDTH * 3 / 4;
	private edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass offscreenLookingGlass;

	@Override
	protected java.awt.image.BufferedImage createThumbnail() throws Throwable {
		if( offscreenLookingGlass != null ) {
			//pass
		} else {
			offscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createOffscreenLookingGlass( null );
			offscreenLookingGlass.setSize( THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT );
		}
		org.alice.stageide.sceneeditor.StorytellingSceneEditor sceneEditor = this.getSceneEditor();
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = sceneEditor.getSGCameraForCreatingThumbnails();
		boolean isClearingAndAddingRequired;
		if( offscreenLookingGlass.getCameraCount() == 1 ) {
			if( offscreenLookingGlass.getCameraAt( 0 ) == sgCamera ) {
				isClearingAndAddingRequired = false;
			} else {
				isClearingAndAddingRequired = true;
			}
		} else {
			isClearingAndAddingRequired = true;
		}
		if( isClearingAndAddingRequired ) {
			offscreenLookingGlass.clearCameras();
			offscreenLookingGlass.addCamera( sgCamera );
		}
		java.awt.image.BufferedImage rv = offscreenLookingGlass.getColorBuffer();
		return rv;
	}

	@Override
	protected org.alice.ide.openprojectpane.TabContentPanel createTemplatesTabContentPane() {
		return new org.alice.stageide.openprojectpane.components.TemplatesTabContentPane();
	}

	@Override
	public java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? >>> updateNameClsPairsForRelationalFillIns( java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? >>> rv ) {
		super.updateNameClsPairsForRelationalFillIns( rv );
		//rv.add( new edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? > >( "Key", org.lookingglassandalice.storytelling.Key.class ) );
		return rv;
	}

}
