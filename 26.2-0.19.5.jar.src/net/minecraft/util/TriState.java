/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public enum TriState implements StringRepresentable {
/*  9 */   TRUE("true"),
/* 10 */   FALSE("false"),
/* 11 */   DEFAULT("default");
/*    */   
/*    */   public static final Codec<TriState> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 17 */     CODEC = Codec.either((Codec)Codec.BOOL, StringRepresentable.fromEnum(TriState::values)).xmap(either -> (TriState)either.map(TriState::from, Function.identity()), triState -> {
/*    */           switch (triState.ordinal()) {
/*    */             default:
/*    */               throw new MatchException(null, null);
/*    */             case 2:
/*    */             
/*    */             case 0:
/*    */             
/*    */             case 1:
/*    */               break;
/*    */           } 
/*    */           return Either.left(Boolean.valueOf(false));
/*    */         });
/*    */   }
/*    */   
/*    */   TriState(String name) {
/* 29 */     this.name = name;
/*    */   }
/*    */   
/*    */   public static TriState from(boolean value) {
/* 33 */     return value ? TRUE : FALSE;
/*    */   }
/*    */   
/*    */   public boolean toBoolean(boolean defaultValue) {
/* 37 */     switch (ordinal()) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */     } 
/* 37 */     return 
/*    */       
/* 40 */       defaultValue;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 46 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\TriState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */