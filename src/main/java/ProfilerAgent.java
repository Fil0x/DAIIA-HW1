import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREInitiator;

public class ProfilerAgent extends Agent {

    private int TOTAL_TIME = 60000;
    private int SPAWN_TIME = 5000;
    private FSMBehaviour fsm;

    public ProfilerAgent() {
        super();

        // This behaviour will be used to create random tourists every <period>
        addBehaviour(new MyTickerBehavior(this, SPAWN_TIME));
        // The total transaction timeout, we can't wait forever for the curator
        addBehaviour(new MyWakerBehavior(this, TOTAL_TIME));
        // Model the FSM
        initFSM();
    }

    private void initFSM() {
        // TODO
        //
        fsm = new FSMBehaviour(this) {
            public int onEnd() {
                System.out.println("FSM behaviour completed.");
                myAgent.doDelete();
                return super.onEnd();
            }
        };


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
            //ACLMessage initiationMessage
            //MySimpleAchieveREInitiator initiator = new MySimpleAchieveREInitiator(getAgent(), );
        }

        @Override
        protected void onTick() {
            System.out.println("Agent:" + getAgent().getName() + "[Ticker:" + getPeriod() + "] passed.");
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(new AID("curator", AID.ISLOCALNAME));
            request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
            request.setOntology(FIPANames.Ontology.SL0_ONTOLOGY);
            getAgent().addBehaviour(new MySimpleAchieveREInitiator(getAgent(), request));

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
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        }

        protected void handleAgree(ACLMessage msg) {
            System.out.println("Engagement agreed. Waiting for completion notification...");
        }
        protected void handleInform(ACLMessage msg) {
            System.out.println("Engagement successfully completed");
        }
        protected void handleNotUnderstood(ACLMessage msg) {
            System.out.println("Engagement request not understood by engager agent");
        }
        protected void handleFailure(ACLMessage msg) {
            System.out.println("Engagement failed");
        }
        protected void handleRefuse(ACLMessage msg) {
            System.out.println("Engagement refused");
        }
    }
}
