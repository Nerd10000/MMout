/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class LimitSlotSource extends TransformedSlotSource {
/*    */   public static final MapCodec<LimitSlotSource> MAP_CODEC;
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   static {
/*  8 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)ExtraCodecs.POSITIVE_INT.fieldOf("limit").forGetter(())).apply((Applicative)i, LimitSlotSource::new));
/*    */   }
/*    */   
/*    */   private LimitSlotSource(SlotSource slotSource, int limit) {
/* 15 */     super(slotSource);
/* 16 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public MapCodec<LimitSlotSource> codec() {
/* 21 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   protected SlotCollection transform(SlotCollection slots) {
/* 26 */     return slots.limit(this.limit);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\LimitSlotSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */