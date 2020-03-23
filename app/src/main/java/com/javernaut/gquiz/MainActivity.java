package com.javernaut.gquiz;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LoggingActivity {

    private static final String KEY_CURRENT_QUESTION_INDEX = "key_current_question_index";
    private static final String KEY_COUNT_QUESTION = "key_count_question";                  //
    private static final String KEY_COUNT_TRUE_QUESTION = "key_count_true_question";        //
    private static final List<Boolean> wasAnswer = new ArrayList<>();                       //
    private static final List<Boolean> wasTrueAnswer = new ArrayList<>();                   //


    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button checkButton;                                                             //
    private TextView questionView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true,
                    false, false),                                  //
            new Question(R.string.question_oceans, true,
                    false, false),                                  //
            new Question(R.string.question_mideast, false,
                    false, false),                                  //
            new Question(R.string.question_africa, false,
                    false, false),                                  //
            new Question(R.string.question_americas, true,
                    false, false),                                  //
            new Question(R.string.question_asia, true,
                    false, false)                                   //
    };

    private int currentQuestionIndex = 0;
    private int countQuestion = 0;                                                          //
    private int countTrueQuestion = 0;                                                      //
    private int allQuestion = mQuestionBank.length;                                         //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt(KEY_CURRENT_QUESTION_INDEX);
            countQuestion = savedInstanceState.getInt(KEY_COUNT_QUESTION);                  //
            countTrueQuestion = savedInstanceState.getInt(KEY_COUNT_TRUE_QUESTION);         //
            for (int x = 0; x < mQuestionBank.length; x++) {                                //
                mQuestionBank[x].setWasAnswer(wasAnswer.get(x));
                mQuestionBank[x].setWasTrueAnswer(wasTrueAnswer.get(x));
            }
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        checkButton = findViewById(R.id.check_button);                                      //
        questionView = findViewById(R.id.question);

        applyCurrentQuestion();

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(true);
                wasAnswer(mQuestionBank[currentQuestionIndex].getWasAnswer());              //
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(false);
                wasAnswer(mQuestionBank[currentQuestionIndex].getWasAnswer());              //
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
        outState.putInt(KEY_COUNT_QUESTION, countQuestion);                                 //
        outState.putInt(KEY_COUNT_TRUE_QUESTION, countTrueQuestion);                        //
        for (Question question : mQuestionBank) {                                           //
            wasAnswer.add(question.getWasAnswer());                                         //
            wasTrueAnswer.add(question.getWasTrueAnswer());                                 //
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

        if (wasTheAnswerCorrect) {                                                          //
            wasTrueAnswer(mQuestionBank[currentQuestionIndex].getWasTrueAnswer());
        }
    }

    private void showToast(int textId) {
        Toast toast = Toast.makeText(MainActivity.this, textId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.show();
    }

    private void wasAnswer(boolean wasAnswer) {                                             //
        if (!wasAnswer) {
            countQuestion++;
            mQuestionBank[currentQuestionIndex].setWasAnswer(true);
        }
    }

    private void wasTrueAnswer(boolean wasTrueAnswer) {                                     //
        if (!wasTrueAnswer) {
            countTrueQuestion++;
            mQuestionBank[currentQuestionIndex].setWasTrueAnswer(true);
        }
    }

    private void showToastQuestion() {                                                      //
        CharSequence text = "Отвечено " + countQuestion + "/" + allQuestion +
                " вопросов.\n"  +  "Правильных ответов: " + countTrueQuestion;
        Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.show();
    }
}
