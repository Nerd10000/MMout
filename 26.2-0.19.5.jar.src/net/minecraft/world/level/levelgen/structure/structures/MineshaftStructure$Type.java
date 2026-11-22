/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public enum Type implements StringRepresentable {
/*  81 */   NORMAL("normal", Blocks.OAK_LOG, Blocks.OAK_PLANKS, Blocks.OAK_FENCE),
/*  82 */   MESA("mesa", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE);
/*     */   
/*     */   public static final Codec<Type> CODEC;
/*     */   
/*     */   private static final IntFunction<Type> BY_ID;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final BlockState woodState;
/*     */   
/*     */   private final BlockState planksState;
/*     */   
/*     */   private final BlockState fenceState;
/*     */   
/*     */   static {
/*  85 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*  86 */     BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */   }
/*     */   
/*     */   Type(String name, Block wood, Block plank, Block fence) {
/*  94 */     this.name = name;
/*  95 */     this.woodState = wood.defaultBlockState();
/*  96 */     this.planksState = plank.defaultBlockState();
/*  97 */     this.fenceState = fence.defaultBlockState();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 101 */     return this.name;
/*     */   }
/*     */   
/*     */   public static Type byId(int id) {
/* 105 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   public BlockState getWoodState() {
/* 109 */     return this.woodState;
/*     */   }
/*     */   
/*     */   public BlockState getPlanksState() {
/* 113 */     return this.planksState;
/*     */   }
/*     */   
/*     */   public BlockState getFenceState() {
/* 117 */     return this.fenceState;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 122 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftStructure$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */