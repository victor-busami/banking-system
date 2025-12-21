# Minimal Bank ATM — Run Instructions

This README explains how to compile and run the current minimal Bank ATM Java project.

Prerequisites

- Java JDK installed and `javac`/`java` on PATH.
- Download the SQLite JDBC driver (example: `sqlite-jdbc-3.36.0.3.jar`) from:
  https://github.com/xerial/sqlite-jdbc/releases

Steps (Windows)

1. Place the downloaded JDBC JAR in the project folder `d:\projects\banking-system` and rename it to `sqlite-jdbc.jar` (or keep its name and adjust commands).

2. Compile all Java sources from the project folder:

```bat
cd /d d:\projects\banking-system
javac -cp ".;sqlite-jdbc.jar" *.java
```

3. Run the application:

```bat
java -cp ".;sqlite-jdbc.jar" BankApp
```

What happens

- The app will create a SQLite database file named `bank.db` in the same folder.
- Use the CLI menu to create customers/accounts, login, deposit, withdraw, and list accounts.

Notes & troubleshooting

- If `javac` fails with "package org.sqlite does not exist" or driver-related errors at runtime, ensure `sqlite-jdbc.jar` is present and the classpath is correct.
- If you prefer a single runnable JAR that bundles dependencies, use a build tool (Maven/Gradle) to create a shaded JAR — this project intentionally keeps build steps manual to stay minimal.

Files of interest

- Source: [Person.java](Person.java)
- Source: [Account.java](Account.java)
- Source: [DBHelper.java](DBHelper.java)
- Source (main): [BankApp.java](BankApp.java)
