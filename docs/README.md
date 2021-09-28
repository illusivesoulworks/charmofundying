# Curio of Undying

Curio of Undying is a mod that uses the Curios API to add a new slot, the Charm slot, to the player inventory and allows
the Totem of Undying to be placed into this slot, either directly or through using the totem in-hand.
## Features

![](https://i.ibb.co/qmyCPfp/undying-screenshot.png)

While the totem is in this slot, the player will be granted the same death protection effect as if it was held in the
player's hand. This allows the player to gain the benefits of the totem without the hassle of needing to remember to
hold the totem or the need to sacrifice a useful hotbar/inventory slot to keep one around.

## Downloads

**CurseForge**
- [![](http://cf.way2muchnoise.eu/short_curio-of-undying_downloads%20on%20Forge.svg)](https://www.curseforge.com/minecraft/mc-mods/curio-of-undying/files) [![](http://cf.way2muchnoise.eu/versions/curio-of-undying.svg)](https://www.curseforge.com/minecraft/mc-mods/curio-of-undying)
- [![](http://cf.way2muchnoise.eu/short_trinket-of-undying-fabric_downloads%20on%20Fabric.svg)](https://www.curseforge.com/minecraft/mc-mods/trinket-of-undying-fabric/files) [![](http://cf.way2muchnoise.eu/versions/trinket-of-undying-fabric.svg)](https://www.curseforge.com/minecraft/mc-mods/trinket-of-undying-fabric)

## Developing

**Help! I'm getting Mixin crashes when I try to launch in development on Forge!**

Curio of Undying uses Mixins to implement its core features. This may cause issues when depending on Curio of Undying
for your project inside a development environment since ForgeGradle/MixinGradle do not yet support this natively like on
the Fabric toolchain. As a workaround, please disable the refmaps in development by setting the `mixin.env.disableRefMap`
JVM argument to `true` in your run configuration.

## Support

Please report all bugs, issues, and feature requests to the
[issue tracker](https://github.com/TheIllusiveC4/CurioOfUndying/issues).

For non-technical support and questions, join the developer's [Discord](https://discord.gg/JWgrdwt).

## License

All source code and assets are licensed under LGPL-3.0-or-later.

## Donations

Donations to the developer can be sent through [Ko-fi](https://ko-fi.com/C0C1NL4O).

## Affiliates

[![BisectHosting](https://i.ibb.co/1G4QPdc/bh-illusive.png)](https://bisecthosting.com/illusive)
