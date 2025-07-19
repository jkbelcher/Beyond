package studio.jkb.beyond;

public enum BeyondVariable {
  BPM("BPM", "/b/Master/BPM", 120, 1, 600),
  BRIGHTNESS("Brightness", "/b/MasterLC/Brightness", 0, 0, 100),
  HUE_SHIFT("HueShift", "/b/MasterLC/HueShift", 0, 0, 360),
  SATURATION("Saturation", "/b/MasterLC/Saturation", 0, -100, 0),
  SPEED("Speed", "/b/MasterLC/AnimationSpeed", 100, 0, 100)
  ;

  public final String label;
  public final String oscPath;
  public final double defaultValue;
  public final double min;
  public final double max;

  BeyondVariable(String label, String oscPath, double defaultValue, double min, double max) {
    this.label = label;
    this.oscPath = oscPath;
    this.defaultValue = defaultValue;
    this.min = min;
    this.max = max;
  }

  @Override
  public String toString() {
    return this.label;
  }

}
