package com.example.covid19_utec;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

import androidx.core.content.ContextCompat;

public class RadioButtonView {
    private Button yesButton;
    private Button noButton;
    private boolean yesButtonSelected;
    private boolean noButtonSelected;
    private Context context;

    RadioButtonView(Button _yesButton, Button _noButton, Context _context) {
        yesButton = _yesButton;
        noButton = _noButton;
        yesButtonSelected = false;
        noButtonSelected = false;
        context = _context;
    }

    public void onButtonClick(Button selectedButton) {
        if(selectedButton.getId() == yesButton.getId()){
            unPaintNoButton();
            if(yesButtonSelected){
                unPaintYesButton();
                return;
            }
            paintYesButton();
            return;
        }
        unPaintYesButton();
        if(noButtonSelected){
            unPaintNoButton();
            return;
        }
        paintNoButton();
    }

    private void paintYesButton() {
        yesButton.setBackground(ContextCompat.getDrawable(context, R.drawable.yes_button_selected));
        yesButton.setTextColor(Color.parseColor("#FFFFFF"));
        yesButtonSelected = true;
    }

    private void unPaintYesButton(){
        yesButton.setBackground(ContextCompat.getDrawable(context, R.drawable.yes_button_unselected));
        yesButton.setTextColor(Color.parseColor("#1a9d6b"));
        yesButtonSelected = false;
    }

    private void paintNoButton() {
        noButton.setBackground(ContextCompat.getDrawable(context, R.drawable.no_button_selected));
        noButton.setTextColor(Color.parseColor("#FFFFFF"));
        noButtonSelected = true;
    }

    private void unPaintNoButton(){
        noButton.setBackground(ContextCompat.getDrawable(context, R.drawable.no_button_unselected));
        noButton.setTextColor(Color.parseColor("#eb0303"));
        noButtonSelected = false;
    }

    public boolean getSelectedButton(){
        if(yesButtonSelected){
            return true;
        }
        return false;
    }
}
