package mlearning.entity;

import java.util.HashSet;
import java.util.List;

public final class Dataset {
    private final String name;
    private final String source;
    private final List<String> features;
    private final List<String> classes;
    private final double[][] inputs;
    private final int[] targets;

    public Dataset(String name, String source, List<String> features, List<String> classes,
                   double[][] inputs, int[] targets) {
        if (features == null || features.isEmpty() || classes == null || classes.size() < 2
                || inputs == null || targets == null || inputs.length != targets.length || inputs.length < 10) {
            throw new IllegalArgumentException("Dataset must contain numeric features and labelled examples.");
        }
        this.name = name;
        this.source = source;
        this.features = List.copyOf(features);
        this.classes = List.copyOf(classes);
        this.inputs = new double[inputs.length][];
        this.targets = targets.clone();
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null || inputs[i].length != features.size()
                    || targets[i] < 0 || targets[i] >= classes.size()) {
                throw new IllegalArgumentException("Dataset row dimensions or class labels are invalid.");
            }
            this.inputs[i] = inputs[i].clone();
            for (double value : inputs[i]) {
                if (!Double.isFinite(value)) {
                    throw new IllegalArgumentException("Dataset values must be finite numbers.");
                }
            }
        }
    }

    public Dataset selectFeatures(List<Integer> selected) {
        if (selected == null || selected.isEmpty() || selected.size() > features.size()
                || new HashSet<>(selected).size() != selected.size()) {
            throw new IllegalArgumentException("Select at least one feature, without duplicates.");
        }
        for (Integer index : selected) {
            if (index == null || index < 0 || index >= features.size()) {
                throw new IllegalArgumentException("Unknown input feature.");
            }
        }
        double[][] values = new double[size()][selected.size()];
        for (int row = 0; row < size(); row++) {
            for (int col = 0; col < selected.size(); col++) {
                values[row][col] = inputs[row][selected.get(col)];
            }
        }
        return new Dataset(name, source, selected.stream().map(features::get).toList(), classes, values, targets);
    }

    public String name() { return name; }
    public String source() { return source; }
    public List<String> features() { return features; }
    public List<String> classes() { return classes; }
    public int size() { return inputs.length; }
    public double[] input(int row) { return inputs[row].clone(); }
    public int target(int row) { return targets[row]; }
}
