package at.redeye.FrameWork.utilities;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

public class MemoryInformation {

	public static String createMemoryInfo() {

		StringBuilder str = new StringBuilder();

		try {
			str.append("\nMEMORY INFO\n");
			// Read MemoryMXBean
			MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
			str.append("Heap Memory Usage: " + memorymbean.getHeapMemoryUsage()
					+ "\n");
			str.append("Non-Heap Memory Usage: "
					+ memorymbean.getNonHeapMemoryUsage() + "\n");

			// Read Garbage Collection information
			List<GarbageCollectorMXBean> gcmbeans = ManagementFactory
					.getGarbageCollectorMXBeans();
			for (GarbageCollectorMXBean gcmbean : gcmbeans) {
				str.append("\nName: " + gcmbean.getName() + "\n");
				str.append("Collection count: " + gcmbean.getCollectionCount()
						+ "\n");
				str.append("Collection time: " + gcmbean.getCollectionTime()
						+ "\n");
				str.append("Memory Pools:\n");
				String[] memoryPoolNames = gcmbean.getMemoryPoolNames();
				for (int i = 0; i < memoryPoolNames.length; i++) {
					str.append("\t" + memoryPoolNames[i] + "\n");
				}
			}
			// Read Memory Pool Information
			str.append("\nMemory Pools Info" + "\n");
			List<MemoryPoolMXBean> mempoolsmbeans = ManagementFactory
					.getMemoryPoolMXBeans();
			for (MemoryPoolMXBean mempoolmbean : mempoolsmbeans) {
				str.append("Name: " + mempoolmbean.getName() + "\n");
				str.append("Usage: " + mempoolmbean.getUsage() + "\n");
				str.append("Collection Usage: "
						+ mempoolmbean.getCollectionUsage() + "\n");
				str.append("Peak Usage: " + mempoolmbean.getPeakUsage() + "\n");
				str.append("Type: " + mempoolmbean.getType() + "\n");
				str.append("Memory Manager Names: " + "\n");
				String[] memManagerNames = mempoolmbean.getMemoryManagerNames();
				for (int i = 0; i < memManagerNames.length; i++) {
					str.append("\t" + memManagerNames[i] + "\n");
				}
				str.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str.toString();

	}

}
