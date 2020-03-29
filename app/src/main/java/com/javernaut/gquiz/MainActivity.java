package com.javernaut.gquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends LoggingActivity {

    private static final int REQUEST_CODE_CHEAT = 42;
    private static final int REQUEST_CODE_CHECK = 423;
    private static final String KEY_CURRENT_QUESTION_INDEX = "key_current_question_index";
    private static final String KEY_WAS_ANSWER = "key_was_answer";
    private final int NOT_ANSWERED = 0;
    private final int WRONG_ANSWER = 1;
    private final int CORRECT_ANSWER = 2;

    private Button trueButton;
    private Button falseButton;
    private Button cheatButton;
    private Button nextButton;
    private Button checkButton;
    private TextView questionView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int[] wasAnswer = new int[mQuestionBank.length];
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt(KEY_CURRENT_QUESTION_INDEX);
            wasAnswer = savedInstanceState.getIntArray(KEY_WAS_ANSWER);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        cheatButton = findViewById(R.id.cheat_button);
        checkButton = findViewById(R.id.stats_button);
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

        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        CheatActivity.makeIntent(MainActivity.this, getCurrentQuestion().getCorrectAnswer()),
                        REQUEST_CODE_CHEAT
                );
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to next question
                if (currentQuestionIndex == mQuestionBank.length - 1) {
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
                int countAnswerWrong = 0;
                int countAnswerTrue = 0;
                for (int x = 0; x < mQuestionBank.length; x++) {
                    if (wasAnswer[x] == WRONG_ANSWER) {
                        countAnswerWrong++;
                    } else {
                        if (wasAnswer[x] == CORRECT_ANSWER) {
                            countAnswerTrue++;
                        }
                    }
                }
                startActivityForResult(
                        CheckActivity.makeIntent(MainActivity.this,
                                (countAnswerTrue + countAnswerWrong),
                                mQuestionBank.length,
                                countAnswerTrue),
                        REQUEST_CODE_CHECK);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_QUESTION_INDEX, currentQuestionIndex);
        outState.putIntArray(KEY_WAS_ANSWER, wasAnswer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (resultCode == Activity.RESULT_OK) {
                showToast(R.string.judgment_toast);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
            wasAnswer[currentQuestionIndex] = CORRECT_ANSWER;
        } else {
            if (wasAnswer[currentQuestionIndex] == NOT_ANSWERED) {
                wasAnswer[currentQuestionIndex] = WRONG_ANSWER;
            }
        }
    }

    private void showToast(int textId) {
        Toast.makeText(MainActivity.this, textId, Toast.LENGTH_SHORT).show();
    }
}
