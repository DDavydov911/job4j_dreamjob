package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidateMap = new ConcurrentHashMap<>();

    private CandidateStore() {
        candidateMap.put(1, new Candidate(1, "Ivanov Ivan", "Junior"));
        candidateMap.put(2, new Candidate(2, "Semenov Semen", "Middle"));
        candidateMap.put(3, new Candidate(3, "Petrov Petr", "Senior"));
        candidateMap.put(4, new Candidate(4, "Artemov Artem", "Architect"));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidateMap.values();
    }
}