/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
/*     */ 
/*     */ public class Builder extends LootItemConditionalFunction.Builder<CopyCustomDataFunction.Builder> {
/*     */   private final NbtProvider source;
/*     */   
/* 101 */   private final List<CopyCustomDataFunction.CopyOperation> ops = Lists.newArrayList();
/*     */   
/*     */   private Builder(NbtProvider source) {
/* 104 */     this.source = source;
/*     */   }
/*     */   
/*     */   public Builder copy(String sourcePath, String targetPath, CopyCustomDataFunction.MergeStrategy mergeStrategy) {
/*     */     try {
/* 109 */       this.ops.add(new CopyCustomDataFunction.CopyOperation(NbtPathArgument.NbtPath.of(sourcePath), NbtPathArgument.NbtPath.of(targetPath), mergeStrategy));
/* 110 */     } catch (CommandSyntaxException e) {
/* 111 */       throw new IllegalArgumentException(e);
/*     */     } 
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public Builder copy(String sourcePath, String targetPath) {
/* 117 */     return copy(sourcePath, targetPath, CopyCustomDataFunction.MergeStrategy.REPLACE);
/*     */   }
/*     */   
/*     */   protected Builder getThis() {
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public LootItemFunction build() {
/* 127 */     return new CopyCustomDataFunction(getConditions(), this.source, this.ops);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */