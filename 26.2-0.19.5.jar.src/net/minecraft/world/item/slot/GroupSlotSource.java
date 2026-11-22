/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class GroupSlotSource extends CompositeSlotSource {
/*  9 */   public static final MapCodec<GroupSlotSource> MAP_CODEC = createCodec(GroupSlotSource::new);
/*    */   
/* 10 */   public static final Codec<GroupSlotSource> INLINE_CODEC = createInlineCodec(GroupSlotSource::new);
/*    */   
/*    */   private GroupSlotSource(List<SlotSource> terms) {
/* 13 */     super(terms);
/*    */   }
/*    */   
/*    */   public MapCodec<GroupSlotSource> codec() {
/* 18 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\GroupSlotSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */