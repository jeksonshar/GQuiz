package com.javernaut.gquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CheckActivity extends AppCompatActivity {

    private static final String KEY_ANSWER = "key_answer";
    private static final String KEY_QUESTION = "key_question";
    private static final String KEY_TRUE_QUESTION = "key_true_question";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        TextView checkAnswerView = findViewById(R.id.check_View);
        checkAnswerView.setText("Отвечено " + getIntent().getIntExtra(KEY_ANSWER, 0)
                + "/" + getIntent().getIntExtra(KEY_QUESTION, 0) + " вопросов\n" +
                "правильных ответов: " + getIntent().getIntExtra(KEY_TRUE_QUESTION, 0));
    }


    public static Intent makeIntent(Context context, int answer, int question, int trueQuestion) {
        Intent intent = new Intent(context, CheckActivity.class);
        intent.putExtra(KEY_ANSWER, answer);
        intent.putExtra(KEY_QUESTION, question);
        intent.putExtra(KEY_TRUE_QUESTION, trueQuestion);

        return intent;
    }
}
