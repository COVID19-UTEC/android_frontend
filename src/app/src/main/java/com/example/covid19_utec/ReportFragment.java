package com.example.covid19_utec;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ReportFragment extends Fragment {

    private String title = "Reportar Síntomas";
    private static ArrayList<SymptomFragment> fragments;
    private ArrayList<String> questions;
    FragmentPagerAdapter adapterViewPager;
    private ArrayList<Boolean> answers;
    PagerTabStrip pagerTabStrip;
    ViewPager vpPager;
    Boolean created=false;

    public ReportFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questions= ((HomeActivity)getActivity()).getQuestions();
        initializeArrays();
        ((HomeActivity)getActivity()).changeFragmentTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_report, container, false);
        pagerTabStrip = view.findViewById(R.id.pager_header);
        pagerTabStrip.setTextColor(Color.BLACK);
        vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        paintLastSession();
        return view;
    }


    public void paintLastSession(){
        for(SymptomFragment f:fragments){
            f.paintActualButton();
        }
    }
    public void initializeArrays(){
        if(created)
            return;
        fragments = new ArrayList<>();
        Boolean visibleSendButton,visibleGoNextButton,visibleGoBackButton;
        visibleGoBackButton=visibleGoNextButton=true;
        visibleSendButton = false;
        for( int i = 0; i<questions.size();i++){
            if(i==0)
                visibleGoBackButton=false;
            if(i== questions.size()-1) {
                visibleGoNextButton=false;
                visibleSendButton=true;
            }
            fragments.add(SymptomFragment.newInstance(questions.get(i),i,visibleGoBackButton,visibleGoNextButton,visibleSendButton));
            visibleGoBackButton=visibleGoNextButton=true;
        }
        created = true;
    }
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = fragments.size();

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "Pregunta" + (position+1);
        }

    }
    public void setCurrentItem (int item, boolean smoothScroll) {
        vpPager.setCurrentItem(item, smoothScroll);
    }

    public void sendDataToActivity(){
        answers = new ArrayList<>();
        for(SymptomFragment b :fragments ){
            answers.add(b.getResult());
        }
        ((HomeActivity)getActivity()).sendData(answers);
    }
}
