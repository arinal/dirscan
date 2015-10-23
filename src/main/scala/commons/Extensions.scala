package commons

object Extensions {
  class StringExtension(word: String) {
    def charCount(char: Char) = word.filter(c => c == char).length
  }
  implicit def StringExtension(word: String) = new StringExtension(word)
}