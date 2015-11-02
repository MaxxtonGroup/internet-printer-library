# Internet Printer Library
Java library for controlling printers over IP.
This library supports the following print protocols:
* RAW
* LPR
* Simple LPR (Bota printers)

This library also supports LPR commands and FGL markup.

## Installation
1. Download or clone the repository
2. Build with gradle: ```gradle build```
3. Add ```/build/libs/internet-printer-library.jar``` to your project
4. optional: add the ```src``` to your project as well
 
## Get started
 1. Define your printer

    ```
    Printer printer = new Printer(ip);
    ```

 2. Create a document

    ```
    PrintDocument document = new PrintDocument("document name");
    document.insert("hello world!");
    ```

 3. Print the document

    ```
    printer.print(document, PrintProtocol.RAW);
    ```



