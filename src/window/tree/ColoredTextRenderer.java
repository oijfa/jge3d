package window.tree;

import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.TableBase;
import de.matthiasmann.twl.utils.TintAnimator;
import de.matthiasmann.twl.utils.TintAnimator.TimeSource;

public class ColoredTextRenderer extends  TableBase.StringCellRenderer {
	String value;
	 @Override
    public void setCellData(int row, int column, Object data) {
		Color c = new Color((byte) 0xFFFF,(byte) 0x0,(byte) 0x0,(byte) 0x0);
        super.setCellData(row, column, data);
        super.setTintAnimator(new TintAnimator(new TimeSource() {
			@Override
			public void resetTime() {}
			
			@Override
			public int getTime() {return 0;}
		}, c));
        this.getTintAnimator().setColor(new Color((byte) 0xFFFF,(byte) 0x0,(byte) 0x0,(byte) 0xFFFF));
    }
	
	public ColoredTextRenderer() {
		super();
	}
	
	
}
