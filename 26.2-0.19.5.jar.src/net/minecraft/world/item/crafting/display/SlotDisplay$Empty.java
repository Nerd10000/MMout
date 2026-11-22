/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ 
/*     */ public class Empty implements SlotDisplay {
/*  77 */   public static final Empty INSTANCE = new Empty();
/*     */   
/*  79 */   public static final MapCodec<Empty> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */   
/*  81 */   public static final StreamCodec<RegistryFriendlyByteBuf, Empty> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */   
/*  83 */   public static final SlotDisplay.Type<Empty> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   public SlotDisplay.Type<Empty> type() {
/*  90 */     return TYPE;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  95 */     return "<empty>";
/*     */   }
/*     */   
/*     */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 100 */     return Stream.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$Empty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */