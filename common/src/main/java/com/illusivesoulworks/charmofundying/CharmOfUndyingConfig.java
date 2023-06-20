package com.illusivesoulworks.charmofundying;

import com.illusivesoulworks.spectrelib.config.SpectreConfig;
import com.illusivesoulworks.spectrelib.config.SpectreConfigLoader;
import com.illusivesoulworks.spectrelib.config.SpectreConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CharmOfUndyingConfig {
  public static final SpectreConfigSpec SERVER_SPEC;
  public static final Server SERVER;
  private static final String CONFIG_PREFIX = "gui." + CharmOfUndyingConstants.MOD_ID + ".config.";

  static {
    final Pair<Server, SpectreConfigSpec> specPairServer = new SpectreConfigSpec.Builder()
        .configure(Server::new);
    SERVER_SPEC = specPairServer.getRight();
    SERVER = specPairServer.getLeft();
  }

  public static class Server {

    public final SpectreConfigSpec.DoubleValue yOffset;
    public final SpectreConfigSpec.DoubleValue xOffset;
    public final SpectreConfigSpec.BooleanValue renderTotem;

    public Server(SpectreConfigSpec.Builder builder) {
      yOffset = builder.comment("The vertical offset for rendering the totem on the player.")
          .translation(CONFIG_PREFIX + "yOffset")
          .defineInRange("yOffset", 0.0D, -100.0D, 100.0D);

      xOffset = builder.comment("The horizontal offset for rendering the totem on the player.")
          .translation(CONFIG_PREFIX + "xOffset")
          .defineInRange("xOffset", 0.0D, -100.0D, 100.0D);

      renderTotem = builder.comment("If enabled, renders the equipped totem on players.")
          .translation(CONFIG_PREFIX + "renderTotem")
          .define("renderTotem", true);
    }
  }

  public static void setup() {
    SpectreConfigLoader.add(SpectreConfig.Type.SERVER, SERVER_SPEC, CharmOfUndyingConstants.MOD_ID);
  }
}
