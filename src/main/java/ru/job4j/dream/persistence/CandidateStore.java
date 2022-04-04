package ru.job4j.dream.persistence;

import ru.job4j.dream.model.Candidate;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final AtomicInteger id;

    private final Map<Integer, Candidate> candidateMap = new ConcurrentHashMap<>();

    private CandidateStore() {
        id = new AtomicInteger(0);
        candidateMap.put(id.incrementAndGet(), new Candidate(id.get(), "Ivanov Ivan", "Junior"));
        candidateMap.put(id.incrementAndGet(), new Candidate(id.get(), "Semenov Semen", "Middle"));
        candidateMap.put(id.incrementAndGet(), new Candidate(id.get(), "Petrov Petr", "Senior"));
        candidateMap.put(id.incrementAndGet(), new Candidate(id.get(), "Artemov Artem", "Architect"));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidateMap.values();
    }

    public void create(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidateMap.put(id.get(), candidate);
    }

    public void update(Candidate candidate) {
        candidateMap.replace(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidateMap.get(id);
    }
}