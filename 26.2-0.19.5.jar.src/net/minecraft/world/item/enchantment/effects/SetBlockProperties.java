/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class SetBlockProperties extends Record implements EnchantmentEntityEffect {
/*    */   private final BlockItemStateProperties properties;
/*    */   
/*    */   private final Vec3i offset;
/*    */   
/*    */   private final Optional<Holder<GameEvent>> triggerGameEvent;
/*    */   
/*    */   public static final MapCodec<SetBlockProperties> CODEC;
/*    */   
/*    */   public SetBlockProperties(BlockItemStateProperties properties, Vec3i offset, Optional<Holder<GameEvent>> triggerGameEvent) {
/* 19 */     this.properties = properties;
/* 19 */     this.offset = offset;
/* 19 */     this.triggerGameEvent = triggerGameEvent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockItemStateProperties properties() {
/* 19 */     return this.properties;
/*    */   }
/*    */   
/*    */   public Vec3i offset() {
/* 19 */     return this.offset;
/*    */   }
/*    */   
/*    */   public Optional<Holder<GameEvent>> triggerGameEvent() {
/* 19 */     return this.triggerGameEvent;
/*    */   }
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockItemStateProperties.CODEC.fieldOf("properties").forGetter(SetBlockProperties::properties), (App)Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(SetBlockProperties::offset), (App)GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(SetBlockProperties::triggerGameEvent)).apply((Applicative)i, SetBlockProperties::new));
/*    */   }
/*    */   
/*    */   public SetBlockProperties(BlockItemStateProperties properties) {
/* 31 */     this(properties, Vec3i.ZERO, (Optional)Optional.of(GameEvent.BLOCK_CHANGE));
/*    */   }
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 36 */     BlockPos blockPos = BlockPos.containing((Position)position).offset(this.offset);
/* 37 */     BlockState state = entity.level().getBlockState(blockPos);
/* 38 */     BlockState modified = this.properties.apply(state);
/* 39 */     if (state != modified && 
/* 40 */       entity.level().setBlock(blockPos, modified, 3))
/* 41 */       this.triggerGameEvent.ifPresent(event -> serverLevel.gameEvent(entity, event, blockPos)); 
/*    */   }
/*    */   
/*    */   public MapCodec<SetBlockProperties> codec() {
/* 48 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\SetBlockProperties.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */