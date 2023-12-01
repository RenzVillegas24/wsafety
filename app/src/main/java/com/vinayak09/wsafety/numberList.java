package com.vinayak09.wsafety;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class numberList extends Fragment {

    private MaterialButton addNumber;
    private TextInputEditText numberInput;
    private TextInputLayout number;
    private LinearLayout numberList;

    public numberList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_number_list, container, false);

        addNumber = view.findViewById(R.id.addNumber);
        numberInput = view.findViewById(R.id.numberInput);
        number = view.findViewById(R.id.number);
        numberList = view.findViewById(R.id.numberList);


        addNumber.setOnClickListener(v -> {

            if (numberInput.getText().toString().isEmpty()) {
                numberInput.setError("Please enter a number");
                return;
            }
            if (Extras.numberList.contains(numberInput.getText().toString())) {
                numberInput.setError("Number already added");
                return;
            }
            if (numberInput.getText().toString().startsWith("09") && numberInput.getText().toString().length() < 11) {
                numberInput.setError("Please enter a valid number");
                return;
            } else if (numberInput.getText().toString().startsWith("639") && numberInput.getText().toString().length() < 12) {
                numberInput.setError("Please enter a valid number");
                return;
            } else if (!numberInput.getText().toString().startsWith("09") && !numberInput.getText().toString().startsWith("639")) {
                numberInput.setError("Please enter a valid number");
                return;
            }


            new Handler().postDelayed(
                    () -> Extras.numberList.add(numberInput.getText().toString()),
                    250);
            updateList(true);
        });

        numberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numberInput.getText().toString().startsWith("09")) {
                    number.setCounterMaxLength(11);
                } else if (numberInput.getText().toString().startsWith("639")) {
                    number.setCounterMaxLength(12);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // delay to wait for the view to be created
        new Handler().postDelayed(
                () -> updateList(false),
                100
        );

        return view;
    }

    public void updateList(boolean animateStart){

        int duration = 250;


        // fade out animation
        numberList.animate()
                .alpha(0)
                .setDuration(animateStart ? duration: 0)
                .start();

        Log.d("numberList", "updateList: " + numberList.getLayoutParams().height);
        ValueAnimator animator = ValueAnimator.ofInt( ((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60f,
                getResources().getDisplayMetrics())) * Extras.numberList.size(), 0);
        animator.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = numberList.getLayoutParams();
            layoutParams.height = val;
            numberList.setLayoutParams(layoutParams);
        });
        animator.setDuration(animateStart ? duration: 0);
        animator.start();


        new Handler().postDelayed(
            () -> {
                numberList.removeAllViews();
                // fade in animation
                numberList.animate()
                        .alpha(1)
                        .setDuration(duration)
                        .start();
                ValueAnimator animator1 = ValueAnimator.ofInt( 0, ((int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        60f,
                        getResources().getDisplayMetrics())) * Extras.numberList.size());
                animator1.addUpdateListener(valueAnimator -> {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = numberList.getLayoutParams();
                    layoutParams.height = val;
                    numberList.setLayoutParams(layoutParams);
                });
                animator1.setDuration(duration);
                animator1.start();

                numberInput.setText("");
                numberList.removeAllViews();
                if (Extras.numberList.size() > 5) {
                    addNumber.setEnabled(false);
                    numberInput.setEnabled(false);
                } else {
                    addNumber.setEnabled(true);
                    numberInput.setEnabled(true);
                }

                for (String number : Extras.numberList) {
                    LinearLayout numberView = new LinearLayout(getContext());

                    MaterialButton deleteButton = new MaterialButton(getContext(), null, com.google.android.material.R.attr.materialIconButtonFilledStyle);
                    // set style of button
                    deleteButton.setLayoutParams(new LinearLayout.LayoutParams(addNumber.getWidth(), addNumber.getHeight()));
                    deleteButton.setTextSize(20);
                    deleteButton.setText("×");
                    deleteButton.setOnClickListener(view1 -> {


                        new Handler().postDelayed(
                                () -> Extras.numberList.remove(number),
                                250);
                        updateList(true);
                    });
                    numberView.addView(deleteButton);


                    TextView numberText = new TextView(getContext());
                    var border = new GradientDrawable();
                    border.setColor(MaterialColors.getColor(numberText, R.attr.colorSecondaryContainer));
                    border.setCornerRadius(10);
                    border.setStroke((int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            2f,
                            getResources().getDisplayMetrics()
                    ), MaterialColors.getColor(numberText, R.attr.colorPrimary));

                    numberText.setBackground(border);
                    numberText.setPadding(40, 40, 40, 40);
                    // set margin on left
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    50f,
                                    getResources().getDisplayMetrics())
                    );
                    params.setMargins(20, 0, 0, 0);
                    numberText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    numberText.setLayoutParams(params);
                    numberText.setTextSize(17);
                    numberText.setText(number);
                    numberView.addView(numberText);

                    numberList.addView(numberView);
                }
            },
                animateStart ? duration: 0
        );

    }
}