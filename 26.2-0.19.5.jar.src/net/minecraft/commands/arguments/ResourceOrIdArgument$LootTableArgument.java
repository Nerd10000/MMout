/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class LootTableArgument extends ResourceOrIdArgument<LootTable> {
/*     */   protected LootTableArgument(CommandBuildContext context) {
/* 102 */     super(context, Registries.LOOT_TABLE, LootTable.DIRECT_CODEC);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrIdArgument$LootTableArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */