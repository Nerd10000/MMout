/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Target implements StringRepresentable {
/*  97 */   CUSTOM_NAME("custom_name"),
/*  98 */   ITEM_NAME("item_name");
/*     */   
/*     */   public static final Codec<Target> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 101 */     CODEC = (Codec<Target>)StringRepresentable.fromEnum(Target::values);
/*     */   }
/*     */   
/*     */   Target(String name) {
/* 106 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 111 */     return this.name;
/*     */   }
/*     */   
/*     */   public DataComponentType<Component> component() {
/* 116 */     switch (ordinal()) {
/*     */       default:
/* 116 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 0:
/*     */         break;
/*     */     } 
/* 116 */     return 
/*     */       
/* 118 */       DataComponents.CUSTOM_NAME;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetNameFunction$Target.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */