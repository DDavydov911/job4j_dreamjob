package ru.job4j.dream.service;

import org.springframework.stereotype.Service;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.CandidateStore;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateStore candidateStore;

    public CandidateService(CandidateStore candidateStore) {
        this.candidateStore = candidateStore;
    }

    public List<Candidate> findAll() {
        return new ArrayList<>(candidateStore.findAll());
    }

    public void add(Candidate candidate) {
        candidateStore.add(candidate);
    }

    public void update(Candidate candidate) {
        candidateStore.update(candidate);
    }

    public void create(Candidate candidate) {
        candidate.setId(candidateStore.id.incrementAndGet());
        candidateStore.create(candidate);
    }

    public Candidate findById(int id) {
        return candidateStore.findById(id);
    }

    public Candidate getById(Integer candidateId) {
        return candidateStore.findById(candidateId);
    }
}
