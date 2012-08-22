package org.lgna.story.event;

public abstract class CollisionEvent<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> extends AbstractEvent {
	private A collidingFromA;
	private B collidingFromB;

	public CollisionEvent( A collidingFromA, B collidingFromB ) {
		this.collidingFromA = collidingFromA;
		this.collidingFromB = collidingFromB;
	}

	public A getCollidingFromGroupA() {
		return this.collidingFromA;
	}

	public B getCollidingFromGroupB() {
		return this.collidingFromB;
	}
}
