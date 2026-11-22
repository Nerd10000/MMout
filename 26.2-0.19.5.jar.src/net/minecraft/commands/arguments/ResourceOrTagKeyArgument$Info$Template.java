/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 135 */     this.registryKey = registryKey;
/*     */   }
/*     */   
/*     */   public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext context) {
/* 140 */     return new ResourceOrTagKeyArgument<>(this.registryKey);
/*     */   }
/*     */   
/*     */   public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() {
/* 145 */     return ResourceOrTagKeyArgument.Info.this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrTagKeyArgument$Info$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */