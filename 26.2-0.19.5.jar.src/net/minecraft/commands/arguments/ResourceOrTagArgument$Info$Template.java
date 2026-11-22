/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagArgument<T>> {
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 157 */     this.registryKey = registryKey;
/*     */   }
/*     */   
/*     */   public ResourceOrTagArgument<T> instantiate(CommandBuildContext context) {
/* 162 */     return new ResourceOrTagArgument<>(context, this.registryKey);
/*     */   }
/*     */   
/*     */   public ArgumentTypeInfo<ResourceOrTagArgument<T>, ?> type() {
/* 167 */     return ResourceOrTagArgument.Info.this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrTagArgument$Info$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */