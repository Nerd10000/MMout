/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HexFormat;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.util.NativeModuleLister;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import org.apache.commons.io.output.TeeOutputStream;
/*     */ import org.lwjgl.Version;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.openal.ALC;
/*     */ import org.lwjgl.opengl.GL;
/*     */ import org.lwjgl.stb.STBImage;
/*     */ import org.lwjgl.system.Configuration;
/*     */ import org.lwjgl.system.Library;
/*     */ import org.lwjgl.system.Platform;
/*     */ import org.lwjgl.util.freetype.FreeType;
/*     */ import org.lwjgl.util.shaderc.Shaderc;
/*     */ import org.lwjgl.util.spvc.Spvc;
/*     */ import org.lwjgl.util.tinyfd.TinyFileDialogs;
/*     */ import org.lwjgl.util.vma.Vma;
/*     */ import org.lwjgl.vulkan.VK;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class NativeLibrariesBootstrap {
/*  57 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  58 */   private static final HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();
/*     */   
/*     */   private static boolean vulkanLoaderAvailable;
/*     */   
/*     */   public static void loadLibraries() throws IOException {
/*  63 */     Stopwatch stopwatch = Stopwatch.createStarted();
/*  65 */     configureLWJGLLibraryPath();
/*  67 */     createAndCheckDirectory((String)Configuration.SHARED_LIBRARY_EXTRACT_PATH.get(""));
/*  68 */     createAndCheckDirectory(System.getProperty("jna.tmpdir", ""));
/*  69 */     createAndCheckDirectory(System.getProperty("io.netty.native.workdir", ""));
/*  70 */     Boolean originalDebugLoader = (Boolean)Configuration.DEBUG_LOADER.get();
/*  72 */     Configuration.DEBUG_LOADER.set(Boolean.valueOf(true));
/*  73 */     Supplier<String> stopCapturing = setupLWJGLCapture();
/*  74 */     int libraryIndex = -1;
/*  75 */     List<LibraryLoadEntry> entries = new ArrayList<>();
/*     */     try {
/*  77 */       if (SharedConstants.DEBUG_SIMULATE_LIBRARY_LOAD_FAILURE)
/*  78 */         throw new UnsatisfiedLinkError("Simulated debug crash"); 
/*  83 */       loadLibrary(stopCapturing, "LWJGL system", NativeLibrariesBootstrap::loadLWJGLSystem);
/*  85 */       vulkanLoaderAvailable = tryLoadingVulkan();
/*  87 */       entries.add(new LibraryLoadEntry("GLFW", NativeLibrariesBootstrap::loadGlfw));
/*  88 */       entries.add(new LibraryLoadEntry("OpenGL", NativeLibrariesBootstrap::loadOpenGL));
/*  89 */       entries.add(new LibraryLoadEntry("OpenAL", NativeLibrariesBootstrap::loadOpenAL));
/*  90 */       entries.add(new LibraryLoadEntry("STB", NativeLibrariesBootstrap::loadSTB));
/*  91 */       entries.add(new LibraryLoadEntry("tinyfd", NativeLibrariesBootstrap::loadTinyFD));
/*  92 */       entries.add(new LibraryLoadEntry("freetype", NativeLibrariesBootstrap::loadFreeType));
/*  93 */       if (vulkanLoaderAvailable) {
/*  95 */         entries.add(new LibraryLoadEntry("shaderc", NativeLibrariesBootstrap::loadShaderc));
/*  96 */         entries.add(new LibraryLoadEntry("spvc", NativeLibrariesBootstrap::loadSpvc));
/*  97 */         entries.add(new LibraryLoadEntry("vma", NativeLibrariesBootstrap::loadVma));
/*     */       } 
/* 101 */       Collections.shuffle(entries);
/* 103 */       for (libraryIndex = 0; libraryIndex < entries.size(); libraryIndex++) {
/* 104 */         LibraryLoadEntry e = entries.get(libraryIndex);
/* 105 */         loadLibrary(stopCapturing, e.name(), e.loader());
/*     */       } 
/* 107 */     } catch (Throwable t) {
/* 108 */       CrashReport crashReport = CrashReport.forThrowable(t, "Loading libraries");
/* 110 */       CrashReportCategory librariesLoaded = crashReport.addCategory("Libraries loaded");
/* 111 */       librariesLoaded.setDetail("Loading order", () -> (String)entries.stream().map(LibraryLoadEntry::name).collect(Collectors.joining(",")));
/* 112 */       librariesLoaded.setDetail("Loading index", Integer.toString(libraryIndex));
/* 113 */       throw new ReportedException(crashReport);
/*     */     } finally {
/* 115 */       stopCapturing.get();
/* 116 */       Configuration.DEBUG_LOADER.set(originalDebugLoader);
/*     */     } 
/* 118 */     long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
/* 119 */     LOGGER.debug("Library load time: {} ms", Long.valueOf(elapsed));
/*     */   }
/*     */   
/*     */   private static void createAndCheckDirectory(String libraryDir) throws IOException {
/* 123 */     if (libraryDir.isEmpty())
/*     */       return; 
/* 128 */     Path libraryDirPath = Path.of(libraryDir, new String[0]);
/* 130 */     Files.createDirectories(libraryDirPath, (FileAttribute<?>[])new FileAttribute[0]);
/* 132 */     RandomSource randomSource = RandomSource.createThreadLocalInstance();
/* 135 */     String trollFileName = System.mapLibraryName("VeryImportant" + randomSource.nextInt(9999));
/* 136 */     Path probeFile = libraryDirPath.resolve(trollFileName);
/* 139 */     byte[] expectedBytes = Ints.toByteArray(randomSource.nextInt());
/* 140 */     Files.write(probeFile, expectedBytes, new java.nio.file.OpenOption[0]);
/*     */     try {
/* 143 */       FileChannel fc = FileChannel.open(probeFile, new java.nio.file.OpenOption[0]);
/*     */       try {
/* 144 */         FileLock lock = tryLock(fc);
/*     */         try {
/* 145 */           if (lock == null)
/* 146 */             throw new IOException("Failed to lock " + String.valueOf(probeFile)); 
/* 148 */           byte[] readBytes = Channels.newInputStream(fc).readAllBytes();
/* 149 */           if (!Arrays.equals(expectedBytes, readBytes))
/* 150 */             throw new IOException("Unexpected probe file contents, expected '" + HEX_FORMAT.formatHex(expectedBytes) + "', but got '" + HEX_FORMAT.formatHex(readBytes) + "'"); 
/* 152 */           if (lock != null)
/* 152 */             lock.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (lock != null)
/*     */             try {
/*     */               lock.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 153 */         if (fc != null)
/* 153 */           fc.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (fc != null)
/*     */           try {
/*     */             fc.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } finally {
/* 154 */       Files.delete(probeFile);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static FileLock tryLock(FileChannel fc) throws IOException {
/* 160 */     for (int i = 0; i < 5; i++) {
/* 161 */       FileLock lock = fc.tryLock(0L, Long.MAX_VALUE, true);
/* 162 */       if (lock != null)
/* 163 */         return lock; 
/*     */       try {
/* 166 */         Thread.sleep(10L);
/* 167 */       } catch (InterruptedException interruptedException) {
/*     */         break;
/*     */       } 
/*     */     } 
/* 171 */     return null;
/*     */   }
/*     */   
/*     */   private static void configureLWJGLLibraryPath() {
/* 177 */     String libraryPathString = (String)Configuration.SHARED_LIBRARY_EXTRACT_PATH.get();
/* 178 */     if (libraryPathString != null) {
/* 179 */       String version = Version.getVersion().replace(' ', '-');
/* 180 */       String arch = Platform.getArchitecture().name().toLowerCase(Locale.ROOT);
/* 181 */       Path newLibraryDir = Path.of(libraryPathString, new String[0]).resolve(version, new String[] { arch });
/* 182 */       Configuration.SHARED_LIBRARY_EXTRACT_PATH.set(newLibraryDir.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   @SuppressForbidden(reason = "System.out needed before bootstrap")
/*     */   private static Supplier<String> setupLWJGLCapture() {
/* 188 */     if (Configuration.DEBUG_STREAM.get() != null || ((Boolean)Configuration.DEBUG.get(Boolean.valueOf(false))).booleanValue())
/* 190 */       return () -> "<LWJGL debug enabled, not capturing>"; 
/* 192 */     CapturingPrintStream capturingPrintStream = new CapturingPrintStream(System.out);
/* 193 */     Configuration.DEBUG_STREAM.set(capturingPrintStream);
/* 194 */     capturingPrintStream.startCapturing();
/* 195 */     Objects.requireNonNull(capturingPrintStream);
/* 195 */     return capturingPrintStream::stopCapturing;
/*     */   }
/*     */   
/*     */   private static void loadLibrary(Supplier<String> debugCapture, String name, Runnable loader) {
/*     */     try {
/* 200 */       LOGGER.debug("Loading {}", name);
/* 201 */       loader.run();
/* 202 */     } catch (Throwable t) {
/* 203 */       CrashReport crashReport = CrashReport.forThrowable(t, "Loading library " + name);
/* 204 */       CrashReportCategory libraryInfoCategory = crashReport.addCategory("Library directory contents");
/* 205 */       String systemPropertyDir = System.getProperty("java.library.path", "");
/* 207 */       String lwjglPropertyDir = (String)Configuration.LIBRARY_PATH.get("");
/* 208 */       if (systemPropertyDir.equals(lwjglPropertyDir)) {
/* 209 */         libraryInfoCategory.setDetail("Contents of shared library directory", () -> listLibrariesDirectory(systemPropertyDir));
/*     */       } else {
/* 211 */         libraryInfoCategory.setDetail("Contents of java.library.path ", () -> listLibrariesDirectory(systemPropertyDir));
/* 212 */         libraryInfoCategory.setDetail("Contents of org.lwjgl.librarypath", () -> listLibrariesDirectory(lwjglPropertyDir));
/*     */       } 
/* 215 */       libraryInfoCategory.setDetail("LWJGL platform", () -> Platform.get().toString());
/* 216 */       libraryInfoCategory.setDetail("LWJGL architecture", () -> Platform.getArchitecture().toString());
/* 218 */       CrashReportCategory lwjglDebugLog = crashReport.addCategory("LWJGL debug log");
/*     */       try {
/* 221 */         lwjglDebugLog.setDetail("Log", debugCapture.get());
/* 222 */       } catch (Throwable e) {
/* 223 */         lwjglDebugLog.setDetail("Log", e);
/*     */       } 
/* 226 */       throw new ReportedException(crashReport);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String listLibrariesDirectory(String libraryDirProperty) throws IOException {
/* 233 */     if (libraryDirProperty == null || libraryDirProperty.isEmpty())
/* 234 */       return "<not set>"; 
/* 239 */     if (libraryDirProperty.contains(";"))
/* 240 */       return "<multiple directories>"; 
/* 243 */     Path libraryDirPath = Path.of(libraryDirProperty, new String[0]);
/* 244 */     if (!Files.isDirectory(libraryDirPath, new java.nio.file.LinkOption[0]))
/* 245 */       return "<not a directory>"; 
/* 248 */     List<Pair<Path, String>> contents = new ArrayList<>();
/* 249 */     DirectoryStream<Path> libraryDir = Files.newDirectoryStream(libraryDirPath);
/*     */     try {
/* 250 */       for (Path dirEntry : libraryDir)
/* 251 */         contents.add(Pair.of(dirEntry, identifyFileContents(dirEntry))); 
/* 253 */       if (libraryDir != null)
/* 253 */         libraryDir.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (libraryDir != null)
/*     */         try {
/*     */           libraryDir.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 254 */     if (contents.isEmpty())
/* 255 */       return "<empty>"; 
/* 257 */     return "\n" + (String)contents.stream().map(s -> "\t\t" + String.valueOf(((Path)s.getFirst()).getFileName()) + ": " + (String)s.getSecond()).collect(Collectors.joining("\n"));
/*     */   }
/*     */   
/*     */   private static String identifyFileContents(Path path) {
/*     */     try {
/* 262 */       if (Files.isRegularFile(path, new java.nio.file.LinkOption[0])) {
/* 264 */         if (path.getFileName().toString().endsWith(".dll")) {
/* 265 */           Optional<String> detailedModuleInfo = NativeModuleLister.tryGetModuleVersion(path.toString()).map(NativeModuleLister.NativeModuleVersion::toString);
/* 266 */           if (detailedModuleInfo.isPresent())
/* 267 */             return "module: " + (String)detailedModuleInfo.get(); 
/*     */         } 
/* 270 */         return Objects.<String>requireNonNullElse(Files.probeContentType(path), "unknown type");
/*     */       } 
/* 272 */       return "not a file";
/* 273 */     } catch (Throwable e) {
/* 274 */       LOGGER.warn("Failed to get details of file {}", path, e);
/* 275 */       return "error: " + e.getMessage();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void loadLWJGLSystem() {
/* 280 */     Library.initialize();
/*     */   }
/*     */   
/*     */   private static void loadGlfw() {
/* 284 */     Objects.requireNonNull(GLFW.getLibrary());
/*     */   }
/*     */   
/*     */   private static void loadOpenGL() {
/* 288 */     Objects.requireNonNull(GL.getFunctionProvider());
/*     */   }
/*     */   
/*     */   private static void loadOpenAL() {
/* 292 */     Objects.requireNonNull(ALC.getFunctionProvider());
/*     */   }
/*     */   
/*     */   private static void loadSTB() {
/* 296 */     String lastStbError = STBImage.stbi_failure_reason();
/* 297 */     if (lastStbError != null)
/* 298 */       throw new IllegalStateException("No error expected, but got " + lastStbError); 
/*     */   }
/*     */   
/*     */   private static boolean tryLoadingVulkan() {
/* 305 */     if (Configuration.VULKAN_EXPLICIT_INIT.get() == null)
/* 306 */       Configuration.VULKAN_EXPLICIT_INIT.set(Boolean.valueOf(true)); 
/*     */     try {
/* 310 */       VK.create();
/* 311 */       return true;
/* 312 */     } catch (Throwable t) {
/* 315 */       LOGGER.warn("Failed to load Vulkan loader", t);
/* 316 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isVulkanLoaderAvailable() {
/* 321 */     return vulkanLoaderAvailable;
/*     */   }
/*     */   
/*     */   private static void loadShaderc() {
/* 325 */     Objects.requireNonNull(Shaderc.getLibrary());
/*     */   }
/*     */   
/*     */   private static void loadSpvc() {
/* 329 */     Objects.requireNonNull(Spvc.getLibrary());
/*     */   }
/*     */   
/*     */   private static void loadVma() {
/*     */     try {
/* 335 */       Vma.vmaDestroyAllocator(0L);
/* 336 */     } catch (NullPointerException nullPointerException) {}
/*     */   }
/*     */   
/*     */   private static void loadTinyFD() {
/* 341 */     Objects.requireNonNull(TinyFileDialogs.tinyfd_getGlobalChar("tinyfd_version"));
/*     */   }
/*     */   
/*     */   private static void loadFreeType() {
/* 345 */     Objects.requireNonNull(FreeType.getLibrary());
/*     */   }
/*     */   
/*     */   private static class CapturingStream extends OutputStream {
/*     */     private ByteArrayOutputStream buffer;
/*     */     
/*     */     public void write(byte[] b, int off, int len) {
/* 353 */       if (this.buffer != null)
/* 354 */         this.buffer.write(b, off, len); 
/*     */     }
/*     */     
/*     */     public void write(int b) {
/* 360 */       if (this.buffer != null)
/* 361 */         this.buffer.write(b); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CapturingPrintStream extends PrintStream {
/*     */     private final NativeLibrariesBootstrap.CapturingStream collector;
/*     */     
/*     */     public CapturingPrintStream(OutputStream out) {
/* 371 */       super((OutputStream)new TeeOutputStream(out, logStream), false, StandardCharsets.UTF_8);
/* 372 */       this.collector = logStream;
/*     */     }
/*     */     
/*     */     public synchronized void startCapturing() {
/* 376 */       this.collector.buffer = new ByteArrayOutputStream();
/*     */     }
/*     */     
/*     */     public synchronized String stopCapturing() {
/* 380 */       ByteArrayOutputStream buffer = this.collector.buffer;
/* 381 */       this.collector.buffer = null;
/* 382 */       if (buffer != null)
/* 383 */         return buffer.toString(StandardCharsets.UTF_8); 
/* 385 */       return "";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LibraryLoadEntry extends Record {
/*     */     private final String name;
/*     */     
/*     */     private final Runnable loader;
/*     */     
/*     */     private LibraryLoadEntry(String name, Runnable loader) {
/* 389 */       this.name = name;
/* 389 */       this.loader = loader;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #389	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #389	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #389	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/platform/NativeLibrariesBootstrap$LibraryLoadEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/* 389 */       return this.name;
/*     */     }
/*     */     
/*     */     public Runnable loader() {
/* 389 */       return this.loader;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\NativeLibrariesBootstrap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */