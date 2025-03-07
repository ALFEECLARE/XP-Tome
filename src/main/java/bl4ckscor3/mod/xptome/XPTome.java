package bl4ckscor3.mod.xptome;

import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(XPTome.MODID)
@EventBusSubscriber(modid = XPTome.MODID)
public class XPTome {
	public static final String MODID = "xpbook";
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	/** @deprecated This is kept for legacy reasons. Use the field below this one. */
	@Deprecated
	public static final RegistryObject<Item> XP_BOOK = ITEMS.register("xp_book", () -> new OldXPTomeItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> XP_TOME = ITEMS.register("xp_tome", () -> new XPTomeItem(new Item.Properties().stacksTo(1)));

	public XPTome() {
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ITEMS.register(modBus);
		modBus.addListener(this::onCreativeModeTabBuildContents);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Configuration.CONFIG_SPEC, "xptome-server.toml");
	}

	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		//prevention for a crash that should theoretically not happen, but apparently does
		XP_BOOK.ifPresent(xpBook -> {
			if (event.getLeft().is(xpBook) || event.getRight().is(xpBook))
				event.setCanceled(true);
		});
		XP_TOME.ifPresent(xpTome -> {
			if (event.getLeft().is(xpTome) || event.getRight().is(xpTome))
				event.setCanceled(true);
		});
	}

	public void onCreativeModeTabBuildContents(CreativeModeTabEvent.BuildContents event) {
		if (event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
			event.getEntries().putAfter(new ItemStack(Blocks.ENCHANTING_TABLE), new ItemStack(XP_TOME.get()), TabVisibility.PARENT_AND_SEARCH_TABS);
		if (event.getTab() == CreativeModeTabs.INGREDIENTS)
			event.getEntries().putAfter(new ItemStack(Items.BOOK), new ItemStack(XP_TOME.get()), TabVisibility.PARENT_AND_SEARCH_TABS);
	}
}
