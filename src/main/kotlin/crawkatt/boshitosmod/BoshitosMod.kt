package crawkatt.boshitosmod

import crawkatt.boshitosmod.block.ModBlocks
import crawkatt.boshitosmod.entity.ModEntities
import crawkatt.boshitosmod.entity.client.BoshitoRenderer
import crawkatt.boshitosmod.item.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.item.CreativeModeTabs
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(BoshitosMod.MOD_ID)
object BoshitosMod {
    const val MOD_ID = "boshitosmod"

    // the logger for our mod
    private val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    // Este es el constructor de la clase. Es el equivalente a `BoshitosMod()`
    init {
        LOGGER.log(Level.INFO, "Hello world!")

        // MOD_BUS es el equivalente a IEventBus
        // Registra los bloques
        ModBlocks.BLOCKS.register(MOD_BUS)

        // Registra los objetos
        ModItems.ITEMS.register(MOD_BUS)

        // Registra las entidades
        ModEntities.ENTITY_TYPES.register(MOD_BUS)

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
                MOD_BUS.addListener(::addCreative)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            })

        println(obj)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
        EntityRenderers.register(ModEntities.BOSHITO.get(), ::BoshitoRenderer)
    }

    private fun addCreative(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RAW_BROTENITA)
        }
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }
}