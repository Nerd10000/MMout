/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ enum Type implements StringRepresentable {
/* 146 */   PASS_THROUGH("pass_through", () -> FilterMask.PASS_THROUGH_CODEC),
/* 147 */   FULLY_FILTERED("fully_filtered", () -> FilterMask.FULLY_FILTERED_CODEC),
/* 148 */   PARTIALLY_FILTERED("partially_filtered", () -> FilterMask.PARTIALLY_FILTERED_CODEC);
/*     */   
/*     */   private final String serializedName;
/*     */   
/*     */   private final Supplier<MapCodec<FilterMask>> codec;
/*     */   
/*     */   Type(String serializedName, Supplier<MapCodec<FilterMask>> codec) {
/* 155 */     this.serializedName = serializedName;
/* 156 */     this.codec = codec;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 161 */     return this.serializedName;
/*     */   }
/*     */   
/*     */   private MapCodec<FilterMask> codec() {
/* 165 */     return this.codec.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\FilterMask$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */