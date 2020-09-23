import clang.*
import org.antlr.v4.runtime.*
import java.io.File

fun main(args: Array<String>) {
    val text = File("examples/demo.c").readText()
    val source = ANTLRInputStream(text)
    val lexer = ClangLexer(source)
    val tokenStream = CommonTokenStream(lexer)
    val parser = ClangParser(tokenStream)
    println(parser.statement().children)
}