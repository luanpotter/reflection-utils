package xyz.luan.reflection.entities;

import java.util.HashMap;
import java.util.Map;

public class Car {

    public static final int WHEELS = 4;
    
    private static Map<String, Car> preBuiltModels;
    static {
        preBuiltModels = new HashMap<>();
        preBuiltModels.put("Ford", new Car("Ford", CarType.SMALL, 145));
    }
    
    public final int height;
    private String model;
    private CarType type;

    public Car(String model, CarType type, int height) {
        this.model = model;
        this.type = type;
        this.height = height;
    }

    public Car(Car c) {
        this(c.model, c.type, c.height);
    }

    public int getHeight() {
        return height;
    }

    public String getModel() {
        return model;
    }

    public CarType getCarType() {
        return this.type;
    }

    public static Car build(String model) {
        return new Car(preBuiltModels.get(model));
    }
}
