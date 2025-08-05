# WireSynth.sc

WireSynth.sc is DSL in Scala for describing components and wiring in circuit board design, which inspired by SpinalHDL.
It generates the final netlist for EDAs from presentation in Scala.

WireSynth.sc aims to reduce the time cost of schematic design.

The major expense in circuit board design is in simulation, layout and signal integrity, but not schematic.

Spending more time on PCB layout instead of drawing and dragging symbols in schematic editor.

It's recommended to use fonts with ligatures.

## Project structure

WireSynth.sc is still in active development and features are **unstable**.

| Path        | Description                                              |
|-------------|----------------------------------------------------------|
| `core`      | DSL implementation.                                      |
| `plugin`    | Scala 3 (Dotty) compiler plugin for marking DSL objects. |
| `generator` | EDA-specified netlist generator.                         |

Building DSL on a host language with full LSP support is more effective than launching another new language.

This is why I chose Scala.

## Setup

### sbt

Use WireSynth.sc in your Scala project.
You have to add dependencies including a compiler plugin.

`lib-kicad` contains parts and footprints authored by KiCad.
They are transformed into Scala source code.

```scala
libraryDependencies ++= Seq(
  "io.github.wiresynth.sc" %% "core" % coreVersion,
  "io.github.wiresynth.sc" %% "generator" % coreVersion,
  "io.github.wiresynth.sc" %% "lib-kicad" % "0.9.1",
  compilerPlugin("io.github.wiresynth.sc" %% "plugin" % coreVersion)
)
```

## Overview

#### Supported EDAs

- [x] KiCad

### Component

`Component` is intermediary as virtual as module for design reusing.
It helps connect wires between different parts and nets.

```scala
case class TopLevel() extends Component:
  val Controller = STM32C011Jx()

  val DC3v3 = BarrelJack()

  DC3v3.P <> Controller.Vdd
  DC3v3.G <> Controller.Vss
```

### Bundle

`Bundle` is derived from `Component` but supports `<>` operator.
It can only accommodate pins.

```scala
case class Differential() extends Bundle:
  val P = Pin()
  val N = Pin()

case class TopLevel() extends Component:
  val A = Differential()
  val B = Differential()
  A <> B
```

### Part

`Part` is derived from `Component` but it's marked as physical part.
Any alive part and pin will be passed to final stage.

```scala
case class STM32C011Jx() extends Part:
  this packageIn kicad.Package_SO.SOIC_8_3p9x4p9mm_P1p27mm

  val Vdd = Pin() @@ 2
  val Gnd = Pin() @@ 3

  val PA0_PA1_PA2_PF2 = Pin() @@ 4
  val PA8_PA9_PA11 = Pin() @@ 5
  val PA10_PA12 = Pin() @@ 6
  val PA13 = Pin() @@ 7
  val PA14_PB6_PC15 = Pin() @@ 8

  val PB7_PC14 = Pin() @@ 1
```

### Pin <> Pin

```scala
// Net: { A, B, C, D }
A <> B <> C <> D

// Net: { A, B } ∪ { B, C, D }
A <> B
B <> C <> D
```

### Bundle <> Bundle

```scala
case class Differential() extends Bundle:
  val P = Pin()
  val N = Pin()

case class TopLevel() extends Component:
  val A = Differential()
  val B = Differential()
  A <> B
```

### Pin <> Neutral <> Pin

```scala
// Resistor
Master.P <> R(10 Ohm) <> Slave.P
Master.N <> R(10 Ohm) <> Slave.N

// Inductor
A <> L(4.7 uH) <> B

// Capacitor
P3v3 <> C(100 pF) <> Gnd
// Equals
val cap = C(100 pF)
cap.A <> P3v3
cap.B <> Gnd
```

### Pin -|+ Polarized |- Pin

```scala
// Capacitor
P12v -|+ CP(47 uF) |- Gnd
// Equals
val cap = CP(47 uF)
// -|+
cap.P <> P12v
// |-
cap.N <> Gnd

// Diode
P3v3 <> R(12 Ohm) -|+ LED |- Gnd
```

## Elaboration

After finishing designing the top level component, run elaboration and choose the target EDA for generating netlist.

```scala
Kicad(Elaboration(TopLevel()), "netlist.net")
```

Final netlist `netlist.net` will be generated in the working directory.
Import it in *KiCad Pcbnew* and lay it out.

## To do

### Refinement

Check numeric range violation.

### Schematic

Draw schematic for single component.

## Not planned

### Pin direction

This feature is quite useless because the hierarchy of PCB is vague, and wires and nets in PCB design are not as strict as the digital circuits described in HDL.

In contrast to the clear hierarchy of SystemVerilog designs, copper board wiring does not necessarily follow a modular design.

## Security

DSL introduce programmability but also bring security risks from attackers.
Be sure to conduct a security audit for third-party code.

## License

Copyright 2025 Jelly Terra <jellyterra@proton.me>

Use of the source code is governed under the GNU Lesser General Public License version 2.1

Please share your improvements.
