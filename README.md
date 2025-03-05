# SEAL: Simple Enigma Machine Simulator

SEAL is a Java-based simulator that emulates the classic Enigma Machine. It leverages JavaFX to provide an interactive experience for exploring encryption techniques, rotor configurations, and the inner workings of the Enigma.

| Developed as part of my bachelor thesis at the School of Electrical Engineering, University in Belgrade.

## Table of Contents

- [Features](#features)
- [Modes of Operation](#modes-of-operation)
- [Configuration and Settings](#configuration-and-settings)
- [Installation](#installation)
- [Usage](#usage)
- [Repository Structure](#repository-structure)
- [Additional Resources](#additional-resources)

## Features

- **Interactive Simulation:** Experience the Enigma Machine in a hands-on way.
- **Multiple Modes:** Choose between Text, Simulation, or Keyboard modes.
- **Customizable Setup:** Configure rotor types, reflectors, plugboard, starting positions, and ring settings.
- **Import/Export:** Easily load and save larger text messages for encryption/decryption.
- **Visual Feedback:** Track rotor states and active wires in real-time.

## Modes of Operation

SEAL supports three distinct modes:

- **Text Mode:**  
  Designed for encrypting and decrypting larger text blocks with import/export functionality.

- **Simulation Mode:**  
  Allows the encryption/decryption of single letters while tracking rotor states and visualizing active wiring.

- **Keyboard Mode:**  
  Offers a realistic keyboard-lampboard experience for single-letter encryption and decryption.

## Configuration and Settings

- **Rotors:**  
  Available types include:  
  - I  
  - II  
  - III  
  - IV  
  - V  

- **Reflectors:**  
  Available types include:  
  - A  
  - B  
  - C

- **Additional Settings:**  
  Configure starting positions and ring settings for the rotors. The plugboard is fully configurable, allowing you to customize your encryption setup.

## Installation

### Prerequisites

- **Java SDK 23:**  
  Ensure you have Java SDK 23 installed on your machine.

- **JavaFX Library:**  
  Download and install the JavaFX library from [Gluon](https://gluonhq.com/products/javafx/).

### Setup

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yourusername/seal-enigma.git
   cd seal-enigma
   ```

2. **Import into IntelliJ IDEA:**

- Open IntelliJ IDEA.
- Select **File > Open** and choose the cloned repository directory.
- IntelliJ will automatically detect the project structure.

3. **Configure JavaFX:**

- Open **File > Project Structure > Libraries**.
- Click the **+** button to add the JavaFX SDK (navigate to the `lib` folder of your JavaFX installation).

## Usage

To start the application:

1. Locate the [`MasterMain` class](https://github.com/natalijamitic/SEAL/blob/main/code/src/mn170085d/MasterMain.java).
2. Right-click the file in IntelliJ IDEA and select **Run 'MasterMain.main()'**.
3. Alternatively, set up a custom run configuration in **Run > Edit Configurations…** and add the necessary VM options for JavaFX.

Enjoy exploring the mechanics of the Enigma Machine!

## Repository Structure
The code is organized into two main packages:
- `enigma`:
Contains the core functionality—handling configuration, encryption/decryption logic, and the inner workings of the simulation.
- `GUI`:
Houses the JavaFX-based user interface, providing an interactive front end for the simulator.

Additionally, the repository includes my bachelor thesis, which offers further insights into the project and its underlying concepts.

## Additional Resources
- JavaFX Documentation:
[Gluon JavaFX](https://gluonhq.com/products/javafx/)
- Bachelor Thesis:
Refer to the [thesis document](https://github.com/natalijamitic/SEAL/blob/main/Natalija%20Mitic%20-%20Diplomski%20Rad.pdf) included in the repository for a detailed explanation of the project design and implementation. The thesis is in Serbian language.


