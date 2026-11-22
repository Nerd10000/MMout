/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Collections;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class StaticAction extends Record implements Action {
/*    */   private final ClickEvent value;
/*    */   
/*    */   public static final Map<ClickEvent.Action, MapCodec<StaticAction>> WRAPPED_CODECS;
/*    */   
/*    */   public StaticAction(ClickEvent value) {
/* 12 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/StaticAction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/StaticAction;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/StaticAction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/StaticAction;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/StaticAction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/StaticAction;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ClickEvent value() {
/* 12 */     return this.value;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     WRAPPED_CODECS = (Map<ClickEvent.Action, MapCodec<StaticAction>>)Util.make(() -> {
/*    */           Map<ClickEvent.Action, MapCodec<StaticAction>> result = new EnumMap<>(ClickEvent.Action.class);
/*    */           for (ClickEvent.Action action : (ClickEvent.Action[])ClickEvent.Action.class.getEnumConstants()) {
/*    */             if (action.isAllowedFromServer()) {
/*    */               MapCodec<ClickEvent> mapCodec = action.valueCodec();
/*    */               result.put(action, mapCodec.xmap(StaticAction::new, StaticAction::value));
/*    */             } 
/*    */           } 
/*    */           return Collections.unmodifiableMap(result);
/*    */         });
/*    */   }
/*    */   
/*    */   public MapCodec<StaticAction> codec() {
/* 28 */     return WRAPPED_CODECS.get(this.value.action());
/*    */   }
/*    */   
/*    */   public Optional<ClickEvent> createAction(Map<String, Action.ValueGetter> parameters) {
/* 33 */     return Optional.of(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\action\StaticAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */