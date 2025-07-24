package studio.jkb.beyond.parameter;

import heronarts.lx.LX;
import heronarts.lx.LXLoopTask;
import heronarts.lx.color.ColorParameter;
import heronarts.lx.color.LXColor;
import studio.jkb.beyond.BeyondVariable;

/**
 * Wraps a color parameter and sends Hue Shift + Saturation to Beyond.
 */
public class BeyondColorSync implements LXLoopTask {

  private static final float SATURATION_RANGE = 100f;

  private final LX lx;

  private final BeyondCompoundParameter hue;
  private final BeyondCompoundParameter saturation;

  private ColorParameter colorParameter = null;

  public BeyondColorSync(LX lx) {
    this(lx, null);
  }

  public BeyondColorSync(LX lx, ColorParameter colorParameter) {
    this.lx = lx;
    this.colorParameter = colorParameter;

    this.hue = new BeyondCompoundParameter(lx, BeyondVariable.HUE_SHIFT);
    this.saturation = new BeyondCompoundParameter(lx, BeyondVariable.SATURATION);

    this.lx.engine.addLoopTask(this);
  }

  public BeyondColorSync setColorParameter(ColorParameter colorParameter) {
    this.colorParameter = colorParameter;
    return this;
  }

  public ColorParameter getColorParameter() {
    return this.colorParameter;
  }

  public BeyondColorSync setOutputEnabled(boolean outputEnabled) {
    this.hue.setOutputEnabled(outputEnabled);
    this.saturation.setOutputEnabled(outputEnabled);
    return this;
  }

  @Override
  public void loop(double deltaMs) {
    if (this.colorParameter == null) {
      return;
    }

    int color = this.colorParameter.calcColor();

    this.hue.setValue(LXColor.h(color));
    this.saturation.setNormalized(LXColor.s(color) / SATURATION_RANGE);
  }

  public void dispose() {
    this.lx.engine.removeLoopTask(this);
  }
}
