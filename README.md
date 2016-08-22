# Java Bytecode Size Optimization

### Master's Thesis: Java Bytecode Size Optimization

This paper deals with the Java bytecode size optimization. It describes the Java Virtual Machine and the Java class file format. It also presents some tools for the bytecode manipulation. Using these tools, I have analyzed selected data and found sequences of instructions, that could be optimized. Based on the results of the analysis, I have designed and implemented methods for bytecode size optimization. The bytecode size of the selected data was reduced by roughly 25%.

### Java Bytecode Analyzer: jbyca

A tool for the analysis of the Java bytecode is mostly useful for finding the typical sequences of instructions. It is possible to analyse very large data sets.

### Java Bytecode Optimizer: jbyco

This tool reduces the size of the given bytecode files. It removes the attributes for debugging and replaces sequences of instructions with optimized sequences. Achieved 25% reduction on the selected data set. Be aware, that the optimizer is not fully debugged. [ProGuard](https://sourceforge.net/projects/proguard/) does a better job.

Used: Java, Gradle, BCEL, ASM

Author: [Vendula Poncová](https://github.com/poncovka)
