/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ public class Info implements ArgumentTypeInfo<TimeArgument, TimeArgument.Info.Template> {
/*     */   public final class Template implements ArgumentTypeInfo.Template<TimeArgument> {
/*     */     private final int min;
/*     */     
/*     */     private Template(int min) {
/*  92 */       this.min = min;
/*     */     }
/*     */     
/*     */     public TimeArgument instantiate(CommandBuildContext context) {
/*  97 */       return TimeArgument.time(this.min);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<TimeArgument, ?> type() {
/* 102 */       return TimeArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 108 */     out.writeInt(template.min);
/*     */   }
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 113 */     int min = in.readInt();
/* 114 */     return new Template(min);
/*     */   }
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 119 */     out.addProperty("min", Integer.valueOf(template.min));
/*     */   }
/*     */   
/*     */   public Template unpack(TimeArgument argument) {
/* 124 */     return new Template(argument.minimum);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\TimeArgument$Info.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */