package org.alice.ide.declarationseditor.events.components;

import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PopupButton;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserCode;

public class EventListenersView extends org.alice.ide.declarationseditor.code.components.AbstractCodeDeclarationView {
	public EventListenersView( org.alice.ide.declarationseditor.CodeComposite composite ) {
		super( composite, new EventsContentPanel( (org.lgna.project.ast.UserMethod)composite.getDeclaration() ) );
		PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();
		LineAxisPanel bottom = new LineAxisPanel( button );
		this.stickyBottomPanel = new StickyBottomPanel();
		this.stickyBottomPanel.setBottomView( bottom );
		//		this.scrollPane.getAwtComponent().getViewport().addChangeListener( new javax.swing.event.ChangeListener() {
		//			public void stateChanged( javax.swing.event.ChangeEvent e ) {
		//				Object src = e.getSource();
		//				if( src instanceof java.awt.Component ) {
		//					java.awt.Component awtComponent = (java.awt.Component)src;
		//					if( awtComponent.isValid() ) {
		//						//pass
		//					} else {
		//						stickyBottomPanel.revalidateAndRepaint();
		//					}
		//				}
		//			}
		//		} );

		this.stickyBottomPanel.setBackgroundColor( this.getBackgroundColor() );
		this.stickyBottomPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 0, 12, 0 ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 8 ) );
		this.scrollPane.setBackgroundColor( this.getBackgroundColor() );
		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			this.addPageEndComponent( ControlFlowComposite.getInstance( composite.getDeclaration() ).getView() );
		}
	}

	@Override
	protected org.lgna.croquet.views.AwtComponentView<?> getMainComponent() {
		return this.stickyBottomPanel;
	}

	private javax.swing.JScrollBar getJVerticalScrollBar() {
		if( org.alice.ide.croquet.models.ui.preferences.IsJavaCodeOnTheSideState.getInstance().getValue() ) {
			return this.getSideBySideScrollPane().getAwtComponent().getVerticalScrollBar();
		} else {
			return this.scrollPane.getAwtComponent().getVerticalScrollBar();
		}
	}

	private void handleStatementsChanged( boolean isScrollDesired ) {
		this.revalidateAndRepaint();
		if( isScrollDesired ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					javax.swing.JScrollBar verticalScrollBar = getJVerticalScrollBar();
					verticalScrollBar.setValue( verticalScrollBar.getMaximum() );
				}
			} );
		}
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.declarationseditor.CodeComposite codeComposite = (org.alice.ide.declarationseditor.CodeComposite)this.getComposite();
		UserCode userCode = (UserCode)codeComposite.getDeclaration();
		userCode.getBodyProperty().getValue().statements.addListPropertyListener( this.statementsListener );

		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		//
	}

	@Override
	protected void handleUndisplayable() {
		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.removeProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		//

		org.alice.ide.declarationseditor.CodeComposite codeComposite = (org.alice.ide.declarationseditor.CodeComposite)this.getComposite();
		UserCode userCode = (UserCode)codeComposite.getDeclaration();
		userCode.getBodyProperty().getValue().statements.removeListPropertyListener( this.statementsListener );
		super.handleUndisplayable();
	}

	@Override
	protected void setJavaCodeOnTheSide( boolean value, boolean isFirstTime ) {
		super.setJavaCodeOnTheSide( value, isFirstTime );
		org.lgna.croquet.views.SwingComponentView<?> codePanel = this.getCodePanelWithDropReceptor();
		if( value ) {
			this.scrollPane.setViewportView( null );
			this.stickyBottomPanel.setTopView( codePanel );
		} else {
			if( isFirstTime ) {
				//pass
			} else {
				this.stickyBottomPanel.removeComponent( codePanel );
			}
			this.scrollPane.setViewportView( codePanel );
			this.stickyBottomPanel.setTopView( this.scrollPane );
		}
	}

	private final edu.cmu.cs.dennisc.property.event.ListPropertyListener<Statement> statementsListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener<Statement>() {
		@Override
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<Statement> e ) {
		}

		@Override
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<Statement> e ) {
			EventListenersView.this.handleStatementsChanged( true );
		}

		@Override
		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<Statement> e ) {
		}

		@Override
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<Statement> e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}

		@Override
		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<Statement> e ) {
		}

		@Override
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<Statement> e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}

		@Override
		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<Statement> e ) {
		}

		@Override
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<Statement> e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}

	};

	private final org.alice.ide.project.events.ProjectChangeOfInterestListener projectChangeOfInterestListener = new org.alice.ide.project.events.ProjectChangeOfInterestListener() {
		@Override
		public void projectChanged() {
			revalidateAndRepaint();
		}
	};

	private final org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane();
	private final StickyBottomPanel stickyBottomPanel;

}
