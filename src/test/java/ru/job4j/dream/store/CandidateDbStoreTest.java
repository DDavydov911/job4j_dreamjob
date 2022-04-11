package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.Main;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDbStoreTest {

    @Test
    public void whenCreateCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Jhon", "Developer");
        store.create(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenUpdateCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Bill", "Analyzer");
        store.create(candidate);
        store.update(new Candidate(candidate.getId(), candidate.getName(), "Developer"));
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
        assertThat(candidateInDb.getDescription(), is("Developer"));
    }
}