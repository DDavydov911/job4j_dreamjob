package ru.job4j.dream.service;

import org.springframework.stereotype.Service;
import ru.job4j.dream.model.City;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CityService {
    private final Map<Integer, City> cities = new ConcurrentHashMap<>();

    public CityService() {
        cities.put(1, new City(1, "Москва"));
        cities.put(2, new City(2, "СПб"));
        cities.put(3, new City(3, "Екб"));
    }

    public List<City> getAllCities() {
        return new ArrayList<>(cities.values());
    }

    public City findById(int id) {
        return cities.get(id);
    }
}
