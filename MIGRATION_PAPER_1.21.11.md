# Migracion a Paper 1.21.11

## Cambios aplicados

- Se agrego configuracion Gradle con Java 21 y Paper API `1.21.11-R0.1-SNAPSHOT`.
- Se migro el build a `paperweight` (`io.papermc.paperweight.userdev`) para compilar con dev bundle Paper 1.21.11.
- Se agrego `plugin.yml` con `api-version: "1.21"`.
- Se refactorizaron rutas persistentes con `NPCStoragePaths` para evitar strings duplicados.
- Se reemplazo `scheduleSyncDelayedTask` por `runTaskLater`.
- Se normalizo `toLowerCase(Locale.ROOT)` para claves y rutas.
- Se tiparon genericos que estaban raw (`Consumer<Skin>`, `Map<Integer, Integer>`).
- Se hizo el lector de paquetes de interaccion mas tolerante a cambios de nombre (`PacketPlayInUseEntity` y `ServerboundInteractPacket`).
- Se incorporaron los modulos faltantes desde el `PlayerNPC.jar` oficial (decompilados): `PlayerNPCPlugin`, `command`, `nms`, `utils`, y `dev.sergiferry.spigot`.
- Se amplio matriz de versiones soportadas hasta `1.21.11` en `ServerVersion`.

## Refactor y migracion NMS completados

Se corrigieron los bloques principales que rompian en 1.21.11:

- `NPC.java` migrado a metodos mojang-mapped (`setPos`, `setYRot`, `setXRot`, `setPose`, `setGlowingTag`, `setSharedFlagOnFire`).
- Paquetes migrados a versiones actuales:
  - movimiento/rotacion: `ClientboundMoveEntityPacket.*`
  - teleport: `ClientboundTeleportEntityPacket.teleport(...)`
  - tab list: `ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER` + `ClientboundPlayerInfoRemovePacket`
  - equipos/team: `ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(...)`
  - metadata: `ClientboundSetEntityDataPacket(int, List<DataValue<?>>)`
- Scoreboard migrado a API moderna (`getPlayerTeam`, `addPlayerTeam`, `addPlayerToTeam`, `Team.Visibility`, `Team.CollisionRule`).
- Authlib migrado a records actuales (`GameProfile.name()/properties()`, `Property.value()/signature()`).
- Enums NMS migrados (`Pose.STANDING...`, `ChatFormatting.BLACK...`).
- Hologramas migrados a `ArmorStand` moderno (`setCustomNameVisible`, `setNoGravity`, `setInvisible`) y marker por reflexion controlada.
- Wrapper `NMSPacketPlayOutSpawnEntity` actualizado para soportar constructor moderno de `ClientboundAddEntityPacket`.
- Limpieza de tipos genericos decompilados en comandos/utilidades para evitar errores de compilacion (`Object`/raw types -> tipos concretos).

Estado actual:

- `paperweightUserdevSetup`: OK
- `compileJava`: OK

## Verificacion

Comando usado:

```bash
GRADLE_USER_HOME=/tmp/.gradle-playernpc9 GRADLE_OPTS='-Dorg.gradle.native=false' /tmp/gradle-9.0.0/bin/gradle --no-daemon compileJava --console=plain
```

Resultado: `BUILD SUCCESSFUL`.

Nota: `build/assemble` puede fallar en este entorno por una limitacion de red de Gradle (`Could not determine a usable wildcard IP for this machine`), independiente del codigo migrado.
