package mlearning.usecase.prepare;

import mlearning.usecase.OutputBoundary;
import java.util.List;

/** Input boundary and simple data structures for US1. */
public interface PrepareDataset {
    record Input(String source, List<Integer> features) { }
    record Row(List<Double> values, String label) { }
    record Output(String name, String source, List<String> features, List<String> classes,
                  int count, List<Row> sample) { }
    void execute(Input input, OutputBoundary<Output> presenter);
}
