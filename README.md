# Internet Printer Library
Java library for controlling printers over IP.
This library supports the following print protocols:
* RAW
* LPR
* Simple LPR (Boca printers)

This library also supports LPR commands and FGL markup.

## Installation
**Gradle:**
```
compile("com.maxxton:internet-printer-library:${maxxton_printer_library_version}")
```
 
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



