/*    */ package net.minecraft.world.entity.animal.golem;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.level.block.WeatheringCopper;
/*    */ 
/*    */ public class CopperGolemOxidationLevels {
/* 10 */   private static final CopperGolemOxidationLevel UNAFFECTED = new CopperGolemOxidationLevel(SoundEvents.COPPER_GOLEM_SPIN, SoundEvents.COPPER_GOLEM_HURT, SoundEvents.COPPER_GOLEM_DEATH, SoundEvents.COPPER_GOLEM_STEP, 
/* 11 */       Identifier.withDefaultNamespace("textures/entity/copper_golem/copper_golem.png"), Identifier.withDefaultNamespace("textures/entity/copper_golem/copper_golem_eyes.png"));
/*    */   
/* 12 */   private static final CopperGolemOxidationLevel EXPOSED = new CopperGolemOxidationLevel(SoundEvents.COPPER_GOLEM_SPIN, SoundEvents.COPPER_GOLEM_HURT, SoundEvents.COPPER_GOLEM_DEATH, SoundEvents.COPPER_GOLEM_STEP, 
/* 13 */       Identifier.withDefaultNamespace("textures/entity/copper_golem/copper_golem_exposed.png"), Identifier.withDefaultNamespace("textures/entity/copper_golem/copper_golem_eyes_exposed.png"));
/*    */   
/* 14 */   private static final CopperGolemOxidationLevel WEATHERED = new CopperGolemOxidationLevel(SoundEvents.COPPER_GOLEM_WEATHERED_SPIN, SoundEvents.COPPER_GOLEM_WEATHERED_HURT, SoundEvents.COPPER_GOLEM_WEATHERED_DEATH, SoundEvents.COPPER_GOLEM_WEATHERED_STEP, 
/* 15 */       Identifier.withDefaultNamespace("textures/entity/copper_golem/copper_golem_weathered.png"), Identifier.withDefaultNamespace("textures/entity/copper_golem/copper_golem_eyes_weathered.png"));
/*    */   
/* 16 */   private static final CopperGolemOxidationLevel OXIDIZED = new CopperGolemOxidationLevel(SoundEvents.COPPER_GOLEM_OXIDIZED_SPIN, SoundEvents.COPPER_GOLEM_OXIDIZED_HURT, SoundEvents.COPPER_GOLEM_OXIDIZED_DEATH, SoundEvents.COPPER_GOLEM_OXIDIZED_STEP, 
/* 17 */       Identifier.withDefaultNamespace("textures/entity/copper_golem/copper_golem_oxidized.png"), Identifier.withDefaultNamespace("textures/entity/copper_golem/copper_golem_eyes_oxidized.png"));
/*    */   
/* 19 */   private static final Map<WeatheringCopper.WeatherState, CopperGolemOxidationLevel> WEATHERED_STATES = Map.of(WeatheringCopper.WeatherState.UNAFFECTED, UNAFFECTED, WeatheringCopper.WeatherState.EXPOSED, EXPOSED, WeatheringCopper.WeatherState.WEATHERED, WEATHERED, WeatheringCopper.WeatherState.OXIDIZED, OXIDIZED);
/*    */   
/*    */   public static CopperGolemOxidationLevel getOxidationLevel(WeatheringCopper.WeatherState state) {
/* 22 */     return WEATHERED_STATES.get(state);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\golem\CopperGolemOxidationLevels.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */