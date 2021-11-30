package pl.bci.g73.web_quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseToUser {
    //JSON dosn't include static fields
    private static String successful = "Congratulations, you're right!";
    private static String failed = "Wrong answer! Please, try again.";

    private boolean success;
    private String feedback;

    public ResponseToUser(boolean success) {
        this.success = success;
        this.feedback = success ?  successful: failed;
    }

}