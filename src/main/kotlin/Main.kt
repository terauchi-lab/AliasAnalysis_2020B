import clang.ClangLexer
import clang.ClangParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

fun main(args: Array<String>) {
    if (args.isEmpty()) return
    val lexer = ClangLexer(CharStreams.fromFileName(args.first()))
    val tokenStream = CommonTokenStream(lexer)
    val parser = ClangParser(tokenStream)

    val funcs = mutableListOf<Function>()
    val tree = parser.compilationUnit()
    val listener = WalkListener(funcs)
    ParseTreeWalker.DEFAULT.walk(listener, tree)

    funcs.forEach {
        it.initPointers()
        it.checkExpr()
        it.print()
    }
}
