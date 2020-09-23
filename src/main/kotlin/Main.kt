import demo.*
import org.antlr.v4.runtime.*

fun main(args: Array<String>) {
    val text = "hello world"
    val source = ANTLRInputStream(text)
    val lexer = DemoLexer(source)
    val tokenStream = CommonTokenStream(lexer)
    val parser = DemoParser(tokenStream)
    println(parser.r().children)
}