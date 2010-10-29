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
package edu.cmu.cs.dennisc.toolkit.issue;

class ExceptionPane extends javax.swing.JPanel {
	private Thread thread;
	private Throwable throwable;

	protected Thread getThread() {
		return this.thread;
	}
	protected Throwable getThrowable() {
		return this.throwable;
	}
	public void setThreadAndThrowable( final Thread thread, final Throwable throwable ) {
		assert thread != null;
		assert throwable != null;
		this.thread = thread;
		this.throwable = throwable;
		this.removeAll();
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
		edu.cmu.cs.dennisc.swing.FauxHyperlink vcShowStackTrace = new edu.cmu.cs.dennisc.swing.FauxHyperlink( new javax.swing.AbstractAction( "show complete stack trace..." ) {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				edu.cmu.cs.dennisc.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( ExceptionPane.this, edu.cmu.cs.dennisc.lang.ThrowableUtilities.getStackTraceAsString( throwable ), "Stack Trace", javax.swing.JOptionPane.INFORMATION_MESSAGE );
			}
		} );

		StringBuffer sb = new StringBuffer();
		sb.append( throwable.getClass().getSimpleName() );
		String message = throwable.getLocalizedMessage();
		if( message != null && message.length() > 0 ) {
			sb.append( "[" );
			sb.append( message );
			sb.append( "]" );
		}
		sb.append( " in " );
		sb.append( thread.getClass().getSimpleName() );
		sb.append( "[" );
		sb.append( thread.getName() );
		sb.append( "]" );

		this.add( new javax.swing.JLabel( sb.toString() ) );
		StackTraceElement[] elements = throwable.getStackTrace();
		if( elements.length > 0 ) {
			StackTraceElement e0 = elements[ 0 ];
			this.add( new javax.swing.JLabel( "class: " + e0.getClassName() ) );
			this.add( new javax.swing.JLabel( "method: " + e0.getMethodName() ) );
			this.add( new javax.swing.JLabel( "in file " + e0.getFileName() + " at line number " + e0.getLineNumber() ) );
		}
		this.add( vcShowStackTrace );
	}
}


public abstract class AbstractCaughtExceptionPane extends IssueReportPane {
	private static javax.swing.JLabel createSystemPropertyLabel( String propertyName ) {
		return new javax.swing.JLabel( propertyName + ": " + System.getProperty( propertyName ) );
	}
	class SystemPropertiesPane extends javax.swing.JPanel {
		class ShowAllSystemPropertiesAction extends javax.swing.AbstractAction {
			public ShowAllSystemPropertiesAction() {
				super( "show all system properties..." );
			}
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				edu.cmu.cs.dennisc.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( AbstractCaughtExceptionPane.this, edu.cmu.cs.dennisc.lang.SystemUtilities.getPropertiesAsXMLString(), "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE );
			}
		}

		private edu.cmu.cs.dennisc.swing.FauxHyperlink vcShowAllSystemProperties = new edu.cmu.cs.dennisc.swing.FauxHyperlink( new ShowAllSystemPropertiesAction() );

		public SystemPropertiesPane() {
			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
			for( String propertyName : getSystemPropertiesForEnvironmentField() ) {
				this.add( createSystemPropertyLabel( propertyName ) );
			}
			this.add( this.vcShowAllSystemProperties );
		}
	}

	private javax.swing.JLabel labelException = createLabelForMultiLine( "exception:" );
	private ExceptionPane paneException = new ExceptionPane();
	private java.awt.Component[] rowException = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( labelException, paneException );

	private javax.swing.JLabel labelEnvironment = createLabelForMultiLine( "environment:" );
	private SystemPropertiesPane paneEnvironment = new SystemPropertiesPane();
	private java.awt.Component[] rowEnvironment = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( labelEnvironment, paneEnvironment );

	private static final String NAME_SUGGESTIVE_TEXT = "please fill in your name (optional)";
	private javax.swing.JLabel labelName = createLabelForSingleLine( "reported by:" );
	private SuggestiveTextField textReporterName = new SuggestiveTextField( "", NAME_SUGGESTIVE_TEXT );
	private java.awt.Component[] rowName = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( labelName, textReporterName );

	private static final String EMAIL_SUGGESTIVE_TEXT = "please fill in your e-mail address (optional)";
	private javax.swing.JLabel labelAddress = createLabelForSingleLine( "e-mail address:" );
	private SuggestiveTextField textReporterEMailAddress = new SuggestiveTextField( "", EMAIL_SUGGESTIVE_TEXT );
	private java.awt.Component[] rowAddress = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( labelAddress, textReporterEMailAddress );

	class MyExpandPane extends edu.cmu.cs.dennisc.swing.ExpandPane {
		@Override
		protected javax.swing.JComponent createCenterPane() {
			javax.swing.JPanel rv = new javax.swing.JPanel();
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 0, 0, 0 ) );
			java.util.List< java.awt.Component[] > rows = new java.util.LinkedList< java.awt.Component[] >();
			rows.add( rowSummary );
			rows.add( rowDescription );
			rows.add( rowSteps );
			rows.add( rowException );
			rows.add( rowEnvironment );
			rows.add( rowName );
			rows.add( rowAddress );
			edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( rv, rows, 8, 4 );
			return rv;
		}
		@Override
		protected java.lang.String getCollapsedButtonText() {
			return "yes >>>";
		}
		@Override
		protected java.lang.String getCollapsedLabelText() {
			return "Can you provide insight into this problem?";
		}
		@Override
		protected java.lang.String getExpandedLabelText() {
			return "Please provide insight:";
		}
	}

	private MyExpandPane expandPane = new MyExpandPane();
	
	public AbstractCaughtExceptionPane() {
		expandPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 16 ) );
		this.add( expandPane, java.awt.BorderLayout.CENTER );
	}
	
	@Override
	protected edu.cmu.cs.dennisc.jira.JIRAReport.Type getJIRAType() {
		return edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG;
	}
	@Override
	protected String getSMTPReplyToPersonal() {
		return this.textReporterName.getText();
	}
	@Override
	protected String getSMTPReplyTo() {
		return this.textReporterEMailAddress.getText();
	}
	@Override
	protected String getEnvironmentText() {
		return null;
	}
	@Override
	protected Throwable getThrowable() {
		return this.paneException.getThrowable();
	}

	@Override
	protected int getPreferredWidth() {
		return 540;
	}
	
	@Override
	protected boolean isClearedToSubmit() {
		return true;
	}
	@Override
	protected int getPreferredDescriptionHeight() {
		return 64;
	}
	@Override
	protected int getPreferredStepsHeight() {
		return 64;
	}
	@Override
	protected boolean isSummaryRequired() {
		return false;
	}
	
	public void setThreadAndThrowable( Thread thread, Throwable throwable ) {
		assert this.paneException != null;
		this.paneException.setThreadAndThrowable( thread, throwable );
		this.revalidate();
	}
	
//	@Override
//	protected Issue updateIssue( edu.cmu.cs.dennisc.issue.Issue rv ) {
//		super.updateIssue( rv );
//		rv.setType( Issue.Type.BUG );
//		rv.setThrowable( this.vcExceptionPane.getThrowable() );
//		return rv;
//	}
//
//	
//	@Override
//	protected java.util.ArrayList< java.awt.Component[] > addBugPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
//		assert this.vcExceptionPane == null;
//		this.vcExceptionPane = new ExceptionPane();
//		javax.swing.JLabel exceptionLabel = new javax.swing.JLabel( "exception:", javax.swing.SwingConstants.TRAILING );
//		exceptionLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
//		rv.add( new java.awt.Component[] { exceptionLabel, this.vcExceptionPane } );
//		rv = super.addBugPaneRows( rv );
//		return rv;
//	}
//	protected java.util.ArrayList< java.awt.Component[] > createInsightPaneRows() {
//		return addInsightPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
//	}
//	protected java.util.ArrayList< java.awt.Component[] > createReporterPaneRows() {
//		return addReporterPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
//	}
//	protected java.util.ArrayList< java.awt.Component[] > addReporterPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
//		javax.swing.JLabel nameLabel = new javax.swing.JLabel( "reported by:", javax.swing.SwingConstants.TRAILING );
//		javax.swing.JLabel emailLabel = new javax.swing.JLabel( "e-mail address:", javax.swing.SwingConstants.TRAILING );
//		nameLabel.setToolTipText( NAME_TEXT );
//		emailLabel.setToolTipText( EMAIL_TEXT );
//		rv.add( new java.awt.Component[] { nameLabel, this.vcReporterName } );
//		rv.add( new java.awt.Component[] { emailLabel, this.vcReporterEMailAddress } );
//		return rv;
//	}
//	private String getReporterEMailAddress() {
//		return this.textReporterEMailAddress.getText();
//	}
//	private String getReporterName() {
//		return this.textReporterName.getText();
//	}

	
//	protected String getSummary() {
//		return this.vcSummary.getText();
//	}
//	private String getDescription() {
//		return this.vcDescription.getText();
//	}
//	private String getStepsToReproduce() {
//		return this.vcStepsToReproduce.getText();
//	}

//	protected java.util.ArrayList< java.awt.Component[] > createBugPaneRows() {
//		return addBugPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
//	}
//	protected java.util.ArrayList< java.awt.Component[] > addBugPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
//		javax.swing.JLabel systemLabel = new javax.swing.JLabel( "environment:", javax.swing.SwingConstants.TRAILING );
//		systemLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
//		rv.add( new java.awt.Component[] { systemLabel, this.paneEnvironment } );
//		return rv;
//	}
	
//	protected Issue updateIssue( Issue rv ) {
//		rv.setSummary( this.getSummary() );
//		rv.setDescription( this.getDescription() );
//		rv.setSteps( this.getStepsToReproduce() );
//		for( edu.cmu.cs.dennisc.issue.Attachment attachment : this.createAttachments() ) {
//			rv.addAttachment( attachment );
//		}
//		return rv;
//	}
//
//	private String getSubSummary( Issue issue ) {
//		String summary = issue.getSummary();
//		if( summary != null && summary.length() > 0 ) {
//			return summary;
//		} else {
//			StringBuffer sb = new StringBuffer();
//			//			sb.append( "summary: unspecified; " );
//			Throwable throwable = issue.getThrowable();
//			if( throwable != null ) {
//				//				sb.append( "exception: " );
//				sb.append( throwable.getClass().getName() );
//				sb.append( "; " );
//				String message = throwable.getMessage();
//				if( message != null ) {
//					sb.append( "message: " );
//					sb.append( message );
//					sb.append( "; " );
//				}
//				StackTraceElement[] stackTraceElements = throwable.getStackTrace();
//				if( stackTraceElements != null && stackTraceElements.length > 0 && stackTraceElements[ 0 ] != null ) {
//					sb.append( "stack[0]: " );
//					sb.append( stackTraceElements[ 0 ].toString() );
//					sb.append( "; " );
//				}
//				//			}
//			}
//			return sb.toString();
//		}
//	}
//
//	protected final String getJIRASummary( Issue issue ) {
//		return getSubSummary( issue );
//	}
//	private StringBuffer updateMailSubject( StringBuffer rv, Issue issue ) {
//		rv.append( issue.getAffectsVersionText() );
//		rv.append( ": " );
//		String summary = issue.getSummary();
//		if( summary != null && summary.length() > 0 ) {
//			rv.append( summary );
//		} else {
//			rv.append( this.getSubSummary( issue ) );
//		}
//		return rv;
//	}
//	protected final String getMailSubject( Issue issue ) {
//		StringBuffer sb = new StringBuffer();
//		updateMailSubject( sb, issue );
//		return sb.toString();
//	}
//	protected final String getMailBody( Issue issue ) {
//		return "detailed decription:\n" + issue.getDescription() + "\n\nsteps to reproduce:\n" + issue.getSteps() + "\n\nexception:\n" + issue.getExceptionText();
//	}
	
}
///**
// * @author Dennis Cosgrove
// */
//@Deprecated
//class FocusAdapter implements java.awt.event.FocusListener {
//	private javax.swing.text.JTextComponent textComponent;
//
//	public FocusAdapter( javax.swing.text.JTextComponent textComponent ) {
//		this.textComponent = textComponent;
//	}
//	public void focusGained( java.awt.event.FocusEvent arg0 ) {
//		if( this.textComponent.getText().length() == 0 ) {
//			this.textComponent.repaint();
//		}
//		//this.textComponent.setBackground( new java.awt.Color( 230, 230, 255 ) );
//	}
//	public void focusLost( java.awt.event.FocusEvent arg0 ) {
//		if( this.textComponent.getText().length() == 0 ) {
//			this.textComponent.repaint();
//		}
//		//this.textComponent.setBackground( java.awt.Color.WHITE );
//	}
//}
//
///**
// * @author Dennis Cosgrove
// */
//@Deprecated
//class SuggestiveTextUtilties {
//	public static void drawBlankTextIfNecessary( javax.swing.text.JTextComponent textComponent, java.awt.Graphics g, String textForBlankCondition ) {
//		String text = textComponent.getText();
//		if( text.length() > 0 ) {
//			//pass
//		} else {
//			java.awt.Font font = textComponent.getFont();
//			font = font.deriveFont( java.awt.Font.ITALIC );
//			//int grayscale = 160;
//			int grayscale;
//			if( textComponent.isFocusOwner() ) {
//				grayscale = 120;
//			} else {
//				grayscale = 160;
//				font = font.deriveFont( font.getSize2D() * 0.9f );
//			}
//			g.setFont( font );
//			g.setColor( new java.awt.Color( grayscale, grayscale, grayscale ) );
//			java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( textForBlankCondition, g );
//			int x = (int)(textComponent.getWidth() - bounds.getWidth() - textComponent.getInsets().right - 4 );
//			int y = (int)bounds.getHeight();
//			g.drawString( textForBlankCondition, x, y );
//		}
//	}
//}
//
//class TextComponentBorder extends javax.swing.border.CompoundBorder {
//	public TextComponentBorder() {
//		super( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ), javax.swing.BorderFactory.createEmptyBorder( 1, 3, 1, 3 ) );
//	}
//}
//
///**
// * @author Dennis Cosgrove
// */
//@Deprecated
//class SuggestiveTextField extends javax.swing.JTextField {
//	private String textForBlankCondition;
//
//	public SuggestiveTextField( String text, String textForBlankCondition ) {
//		super( text );
//		this.setBorder( new TextComponentBorder() );
//		this.textForBlankCondition = textForBlankCondition;
//		this.addFocusListener( new FocusAdapter( this ) );
//		//setToolTipText( this.textForBlankCondition );
//	}
//	@Override
//	public java.awt.Dimension getMaximumSize() {
//		java.awt.Dimension rv = super.getMaximumSize();
//		java.awt.Dimension preferred = getPreferredSize();
//		rv.height = preferred.height;
//		return rv;
//	}
//	@Override
//	protected void paintComponent( java.awt.Graphics g ) {
//		super.paintComponent( g );
//		SuggestiveTextUtilties.drawBlankTextIfNecessary( this, g, this.textForBlankCondition );
//	}
//}
//
///**
// * @author Dennis Cosgrove
// */
//@Deprecated
//class SuggestiveTextArea extends javax.swing.JTextArea {
//	private String textForBlankCondition;
//	private int preferredHeight;
//
//	public SuggestiveTextArea( String text, String textForBlankCondition, int preferredHeight ) {
//		super( text );
//		this.preferredHeight = preferredHeight;
//		this.textForBlankCondition = textForBlankCondition;
//		//this.setToolTipText( this.textForBlankCondition );
//		this.setBorder( new TextComponentBorder() );
//		this.addFocusListener( new FocusAdapter( this ) );
//		this.addKeyListener( new java.awt.event.KeyListener() {
//			public void keyPressed( java.awt.event.KeyEvent e ) {
//				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_TAB ) {
//					e.consume();
//					if( e.isShiftDown() ) {
//						java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
//					} else {
//						java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
//					}
//				}
//			}
//			public void keyReleased( java.awt.event.KeyEvent e ) {
//			}
//			public void keyTyped( java.awt.event.KeyEvent e ) {
//			}
//		} );
//	}
//	@Override
//	public boolean isManagingFocus() {
//		return false;
//	}
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumHeight( super.getPreferredSize(), this.preferredHeight );
//	}
//	@Override
//	protected void paintComponent( java.awt.Graphics g ) {
//		super.paintComponent( g );
//		SuggestiveTextUtilties.drawBlankTextIfNecessary( this, g, this.textForBlankCondition );
//	}
//}
//
///**
// * @author Dennis Cosgrove
// */
//public abstract class IssueReportPane extends javax.swing.JPanel {
//	protected abstract IssueReportGenerator getIssueReportGenerator();
//	
//	class SubmitAction extends javax.swing.AbstractAction {
//		public SubmitAction() {
//			super( "submit bug report" );
//		}
//		public void actionPerformed( java.awt.event.ActionEvent e ) {
//			if( IssueReportPane.this.isClearedToSubmit() ) {
//				IssueReportPane.this.isSubmitAttempted = true;
//				IssueReportPane.this.isSubmitSuccessful = IssueReportPane.this.submit();
//				if( IssueReportPane.this.window != null ) {
//					IssueReportPane.this.window.setVisible( false );
//				}
//			}
//		}
//	}
//
//	private javax.swing.JButton vcSubmit = new javax.swing.JButton( new SubmitAction() );
//
//	
//	public Iterable< String > getSystemPropertiesForEnvironmentField() {
//		java.util.List< String > rv = new java.util.LinkedList< String >();
//		rv.add( "java.version" );
//		rv.add( "os.name" );
//		return rv;
//	}
//
//	
//	private java.awt.Component[] createSummaryRow() {
//		return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow();
//	}
//	
//	private static javax.swing.JLabel createSystemPropertyLabel( String propertyName ) {
//		return new javax.swing.JLabel( propertyName + ": " + System.getProperty( propertyName ) );
//	}
//
//	class SystemPropertiesPane extends javax.swing.JPanel {
//		class ShowAllSystemPropertiesAction extends javax.swing.AbstractAction {
//			public ShowAllSystemPropertiesAction() {
//				super( "show all system properties..." );
//			}
//			public void actionPerformed( java.awt.event.ActionEvent e ) {
//				edu.cmu.cs.dennisc.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( IssueReportPane.this, edu.cmu.cs.dennisc.lang.SystemUtilities.getPropertiesAsXMLString(), "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE );
//			}
//		}
//
//		private edu.cmu.cs.dennisc.swing.FauxHyperlink vcShowAllSystemProperties = new edu.cmu.cs.dennisc.swing.FauxHyperlink( new ShowAllSystemPropertiesAction() );
//
//		public SystemPropertiesPane() {
//			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
//			this.add( createSystemPropertyLabel( "java.version" ) );
//			this.add( createSystemPropertyLabel( "os.name" ) );
//			this.add( this.vcShowAllSystemProperties );
//		}
//
//	}
//
//	protected abstract int getPreferredDescriptionHeight();
//	protected abstract int getPreferredStepsHeight();
//	private static final String SUMMARY_TEXT = "please fill in a one line synopsis";
//	private static final String DESCRIPTION_TEXT = "please fill in a detailed description";
//	private static final String STEPS_TEXT = "please fill in the steps required to reproduce the bug";
//	private SuggestiveTextField vcSummary;
//	private SuggestiveTextArea vcDescription = new SuggestiveTextArea( "", DESCRIPTION_TEXT, this.getPreferredDescriptionHeight() );
//	private SuggestiveTextArea vcStepsToReproduce = new SuggestiveTextArea( "", STEPS_TEXT, this.getPreferredStepsHeight() );
//	private SystemPropertiesPane vcSystemPropertiesPane = new SystemPropertiesPane();
//
//	private static final String NAME_TEXT = "please fill in your name (optional)";
//	private static final String EMAIL_TEXT = "please fill in your e-mail address (optional)";
//	private SuggestiveTextField vcReporterName = new SuggestiveTextField( "", NAME_TEXT );
//	private SuggestiveTextField vcReporterEMailAddress = new SuggestiveTextField( "", EMAIL_TEXT );
//
//
//	public IssueReportPane() {
//		//this.vcSubmit.setBackground( java.awt.Color.GREEN.brighter() );
//		java.awt.Font font = this.vcSubmit.getFont();
//		this.vcSubmit.setFont( font.deriveFont( font.getSize2D() * 1.5f ) );
//		this.vcSubmit.setAlignmentX( java.awt.Component.CENTER_ALIGNMENT );
//	}
//
//	private java.awt.Window window;
//
//	public java.awt.Window setWindow() {
//		return this.window;
//	}
//	public void setWindow( java.awt.Window window ) {
//		this.window = window;
//	}
//
//	protected abstract boolean isSummaryRequired();
//	protected java.util.ArrayList< java.awt.Component[] > createInsightPaneRows() {
//		return addInsightPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
//	}
//	
//	protected abstract boolean isStepsPaneDesired();
//	protected java.util.ArrayList< java.awt.Component[] > addInsightPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
//		javax.swing.JLabel summaryLabel = new javax.swing.JLabel( "summary:", javax.swing.SwingConstants.TRAILING );
//		javax.swing.JLabel descriptionLabel = new javax.swing.JLabel( "description:", javax.swing.SwingConstants.TRAILING );
//		
//		String summaryText = SUMMARY_TEXT;
//		if( this.isSummaryRequired() ) {
//			summaryText = summaryText + " (Required)";
//		}
//		this.vcSummary = new SuggestiveTextField( "", summaryText );
//		summaryLabel.setToolTipText( summaryText );
//		descriptionLabel.setToolTipText( DESCRIPTION_TEXT );
//
//		descriptionLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
//
//		
//		javax.swing.JScrollPane scrollDescription = new javax.swing.JScrollPane( this.vcDescription );
//		scrollDescription.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
//
//		rv.add( new java.awt.Component[] { summaryLabel, this.vcSummary } );
//		rv.add( new java.awt.Component[] { descriptionLabel, scrollDescription } );
//		
//		if( this.isStepsPaneDesired() ) {
//			javax.swing.JLabel stepsToReproduceLabel = new javax.swing.JLabel( "steps:", javax.swing.SwingConstants.TRAILING );
//			stepsToReproduceLabel.setToolTipText( STEPS_TEXT );
//			stepsToReproduceLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
//			javax.swing.JScrollPane scrollSteps = new javax.swing.JScrollPane( this.vcStepsToReproduce );
//			scrollSteps.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
//			rv.add( new java.awt.Component[] { stepsToReproduceLabel, scrollSteps } );
//		}
//		return rv;
//	}
//
//	protected java.util.ArrayList< java.awt.Component[] > createBugPaneRows() {
//		return addBugPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
//	}
//	protected java.util.ArrayList< java.awt.Component[] > addBugPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
//		javax.swing.JLabel systemLabel = new javax.swing.JLabel( "environment:", javax.swing.SwingConstants.TRAILING );
//		systemLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
//		rv.add( new java.awt.Component[] { systemLabel, this.vcSystemPropertiesPane } );
//		return rv;
//	}
//
//	protected java.util.ArrayList< java.awt.Component[] > createReporterPaneRows() {
//		return addReporterPaneRows( new java.util.ArrayList< java.awt.Component[] >() );
//	}
//	protected java.util.ArrayList< java.awt.Component[] > addReporterPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
//		javax.swing.JLabel nameLabel = new javax.swing.JLabel( "reported by:", javax.swing.SwingConstants.TRAILING );
//		javax.swing.JLabel emailLabel = new javax.swing.JLabel( "e-mail address:", javax.swing.SwingConstants.TRAILING );
//		nameLabel.setToolTipText( NAME_TEXT );
//		emailLabel.setToolTipText( EMAIL_TEXT );
//		rv.add( new java.awt.Component[] { nameLabel, this.vcReporterName } );
//		rv.add( new java.awt.Component[] { emailLabel, this.vcReporterEMailAddress } );
//		return rv;
//	}
//
//	public javax.swing.JButton getSubmitButton() {
//		return this.vcSubmit;
//	}
//	
//	protected abstract int getPreferredWidth();
//	
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		java.awt.Dimension rv = super.getPreferredSize();
//		//rv.width = Math.max( rv.width, 640 );
//		rv.width = this.getPreferredWidth();
//		return rv;
//	}
//
//	private String getReporterEMailAddress() {
//		return this.vcReporterEMailAddress.getText();
//	}
//	private String getReporterName() {
//		return this.vcReporterName.getText();
//	}
//
//	protected Issue updateIssue( Issue rv ) {
//		rv.setSummary( this.getSummary() );
//		rv.setDescription( this.getDescription() );
//		rv.setSteps( this.getStepsToReproduce() );
//		for( edu.cmu.cs.dennisc.issue.Attachment attachment : this.createAttachments() ) {
//			rv.addAttachment( attachment );
//		}
//		return rv;
//	}
//
//	private String getSubSummary( Issue issue ) {
//		String summary = issue.getSummary();
//		if( summary != null && summary.length() > 0 ) {
//			return summary;
//		} else {
//			StringBuffer sb = new StringBuffer();
//			//			sb.append( "summary: unspecified; " );
//			Throwable throwable = issue.getThrowable();
//			if( throwable != null ) {
//				//				sb.append( "exception: " );
//				sb.append( throwable.getClass().getName() );
//				sb.append( "; " );
//				String message = throwable.getMessage();
//				if( message != null ) {
//					sb.append( "message: " );
//					sb.append( message );
//					sb.append( "; " );
//				}
//				StackTraceElement[] stackTraceElements = throwable.getStackTrace();
//				if( stackTraceElements != null && stackTraceElements.length > 0 && stackTraceElements[ 0 ] != null ) {
//					sb.append( "stack[0]: " );
//					sb.append( stackTraceElements[ 0 ].toString() );
//					sb.append( "; " );
//				}
//				//			}
//			}
//			return sb.toString();
//		}
//	}
//
//	protected final String getJIRASummary( Issue issue ) {
//		return getSubSummary( issue );
//	}
//	private StringBuffer updateMailSubject( StringBuffer rv, Issue issue ) {
//		rv.append( issue.getAffectsVersionText() );
//		rv.append( ": " );
//		String summary = issue.getSummary();
//		if( summary != null && summary.length() > 0 ) {
//			rv.append( summary );
//		} else {
//			rv.append( this.getSubSummary( issue ) );
//		}
//		return rv;
//	}
//	protected final String getMailSubject( Issue issue ) {
//		StringBuffer sb = new StringBuffer();
//		updateMailSubject( sb, issue );
//		return sb.toString();
//	}
//	protected final String getMailBody( Issue issue ) {
//		return "detailed decription:\n" + issue.getDescription() + "\n\nsteps to reproduce:\n" + issue.getSteps() + "\n\nexception:\n" + issue.getExceptionText();
//	}
//	
//	protected String getSummary() {
//		return this.vcSummary.getText();
//	}
//	private String getDescription() {
//		return this.vcDescription.getText();
//	}
//	private String getStepsToReproduce() {
//		return this.vcStepsToReproduce.getText();
//	}
//	protected java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > updateCriticalAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > rv ) {
//		if( this.isInclusionOfCompleteSystemPropertiesDesired() ) {
//			rv.add( new edu.cmu.cs.dennisc.issue.Attachment() {
//				public byte[] getBytes() {
//					return edu.cmu.cs.dennisc.lang.SystemUtilities.getPropertiesAsXMLByteArray();
//				}
//				public String getMIMEType() {
//					return "application/xml";
//				}
//				public String getFileName() {
//					return "systemProperties.xml";
//				}
//			} );
//		}
//		return rv;
//	}
//	protected java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > updateBonusAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > rv ) {
//		return rv;
//	}
//	protected java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > createAttachments() {
//		java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > rv = new java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment >();
//		updateCriticalAttachments( rv );
//		try {
//			java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment > bonus = updateBonusAttachments( new java.util.ArrayList< edu.cmu.cs.dennisc.issue.Attachment >() );
//			rv.addAll( bonus );
//		} catch( Throwable t ) {
//			//todo:
//			t.printStackTrace();
//		}
//		return rv;
//	}
//
//	private boolean isSubmitAttempted = false;
//	private boolean isSubmitSuccessful = false;
//	private boolean isSubmitBackgrounded = false;
//
//	public boolean isSubmitAttempted() {
//		return this.isSubmitAttempted;
//	}
//	public boolean isSubmitBackgrounded() {
//		return this.isSubmitBackgrounded;
//	}
//	public boolean isSubmitSuccessful() {
//		return this.isSubmitSuccessful;
//	}
//	protected abstract boolean isInclusionOfCompleteSystemPropertiesDesired();
//	protected abstract boolean isClearedToSubmit();
//	protected abstract String getProjectKey();
//	
//	protected boolean submit() {
//		Issue issue = this.createIssue();
//		updateIssue( issue );
//		ProgressPane progressPane = new ProgressPane();
//
//		java.net.URL jiraViaRPCURL;
//		try {
//			jiraViaRPCURL = this.getJIRAViaRPCServer();
//		} catch( java.net.MalformedURLException murl ) {
//			throw new RuntimeException( murl );
//		}
//		java.net.URL jiraViaSOAPURL;
//		try {
//			jiraViaSOAPURL = this.getJIRAViaSOAPServer();
//		} catch( java.net.MalformedURLException murl ) {
//			throw new RuntimeException( murl );
//		}
//		progressPane.initializeAndExecuteWorker( issue, this.getProjectKey(), jiraViaRPCURL, jiraViaSOAPURL, this.getJIRAViaRPCAuthenticator(), this.getJIRAViaSOAPAuthenticator(), this.getMailServer(), this.getMailAuthenticator(), this.getReporterEMailAddress(), this.getReporterName(), this.getMailRecipient(), this.isInclusionOfCompleteSystemPropertiesDesired() );
//
//		this.isSubmitBackgrounded = false;
//		javax.swing.JFrame frame = new javax.swing.JFrame();
//		javax.swing.JDialog dialog = new javax.swing.JDialog( frame, "Uploading Bug Report", true );
//		dialog.addWindowListener( new java.awt.event.WindowAdapter() {
//			@Override
//			public void windowClosing( java.awt.event.WindowEvent e ) {
//				IssueReportPane.this.isSubmitBackgrounded = true;
//				e.getComponent().setVisible( false );
//			}
//		} );
//		dialog.getContentPane().add( progressPane );
//		dialog.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
//		dialog.pack();
//		dialog.setVisible( true );
//
//		if( this.isSubmitBackgrounded ) {
//			//pass
//		} else {
//			this.isSubmitBackgrounded = progressPane.isBackgrounded();
//		}
//
//		return progressPane.isSuccessful();
//	}
//}
