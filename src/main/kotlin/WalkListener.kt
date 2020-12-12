import clang.ClangBaseListener
import clang.ClangParser
import org.antlr.v4.runtime.tree.TerminalNode

class WalkListener(private val funcs: MutableList<Function>) : ClangBaseListener() {
    override fun enterAssignmentExpression(ctx: ClangParser.AssignmentExpressionContext?) {
        super.enterAssignmentExpression(ctx)
        if (ctx?.assignmentOperator()?.text == "=")
            funcs.last().expressions.add(ctx)
    }

    override fun enterInitDeclaratorList(ctx: ClangParser.InitDeclaratorListContext?) {
        super.enterInitDeclaratorList(ctx)
        ctx?.initDeclarator()?.declarator()?.directDeclarator()?.Identifier()?.let { funcs.last().variables.add(it) }
    }

    override fun enterExternalDeclaration(ctx: ClangParser.ExternalDeclarationContext?) {
        super.enterExternalDeclaration(ctx)
        ctx?.functionDefinition()?.declarator()?.directDeclarator()?.directDeclarator()?.Identifier()?.let {
            val list = mutableListOf<Pair<Boolean, TerminalNode>>()
            var arg = ctx.functionDefinition()?.declarator()?.directDeclarator()?.parameterTypeList()?.parameterList()
            while (arg != null) {
                list.add(
                    arg.parameterDeclaration().run {
                        Pair(
                            declarationSpecifiers().declarationSpecifier().first().typeSpecifier()
                                .pointer().text == "*",
                            declarator().directDeclarator().Identifier()
                        )
                    })
                arg = arg.parameterList()
            }
            funcs.add(Function(it.text, list.reversed()))
        }
    }

    override fun enterPostfixExpression(ctx: ClangParser.PostfixExpressionContext?) {
        super.enterPostfixExpression(ctx)
        ctx?.postfixExpression()?.primaryExpression()?.Identifier()?.let {
            val list = mutableListOf<ClangParser.AssignmentExpressionContext>()
            var arg = ctx.argumentExpressionList()
            while (arg != null) {
                list.add(arg.assignmentExpression())
                arg = arg.argumentExpressionList()
            }
            funcs.last().calls.add(Call(it.text, list.reversed()))
        }
    }
}