import clang.ClangParser
import org.antlr.v4.runtime.tree.TerminalNode

class Function(val name: String, val args: List<Pair<Boolean, TerminalNode>>) {
    val variables = mutableSetOf<TerminalNode>()
    val expressions = mutableListOf<ClangParser.AssignmentExpressionContext>()
    val calls = mutableListOf<Call>()
    private val pointers = mutableListOf<Pair<TerminalNode, MutableSet<TerminalNode>>>()
    private val edges = mutableListOf<Pair<TerminalNode, MutableSet<TerminalNode>>>()

    fun initPointers() {
        args.filter { it.first }.forEach {
            pointers.add(Pair(it.second, mutableSetOf()))
            edges.add(Pair(it.second, mutableSetOf()))
        }
        variables.reversed().forEach {
            pointers.add(Pair(it, mutableSetOf()))
            edges.add(Pair(it, mutableSetOf()))
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
                                    p.first.text == it.unaryExpression().postfixExpression().primaryExpression()
                                        .Identifier().text
                                }?.second?.add(
                                    c.castExpression().unaryExpression().postfixExpression().primaryExpression()
                                        .Identifier()
                                )
                            }
                        }
                        "*" -> {
                            //a=*b;
                            if (it.unaryExpression()?.unaryOperator() == null) {
                                edges.find { e ->
                                    e.first.text == it.unaryExpression().postfixExpression().primaryExpression()
                                        .Identifier().text
                                }?.second?.addAll(pointers.find { p ->
                                    p.first.text == c.castExpression().unaryExpression().postfixExpression()
                                        .primaryExpression().Identifier().text
                                }?.second ?: emptySet())
                            }
                        }
                        null -> {
                            //a=b;
                            if (it.unaryExpression()?.unaryOperator() == null) {
                                edges.find { e ->
                                    e.first.text == it.unaryExpression().postfixExpression().primaryExpression()
                                        .Identifier().text
                                }?.second?.add(c.postfixExpression().primaryExpression().Identifier())
                            }
                            //*a=b;
                            if (it.unaryExpression()?.unaryOperator()?.text == "*") {
                                pointers.find { p ->
                                    p.first.text == it.unaryExpression().castExpression().unaryExpression()
                                        .postfixExpression().primaryExpression().Identifier().text
                                }?.second?.forEach { t ->
                                    edges.find { e -> e.first.text == t.text }?.second?.add(
                                        c.postfixExpression().primaryExpression().Identifier()
                                    )
                                }
                            }
                        }
                    }
                }
            updatePts()
        }
        updatePts()
    }

    private fun updatePts() {
        edges.forEach {
            it.second.forEach { s ->
                pointers.find { p ->
                    p.first.text == it.first.text
                }?.second?.addAll(pointers.find { p ->
                    p.first.text == s.text
                }?.second ?: emptySet())
            }
        }
    }

    fun print() {
        println("$name ${args.map { (if (it.first) "*" else "") + it.second.text }}")
        expressions.forEach {
            println("\t${it.text}")
        }
        calls.forEach {
            println("\t$it")
        }
        pointers.forEach {
            println("\t${it.first.text}:${it.second}")
        }
    }
}