/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ enum null {
/*     */   public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 151 */     List<Tag> targets = path.getOrCreate(target, ListTag::new);
/* 152 */     targets.forEach(tag -> {
/*     */           if (tag instanceof ListTag) {
/*     */             ListTag listTag = (ListTag)tag;
/*     */             sources.forEach(());
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$MergeStrategy$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */