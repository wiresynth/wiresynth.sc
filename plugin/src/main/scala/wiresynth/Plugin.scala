// Copyright 2025 Jelly Terra <jellyterra@proton.me>
// Use of this source code form is governed under the GNU Lesser General Public License version 2.1

package wiresynth

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Names.termName
import dotty.tools.dotc.core.{Flags, Symbols}
import dotty.tools.dotc.plugins.{PluginPhase, StandardPlugin}

import scala.collection.immutable.List

class Plugin extends StandardPlugin {
  val name        = "wiresynth"
  val description = "WireSynth"

  override def initialize(
      options: List[String]
  )(using Context): List[PluginPhase] = {
    new MarkPhase :: Nil
  }
}

class MarkPhase extends PluginPhase {
  override val phaseName = "Mark"

  override val runsAfter = Set("inlining")

  override def transformValDef(
      tree: tpd.ValDef
  )(using ctx: Context): tpd.Tree = {

    val dataTrait = Symbols.requiredClassRef("wiresynth.dsl.Data").symbol

    if tree.rhs.isEmpty || !tree.rhs.tpe.derivesFrom(dataTrait) then {
      return tree
    }

    val internal = Symbols.newSymbol(
      tree.symbol,
      termName("data"),
      Flags.Local,
      tree.tpe
    )

    val owner = tree.symbol.owner

    if owner.typeRef.derivesFrom(dataTrait) then {
      // val A = {
      //   val data = Data()
      //   this.addChild(data)
      //   data.autoMark("A")
      // }
      cpy.ValDef(tree)(rhs =
        tpd.Block(
          List(
            tpd.ValDef(
              internal.asTerm,
              tpd.Apply(
                tpd.Select(tree.rhs, termName("autoMark")),
                List(tpd.Literal(Constant(tree.name.show)))
              )
            ),
            tpd.Apply(
              tpd.Select(
                tpd.This(owner.asClass),
                termName("addChild")
              ),
              List(tpd.ref(internal))
            )
          ),
          tpd.ref(internal)
        )
      )
    } else {
      // val A = Data().autoMark("A")
      cpy.ValDef(tree)(rhs =
        tpd.Apply(
          tpd.Select(tree.rhs, termName("autoMark")),
          List(tpd.Literal(Constant(tree.name.show)))
        )
      )
    }
  }
}
