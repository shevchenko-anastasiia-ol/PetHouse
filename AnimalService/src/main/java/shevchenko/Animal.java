package shevchenko;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "animals")
public class Animal extends PanacheEntity {
    public String name;
    public String species;
    public int age;
    
    @Column(name = "healthstatus")
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

