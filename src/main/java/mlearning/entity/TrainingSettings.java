package mlearning.entity;

public record TrainingSettings(int epochs, double learningRate) {
    public static final int SPLIT_SEED = 207;

    public TrainingSettings {
        if (epochs < 1 || epochs > 1000) {
            throw new IllegalArgumentException("Epochs must be between 1 and 1000.");
        }
        if (!Double.isFinite(learningRate) || learningRate < 0.001 || learningRate > 0.1) {
            throw new IllegalArgumentException("Learning rate must be between 0.001 and 0.1.");
        }
    }
}
