/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ public class Info implements ArgumentTypeInfo<ScoreHolderArgument, ScoreHolderArgument.Info.Template> {
/*     */   private static final byte FLAG_MULTIPLE = 1;
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<ScoreHolderArgument> {
/*     */     private final boolean multiple;
/*     */     
/*     */     private Template(boolean multiple) {
/* 209 */       this.multiple = multiple;
/*     */     }
/*     */     
/*     */     public ScoreHolderArgument instantiate(CommandBuildContext context) {
/* 214 */       return new ScoreHolderArgument(this.multiple);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<ScoreHolderArgument, ?> type() {
/* 219 */       return ScoreHolderArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 225 */     int flags = 0;
/* 226 */     if (template.multiple)
/* 227 */       flags |= 0x1; 
/* 229 */     out.writeByte(flags);
/*     */   }
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 234 */     byte flags = in.readByte();
/* 235 */     boolean multiple = ((flags & 0x1) != 0);
/* 236 */     return new Template(multiple);
/*     */   }
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 241 */     out.addProperty("amount", template.multiple ? "multiple" : "single");
/*     */   }
/*     */   
/*     */   public Template unpack(ScoreHolderArgument argument) {
/* 246 */     return new Template(argument.multiple);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ScoreHolderArgument$Info.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */