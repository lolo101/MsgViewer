package at.redeye.FrameWork.utilities;

public class FreeMemory {

    public static String getMeminfo() {
        Runtime rt = Runtime.getRuntime();

        long fm = rt.freeMemory();
        long tm = rt.totalMemory();
        long mm = rt.maxMemory();
        long um = tm - fm;

        return
                "Free memory = " + fm / 1024 / 1024 + "m\n" +
                "Total memory = " + tm / 1024 / 1024 + "m\n" +
                "Maximum memory = " + mm / 1024 / 1024 + "m\n" +
                "Memory used = " + um / 1024 / 1024 + "m\n";
    }
}
