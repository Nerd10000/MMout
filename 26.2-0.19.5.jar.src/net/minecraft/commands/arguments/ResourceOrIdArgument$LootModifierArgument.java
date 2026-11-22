/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ 
/*     */ public class LootModifierArgument extends ResourceOrIdArgument<LootItemFunction> {
/*     */   protected LootModifierArgument(CommandBuildContext context) {
/* 116 */     super(context, Registries.ITEM_MODIFIER, LootItemFunctions.ROOT_CODEC);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrIdArgument$LootModifierArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */