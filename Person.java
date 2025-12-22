import java.util.Objects;

public abstract class Person {
    //encapsulation(use of private fields)
    //data types & variables(id, name)
    private int id;
    private String name; 

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //encapsulated getters/setters
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

    //abstraction(abstract method to get role)
    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("%s{id=%d, name='%s'}", getRole(), id, name);
    }
}

//customer extends person to demonstrate inheritance and polymorphism.

class Customer extends Person {
    //data types & variables(pin)
    //encapsulation(use of private field)
    private int pin; 

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

    //polymorphism(overrides toString method)
    @Override
    public String toString() {
        return String.format("Customer{id=%d, name='%s'}", getId(), getName());
    }
}
