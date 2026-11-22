/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ 
/*    */ public final class DataVersion extends Record {
/*    */   private final int version;
/*    */   
/*    */   private final String series;
/*    */   
/*    */   public static final String MAIN_SERIES = "main";
/*    */   
/*    */   public DataVersion(int version, String series) {
/*  5 */     this.version = version;
/*  5 */     this.series = series;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/DataVersion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/DataVersion;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/DataVersion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/DataVersion;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/DataVersion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/DataVersion;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int version() {
/*  5 */     return this.version;
/*    */   }
/*    */   
/*    */   public String series() {
/*  5 */     return this.series;
/*    */   }
/*    */   
/*    */   public boolean isSideSeries() {
/* 12 */     return !this.series.equals("main");
/*    */   }
/*    */   
/*    */   public boolean isCompatible(DataVersion other) {
/* 16 */     if (SharedConstants.DEBUG_OPEN_INCOMPATIBLE_WORLDS)
/* 17 */       return true; 
/* 19 */     return series().equals(other.series());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\DataVersion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */