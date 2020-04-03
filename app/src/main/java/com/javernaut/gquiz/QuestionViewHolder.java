package com.javernaut.gquiz;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionViewHolder extends RecyclerView.ViewHolder {
    private final TextView question;
    private final TextView answer;

    public QuestionViewHolder(@NonNull View itemView) {
        super(itemView);
        this.question = itemView.findViewById(R.id.question);
        this.answer = itemView.findViewById(R.id.answer);
    }

    public void bindTo(Question question){
        this.question.setText(question.getQuestionResId());
        this.answer.setText(String.valueOf(question.getCorrectAnswer()));
    }
}
