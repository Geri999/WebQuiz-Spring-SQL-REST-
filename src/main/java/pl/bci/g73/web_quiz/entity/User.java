package pl.bci.g73.web_quiz.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Email(regexp = "^\\S+@\\S+\\.\\S+$")
    private String email;

    @Size(min = 5)
    private String password;

    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    private List<QuizRecord> quizRecordList;

    private String role="USER";

    @OneToMany(mappedBy = "userId2", fetch = FetchType.LAZY)
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL) //bez tego działało
    private List<CompletedQuiz> completedQuizs;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
