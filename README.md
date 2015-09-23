# LPR Library for Java
This is an easy to use Java library for controlling a printer over IP using LPR.
Without any other dependencies.
## Installation
1. Download or clone the repository
2. Add ```/dist/LPR_4_Java.jar``` to your project
3. optional: add the src and javadocs to your project as well
 
## Get started
 1. Define your printer
    ```
    LPRPrinter printer = new LPRPrinter(ip, 515);
    ```
 2. Create a document
    ```
    LPRDocument document = new LPRDocument("document name");
    document.insert("hello world!");
    ```
 3. Print the document
    ```
    printer.print(document);
    ```


