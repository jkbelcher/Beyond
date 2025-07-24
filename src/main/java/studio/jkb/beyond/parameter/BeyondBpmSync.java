package studio.jkb.beyond.parameter;

import heronarts.lx.LX;
import heronarts.lx.parameter.LXParameterListener;
import studio.jkb.beyond.BeyondVariable;

/**
 * An object that can be created in code to sync Chromatik BPM to Beyond
 */
public class BeyondBpmSync {

  private final LX lx;

  private final BeyondCompoundParameter bpm;

  private final LXParameterListener tempoBpmListener;

  public BeyondBpmSync(LX lx) {
    this.lx = lx;

    this.bpm = new BeyondCompoundParameter(lx, BeyondVariable.BPM);

    this.tempoBpmListener =  (p) -> {
        bpm.setValue(lx.engine.tempo.bpm.getValue());
    };
    lx.engine.tempo.bpm.addListener(this.tempoBpmListener, true);

    // TODO: sync beat alignment
  }

  public BeyondBpmSync setOutputEnabled(boolean outputEnabled) {
    this.bpm.setOutputEnabled(outputEnabled);
    return this;
  }

  public void dispose() {
    this.lx.engine.tempo.bpm.removeListener(this.tempoBpmListener);
  }
}
