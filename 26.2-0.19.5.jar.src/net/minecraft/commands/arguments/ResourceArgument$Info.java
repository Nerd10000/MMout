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
/*     */ public class Info<T> implements ArgumentTypeInfo<ResourceArgument<T>, ResourceArgument.Info<T>.Template> {
/*     */   public final class Template implements ArgumentTypeInfo.Template<ResourceArgument<T>> {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private Template(ResourceKey<? extends Registry<T>> registryKey) {
/* 132 */       this.registryKey = registryKey;
/*     */     }
/*     */     
/*     */     public ResourceArgument<T> instantiate(CommandBuildContext context) {
/* 137 */       return new ResourceArgument<>(context, this.registryKey);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ResourceArgument<T>, ?> type() {
/* 142 */       return ResourceArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 148 */     out.writeResourceKey(template.registryKey);
/*     */   }
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 153 */     return new Template(in.readRegistryKey());
/*     */   }
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 158 */     out.addProperty("registry", template.registryKey.identifier().toString());
/*     */   }
/*     */   
/*     */   public Template unpack(ResourceArgument<T> argument) {
/* 163 */     return new Template(argument.registryKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceArgument$Info.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */