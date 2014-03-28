# tardis

A playpen for parsing dates.

A Guess: Natural language date parsing is inherently functional, e.g.
`Saturday at 3pm` is simply a map across Saturdays, and `before May` is
a filter function.

To parse dates:

1. Create a high level set of functions that map to common date language
2. Translate from natural language to a syntax tree. 8|
3. Profit

## Output

WIP

{:type :any|:every
 :dates [[start-time end-time], ...]}


## Functions

- Types
  - Generators (lazily generate a sequence)
  - Filters
  - Translators (convert one interval to another)

## Sentences

For actual code, see `test/tardis/bespoke_test.clj`

"Let's catch up Friday this week."
- `Friday`: weekday generator
- `this week`: week filter
- This should be of `:type :any`

"Let's meet every Wednesday after next Tuesday before April."
- `every Wednesday`: weekday generator
- `after next Tuesday`: after(p) weekday filter
- `before April`: before(p) month filter
- `every`: `:type :all`

"every Friday at 3pm"
- `every Friday`: weekday generator
- `at 3pm`: to time translator

"the last Friday of every month"
- `every month`: month generator
- `last Friday`: translator


## Usage

```clojure
lein test
```

Copyright 2014