NGram
=====

1. Stand in scr
2. javac Main.java
3. java Main parse (uses corpus_short.txt, adjust Main.java to change)

Result with categories:
java Main < ../data/toInputRead

Result without categories:
java Main nocategory < ../data/toInputRead 


--- PARSING ---

No tags:
java Main parse dictionary corpus out dontcategorize

Only <unk>:
java Main parse dictionary corpus out nocategory

With categories:
java Main parse dictionary corpus out


--- TESTING ---

With categories:
java Main input_with_categories < clean_input

Without categories:
java Main input_without_categories < clean_input