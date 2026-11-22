/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public class Info<T> implements ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ResourceOrTagKeyArgument.Info<T>.Template> {
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 135 */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext context) {
/* 140 */       return new ResourceOrTagKeyArgument<>(this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() {
/* 145 */       return ResourceOrTagKeyArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 151 */     out.writeResourceKey(template.registryKey);
/*     */   }
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 156 */     return new Template(in.readRegistryKey());
/*     */   }
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 161 */     out.addProperty("registry", template.registryKey.identifier().toString());
/*     */   }
/*     */   
/*     */   public Template unpack(ResourceOrTagKeyArgument<T> argument) {
/* 166 */     return new Template(argument.registryKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrTagKeyArgument$Info.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */