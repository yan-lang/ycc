package yan.ycc.api.phase;

import yan.foundation.driver.lang.Phase;
import yan.foundation.exec.GenericValue;
import yan.foundation.exec.InterpreterImpl;
import yan.foundation.ir.Module;

import java.util.List;

public class Interpreter extends Phase<Module, Object> {

    yan.foundation.exec.Interpreter interpreter;

    @Override
    public Object transform(Module module) {
        interpreter = new InterpreterImpl(module);
        GenericValue returnValue = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        return null;
    }
}

