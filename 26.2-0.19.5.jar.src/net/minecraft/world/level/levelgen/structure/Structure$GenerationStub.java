/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*     */ 
/*     */ public final class GenerationStub extends Record {
/*     */   private final BlockPos position;
/*     */   
/*     */   private final Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator;
/*     */   
/*     */   public GenerationStub(BlockPos position, Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator) {
/* 150 */     this.position = position;
/* 150 */     this.generator = generator;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #150	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #150	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #150	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos position() {
/* 150 */     return this.position;
/*     */   }
/*     */   
/*     */   public Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator() {
/* 150 */     return this.generator;
/*     */   }
/*     */   
/*     */   public GenerationStub(BlockPos position, Consumer<StructurePiecesBuilder> generator) {
/* 152 */     this(position, Either.left(generator));
/*     */   }
/*     */   
/*     */   public StructurePiecesBuilder getPiecesBuilder() {
/* 156 */     return (StructurePiecesBuilder)this.generator.map(pieceGenerator -> {
/*     */           StructurePiecesBuilder newBuilder = new StructurePiecesBuilder();
/*     */           pieceGenerator.accept(newBuilder);
/*     */           return newBuilder;
/*     */         }previousBuilder -> previousBuilder);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\Structure$GenerationStub.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */