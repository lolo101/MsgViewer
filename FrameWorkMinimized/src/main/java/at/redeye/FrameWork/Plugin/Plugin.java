package at.redeye.FrameWork.Plugin;

public interface Plugin {

    String getName();
    String getLicenceText();
    String getChangeLog();

    void initPlugin( Object obj );

    /**
     * @return true if the external jar file can be loaded
     */
    boolean isAvailable();

    @Override
    String toString();
}
