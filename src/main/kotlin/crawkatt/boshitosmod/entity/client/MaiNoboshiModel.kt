package crawkatt.boshitosmod.entity.client

import com.google.common.collect.ImmutableList
import com.google.common.collect.Iterables
import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.entity.custom.MaiNoboshiEntity
import net.minecraft.client.model.AnimationUtils
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth

// Dado que el Modelo de Mai es básicamente el modelo de un jugador, se puede heredar de HumanoidModel
// y se puede utilizar el modelo de un jugador como base, ahorrando la creación de un modelo desde cero en BlockBench.
// Esto también hereda las animaciones del jugador y el render del jugador, lo que permite añadir objetos a la mano
// sin Bugs)
class MaiNoboshiModel(modelPart: ModelPart) : HumanoidModel<MaiNoboshiEntity>(modelPart) {
    private val leftSleeve: ModelPart = modelPart.getChild("left_sleeve")
    private val rightSleeve: ModelPart = modelPart.getChild("right_sleeve")
    private val leftPants: ModelPart = modelPart.getChild("left_pants")
    private val rightPants: ModelPart = modelPart.getChild("right_pants")
    private val jacket: ModelPart = modelPart.getChild("jacket")

    companion object {
        private val LAYER = ResourceLocation(BoshitosMod.MOD_ID, "mainoboshi")
        val LAYER_LOCATION = ModelLayerLocation(LAYER, "main")

        fun createBodyLayer(): LayerDefinition {
            val deformation = CubeDeformation(0.0F)
            val meshDefinition = createMesh(CubeDeformation.NONE, 0.0F)
            val partDefinition = meshDefinition.root

            partDefinition.addOrReplaceChild(
                "left_arm", CubeListBuilder.create().texOffs(32, 48)
                    .addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(5.0F, 2.5F, 0.0F)
            )

            partDefinition.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create().texOffs(40, 16)
                    .addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(-5.0F, 2.5F, 0.0F)
            )
            partDefinition.addOrReplaceChild(
                "left_sleeve",
                CubeListBuilder.create().texOffs(48, 48)
                    .addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)),
                PartPose.offset(5.0F, 2.5F, 0.0F)
            )

            partDefinition.addOrReplaceChild(
                "right_sleeve",
                CubeListBuilder.create().texOffs(40, 32)
                    .addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)),
                PartPose.offset(-5.0F, 2.5F, 0.0F)
            )

            partDefinition.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create().texOffs(16, 48)
                    .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(1.9F, 12.0F, 0.0F)
            )

            partDefinition.addOrReplaceChild(
                "left_pants",
                CubeListBuilder.create().texOffs(0, 48)
                    .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)),
                PartPose.offset(1.9F, 12.0F, 0.0F)
            )

            partDefinition.addOrReplaceChild(
                "right_pants",
                CubeListBuilder.create().texOffs(0, 32)
                    .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)),
                PartPose.offset(-1.9F, 12.0F, 0.0F)
            )

            partDefinition.addOrReplaceChild(
                "jacket",
                CubeListBuilder.create().texOffs(16, 32)
                    .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation.extend(0.25F)),
                PartPose.ZERO
            )

            return LayerDefinition.create(meshDefinition, 64, 64)
        }
    }

    override fun bodyParts(): Iterable<ModelPart> {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(leftPants, rightPants, leftSleeve, rightSleeve, jacket))
    }

    override fun setAllVisible(allVisible: Boolean) {
        super.setAllVisible(allVisible)
        leftSleeve.visible = allVisible
        rightSleeve.visible = allVisible
        leftPants.visible = allVisible
        rightPants.visible = allVisible
        jacket.visible = allVisible
    }

    override fun setupAttackAnimation(entity: MaiNoboshiEntity, ageInTicks: Float) {
        val mainHandItem = entity.mainHandItem
        if (entity.isAggressive && !mainHandItem.isEmpty) {
            val attackSwingProgress = Mth.sin(attackTime * Mth.PI)
            val attackAnimationProgress = Mth.sin((1.0f - (1.0f - attackTime) * (1.0f - attackTime)) * Mth.PI)
            rightArm.zRot = 0.0f
            leftArm.zRot = 0.0f
            rightArm.yRot = -(0.1f - attackSwingProgress * 0.6f)
            leftArm.yRot = 0.1f - attackSwingProgress * 0.6f
            rightArm.xRot = -1.5707964f
            leftArm.xRot = -1.5707964f
            rightArm.xRot -= attackSwingProgress * 1.2f - attackAnimationProgress * 0.4f
            leftArm.xRot -= attackSwingProgress * 1.2f - attackAnimationProgress * 0.4f
            AnimationUtils.bobArms(rightArm, leftArm, ageInTicks)
        }

        super.setupAttackAnimation(entity, ageInTicks)
        leftSleeve.copyFrom(leftArm)
        rightSleeve.copyFrom(rightArm)
    }

    override fun setupAnim(
        entity: MaiNoboshiEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)

        // Copiar las rotaciones de los brazos y piernas a las mangas y pantalones
        leftPants.copyFrom(leftLeg)
        rightPants.copyFrom(rightLeg)
        leftSleeve.copyFrom(leftArm)
        rightSleeve.copyFrom(rightArm)
        jacket.copyFrom(body)
    }
}