package com.javernaut.gquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class MainActivity extends LoggingActivity {

    private static final String KEY_CURRENT_QUESTION_INDEX = "key_current_question_index";
    private static final String KEY_COUNT_QUESTION = "key_count_question";
    private static final String KEY_COUNT_TRUE_QUESTION = "key_count_true_question";
    private static final String KEY_WAS_ANSWER = "key_was_answer";

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button checkButton;
    private TextView questionView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int[] wasAnswer = new int[mQuestionBank.length*2 + 2];
    private int currentQuestionIndex = 0;
    private int allQuestion = mQuestionBank.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt(KEY_CURRENT_QUESTION_INDEX);
            wasAnswer[wasAnswer.length - 2] = savedInstanceState.getInt(KEY_COUNT_QUESTION);
            wasAnswer[wasAnswer.length - 1] = savedInstanceState.getInt(KEY_COUNT_TRUE_QUESTION);
            for (int x = 0; x < allQuestion; x++) {
                wasAnswer[x] = Objects.requireNonNull(
                        savedInstanceState.getIntArray(KEY_WAS_ANSWER))[x];
                wasAnswer[x + allQuestion] = Objects.requireNonNull(
                        savedInstanceState.getIntArray(KEY_WAS_ANSWER))[x + allQuestion];
            }
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        checkButton = findViewById(R.id.check_button);
        questionView = findViewById(R.id.question);

        applyCurrentQuestion();

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to next question
                if (currentQuestionIndex == allQuestion - 1) {
                    currentQuestionIndex = 0;
                } else {
                    currentQuestionIndex++;
                }

                // apply question
                applyCurrentQuestion();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastQuestion();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_QUESTION_INDEX, currentQuestionIndex);
        outState.putInt(KEY_COUNT_QUESTION, wasAnswer[wasAnswer.length - 2]);
        outState.putInt(KEY_COUNT_TRUE_QUESTION, wasAnswer[wasAnswer.length - 1]);
        outState.putIntArray(KEY_WAS_ANSWER, wasAnswer);
    }

    private void applyCurrentQuestion() {
        questionView.setText(getCurrentQuestion().getQuestionResId());
    }

    private Question getCurrentQuestion() {
        return mQuestionBank[currentQuestionIndex];
    }

    private void onAnswerSelected(boolean currentAnswer) {
        boolean wasTheAnswerCorrect = currentAnswer == getCurrentQuestion().getCorrectAnswer();

        showToast(wasTheAnswerCorrect ? R.string.correct_toast : R.string.incorrect_toast);

        if (wasTheAnswerCorrect) {
            addCountTrueQuestion(wasAnswer[currentQuestionIndex + allQuestion]);
        }
        addCountQuestion(wasAnswer[currentQuestionIndex]);
    }

    private void showToast(int textId) {
        Toast.makeText(MainActivity.this, textId, Toast.LENGTH_SHORT).show();
    }

    private void addCountQuestion(int wasAnswerVar) {
        if (wasAnswerVar == 0) {
            wasAnswer[currentQuestionIndex] = 1;
            if (wasAnswer[wasAnswer.length - 2] <= allQuestion) {
                wasAnswer[wasAnswer.length - 2]++;
            }
        }
    }

    private void addCountTrueQuestion(int wasTrueAnswerVar) {
        if (wasTrueAnswerVar == 0) {
            wasAnswer[currentQuestionIndex + allQuestion] = 1;
            if (wasAnswer[wasAnswer.length - 1] <= allQuestion) {
                wasAnswer[wasAnswer.length - 1]++;
            }
        }
    }

    private void showToastQuestion() {
        CharSequence text = "Отвечено " + wasAnswer[wasAnswer.length - 2] + "/" + allQuestion +
                " вопросов.\n"  +  "Правильных ответов: " + wasAnswer[wasAnswer.length - 1];
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
