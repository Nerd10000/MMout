/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum MergeStrategy implements StringRepresentable {
/* 142 */   REPLACE("replace") {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 145 */       path.set(target, (Tag)Iterables.getLast(sources));
/*     */     }
/*     */   },
/* 148 */   APPEND("append") {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 151 */       List<Tag> targets = path.getOrCreate(target, ListTag::new);
/* 152 */       targets.forEach(tag -> {
/*     */             if (tag instanceof ListTag) {
/*     */               ListTag listTag = (ListTag)tag;
/*     */               sources.forEach(());
/*     */             } 
/*     */           });
/*     */     }
/*     */   },
/* 160 */   MERGE("merge") {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 163 */       List<Tag> targets = path.getOrCreate(target, CompoundTag::new);
/* 164 */       targets.forEach(tag -> {
/*     */             if (tag instanceof CompoundTag) {
/*     */               CompoundTag compoundTag = (CompoundTag)tag;
/*     */               sources.forEach(());
/*     */             } 
/*     */           });
/*     */     }
/*     */   };
/*     */   
/*     */   public static final Codec<MergeStrategy> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 177 */     CODEC = (Codec<MergeStrategy>)StringRepresentable.fromEnum(MergeStrategy::values);
/*     */   }
/*     */   
/*     */   MergeStrategy(String name) {
/* 184 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 189 */     return this.name;
/*     */   }
/*     */   
/*     */   public abstract void merge(Tag paramTag, NbtPathArgument.NbtPath paramNbtPath, List<Tag> paramList) throws CommandSyntaxException;
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$MergeStrategy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */