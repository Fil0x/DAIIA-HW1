import java.util.ArrayList;
import java.util.List;

public class ArtifactIndex{

    public List<Integer> searchArtifactIDs(List<String> interests){
        //TODO dummy implementation
        ArrayList<Integer> result = new ArrayList<>();
        result.add(Utilities.getArtifact().getId());
        result.add(Utilities.getArtifact().getId());
        result.add(Utilities.getArtifact().getId());

        return result;


    }

    public List<Artifact> searchArtifacts(List<Integer> artifactIDs){
        //T
        ArrayList<Artifact> result = new ArrayList<>();
        result.add(Utilities.getArtifact());
        result.add(Utilities.getArtifact());
        result.add(Utilities.getArtifact());

        return result;
    }
}
