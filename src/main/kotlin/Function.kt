import clang.ClangParser
import org.antlr.v4.runtime.tree.TerminalNode

class Function(private val name: String, private val args: List<Pair<Boolean, TerminalNode>>) {
    val variables = mutableSetOf<TerminalNode>()
    val expressions = mutableListOf<ClangParser.AssignmentExpressionContext>()
    val calls = mutableListOf<Call>()
    private val pointers = mutableListOf<Pair<TerminalNode, MutableSet<String>>>()
    private val edges = mutableListOf<Pair<TerminalNode, MutableSet<String>>>()
    private val callEdges = mutableListOf<Pair<TerminalNode, MutableSet<Pair<String, String>>>>()

    fun initPointers() {
        args.filter { it.first }.forEach {
            pointers.add(Pair(it.second, mutableSetOf()))
            edges.add(Pair(it.second, mutableSetOf()))
            callEdges.add(Pair(it.second, mutableSetOf()))
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
                                        .Identifier().text
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
                                }?.second?.add(c.postfixExpression().primaryExpression().Identifier().text)
                            }
                            //*a=b;
                            if (it.unaryExpression()?.unaryOperator()?.text == "*") {
                                pointers.find { p ->
                                    p.first.text == it.unaryExpression().castExpression().unaryExpression()
                                        .postfixExpression().primaryExpression().Identifier().text
                                }?.second?.forEach { t ->
                                    edges.find { e -> e.first.text == t }?.second?.add(
                                        c.postfixExpression().primaryExpression().Identifier().text
                                    )
                                }
                            }
                        }
                    }
                }
            updatePts()
        }
        updatePts()
        checkCall()
    }

    private fun checkCall() {
        calls.forEach {
            functions.find { f -> f.name == it.name }?.let { f ->
                f.args.filter { a -> a.first }.forEach { a ->
                    f.callEdges.find { c -> c.first.text == a.second.text }?.second?.add(
                        Pair(
                            name,
                            it.args[f.args.indexOf(a)].conditionalExpression().logicalOrExpression()
                                .logicalAndExpression().inclusiveOrExpression().exclusiveOrExpression().andExpression()
                                .equalityExpression().relationalExpression().shiftExpression().additiveExpression()
                                .multiplicativeExpression().castExpression().unaryExpression().castExpression()
                                .unaryExpression().postfixExpression().primaryExpression().Identifier().text
                        )
                    )
                }
            }
        }
    }

    private fun updatePts() {
        edges.forEach {
            it.second.forEach { s ->
                pointers.find { p ->
                    p.first.text == it.first.text
                }?.second?.addAll(pointers.find { p ->
                    p.first.text == s
                }?.second ?: emptySet())
            }
        }
    }

    fun updateForCall() {
        callEdges.forEach {
            pointers.find { p -> p.first.text == it.first.text }?.let { p ->
                it.second.forEach { s ->
                    p.second.add("${s.first}.${s.second}")
                    p.second.addAll(
                        functions.find { f -> f.name == s.first }?.pointers?.find { fp -> fp.first.text == s.second }?.second?.map { m -> "${s.first}.$m" }
                            ?: emptySet()
                    )
                }
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