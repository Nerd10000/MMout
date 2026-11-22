/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Action implements StringRepresentable {
/* 113 */   SHOW_TEXT("show_text", true, (MapCodec)HoverEvent.ShowText.CODEC),
/* 114 */   SHOW_ITEM("show_item", true, (MapCodec)HoverEvent.ShowItem.CODEC),
/* 115 */   SHOW_ENTITY("show_entity", true, (MapCodec)HoverEvent.ShowEntity.CODEC);
/*     */   
/*     */   public static final Codec<Action> UNSAFE_CODEC;
/*     */   
/*     */   public static final Codec<Action> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean allowFromServer;
/*     */   
/*     */   private final MapCodec<? extends HoverEvent> codec;
/*     */   
/*     */   static {
/* 118 */     UNSAFE_CODEC = StringRepresentable.fromValues(Action::values);
/* 119 */     CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */   }
/*     */   
/*     */   Action(String name, boolean allowFromServer, MapCodec<? extends HoverEvent> codec) {
/* 126 */     this.name = name;
/* 127 */     this.allowFromServer = allowFromServer;
/* 128 */     this.codec = codec;
/*     */   }
/*     */   
/*     */   public boolean isAllowedFromServer() {
/* 132 */     return this.allowFromServer;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 137 */     return this.name;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 142 */     return "<action " + this.name + ">";
/*     */   }
/*     */   
/*     */   private static DataResult<Action> filterForSerialization(Action action) {
/* 146 */     if (!action.isAllowedFromServer())
/* 147 */       return DataResult.error(() -> "Action not allowed: " + String.valueOf(action)); 
/* 149 */     return DataResult.success(action, Lifecycle.stable());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\HoverEvent$Action.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */