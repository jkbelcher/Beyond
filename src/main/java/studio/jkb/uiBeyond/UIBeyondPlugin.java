package studio.jkb.uiBeyond;

import heronarts.glx.ui.UI;
import heronarts.glx.ui.component.UIButton;
import heronarts.glx.ui.component.UICollapsibleSection;
import studio.jkb.beyond.BeyondPlugin;

public class UIBeyondPlugin extends UICollapsibleSection {

  private static final float VERTICAL_SPACING = 4;

  public UIBeyondPlugin(UI ui, BeyondPlugin plugin, float w) {
    super(ui, 0, 0, w, 0);
    this.setTitle("BEYOND");
    this.setLayout(Layout.VERTICAL, VERTICAL_SPACING);
    this.setPadding(2, 0);

    addChildren(
      new UIButton(getContentWidth(), 16, plugin.setUpNow)
        .setBorderRounding(4)
    );
  }

}
