package autotutorial.ecard.menu;

public class PictureEffectsMenuModel extends edu.cmu.cs.dennisc.croquet.PredeterminedMenuModel {
	private static class SingletonHolder {
		private static PictureEffectsMenuModel instance = new PictureEffectsMenuModel();
	}
	public static PictureEffectsMenuModel getInstance() {
		return SingletonHolder.instance;
	}
	private PictureEffectsMenuModel() {
		super( java.util.UUID.fromString( "c7d44cca-5fa8-48a6-8904-20c0ee16b38b" ), 
				DropShadowModel.getInstance(),
				edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR,
				RoundedCornersModel.getInstance(),
				autotutorial.ecard.ThoughtBubbleEffectModel.getInstance(),
				EllipseModel.getInstance(),
				edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR,
				RotateForwardModel.getInstance(),
				RotateBackwardModel.getInstance()
		);
	}
}