# 🐍 Snake MVC

Videojuego **Snake** en **Java (Swing/AWT)** desarrollado con el patrón **Modelo-Vista-Controlador**.
Proyecto de la **Práctica Final de Diseño de Interfaces de Usuario (DIU) 2024/25** del Grado en Ingeniería Informática de Servicios y Aplicaciones (E.I. Informática de Segovia, Universidad de Valladolid).

## ✨ Características

- **4 modos de juego**
  - **Clásico**: la serpiente crece al comer manzanas; pierdes al chocar.
  - **Infinito**: los bordes te teletransportan al lado opuesto; solo pierdes si te muerdes a ti mismo.
  - **Contrarreloj**: partes con 20 s y cada manzana suma o resta tiempo (la verde resta 2 s).
  - **Multijugador local**: dos serpientes compiten en la misma pantalla.
- **Manzanas especiales**: unas dan puntos extra, la dorada es temporal (desaparece a los 5 s) y otra te hace más pequeña.
- **Ranking** de las 10 mejores puntuaciones por modo, guardado en local.
- **Configuración**: velocidad (lento / moderado / rápido), tamaño del mapa (pequeño / mediano / grande), aspecto de la serpiente (4 skins) y audio (música y efectos por separado).
- **Accesibilidad visual**: modos para daltonismo (protanopia, deuteranopia y tritanopia).
- **Ayuda** accesible desde el menú y **pausa** durante la partida.
- **Música y efectos de sonido** que cambian según la velocidad elegida.

## 🎮 Controles

| Acción | Jugador 1 | Jugador 2 (multijugador) |
|---|---|---|
| Arriba / Abajo / Izquierda / Derecha | ↑ ↓ ← → | W S A D |
| Pausar / reanudar | Espacio | — |

## 🏗️ Arquitectura (MVC)

```
src/
├── Main.java            # Punto de entrada
├── modelo/              # Lógica y estado del juego
│   ├── SnakeModel, InfiniteSnakeModel, TimerSnakeModel, MultiSnakeModel
│   ├── GameConfig, AudioConfig
│   └── RankingManager, RankingEntry
├── vista/               # Interfaz Swing
│   ├── MainMenuPanel, GameModesPanel, ConfigPanel
│   ├── RankingPanel, HelpOverlayPanel
│   └── SnakeView, InfiniteSnakeView, TimerSnakeView, MultiSnakeView
├── controlador/         # Entrada de teclado y bucle de juego
│   ├── SnakeGame, AbstractSnakeController
│   └── SnakeController, MultiSnakeController
└── util/                # Audio
    └── AudioManager, MusicPlayer
img/                     # Recursos gráficos (fondos, iconos, skins)
audio/                   # Música y efectos (.wav)
```

## 🚀 Cómo ejecutarlo

**Requisitos:** JDK 21 o superior (el código usa `switch` con flechas y la configuración del proyecto apunta a JDK 21).

> ⚠️ El juego carga `img/` y `audio/` con rutas relativas: ejecútalo **desde la raíz del proyecto**.

```bash
# Compilar
mkdir -p out
javac -d out $(find src -name "*.java")

# Ejecutar (desde la raíz del repositorio)
java -cp out Main
```

También puedes abrir la carpeta en IntelliJ IDEA, marcar `src` como *Sources Root* y ejecutar `Main`.

## 🔊 Sobre el audio

Los archivos de música son `.wav` sin comprimir y pesan entre 18 y 59 MB cada uno, por lo que **no se incluyen en el repositorio**. El juego funciona sin ellos (si no encuentra un archivo, muestra un aviso por consola y continúa sin sonido). Para activar la música, coloca en `audio/`:

`menu.wav`, `lenta.wav`, `moderada.wav`, `rapidovss.wav`, `eat.wav` y `gameover.wav`.

## 🛠️ Tecnologías

Java 21 · Swing/AWT · `javax.sound.sampled` · Serialización Java para el ranking

## 📄 Contexto académico

Práctica Final de DIU (curso 2024/25), donde se aplica el modelo de desarrollo de IxD: descubrir requisitos, diseñar alternativas, prototipar y evaluar. La memoria incluye personas, prototipos (baja y alta fidelidad), diagramas de clases y de casos de uso, y evaluación de usabilidad.

## 👤 Autoría

Proyecto realizado por **Alejandro** — Universidad de Valladolid, Campus de Segovia.
