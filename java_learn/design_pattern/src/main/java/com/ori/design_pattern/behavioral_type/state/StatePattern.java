package com.ori.design_pattern.behavioral_type.state;

public class StatePattern {
    public static void main(String[] args) {
        StateMachine stateMachine = new StateMachine();
        stateMachine.toState2();
        stateMachine.toState3();
        stateMachine.toState1();
    }
}

enum State {
    STATE1(1),
    STATE2(2),
    STATE3(3);

    private int value;

    private State(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

interface IState {
    State getName();

    void toState2();

    void toState3();

    void toState1();
}



class StateMachine {
    private IState currentState;

    public StateMachine() {
        currentState = new State1(this);
    }

    public IState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(IState currentState) {
        this.currentState = currentState;
    }

    public void toState1(){
        this.currentState.toState1();
    }

    public void toState2(){
        this.currentState.toState2();
    }

    public void toState3(){
        this.currentState.toState3();
    }
}


class State1 implements IState {

    private StateMachine stateMachine;

    public State1(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public State getName() {
        return State.STATE1;
    }

    @Override
    public void toState2() {
        State2 state2 = new State2(stateMachine);
        System.out.println(getName() + " to " + state2.getName());
        stateMachine.setCurrentState(state2);
    }

    @Override
    public void toState3() {
        State3 state3 = new State3(stateMachine);
        System.out.println(getName() + " to " + state3.getName());
        stateMachine.setCurrentState(state3);
    }

    @Override
    public void toState1() {
        //doNothing
    }
}

class State2 implements IState {

    private StateMachine stateMachine;

    public State2(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public State getName() {
        return State.STATE2;
    }

    @Override
    public void toState2() {
        //doNothing
    }

    @Override
    public void toState3() {
        State3 state3 = new State3(stateMachine);
        System.out.println(getName() + " to " + state3.getName());
        stateMachine.setCurrentState(state3);
    }

    @Override
    public void toState1() {
        State1 state1 = new State1(stateMachine);
        System.out.println(getName() + " to " + state1.getName());
        stateMachine.setCurrentState(state1);
    }
}

class State3 implements IState{

    private StateMachine stateMachine;

    public State3(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public State getName() {
        return State.STATE3;
    }

    @Override
    public void toState2() {
        State2 state2 = new State2(stateMachine);
        System.out.println(getName() + " to " + state2.getName());
        stateMachine.setCurrentState(state2);
    }

    @Override
    public void toState3() {
        //do nothing
    }

    @Override
    public void toState1() {
        State1 state1 = new State1(stateMachine);
        System.out.println(getName() + " to " + state1.getName());
        stateMachine.setCurrentState(state1);
    }
}
