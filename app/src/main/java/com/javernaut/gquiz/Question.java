package com.javernaut.gquiz;

public class Question {

    private int questionResId;
    private boolean correctAnswer;
    private  boolean wasAnswer;
    private boolean wasTrueAnswer;

    public Question(int questionResId, boolean correctAnswer, boolean wasAnswer, boolean wasTrueAnswer) {
        this.questionResId = questionResId;
        this.correctAnswer = correctAnswer;
        this.wasAnswer = wasAnswer;
        this.wasTrueAnswer = wasTrueAnswer;
    }

    public int getQuestionResId() {
        return questionResId;
    }

    public boolean getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean getWasAnswer() {
        return wasAnswer;
    }

    public void setWasAnswer(boolean wasAnswer) {
        this.wasAnswer = wasAnswer;
    }

    public boolean getWasTrueAnswer() {
        return wasTrueAnswer;
    }

    public void setWasTrueAnswer(boolean wasTrueAnswer) {
        this.wasTrueAnswer = wasTrueAnswer;
    }
}