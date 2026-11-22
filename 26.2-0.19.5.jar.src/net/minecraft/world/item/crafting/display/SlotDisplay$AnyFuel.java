/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.level.block.entity.FuelValues;
/*     */ 
/*     */ public class AnyFuel implements SlotDisplay {
/* 105 */   public static final AnyFuel INSTANCE = new AnyFuel();
/*     */   
/* 107 */   public static final MapCodec<AnyFuel> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */   
/* 109 */   public static final StreamCodec<RegistryFriendlyByteBuf, AnyFuel> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */   
/* 111 */   public static final SlotDisplay.Type<AnyFuel> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<AnyFuel> type() {
/* 118 */     return TYPE;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 123 */     return "<any fuel>";
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 128 */     if (factory instanceof DisplayContentsFactory.ForStacks) {
/* 128 */       DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 129 */       FuelValues fuelValues = (FuelValues)context.getOptional(SlotDisplayContext.FUEL_VALUES);
/* 130 */       if (fuelValues != null) {
/* 131 */         Objects.requireNonNull(stacks);
/* 131 */         return fuelValues.fuelItems().stream().map(stacks::forStack);
/*     */       } 
/*     */     } 
/* 134 */     return Stream.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$AnyFuel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */