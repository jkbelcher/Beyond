package studio.jkb.beyond.modulator;

import heronarts.glx.ui.component.UISlider;
import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.studio.LXStudio;
import heronarts.lx.studio.ui.modulation.UIModulator;
import heronarts.lx.studio.ui.modulation.UIModulatorControls;
import studio.jkb.beyond.parameter.BeyondCompoundParameter;
import studio.jkb.beyond.BeyondVariable;

@LXCategory("Beyond")
@LXModulator.Global("Beyond Brightness")
@LXModulator.Device("Beyond Brightness")
public class BeyondBrightnessModulator extends LXModulator implements UIModulatorControls<BeyondBrightnessModulator> {

  public final BeyondCompoundParameter brightness;

  public BeyondBrightnessModulator(LX lx) {
    super("Beyond Brightness");
    this.brightness = new BeyondCompoundParameter(lx, BeyondVariable.BRIGHTNESS);

    addParameter("brightness", this.brightness);
  }

  @Override
  protected double computeValue(double deltaMs) {
    return 0;
  }

  @Override
  public void buildModulatorControls(LXStudio.UI ui, UIModulator uiModulator, BeyondBrightnessModulator modulator) {
    uiModulator.setLayout(UIModulator.Layout.VERTICAL, 4);
    uiModulator.addChildren(
      new UISlider(UISlider.Direction.HORIZONTAL,100, 20, this.brightness)
    );
  }
}
