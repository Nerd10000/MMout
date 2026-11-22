/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ import net.minecraft.world.item.component.TooltipProvider;
/*     */ 
/*     */ public enum Pattern implements StringRepresentable, TooltipProvider {
/* 108 */   KOB("kob", TropicalFish.Base.SMALL, 0),
/* 109 */   SUNSTREAK("sunstreak", TropicalFish.Base.SMALL, 1),
/* 110 */   SNOOPER("snooper", TropicalFish.Base.SMALL, 2),
/* 111 */   DASHER("dasher", TropicalFish.Base.SMALL, 3),
/* 112 */   BRINELY("brinely", TropicalFish.Base.SMALL, 4),
/* 113 */   SPOTTY("spotty", TropicalFish.Base.SMALL, 5),
/* 114 */   FLOPPER("flopper", TropicalFish.Base.LARGE, 0),
/* 115 */   STRIPEY("stripey", TropicalFish.Base.LARGE, 1),
/* 116 */   GLITTER("glitter", TropicalFish.Base.LARGE, 2),
/* 117 */   BLOCKFISH("blockfish", TropicalFish.Base.LARGE, 3),
/* 118 */   BETTY("betty", TropicalFish.Base.LARGE, 4),
/* 119 */   CLAYFISH("clayfish", TropicalFish.Base.LARGE, 5);
/*     */   
/*     */   public static final Codec<Pattern> CODEC;
/*     */   
/*     */   private static final IntFunction<Pattern> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Pattern> STREAM_CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final Component displayName;
/*     */   
/*     */   private final TropicalFish.Base base;
/*     */   
/*     */   private final int packedId;
/*     */   
/*     */   static {
/* 121 */     CODEC = (Codec<Pattern>)StringRepresentable.fromEnum(Pattern::values);
/* 123 */     BY_ID = ByIdMap.sparse(Pattern::getPackedId, (Object[])values(), KOB);
/* 125 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Pattern::getPackedId);
/*     */   }
/*     */   
/*     */   Pattern(String name, TropicalFish.Base base, int index) {
/* 136 */     this.name = name;
/* 137 */     this.base = base;
/* 138 */     this.packedId = base.id | index << 8;
/* 139 */     this.displayName = (Component)Component.translatable("entity.minecraft.tropical_fish.type." + this.name);
/*     */   }
/*     */   
/*     */   public static Pattern byId(int packedId) {
/* 143 */     return BY_ID.apply(packedId);
/*     */   }
/*     */   
/*     */   public TropicalFish.Base base() {
/* 147 */     return this.base;
/*     */   }
/*     */   
/*     */   public int getPackedId() {
/* 151 */     return this.packedId;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 156 */     return this.name;
/*     */   }
/*     */   
/*     */   public Component displayName() {
/* 160 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 165 */     DyeColor baseColor = (DyeColor)components.getOrDefault(DataComponents.TROPICAL_FISH_BASE_COLOR, TropicalFish.DEFAULT_VARIANT.baseColor());
/* 166 */     DyeColor patternColor = (DyeColor)components.getOrDefault(DataComponents.TROPICAL_FISH_PATTERN_COLOR, TropicalFish.DEFAULT_VARIANT.patternColor());
/* 168 */     ChatFormatting[] styles = { ChatFormatting.ITALIC, ChatFormatting.GRAY };
/* 170 */     int commonIndex = TropicalFish.COMMON_VARIANTS.indexOf(new TropicalFish.Variant(this, baseColor, patternColor));
/* 171 */     if (commonIndex != -1) {
/* 172 */       consumer.accept(Component.translatable(TropicalFish.getPredefinedName(commonIndex)).withStyle(styles));
/*     */       return;
/*     */     } 
/* 176 */     consumer.accept(this.displayName.plainCopy().withStyle(styles));
/* 177 */     MutableComponent colorComponent = Component.translatable("color.minecraft." + baseColor.getName());
/* 178 */     if (baseColor != patternColor)
/* 179 */       colorComponent.append(", ").append((Component)Component.translatable("color.minecraft." + patternColor.getName())); 
/* 181 */     colorComponent.withStyle(styles);
/* 182 */     consumer.accept(colorComponent);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\TropicalFish$Pattern.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */