/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Clear implements RuleBlockEntityModifier {
/*  9 */   private static final Clear INSTANCE = new Clear();
/*    */   
/* 10 */   public static final MapCodec<Clear> CODEC = MapCodec.unit(INSTANCE);
/*    */   
/*    */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) {
/* 14 */     return new CompoundTag();
/*    */   }
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 19 */     return RuleBlockEntityModifierType.CLEAR;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\Clear.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */