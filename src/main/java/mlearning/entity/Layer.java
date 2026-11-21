package mlearning.entity;

public record Layer(Type type, int neurons, Activation activation) {
    public enum Type { INPUT, DENSE, OUTPUT }
    public enum Activation { NONE, TANH, RELU, SOFTMAX }

    public Layer {
        if (type == null || activation == null || neurons < 1 || neurons > 32) {
            throw new IllegalArgumentException("Each layer needs a type, activation, and 1–32 neurons.");
        }
        boolean valid = switch (type) {
            case INPUT -> activation == Activation.NONE;
            case OUTPUT -> activation == Activation.SOFTMAX;
            case DENSE -> activation == Activation.TANH || activation == Activation.RELU;
        };
        if (!valid) {
            throw new IllegalArgumentException("Use no activation for input, tanh/ReLU for hidden, and softmax for output.");
        }
    }
}
