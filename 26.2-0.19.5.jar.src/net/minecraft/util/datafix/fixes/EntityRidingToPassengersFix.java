/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ 
/*     */ public class EntityRidingToPassengersFix extends DataFix {
/*     */   public EntityRidingToPassengersFix(Schema outputSchema, boolean changesType) {
/*  22 */     super(outputSchema, changesType);
/*     */   }
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  27 */     Schema inputSchema = getInputSchema();
/*  28 */     Schema outputSchema = getOutputSchema();
/*  30 */     Type<?> oldEntityTreeType = inputSchema.getTypeRaw(References.ENTITY_TREE);
/*  31 */     Type<?> newEntityTreeType = outputSchema.getTypeRaw(References.ENTITY_TREE);
/*  32 */     Type<?> entityType = inputSchema.getTypeRaw(References.ENTITY);
/*  34 */     return cap(inputSchema, outputSchema, oldEntityTreeType, newEntityTreeType, entityType);
/*     */   }
/*     */   
/*     */   private <OldEntityTree, NewEntityTree, Entity> TypeRewriteRule cap(Schema inputSchema, Schema outputType, Type<OldEntityTree> oldEntityTreeType, Type<NewEntityTree> newEntityTreeType, Type<Entity> entityType) {
/*  38 */     Type<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> oldType = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(
/*  39 */           DSL.optional((Type)DSL.field("Riding", oldEntityTreeType)), entityType));
/*  43 */     Type<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> newType = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(
/*  44 */           DSL.optional((Type)DSL.field("Passengers", (Type)DSL.list(newEntityTreeType))), entityType));
/*  48 */     Type<?> oldEntityType = inputSchema.getType(References.ENTITY_TREE);
/*  49 */     Type<?> newEntityType = outputType.getType(References.ENTITY_TREE);
/*  51 */     if (!Objects.equals(oldEntityType, oldType))
/*  52 */       throw new IllegalStateException("Old entity type is not what was expected."); 
/*  55 */     if (!newEntityType.equals(newType, true, true))
/*  56 */       throw new IllegalStateException("New entity type is not what was expected."); 
/*  60 */     Type<?> patchedEntityTreeType = ExtraDataFixUtils.patchSubType(oldType, oldType, newType);
/*  62 */     OpticFinder<Entity> entityFinder = DSL.typeFinder(entityType);
/*  63 */     OpticFinder<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> newEntityTreeValueFinder = DSL.typeFinder(newType);
/*  64 */     OpticFinder<NewEntityTree> ridingFinder = DSL.fieldFinder("Riding", newEntityTreeType);
/*  66 */     Type<?> oldPlayerType = inputSchema.getType(References.PLAYER);
/*  67 */     Type<?> newPlayerType = outputType.getType(References.PLAYER);
/*  69 */     return TypeRewriteRule.seq(
/*  70 */         fixTypeEverywhere("EntityRidingToPassengerFix", oldType, newType, ops -> ()), 
/*     */         
/*  87 */         writeAndRead("player RootVehicle injecter", oldPlayerType, newPlayerType));
/*     */   }
/*     */   
/*     */   private static <Entity, EntityTree> Pair<String, Pair<Either<List<EntityTree>, Unit>, Entity>> addPassengerToTop(Pair<String, Pair<Either<List<EntityTree>, Unit>, Entity>> root, Entity passengerEntity, DynamicOps<?> ops, Type<EntityTree> rawEntityTreeType, OpticFinder<Pair<String, Pair<Either<List<EntityTree>, Unit>, Entity>>> entityTreeFinder) {
/*     */     Pair<String, Pair<Either<List<EntityTree>, Unit>, Entity>> newPassenger;
/*  98 */     Entity rootEntity = (Entity)((Pair)root.getSecond()).getSecond();
/*  99 */     Optional<List<EntityTree>> passengers = ((Either)((Pair)root.getSecond()).getFirst()).left();
/* 102 */     if (passengers.isPresent() && !((List)passengers.get()).isEmpty()) {
/* 103 */       Pair<String, Pair<Either<List<EntityTree>, Unit>, Entity>> unwrappedPassenger = unwrapRecursiveValue(((List<EntityTree>)passengers.get()).getFirst(), ops, rawEntityTreeType, entityTreeFinder);
/* 104 */       newPassenger = addPassengerToTop(unwrappedPassenger, passengerEntity, ops, rawEntityTreeType, entityTreeFinder);
/*     */     } else {
/* 106 */       newPassenger = Pair.of(References.ENTITY_TREE.typeName(), Pair.of(Either.right(Unit.INSTANCE), passengerEntity));
/*     */     } 
/* 109 */     List<EntityTree> newPassengers = List.of(wrapRecursiveValue(newPassenger, ops, rawEntityTreeType, entityTreeFinder));
/* 110 */     return Pair.of(References.ENTITY_TREE.typeName(), Pair.of(Either.left(newPassengers), rootEntity));
/*     */   }
/*     */   
/*     */   private static <Raw, Value> Value unwrapRecursiveValue(Raw raw, DynamicOps<?> ops, Type<Raw> rawType, OpticFinder<Value> valueFinder) {
/* 119 */     return (new Typed(rawType, ops, raw)).getOptional(valueFinder).orElseThrow();
/*     */   }
/*     */   
/*     */   private static <Raw, Value> Raw wrapRecursiveValue(Value value, DynamicOps<?> ops, Type<Raw> rawType, OpticFinder<Value> valueFinder) {
/* 129 */     return (Raw)((Typed)rawType.pointTyped(ops).orElseThrow())
/* 130 */       .set(valueFinder, value)
/* 131 */       .getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityRidingToPassengersFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */