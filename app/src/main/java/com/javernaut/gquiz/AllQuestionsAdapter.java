package com.javernaut.gquiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllQuestionsAdapter extends RecyclerView.Adapter<QuestionViewHolder> {
    private final Question[] mQuestions;

    public AllQuestionsAdapter(Question[] questions) {
        mQuestions = new Question[questions.length];
        for (int pos = 0; pos < questions.length; pos++) {
            mQuestions[pos] = questions[pos];
        }
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.object_question, parent, false);
        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bindTo(mQuestions[position]);
    }

    @Override
    public int getItemCount() {
        return mQuestions.length;
    }
}
