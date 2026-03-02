# PlayerNPC

PlayerNPC es un plugin y API para Paper que permite crear NPCs tipo jugador (personales y globales) con skin, hologramas de texto, movimiento, seguimiento de mirada y acciones de click.

Este repositorio esta migrado a Paper API `1.21.11`.

## Caracteristicas

- NPCs personales (visibilidad y comportamiento por jugador).
- NPCs globales (compartidos con reglas de visibilidad configurables).
- Soporte de skins (textura/firma de Mojang y cache local).
- Hologramas de texto con alineacion y opacidad por linea.
- Acciones de click (comandos, mensajes, titulos, teleport, bungee connect).
- Comandos de administracion en runtime (`/npclib`, `/npcpersonal`, `/npcglobal`).
- Capa NMS actualizada a mappings modernos de Paper 1.21.11.

## Requisitos

- Java `21`
- Servidor Paper `1.21.11`
- Gradle (este repositorio no incluye wrapper)

## Compilacion

Desde la raiz del proyecto:

```bash
./gradlew paperweightUserdevSetup
./gradlew compileJava
./gradlew build
```

Artefacto esperado del plugin:

- `build/libs/PlayerNPC-2022.7-paper-1.21.11.jar`

## Instalacion

1. Compila el proyecto.
2. Copia el jar generado a `plugins/` en tu servidor.
3. Inicia o reinicia tu servidor Paper `1.21.11`.

## Comandos

- `/npclib` - Configuracion de NPCLib.
- `/npcglobal` - Gestion de atributos de NPC global.
- `/npcpersonal` - Gestion de atributos de NPC personal.

Permiso usado:

- `playernpc.command`

## Inicio Rapido API

Ejemplo de NPC personal:

```java
NPCLib npcLib = PlayerNPCPlugin.getInstance().getNPCLib();

NPC.Personal npc = npcLib.generatePersonalNPC(
        player,
        yourPlugin,
        "demo_npc",
        player.getLocation()
);

npc.setSkin(player, skin -> {
    npc.setText("Hola " + player.getName());
    npc.create();
    npc.show();
});
```

Ejemplo de NPC global:

```java
NPC.Global global = npcLib.generateGlobalNPC(
        yourPlugin,
        "spawn_guide",
        NPC.Global.Visibility.EVERYONE,
        spawnLocation
);

global.setText("Bienvenido al servidor");
global.createAllPlayers();
global.show();
```

## Notas del Proyecto

- Clase principal: `dev.sergiferry.playernpc.PlayerNPCPlugin`
- `plugin.yml` usa `api-version: 1.21`
- Detalle de migracion: `MIGRATION_PAPER_1.21.11.md`
- Javadocs legacy disponibles en `docs/`

## Estado

- `compileJava`: OK con setup de Paper `1.21.11`
- Migracion NMS a nombres mapeados modernos: completada
