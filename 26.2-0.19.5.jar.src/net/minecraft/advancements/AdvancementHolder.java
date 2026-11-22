/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class AdvancementHolder extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Advancement value;
/*    */   
/*    */   public AdvancementHolder(Identifier id, Advancement value) {
/* 11 */     this.id = id;
/* 11 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 11 */     return this.id;
/*    */   }
/*    */   
/*    */   public Advancement value() {
/* 11 */     return this.value;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<RegistryFriendlyByteBuf, AdvancementHolder> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, AdvancementHolder::id, Advancement.STREAM_CODEC, AdvancementHolder::value, AdvancementHolder::new);
/*    */   
/* 18 */   public static final StreamCodec<RegistryFriendlyByteBuf, List<AdvancementHolder>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
/*    */   
/*    */   public boolean equals(Object obj) {
/* 22 */     if (this == obj)
/* 23 */       return true; 
/* 25 */     if (obj instanceof AdvancementHolder) {
/* 25 */       AdvancementHolder holder = (AdvancementHolder)obj;
/* 25 */       if (this.id.equals(holder.id));
/*    */     } 
/* 25 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 30 */     return this.id.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 35 */     return this.id.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\AdvancementHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */