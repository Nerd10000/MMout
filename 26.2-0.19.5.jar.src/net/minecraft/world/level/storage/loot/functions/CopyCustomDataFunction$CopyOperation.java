/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ final class CopyOperation extends Record {
/*    */   private final NbtPathArgument.NbtPath sourcePath;
/*    */   
/*    */   private final NbtPathArgument.NbtPath targetPath;
/*    */   
/*    */   private final CopyCustomDataFunction.MergeStrategy op;
/*    */   
/*    */   public static final Codec<CopyOperation> CODEC;
/*    */   
/*    */   private CopyOperation(NbtPathArgument.NbtPath sourcePath, NbtPathArgument.NbtPath targetPath, CopyCustomDataFunction.MergeStrategy op) {
/* 31 */     this.sourcePath = sourcePath;
/* 31 */     this.targetPath = targetPath;
/* 31 */     this.op = op;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NbtPathArgument.NbtPath sourcePath() {
/* 31 */     return this.sourcePath;
/*    */   }
/*    */   
/*    */   public NbtPathArgument.NbtPath targetPath() {
/* 31 */     return this.targetPath;
/*    */   }
/*    */   
/*    */   public CopyCustomDataFunction.MergeStrategy op() {
/* 31 */     return this.op;
/*    */   }
/*    */   
/*    */   static {
/* 32 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)NbtPathArgument.NbtPath.CODEC.fieldOf("source").forGetter(CopyOperation::sourcePath), (App)NbtPathArgument.NbtPath.CODEC.fieldOf("target").forGetter(CopyOperation::targetPath), (App)CopyCustomDataFunction.MergeStrategy.CODEC.fieldOf("op").forGetter(CopyOperation::op)).apply((Applicative)i, CopyOperation::new));
/*    */   }
/*    */   
/*    */   public void apply(Supplier<Tag> target, Tag source) {
/*    */     try {
/* 40 */       List<Tag> sourceTags = this.sourcePath.get(source);
/* 41 */       if (!sourceTags.isEmpty())
/* 42 */         this.op.merge(target.get(), this.targetPath, sourceTags); 
/* 44 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$CopyOperation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */