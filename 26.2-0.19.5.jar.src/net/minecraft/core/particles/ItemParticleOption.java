/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ 
/*    */ public class ItemParticleOption implements ParticleOptions {
/*    */   private final ParticleType<ItemParticleOption> type;
/*    */   
/*    */   private final ItemStackTemplate itemStack;
/*    */   
/*    */   public static MapCodec<ItemParticleOption> codec(ParticleType<ItemParticleOption> type) {
/* 11 */     return ItemStackTemplate.CODEC.xmap(stack -> new ItemParticleOption(type, stack), o -> o.itemStack).fieldOf("item");
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super RegistryFriendlyByteBuf, ItemParticleOption> streamCodec(ParticleType<ItemParticleOption> type) {
/* 15 */     return ItemStackTemplate.STREAM_CODEC.map(stack -> new ItemParticleOption(type, stack), o -> o.itemStack);
/*    */   }
/*    */   
/*    */   public ItemParticleOption(ParticleType<ItemParticleOption> type, Item item) {
/* 22 */     this(type, new ItemStackTemplate(item));
/*    */   }
/*    */   
/*    */   public ItemParticleOption(ParticleType<ItemParticleOption> type, ItemStackTemplate itemStack) {
/* 26 */     this.type = type;
/* 27 */     this.itemStack = itemStack;
/*    */   }
/*    */   
/*    */   public ParticleType<ItemParticleOption> getType() {
/* 32 */     return this.type;
/*    */   }
/*    */   
/*    */   public ItemStackTemplate getItem() {
/* 36 */     return this.itemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\ItemParticleOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */