import clang.ClangParser
import org.antlr.v4.runtime.tree.TerminalNode

class Function(val name: String) {
    val variables = mutableSetOf<TerminalNode>()
    val expressions = mutableListOf<ClangParser.AssignmentExpressionContext>()
    val pointers =
            mutableListOf<Pair<TerminalNode, MutableSet<TerminalNode>>>()

    fun initPointers() {
        variables.reversed().forEach {
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
                                    pointers.find { p ->
                                        p.first.text == it.unaryExpression().postfixExpression().primaryExpression().Identifier().text
                                    }?.second?.addAll(pointers.find { p ->
                                        p.first.text == c.castExpression().unaryExpression().postfixExpression().primaryExpression().Identifier().text
                                    }?.second?.run {
                                        val set = mutableSetOf<TerminalNode>()
                                        forEach { t ->
                                            set.addAll(pointers.find { p -> p.first.text == t.text }?.second
                                                    ?: emptySet())
                                        }
                                        set
                                    } ?: emptySet())
                                }
                            }
                            null -> {
                                //a=b;
                                if (it.unaryExpression()?.unaryOperator() == null) {
                                    pointers.find { p ->
                                        p.first.text == it.unaryExpression().postfixExpression().primaryExpression().Identifier().text
                                    }?.second?.addAll(pointers.find { p ->
                                        p.first.text == c.postfixExpression().primaryExpression().Identifier().text
                                    }?.second ?: emptySet())
                                }
                                //*a=b;
                                if (it.unaryExpression()?.unaryOperator()?.text == "*") {
                                    val bpts = pointers.find { p ->
                                        p.first.text == c.postfixExpression().primaryExpression().Identifier().text
                                    }?.second ?: emptySet()
                                    pointers.find { p ->
                                        p.first.text == it.unaryExpression().castExpression().unaryExpression().postfixExpression().primaryExpression().Identifier().text
                                    }?.second?.forEach { t ->
                                        pointers.find { p -> p.first.text == t.text }?.second?.addAll(bpts)
                                    }
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