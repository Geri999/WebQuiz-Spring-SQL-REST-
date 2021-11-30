package pl.bci.g73.web_quiz.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.bci.g73.web_quiz.entity.CompletedQuiz;
import pl.bci.g73.web_quiz.entity.User;

public interface ICompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Long> {

    @Query("select c from CompletedQuiz c where c.userId2 = ?1 order by c.completedAt DESC")
    Page<CompletedQuiz> findCompletedByUser(User userId2, Pageable pageable);
}