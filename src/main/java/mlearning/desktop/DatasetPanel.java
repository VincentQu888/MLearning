package mlearning.desktop;

import mlearning.adapter.LearningController;
import mlearning.adapter.LearningViewModel;
import mlearning.usecase.prepare.PrepareDataset;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/** Dataset view: collects selections and renders source/sample output */
public final class DatasetPanel extends JPanel {
    private final LearningViewModel model;
    private final LearningController controller;
    private final List<JCheckBox> features = new ArrayList<>();
    private final JButton prepare = new JButton("Prepare dataset");
    private final JTextArea description = new JTextArea(5, 18);
    private final JTable sample = new JTable();
    private final JLabel status = new JLabel();
    private PrepareDataset.Output rendered;

    public DatasetPanel(LearningViewModel model, LearningController controller) {
        super(new BorderLayout(8, 12));
        this.model = model; this.controller = controller;
        setBorder(BorderFactory.createTitledBorder("01 / Dataset"));
        JPanel inputs = new JPanel(); inputs.setLayout(new BoxLayout(inputs, BoxLayout.Y_AXIS));
        inputs.add(new JLabel("Dataset: OpenML Iris (live API)"));
        inputs.add(Box.createVerticalStrut(15));
        inputs.add(new JLabel("Input measurements (cm)"));
        for (String feature : List.of("Sepal length", "Sepal width", "Petal length", "Petal width")) {
            JCheckBox checkBox = new JCheckBox(feature, true);
            checkBox.setName("feature-" + features.size());
            features.add(checkBox); inputs.add(checkBox);
            checkBox.addActionListener(event -> model.datasetSelectionChanged());
        }
        inputs.add(Box.createVerticalStrut(12));
        inputs.add(new JLabel("Target: flower species (fixed)"));
        inputs.add(Box.createVerticalStrut(15));
        prepare.setName("prepare-dataset"); prepare.setMnemonic(KeyEvent.VK_D);
        prepare.addActionListener(event -> prepare()); inputs.add(prepare);
        for (Component child : inputs.getComponents()) if (child instanceof JComponent component) component.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(inputs, BorderLayout.NORTH);
        description.setEditable(false); description.setLineWrap(true); description.setWrapStyleWord(true);
        description.setOpaque(false); description.setName("dataset-description");
        description.getAccessibleContext().setAccessibleName("Dataset description and source");
        description.setText("150 flowers, four measurements, three species.\n\nSelect features, then prepare the dataset. Internet access is required.");
        JPanel details = new JPanel(new BorderLayout(0, 10));
        details.add(description, BorderLayout.NORTH);
        sample.setName("dataset-sample"); sample.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sample.getAccessibleContext().setAccessibleName("Example measurements and species");
        JScrollPane scroll = new JScrollPane(sample); scroll.setPreferredSize(new Dimension(230, 150));
        details.add(scroll, BorderLayout.CENTER); add(details, BorderLayout.CENTER);
        status.setName("dataset-status");
        status.getAccessibleContext().setAccessibleName("Dataset operation status and errors");
        add(status, BorderLayout.SOUTH);
        model.addListener(event -> refresh());
        refresh();
    }

    private void prepare() {
        if (model.busy()) return;
        List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < features.size(); i++) if (features.get(i).isSelected()) selected.add(i);
        model.beginTask("Retrieving Iris from OpenML...");
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() {
                controller.prepare("openml", selected);
                return null;
            }
            @Override protected void done() {
                try { get(); }
                catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    model.fail("Dataset retrieval was interrupted. Please retry.");
                } catch (ExecutionException exception) {
                    System.getLogger(getClass().getName()).log(System.Logger.Level.ERROR,
                            "Dataset retrieval failed", exception.getCause());
                    model.fail("Dataset retrieval failed unexpectedly. Please retry.");
                } finally { model.endTask(); }
            }
        }.execute();
    }

    private void refresh() {
        status.setText(model.status());
        status.setToolTipText(model.status());
        prepare.setEnabled(!model.busy());
        features.forEach(checkBox -> checkBox.setEnabled(!model.busy()));
        PrepareDataset.Output data = model.dataset();
        if (data == null || data == rendered) return;
        rendered = data;
        description.setText(data.count() + " examples / " + data.classes().size() + " species\nSource: " + data.source()
                + "\n\nPrepared features: " + String.join(", ", data.features()));
        List<String> headers = new ArrayList<>(data.features()); headers.add("Species");
        Object[][] rows = new Object[data.sample().size()][];
        for (int i = 0; i < rows.length; i++) {
            PrepareDataset.Row row = data.sample().get(i);
            List<Object> values = new ArrayList<>(row.values()); values.add(row.label()); rows[i] = values.toArray();
        }
        sample.setModel(new DefaultTableModel(rows, headers.toArray()) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });
        for (int i = 0; i < sample.getColumnCount(); i++) sample.getColumnModel().getColumn(i).setPreferredWidth(120);
    }
}
