# Turq's Coding Exercise for Gremlin

## Usage
Build with Maven, then run with or without a language name.

## Notes
 * The only external libraries I pulled in were Junit and Mockito, as this project was simple enough to not necessitate more.
 * I hard stopped at 2 hours, but all I had left was a single TODO for exponential backoff and the open question of whether to accept the Russian name for Russian.
 * Forismatic provides a "text" response type which is acceptably formatted, so for readability I've used that rather than parsing a more verbose format.
 * The unit tests do not require network connection or hit the Forismatic API. It may be worthwhile to also have integration tests which do go through the full stack.