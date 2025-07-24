/**
 * @author Justin Belcher <justin@jkb.studio>
 */

package studio.jkb.beyond;

import heronarts.lx.LX;
import heronarts.lx.LXPlugin;
import heronarts.lx.modulation.LXModulationEngine;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.osc.LXOscConnection;
import heronarts.lx.parameter.TriggerParameter;
import heronarts.lx.studio.LXStudio;
import heronarts.lx.utils.LXUtils;
import studio.jkb.beyond.modulator.BeyondBpmModulator;
import studio.jkb.beyond.modulator.BeyondBrightnessModulator;
import studio.jkb.beyond.modulator.BeyondChannelsModulator;
import studio.jkb.beyond.modulator.BeyondColorModulator;
import studio.jkb.beyond.modulator.BeyondSpeedModulator;
import studio.jkb.uiBeyond.UIBeyondPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Plugin for Chromatik that connects to Pangolin Beyond
 */
@LXPlugin.Name("Beyond")
public class BeyondPlugin implements LXStudio.Plugin {

  private static final int BEYOND_OSC_PORT = 8000;
  private static final String BEYOND_OSC_FILTER = "/b";

  public final TriggerParameter setUpNow =
    new TriggerParameter("Set Up Now", this::runSetup)
      .setDescription("Add an OSC output for BEYOND and add global modulators for brightness and color sync");

  private final LX lx;

  public BeyondPlugin(LX lx) {
    this.lx = lx;
    LOG.log("BeyondPlugin(LX) version: " + loadVersion());
  }

  @Override
  public void initialize(LX lx) {

  }

  @Override
  public void initializeUI(LXStudio lx, LXStudio.UI ui) {

  }

  @Override
  public void onUIReady(LXStudio lx, LXStudio.UI ui) {
    new UIBeyondPlugin(ui, this, ui.leftPane.content.getContentWidth())
      .addToContainer(ui.leftPane.content, 2);
  }

  // Special for custom builds: register anything that would have been auto-imported from LXPackage.

  /**
   * Projects that import this library and are not an LXPackage (such as a custom build)
   * should call this method from their initialize().
   */
  public static void registerComponents(LX lx) {
    lx.registry.addModulator(BeyondBpmModulator.class);
    lx.registry.addModulator(BeyondBrightnessModulator.class);
    lx.registry.addModulator(BeyondChannelsModulator.class);
    lx.registry.addModulator(BeyondColorModulator.class);
    lx.registry.addModulator(BeyondSpeedModulator.class);
  }

  /**
   * Projects that import this library and are not an LXPackage (such as a custom build)
   * should call this method from their initializeUI().
   */
  public static void registerUIComponents(LXStudio lx, LXStudio.UI ui) { }

  /**
   * Add the basic common items: OSC output, global brightness modulator, global color modulator.
   */
  private void runSetup() {
    confirmOscOutput(this.lx);
    addBrightnessModulator();
    addColorModulator();
  }

  public static LXOscConnection.Output confirmOscOutput(LX lx) {
    return confirmOscOutput(lx, null, BEYOND_OSC_PORT);
  }

  public static LXOscConnection.Output confirmOscOutput(LX lx, String host, int port) {
    for (LXOscConnection.Output output : lx.engine.osc.outputs) {
      if (output.hasFilter.isOn() && BEYOND_OSC_FILTER.equals(output.filter.getString())) {
        return output;
      }
    }

    LXOscConnection.Output oscOutput = lx.engine.osc.addOutput();
    if (!LXUtils.isEmpty(host)) {
      oscOutput.host.setValue(host);
    }
    oscOutput.port.setValue(port);
    oscOutput.filter.setValue(BEYOND_OSC_FILTER);
    oscOutput.hasFilter.setValue(true);
    try {
      oscOutput.active.setValue(true);
    } catch (Exception e) {
      LOG.error(e, "Failed to activate OSC output for BEYOND. Set the correct IP and port.");
      return null;
    }
    return oscOutput;
  }

  public BeyondBrightnessModulator addBrightnessModulator() {
    return addBrightnessModulator(this.lx.engine.modulation);
  }

  public BeyondBrightnessModulator addBrightnessModulator(LXModulationEngine modulationEngine) {
    LXModulator brightnessModulator = findModulator(BeyondBrightnessModulator.class);
    if (brightnessModulator != null) {
      // If modulator already exists, make sure it is running
      if (!brightnessModulator.isRunning()) {
        brightnessModulator.start();
      }
    } else {
      // Create new global modulator
      brightnessModulator = new BeyondBrightnessModulator(this.lx);
      this.lx.engine.modulation.startModulator(brightnessModulator);
    }

    return (BeyondBrightnessModulator) brightnessModulator;
  }

  public BeyondColorModulator addColorModulator() {
    return addColorModulator(this.lx.engine.modulation);
  }

  public BeyondColorModulator addColorModulator(LXModulationEngine modulationEngine) {
    LXModulator colorModulator = findModulator(BeyondColorModulator.class);
    if (colorModulator != null) {
      if (!colorModulator.isRunning()) {
        colorModulator.start();
      }
    } else {
      colorModulator = new BeyondColorModulator(this.lx);
      this.lx.engine.modulation.startModulator(colorModulator);
    }
    return (BeyondColorModulator) colorModulator;
  }

  private LXModulator findModulator(Class<? extends LXModulator> clazz) {
    for (LXModulator modulator : this.lx.engine.modulation.getModulators()) {
      if (clazz.isInstance(modulator)) {
        return modulator;
      }
    }
    return null;
  }

  /**
   * Loads 'beyond.properties', after maven resource filtering has been applied. Note that you
   * may need to run `mvn clean package` once to generate the templated properties file.
   * To verify: `cat target/classes/beyond.properties`.
   */
  private String loadVersion() {
    String version = "";
    Properties properties = new Properties();
    try (InputStream inputStream =
           getClass().getClassLoader().getResourceAsStream("beyond.properties")) {
      properties.load(inputStream);
      version = properties.getProperty("beyond.version");
    } catch (IOException e) {
      LOG.error("Failed to load version information " + e);
    }
    return version;
  }
}
