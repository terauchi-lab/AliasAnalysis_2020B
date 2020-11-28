import clang.ClangBaseListener
import clang.ClangParser

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
        ctx?.functionDefinition()?.declarator()?.directDeclarator()?.directDeclarator()?.text?.let {
            funcs.add(Function(it))
        }
    }
}