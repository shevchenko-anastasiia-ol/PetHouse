package shevchenko;

public class Animal {
    public Long id;
    public String name;
    public String species;
    public int age;
    public String healthStatus;
    public boolean adopted;

    public Animal() {}

    public Animal(Long id, String name, String species, int age, String healthStatus, boolean adopted) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.healthStatus = healthStatus;
        this.adopted = adopted;
    }
}
