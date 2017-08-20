package ch.grze.frogmentexample.sample.activityswitching;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.grze.frogment.activity.StateAwareFrogmentActivity;
import ch.grze.frogment.frogment.FrogmentData;
import ch.grze.frogmentexample.R;

public class Activity1 extends StateAwareFrogmentActivity<State> {
    @BindView(R.id.state) protected TextView stateView;
    
    public Activity1() {
        super(R.id.fragment_container);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_with_state);
        ButterKnife.bind(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public State getDefaultState() {
        return new State("Activity1 default state");
    }

    @Override
    protected FrogmentData getDefaultFrogmentData() {
        return FrogmentData.forClass(FragmentFirstWithActivitySwitch.class);
    }

    @Override
    public void onStateChanged(State state) {
        super.onStateChanged(state);

        updateView();
    }

    private void updateView() {
        stateView.setText(state.getText());
    }
}
