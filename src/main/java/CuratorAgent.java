import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREResponder;

import java.io.IOException;
import java.io.Serializable;


public class CuratorAgent extends Agent {
    private static final String NAME = "curator";

    private ArtifactIndex artifactIndex = new ArtifactIndex();

    public CuratorAgent(){
        super();
        MessageTemplate mt = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        addBehaviour(new MySimpleAchieveREResponder(this, mt));
    }

    protected void setup(){
        // Artifact search
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("artifact-search");
        sd1.setName("provide-artifact-search");
        sd1.addOntologies("request-ids");

        // Artifact lookup based on item ID
        ServiceDescription sd2 = new ServiceDescription();
        sd2.setType("artifact-lookup");
        sd2.setName("provide-artifact-lookup");
        sd2.addOntologies("request-iteminfo");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd1);
        dfd.addServices(sd2);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private class MySimpleAchieveREResponder extends SimpleAchieveREResponder {

        public MySimpleAchieveREResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        protected ACLMessage prepareResponse(ACLMessage request){
            ACLMessage reply = request.createReply();
            System.out.println("(Curator) preparing response to request from: " + request.getSender().getLocalName());
            //System.out.println("(Curator) with content: " + request.getContent());
            if ("platform".equals(request.getEnvelope().getComments())){
                System.out.println("(Curator) Message received from a platform called: " + request.getSender().getLocalName());
                reply.setPerformative(ACLMessage.AGREE);

            } else if ("profiler".equals(request.getEnvelope().getComments())){
                System.out.println("(Curator) Message received from a profiler called: "+ request.getSender().getLocalName());
                reply.setPerformative(ACLMessage.AGREE);
            } else {
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);

            }
            return reply;
        }

        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
            ACLMessage reply = request.createReply();
            User u;
            try {
                u = (User)request.getContentObject();
                reply.setContentObject((Serializable)artifactIndex.searchArtifacts(u.getInterests()));

            } catch (UnreadableException e) {
                System.err.println("(Curator) Could not deserialize user");
            } catch (IOException e) {
                System.err.println("(Curator) Could not serialize artifact id list");
            }

            reply.setPerformative(ACLMessage.INFORM);

            return reply;
        }
    }
}
