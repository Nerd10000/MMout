/*     */ package net.minecraft;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.slf4j.Logger;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ import oshi.hardware.GlobalMemory;
/*     */ import oshi.hardware.GraphicsCard;
/*     */ import oshi.hardware.HardwareAbstractionLayer;
/*     */ import oshi.hardware.PhysicalMemory;
/*     */ import oshi.hardware.VirtualMemory;
/*     */ import oshi.software.os.OSProcess;
/*     */ import oshi.software.os.OperatingSystem;
/*     */ 
/*     */ public class SystemReport {
/*     */   public static final long BYTES_PER_MEBIBYTE = 1048576L;
/*     */   
/*     */   private static final long ONE_GIGA = 1000000000L;
/*     */   
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  36 */   private static final String OPERATING_SYSTEM = System.getProperty("os.name") + " (" + System.getProperty("os.name") + ") version " + System.getProperty("os.arch");
/*     */   
/*  37 */   private static final String JAVA_VERSION = System.getProperty("java.version") + ", " + System.getProperty("java.version");
/*     */   
/*  38 */   private static final String JAVA_VM_VERSION = System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.name") + "), " + System.getProperty("java.vm.info");
/*     */   
/*  40 */   private final List<CrashReportCategory.Entry> entries = new ArrayList<>();
/*     */   
/*     */   public SystemReport() {
/*  43 */     setDetail("Minecraft Version", () -> SharedConstants.getCurrentVersion().name());
/*  44 */     setDetail("Minecraft Version ID", () -> SharedConstants.getCurrentVersion().id());
/*  45 */     setDetail("Operating System", OPERATING_SYSTEM);
/*  46 */     setDetail("Java Version", JAVA_VERSION);
/*  47 */     setDetail("Java VM Version", JAVA_VM_VERSION);
/*  49 */     setDetail("Memory", () -> {
/*     */           Runtime runtime = Runtime.getRuntime();
/*     */           long max = runtime.maxMemory();
/*     */           long total = runtime.totalMemory();
/*     */           long free = runtime.freeMemory();
/*     */           long maxMb = max / 1048576L;
/*     */           long totalMb = total / 1048576L;
/*     */           long freeMb = free / 1048576L;
/*     */           return "" + free + " bytes (" + free + " MiB) / " + freeMb + " bytes (" + total + " MiB) up to " + totalMb + " bytes (" + max + " MiB)";
/*     */         });
/*  61 */     setDetail("Memory (heap)", () -> printMemoryUsage(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()));
/*  62 */     setDetail("Memory (non-head)", () -> printMemoryUsage(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()));
/*  64 */     setDetail("CPUs", () -> String.valueOf(Runtime.getRuntime().availableProcessors()));
/*  66 */     ignoreErrors("hardware", () -> putHardware(new SystemInfo()));
/*  68 */     ignoreErrors("software", () -> putSoftware(new SystemInfo()));
/*  70 */     setDetail("JVM Flags", () -> printJvmFlags(()));
/*  72 */     setDetail("Debug Flags", () -> printJvmFlags(()));
/*     */   }
/*     */   
/*     */   private static String printMemoryUsage(MemoryUsage memoryUsage) {
/*  76 */     return String.format(Locale.ROOT, "init: %03dMiB, used: %03dMiB, committed: %03dMiB, max: %03dMiB", new Object[] { Long.valueOf(memoryUsage.getInit() / 1048576L), 
/*  78 */           Long.valueOf(memoryUsage.getUsed() / 1048576L), 
/*  79 */           Long.valueOf(memoryUsage.getCommitted() / 1048576L), 
/*  80 */           Long.valueOf(memoryUsage.getMax() / 1048576L) });
/*     */   }
/*     */   
/*     */   private static String printJvmFlags(Predicate<String> selector) {
/*  85 */     List<String> allArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
/*  86 */     List<String> selectedArguments = allArguments.stream().filter(selector).toList();
/*  87 */     return String.format(Locale.ROOT, "%d total; %s", new Object[] { Integer.valueOf(selectedArguments.size()), String.join(" ", (Iterable)selectedArguments) });
/*     */   }
/*     */   
/*     */   public void setDetail(String key, String value) {
/*  91 */     this.entries.add(new CrashReportCategory.Entry(key, value));
/*     */   }
/*     */   
/*     */   public void setDetail(String key, CrashReportDetail<Object> valueSupplier) {
/*     */     try {
/*  96 */       this.entries.add(new CrashReportCategory.Entry(key, valueSupplier.call()));
/*  97 */     } catch (Throwable t) {
/*  98 */       LOGGER.warn("Failed to get system info for {}", key, t);
/*  99 */       this.entries.add(new CrashReportCategory.Entry(key, t));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putHardware(SystemInfo systemInfo) {
/* 104 */     HardwareAbstractionLayer hardware = systemInfo.getHardware();
/* 105 */     ignoreErrors("processor", () -> putProcessor(hardware.getProcessor()));
/* 106 */     ignoreErrors("graphics", () -> putGraphics(hardware.getGraphicsCards()));
/* 107 */     ignoreErrors("memory", () -> putMemory(hardware.getMemory()));
/* 108 */     ignoreErrors("storage", this::putStorage);
/*     */   }
/*     */   
/*     */   private void putSoftware(SystemInfo systemInfo) {
/* 112 */     OperatingSystem os = systemInfo.getOperatingSystem();
/* 114 */     Objects.requireNonNull(os);
/* 114 */     setDetail("Operating System Version", os::toString);
/* 115 */     Objects.requireNonNull(os);
/* 115 */     setDetail("Process Elevated", os::isElevated);
/* 116 */     ignoreErrors("process", () -> putProcessDetails(os.getCurrentProcess()));
/*     */   }
/*     */   
/*     */   private void ignoreErrors(String group, Runnable action) {
/*     */     try {
/* 121 */       action.run();
/* 122 */     } catch (Throwable t) {
/* 123 */       LOGGER.warn("Failed retrieving info for group {}", group, t);
/* 124 */       this.entries.add(new CrashReportCategory.Entry(group, t));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static float sizeInMiB(long bytes) {
/* 129 */     return (float)bytes / 1048576.0F;
/*     */   }
/*     */   
/*     */   private void putPhysicalMemory(List<PhysicalMemory> memoryPackages) {
/* 133 */     int memorySlot = 0;
/* 134 */     for (PhysicalMemory physicalMemory : memoryPackages) {
/* 135 */       String prefix = String.format(Locale.ROOT, "Memory slot #%d ", new Object[] { Integer.valueOf(memorySlot++) });
/* 136 */       setDetail(prefix + "capacity (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(physicalMemory.getCapacity())) }));
/* 137 */       setDetail(prefix + "clockSpeed (GHz)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)physicalMemory.getClockSpeed() / 1.0E9F) }));
/* 138 */       Objects.requireNonNull(physicalMemory);
/* 138 */       setDetail(prefix + "type", physicalMemory::getMemoryType);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putVirtualMemory(VirtualMemory virtualMemory) {
/* 143 */     setDetail("Virtual memory max (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(virtualMemory.getVirtualMax())) }));
/* 144 */     setDetail("Virtual memory used (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(virtualMemory.getVirtualInUse())) }));
/* 145 */     setDetail("Swap memory total (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(virtualMemory.getSwapTotal())) }));
/* 146 */     setDetail("Swap memory used (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(virtualMemory.getSwapUsed())) }));
/*     */   }
/*     */   
/*     */   private void putMemory(GlobalMemory memory) {
/* 150 */     ignoreErrors("physical memory", () -> putPhysicalMemory(memory.getPhysicalMemory()));
/* 151 */     ignoreErrors("virtual memory", () -> putVirtualMemory(memory.getVirtualMemory()));
/*     */   }
/*     */   
/*     */   private void putGraphics(List<GraphicsCard> graphicsCards) {
/* 155 */     int gpuIndex = 0;
/* 156 */     for (GraphicsCard graphicsCard : graphicsCards) {
/* 157 */       String prefix = String.format(Locale.ROOT, "Graphics card #%d ", new Object[] { Integer.valueOf(gpuIndex++) });
/* 158 */       Objects.requireNonNull(graphicsCard);
/* 158 */       setDetail(prefix + "name", graphicsCard::getName);
/* 159 */       Objects.requireNonNull(graphicsCard);
/* 159 */       setDetail(prefix + "vendor", graphicsCard::getVendor);
/* 160 */       setDetail(prefix + "VRAM (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(graphicsCard.getVRam())) }));
/* 161 */       Objects.requireNonNull(graphicsCard);
/* 161 */       setDetail(prefix + "deviceId", graphicsCard::getDeviceId);
/* 162 */       Objects.requireNonNull(graphicsCard);
/* 162 */       setDetail(prefix + "versionInfo", graphicsCard::getVersionInfo);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putProcessor(CentralProcessor processor) {
/* 167 */     CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
/* 169 */     Objects.requireNonNull(processorIdentifier);
/* 169 */     setDetail("Processor Vendor", processorIdentifier::getVendor);
/* 170 */     Objects.requireNonNull(processorIdentifier);
/* 170 */     setDetail("Processor Name", processorIdentifier::getName);
/* 171 */     Objects.requireNonNull(processorIdentifier);
/* 171 */     setDetail("Identifier", processorIdentifier::getIdentifier);
/* 172 */     Objects.requireNonNull(processorIdentifier);
/* 172 */     setDetail("Microarchitecture", processorIdentifier::getMicroarchitecture);
/* 173 */     setDetail("Frequency (GHz)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)processorIdentifier.getVendorFreq() / 1.0E9F) }));
/* 175 */     setDetail("Number of physical packages", () -> String.valueOf(processor.getPhysicalPackageCount()));
/* 176 */     setDetail("Number of physical CPUs", () -> String.valueOf(processor.getPhysicalProcessorCount()));
/* 177 */     setDetail("Number of logical CPUs", () -> String.valueOf(processor.getLogicalProcessorCount()));
/*     */   }
/*     */   
/*     */   private void putStorage() {
/* 181 */     putSpaceForProperty("jna.tmpdir");
/* 182 */     putSpaceForProperty("org.lwjgl.system.SharedLibraryExtractPath");
/* 183 */     putSpaceForProperty("io.netty.native.workdir");
/* 184 */     putSpaceForProperty("java.io.tmpdir");
/* 185 */     putSpaceForPath("workdir", () -> "");
/*     */   }
/*     */   
/*     */   private void putProcessDetails(OSProcess process) {
/* 189 */     setDetail("Process Loads", () -> {
/*     */           double userTimeSeconds = process.getUserTime() / 1000.0D;
/*     */           double kernelTimeSeconds = process.getKernelTime() / 1000.0D;
/*     */           double upTimeSeconds = process.getUpTime() / 1000.0D;
/*     */           double totalTimeSeconds = kernelTimeSeconds + userTimeSeconds;
/*     */           return String.format(Locale.ROOT, "Uptime: %.0fs, user: %.0fs (%.2f%%), kernel: %.0fs (%.2f%%), total: %.0fs (%.2f%%)", new Object[] { Double.valueOf(upTimeSeconds), Double.valueOf(userTimeSeconds), Double.valueOf(userTimeSeconds / upTimeSeconds * 100.0D), Double.valueOf(kernelTimeSeconds), Double.valueOf(kernelTimeSeconds / upTimeSeconds * 100.0D), Double.valueOf(totalTimeSeconds), Double.valueOf(totalTimeSeconds / upTimeSeconds * 100.0D) });
/*     */         });
/* 202 */     setDetail("Process Virtual Size (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(process.getVirtualSize())) }));
/* 203 */     setDetail("Process Resident Size (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(process.getResidentSetSize())) }));
/*     */   }
/*     */   
/*     */   private void putSpaceForProperty(String env) {
/* 207 */     putSpaceForPath(env, () -> System.getProperty(env));
/*     */   }
/*     */   
/*     */   private void putSpaceForPath(String id, Supplier<String> pathSupplier) {
/* 211 */     String key = "Space in storage for " + id + " (MiB)";
/*     */     try {
/* 214 */       String path = pathSupplier.get();
/* 215 */       if (path == null) {
/* 216 */         setDetail(key, "<path not set>");
/*     */         return;
/*     */       } 
/* 220 */       FileStore store = Files.getFileStore(Path.of(path, new String[0]));
/* 221 */       setDetail(key, () -> String.format(Locale.ROOT, "available: %.2f, total: %.2f", new Object[] { Float.valueOf(sizeInMiB(store.getUsableSpace())), Float.valueOf(sizeInMiB(store.getTotalSpace())) }));
/* 222 */     } catch (InvalidPathException e) {
/* 223 */       LOGGER.warn("{} is not a path", id, e);
/* 224 */       setDetail(key, "<invalid path>");
/* 225 */     } catch (NoSuchFileException|java.io.FileNotFoundException e) {
/* 227 */       setDetail(key, "<no such file>");
/* 228 */     } catch (Exception e) {
/* 229 */       LOGGER.warn("Failed retrieving storage space for {}", id, e);
/* 230 */       setDetail(key, "ERR");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void appendToCrashReportString(StringBuilder sb) {
/* 235 */     sb.append("-- ").append("System Details").append(" --\n");
/* 236 */     sb.append("Details:");
/* 237 */     this.entries.forEach(entry -> {
/*     */           sb.append("\n\t");
/*     */           sb.append(entry.key());
/*     */           sb.append(": ");
/*     */           sb.append(entry.value());
/*     */         });
/*     */   }
/*     */   
/*     */   public String toLineSeparatedString() {
/* 246 */     return this.entries.stream()
/* 247 */       .map(e -> e.key() + ": " + e.key())
/* 248 */       .collect(Collectors.joining(System.lineSeparator()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\SystemReport.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */