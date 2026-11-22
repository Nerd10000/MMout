/*    */ package net.minecraft.world.waypoints;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ import net.minecraft.world.scores.TeamColor;
/*    */ 
/*    */ public interface Waypoint {
/* 23 */   public static final AttributeModifier WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER = new AttributeModifier(
/* 24 */       Identifier.withDefaultNamespace("waypoint_transmit_range_hide"), -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
/*    */   
/*    */   static Item.Properties addHideAttribute(Item.Properties properties) {
/* 30 */     return properties.component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder().add(Attributes.WAYPOINT_TRANSMIT_RANGE, WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER, EquipmentSlotGroup.HEAD, ItemAttributeModifiers.Display.hidden()).build());
/*    */   }
/*    */   
/*    */   public static class Icon {
/*    */     public static final Codec<Icon> CODEC;
/*    */     
/*    */     public static final StreamCodec<ByteBuf, Icon> STREAM_CODEC;
/*    */     
/*    */     static {
/* 34 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ResourceKey.codec(WaypointStyleAssets.ROOT_ID).fieldOf("style").forGetter(()), (App)ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color").forGetter(())).apply((Applicative)i, Icon::new));
/* 38 */       STREAM_CODEC = StreamCodec.composite(
/* 39 */           ResourceKey.streamCodec(WaypointStyleAssets.ROOT_ID), icon -> icon.style, 
/* 40 */           ByteBufCodecs.optional(ByteBufCodecs.RGB_COLOR), icon -> icon.color, Icon::new);
/*    */     }
/*    */     
/* 44 */     public static final Icon NULL = new Icon();
/*    */     
/* 46 */     public ResourceKey<WaypointStyleAsset> style = WaypointStyleAssets.DEFAULT;
/*    */     
/* 47 */     public Optional<Integer> color = Optional.empty();
/*    */     
/*    */     private Icon(ResourceKey<WaypointStyleAsset> style, Optional<Integer> color) {
/* 53 */       this.style = style;
/* 54 */       this.color = color;
/*    */     }
/*    */     
/*    */     public boolean hasData() {
/* 58 */       return (this.style != WaypointStyleAssets.DEFAULT || this.color.isPresent());
/*    */     }
/*    */     
/*    */     public Icon cloneAndAssignStyle(LivingEntity livingEntity) {
/* 62 */       ResourceKey<WaypointStyleAsset> overrideStyle = getOverrideStyle();
/* 64 */       Optional<Integer> colorOverride = this.color.or(() -> Optional.<PlayerTeam>ofNullable(livingEntity.getTeam()).flatMap(()).map(()));
/* 70 */       if (overrideStyle == this.style && colorOverride.isEmpty())
/* 71 */         return this; 
/* 74 */       return new Icon(overrideStyle, colorOverride);
/*    */     }
/*    */     
/*    */     public void copyFrom(Icon other) {
/* 78 */       this.color = other.color;
/* 79 */       this.style = other.style;
/*    */     }
/*    */     
/*    */     private ResourceKey<WaypointStyleAsset> getOverrideStyle() {
/* 83 */       return (this.style != WaypointStyleAssets.DEFAULT) ? this.style : WaypointStyleAssets.DEFAULT;
/*    */     }
/*    */     
/*    */     public Icon() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\Waypoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */