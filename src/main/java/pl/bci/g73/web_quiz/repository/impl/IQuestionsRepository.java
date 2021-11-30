package pl.bci.g73.web_quiz.repository.impl;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.bci.g73.web_quiz.entity.QuizRecord;

@Repository
public interface IQuestionsRepository extends PagingAndSortingRepository<QuizRecord, Long> {
//    private List<QuizRecord> quizRecords = new ArrayList<>();
//
//    public QuizRecord getQuizRecordById(int id){
//        return quizRecords
//                .stream()
//                .filter(q -> q.getId()==id)
//                .findFirst().get();
}