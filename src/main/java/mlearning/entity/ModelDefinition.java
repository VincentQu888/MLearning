package mlearning.entity;

import java.util.List;

/** A sequential network: one input, at most three hidden layers, one output. */
public record ModelDefinition(List<Layer> layers, int inputSize, int outputSize) {
    public ModelDefinition {
        if (layers == null || layers.size() < 2 || layers.size() > 5 || layers.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException("Connect one input, up to three hidden layers, and one output.");
        }
        layers = List.copyOf(layers);
        Layer first = layers.get(0);
        Layer last = layers.get(layers.size() - 1);
        if (first.type() != Layer.Type.INPUT || last.type() != Layer.Type.OUTPUT
                || first.neurons() != inputSize || last.neurons() != outputSize) {
            throw new IllegalArgumentException("Start with the dataset's input size and end with its number of classes.");
        }
        for (int i = 1; i < layers.size() - 1; i++) {
            if (layers.get(i).type() != Layer.Type.DENSE) {
                throw new IllegalArgumentException("Only hidden layers may appear between input and output.");
            }
        }
    }

    public int parameterCount() {
        int count = 0;
        for (int i = 1; i < layers.size(); i++) {
            count += (layers.get(i - 1).neurons() + 1) * layers.get(i).neurons();
        }
        return count;
    }
}
