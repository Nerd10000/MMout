/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class BooleanProperty extends Property<Boolean> {
/*  7 */   private static final List<Boolean> VALUES = List.of(Boolean.valueOf(true), Boolean.valueOf(false));
/*    */   
/*    */   private static final int TRUE_INDEX = 0;
/*    */   
/*    */   private static final int FALSE_INDEX = 1;
/*    */   
/*    */   private BooleanProperty(String name) {
/* 12 */     super(name, Boolean.class);
/*    */   }
/*    */   
/*    */   public List<Boolean> getPossibleValues() {
/* 17 */     return VALUES;
/*    */   }
/*    */   
/*    */   public static BooleanProperty create(String name) {
/* 21 */     return new BooleanProperty(name);
/*    */   }
/*    */   
/*    */   public Optional<Boolean> getValue(String name) {
/* 26 */     switch (name) {
/*    */       case "true":
/*    */       
/*    */       case "false":
/*    */       
/*    */     } 
/* 26 */     return 
/*    */       
/* 29 */       Optional.empty();
/*    */   }
/*    */   
/*    */   public String getName(Boolean value) {
/* 35 */     return value.toString();
/*    */   }
/*    */   
/*    */   public int getInternalIndex(Boolean value) {
/* 40 */     return value.booleanValue() ? 0 : 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\BooleanProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */