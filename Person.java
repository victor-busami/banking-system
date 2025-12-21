import java.util.Objects;

/**
 * Abstract Person class demonstrating abstraction and encapsulation.
 * Contains a concrete subclass Customer in the same file to keep total files to four.
 *
 * Clear concept mapping (this file):
 * 1. Data types & variables: `id` (int), `name` (String), `pin` in `Customer` (int).
 * 2. Methods: constructor, getters/setters, abstract `getRole`, overridden `toString`.
 * 3. Classes: `Person` (abstract) and `Customer` (concrete class).
 * 4. Encapsulation: private fields with public accessors (`getId`, `getName`, etc.).
 * 5. Inheritance: `Customer` extends `Person`.
 * 6. Polymorphism: `Customer` overrides `getRole()` and `toString()` (runtime polymorphism).
 * 7. Abstraction: `Person` is abstract and declares abstract `getRole()` which subclasses implement.
 */
public abstract class Person {
    private int id;
    private String name; // String data type

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Encapsulated getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Abstraction: subclasses must define role
    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("%s{id=%d, name='%s'}", getRole(), id, name);
    }
}

/**
 * Customer extends Person to demonstrate inheritance and polymorphism.
 */
class Customer extends Person {
    private int pin; // Encapsulated PIN (simple auth)

    public Customer(int id, String name, int pin) {
        super(id, name);
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    // Polymorphism: override toString
    @Override
    public String toString() {
        return String.format("Customer{id=%d, name='%s'}", getId(), getName());
    }
}
