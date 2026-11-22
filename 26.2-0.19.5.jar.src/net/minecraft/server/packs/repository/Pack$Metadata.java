/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class Metadata extends Record {
/*    */   private final Component description;
/*    */   
/*    */   private final PackCompatibility compatibility;
/*    */   
/*    */   private final FeatureFlagSet requestedFeatures;
/*    */   
/*    */   private final List<String> overlays;
/*    */   
/*    */   public Metadata(Component description, PackCompatibility compatibility, FeatureFlagSet requestedFeatures, List<String> overlays) {
/* 36 */     this.description = description;
/* 36 */     this.compatibility = compatibility;
/* 36 */     this.requestedFeatures = requestedFeatures;
/* 36 */     this.overlays = overlays;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/repository/Pack$Metadata;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/repository/Pack$Metadata;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/repository/Pack$Metadata;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component description() {
/* 36 */     return this.description;
/*    */   }
/*    */   
/*    */   public PackCompatibility compatibility() {
/* 36 */     return this.compatibility;
/*    */   }
/*    */   
/*    */   public FeatureFlagSet requestedFeatures() {
/* 36 */     return this.requestedFeatures;
/*    */   }
/*    */   
/*    */   public List<String> overlays() {
/* 36 */     return this.overlays;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\Pack$Metadata.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */