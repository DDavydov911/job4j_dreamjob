package ru.job4j.dream.service;

import org.springframework.stereotype.Service;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.CandidateDbStore;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateDbStore candidateDbStore;

    public CandidateService(CandidateDbStore candidateDbStore) {
        this.candidateDbStore = candidateDbStore;
    }

    public List<Candidate> findAll() {
        return new ArrayList<>(candidateDbStore.findAll());
    }

    public void add(Candidate candidate) {
        candidateDbStore.create(candidate);
    }

    public void update(Candidate candidate) {
        candidateDbStore.update(candidate);
    }
/*
    public void create(Candidate candidate) {
        candidate.setId(candidateDbStore.id.incrementAndGet());
        candidateDbStore.create(candidate);
    }
*/
    public Candidate findById(int id) {
        return candidateDbStore.findById(id);
    }

    public Candidate getById(Integer candidateId) {
        return candidateDbStore.findById(candidateId);
    }

    public void deletePhotoById(int id) {
        candidateDbStore.deletePhotoById(id);
    }
}
