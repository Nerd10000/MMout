/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public final class StructurePieceSerializationContext extends Record {
/*    */   private final ResourceManager resourceManager;
/*    */   
/*    */   private final RegistryAccess registryAccess;
/*    */   
/*    */   private final StructureTemplateManager structureTemplateManager;
/*    */   
/*    */   public StructurePieceSerializationContext(ResourceManager resourceManager, RegistryAccess registryAccess, StructureTemplateManager structureTemplateManager) {
/*  9 */     this.resourceManager = resourceManager;
/*  9 */     this.registryAccess = registryAccess;
/*  9 */     this.structureTemplateManager = structureTemplateManager;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceManager resourceManager() {
/*  9 */     return this.resourceManager;
/*    */   }
/*    */   
/*    */   public RegistryAccess registryAccess() {
/*  9 */     return this.registryAccess;
/*    */   }
/*    */   
/*    */   public StructureTemplateManager structureTemplateManager() {
/*  9 */     return this.structureTemplateManager;
/*    */   }
/*    */   
/*    */   public static StructurePieceSerializationContext fromLevel(ServerLevel level) {
/* 15 */     MinecraftServer server = level.getServer();
/* 16 */     return new StructurePieceSerializationContext(server
/* 17 */         .getResourceManager(), (RegistryAccess)server
/* 18 */         .registryAccess(), server
/* 19 */         .getStructureManager());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pieces\StructurePieceSerializationContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */