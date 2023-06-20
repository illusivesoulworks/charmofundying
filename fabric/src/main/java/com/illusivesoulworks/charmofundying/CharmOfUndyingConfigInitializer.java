package com.illusivesoulworks.charmofundying;

import com.illusivesoulworks.spectrelib.config.SpectreLibInitializer;

public class CharmOfUndyingConfigInitializer implements SpectreLibInitializer {

  @Override
  public void onInitializeConfig() {
    CharmOfUndyingConfig.setup();
  }
}
