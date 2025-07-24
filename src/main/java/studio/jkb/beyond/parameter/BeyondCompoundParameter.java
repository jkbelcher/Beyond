package studio.jkb.beyond.parameter;

import heronarts.lx.LX;
import heronarts.lx.LXLoopTask;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.utils.LXUtils;
import studio.jkb.beyond.BeyondVariable;

public class BeyondCompoundParameter extends CompoundParameter {

  private final LX lx;

  private String beyondPath;
  private boolean isValidPath = false;
  private boolean listen = true;
  private boolean registered = false;

  private double lastValue = 0;

  private final LXLoopTask checkForUpdates = new LXLoopTask() {
    @Override
    public void loop(double deltaMs) {
      // Retrieve *potentially modulated* value, send if changed
      double value = getValue();
      if (lastValue != value) {
        lastValue = value;
        sendOscMessage(beyondPath, (float) value);
      }
    }
  };

  public BeyondCompoundParameter(LX lx, String label, String beyondPath) {
    this(lx, label, beyondPath, 0, 0, 1);
  }

  public BeyondCompoundParameter(LX lx, BeyondVariable v) {
    this(lx, v.label, v.oscPath, v.defaultValue, v.min, v.max);
  }

  public BeyondCompoundParameter(LX lx, BeyondVariable v, String label) {
    this(lx, label, v.oscPath, v.defaultValue, v.min, v.max);
  }

  public BeyondCompoundParameter(LX lx, String label, String beyondPath, double value, double v0, double v1) {
    super(label, value, v0, v1);
    this.lx = lx;

    setBeyondPath(beyondPath);
    updateRegistration();

    lx.engine.addLoopTask(this.checkForUpdates);
  }


  public final String getBeyondPath() {
    return this.beyondPath;
  }

  public BeyondCompoundParameter setBeyondPath(String beyondPath) {
    this.beyondPath = beyondPath;
    this.isValidPath = !LXUtils.isEmpty(beyondPath);
    return this;
  }

  /**
   * Set whether this parameter should listen for OSC input matching its Beyond path
   */
  private BeyondCompoundParameter setListen(boolean listen) {
    this.listen = listen;
    updateRegistration();
    return this;
  }

  private void updateRegistration() {
    if (this.listen) {
      if (!this.registered) {
        register();
      }
    } else {
      if (this.registered) {
        unregister();
      }
    }
  }

  private void register() {
    this.registered = true;
    // TODO: listen for OSC input
  }

  private void unregister() {
    this.registered = false;
    // TODO
  }

  private boolean canSend() {
    return
      this.isValidPath;
      // && (this.lx != null)
      // && (this.lx.engine != null)
      // && (this.lx.engine.osc != null);
  }


  public void sendOscMessage(String address, float value) {
    if (canSend()) {
      lx.engine.osc.sendMessage(address, value);
    }
  }

  public void sendOscMessage(String address, int value) {
    if (canSend()) {
      lx.engine.osc.sendMessage(address, value);
    }
  }

  @Override
  public void dispose() {
    this.lx.engine.removeLoopTask(this.checkForUpdates);
    super.dispose();
  }
}
