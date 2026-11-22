/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWCharCallbackI;
/*     */ import org.lwjgl.glfw.GLFWCursorPosCallbackI;
/*     */ import org.lwjgl.glfw.GLFWDropCallbackI;
/*     */ import org.lwjgl.glfw.GLFWIMEStatusCallbackI;
/*     */ import org.lwjgl.glfw.GLFWKeyCallbackI;
/*     */ import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
/*     */ import org.lwjgl.glfw.GLFWPreeditCallbackI;
/*     */ import org.lwjgl.glfw.GLFWScrollCallbackI;
/*     */ 
/*     */ public class InputConstants {
/*     */   public static final int KEY_0 = 48;
/*     */   
/*     */   public static final int KEY_1 = 49;
/*     */   
/*     */   public static final int KEY_2 = 50;
/*     */   
/*     */   public static final int KEY_3 = 51;
/*     */   
/*     */   public static final int KEY_4 = 52;
/*     */   
/*     */   public static final int KEY_5 = 53;
/*     */   
/*     */   public static final int KEY_6 = 54;
/*     */   
/*     */   public static final int KEY_7 = 55;
/*     */   
/*     */   public static final int KEY_8 = 56;
/*     */   
/*     */   public static final int KEY_9 = 57;
/*     */   
/*     */   public static final int KEY_A = 65;
/*     */   
/*     */   public static final int KEY_B = 66;
/*     */   
/*     */   public static final int KEY_C = 67;
/*     */   
/*     */   public static final int KEY_D = 68;
/*     */   
/*     */   public static final int KEY_E = 69;
/*     */   
/*     */   public static final int KEY_F = 70;
/*     */   
/*     */   public static final int KEY_G = 71;
/*     */   
/*     */   public static final int KEY_H = 72;
/*     */   
/*     */   public static final int KEY_I = 73;
/*     */   
/*     */   public static final int KEY_J = 74;
/*     */   
/*     */   public static final int KEY_K = 75;
/*     */   
/*     */   public static final int KEY_L = 76;
/*     */   
/*     */   public static final int KEY_M = 77;
/*     */   
/*     */   public static final int KEY_N = 78;
/*     */   
/*     */   public static final int KEY_O = 79;
/*     */   
/*     */   public static final int KEY_P = 80;
/*     */   
/*     */   public static final int KEY_Q = 81;
/*     */   
/*     */   public static final int KEY_R = 82;
/*     */   
/*     */   public static final int KEY_S = 83;
/*     */   
/*     */   public static final int KEY_T = 84;
/*     */   
/*     */   public static final int KEY_U = 85;
/*     */   
/*     */   public static final int KEY_V = 86;
/*     */   
/*     */   public static final int KEY_W = 87;
/*     */   
/*     */   public static final int KEY_X = 88;
/*     */   
/*     */   public static final int KEY_Y = 89;
/*     */   
/*     */   public static final int KEY_Z = 90;
/*     */   
/*     */   public static final int KEY_F1 = 290;
/*     */   
/*     */   public static final int KEY_F2 = 291;
/*     */   
/*     */   public static final int KEY_F3 = 292;
/*     */   
/*     */   public static final int KEY_F4 = 293;
/*     */   
/*     */   public static final int KEY_F5 = 294;
/*     */   
/*     */   public static final int KEY_F6 = 295;
/*     */   
/*     */   public static final int KEY_F7 = 296;
/*     */   
/*     */   public static final int KEY_F8 = 297;
/*     */   
/*     */   public static final int KEY_F9 = 298;
/*     */   
/*     */   public static final int KEY_F10 = 299;
/*     */   
/*     */   public static final int KEY_F11 = 300;
/*     */   
/*     */   public static final int KEY_F12 = 301;
/*     */   
/*     */   public static final int KEY_F13 = 302;
/*     */   
/*     */   public static final int KEY_F14 = 303;
/*     */   
/*     */   public static final int KEY_F15 = 304;
/*     */   
/*     */   public static final int KEY_F16 = 305;
/*     */   
/*     */   public static final int KEY_F17 = 306;
/*     */   
/*     */   public static final int KEY_F18 = 307;
/*     */   
/*     */   public static final int KEY_F19 = 308;
/*     */   
/*     */   public static final int KEY_F20 = 309;
/*     */   
/*     */   public static final int KEY_F21 = 310;
/*     */   
/*     */   public static final int KEY_F22 = 311;
/*     */   
/*     */   public static final int KEY_F23 = 312;
/*     */   
/*     */   public static final int KEY_F24 = 313;
/*     */   
/*     */   public static final int KEY_F25 = 314;
/*     */   
/*     */   public static final int KEY_NUMLOCK = 282;
/*     */   
/*     */   public static final int KEY_NUMPAD0 = 320;
/*     */   
/*     */   public static final int KEY_NUMPAD1 = 321;
/*     */   
/*     */   public static final int KEY_NUMPAD2 = 322;
/*     */   
/*     */   public static final int KEY_NUMPAD3 = 323;
/*     */   
/*     */   public static final int KEY_NUMPAD4 = 324;
/*     */   
/*     */   public static final int KEY_NUMPAD5 = 325;
/*     */   
/*     */   public static final int KEY_NUMPAD6 = 326;
/*     */   
/*     */   public static final int KEY_NUMPAD7 = 327;
/*     */   
/*     */   public static final int KEY_NUMPAD8 = 328;
/*     */   
/*     */   public static final int KEY_NUMPAD9 = 329;
/*     */   
/*     */   public static final int KEY_NUMPADCOMMA = 330;
/*     */   
/*     */   public static final int KEY_NUMPADENTER = 335;
/*     */   
/*     */   public static final int KEY_NUMPADEQUALS = 336;
/*     */   
/*     */   public static final int KEY_DOWN = 264;
/*     */   
/*     */   public static final int KEY_LEFT = 263;
/*     */   
/*     */   public static final int KEY_RIGHT = 262;
/*     */   
/*     */   public static final int KEY_UP = 265;
/*     */   
/*     */   public static final int KEY_ADD = 334;
/*     */   
/*     */   public static final int KEY_APOSTROPHE = 39;
/*     */   
/*     */   public static final int KEY_BACKSLASH = 92;
/*     */   
/*     */   public static final int KEY_COMMA = 44;
/*     */   
/*     */   public static final int KEY_EQUALS = 61;
/*     */   
/*     */   public static final int KEY_GRAVE = 96;
/*     */   
/*     */   public static final int KEY_LBRACKET = 91;
/*     */   
/*     */   public static final int KEY_MINUS = 45;
/*     */   
/*     */   public static final int KEY_MULTIPLY = 332;
/*     */   
/*     */   public static final int KEY_PERIOD = 46;
/*     */   
/*     */   public static final int KEY_RBRACKET = 93;
/*     */   
/*     */   public static final int KEY_SEMICOLON = 59;
/*     */   
/*     */   public static final int KEY_SLASH = 47;
/*     */   
/*     */   public static final int KEY_SPACE = 32;
/*     */   
/*     */   public static final int KEY_TAB = 258;
/*     */   
/*     */   public static final int KEY_LALT = 342;
/*     */   
/*     */   public static final int KEY_LCONTROL = 341;
/*     */   
/*     */   public static final int KEY_LSHIFT = 340;
/*     */   
/*     */   public static final int KEY_LSUPER = 343;
/*     */   
/*     */   public static final int KEY_RALT = 346;
/*     */   
/*     */   public static final int KEY_RCONTROL = 345;
/*     */   
/*     */   public static final int KEY_RSHIFT = 344;
/*     */   
/*     */   public static final int KEY_RSUPER = 347;
/*     */   
/*     */   public static final int KEY_RETURN = 257;
/*     */   
/*     */   public static final int KEY_ESCAPE = 256;
/*     */   
/*     */   public static final int KEY_BACKSPACE = 259;
/*     */   
/*     */   public static final int KEY_DELETE = 261;
/*     */   
/*     */   public static final int KEY_END = 269;
/*     */   
/*     */   public static final int KEY_HOME = 268;
/*     */   
/*     */   public static final int KEY_INSERT = 260;
/*     */   
/*     */   public static final int KEY_PAGEDOWN = 267;
/*     */   
/*     */   public static final int KEY_PAGEUP = 266;
/*     */   
/*     */   public static final int KEY_CAPSLOCK = 280;
/*     */   
/*     */   public static final int KEY_PAUSE = 284;
/*     */   
/*     */   public static final int KEY_SCROLLLOCK = 281;
/*     */   
/*     */   public static final int KEY_PRINTSCREEN = 283;
/*     */   
/*     */   public static final int PRESS = 1;
/*     */   
/*     */   public static final int RELEASE = 0;
/*     */   
/*     */   public static final int REPEAT = 2;
/*     */   
/*     */   public static final int MOUSE_BUTTON_LEFT = 0;
/*     */   
/*     */   public static final int MOUSE_BUTTON_RIGHT = 1;
/*     */   
/*     */   public static final int MOUSE_BUTTON_MIDDLE = 2;
/*     */   
/*     */   public static final int MOUSE_BUTTON_4 = 3;
/*     */   
/*     */   public static final int MOUSE_BUTTON_5 = 4;
/*     */   
/*     */   public static final int MOUSE_BUTTON_6 = 5;
/*     */   
/*     */   public static final int MOUSE_BUTTON_7 = 6;
/*     */   
/*     */   public static final int MOUSE_BUTTON_8 = 7;
/*     */   
/*     */   public static final int MOD_SHIFT = 1;
/*     */   
/*     */   public static final int MOD_CONTROL = 2;
/*     */   
/*     */   public static final int MOD_ALT = 4;
/*     */   
/*     */   public static final int MOD_SUPER = 8;
/*     */   
/*     */   public static final int MOD_CAPS_LOCK = 16;
/*     */   
/*     */   public static final int MOD_NUM_LOCK = 32;
/*     */   
/*     */   public static final int CURSOR = 208897;
/*     */   
/*     */   public static final int CURSOR_DISABLED = 212995;
/*     */   
/*     */   public static final int CURSOR_NORMAL = 212993;
/*     */   
/* 182 */   public static final Key UNKNOWN = Type.KEYSYM.getOrCreate(-1);
/*     */   
/*     */   public enum Type {
/*     */     MOUSE, SCANCODE, KEYSYM;
/*     */     
/*     */     private final BiFunction<Integer, String, Component> displayTextSupplier;
/*     */     
/*     */     private final String defaultPrefix;
/*     */     
/*     */     static {
/* 185 */       KEYSYM = new Type("KEYSYM", 0, "key.keyboard", (value, name) -> {
/*     */             if ("key.keyboard.unknown".equals(name))
/*     */               return (Component)Component.translatable(name); 
/*     */             String systemName = GLFW.glfwGetKeyName(value.intValue(), -1);
/*     */             return (systemName != null) ? (Component)Component.literal(systemName.toUpperCase(Locale.ROOT)) : (Component)Component.translatable(name);
/*     */           });
/* 192 */       SCANCODE = new Type("SCANCODE", 1, "scancode", (value, name) -> {
/*     */             String systemName = GLFW.glfwGetKeyName(-1, value.intValue());
/*     */             return (systemName != null) ? (Component)Component.literal(systemName) : (Component)Component.translatable(name);
/*     */           });
/* 196 */       MOUSE = new Type("MOUSE", 2, "key.mouse", (value, name) -> Language.getInstance().has(name) ? (Component)Component.translatable(name) : (Component)Component.translatable("key.mouse", new Object[] { Integer.valueOf(value.intValue() + 1) }));
/*     */     }
/*     */     
/*     */     private static void addKey(Type type, String name, int value) {
/* 199 */       InputConstants.Key key = new InputConstants.Key(name, type, value);
/* 200 */       type.map.put(value, key);
/*     */     }
/*     */     
/*     */     static {
/* 206 */       addKey(KEYSYM, "key.keyboard.unknown", -1);
/* 208 */       addKey(MOUSE, "key.mouse.left", 0);
/* 209 */       addKey(MOUSE, "key.mouse.right", 1);
/* 210 */       addKey(MOUSE, "key.mouse.middle", 2);
/* 211 */       addKey(MOUSE, "key.mouse.4", 3);
/* 212 */       addKey(MOUSE, "key.mouse.5", 4);
/* 213 */       addKey(MOUSE, "key.mouse.6", 5);
/* 214 */       addKey(MOUSE, "key.mouse.7", 6);
/* 215 */       addKey(MOUSE, "key.mouse.8", 7);
/* 217 */       addKey(KEYSYM, "key.keyboard.0", 48);
/* 218 */       addKey(KEYSYM, "key.keyboard.1", 49);
/* 219 */       addKey(KEYSYM, "key.keyboard.2", 50);
/* 220 */       addKey(KEYSYM, "key.keyboard.3", 51);
/* 221 */       addKey(KEYSYM, "key.keyboard.4", 52);
/* 222 */       addKey(KEYSYM, "key.keyboard.5", 53);
/* 223 */       addKey(KEYSYM, "key.keyboard.6", 54);
/* 224 */       addKey(KEYSYM, "key.keyboard.7", 55);
/* 225 */       addKey(KEYSYM, "key.keyboard.8", 56);
/* 226 */       addKey(KEYSYM, "key.keyboard.9", 57);
/* 228 */       addKey(KEYSYM, "key.keyboard.a", 65);
/* 229 */       addKey(KEYSYM, "key.keyboard.b", 66);
/* 230 */       addKey(KEYSYM, "key.keyboard.c", 67);
/* 231 */       addKey(KEYSYM, "key.keyboard.d", 68);
/* 232 */       addKey(KEYSYM, "key.keyboard.e", 69);
/* 233 */       addKey(KEYSYM, "key.keyboard.f", 70);
/* 234 */       addKey(KEYSYM, "key.keyboard.g", 71);
/* 235 */       addKey(KEYSYM, "key.keyboard.h", 72);
/* 236 */       addKey(KEYSYM, "key.keyboard.i", 73);
/* 237 */       addKey(KEYSYM, "key.keyboard.j", 74);
/* 238 */       addKey(KEYSYM, "key.keyboard.k", 75);
/* 239 */       addKey(KEYSYM, "key.keyboard.l", 76);
/* 240 */       addKey(KEYSYM, "key.keyboard.m", 77);
/* 241 */       addKey(KEYSYM, "key.keyboard.n", 78);
/* 242 */       addKey(KEYSYM, "key.keyboard.o", 79);
/* 243 */       addKey(KEYSYM, "key.keyboard.p", 80);
/* 244 */       addKey(KEYSYM, "key.keyboard.q", 81);
/* 245 */       addKey(KEYSYM, "key.keyboard.r", 82);
/* 246 */       addKey(KEYSYM, "key.keyboard.s", 83);
/* 247 */       addKey(KEYSYM, "key.keyboard.t", 84);
/* 248 */       addKey(KEYSYM, "key.keyboard.u", 85);
/* 249 */       addKey(KEYSYM, "key.keyboard.v", 86);
/* 250 */       addKey(KEYSYM, "key.keyboard.w", 87);
/* 251 */       addKey(KEYSYM, "key.keyboard.x", 88);
/* 252 */       addKey(KEYSYM, "key.keyboard.y", 89);
/* 253 */       addKey(KEYSYM, "key.keyboard.z", 90);
/* 255 */       addKey(KEYSYM, "key.keyboard.f1", 290);
/* 256 */       addKey(KEYSYM, "key.keyboard.f2", 291);
/* 257 */       addKey(KEYSYM, "key.keyboard.f3", 292);
/* 258 */       addKey(KEYSYM, "key.keyboard.f4", 293);
/* 259 */       addKey(KEYSYM, "key.keyboard.f5", 294);
/* 260 */       addKey(KEYSYM, "key.keyboard.f6", 295);
/* 261 */       addKey(KEYSYM, "key.keyboard.f7", 296);
/* 262 */       addKey(KEYSYM, "key.keyboard.f8", 297);
/* 263 */       addKey(KEYSYM, "key.keyboard.f9", 298);
/* 264 */       addKey(KEYSYM, "key.keyboard.f10", 299);
/* 265 */       addKey(KEYSYM, "key.keyboard.f11", 300);
/* 266 */       addKey(KEYSYM, "key.keyboard.f12", 301);
/* 267 */       addKey(KEYSYM, "key.keyboard.f13", 302);
/* 268 */       addKey(KEYSYM, "key.keyboard.f14", 303);
/* 269 */       addKey(KEYSYM, "key.keyboard.f15", 304);
/* 270 */       addKey(KEYSYM, "key.keyboard.f16", 305);
/* 271 */       addKey(KEYSYM, "key.keyboard.f17", 306);
/* 272 */       addKey(KEYSYM, "key.keyboard.f18", 307);
/* 273 */       addKey(KEYSYM, "key.keyboard.f19", 308);
/* 274 */       addKey(KEYSYM, "key.keyboard.f20", 309);
/* 275 */       addKey(KEYSYM, "key.keyboard.f21", 310);
/* 276 */       addKey(KEYSYM, "key.keyboard.f22", 311);
/* 277 */       addKey(KEYSYM, "key.keyboard.f23", 312);
/* 278 */       addKey(KEYSYM, "key.keyboard.f24", 313);
/* 279 */       addKey(KEYSYM, "key.keyboard.f25", 314);
/* 281 */       addKey(KEYSYM, "key.keyboard.num.lock", 282);
/* 282 */       addKey(KEYSYM, "key.keyboard.keypad.0", 320);
/* 283 */       addKey(KEYSYM, "key.keyboard.keypad.1", 321);
/* 284 */       addKey(KEYSYM, "key.keyboard.keypad.2", 322);
/* 285 */       addKey(KEYSYM, "key.keyboard.keypad.3", 323);
/* 286 */       addKey(KEYSYM, "key.keyboard.keypad.4", 324);
/* 287 */       addKey(KEYSYM, "key.keyboard.keypad.5", 325);
/* 288 */       addKey(KEYSYM, "key.keyboard.keypad.6", 326);
/* 289 */       addKey(KEYSYM, "key.keyboard.keypad.7", 327);
/* 290 */       addKey(KEYSYM, "key.keyboard.keypad.8", 328);
/* 291 */       addKey(KEYSYM, "key.keyboard.keypad.9", 329);
/* 292 */       addKey(KEYSYM, "key.keyboard.keypad.add", 334);
/* 293 */       addKey(KEYSYM, "key.keyboard.keypad.decimal", 330);
/* 294 */       addKey(KEYSYM, "key.keyboard.keypad.enter", 335);
/* 295 */       addKey(KEYSYM, "key.keyboard.keypad.equal", 336);
/* 296 */       addKey(KEYSYM, "key.keyboard.keypad.multiply", 332);
/* 297 */       addKey(KEYSYM, "key.keyboard.keypad.divide", 331);
/* 298 */       addKey(KEYSYM, "key.keyboard.keypad.subtract", 333);
/* 300 */       addKey(KEYSYM, "key.keyboard.down", 264);
/* 301 */       addKey(KEYSYM, "key.keyboard.left", 263);
/* 302 */       addKey(KEYSYM, "key.keyboard.right", 262);
/* 303 */       addKey(KEYSYM, "key.keyboard.up", 265);
/* 305 */       addKey(KEYSYM, "key.keyboard.apostrophe", 39);
/* 306 */       addKey(KEYSYM, "key.keyboard.backslash", 92);
/* 307 */       addKey(KEYSYM, "key.keyboard.comma", 44);
/* 308 */       addKey(KEYSYM, "key.keyboard.equal", 61);
/* 309 */       addKey(KEYSYM, "key.keyboard.grave.accent", 96);
/* 310 */       addKey(KEYSYM, "key.keyboard.left.bracket", 91);
/* 311 */       addKey(KEYSYM, "key.keyboard.minus", 45);
/* 312 */       addKey(KEYSYM, "key.keyboard.period", 46);
/* 313 */       addKey(KEYSYM, "key.keyboard.right.bracket", 93);
/* 314 */       addKey(KEYSYM, "key.keyboard.semicolon", 59);
/* 315 */       addKey(KEYSYM, "key.keyboard.slash", 47);
/* 316 */       addKey(KEYSYM, "key.keyboard.space", 32);
/* 317 */       addKey(KEYSYM, "key.keyboard.tab", 258);
/* 319 */       addKey(KEYSYM, "key.keyboard.left.alt", 342);
/* 320 */       addKey(KEYSYM, "key.keyboard.left.control", 341);
/* 321 */       addKey(KEYSYM, "key.keyboard.left.shift", 340);
/* 322 */       addKey(KEYSYM, "key.keyboard.left.win", 343);
/* 323 */       addKey(KEYSYM, "key.keyboard.right.alt", 346);
/* 324 */       addKey(KEYSYM, "key.keyboard.right.control", 345);
/* 325 */       addKey(KEYSYM, "key.keyboard.right.shift", 344);
/* 326 */       addKey(KEYSYM, "key.keyboard.right.win", 347);
/* 328 */       addKey(KEYSYM, "key.keyboard.enter", 257);
/* 329 */       addKey(KEYSYM, "key.keyboard.escape", 256);
/* 331 */       addKey(KEYSYM, "key.keyboard.backspace", 259);
/* 332 */       addKey(KEYSYM, "key.keyboard.delete", 261);
/* 333 */       addKey(KEYSYM, "key.keyboard.end", 269);
/* 334 */       addKey(KEYSYM, "key.keyboard.home", 268);
/* 335 */       addKey(KEYSYM, "key.keyboard.insert", 260);
/* 336 */       addKey(KEYSYM, "key.keyboard.page.down", 267);
/* 337 */       addKey(KEYSYM, "key.keyboard.page.up", 266);
/* 339 */       addKey(KEYSYM, "key.keyboard.caps.lock", 280);
/* 340 */       addKey(KEYSYM, "key.keyboard.pause", 284);
/* 341 */       addKey(KEYSYM, "key.keyboard.scroll.lock", 281);
/* 343 */       addKey(KEYSYM, "key.keyboard.menu", 348);
/* 344 */       addKey(KEYSYM, "key.keyboard.print.screen", 283);
/* 345 */       addKey(KEYSYM, "key.keyboard.world.1", 161);
/* 346 */       addKey(KEYSYM, "key.keyboard.world.2", 162);
/*     */     }
/*     */     
/* 349 */     private final Int2ObjectMap<InputConstants.Key> map = (Int2ObjectMap<InputConstants.Key>)new Int2ObjectOpenHashMap();
/*     */     
/*     */     private static final String KEY_KEYBOARD_UNKNOWN = "key.keyboard.unknown";
/*     */     
/*     */     Type(String defaultPrefix, BiFunction<Integer, String, Component> displayTextSupplier) {
/* 356 */       this.defaultPrefix = defaultPrefix;
/* 357 */       this.displayTextSupplier = displayTextSupplier;
/*     */     }
/*     */     
/*     */     public InputConstants.Key getOrCreate(int value) {
/* 361 */       return (InputConstants.Key)this.map.computeIfAbsent(value, intValue -> {
/*     */             int humanReadableValue = intValue;
/*     */             if (this == MOUSE)
/*     */               humanReadableValue++; 
/*     */             String name = this.defaultPrefix + "." + this.defaultPrefix;
/*     */             return new InputConstants.Key(name, this, intValue);
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Key {
/*     */     private final String name;
/*     */     
/*     */     private final InputConstants.Type type;
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private final Supplier<Component> displayName;
/*     */     
/* 379 */     private static final Map<String, Key> NAME_MAP = Maps.newHashMap();
/*     */     
/*     */     private Key(String name, InputConstants.Type type, int value) {
/* 382 */       this.name = name;
/* 383 */       this.type = type;
/* 384 */       this.value = value;
/* 386 */       this.displayName = (Supplier<Component>)Suppliers.memoize(() -> (Component)type.displayTextSupplier.apply(Integer.valueOf(value), name));
/* 387 */       NAME_MAP.put(name, this);
/*     */     }
/*     */     
/*     */     public InputConstants.Type getType() {
/* 391 */       return this.type;
/*     */     }
/*     */     
/*     */     public int getValue() {
/* 395 */       return this.value;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 399 */       return this.name;
/*     */     }
/*     */     
/*     */     public Component getDisplayName() {
/* 403 */       return this.displayName.get();
/*     */     }
/*     */     
/*     */     public OptionalInt getNumericKeyValue() {
/* 407 */       if (this.value >= 48 && this.value <= 57)
/* 408 */         return OptionalInt.of(this.value - 48); 
/* 410 */       if (this.value >= 320 && this.value <= 329)
/* 411 */         return OptionalInt.of(this.value - 320); 
/* 413 */       return OptionalInt.empty();
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 418 */       if (this == o)
/* 419 */         return true; 
/* 421 */       if (o == null || getClass() != o.getClass())
/* 422 */         return false; 
/* 424 */       Key key = (Key)o;
/* 425 */       return (this.value == key.value && this.type == key.type);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 430 */       return Objects.hash(new Object[] { this.type, Integer.valueOf(this.value) });
/*     */     }
/*     */     
/*     */     public String toString() {
/* 435 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static Key getKey(KeyEvent event) {
/* 440 */     if (event.key() == -1)
/* 441 */       return Type.SCANCODE.getOrCreate(event.scancode()); 
/* 443 */     return Type.KEYSYM.getOrCreate(event.key());
/*     */   }
/*     */   
/*     */   public static Key getKey(String name) {
/* 447 */     if (Key.NAME_MAP.containsKey(name))
/* 448 */       return Key.NAME_MAP.get(name); 
/* 451 */     for (Type type : Type.values()) {
/* 452 */       if (name.startsWith(type.defaultPrefix)) {
/* 453 */         String humanReadableValue = name.substring(type.defaultPrefix.length() + 1);
/* 454 */         int intValue = Integer.parseInt(humanReadableValue);
/* 455 */         if (type == Type.MOUSE)
/* 456 */           intValue--; 
/* 458 */         return type.getOrCreate(intValue);
/*     */       } 
/*     */     } 
/* 461 */     throw new IllegalArgumentException("Unknown key name: " + name);
/*     */   }
/*     */   
/*     */   public static boolean isKeyDown(Window window, int key) {
/* 465 */     return (GLFW.glfwGetKey(window.handle(), key) == 1);
/*     */   }
/*     */   
/*     */   public static void setupKeyboardCallbacks(Window window, GLFWKeyCallbackI keyPressCallback, GLFWCharCallbackI charTypedCallback, GLFWPreeditCallbackI preeditCallback, GLFWIMEStatusCallbackI imeStatusCallback) {
/* 469 */     GLFW.glfwSetKeyCallback(window.handle(), keyPressCallback);
/* 472 */     GLFW.glfwSetCharCallback(window.handle(), charTypedCallback);
/* 473 */     GLFW.glfwSetPreeditCallback(window.handle(), preeditCallback);
/* 474 */     GLFW.glfwSetIMEStatusCallback(window.handle(), imeStatusCallback);
/*     */   }
/*     */   
/*     */   public static void setupMouseCallbacks(Window window, GLFWCursorPosCallbackI onMoveCallback, GLFWMouseButtonCallbackI onPressCallback, GLFWScrollCallbackI onScrollCallback, GLFWDropCallbackI onDropCallback) {
/* 478 */     GLFW.glfwSetCursorPosCallback(window.handle(), onMoveCallback);
/* 479 */     GLFW.glfwSetMouseButtonCallback(window.handle(), onPressCallback);
/* 480 */     GLFW.glfwSetScrollCallback(window.handle(), onScrollCallback);
/* 481 */     GLFW.glfwSetDropCallback(window.handle(), onDropCallback);
/*     */   }
/*     */   
/*     */   public static void grabOrReleaseMouse(Window window, int cursorMode, double xpos, double ypos) {
/* 485 */     GLFW.glfwSetCursorPos(window.handle(), xpos, ypos);
/* 486 */     GLFW.glfwSetInputMode(window.handle(), 208897, cursorMode);
/*     */   }
/*     */   
/*     */   public static boolean isRawMouseInputSupported() {
/* 490 */     return GLFW.glfwRawMouseMotionSupported();
/*     */   }
/*     */   
/*     */   public static void updateRawMouseInput(Window window, boolean value) {
/* 494 */     if (isRawMouseInputSupported())
/* 495 */       GLFW.glfwSetInputMode(window.handle(), 208901, GLX.glfwBool(value)); 
/*     */   }
/*     */   
/*     */   @Retention(RetentionPolicy.CLASS)
/*     */   @Target({ElementType.TYPE_USE})
/*     */   public static @interface Value {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\InputConstants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */