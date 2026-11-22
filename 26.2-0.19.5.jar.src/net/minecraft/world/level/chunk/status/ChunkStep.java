/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.server.level.GenerationChunkHolder;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ 
/*     */ public final class ChunkStep extends Record {
/*     */   private final ChunkStatus targetStatus;
/*     */   
/*     */   private final ChunkDependencies directDependencies;
/*     */   
/*     */   private final ChunkDependencies accumulatedDependencies;
/*     */   
/*     */   private final int blockStateWriteRadius;
/*     */   
/*     */   private final ChunkStatusTask task;
/*     */   
/*     */   public ChunkStep(ChunkStatus targetStatus, ChunkDependencies directDependencies, ChunkDependencies accumulatedDependencies, int blockStateWriteRadius, ChunkStatusTask task) {
/*  15 */     this.targetStatus = targetStatus;
/*  15 */     this.directDependencies = directDependencies;
/*  15 */     this.accumulatedDependencies = accumulatedDependencies;
/*  15 */     this.blockStateWriteRadius = blockStateWriteRadius;
/*  15 */     this.task = task;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/status/ChunkStep;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/ChunkStep;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/status/ChunkStep;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/ChunkStep;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/status/ChunkStep;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/status/ChunkStep;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ChunkStatus targetStatus() {
/*  15 */     return this.targetStatus;
/*     */   }
/*     */   
/*     */   public ChunkDependencies directDependencies() {
/*  15 */     return this.directDependencies;
/*     */   }
/*     */   
/*     */   public ChunkDependencies accumulatedDependencies() {
/*  15 */     return this.accumulatedDependencies;
/*     */   }
/*     */   
/*     */   public int blockStateWriteRadius() {
/*  15 */     return this.blockStateWriteRadius;
/*     */   }
/*     */   
/*     */   public ChunkStatusTask task() {
/*  15 */     return this.task;
/*     */   }
/*     */   
/*     */   public int getAccumulatedRadiusOf(ChunkStatus status) {
/*  23 */     if (status == this.targetStatus)
/*  24 */       return 0; 
/*  26 */     return this.accumulatedDependencies.getRadiusOf(status);
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkAccess> apply(WorldGenContext context, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk) {
/*  30 */     if (chunk.getPersistedStatus().isBefore(this.targetStatus)) {
/*  31 */       ProfiledDuration profiledDuration = JvmProfiler.INSTANCE.onChunkGenerate(chunk.getPos(), context.level().dimension(), this.targetStatus.getName());
/*  33 */       return this.task.doWork(context, this, cache, chunk).thenApply(newCenterChunk -> completeChunkGeneration(newCenterChunk, profiledDuration));
/*     */     } 
/*  35 */     return this.task.doWork(context, this, cache, chunk);
/*     */   }
/*     */   
/*     */   private ChunkAccess completeChunkGeneration(ChunkAccess newCenterChunk, ProfiledDuration profiledDuration) {
/*  40 */     if (newCenterChunk instanceof ProtoChunk) {
/*  40 */       ProtoChunk protochunk = (ProtoChunk)newCenterChunk;
/*  40 */       if (protochunk.getPersistedStatus().isBefore(this.targetStatus))
/*  41 */         protochunk.setPersistedStatus(this.targetStatus); 
/*     */     } 
/*  43 */     if (profiledDuration != null)
/*  44 */       profiledDuration.finish(true); 
/*  46 */     return newCenterChunk;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final ChunkStatus status;
/*     */     
/*     */     private final ChunkStep parent;
/*     */     
/*     */     private ChunkStatus[] directDependenciesByRadius;
/*     */     
/*  54 */     private int blockStateWriteRadius = -1;
/*     */     
/*  55 */     private ChunkStatusTask task = ChunkStatusTasks::passThrough;
/*     */     
/*     */     protected Builder(ChunkStatus status) {
/*  58 */       if (status.getParent() != status)
/*  59 */         throw new IllegalArgumentException("Not starting with the first status: " + String.valueOf(status)); 
/*  61 */       this.status = status;
/*  62 */       this.parent = null;
/*  63 */       this.directDependenciesByRadius = new ChunkStatus[0];
/*     */     }
/*     */     
/*     */     protected Builder(ChunkStatus status, ChunkStep parent) {
/*  67 */       if (parent.targetStatus.getIndex() != status.getIndex() - 1)
/*  68 */         throw new IllegalArgumentException("Out of order status: " + String.valueOf(status)); 
/*  70 */       this.status = status;
/*  71 */       this.parent = parent;
/*  72 */       this.directDependenciesByRadius = new ChunkStatus[] { parent.targetStatus };
/*     */     }
/*     */     
/*     */     public Builder addRequirement(ChunkStatus status, int radius) {
/*  76 */       if (status.isOrAfter(this.status))
/*  77 */         throw new IllegalArgumentException("Status " + String.valueOf(status) + " can not be required by " + String.valueOf(this.status)); 
/*  79 */       ChunkStatus[] previous = this.directDependenciesByRadius;
/*  80 */       int newLength = radius + 1;
/*  81 */       if (newLength > previous.length) {
/*  82 */         this.directDependenciesByRadius = new ChunkStatus[newLength];
/*  83 */         Arrays.fill((Object[])this.directDependenciesByRadius, status);
/*     */       } 
/*  85 */       for (int i = 0; i < Math.min(newLength, previous.length); i++)
/*  86 */         this.directDependenciesByRadius[i] = ChunkStatus.max(previous[i], status); 
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public Builder blockStateWriteRadius(int radius) {
/*  92 */       this.blockStateWriteRadius = radius;
/*  93 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTask(ChunkStatusTask task) {
/*  97 */       this.task = task;
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     public ChunkStep build() {
/* 102 */       return new ChunkStep(this.status, new ChunkDependencies(ImmutableList.copyOf((Object[])this.directDependenciesByRadius)), new ChunkDependencies(ImmutableList.copyOf((Object[])buildAccumulatedDependencies())), this.blockStateWriteRadius, this.task);
/*     */     }
/*     */     
/*     */     private ChunkStatus[] buildAccumulatedDependencies() {
/* 106 */       if (this.parent == null)
/* 107 */         return this.directDependenciesByRadius; 
/* 109 */       int radiusOfParent = getRadiusOfParent(this.parent.targetStatus);
/* 110 */       ChunkDependencies parentDependencies = this.parent.accumulatedDependencies;
/* 111 */       ChunkStatus[] accumulatedDependencies = new ChunkStatus[Math.max(radiusOfParent + parentDependencies.size(), this.directDependenciesByRadius.length)];
/* 112 */       for (int distance = 0; distance < accumulatedDependencies.length; distance++) {
/* 113 */         int distanceInParent = distance - radiusOfParent;
/* 114 */         if (distanceInParent < 0 || distanceInParent >= parentDependencies.size()) {
/* 115 */           accumulatedDependencies[distance] = this.directDependenciesByRadius[distance];
/* 116 */         } else if (distance >= this.directDependenciesByRadius.length) {
/* 117 */           accumulatedDependencies[distance] = parentDependencies.get(distanceInParent);
/*     */         } else {
/* 119 */           accumulatedDependencies[distance] = ChunkStatus.max(this.directDependenciesByRadius[distance], parentDependencies.get(distanceInParent));
/*     */         } 
/*     */       } 
/* 122 */       return accumulatedDependencies;
/*     */     }
/*     */     
/*     */     private int getRadiusOfParent(ChunkStatus status) {
/* 126 */       for (int i = this.directDependenciesByRadius.length - 1; i >= 0; i--) {
/* 127 */         if (this.directDependenciesByRadius[i].isOrAfter(status))
/* 128 */           return i; 
/*     */       } 
/* 131 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\status\ChunkStep.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */