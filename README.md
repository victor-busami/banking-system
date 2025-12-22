# Minimal Bank ATM — Run Instructions

Prerequisites

- Java JDK installed

Steps

Compile all Java sources from the project folder:

```bat
javac -cp ".;sqlite-jdbc.jar" *.java
```

Run the application — two modes available (CLI and GUI):

- CLI mode (console):

```bat
java -cp ".;sqlite-jdbc.jar" BankApp
```

- GUI mode (Swing):

```bat
java -cp ".;sqlite-jdbc.jar" BankGUI
```

Files of interest

- Source: [Person.java](Person.java)
- Source: [Account.java](Account.java)
- Source: [DBHelper.java](DBHelper.java)
- Source (main): [BankApp.java](BankApp.java)
- GUI entry: [BankGUI.java](BankGUI.java)
