package org.alice.stageide.cascade.fillerinners;

public class LeavesViewEventListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public LeavesViewEventListenerFillerInner() {
		super( org.lgna.story.event.LeavesViewEventListener.class );
	}
	@Override
	public java.util.List< org.lgna.croquet.CascadeBlankChild > addItems( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.project.annotations.ValueDetails< ? > details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		rv.add( org.alice.stageide.croquet.models.cascade.adapters.LeavesViewEventListenerFillIn.getInstance() );
		return rv;
	}
}
