package com.javernaut.gquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends LoggingActivity {

    private static final String KEY_CURRENT_QUESTION_INDEX = "key_current_question_index";
    private static final String KEY_COUNT_QUESTION = "key_count_question";
    private static final String KEY_COUNT_TRUE_QUESTION = "key_count_true_question";
    private static final String KEY_WAS_ANSWER_SAVE = "key_was_answer_save";
    private static final String KEY_WAS_TRUE_ANSWER_SAVE = "key_was_true_answer_save";

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

    private Bundle wasAnswerSave = new Bundle();
    private Bundle wasTrueAnswerSave = new Bundle();

    private int currentQuestionIndex = 0;
    private int countQuestion = 0;
    private int countTrueQuestion = 0;
    private int allQuestion = mQuestionBank.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt(KEY_CURRENT_QUESTION_INDEX);
            countQuestion = savedInstanceState.getInt(KEY_COUNT_QUESTION);
            countTrueQuestion = savedInstanceState.getInt(KEY_COUNT_TRUE_QUESTION);

            wasAnswerSave.putAll(savedInstanceState.getBundle(KEY_WAS_ANSWER_SAVE));
            wasTrueAnswerSave.putAll(savedInstanceState.getBundle(KEY_WAS_TRUE_ANSWER_SAVE));
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
                wasAnswer(wasAnswerSave.getBoolean(String.valueOf(currentQuestionIndex)));
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(false);
                wasAnswer(wasAnswerSave.getBoolean(String.valueOf(currentQuestionIndex)));
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
                showToastQuestion();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_QUESTION_INDEX, currentQuestionIndex);
        outState.putInt(KEY_COUNT_QUESTION, countQuestion);
        outState.putInt(KEY_COUNT_TRUE_QUESTION, countTrueQuestion);

        outState.putBundle(KEY_WAS_ANSWER_SAVE, wasAnswerSave);
        outState.putBundle(KEY_WAS_TRUE_ANSWER_SAVE, wasTrueAnswerSave);
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
            wasTrueAnswer(wasTrueAnswerSave.getBoolean(String.valueOf(currentQuestionIndex)));
        }
    }

    private void showToast(int textId) {
        Toast.makeText(MainActivity.this, textId, Toast.LENGTH_SHORT).show();
    }

    private void wasAnswer(boolean wasAnswerVar) {
        if (!wasAnswerVar) {
            wasAnswerSave.putBoolean(String.valueOf(currentQuestionIndex), true);
            if (countQuestion <= mQuestionBank.length) {
                countQuestion++;
            }
        }
    }

    private void wasTrueAnswer(boolean wasTrueAnswerVar) {
        if (!wasTrueAnswerVar) {
            wasTrueAnswerSave.putBoolean(String.valueOf(currentQuestionIndex), true);
            if (countTrueQuestion <= mQuestionBank.length) {
                countTrueQuestion++;
            }
        }
    }

    private void showToastQuestion() {
        CharSequence text = "Отвечено " + countQuestion + "/" + allQuestion +
                " вопросов.\n"  +  "Правильных ответов: " + countTrueQuestion;
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
