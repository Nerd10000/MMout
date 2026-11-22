/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ 
/*     */ public final class ErrorMarker extends Record {
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final Component text;
/*     */   
/*     */   public static final Codec<ErrorMarker> CODEC;
/*     */   
/*     */   public ErrorMarker(BlockPos pos, Component text) {
/* 485 */     this.pos = pos;
/* 485 */     this.text = text;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #485	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #485	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #485	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 485 */     return this.pos;
/*     */   }
/*     */   
/*     */   public Component text() {
/* 485 */     return this.text;
/*     */   }
/*     */   
/*     */   static {
/* 489 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(ErrorMarker::pos), (App)ComponentSerialization.CODEC.fieldOf("text").forGetter(ErrorMarker::text)).apply((Applicative)i, ErrorMarker::new));
/*     */   }
/*     */   
/* 494 */   public static final Codec<List<ErrorMarker>> LIST_CODEC = CODEC.listOf();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\TestInstanceBlockEntity$ErrorMarker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */