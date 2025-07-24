package studio.jkb.beyond.parameter;

import heronarts.lx.LX;
import heronarts.lx.LXLoopTask;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.utils.LXUtils;
import studio.jkb.beyond.BeyondVariable;

public class BeyondCompoundParameter extends CompoundParameter {

  private final LX lx;

  // OSC address in beyond
  private String beyondPath;
  private boolean isValidPath = false;

  // Whether parameter values will be sent to Beyond
  private boolean outputEnabled = true;

  // OSC feedback, not yet implemented
  private boolean feedbackEnabled = false;
  private boolean registered = false;

  // Last value that was sent to Beyond
  private double lastValue = 0;
  private boolean needsUpdate = false;

  private final LXLoopTask checkForUpdates = new LXLoopTask() {
    @Override
    public void loop(double deltaMs) {
      // Don't even query the modulated value if output is disabled
      if (canSend()) {
        // Retrieve *potentially modulated* value, send if changed
        double value = getValue();
        if (lastValue != value || needsUpdate) {
          lastValue = value;
          needsUpdate = false;
          sendOscMessage(beyondPath, (float) value);
        }
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


  private BeyondCompoundParameter setOutputEnabled(boolean outputEnabled) {
    this.outputEnabled = outputEnabled;
    if (outputEnabled) {
      this.needsUpdate = true;
    }
    return this;
  }

  // OSC Feedback

  /**
   * Set whether this parameter should listen for OSC input matching its Beyond path
   */
  private BeyondCompoundParameter setFeedbackEnabled(boolean feedbackEnabled) {
    this.feedbackEnabled = feedbackEnabled;
    updateRegistration();
    return this;
  }

  private void updateRegistration() {
    if (this.feedbackEnabled) {
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

  // Output

  private boolean canSend() {
    return this.isValidPath && this.outputEnabled;
  }

  private void sendOscMessage(String address, float value) {
    lx.engine.osc.sendMessage(address, value);
  }

  public BeyondCompoundParameter resend() {
    this.needsUpdate = true;
    return this;
  }

  @Override
  public void dispose() {
    this.lx.engine.removeLoopTask(this.checkForUpdates);
    super.dispose();
  }
}
