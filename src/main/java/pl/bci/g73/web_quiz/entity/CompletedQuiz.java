package pl.bci.g73.web_quiz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class CompletedQuiz {


    public CompletedQuiz(Long id, LocalDateTime completedAt, User userId2) {
        this.id = id;
        this.completedAt = completedAt;
        this.userId2 = userId2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long idCompletedQuiz;

    //idQuiz
    private Long id;

    private LocalDateTime completedAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User userId2;

}
