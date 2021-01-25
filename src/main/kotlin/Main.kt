import clang.ClangLexer
import clang.ClangParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

@JvmField
val functions = mutableListOf<Function>()

fun main(args: Array<String>) {
    if (args.isEmpty()) return
    val lexer = ClangLexer(CharStreams.fromFileName(args.first()))
    val tokenStream = CommonTokenStream(lexer)
    val parser = ClangParser(tokenStream)

    val tree = parser.compilationUnit()
    val listener = WalkListener()
    ParseTreeWalker.DEFAULT.walk(listener, tree)

    functions.forEach {
        it.initPointers()
        it.checkExpr()
    }
    repeat(functions.size) {
        functions.forEach {
            it.updateForCall()
            it.updatePts()
        }
    }
    functions.forEach {
        it.print()
    }
}
