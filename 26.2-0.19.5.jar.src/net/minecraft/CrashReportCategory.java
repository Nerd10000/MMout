/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class CrashReportCategory {
/*     */   private final String title;
/*     */   
/*  16 */   private final List<Entry> entries = Lists.newArrayList();
/*     */   
/*  17 */   private StackTraceElement[] stackTrace = new StackTraceElement[0];
/*     */   
/*     */   public CrashReportCategory(String title) {
/*  20 */     this.title = title;
/*     */   }
/*     */   
/*     */   public static String formatLocation(LevelHeightAccessor levelHeightAccessor, double x, double y, double z) {
/*  24 */     return String.format(Locale.ROOT, "%.2f,%.2f,%.2f - %s", new Object[] { Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), formatLocation(levelHeightAccessor, BlockPos.containing(x, y, z)) });
/*     */   }
/*     */   
/*     */   public static String formatLocation(LevelHeightAccessor levelHeightAccessor, BlockPos pos) {
/*  28 */     return formatLocation(levelHeightAccessor, pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public static String formatLocation(LevelHeightAccessor levelHeightAccessor, int x, int y, int z) {
/*  32 */     StringBuilder result = new StringBuilder();
/*     */     try {
/*  35 */       result.append(String.format(Locale.ROOT, "World: (%d,%d,%d)", new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z) }));
/*  36 */     } catch (Throwable ignored) {
/*  37 */       result.append("(Error finding world loc)");
/*     */     } 
/*  40 */     result.append(", ");
/*     */     try {
/*  43 */       int sectionX = SectionPos.blockToSectionCoord(x);
/*  44 */       int sectionY = SectionPos.blockToSectionCoord(y);
/*  45 */       int sectionZ = SectionPos.blockToSectionCoord(z);
/*  46 */       int relativeX = x & 0xF;
/*  47 */       int relativeY = y & 0xF;
/*  48 */       int relativeZ = z & 0xF;
/*  49 */       int minBlockX = SectionPos.sectionToBlockCoord(sectionX);
/*  50 */       int minBlockY = levelHeightAccessor.getMinY();
/*  51 */       int minBlockZ = SectionPos.sectionToBlockCoord(sectionZ);
/*  52 */       int maxBlockX = SectionPos.sectionToBlockCoord(sectionX + 1) - 1;
/*  53 */       int maxBlockY = levelHeightAccessor.getMaxY();
/*  54 */       int maxBlockZ = SectionPos.sectionToBlockCoord(sectionZ + 1) - 1;
/*  55 */       result.append(String.format(Locale.ROOT, "Section: (at %d,%d,%d in %d,%d,%d; chunk contains blocks %d,%d,%d to %d,%d,%d)", new Object[] { 
/*  55 */               Integer.valueOf(relativeX), Integer.valueOf(relativeY), Integer.valueOf(relativeZ), Integer.valueOf(sectionX), Integer.valueOf(sectionY), Integer.valueOf(sectionZ), Integer.valueOf(minBlockX), Integer.valueOf(minBlockY), Integer.valueOf(minBlockZ), Integer.valueOf(maxBlockX), 
/*  55 */               Integer.valueOf(maxBlockY), Integer.valueOf(maxBlockZ) }));
/*  56 */     } catch (Throwable ignored) {
/*  57 */       result.append("(Error finding chunk loc)");
/*     */     } 
/*  60 */     result.append(", ");
/*     */     try {
/*  63 */       int regionX = x >> 9;
/*  64 */       int regionZ = z >> 9;
/*  65 */       int minChunkX = regionX << 5;
/*  66 */       int minChunkZ = regionZ << 5;
/*  67 */       int maxChunkX = (regionX + 1 << 5) - 1;
/*  68 */       int maxChunkZ = (regionZ + 1 << 5) - 1;
/*  69 */       int minBlockX = regionX << 9;
/*  70 */       int minBlockY = levelHeightAccessor.getMinY();
/*  71 */       int minBlockZ = regionZ << 9;
/*  72 */       int maxBlockX = (regionX + 1 << 9) - 1;
/*  73 */       int maxBlockY = levelHeightAccessor.getMaxY();
/*  74 */       int maxBlockZ = (regionZ + 1 << 9) - 1;
/*  75 */       result.append(String.format(Locale.ROOT, "Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,%d,%d to %d,%d,%d)", new Object[] { 
/*  75 */               Integer.valueOf(regionX), Integer.valueOf(regionZ), Integer.valueOf(minChunkX), Integer.valueOf(minChunkZ), Integer.valueOf(maxChunkX), Integer.valueOf(maxChunkZ), Integer.valueOf(minBlockX), Integer.valueOf(minBlockY), Integer.valueOf(minBlockZ), Integer.valueOf(maxBlockX), 
/*  75 */               Integer.valueOf(maxBlockY), Integer.valueOf(maxBlockZ) }));
/*  76 */     } catch (Throwable ignored) {
/*  77 */       result.append("(Error finding world loc)");
/*     */     } 
/*  80 */     return result.toString();
/*     */   }
/*     */   
/*     */   public CrashReportCategory setDetail(String key, CrashReportDetail<String> callback) {
/*     */     try {
/*  85 */       setDetail(key, callback.call());
/*  86 */     } catch (Throwable t) {
/*  87 */       setDetailError(key, t);
/*     */     } 
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public CrashReportCategory setDetail(String key, Object value) {
/*  93 */     this.entries.add(new Entry(key, value));
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public void setDetailError(String key, Throwable t) {
/*  98 */     setDetail(key, t);
/*     */   }
/*     */   
/*     */   public int fillInStackTrace(int nestedOffset) {
/* 102 */     StackTraceElement[] full = Thread.currentThread().getStackTrace();
/* 105 */     if (full.length <= 0)
/* 106 */       return 0; 
/* 109 */     this.stackTrace = new StackTraceElement[full.length - 3 - nestedOffset];
/* 110 */     System.arraycopy(full, 3 + nestedOffset, this.stackTrace, 0, this.stackTrace.length);
/* 111 */     return this.stackTrace.length;
/*     */   }
/*     */   
/*     */   public boolean validateStackTrace(StackTraceElement source, StackTraceElement next) {
/* 115 */     if (this.stackTrace.length == 0 || source == null)
/* 116 */       return false; 
/* 119 */     StackTraceElement current = this.stackTrace[0];
/* 122 */     if (current.isNativeMethod() != source.isNativeMethod() || 
/* 123 */       !Objects.equals(current.getClassName(), source.getClassName()) || 
/* 124 */       !Objects.equals(current.getFileName(), source.getFileName()) || 
/* 125 */       !Objects.equals(current.getMethodName(), source.getMethodName()))
/* 127 */       return false; 
/* 130 */     if (((next != null) ? true : false) != ((this.stackTrace.length > 1) ? true : false))
/* 131 */       return false; 
/* 133 */     if (next != null && !this.stackTrace[1].equals(next))
/* 134 */       return false; 
/* 137 */     this.stackTrace[0] = source;
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   public void getDetails(StringBuilder builder) {
/* 143 */     builder.append("-- ").append(this.title).append(" --\n");
/* 144 */     builder.append("Details:");
/* 146 */     for (Entry entry : this.entries) {
/* 147 */       builder.append("\n\t");
/* 148 */       builder.append(entry.key());
/* 149 */       builder.append(": ");
/* 150 */       builder.append(entry.value());
/*     */     } 
/* 153 */     if (this.stackTrace.length > 0) {
/* 154 */       builder.append("\nStacktrace:");
/* 156 */       for (StackTraceElement element : this.stackTrace) {
/* 157 */         builder.append("\n\tat ");
/* 158 */         builder.append(element);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getStacktrace() {
/* 164 */     return this.stackTrace;
/*     */   }
/*     */   
/*     */   public static void populateBlockDetails(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor, BlockPos pos, BlockState state) {
/* 168 */     Objects.requireNonNull(state);
/* 168 */     category.setDetail("Block", state::toString);
/* 170 */     populateBlockLocationDetails(category, levelHeightAccessor, pos);
/*     */   }
/*     */   
/*     */   public static CrashReportCategory populateBlockLocationDetails(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor, BlockPos pos) {
/* 174 */     return category.setDetail("Block location", () -> formatLocation(levelHeightAccessor, pos));
/*     */   }
/*     */   
/*     */   public static final class Entry extends Record {
/*     */     private final String key;
/*     */     
/*     */     private final String value;
/*     */     
/*     */     public String value() {
/* 177 */       return this.value;
/*     */     }
/*     */     
/*     */     public String key() {
/* 177 */       return this.key;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/CrashReportCategory$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/CrashReportCategory$Entry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/CrashReportCategory$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/CrashReportCategory$Entry;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/CrashReportCategory$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/CrashReportCategory$Entry;
/*     */     }
/*     */     
/*     */     public Entry(String key, String value) {
/* 177 */       this.key = key;
/* 177 */       this.value = value;
/*     */     }
/*     */     
/*     */     public Entry(String key, Object rawValue) {
/* 187 */       this(key, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\CrashReportCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */