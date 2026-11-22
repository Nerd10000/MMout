/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class WeatheringCopperGolemStatueBlock extends CopperGolemStatueBlock implements WeatheringCopper {
/*    */   public static final MapCodec<WeatheringCopperGolemStatueBlock> CODEC;
/*    */   
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), (App)propertiesCodec()).apply((Applicative)i, WeatheringCopperGolemStatueBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WeatheringCopperGolemStatueBlock> codec() {
/* 28 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 32 */     super(weatherState, properties);
/*    */   }
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 37 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 42 */     changeOverTime(state, level, pos, random);
/*    */   }
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 47 */     return getWeatheringState();
/*    */   }
/*    */   
/*    */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 52 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 52 */     if (blockEntity instanceof CopperGolemStatueBlockEntity) {
/* 52 */       CopperGolemStatueBlockEntity copperGolemStatueBlockEntity = (CopperGolemStatueBlockEntity)blockEntity;
/* 53 */       if (itemStack.is(ItemTags.AXES)) {
/* 54 */         if (getAge().equals(WeatheringCopper.WeatherState.UNAFFECTED)) {
/* 55 */           CopperGolem copperGolem = copperGolemStatueBlockEntity.removeStatue(state);
/* 56 */           itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/* 57 */           if (copperGolem != null) {
/* 58 */             level.addFreshEntity((Entity)copperGolem);
/* 59 */             level.removeBlock(pos, false);
/* 60 */             return (InteractionResult)InteractionResult.SUCCESS;
/*    */           } 
/*    */         } 
/*    */       } else {
/* 63 */         if (itemStack.is(Items.HONEYCOMB))
/* 64 */           return (InteractionResult)InteractionResult.PASS; 
/* 66 */         updatePose(level, state, pos, player);
/* 67 */         return (InteractionResult)InteractionResult.SUCCESS;
/*    */       } 
/*    */     } 
/* 70 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopperGolemStatueBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */