package com.javernaut.gquiz;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends LoggingActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button previousButton;
    private TextView questionView;

    private int currantQuestionIndex = 0;

    // база данных вопросов приложения
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionView = findViewById(R.id.question);

        applyCurrentQuestion();

        // обработка кнопки True
        trueButton = findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(true);
            }
        });

        // обработка кнопки False
        falseButton = findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(false);
            }
        });

        // обработка кнопки Next
        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to next question
                if (currantQuestionIndex == mQuestionBank.length - 1) {
                    currantQuestionIndex = 0;
                } else {
                    currantQuestionIndex++;
                }
                // apply question
                applyCurrentQuestion();
            }
        });

        // обработка кнопки Previous
        previousButton = findViewById(R.id.previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to previous question
                if (currantQuestionIndex == 0) {
                    currantQuestionIndex = mQuestionBank.length - 1;
                } else {
                    currantQuestionIndex--;
                }
                // apply question
                applyCurrentQuestion();
            }
        });
    }

    // выводим в поле вопроса текущий вопрос из базы данных
    private void applyCurrentQuestion() {
        questionView.setText(getCurrentQuestion().getQuestionResId());
    }

    // получаем текущий вопрос и ответ на него из базы данных
    private Question getCurrentQuestion() {
        return mQuestionBank[currantQuestionIndex];
    }

    // проверяем правильность ответа пользователем
    private void onAnswerSelected(boolean currentAnswer) {
        boolean wasTheAnswerCorrect = currentAnswer == getCurrentQuestion().getCorrectAnswer();
        showToast(wasTheAnswerCorrect ? R.string.correct_toast : R.string.incorrect_toast);
    }

    // выводим всплывающее сообщение о правильности ответа пользователем
    private void showToast(int textId) {
        Toast toast = Toast.makeText(MainActivity.this, textId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, - 250);
        toast.show();
    }
}
