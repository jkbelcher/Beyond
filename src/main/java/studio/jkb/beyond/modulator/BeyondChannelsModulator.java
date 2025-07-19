package studio.jkb.beyond.modulator;

import heronarts.glx.ui.UI2dContainer;
import heronarts.glx.ui.component.UISlider;
import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.studio.LXStudio;
import heronarts.lx.studio.ui.modulation.UIModulator;
import heronarts.lx.studio.ui.modulation.UIModulatorControls;
import studio.jkb.beyond.parameter.BeyondCompoundParameter;

@LXCategory("Beyond")
@LXModulator.Global("Beyond Channels")
@LXModulator.Device("Beyond Channels")
public class BeyondChannelsModulator extends LXModulator implements UIModulatorControls<BeyondChannelsModulator> {

  private static final int NUM_CHANNELS = 12;

  private BeyondCompoundParameter[] channels = new BeyondCompoundParameter[NUM_CHANNELS];

  public BeyondChannelsModulator(LX lx) {
    super("Beyond Channels");

    for (int i = 0; i < NUM_CHANNELS; ++i) {
      this.channels[i] = new BeyondCompoundParameter(lx, "Channel " + (i + 1), "/b/Channels/" + (i + 1) + "/Value");
      addParameter("channel" + i, this.channels[i]);
    }
  }

  @Override
  protected double computeValue(double deltaMs) {
    return 0;
  }

  @Override
  public void buildModulatorControls(LXStudio.UI ui, UIModulator uiModulator, BeyondChannelsModulator modulator) {
    uiModulator.setLayout(UI2dContainer.Layout.VERTICAL, 4);

    for (int i = 0; i < NUM_CHANNELS; ++i) {
      new UISlider(UISlider.Direction.HORIZONTAL,100, 20, this.channels[i])
        .addToContainer(uiModulator);
    }
  }

}
