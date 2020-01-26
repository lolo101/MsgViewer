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
			str.append("Heap Memory Usage: ").append(memorymbean.getHeapMemoryUsage()).append("\n");
			str.append("Non-Heap Memory Usage: ").append(memorymbean.getNonHeapMemoryUsage()).append("\n");

			// Read Garbage Collection information
			List<GarbageCollectorMXBean> gcmbeans = ManagementFactory
					.getGarbageCollectorMXBeans();
			for (GarbageCollectorMXBean gcmbean : gcmbeans) {
				str.append("\nName: ").append(gcmbean.getName()).append("\n");
				str.append("Collection count: ").append(gcmbean.getCollectionCount()).append("\n");
				str.append("Collection time: ").append(gcmbean.getCollectionTime()).append("\n");
				str.append("Memory Pools:\n");
				String[] memoryPoolNames = gcmbean.getMemoryPoolNames();
				for (int i = 0; i < memoryPoolNames.length; i++) {
					str.append("\t").append(memoryPoolNames[i]).append("\n");
				}
			}
			// Read Memory Pool Information
			str.append("\nMemory Pools Info" + "\n");
			List<MemoryPoolMXBean> mempoolsmbeans = ManagementFactory
					.getMemoryPoolMXBeans();
			for (MemoryPoolMXBean mempoolmbean : mempoolsmbeans) {
				str.append("Name: ").append(mempoolmbean.getName()).append("\n");
				str.append("Usage: ").append(mempoolmbean.getUsage()).append("\n");
				str.append("Collection Usage: ").append(mempoolmbean.getCollectionUsage()).append("\n");
				str.append("Peak Usage: ").append(mempoolmbean.getPeakUsage()).append("\n");
				str.append("Type: ").append(mempoolmbean.getType()).append("\n");
				str.append("Memory Manager Names: " + "\n");
				String[] memManagerNames = mempoolmbean.getMemoryManagerNames();
				for (int i = 0; i < memManagerNames.length; i++) {
					str.append("\t").append(memManagerNames[i]).append("\n");
				}
				str.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str.toString();

	}

}
