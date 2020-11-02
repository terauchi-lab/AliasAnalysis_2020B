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
        if (ctx?.assignmentOperator()?.text == "=")
            funcs.last().expressions.add(ctx)
        ctx?.unaryExpression()?.postfixExpression()?.primaryExpression()?.Identifier().let { println(it) }
    }

    override fun enterInitDeclaratorList(ctx: ClangParser.InitDeclaratorListContext?) {
        super.enterInitDeclaratorList(ctx)
        ctx?.initDeclarator()?.declarator()?.directDeclarator()?.let { funcs.last().variables.add(it) }
    }

    override fun enterExternalDeclaration(ctx: ClangParser.ExternalDeclarationContext?) {
        super.enterExternalDeclaration(ctx)
        ctx?.functionDefinition()?.declarator()?.directDeclarator()?.directDeclarator()?.text?.let {
            funcs.add(Function(it))
        }
    }
}