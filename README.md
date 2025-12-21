# Minimal Bank ATM — Run Instructions

This README explains how to compile and run the current minimal Bank ATM Java project.

Prerequisites

- Java JDK installed and `javac`/`java` on PATH.
- Download the SQLite JDBC driver (example: `sqlite-jdbc-3.36.0.3.jar`) from:
  https://github.com/xerial/sqlite-jdbc/releases

Steps (Windows)

1. Place the downloaded JDBC JAR in the project folder `d:\projects\banking-system` and rename it to `sqlite-jdbc.jar` (or keep its name and adjust commands).

2. Compile all Java sources from the project folder:

3. Compile all Java sources from the project folder:

```bat
cd /d d:\projects\banking-system
javac -cp ".;sqlite-jdbc.jar" *.java
```

3. Run the application — two modes available (CLI and GUI):

- CLI mode (console):

```bat
java -cp ".;sqlite-jdbc.jar" BankApp
```

- GUI mode (Swing):

```bat
java -cp ".;sqlite-jdbc.jar" BankGUI
```

What happens

- The app will create a SQLite database file named `bank.db` in the same folder.
- Use the CLI menu to create customers/accounts, login, deposit, withdraw, and list accounts.

Notes about modes

- Both the CLI (`BankApp`) and the GUI (`BankGUI`) use the same SQLite database file `bank.db` in the project folder. Actions performed in one mode (for example creating an account in the GUI) will be visible in the other.
- Choose CLI for quick text-based interaction or scripted usage; choose GUI for a simple graphical interface (Swing) to interact with accounts.

Notes & troubleshooting

- If `javac` fails with "package org.sqlite does not exist" or driver-related errors at runtime, ensure `sqlite-jdbc.jar` is present and the classpath is correct.
- If you prefer a single runnable JAR that bundles dependencies, use a build tool (Maven/Gradle) to create a shaded JAR — this project intentionally keeps build steps manual to stay minimal.

Troubleshooting

- If you see "package org.sqlite does not exist" during compilation, confirm `sqlite-jdbc.jar` is in the project folder and the `-cp` classpath is used with `javac`.
- If the GUI shows errors at runtime about the JDBC driver, ensure you run `java` with the same `-cp` including `sqlite-jdbc.jar`.
- If `bank.db` appears locked or inaccessible, close any running instance of the app and retry. SQLite locks can persist if a JVM process didn't exit cleanly.

Files of interest

- Source: [Person.java](Person.java)
- Source: [Account.java](Account.java)
- Source: [DBHelper.java](DBHelper.java)
- Source (main): [BankApp.java](BankApp.java)
- GUI entry: [BankGUI.java](BankGUI.java)

If you want, I can add a short section showing example CLI sessions and GUI screenshots/placeholders.

## Concept mapping (where each concept is used)

- **1. Data types & variables:** `Account.java`, `Person.java`, `DBHelper.java`, `BankApp.java` — fields use `int`, `double`, `String`, and `Scanner`.
- **2. Methods:** All source files contain methods; see constructors, getters/setters, CRUD methods, and CLI handlers in `BankApp.java`.
- **3. Classes:** `Person` (abstract) and `Customer` (concrete) in `Person.java`; `Account` in `Account.java`; `DBHelper` in `DBHelper.java`.
- **4. Encapsulation:** Private fields with public getters/setters in `Account.java` and `Person.java`; `DBHelper` hides connection details.
- **5. Inheritance:** `Customer` extends `Person` (`Person.java`).
- **6. Polymorphism:** `Customer` overrides `getRole()` and `toString()` (`Person.java`).
- **7. Abstraction:** `Person` is an abstract class with abstract method `getRole()` (`Person.java`).
- **8. Database Support:** All DB interactions are in `DBHelper.java` (JDBC, SQL create/read/update), used by `BankApp.java`.
- **9. Input validation:** `BankApp.java` validates CLI inputs (`readInt`/`readDouble`, empty name checks) and `Account` methods validate amounts.
- **10. Error handling:** `DBHelper.java` uses `try/catch (SQLException)` and logs errors; `BankApp.java` handles parsing and `IllegalArgumentException` thrown by `Account`.
