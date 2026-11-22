/*    */ package net.minecraft.world.level.chunk.status;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ThreadedLevelLightEngine;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public final class WorldGenContext extends Record {
/*    */   private final ServerLevel level;
/*    */   
/*    */   private final ChunkGenerator generator;
/*    */   
/*    */   private final StructureTemplateManager structureManager;
/*    */   
/*    */   private final ThreadedLevelLightEngine lightEngine;
/*    */   
/*    */   private final Executor mainThreadExecutor;
/*    */   
/*    */   private final LevelChunk.UnsavedListener unsavedListener;
/*    */   
/*    */   public WorldGenContext(ServerLevel level, ChunkGenerator generator, StructureTemplateManager structureManager, ThreadedLevelLightEngine lightEngine, Executor mainThreadExecutor, LevelChunk.UnsavedListener unsavedListener) {
/* 11 */     this.level = level;
/* 11 */     this.generator = generator;
/* 11 */     this.structureManager = structureManager;
/* 11 */     this.lightEngine = lightEngine;
/* 11 */     this.mainThreadExecutor = mainThreadExecutor;
/* 11 */     this.unsavedListener = unsavedListener;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/status/WorldGenContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/WorldGenContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/status/WorldGenContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/WorldGenContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/status/WorldGenContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/status/WorldGenContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ServerLevel level() {
/* 11 */     return this.level;
/*    */   }
/*    */   
/*    */   public ChunkGenerator generator() {
/* 11 */     return this.generator;
/*    */   }
/*    */   
/*    */   public StructureTemplateManager structureManager() {
/* 11 */     return this.structureManager;
/*    */   }
/*    */   
/*    */   public ThreadedLevelLightEngine lightEngine() {
/* 11 */     return this.lightEngine;
/*    */   }
/*    */   
/*    */   public Executor mainThreadExecutor() {
/* 11 */     return this.mainThreadExecutor;
/*    */   }
/*    */   
/*    */   public LevelChunk.UnsavedListener unsavedListener() {
/* 11 */     return this.unsavedListener;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\status\WorldGenContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */