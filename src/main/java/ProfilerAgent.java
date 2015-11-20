import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.states.MsgReceiver;

import java.io.IOException;
import java.util.List;

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
            sb.addSubBehaviour(new WaitArtifactIDsFromPlatform(getAgent()));
            getAgent().addBehaviour(sb);
        }
    }

    private class RequestTourGuide extends OneShotBehaviour {
        public RequestTourGuide(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {
            try {
                // Get the platform AID
                AID platfom;
                DFAgentDescription dfd = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("provide-tour");
                dfd.addServices(sd);
                DFAgentDescription[] result = DFService.search(getAgent(), dfd);
                if (result.length>0) {
                    platfom = result[0].getName();
                    // Create a random user
                    User u = Utilities.getUser(5);

                    // send the first message to the platform to ask for interesting artifacts
                    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                    request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                    request.addReceiver(platfom);
                    request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                    request.setOntology(FIPANames.Ontology.SL0_ONTOLOGY);
                    request.setContentObject(u);
                    Envelope envelope = new Envelope();
                    envelope.setComments("profiler");
                    request.setEnvelope(envelope);
                    send(request);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }

    private class WaitArtifactIDsFromPlatform extends MsgReceiver{

        public WaitArtifactIDsFromPlatform(Agent a) {
            super(a, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                    MsgReceiver.INFINITE, new DataStore(), "key");
        }

        @Override
        protected void handleMessage(ACLMessage msg) {
            System.out.println("(Profiler) Received msg");

            try {
                List<Integer> artifactIds = (List<Integer>)msg.getContentObject();
                StringBuilder sb = new StringBuilder();
                sb.append("(Profiler) Got artifacts: ");
                for (Integer i : artifactIds){
                    sb.append(i);
                    sb.append(" ");
                }
                System.out.println(sb.toString());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
    }
}
