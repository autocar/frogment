package ch.grze.frogmentexample.sample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.grze.frogment.frogment.Frogment;
import ch.grze.frogmentexample.R;

public class FragmentSecond extends Frogment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }
}
