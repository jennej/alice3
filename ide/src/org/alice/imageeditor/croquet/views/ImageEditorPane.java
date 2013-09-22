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
package org.alice.imageeditor.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditorPane extends org.lgna.croquet.components.MigPanel {
	private static java.awt.Shape createShape( java.awt.Point a, java.awt.Point b ) {
		int x = Math.min( a.x, b.x );
		int y = Math.min( a.y, b.y );
		int width = Math.abs( b.x - a.x );
		int height = Math.abs( b.y - a.y );
		return new java.awt.Rectangle( x, y, width, height );
	}

	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 3.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	private static final javax.swing.KeyStroke ESCAPE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ESCAPE, 0 );
	private static final javax.swing.KeyStroke CLEAR_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK );

	private class JImageView extends javax.swing.JComponent {
		private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
			public void mousePressed( java.awt.event.MouseEvent e ) {
				if( e.getButton() == java.awt.event.MouseEvent.BUTTON1 ) {
					ptPressed = e.getPoint();
				} else {
					ptPressed = null;
					repaint();
				}
			}

			public void mouseReleased( java.awt.event.MouseEvent e ) {
				if( ptPressed != null ) {
					org.alice.imageeditor.croquet.ImageEditorFrame composite = getComposite();
					composite.addShape( createShape( ptPressed, e.getPoint() ) );
					ptPressed = null;
					ptDragged = null;
					repaint();
				}
			}

			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}

			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}

			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
		};

		private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
			public void mouseMoved( java.awt.event.MouseEvent e ) {

			}

			public void mouseDragged( java.awt.event.MouseEvent e ) {
				if( ptPressed != null ) {
					ptDragged = e.getPoint();
					repaint();
				}
			}
		};

		private final java.awt.event.ActionListener escapeListener = new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				ptPressed = null;
				ptDragged = null;
				repaint();
			}
		};

		private java.awt.Point ptPressed;
		private java.awt.Point ptDragged;

		private void render( java.awt.Graphics g ) {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			org.alice.imageeditor.croquet.ImageEditorFrame composite = getComposite();
			java.awt.Image image = composite.getImageHolder().getValue();
			if( image != null ) {
				g.drawImage( image, 0, 0, this );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
			}

			java.awt.Stroke prevStroke = g2.getStroke();
			g2.setPaint( java.awt.Color.RED );
			g2.setStroke( STROKE );
			for( java.awt.Shape shape : composite.getShapes() ) {
				g2.draw( shape );
			}

			if( ( ptPressed != null ) && ( ptDragged != null ) ) {
				g2.draw( createShape( ptPressed, ptDragged ) );
			}
			g2.setStroke( prevStroke );
		}

		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );
			this.render( g );
		}

		@Override
		public java.awt.Dimension getPreferredSize() {
			org.alice.imageeditor.croquet.ImageEditorFrame composite = getComposite();
			java.awt.Image image = composite.getImageHolder().getValue();
			if( image != null ) {
				return new java.awt.Dimension( image.getWidth( this ), image.getHeight( this ) );
			} else {
				return super.getPreferredSize();
			}
		}

		@Override
		public void addNotify() {
			super.addNotify();
			this.addMouseListener( this.mouseListener );
			this.addMouseMotionListener( this.mouseMotionListener );
			this.registerKeyboardAction( this.escapeListener, ESCAPE_KEY_STROKE, javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );
		}

		@Override
		public void removeNotify() {
			this.unregisterKeyboardAction( CLEAR_KEY_STROKE );
			this.unregisterKeyboardAction( ESCAPE_KEY_STROKE );
			this.removeMouseMotionListener( this.mouseMotionListener );
			this.removeMouseListener( this.mouseListener );
			super.removeNotify();
		}
	};

	private final java.awt.event.FocusListener comboBoxEditorFocusListener = new java.awt.event.FocusListener() {
		public void focusGained( java.awt.event.FocusEvent e ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					javax.swing.ComboBoxEditor editor = getComposite().getJComboBox().getEditor();
					java.awt.Component editorComponent = editor.getEditorComponent();
					if( editorComponent instanceof javax.swing.text.JTextComponent ) {
						javax.swing.text.JTextComponent jTextComponent = (javax.swing.text.JTextComponent)editorComponent;
						String rootDirectoryPath = getComposite().getRootDirectoryState().getValue();
						String path = jTextComponent.getText();
						if( path.startsWith( rootDirectoryPath ) ) {
							int startIndex = rootDirectoryPath.length();
							if( path.length() > startIndex ) {
								char c = path.charAt( startIndex );
								if( ( c == '/' ) || ( c == '\\' ) ) {
									startIndex += 1;
								}
							}
							int endIndex = path.length();
							if( path.endsWith( ".png" ) ) {
								endIndex -= 4;
							}
							jTextComponent.select( startIndex, endIndex );
						} else {
							jTextComponent.selectAll();
						}
					} else {
						editor.selectAll();
					}
				}
			} );
		}

		public void focusLost( java.awt.event.FocusEvent e ) {
		}
	};

	private final org.lgna.croquet.event.ValueListener<java.awt.Image> imageListener = new org.lgna.croquet.event.ValueListener<java.awt.Image>() {
		public void valueChanged( org.lgna.croquet.event.ValueEvent<java.awt.Image> e ) {
			repaint();
		}
	};

	private final org.lgna.croquet.event.ValueListener<String> pathListener = new org.lgna.croquet.event.ValueListener<String>() {
		public void valueChanged( org.lgna.croquet.event.ValueEvent<String> e ) {
			updatePathLabel( e.getNextValue() );
		}
	};

	private final JImageView jImageView = new JImageView();

	private final edu.cmu.cs.dennisc.javax.swing.JShowLabel jPathLabel = new edu.cmu.cs.dennisc.javax.swing.JShowLabel();

	private final org.lgna.croquet.components.Button saveButton;

	public ImageEditorPane( org.alice.imageeditor.croquet.ImageEditorFrame composite ) {
		super( composite, "", "[][]16[]" );

		this.saveButton = composite.getSaveOperation().createButton();

		javax.swing.JComboBox jComboBox = composite.getJComboBox();
		jComboBox.setRenderer( new org.alice.imageeditor.croquet.views.renderers.FilenameListCellRenderer() );
		jComboBox.setMaximumRowCount( 24 );
		jComboBox.setMinimumSize( new java.awt.Dimension( 0, 0 ) );

		this.getAwtComponent().add( this.jImageView, "align center, spanx 2, spany 2" );

		this.addComponent( composite.getClearOperation().createButton(), "wrap" );
		this.addComponent( composite.getCopyOperation().createButton(), "aligny top, wrap" );

		this.addComponent( composite.getRootDirectoryState().getSidekickLabel().createLabel(), "align right" );
		this.addComponent( composite.getRootDirectoryState().createTextField(), "split 2, growx, shrinkx" );
		this.addComponent( composite.getBrowseOperation().createButton() );
		this.addComponent( this.saveButton, "spany 3, grow, wrap" );

		this.addComponent( new org.lgna.croquet.components.Label( "file:" ), "align right" );
		this.getAwtComponent().add( jComboBox, "wrap" );
		this.getAwtComponent().add( this.jPathLabel, "skip 1" );

		this.jPathLabel.setForeground( java.awt.Color.DARK_GRAY );

		this.updatePathLabel( composite.getPathHolder().getValue() );
	}

	private void updatePathLabel( String nextPath ) {
		if( edu.cmu.cs.dennisc.java.io.FileUtilities.isValidPath( nextPath ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "INVALID PATH:", nextPath );
		}

		this.jPathLabel.setText( nextPath );
		if( nextPath != null ) {
			java.awt.Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
			if( awtEditorComponent instanceof javax.swing.JTextField ) {
				javax.swing.JTextField jTextField = (javax.swing.JTextField)awtEditorComponent;
				boolean isEqual = jTextField.getText().contentEquals( nextPath );
				this.jPathLabel.setShowing( isEqual == false );
			}
		}
	}

	@Override
	public org.alice.imageeditor.croquet.ImageEditorFrame getComposite() {
		return (org.alice.imageeditor.croquet.ImageEditorFrame)super.getComposite();
	}

	public java.awt.Image render() {
		org.alice.imageeditor.croquet.ImageEditorFrame composite = getComposite();
		java.awt.Image image = composite.getImageHolder().getValue();
		if( image != null ) {
			java.awt.image.BufferedImage rv = new java.awt.image.BufferedImage( image.getWidth( this.jImageView ), image.getHeight( this.jImageView ), java.awt.image.BufferedImage.TYPE_INT_BGR );
			java.awt.Graphics g = rv.getGraphics();
			this.jImageView.render( g );
			g.dispose();
			return rv;
		} else {
			return null;
		}
	}

	@Override
	public void handleCompositePreActivation() {
		this.getComposite().getImageHolder().addAndInvokeValueListener( this.imageListener );
		this.getComposite().getPathHolder().addAndInvokeValueListener( this.pathListener );
		java.awt.Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
		awtEditorComponent.addFocusListener( this.comboBoxEditorFocusListener );
		this.getRoot().setDefaultButton( this.saveButton );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				getComposite().getJComboBox().getEditor().getEditorComponent().requestFocusInWindow();
			}
		} );
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		super.handleCompositePostDeactivation();
		java.awt.Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
		awtEditorComponent.removeFocusListener( this.comboBoxEditorFocusListener );
		this.getComposite().getPathHolder().removeValueListener( this.pathListener );
		this.getComposite().getImageHolder().removeValueListener( this.imageListener );
	}
}
