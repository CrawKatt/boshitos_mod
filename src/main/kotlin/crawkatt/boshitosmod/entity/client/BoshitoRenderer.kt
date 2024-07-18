package crawkatt.boshitosmod.entity.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.entity.custom.BoshitoEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.renderer.GeoEntityRenderer
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer

class BoshitoRenderer(pContext: EntityRendererProvider.Context?) :
    GeoEntityRenderer<BoshitoEntity>(pContext, BoshitoModel()) {
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation(BoshitosMod.MOD_ID, "textures/entity/boshito.png")
    }

    // `init` es el equivalente a un constructor en Java
    init {
        this.shadowRadius = 0.6f // Tamaño de la sombra de la Entidad

        this.addRenderLayer(object : BlockAndItemGeoLayer<BoshitoEntity>(
            this,
            { bone: GeoBone, animatable: BoshitoEntity ->
                if (bone.name == "rightArm") {
                    animatable.getItemInHand(InteractionHand.MAIN_HAND)
                } else {
                    null
                }
            },
            { _, _ -> null }
        ) {
            override fun getTransformTypeForStack(
                bone: GeoBone?,
                stack: ItemStack?,
                animatable: BoshitoEntity?
            ): ItemDisplayContext {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
            }

            override fun renderStackForBone(
                poseStack: PoseStack,
                bone: GeoBone?,
                stack: ItemStack?,
                animatable: BoshitoEntity?,
                bufferSource: MultiBufferSource?,
                partialTick: Float,
                packedLight: Int,
                packedOverlay: Int
            ) {
                // Item translation
                poseStack.translate(bone!!.posX, -bone.posY - 0.20F, -bone.posZ - 0.15F)

                poseStack.mulPose(Axis.XP.rotationDegrees(bone.rotX - 40.5F))

                super.renderStackForBone(
                    poseStack,
                    bone,
                    stack,
                    animatable,
                    bufferSource,
                    partialTick,
                    packedLight,
                    packedOverlay
                )
            }
        })
    }

    override fun getTextureLocation(pEntity: BoshitoEntity): ResourceLocation {
        return TEXTURE
    }

    override fun render(
        pEntity: BoshitoEntity, pEntityYaw: Float, pPartialTicks: Float, pMatrixStack: PoseStack,
        pBuffer: MultiBufferSource, pPackedLight: Int
    ) {
        if (pEntity.isBaby) {
            pMatrixStack.scale(0.3f, 0.3f, 0.3f)
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight)
    }
}