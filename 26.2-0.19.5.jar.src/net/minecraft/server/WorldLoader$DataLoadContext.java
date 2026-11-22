/*    */ package net.minecraft.server;
/*    */ 
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.world.level.WorldDataConfiguration;
/*    */ 
/*    */ public final class DataLoadContext extends Record {
/*    */   private final ResourceManager resources;
/*    */   
/*    */   private final WorldDataConfiguration dataConfiguration;
/*    */   
/*    */   private final HolderLookup.Provider datapackWorldgen;
/*    */   
/*    */   private final RegistryAccess.Frozen datapackDimensions;
/*    */   
/*    */   public DataLoadContext(ResourceManager resources, WorldDataConfiguration dataConfiguration, HolderLookup.Provider datapackWorldgen, RegistryAccess.Frozen datapackDimensions) {
/* 66 */     this.resources = resources;
/* 66 */     this.dataConfiguration = dataConfiguration;
/* 66 */     this.datapackWorldgen = datapackWorldgen;
/* 66 */     this.datapackDimensions = datapackDimensions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$DataLoadContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$DataLoadContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$DataLoadContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceManager resources() {
/* 66 */     return this.resources;
/*    */   }
/*    */   
/*    */   public WorldDataConfiguration dataConfiguration() {
/* 66 */     return this.dataConfiguration;
/*    */   }
/*    */   
/*    */   public HolderLookup.Provider datapackWorldgen() {
/* 66 */     return this.datapackWorldgen;
/*    */   }
/*    */   
/*    */   public RegistryAccess.Frozen datapackDimensions() {
/* 66 */     return this.datapackDimensions;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\WorldLoader$DataLoadContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */