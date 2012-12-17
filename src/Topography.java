import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mdolan
 * Date: 12/17/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Topography {

    public List<Node> getNodes();

    public List<Link> getLinks(Node node);

    public Node getRandomNode();

    public void updateTypography();

}
