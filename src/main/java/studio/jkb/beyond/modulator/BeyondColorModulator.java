package studio.jkb.beyond.modulator;

import heronarts.glx.ui.component.UIDropMenu;
import heronarts.glx.ui.component.UILabel;
import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXDynamicColor;
import heronarts.lx.color.LXPalette;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.studio.LXStudio;
import heronarts.lx.studio.ui.modulation.UIModulator;
import heronarts.lx.studio.ui.modulation.UIModulatorControls;
import studio.jkb.beyond.parameter.BeyondCompoundParameter;
import studio.jkb.beyond.BeyondVariable;

@LXCategory("Beyond")
@LXModulator.Global("Beyond Color")
@LXModulator.Device("Beyond Color")
public class BeyondColorModulator extends LXModulator implements UIModulatorControls<BeyondColorModulator> {

  private static final float SATURATION_RANGE = 100f;

  private final LX lx;

  private final BeyondCompoundParameter hue;
  private final BeyondCompoundParameter saturation;

  public final DiscreteParameter paletteIndex =
    new LXPalette.IndexSelector("Index")
      .setDescription("Target index in the global palette's active swatch");

  public BeyondColorModulator(LX lx) {
    super("Beyond Color");
    this.lx = lx;

    this.hue = new BeyondCompoundParameter(lx, BeyondVariable.HUE_SHIFT);
    this.saturation = new BeyondCompoundParameter(lx, BeyondVariable.SATURATION);

    addParameter("paletteIndex", this.paletteIndex);
  }

  @Override
  protected double computeValue(double deltaMs) {
    final int index = this.paletteIndex.getValuei() - 1;
    final LXDynamicColor color = this.lx.engine.palette.swatch.getColor(index);

    this.hue.setValue(color.getHue());
    this.saturation.setNormalized(color.getSaturation() / SATURATION_RANGE);

    return 0;
  }

  @Override
  public void buildModulatorControls(LXStudio.UI ui, UIModulator uiModulator, BeyondColorModulator modulator) {
    uiModulator.setLayout(UIModulator.Layout.HORIZONTAL, 4);
    uiModulator.addChildren(
      new UILabel(70, "Palette Index:"),
      new UIDropMenu(0, 0, 48, 16, modulator.paletteIndex)
      );
  }
}
