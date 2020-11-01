import clang.ClangBaseListener
import clang.ClangParser
import org.antlr.v4.runtime.tree.TerminalNode

class WalkListener(val funcs: MutableList<Function>) : ClangBaseListener() {
    override fun visitTerminal(node: TerminalNode?) {
        super.visitTerminal(node)
        println(node?.symbol)
    }

    override fun enterAssignmentExpression(ctx: ClangParser.AssignmentExpressionContext?) {
        super.enterAssignmentExpression(ctx)
        println("assign")
        ctx?.children?.forEach {
            print(it.text)
        }
        println()
    }

    override fun enterInitDeclaratorList(ctx: ClangParser.InitDeclaratorListContext?) {
        super.enterInitDeclaratorList(ctx)
        println("init")
        println(ctx?.children?.map { it.text })
    }

    override fun enterExternalDeclaration(ctx: ClangParser.ExternalDeclarationContext?) {
        super.enterExternalDeclaration(ctx)
        println("external")
        ctx?.functionDefinition()?.declarator()?.directDeclarator()?.directDeclarator()?.text?.let {
            funcs.add(Function(it))
        }
    }
}