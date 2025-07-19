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
@LXModulator.Global("Beyond Speed")
@LXModulator.Device("Beyond Speed")
public class BeyondSpeedModulator extends LXModulator implements UIModulatorControls<BeyondSpeedModulator> {

  public final BeyondCompoundParameter speed;

  public BeyondSpeedModulator(LX lx) {
    super("Beyond Speed");
    this.speed = new BeyondCompoundParameter(lx, BeyondVariable.SPEED);

    addParameter("speed", this.speed);
  }

  @Override
  protected double computeValue(double deltaMs) {
    return 0;
  }

  @Override
  public void buildModulatorControls(LXStudio.UI ui, UIModulator uiModulator, BeyondSpeedModulator modulator) {
    uiModulator.setLayout(UIModulator.Layout.VERTICAL, 4);
    uiModulator.addChildren(
      new UISlider(UISlider.Direction.HORIZONTAL,100, 20, this.speed)
    );
  }
}
