package ch.grze.frogment.frogment

import ch.grze.frogment.DefaultStateProvider
import ch.grze.frogment.State
import ch.grze.frogment.StateAware

interface StateAwareFrogmentInterface<S : State> : FrogmentInterface, StateAware<S>, DefaultStateProvider<S> {
    companion object {
        val STATE = "state"
    }

    val stateAwareFrogmentComponent: StateAwareFrogmentComponent<S>
}