package pl.bci.g73.web_quiz.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.bci.g73.web_quiz.entity.*;
import pl.bci.g73.web_quiz.model.QuizService;
import pl.bci.g73.web_quiz.repository.impl.ICompletedQuizRepository;
import pl.bci.g73.web_quiz.repository.impl.IQuestionsRepository;
import pl.bci.g73.web_quiz.repository.impl.IUserRepository;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final IQuestionsRepository quizRepository;
    private final QuizService quizService;
    private final IUserRepository userRepository;
    private final ICompletedQuizRepository completedQuizRepository;
    private final PasswordEncoder encoder;

    Authentication authentication;

    @Autowired
    public QuizController(IQuestionsRepository quizRepository, QuizService quizService,
                          IUserRepository userRepository, ICompletedQuizRepository completedQuizRepository,
                          PasswordEncoder encoder) {
        this.quizRepository = quizRepository;
        this.quizService = quizService;
        this.userRepository = userRepository;
        this.completedQuizRepository = completedQuizRepository;
        this.encoder = encoder;
    }

    @PostMapping(path = "/quizzes")
    public QuizRecord addQuiz(@RequestBody @Valid QuizRecord quizRecord) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> userByEmail = userRepository.findUserByEmail(email);

        quizRecord.setUserId(userByEmail.get());
        return quizRepository.save(quizRecord);
    }

    @PostMapping(path = "/quizzes/{id}/solve", consumes = "application/json")
    public ResponseToUser solvingQuiz(@PathVariable("id") long id, @RequestBody Answer answers) {
        boolean result = answers.getAnswer().equals(quizService.getQuizRecordById(id).getAnswer());

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email).get();

        if (result) {
            CompletedQuiz completedQuiz = new CompletedQuiz(id, LocalDateTime.now(), user);
            user.getCompletedQuizs().add(completedQuiz);
            completedQuizRepository.save(completedQuiz);
        }
        return new ResponseToUser(result);
    }

    @GetMapping(path = "/quizzes/{id}")
    public QuizRecord getQuizById(@PathVariable("id") long id) {
        return quizService.getQuizRecordById(id);
    }

    @GetMapping(path = "/quizzes")
    public Page<QuizRecord> getAllQuizzes(@RequestParam @Min(0) int page) {
        Pageable paging = PageRequest.of(page, 10);
        return quizRepository.findAll(paging);
    }

    @GetMapping(path = "/quizzes/completed")
    public Page<CompletedQuiz> getAllQuizzesCompleted(@RequestParam int page) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email).get();

        Pageable paging = PageRequest.of(page, 10, Sort.by("completedAt").descending());
        Page<CompletedQuiz> completedByUser = completedQuizRepository.findCompletedByUser(user, paging);

        return completedByUser;
    }


    @DeleteMapping(path = "/quizzes/{id}")
    public void quizDelete(@PathVariable("id") long id) {

        if (quizService.getQuizRecordById(id) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND); //404

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String theQuizOwnerEmail = quizService.getQuizRecordById(id).getUserId().getEmail();
        if (!currentUserEmail.equals(theQuizOwnerEmail)) throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403

        quizRepository.deleteById(id);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT); //204
    }


    @PostMapping(path = "/register")
    public void registration(@RequestBody @Valid User user) {
        boolean present = userRepository.findUserByEmail(user.getEmail()).isPresent();
        if (present) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        String encodePassword = encoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);
    }
}

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public void HandleMethodArgumentNotValidException(Exception e) {
//        System.err.println("ERROR: " + e.getMessage());
//    }
//}


