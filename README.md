# Bank ATM — Run Instructions

Prerequisites

- Java JDK installed

Steps

Compile all Java sources from the project folder:

```bat
javac -cp ".;sqlite-jdbc.jar" *.java
```

Run the application

- CLI mode (console):

```bat
java --enable-native-access=ALL-UNNAMED -cp ".;sqlite-jdbc.jar" BankApp
```

Files of interest

- Source: [Person.java](Person.java)
- Source: [Account.java](Account.java)
- Source: [DBHelper.java](DBHelper.java)
- Source (main): [BankApp.java](BankApp.java)

