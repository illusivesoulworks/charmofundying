package com.illusivesoulworks.charmofundying;

import com.illusivesoulworks.spectrelib.config.SpectreLibInitializer;
import org.quiltmc.loader.api.ModContainer;

public class CharmOfUndyingConfigInitializer implements SpectreLibInitializer {

  @Override
  public void onInitializeConfig(ModContainer modContainer) {
    CharmOfUndyingConfig.setup();
  }
}
