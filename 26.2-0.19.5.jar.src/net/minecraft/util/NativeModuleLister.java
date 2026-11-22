/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.sun.jna.Memory;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Platform;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.platform.win32.Kernel32;
/*     */ import com.sun.jna.platform.win32.Kernel32Util;
/*     */ import com.sun.jna.platform.win32.Tlhelp32;
/*     */ import com.sun.jna.platform.win32.Version;
/*     */ import com.sun.jna.platform.win32.Win32Exception;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class NativeModuleLister {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int LANG_MASK = 65535;
/*     */   
/*     */   private static final int DEFAULT_LANG = 1033;
/*     */   
/*     */   private static final int CODEPAGE_MASK = -65536;
/*     */   
/*     */   private static final int DEFAULT_CODEPAGE = 78643200;
/*     */   
/*     */   public static List<NativeModuleInfo> listModules() {
/*  38 */     if (!Platform.isWindows())
/*  39 */       return (List<NativeModuleInfo>)ImmutableList.of(); 
/*  42 */     int selfHandle = Kernel32.INSTANCE.GetCurrentProcessId();
/*  44 */     ImmutableList.Builder<NativeModuleInfo> result = ImmutableList.builder();
/*  46 */     List<Tlhelp32.MODULEENTRY32W> modules = Kernel32Util.getModules(selfHandle);
/*  48 */     for (Tlhelp32.MODULEENTRY32W module : modules) {
/*  49 */       String name = module.szModule();
/*  50 */       Optional<NativeModuleVersion> versionInfo = tryGetModuleVersion(module.szExePath());
/*  51 */       result.add(new NativeModuleInfo(name, versionInfo));
/*     */     } 
/*  54 */     return (List<NativeModuleInfo>)result.build();
/*     */   }
/*     */   
/*     */   public static Optional<NativeModuleVersion> tryGetModuleVersion(String path) {
/*  58 */     if (!Platform.isWindows())
/*  59 */       return Optional.empty(); 
/*     */     try {
/*  63 */       IntByReference dwDummy = new IntByReference();
/*  65 */       int versionLength = Version.INSTANCE.GetFileVersionInfoSize(path, dwDummy);
/*  67 */       if (versionLength == 0) {
/*  68 */         int lastError = Native.getLastError();
/*  69 */         if (lastError == 1813 || lastError == 1812)
/*  70 */           return Optional.empty(); 
/*  72 */         throw new Win32Exception(lastError);
/*     */       } 
/*  75 */       Memory memory = new Memory(versionLength);
/*  77 */       if (!Version.INSTANCE.GetFileVersionInfo(path, 0, versionLength, (Pointer)memory))
/*  78 */         throw new Win32Exception(Native.getLastError()); 
/*  81 */       IntByReference size = new IntByReference();
/*  82 */       Pointer translationsBuffer = queryVersionValue((Pointer)memory, "\\VarFileInfo\\Translation", size);
/*  83 */       int[] langsAndCodepages = translationsBuffer.getIntArray(0L, size.getValue() / 4);
/*  85 */       OptionalInt maybeLangAndCodepage = findLangAndCodepage(langsAndCodepages);
/*  86 */       if (maybeLangAndCodepage.isEmpty())
/*  87 */         return Optional.empty(); 
/*  90 */       int langAndCodepage = maybeLangAndCodepage.getAsInt();
/*  91 */       int lang = langAndCodepage & 0xFFFF;
/*  92 */       int codepage = (langAndCodepage & 0xFFFF0000) >> 16;
/*  93 */       String description = queryVersionString((Pointer)memory, langTableKey("FileDescription", lang, codepage), size);
/*  94 */       String companyName = queryVersionString((Pointer)memory, langTableKey("CompanyName", lang, codepage), size);
/*  95 */       String fileVersion = queryVersionString((Pointer)memory, langTableKey("FileVersion", lang, codepage), size);
/*  97 */       return Optional.of(new NativeModuleVersion(description, fileVersion, companyName));
/*  98 */     } catch (Exception e) {
/*  99 */       LOGGER.info("Failed to find module info for {}", path, e);
/* 101 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String langTableKey(String key, int lang, int codepage) {
/* 105 */     return String.format(Locale.ROOT, "\\StringFileInfo\\%04x%04x\\%s", new Object[] { Integer.valueOf(lang), Integer.valueOf(codepage), key });
/*     */   }
/*     */   
/*     */   private static OptionalInt findLangAndCodepage(int[] langsAndCodepages) {
/* 109 */     OptionalInt bestSoFar = OptionalInt.empty();
/* 110 */     for (int langAndCodepage : langsAndCodepages) {
/* 111 */       if ((langAndCodepage & 0xFFFF0000) == 78643200 && (
/* 112 */         langAndCodepage & 0xFFFF) == 1033)
/* 113 */         return OptionalInt.of(langAndCodepage); 
/* 116 */       bestSoFar = OptionalInt.of(langAndCodepage);
/*     */     } 
/* 118 */     return bestSoFar;
/*     */   }
/*     */   
/*     */   private static Pointer queryVersionValue(Pointer lpData, String key, IntByReference outSize) {
/* 122 */     PointerByReference lplpBuffer = new PointerByReference();
/* 123 */     if (!Version.INSTANCE.VerQueryValue(lpData, key, lplpBuffer, outSize))
/* 124 */       throw new UnsupportedOperationException("Can't get version value " + key); 
/* 126 */     return lplpBuffer.getValue();
/*     */   }
/*     */   
/*     */   private static String queryVersionString(Pointer lpData, String key, IntByReference outSize) {
/*     */     try {
/* 131 */       Pointer ptr = queryVersionValue(lpData, key, outSize);
/* 133 */       byte[] result = ptr.getByteArray(0L, (outSize.getValue() - 1) * 2);
/* 134 */       return sanitize(new String(result, StandardCharsets.UTF_16LE));
/* 135 */     } catch (Exception e) {
/* 136 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String sanitize(String input) {
/* 141 */     return Util.CONTROL_CHARACTER_ESCAPER.escape(input);
/*     */   }
/*     */   
/*     */   public static void addCrashSection(CrashReportCategory category) {
/* 145 */     category.setDetail("Modules", () -> (String)listModules().stream().sorted(Comparator.comparing(())).map(()).collect(Collectors.joining()));
/*     */   }
/*     */   
/*     */   public static final class NativeModuleVersion extends Record {
/*     */     private final String description;
/*     */     
/*     */     private final String version;
/*     */     
/*     */     private final String company;
/*     */     
/*     */     public NativeModuleVersion(String description, String version, String company) {
/* 154 */       this.description = description;
/* 154 */       this.version = version;
/* 154 */       this.company = company;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/NativeModuleLister$NativeModuleVersion;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/NativeModuleLister$NativeModuleVersion;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/NativeModuleLister$NativeModuleVersion;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/NativeModuleLister$NativeModuleVersion;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String description() {
/* 154 */       return this.description;
/*     */     }
/*     */     
/*     */     public String version() {
/* 154 */       return this.version;
/*     */     }
/*     */     
/*     */     public String company() {
/* 154 */       return this.company;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 162 */       return this.description + ":" + this.description + ":" + this.version;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class NativeModuleInfo extends Record {
/*     */     private final String name;
/*     */     
/*     */     private final Optional<NativeModuleLister.NativeModuleVersion> version;
/*     */     
/*     */     public NativeModuleInfo(String name, Optional<NativeModuleLister.NativeModuleVersion> version) {
/* 166 */       this.name = name;
/* 166 */       this.version = version;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/NativeModuleLister$NativeModuleInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/NativeModuleLister$NativeModuleInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/NativeModuleLister$NativeModuleInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/NativeModuleLister$NativeModuleInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/* 166 */       return this.name;
/*     */     }
/*     */     
/*     */     public Optional<NativeModuleLister.NativeModuleVersion> version() {
/* 166 */       return this.version;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 172 */       return this.version.<String>map(v -> this.name + ":" + this.name).orElse(this.name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\NativeModuleLister.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */