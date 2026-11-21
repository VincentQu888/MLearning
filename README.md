# MLearning

## Team Information

### Team Members

- Milos Kicic
- Vincent Qu

**Course:** CSC207  
**Project name:** MLearning (working title)  
**Use-case leads:** 
1, 3 - Milos Kicic
2, 4 - Vincent Qu

---

## Project Description

### Domain

Education — introductory machine learning and visual programming.

MLearning is a Java Swing desktop application inspired by Scratch that helps beginners build and train small neural networks without writing machine-learning code. Learners arrange blocks representing an input layer, hidden layers, and an output layer. They can change layer settings, select an educational dataset, train their model, and test its predictions to explore how a neural network learns.


### Initial Scope

- Small feed-forward classification networks with one input layer, zero to three dense hidden layers, and one output layer.
- Each hidden layer supports 1–32 neurons and tanh or ReLU activation. Input and output dimensions follow the dataset; the output uses softmax.
- Training supports 1–1000 epochs and a learning rate of 0.001–0.1.
- A curated dataset
- One in-memory project and its current training run. 
- Keyboard-operable controls for adding, ordering, configuring, and removing blocks, alongside drag-and-drop.

---

## User Stories

### User Story 1 — Prepare an Educational Dataset

As a student beginning to learn machine learning, I want to select a practice dataset and identify its input features and target label, so that I can understand what information my model will learn from and predict.

### User Story 2 — Build a Neural Network

As a learner with little programming experience, I want to create and modify a neural network's layer structure without writing code, so that I can explore how input, hidden, and output layers fit together.

### User Story 3 — Train and Evaluate a Model

As a student learning about model training, I want to train my configured network and review its performance on held-out examples, so that I can judge whether it has learned useful patterns beyond its training examples.

### User Story 4 — Try a Prediction

As a learner exploring a trained model, I want to obtain a prediction for a new example, so that I can see how the model applies what it has learned to unfamiliar inputs.

### User Story 5 — Resume an Experiment (Future Extension)

As a student working across multiple study sessions, I want to save and reopen my model design and experiment settings, so that I can continue my work without rebuilding it from scratch.

---

## Use Cases

### Use Case 1 — Prepare a Dataset

**Related story:** User Story 1  
**Priority:** MVP  
**Lead:** Milos Kicic 
**Primary actor:** Learner  
**Preconditions:** The learner has an active project.  
**Trigger:** The learner chooses to select a dataset.

**Main success scenario:**
1. The system presents its curated dataset choices.
2. The learner selects a dataset.
3. The system retrieves the dataset and displays its source, feature names, fixed species target, and sample rows.
4. The learner selects numeric input features; the target remains flower species.
5. The system validates the selection and associates the prepared dataset with the project.
6. The system displays the selected features, target, and number of target classes.

**Extensions:**
- Retrieval fails: explain the failure and allow a retry without replacing the previous dataset selection.
- The selection is unsupported or incomplete: identify the issue and request a valid selection.
- The learner cancels: keep the previous dataset selection unchanged.
- A different dataset configuration is accepted: mark incompatible network configurations and invalidate previous training results.

**Success outcome:** The project has a valid dataset configuration with known input and output dimensions.

### Use Case 2 — Build or Modify a Network

**Related story:** User Story 2  
**Priority:** MVP  
**Lead:** Vincent Qu  
**Primary actor:** Learner  
**Preconditions:** The project has a prepared dataset.  
**Trigger:** The learner chooses to create or edit the network.

**Main success scenario:**
1. The system displays the network workspace and available layer blocks.
2. The learner adds an input block, any desired hidden-layer blocks, and an output block through drag-and-drop or keyboard-operable controls.
3. The learner arranges the blocks into a single connected sequence and configures the hidden layers.
4. The system derives the input size from the selected features and the output size from the target classes.
5. The learner requests validation of the network.
6. The system checks the layer settings and connections, then confirms readiness for training.

**Extensions:**
- Invalid layer order, count, dimensions, or settings: identify the problem and keep training unavailable until corrected. The draft remains editable.
- An existing network is changed: invalidate the previous trained model and results to prevent predictions using an outdated design.

Connections follow the ordered block sequence; arbitrary disconnected edges and cycles cannot be created.

**Success outcome:** The project has a valid, connected network definition compatible with its dataset.

### Use Case 3 — Train and Evaluate a Network

**Related story:** User Story 3  
**Priority:** MVP  
**Lead:** To be assigned  
**Primary actor:** Milos Kicic  
**Preconditions:** The project has a prepared dataset and a valid network definition; no training run is active.  
**Trigger:** The learner requests training.

**Main success scenario:**
1. The system presents training settings and explains the training/held-out split.
2. The learner supplies an epoch count and learning rate within the supported limits and confirms training.
3. The system validates the settings and creates a run tied to the current dataset and network configuration.
4. The system separates training and held-out examples using a recorded split seed. Data-dependent preprocessing is fitted only on training examples.
5. The system shows a training-in-progress state while Java trains the network. When the bounded run completes, it displays a loss curve and an accessible table of per-epoch training losses.
6. The system evaluates the trained network on held-out examples and displays accuracy separately from training loss.

**Extensions:**
- Invalid settings: explain the issue and do not start training.
- Training fails: report the failure without exposing a partially trained model for prediction.
- Another training request arrives during an active run: reject the duplicate. Dataset and network edits remain unavailable until the run ends.

**Success outcome:** A completed run contains a trained model and evaluation results associated with the exact configuration used for training.

### Use Case 4 — Predict a Class for a New Example

**Related story:** User Story 4  
**Priority:** MVP  
**Lead:** Vincent Qu
**Primary actor:** Learner  
**Preconditions:** The project has a successfully trained model matching its current dataset and network configuration.  
**Trigger:** The learner chooses to test a new example.

**Main success scenario:**
1. The system presents the required input features with their names and expected numeric format.
2. The learner supplies a value for each feature and requests a prediction.
3. The system validates the values and applies the preprocessing recorded during training.
4. The system obtains a prediction from the trained model.
5. The system displays the predicted class alongside the submitted values.

**Extensions:**
- A value is missing, non-numeric, or non-finite: identify the field and request a correction without producing a prediction.
- Prediction fails: explain that no prediction was produced and allow a retry.

**Success outcome:** The learner receives a class prediction for a valid new example; the trained model is unchanged.

### Use Case 5 — Save and Reopen an Experiment

**Related story:** User Story 5  
**Priority:** Future extension; not part of the MVP  
**Lead:** To be assigned if scheduled  
**Primary actor:** Learner  
**Preconditions:** An active project exists for saving, or a saved project file is available for reopening.  
**Trigger:** The learner chooses to save or reopen a project.

**Main success scenario:**
1. The learner requests saving and chooses a file destination.
2. The system saves the dataset reference, feature/target selection, network definition, and training settings.
3. In a later session, the learner selects the saved file to reopen.
4. The system validates the file and restores the experiment configuration.
5. The system presents the restored project and indicates that training is required before prediction.

**Extensions:**
- The learner cancels: leave the active project unchanged.
- Saving fails: report the failure without claiming success.
- A file is invalid or references an unavailable dataset: explain the problem and leave the active project unchanged.

**Success outcome:** The learner can resume a saved experiment configuration. Trained weights are not included in this proposed extension.

---

## Minimal Viable Product (MVP)

The MVP workflow is **prepare a dataset → build a network → train and evaluate → try a prediction**. The draft prototype implements these four use cases.

| Team Member | MVP Use Case |
|-------------|--------------|
| Milos Kicic | UC1 — Prepare a Dataset |
| Vincent Qu | UC2 — Build or Modify a Network |
| Milos Kicic | UC3 — Train and Evaluate a Network |
| Vincent Qu | UC4 — Predict a Class for a New Example |

---

## Entities
### Dataset
- **Instance variables:** `name`, `source`, `features`, `classes`, `inputs`, `targets`.
- **Relevant methods:** `selectFeatures(indices)`, `size()`, `input(row)`, `target(row)`, and metadata accessors.
- **Rules:** Numeric values must be finite; dimensions and labels must be valid. Arrays are copied, and feature selection returns a new dataset.


### TrainingSettings
- **Instance variables:** `epochs`, `learningRate` (immutable record); `SPLIT_SEED` is 207.
- **Relevant methods:** Record accessors and constructor validation of resource limits.

### Dataset
- **Instance variables:** `name`, `source`, `features`, `classes`, `inputs`, `targets`.
- **Relevant methods:** `selectFeatures(indices)`, `size()`, `input(row)`, `target(row)`, and metadata accessors.
- **Rules:** Numeric values must be finite; dimensions and labels must be valid. Arrays are copied, and feature selection returns a new dataset.

### TrainedModel
- A domain prediction interface: `predict(double[] inputs)` returns class probabilities in the dataset's class order.
- The concrete network, weights, and training-fitted normalization live in the outer training adapter, not in the use cases.
- A separate persistent `TrainingRun` history entity is deferred: only the current model is stored, and the latest metrics are presented in the Swing view.

### Layer
- **Instance variables:** `type`, `neurons`, `activation` (immutable record).
- **Relevant methods:** Record accessors and constructor validation.
- **Rules:** Input uses no activation, hidden layers use tanh/ReLU, output uses softmax; 1–32 neurons per layer.

### LearningProject
- **Instance variables:** `dataset`, `definition`, `trainedModel`, `training`.
- **Relevant methods:** `selectDataset(...)`, `define(...)`, `clearDefinition()`, `beginTraining()`, `finishTraining(...)`, and checked accessors.
- **Rules:** Dataset or applied network changes discard trained results; edits, duplicate training, and prediction are rejected while training is active. Use cases synchronize project transitions.

### ModelDefinition
- **Instance variables:** `layers` (immutable ordered list), `inputSize`, `outputSize`.
- **Relevant methods:** Record accessors, constructor validation, `parameterCount()`.
- **Rules:** Exactly one input first and one output last, with at most three hidden layers between them. Dimensions must match the prepared dataset. Editing replaces the definition instead of mutating it.

---

## API Usage

### OpenML REST API

**Documentation:** [OpenML REST API](https://docs.openml.org/reference/api/)  
**Dataset:** [Iris #61, version 1](https://www.openml.org/d/61)

- Selecting OpenML Iris calls `https://www.openml.org/api/v1/json/data/61` from Java, validates the dataset/version/file ID, then downloads `https://openml.org/data/v1/download/61/iris.arff`.
- No API key is needed. Requests are only for the public dataset
- API supplies data, which is used for training the models that users create.
