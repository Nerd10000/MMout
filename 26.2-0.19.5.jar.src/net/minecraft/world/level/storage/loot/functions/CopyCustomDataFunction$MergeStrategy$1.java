/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ enum null {
/*     */   public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 145 */     path.set(target, (Tag)Iterables.getLast(sources));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$MergeStrategy$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */