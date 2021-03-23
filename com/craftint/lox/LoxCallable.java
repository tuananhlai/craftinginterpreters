package com.craftint.lox;

import java.util.List;

interface LoxCallable {
    // We pass in the interpreter in case the class implementing call() needs it
    Object call(Interpreter interpreter, List<Object> arguments);

    int arity();
}