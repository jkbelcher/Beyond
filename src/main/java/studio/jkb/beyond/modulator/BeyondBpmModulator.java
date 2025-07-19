package studio.jkb.beyond.modulator;

import heronarts.glx.ui.component.UISlider;
import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.LXParameterListener;
import heronarts.lx.studio.LXStudio;
import heronarts.lx.studio.ui.modulation.UIModulator;
import heronarts.lx.studio.ui.modulation.UIModulatorControls;
import studio.jkb.beyond.BeyondVariable;
import studio.jkb.beyond.parameter.BeyondCompoundParameter;

@LXCategory("Beyond")
@LXModulator.Global("Beyond BPM")
@LXModulator.Device("Beyond BPM")
public class BeyondBpmModulator extends LXModulator implements UIModulatorControls<BeyondBpmModulator> {

  private final BeyondCompoundParameter bpm;

  private final LXParameterListener tempoBpmListener;

  private final LX lx;

  public BeyondBpmModulator(LX lx) {
    super("Beyond BPM");
    this.lx = lx;

    this.bpm = new BeyondCompoundParameter(lx, BeyondVariable.BPM);

    this.tempoBpmListener =  (p) -> {
      if (this.isRunning()) {
        bpm.setValue(lx.engine.tempo.bpm.getValue());
      }
    };
    lx.engine.tempo.bpm.addListener(this.tempoBpmListener, true);

    // TODO: sync beat alignment
  }


  @Override
  protected void onStart() {
    super.onStart();
    double newValue = this.lx.engine.tempo.bpm.getValue();
    if (this.bpm.getValue() != newValue) {
      this.bpm.setValue(newValue);
    } else {
      this.bpm.bang(); // TODO: force BeyondCompoundParameter to send on ParameterChanged from bang
    }
  }

  @Override
  protected double computeValue(double deltaMs) {
    return 0;
  }

  @Override
  public void buildModulatorControls(LXStudio.UI ui, UIModulator uiModulator, BeyondBpmModulator modulator) {

  }

  @Override
  public void dispose() {
    this.lx.engine.tempo.bpm.removeListener(this.tempoBpmListener);
    super.dispose();
  }
}
