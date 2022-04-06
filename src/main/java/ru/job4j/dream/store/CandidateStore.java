package ru.job4j.dream.store;

import org.springframework.stereotype.Repository;
import ru.job4j.dream.model.Candidate;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Repository
public class CandidateStore {


    public final AtomicInteger id;

    private final Map<Integer, Candidate> candidateMap;

    private CandidateStore() {
        id = new AtomicInteger(0);
        candidateMap = new ConcurrentHashMap<>();
        candidateMap.put(id.incrementAndGet(), new Candidate(id.get(), "Ivanov Ivan", "Junior"));
        candidateMap.put(id.incrementAndGet(), new Candidate(id.get(), "Semenov Semen", "Middle"));
        candidateMap.put(id.incrementAndGet(), new Candidate(id.get(), "Petrov Petr", "Senior"));
        candidateMap.put(id.incrementAndGet(), new Candidate(id.get(), "Artemov Artem", "Architect"));
    }

    public Collection<Candidate> findAll() {
        return candidateMap.values();
    }

    public void add(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidateMap.put(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidateMap.get(id);
    }

    public void update(Candidate candidate) {
        candidateMap.replace(candidate.getId(), candidate);
    }

    public void create(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidateMap.put(candidate.getId(), candidate);
    }
}