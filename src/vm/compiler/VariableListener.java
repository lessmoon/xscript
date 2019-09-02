package vm.compiler;

import java.util.function.Consumer;

public class VariableListener<T> {
    final Consumer<T> onChanged;
    final Consumer<T> onFinished;

    public VariableListener(Consumer<T> onFinished) {
        this(null, onFinished);
    }

    public VariableListener(Consumer<T> onChanged, Consumer<T> onFinished) {
        this.onChanged = onChanged;
        this.onFinished = onFinished;
    }

    public void onChanged(T value) {
        if (onChanged != null) {
            onChanged.accept(value);
        }
    }

    public void onFinished(T value) {
        if (onFinished != null) {
            onFinished.accept(value);
        } 
    }
}