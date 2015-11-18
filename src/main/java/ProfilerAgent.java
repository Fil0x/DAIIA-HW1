import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

public class ProfilerAgent extends Agent{
    public ProfilerAgent() {
        super();

        // This behaviour will be used to create random tourists every <period>
        addBehaviour(new MyTickerBehavior(this, 3000));
        // The total transaction timeout, we can't wait forever for the curator
        addBehaviour(new MyWakerBehavior(this, 8000));
    }

    protected void setup() {
        System.out.println("Agent:" + getAID().getName() + " is ready!");
    }

    protected void takeDown() {
        System.out.println("Agent:" + getAID().getName() + " terminating...");
    }

    private class MyTickerBehavior extends TickerBehaviour {
        public MyTickerBehavior(Agent a, long period) {
            super(a, period);
        }

        @Override
        public void onStart() {
            System.out.println("Agent:" + getAgent().getName() + "[Ticker:" + getPeriod() + "] is ready!");
        }

        @Override
        protected void onTick() {
            System.out.println("Agent:" + getAgent().getName() + "[Ticker:" + getPeriod() + "] passed.");
        }
    }

    private class MyWakerBehavior extends WakerBehaviour {

        public MyWakerBehavior(Agent a, long timeout) {
            super(a, timeout);
        }

        @Override
        public void onStart() {
            System.out.println("Agent:" + getAgent().getName() + "[Waker]: is ready!");
        }

        @Override
        public void onWake(){
            System.out.println("Agent:" + getAgent().getName() + "[Waker]: is ready, woke up.");
        }
    }

    private class MySimpleAchieveREInitiator extends SimpleAchieveREInitiator {

        public MySimpleAchieveREInitiator(Agent a, ACLMessage msg) {
            super(a, msg);
        }
    }
}
