public interface Router {

    public void setTypography(Topography t);

    public Link getNextStep(Node src, Packet packet);

    public void updateRouter();

}
