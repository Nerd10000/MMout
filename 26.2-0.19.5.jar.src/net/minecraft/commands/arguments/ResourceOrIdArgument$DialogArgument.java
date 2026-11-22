/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ 
/*     */ public class DialogArgument extends ResourceOrIdArgument<Dialog> {
/*     */   protected DialogArgument(CommandBuildContext context) {
/* 144 */     super(context, Registries.DIALOG, Dialog.DIRECT_CODEC);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrIdArgument$DialogArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */