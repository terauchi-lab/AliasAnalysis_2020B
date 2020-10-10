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
}