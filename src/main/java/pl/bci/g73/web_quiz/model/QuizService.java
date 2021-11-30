package pl.bci.g73.web_quiz.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.bci.g73.web_quiz.entity.QuizRecord;
import pl.bci.g73.web_quiz.repository.impl.IQuestionsRepository;

@Service
public class QuizService {
    private IQuestionsRepository qRepository;

    @Autowired
    public QuizService(IQuestionsRepository qRepository) {
        this.qRepository = qRepository;
    }

    public QuizRecord getQuizRecordById(long id) {
        QuizRecord quizRecord = qRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return quizRecord;
    }
}