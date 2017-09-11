package ch.grze.frogment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

import ch.grze.frogment.backstack.BackStackChangeListener;
import ch.grze.frogment.backstack.BackStackFrogmentManager;
import ch.grze.frogment.exception.UnableToCreateFrogmentInstanceException;
import ch.grze.frogment.core.Core;
import ch.grze.frogment.frogment.Frogment;
import ch.grze.frogment.frogment.FrogmentData;
import ch.grze.frogment.frogment.FrogmentState;
import ch.grze.frogment.frogment.StateAwareFrogment;

public abstract class FrogmentActivity extends AppCompatActivity implements BackStackChangeListener {
    public static final String FROGMENT_DATA = "frogment_data";

    private final BackStackFrogmentManager backStackFrogmentManager;
    private final int frogmentContainerId;

    private FrogmentData frogmentData;
    private Core core;

    public FrogmentActivity(@IdRes int frogmentContainerId) {
        this.frogmentContainerId = frogmentContainerId;

        backStackFrogmentManager = new BackStackFrogmentManager(getSupportFragmentManager(), this);
    }

    final public void setCore(Core core) {
        this.core = core;
    }

    @Override @CallSuper
    public void onFrogmentPushed(Frogment frogment) {
        onFrogmentConfigure(frogment);
    }

    @Override @CallSuper
    public void onFrogmentPopped(Frogment frogment) {
        onFrogmentConfigure(frogment);
    }

    @Override @CallSuper
    public void onBackStackEmpty() {
        finish();
    }

    final public FrogmentData getFrogmentData() {
        return frogmentData;
    }

    public void switchFrogment(FrogmentData data) {
        final Frogment frogment = getFrogmentFrom(data);
        frogment.setData(data);

        onFrogmentConfigure(frogment);

        getSupportFragmentManager().beginTransaction()
                .replace(frogmentContainerId, frogment, data.getTag())
                .addToBackStack(data.getTag())
                .commit();
    }

    public void switchActivity(FrogmentActivityData data) {
        final Intent intent = new Intent(this, data.getClazz());

        if (StateAwareFrogmentActivity.class.isAssignableFrom(data.getClazz()) && data.getState() != null) {
            intent.putExtra(StateAwareFrogmentActivity.STATE, data.getState());
        }

        if (FrogmentActivity.class.isAssignableFrom(data.getClazz()) && data.getFrogmentData() != null) {
            intent.putExtra(FrogmentActivity.FROGMENT_DATA, data.getFrogmentData());
        }

        startActivity(intent);
        finish();
    }

    protected abstract FrogmentData getDefaultFrogmentData();

    @CallSuper
    protected void onFrogmentConfigure(Frogment frogment) {
        final FrogmentData data = frogment.getData();
        frogmentData = data;

        if (frogment instanceof StateAwareFrogment) {
            final StateAwareFrogment stateAwareFrogment = (StateAwareFrogment) frogment;
            final FrogmentState state;

            if (data.getState() == null || !(data.getState() instanceof FrogmentState)) {
                state = (FrogmentState) stateAwareFrogment.getDefaultState();
            } else {
                state = data.getState();
            }

            final Bundle bundle = new Bundle();
            bundle.putParcelable(StateAwareFrogment.STATE, state);

            frogment.setArguments(bundle);
        }
    }

    protected Frogment getFrogmentInstance(Class<? extends Frogment> frogmentClass) {
        try {
            return frogmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnableToCreateFrogmentInstanceException(frogmentClass);
        }
    }

    protected <T> T getDataFromBundle(String key, Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getParcelable(key) != null) {
            return (T) savedInstanceState.getParcelable(key);
        }

        return null;
    }

    protected <T> T getDataFromIntent(String key, Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().getParcelable(key) != null) {
            return (T) intent.getExtras().getParcelable(key);
        }

        return null;
    }

    protected <T> T getData(String key, T defaultData, Intent intent, Bundle bundle) {
        T data;

        final T dataFromIntent = getDataFromIntent(key, intent);
        final T dataFromSavedInstance = getDataFromBundle(key, bundle);

        data = (dataFromSavedInstance != null) ? dataFromSavedInstance : dataFromIntent;
        return (data == null) ? defaultData : data;
    }

    private Frogment getFrogmentFrom(FrogmentData data) {
        final Frogment fragmentByTag = (Frogment) getSupportFragmentManager().findFragmentByTag(data.getTag());

        return fragmentByTag != null ? fragmentByTag : getFrogmentInstance(data.getClazz());
    }
}
