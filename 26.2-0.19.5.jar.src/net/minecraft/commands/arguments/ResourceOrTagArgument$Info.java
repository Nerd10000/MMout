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
/*     */ public class Info<T> implements ArgumentTypeInfo<ResourceOrTagArgument<T>, ResourceOrTagArgument.Info<T>.Template> {
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 157 */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceOrTagArgument<T> instantiate(CommandBuildContext context) {
/* 162 */       return new ResourceOrTagArgument<>(context, this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceOrTagArgument<T>, ?> type() {
/* 167 */       return ResourceOrTagArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 173 */     out.writeResourceKey(template.registryKey);
/*     */   }
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 178 */     return new Template(in.readRegistryKey());
/*     */   }
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 183 */     out.addProperty("registry", template.registryKey.identifier().toString());
/*     */   }
/*     */   
/*     */   public Template unpack(ResourceOrTagArgument<T> argument) {
/* 188 */     return new Template(argument.registryKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrTagArgument$Info.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */