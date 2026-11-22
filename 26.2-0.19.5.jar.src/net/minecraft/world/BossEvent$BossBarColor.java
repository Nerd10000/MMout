/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum BossBarColor implements StringRepresentable {
/*  92 */   PINK("pink", ChatFormatting.RED),
/*  93 */   BLUE("blue", ChatFormatting.BLUE),
/*  94 */   RED("red", ChatFormatting.DARK_RED),
/*  95 */   GREEN("green", ChatFormatting.GREEN),
/*  96 */   YELLOW("yellow", ChatFormatting.YELLOW),
/*  97 */   PURPLE("purple", ChatFormatting.DARK_BLUE),
/*  98 */   WHITE("white", ChatFormatting.WHITE);
/*     */   
/*     */   public static final Codec<BossBarColor> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final ChatFormatting formatting;
/*     */   
/*     */   static {
/* 101 */     CODEC = (Codec<BossBarColor>)StringRepresentable.fromEnum(BossBarColor::values);
/*     */   }
/*     */   
/*     */   BossBarColor(String name, ChatFormatting formatting) {
/* 107 */     this.name = name;
/* 108 */     this.formatting = formatting;
/*     */   }
/*     */   
/*     */   public ChatFormatting getFormatting() {
/* 112 */     return this.formatting;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 116 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 121 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\BossEvent$BossBarColor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */