import clang.ClangBaseListener
import clang.ClangParser
import org.antlr.v4.runtime.tree.TerminalNode

class WalkListener: ClangBaseListener() {
    override fun visitTerminal(node: TerminalNode?) {
        super.visitTerminal(node)
        println(node?.symbol)
    }

    override fun enterBlockItem(ctx: ClangParser.BlockItemContext?) {
        super.enterBlockItem(ctx)
        println("block")
    }

    override fun enterAssignmentExpression(ctx: ClangParser.AssignmentExpressionContext?) {
        super.enterAssignmentExpression(ctx)
        println("assign")
    }

    override fun exitBlockItem(ctx: ClangParser.BlockItemContext?) {
        super.exitBlockItem(ctx)
        println("exit block")
    }

    override fun exitAssignmentExpression(ctx: ClangParser.AssignmentExpressionContext?) {
        super.exitAssignmentExpression(ctx)
        println("exit assign")
    }

    override fun enterPointer(ctx: ClangParser.PointerContext?) {
        super.enterPointer(ctx)
        println("pointer")
    }

    override fun exitPointer(ctx: ClangParser.PointerContext?) {
        super.exitPointer(ctx)
        println("exit pointer")
    }
}