/**
 * Created with IntelliJ IDEA.
 * User: mdolan
 * Date: 12/17/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Monitor {

    public void dropped(Packet packet, FailureCondition fc);

    public void arrived(Packet packet);

}
