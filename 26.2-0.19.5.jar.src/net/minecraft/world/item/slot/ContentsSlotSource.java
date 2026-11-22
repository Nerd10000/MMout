/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ 
/*    */ public class ContentsSlotSource extends TransformedSlotSource {
/*    */   public static final MapCodec<ContentsSlotSource> MAP_CODEC;
/*    */   
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   static {
/*  9 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(())).apply((Applicative)i, ContentsSlotSource::new));
/*    */   }
/*    */   
/*    */   private ContentsSlotSource(SlotSource slotSource, ContainerComponentManipulator<?> component) {
/* 16 */     super(slotSource);
/* 17 */     this.component = component;
/*    */   }
/*    */   
/*    */   public MapCodec<ContentsSlotSource> codec() {
/* 22 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   protected SlotCollection transform(SlotCollection slots) {
/* 27 */     Objects.requireNonNull(this.component);
/* 27 */     return slots.flatMap(this.component::getSlots);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\ContentsSlotSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */