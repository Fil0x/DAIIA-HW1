import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

import java.io.IOException;

public class ProfilerAgent extends Agent {

    private int TOTAL_TIME = 60000;
    private int SPAWN_TIME = 3000;
    private static final String NAME = "profiler";

    public ProfilerAgent() {
        super();

        // This behaviour will be used to create random tourists every <period>
        addBehaviour(new TourSpawner(this, SPAWN_TIME));
        // The total transaction timeout, we can't wait forever for the curator
        // addBehaviour(new MyWakerBehavior(this, TOTAL_TIME));
    }


    protected void setup() {
        System.out.println("Agent:" + getAID().getName() + " is ready!");
    }

    protected void takeDown() {
        System.out.println("Agent:" + getAID().getName() + " terminating...");
    }

    private class TourSpawner extends TickerBehaviour {
        public TourSpawner(Agent a, long period) {
            super(a, period);
        }

        @Override
        public void onStart() {
            System.out.println("Agent:" + getAgent().getName() + "[Ticker:" + getPeriod() + "] is ready!");
            //ACLMessage initiationMessage
            //RequestTourGuide initiator = new RequestTourGuide(getAgent(), );
        }

        @Override
        protected void onTick() {
            SequentialBehaviour sb = new SequentialBehaviour(getAgent());
            sb.addSubBehaviour(new RequestTourGuide(getAgent()));

            getAgent().addBehaviour(sb);
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

    private class RequestTourGuide extends OneShotBehaviour {
        public RequestTourGuide(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {
            try {
                // Create a random user
                User u = Utilities.getUser(5);

                // send the first message to the platform to ask for interesting artifacts
                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                request.addReceiver(new AID("platform", AID.ISLOCALNAME));
                request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                request.setOntology(FIPANames.Ontology.SL0_ONTOLOGY);
                request.setContentObject(u);

                send(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
