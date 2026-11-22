/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class AppendStatic implements RuleBlockEntityModifier {
/*    */   public static final MapCodec<AppendStatic> CODEC;
/*    */   
/*    */   private final CompoundTag tag;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CompoundTag.CODEC.fieldOf("data").forGetter(())).apply((Applicative)i, AppendStatic::new));
/*    */   }
/*    */   
/*    */   public AppendStatic(CompoundTag tag) {
/* 16 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) {
/* 21 */     return (existingTag == null) ? this.tag.copy() : existingTag.merge(this.tag);
/*    */   }
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 26 */     return RuleBlockEntityModifierType.APPEND_STATIC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\AppendStatic.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */