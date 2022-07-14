/*
 * Copyright (C) 2019-2022 Illusive Soulworks
 *
 * Charm of Undying is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Charm of Undying is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Charm of Undying.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.charmofundying.common;

import com.google.common.collect.ImmutableSet;
import com.illusivesoulworks.charmofundying.common.integration.BMEnchantedTotemEffectProvider;
import com.illusivesoulworks.charmofundying.platform.Services;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.world.item.Item;

public class TotemProviders {

  public static Predicate<Item> IS_TOTEM = new Predicate<>() {
    @Override
    public boolean test(Item item) {
      return EFFECT_PROVIDERS.containsKey(Services.PLATFORM.getRegistryName(item));
    }
  };

  private static final Map<String, ITotemEffectProvider> EFFECT_PROVIDERS = new HashMap<>();

  public static void init() {
    EFFECT_PROVIDERS.put("minecraft:totem_of_undying", new VanillaTotemEffectProvider());

    if (Services.PLATFORM.isModLoaded("biomemakeover")) {
      EFFECT_PROVIDERS.put("biomemakeover:enchanted_totem", new BMEnchantedTotemEffectProvider());
    }
  }

  public static Set<String> getItems() {
    return ImmutableSet.copyOf(EFFECT_PROVIDERS.keySet());
  }

  public static Optional<ITotemEffectProvider> getEffectProvider(final Item item) {
    return Optional.ofNullable(EFFECT_PROVIDERS.get(Services.PLATFORM.getRegistryName(item)));
  }
}
