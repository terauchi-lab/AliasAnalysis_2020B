import clang.ClangParser
import org.antlr.v4.runtime.tree.TerminalNode

class Function(val name: String) {
    val variables = mutableSetOf<TerminalNode>()
    val expressions = mutableListOf<ClangParser.AssignmentExpressionContext>()
    val pointers =
            mutableListOf<Pair<TerminalNode, MutableSet<TerminalNode>>>()

    fun initPointers() {
        variables.forEach {
            pointers.add(Pair(it, mutableSetOf()))
        }
    }

    fun checkExpr() {
        expressions.forEach {
            it.assignmentExpression().conditionalExpression().logicalOrExpression().logicalAndExpression()
                    .inclusiveOrExpression().exclusiveOrExpression().andExpression().equalityExpression()
                    .relationalExpression().shiftExpression().additiveExpression()
                    .multiplicativeExpression().castExpression().unaryExpression().let { c ->
                        when (c.unaryOperator()?.text) {
                            "&" -> {
                                //a=&b;
                                if (it.unaryExpression()?.unaryOperator() == null) {
                                    pointers.find { p ->
                                        p.first.text == it.unaryExpression().postfixExpression().primaryExpression().Identifier().text
                                    }?.second?.add(c.castExpression().unaryExpression().postfixExpression().primaryExpression().Identifier())
                                }
                            }
                            "*" -> {
                                //a=*b;
                                if (it.unaryExpression()?.unaryOperator() == null) {

                                }
                            }
                            null -> {
                                //a=b;
                                if (it.unaryExpression()?.unaryOperator() == null) {

                                }
                                //*a=b;
                                if (it.unaryExpression()?.unaryOperator()?.text == "*") {

                                }
                            }
                        }
                    }
        }
    }

    fun print() {
        println(name)
        expressions.forEach {
            println("\t${it.text}")
        }
        pointers.forEach {
            println("\t${it.first.text}:${it.second}")
        }
    }
}