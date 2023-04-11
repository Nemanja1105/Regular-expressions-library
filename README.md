# regular-expressions-library

This library is intended for working with regular languages represented by finite automata (DFAs and ε-NFAs) and/or regular expressions. It enables the following functionalities for working with regular languages:

• Execution of user-defined finite automata (DFAs and ε-NFAs)
• Construction of representations of union, intersection, difference, concatenation, complement, and Kleene star of a language, with support for chaining operations
• Determination of the length of the shortest and longest word in the language, as well as testing for language finiteness
• Minimization of the number of states for deterministic finite automata
• Transformation of ε-NFAs to DFAs, as well as transformation of regular expressions to finite automata
• Comparison of representations of regular languages, including regular expressions, for language equality

Additionally, the library includes an application that loads a specification of a regular language representation (supported representations are DFAs, ε-NFAs, and regular expressions) and tests the membership of specified strings to the represented language. It enables lexical analysis of the specification of the regular language representation and, in case of lexical irregularities, records the number of relevant lines in the specification that contain irregularities.

Furthermore, the library includes an application for generating source code for simulating a state machine based on a specification of a deterministic finite automaton. It enables users of the generated code to specify reactions to events upon entering and exiting each state. It also allows users of the generated code to specify reactions to the execution of a transition for each symbol of the automaton's alphabet, which can depend on the state from which the transition is made. Chaining of reactions is also supported, enabling the effective formation of a chain of reactions to an event.
