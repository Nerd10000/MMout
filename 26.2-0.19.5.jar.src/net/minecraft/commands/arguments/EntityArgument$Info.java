/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ public class Info implements ArgumentTypeInfo<EntityArgument, EntityArgument.Info.Template> {
/*     */   private static final byte FLAG_SINGLE = 1;
/*     */   
/*     */   private static final byte FLAG_PLAYERS_ONLY = 2;
/*     */   
/*     */   public final class Template implements ArgumentTypeInfo.Template<EntityArgument> {
/*     */     private final boolean single;
/*     */     
/*     */     private final boolean playersOnly;
/*     */     
/*     */     private Template(boolean single, boolean playersOnly) {
/* 160 */       this.single = single;
/* 161 */       this.playersOnly = playersOnly;
/*     */     }
/*     */     
/*     */     public EntityArgument instantiate(CommandBuildContext context) {
/* 166 */       return new EntityArgument(this.single, this.playersOnly);
/*     */     }
/*     */     
/*     */     public ArgumentTypeInfo<EntityArgument, ?> type() {
/* 171 */       return EntityArgument.Info.this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 177 */     int flags = 0;
/* 178 */     if (template.single)
/* 179 */       flags |= 0x1; 
/* 181 */     if (template.playersOnly)
/* 182 */       flags |= 0x2; 
/* 184 */     out.writeByte(flags);
/*     */   }
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 189 */     byte flags = in.readByte();
/* 190 */     return new Template(((flags & 0x1) != 0), ((flags & 0x2) != 0));
/*     */   }
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 195 */     out.addProperty("amount", template.single ? "single" : "multiple");
/* 196 */     out.addProperty("type", template.playersOnly ? "players" : "entities");
/*     */   }
/*     */   
/*     */   public Template unpack(EntityArgument argument) {
/* 201 */     return new Template(argument.single, argument.playersOnly);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\EntityArgument$Info.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */