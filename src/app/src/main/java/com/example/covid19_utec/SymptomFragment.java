package com.example.covid19_utec;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SymptomFragment extends Fragment {
    int position;
    Boolean value;
    Boolean created = false;
    String question;
    Button yesButton;
    Button noButton;
    Button sendButton;
    ImageButton goback;
    ImageButton gonext;
    Boolean visibleGoNextButton;
    Boolean visibleGoBackButton;
    Boolean visibleSendButton;
    RadioButtonView groupRadio;

    // newInstance constructor for creating fragment with arguments
    public  static SymptomFragment newInstance( String question, int position, Boolean visibleGoBackButton , Boolean visibleGoNextButton, Boolean visibleSendButton) {
        SymptomFragment fragmentFirst = new SymptomFragment();
        Bundle args = new Bundle();
        args.putString("question", question);
        args.putInt("position",position);
        args.putBoolean("goback",visibleGoBackButton);
        args.putBoolean("gonext",visibleGoNextButton);
        args.putBoolean("send",visibleSendButton);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        question = getArguments().getString("question");
        position = getArguments().getInt("position");
        visibleSendButton = getArguments().getBoolean("send");
        visibleGoNextButton =  getArguments().getBoolean("gonext");
        visibleGoBackButton = getArguments().getBoolean("goback");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_symptom, container, false);
        createViews(view);
        TextView tvLabel = (TextView) view.findViewById(R.id.question);
        tvLabel.setText(question);
        return view;
    }

    private void createViews(View view){
        setVariables(view);
        setOnCLickListeners();
        if(created){
            paintActualButton();
        }
        created = true;
    }

    public void paintActualButton(){
        if(value == null)
            return;
        if(value)
            groupRadio.paintYesButton();
        else
            groupRadio.paintNoButton();
    }

    private void setVariables(View view){
        yesButton = view.findViewById(R.id.si_button);
        noButton = view.findViewById(R.id.no_button);
        groupRadio = new RadioButtonView(yesButton,noButton,view.getContext());
        sendButton = view.findViewById(R.id.send);
        goback = view.findViewById(R.id.atras);
        gonext= view.findViewById(R.id.siguiente);
        setVisibility();
    }

    private void setOnCLickListeners(){
        yesButton.setOnClickListener((View v) -> {
            groupRadio.onButtonClick(yesButton);
            value=groupRadio.getSelectedButton();
            goNextFragment();
        });

        noButton.setOnClickListener((View v) ->{
            groupRadio.onButtonClick(noButton);
            value=groupRadio.getSelectedButton();
            goNextFragment();
        });
        sendButton.setOnClickListener((View v)->{
            ((ReportFragment)getParentFragment()).sendDataToActivity();
        });
        gonext.setOnClickListener((View v)->{
            ((ReportFragment)getParentFragment()).setCurrentItem(position+1,true);
        });
        goback.setOnClickListener((View v)->{
            ((ReportFragment)getParentFragment()).setCurrentItem(position-1,true);
        });
    }

    public void goNextFragment(){
        if(groupRadio.areButtonsPainted())
            ((ReportFragment)getParentFragment()).setCurrentItem(position+1,true);
    }

    public void setVisibility(){
        if(!visibleGoBackButton)
            goback.setVisibility(View.GONE);
        if(!visibleGoNextButton)
            gonext.setVisibility(View.GONE);
        if(!visibleSendButton){
            sendButton.setVisibility(View.GONE);
        }
    }

    public Boolean getResult(){
        if(value == null)
            return false;
        return value;
    }

}
