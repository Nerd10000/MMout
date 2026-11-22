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
/*     */ public class Info<T> implements ArgumentTypeInfo<ResourceKeyArgument<T>, ResourceKeyArgument.Info<T>.Template> {
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceKeyArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 121 */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceKeyArgument<T> instantiate(CommandBuildContext context) {
/* 126 */       return new ResourceKeyArgument<>(this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceKeyArgument<T>, ?> type() {
/* 131 */       return ResourceKeyArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 137 */     out.writeResourceKey(template.registryKey);
/*     */   }
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 142 */     return new Template(in.readRegistryKey());
/*     */   }
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 147 */     out.addProperty("registry", template.registryKey.identifier().toString());
/*     */   }
/*     */   
/*     */   public Template unpack(ResourceKeyArgument<T> argument) {
/* 152 */     return new Template(argument.registryKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceKeyArgument$Info.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */