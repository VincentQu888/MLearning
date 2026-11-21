package mlearning.entity;

/** Prediction capability independent of the training library or UI.
 * Implementations must accept exactly the trained feature order and return
 * finite class probabilities in dataset class order, summing to one.
 */
@FunctionalInterface
public interface TrainedModel {
    double[] predict(double[] inputs);
}
