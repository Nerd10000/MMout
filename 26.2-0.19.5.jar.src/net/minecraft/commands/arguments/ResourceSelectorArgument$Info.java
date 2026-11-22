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
/*     */ public class Info<T> implements ArgumentTypeInfo<ResourceSelectorArgument<T>, ResourceSelectorArgument.Info<T>.Template> {
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceSelectorArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 105 */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceSelectorArgument<T> instantiate(CommandBuildContext context) {
/* 110 */       return new ResourceSelectorArgument<>(context, this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceSelectorArgument<T>, ?> type() {
/* 115 */       return ResourceSelectorArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 121 */     out.writeResourceKey(template.registryKey);
/*     */   }
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 126 */     return new Template(in.readRegistryKey());
/*     */   }
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 131 */     out.addProperty("registry", template.registryKey.identifier().toString());
/*     */   }
/*     */   
/*     */   public Template unpack(ResourceSelectorArgument<T> argument) {
/* 136 */     return new Template(argument.registryKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceSelectorArgument$Info.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */